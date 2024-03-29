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
package com.ironveilrp.caturix.dispatcher

import com.ironveilrp.caturix.CommandCallable
import com.ironveilrp.caturix.CommandMapping

/**
 * Executes a command based on user input.
 */
interface Dispatcher : CommandCallable {
	/**
	 * Register a command with this dispatcher.
	 *
	 * @param callable the command executor
	 * @param alias a list of aliases, where the first alias is the primary name
	 */
	fun registerCommand(callable: CommandCallable, vararg alias: String)

	/**
	 * Get a list of commands. Each command, regardless of how many aliases
	 * it may have, will only appear once in the returned set.
	 *
	 *
	 * The returned collection cannot be modified.
	 *
	 * @return a list of registrations
	 */
	val commands: Set<CommandMapping>

	/**
	 * Get a list of primary aliases.
	 *
	 *
	 * The returned collection cannot be modified.
	 *
	 * @return a list of aliases
	 */
	val primaryAliases: Set<String>

	/**
	 * Get a list of all the command aliases, which includes the primary alias.
	 *
	 *
	 * A command may have more than one alias assigned to it. The returned
	 * collection cannot be modified.
	 *
	 * @return a list of aliases
	 */
	val aliases: Set<String>

	/**
	 * Get the [CommandCallable] associated with an alias. Returns
	 * null if no command is named by the given alias.
	 *
	 * @param alias the alias
	 * @return the command mapping (null if not found)
	 */
	fun get(alias: String): CommandMapping?

	/**
	 * Returns whether the dispatcher contains a registered command for the given alias.
	 *
	 * @param alias the alias
	 * @return true if a registered command exists
	 */
	fun contains(alias: String): Boolean
}
