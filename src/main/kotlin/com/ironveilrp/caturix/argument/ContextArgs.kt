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
package com.ironveilrp.caturix.argument

/**
 * Provides a view of a [CommandContext] as arguments.
 */
internal class ContextArgs(private val context: CommandContext) : AbstractCommandArgs() {
	private var position = 0

	override fun hasNext(): Boolean {
		return position < context.argsLength()
	}

	@Throws(MissingArgumentException::class)
	override fun next(): String {
		try {
			return context.getString(position++)
		} catch (ignored: IndexOutOfBoundsException) {
			throw MissingArgumentException()
		}
	}

	@Throws(MissingArgumentException::class)
	override fun peek(): String {
		try {
			return context.getString(position)
		} catch (ignored: IndexOutOfBoundsException) {
			throw MissingArgumentException()
		}
	}

	override fun position(): Int {
		return position
	}

	override fun size(): Int {
		return context.argsLength()
	}

	override fun markConsumed() {
		position = context.argsLength()
	}

	override val flags: Map<Char, String> get() = context.flagsMap
	override val namespace: Namespace get() = context.namespace
}
