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
 * Argument exceptions occur when there is a problem with user-provided
 * arguments, whether it be because of incorrect arguments, missing arguments,
 * or excess arguments.
 *
 * @see ArgumentParseException
 *
 * @see MissingArgumentException
 *
 * @see UnusedArgumentException
 */
open class ArgumentException : Exception {
	protected constructor() : super()
	protected constructor(message: String?) : super(message)
	protected constructor(message: String?, cause: Throwable?) : super(message, cause)
	protected constructor(cause: Throwable?) : super(cause)
}
