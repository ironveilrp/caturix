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
package com.ironveilrp.caturix.argument

/**
 * Builds instances of [CommandArgs].
 */
object Arguments {
	/**
	 * Create an argument stack from a CommandContext.
	 *
	 * @param context The instance of a CommandContext
	 * @return The arguments
	 */
	fun viewOf(context: CommandContext): CommandArgs {
		return ContextArgs(context)
	}

	/**
	 * Create an argument stack from a list of string arguments using
	 * an empty namespace.
	 *
	 * @param arguments The list of string arguments
	 * @return The arguments
	 */
	fun copyOf(arguments: List<String>): CommandArgs {
		return StringListArgs(arguments, emptyMap(), Namespace())
	}

	/**
	 * Create an argument stack from a list of string arguments using
	 * an empty namespace.
	 *
	 * @param arguments The array of string arguments
	 * @return The arguments
	 */
	fun of(vararg arguments: String): CommandArgs {
		return copyOf(arguments.toList())
	}

	/**
	 * Create an argument stack from a list of string arguments.
	 *
	 * @param arguments The list of string arguments
	 * @param flags A map of flags, where the key is the flag and the value may be null
	 * @return The arguments
	 */
	fun copyOf(arguments: List<String>, flags: Map<Char, String?>): CommandArgs {
		return StringListArgs(arguments, flags, Namespace())
	}

	/**
	 * Create an argument stack from a list of string arguments.
	 *
	 * @param arguments The list of string arguments
	 * @param flags A map of flags, where the key is the flag and the value may be null
	 * @param namespace The associated namespace
	 * @return The arguments
	 */
	fun copyOf(arguments: List<String>, flags: Map<Char, String?>, namespace: Namespace): CommandArgs {
		return StringListArgs(arguments, flags, namespace)
	}
}
