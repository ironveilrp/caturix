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

/**
 * A description of a command, providing information on the command's
 * parameters, a short description, a help text, and usage information.
 * However, it is up for implementations to provide the information
 * some implementations may provide very little information.
 *
 *
 * This class does not define a way to execute the command. See
 * [CommandCallable], which has a `getDescription()` method,
 * for an interface that does define how a command is executed.
 */
interface Description {
	/**
	 * Get the list of parameters for this command.
	 *
	 * @return A list of parameters
	 */
	val parameters: List<Parameter>

	val shortDescription: String?

	val help: String?

	/**
	 * Get the usage string of this command.
	 *
	 *
	 * A usage string may look like
	 * `[-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;`.
	 *
	 * @return A usage string
	 */
	val usage: String

	/**
	 * Get a list of permissions that the player may have to have permission.
	 *
	 *
	 * Permission data may or may not be available. This is only useful as a
	 * potential hint.
	 *
	 * @return The list of permissions
	 */
	val permissions: List<String>
}
