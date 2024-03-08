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
package com.ironveilrp.caturix.util

import com.ironveilrp.caturix.CommandMapping
import java.util.regex.Pattern

/**
 * Compares the primary aliases of two [CommandMapping] using
 * [String.compareTo].
 */
class PrimaryAliasComparator
/**
 * Create a new instance.
 *
 * @param removalPattern A regex to remove unwanted characters from the compared aliases
 */(private val removalPattern: Pattern?) : Comparator<CommandMapping> {
	private fun clean(alias: String?): String? {
		if (removalPattern != null) {
			return removalPattern.matcher(alias).replaceAll("")
		}
		return alias
	}

	override fun compare(o1: CommandMapping, o2: CommandMapping): Int {
		return clean(o1.primaryAlias)!!.compareTo(clean(o2.primaryAlias)!!)
	}

	companion object {
		/**
		 * An instance of this class.
		 */
		val INSTANCE: PrimaryAliasComparator = PrimaryAliasComparator(null)
	}
}
