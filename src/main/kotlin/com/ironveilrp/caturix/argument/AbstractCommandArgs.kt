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

abstract class AbstractCommandArgs : CommandArgs {
	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextInt(): Int {
		val next = next()
		try {
			return next.toInt()
		} catch (ignored: NumberFormatException) {
			throw ArgumentParseException("Expected a number, got '$next'")
		}
	}

	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextShort(): Short {
		val next = next()
		try {
			return next.toShort()
		} catch (ignored: NumberFormatException) {
			throw ArgumentParseException("Expected a number, got '$next'")
		}
	}

	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextByte(): Byte {
		val next = next()
		try {
			return next.toByte()
		} catch (ignored: NumberFormatException) {
			throw ArgumentParseException("Expected a number, got '$next'")
		}
	}

	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextDouble(): Double {
		val next = next()
		try {
			return next.toDouble()
		} catch (ignored: NumberFormatException) {
			throw ArgumentParseException("Expected a number, got '$next'")
		}
	}

	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextFloat(): Float {
		val next = next()
		try {
			return next.toFloat()
		} catch (ignored: NumberFormatException) {
			throw ArgumentParseException("Expected a number, got '$next'")
		}
	}

	@Throws(MissingArgumentException::class, ArgumentParseException::class)
	override fun nextBoolean(): Boolean {
		val next = next()
		return if (next.equals("yes", ignoreCase = true) || next.equals("true", ignoreCase = true) || next.equals(
				"y",
				ignoreCase = true
			) || next.equals("1", ignoreCase = true)
		) {
			true
		} else if (next.equals("no", ignoreCase = true) || next.equals("false", ignoreCase = true) || next.equals(
				"n",
				ignoreCase = true
			) || next.equals("0", ignoreCase = true)
		) {
			false
		} else {
			throw ArgumentParseException("Expected a boolean (yes/no), got '$next'")
		}
	}
}
