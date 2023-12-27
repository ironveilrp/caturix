/*
 * Caturix, a command processing library
 * Copyright (C) Intake team and contributors
 * Copyright (C) Caturix team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.fablesfantasyrp.caturix.parametric.provider

import com.fablesfantasyrp.caturix.argument.ArgumentException
import com.fablesfantasyrp.caturix.argument.ArgumentParseException
import com.fablesfantasyrp.caturix.argument.CommandArgs
import com.google.common.collect.Lists
import com.fablesfantasyrp.caturix.argument.Namespace
import com.fablesfantasyrp.caturix.parametric.Provider
import com.fablesfantasyrp.caturix.parametric.ProvisionException
import java.util.*
import java.util.regex.Pattern

/**
 * Searches an enum for a near-matching value.
 *
 *
 * When comparing for a match, both the search and test entry have
 * non-alphanumeric characters stripped.
 *
 * @param <T> The type of the enum
</T> */
class EnumProvider<T : Enum<T>>(private val enumClass: Class<T>) : Provider<T> {
	override val isProvided: Boolean
		get() = false

	@Throws(ArgumentException::class, ProvisionException::class)
	override fun get(arguments: CommandArgs, modifiers: List<Annotation>): T {
		val name: String = arguments.next()
		val test = simplify(name)

		for (entry in enumClass.enumConstants) {
			if (simplify(entry!!.name).equals(test, ignoreCase = true)) {
				return entry
			}
		}

		throw ArgumentParseException("No matching value found in the '" + enumClass.simpleName + "' list.")
	}

	override fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String> {
		val suggestions: MutableList<String> = Lists.newArrayList()
		val test = simplify(prefix)

		for (entry in enumClass.enumConstants) {
			val name = simplify(entry!!.name)
			if (name.startsWith(test)) {
				suggestions.add(entry.name.lowercase(Locale.getDefault()))
			}
		}

		return suggestions
	}

	companion object {
		private val NON_ALPHANUMERIC: Pattern = Pattern.compile("[^A-Za-z0-9]")

		private fun simplify(t: String): String {
			return NON_ALPHANUMERIC.matcher(t.lowercase(Locale.getDefault())).replaceAll("")
		}
	}
}
