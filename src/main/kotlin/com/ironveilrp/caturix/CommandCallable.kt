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
package com.ironveilrp.caturix

import com.ironveilrp.caturix.argument.Namespace
import com.ironveilrp.caturix.completion.CommandCompleter
import com.ironveilrp.caturix.util.auth.AuthorizationException

/**
 * A command that can be executed.
 */
interface CommandCallable : CommandCompleter {
	/**
	 * Execute the command.
	 *
	 *
	 * `parentCommands` is a list of "parent" commands, including
	 * the current command, where each deeper command is appended to
	 * the list of parent commands.
	 *
	 *
	 * For example, if the command entered was `/world create ocean` and
	 * the command in question was the "create" command, then:
	 *
	 *
	 *  * `arguments` would be `ocean`
	 *  * `parentCommands` would be `world create`
	 *
	 *
	 *
	 * On the other hand, if the command was "world," then:
	 *
	 *
	 *  * `arguments` would be `create ocean`
	 *  * `parentCommands` would be `world`
	 *
	 *
	 * @param arguments The arguments
	 * @param namespace Additional values used for execution
	 * @param parentCommands The list of parent commands
	 * @return Whether the command succeeded
	 * @throws CommandException If there is an error with the command
	 * @throws InvocationCommandException If there is an error with executing the command
	 * @throws AuthorizationException If there is a authorization error
	 */
	@Throws(CommandException::class, InvocationCommandException::class, AuthorizationException::class)
	suspend fun call(arguments: String, namespace: Namespace, parentCommands: List<String>): Boolean

	/**
	 * Get the object describing the command.
	 *
	 * @return The object describing the command
	 */
	val description: Description?

	/**
	 * Test whether the user is permitted to use the command.
	 *
	 * @param namespace The namespace
	 * @return Whether permission is provided
	 */
	fun testPermission(namespace: Namespace): Boolean
}
