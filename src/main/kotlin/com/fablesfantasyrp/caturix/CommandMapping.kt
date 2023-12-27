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
 * Provides information about a mapping between a command and its aliases.
 */
interface CommandMapping {
	/**
	 * Get the primary alias.
	 *
	 * @return The primary alias
	 */
	val primaryAlias: String

	/**
	 * Get a list of all aliases.
	 *
	 * @return Aliases
	 */
	val allAliases: List<String>

	/**
	 * Get the callable
	 *
	 * @return The callable
	 */
	val callable: CommandCallable

	/**
	 * Get the [Description] form the callable.
	 *
	 * @return The description
	 */
	val description: Description?
}
