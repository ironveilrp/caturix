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
package com.fablesfantasyrp.caturix

import com.fablesfantasyrp.caturix.argument.Arguments
import com.fablesfantasyrp.caturix.argument.CommandArgs

/**
 * Defines the type of parameter, whether it is positional, a flag, optional,
 * or required.
 */
abstract class OptionType private constructor() {
	abstract val flag: Char?

	/**
	 * Return whether the flag is a value flag.
	 *
	 * @return true if the flag is a value flag
	 * @see .getFlag
	 */
	abstract val isValueFlag: Boolean

	/**
	 * Get whether this parameter is optional.
	 *
	 * @return true if the parameter does not have to be specified
	 */
	abstract val isOptional: Boolean

	/**
	 * Create a new list of command arguments for the given arguments that
	 * is appropriate for this type of parameter.
	 *
	 *
	 * For example, if the type of parameter is a value flag,
	 * then the returned arguments object will only have the value flag's
	 * value as the argument.
	 *
	 * @param arguments The list of arguments
	 * @return The new list of arguments
	 */
	abstract fun transform(arguments: CommandArgs): CommandArgs

	private class RequiredPositional : OptionType() {
		override val flag: Char? = null
		override val isValueFlag: Boolean = false
		override val isOptional: Boolean = false

		override fun transform(arguments: CommandArgs): CommandArgs {
			return arguments
		}
	}

	private class OptionalPositional : OptionType() {
		override val flag: Char? = null
		override val isValueFlag: Boolean = false
		override val isOptional: Boolean = true

		override fun transform(arguments: CommandArgs): CommandArgs {
			return arguments
		}
	}

	private class BooleanFlag(override val flag: Char) : OptionType() {
		override val isValueFlag: Boolean = false
		override val isOptional: Boolean = true

		override fun transform(arguments: CommandArgs): CommandArgs {
			val v = if (arguments.flags.containsKey(flag)) "true" else "false"
			return Arguments.copyOf(listOf(v), arguments.flags, arguments.namespace)
		}
	}

	private class ValueFlag(override val flag: Char) : OptionType() {
		override val isValueFlag: Boolean = true
		override val isOptional: Boolean = true

		override fun transform(arguments: CommandArgs): CommandArgs {
			val args = arguments.flags[flag]?.let { listOf(it) } ?: emptyList()

			return Arguments.copyOf(args, arguments.flags, arguments.namespace)
		}
	}

	companion object {
		private val REQUIRED_PARAMETER = RequiredPositional()
		private val OPTIONAL_PARAMETER = OptionalPositional()

		fun positional(): OptionType = REQUIRED_PARAMETER
		fun optionalPositional(): OptionType = OPTIONAL_PARAMETER

		/**
		 * Get the non-value boolean flag type of parameter.
		 *
		 * @param flag The flag character
		 * @return An option type
		 */
		fun flag(flag: Char): OptionType = BooleanFlag(flag)

		/**
		 * Get the value flag type of parameter.
		 *
		 * @param flag The flag character
		 * @return An option type
		 */
		fun valueFlag(flag: Char): OptionType = ValueFlag(flag)
	}
}
