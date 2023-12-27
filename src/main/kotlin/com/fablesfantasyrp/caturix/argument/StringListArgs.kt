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

open class StringListArgs(arguments: List<String>,
						  override val flags: Map<Char, String?>,
						  override val namespace: Namespace) : AbstractCommandArgs() {
	private val arguments: MutableList<String> = arguments.toMutableList()
	private var position = 0

	protected open fun insert(argument: String) {
		arguments.add(position, argument)
	}

	override fun hasNext(): Boolean {
		return position < arguments.size
	}

	@Throws(MissingArgumentException::class)
	override fun next(): String {
		try {
			return arguments[position++]
		} catch (ignored: IndexOutOfBoundsException) {
			throw MissingArgumentException()
		}
	}

	@Throws(MissingArgumentException::class)
	override fun peek(): String? {
		try {
			return arguments[position]
		} catch (ignored: IndexOutOfBoundsException) {
			throw MissingArgumentException()
		}
	}

	override fun position(): Int {
		return position
	}

	override fun size(): Int {
		return arguments.size
	}

	override fun markConsumed() {
		position = arguments.size
	}
}
