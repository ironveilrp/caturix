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
package com.ironveilrp.caturix.util.auth

/**
 * A subject has authorization attached to it.
 */
interface Subject {
	/**
	 * Get a list of groups that this subject is a part of.
	 *
	 * @return An array containing a group name per entry
	 */
	val groups: Array<String>

	/**
	 * Check whether this subject has been granted the given permission
	 * and throw an exception on error.
	 *
	 * @param permission The permission
	 * @throws AuthorizationException If not permitted
	 */
	@Throws(AuthorizationException::class)
	fun checkPermission(permission: String)

	/**
	 * Return whether this subject has the given permission.
	 *
	 * @param permission The permission
	 * @return Whether permission is granted
	 */
	fun hasPermission(permission: String): Boolean
}
