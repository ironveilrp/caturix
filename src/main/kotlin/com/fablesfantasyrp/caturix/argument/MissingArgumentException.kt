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

import com.fablesfantasyrp.caturix.*

/**
 * Thrown when the user has not provided a sufficient number of arguments,
 * which may include positional arguments and/or flag arguments.
 */
class MissingArgumentException : ArgumentException {
	val parameter: Parameter?

	constructor() {
		this.parameter = null
	}

	constructor(parameter: Parameter?) {
		this.parameter = parameter
	}

	constructor(cause: Throwable, parameter: Parameter?) : super(cause) {
		this.parameter = parameter
	}
}
