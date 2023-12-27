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
package com.fablesfantasyrp.caturix.completion

import com.fablesfantasyrp.caturix.*
import com.fablesfantasyrp.caturix.argument.*

/**
 * Provides a method that can provide tab completion for commands.
 */
interface CommandCompleter {
	/**
	 * Get a list of suggestions based on input.
	 *
	 * @param arguments the arguments entered up to this point
	 * @param locals The namespace to send to providers
	 * @return a list of suggestions
	 * @throws CommandException thrown if there was a parsing error
	 */
	@Throws(CommandException::class)
	fun getSuggestions(arguments: String, locals: Namespace): List<String>
}
