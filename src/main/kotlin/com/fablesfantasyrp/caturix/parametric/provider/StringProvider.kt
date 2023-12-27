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

import com.fablesfantasyrp.caturix.argument.*
import com.fablesfantasyrp.caturix.parametric.*
import com.fablesfantasyrp.caturix.parametric.annotation.Validate

internal open class StringProvider : Provider<String> {
	override val isProvided: Boolean
		get() = false

	@Throws(ArgumentException::class)
	override fun get(arguments: CommandArgs, modifiers: List<Annotation>): String {
		val v = arguments.next()
		validate(v, modifiers)
		return v
	}

	override fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String> {
		return emptyList()
	}

	companion object {
		val INSTANCE: StringProvider = StringProvider()

		/**
		 * Validate a string value using relevant modifiers.
		 *
		 * @param string the string
		 * @param modifiers the list of modifiers to scan
		 * @throws ArgumentParseException on a validation error
		 */
		@JvmStatic
		@Throws(ArgumentParseException::class)
		protected fun validate(string: String?, modifiers: List<Annotation?>) {
			if (string == null) {
				return
			}

			for (modifier in modifiers) {
				if (modifier is Validate && modifier.regex.isNotEmpty() && !string.matches(modifier.regex.toRegex())) {
					throw ArgumentParseException(
						String.format(
							"The given text doesn't match the right format (technically speaking, the 'format' is %s)",
							modifier.regex
						)
					)
				}
			}
		}
	}
}
