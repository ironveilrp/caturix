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
 * Thrown when a command is not used properly.
 *
 *
 * When handling this exception, print the error message if it is not null.
 * Print a one line help instruction unless [.isFullHelpSuggested]
 * is true, which, in that case, the full help of the command should be
 * shown.
 *
 *
 * If no error message is set and full help is not to be shown, then a generic
 * "you used this command incorrectly" message should be shown.
 */
class InvalidUsageException @JvmOverloads constructor(
	message: String?,
	val command: CommandCallable,
	aliasStack: List<String>,
	fullHelpSuggested: Boolean = false,
	cause: Throwable? = null
) : CommandException(message, cause) {

	/**
	 * Return whether the full usage of the command should be shown.
	 *
	 * @return show full usage
	 */
	val isFullHelpSuggested: Boolean = fullHelpSuggested

	/**
	 * Get a list of command names that were invoked, with the first-most
	 * listed command being the most "top-level" command.
	 *
	 *
	 * For example, if `/party add` was invoked but this exception
	 * was raised, then the aliases list would consist of
	 * `[party, add]`.
	 *
	 * @return a list of aliases
	 */
	val aliasStack: List<String> = aliasStack

	/**
	 * Create a new instance with no error message and with no suggestion
	 * that full and complete help for the command should be shown. This will
	 * result in a generic error message.
	 *
	 * @param command the command
	 * @param aliasStack the command text that was typed, including parent commands
	 */
	constructor(command: CommandCallable, aliasStack: List<String>) : this(null, command, aliasStack)
}
