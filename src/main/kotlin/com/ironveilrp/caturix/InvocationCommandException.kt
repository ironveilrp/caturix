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

/**
 * Thrown when invocation of a command fails, wrapping the exception that
 * is thrown.
 *
 *
 * [.getCause] will not be null.
 */
class InvocationCommandException : Exception {
	constructor(cause: Throwable?) : super(checkNotNull(cause))

	constructor(message: String?, cause: Throwable?) : super(message, checkNotNull(cause))
}
