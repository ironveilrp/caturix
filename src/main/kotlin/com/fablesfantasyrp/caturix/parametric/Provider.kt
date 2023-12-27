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
package com.fablesfantasyrp.caturix.parametric

import com.fablesfantasyrp.caturix.argument.*

/**
 * An object that provides instances given a key and some arguments.
 *
 *
 * Providers do the heavy work of reading passed in arguments and
 * transforming them into Java objects.
 */
interface Provider<T> {
	/**
	 * Gets whether this provider does not actually consume values
	 * from the argument stack and instead generates them otherwise.
	 *
	 * @return Whether values are provided without use of the arguments
	 */
	val isProvided: Boolean

	/**
	 * Provide a value given the arguments.
	 *
	 * @param arguments The arguments
	 * @param modifiers The modifiers on the parameter
	 * @return The value provided
	 * @throws ArgumentException If there is a problem with the argument
	 * @throws ProvisionException If there is a problem with the provider
	 */
	@Throws(ArgumentException::class, ProvisionException::class)
	fun get(arguments: CommandArgs, modifiers: List<Annotation>): T

	/**
	 * Get a list of suggestions for the given parameter and user arguments.
	 *
	 *
	 * If no suggestions could be enumerated, an empty list should
	 * be returned.
	 *
	 * @param prefix What the user has typed so far (may be an empty string)
	 * @param locals The namespace under which this command's suggestions are being provided
	 * @param modifiers The modifiers on the parameter
	 * @return A list of suggestions
	 */
	fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String>
}
