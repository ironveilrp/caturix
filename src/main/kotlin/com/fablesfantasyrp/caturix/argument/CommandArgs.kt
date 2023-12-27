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
package com.fablesfantasyrp.caturix.argument

/**
 * Provides access to provided user arguments as a stack.
 */
interface CommandArgs {
	/**
	 * Tests whether there are additional arguments that can be read.
	 *
	 * @return Whether there are additional arguments
	 */
	fun hasNext(): Boolean

	/**
	 * Read the next argument.
	 *
	 * @return The next argument
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 */
	@Throws(MissingArgumentException::class)
	fun next(): String

	/**
	 * Read the next argument as an integer.
	 *
	 * @return The next argument as an integer
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an integer
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextInt(): Int

	/**
	 * Read the next argument as a short.
	 *
	 * @return The next argument as an short
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an short
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextShort(): Short

	/**
	 * Read the next argument as a byte.
	 *
	 * @return The next argument as an byte
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an byte
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextByte(): Byte

	/**
	 * Read the next argument as a double.
	 *
	 * @return The next argument as an double
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an double
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextDouble(): Double

	/**
	 * Read the next argument as a float.
	 *
	 * @return The next argument as an float
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an float
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextFloat(): Float

	/**
	 * Read the next argument as a boolean.
	 *
	 * @return The next argument as an boolean
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 * @throws ArgumentParseException Thrown if the next argument could not be parsed to an boolean
	 */
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	fun nextBoolean(): Boolean

	/**
	 * Return the next argument without moving the pointer.
	 *
	 * @return The next argument
	 * @throws MissingArgumentException Thrown if there are no remaining arguments
	 */
	@Throws(MissingArgumentException::class)
	fun peek(): String

	/**
	 * Get the current position of the pointer in the stack of arguments.
	 *
	 *
	 * The current position indicates the value that will be returned by the
	 * next call to [.next] and it starts at 0. If the current position
	 * is equal to [.size], then there are no more arguments
	 * available.
	 *
	 * @return The current position, starting from 0
	 */
	fun position(): Int

	/**
	 * Return the number of arguments in total.
	 *
	 * @return The number of arguments
	 */
	fun size(): Int

	/**
	 * Move the pointer to the end so that there are no unconsumed arguments.
	 */
	fun markConsumed()

	/**
	 * Get a map of defined flags.
	 *
	 *
	 * Keys are the flag (case-sensitive) and values are the values for
	 * the flag. For boolean flags, the value should be the string "true". Flags
	 * are commonly defined by the player by using syntax similar to
	 * `-X value`.
	 *
	 * @return The map of flags
	 */
	val flags: Map<Char, String?>

	/**
	 * Get the map of provided values.
	 *
	 *
	 * The keys and values in a Namespace are defined before command
	 * parsing has begun and they can be used by commands or
	 * [Provider] to get session-related values.
	 *
	 * @return The map of provided values
	 */
	val namespace: Namespace
}
