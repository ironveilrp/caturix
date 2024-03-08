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
package com.fablesfantasyrp.caturix.parametric.provider

import com.fablesfantasyrp.caturix.argument.*
import com.fablesfantasyrp.caturix.parametric.*
import com.fablesfantasyrp.caturix.parametric.annotation.Range

internal abstract class NumberProvider<T : Number> : Provider<T> {
	override val isProvided: Boolean
		get() = false

	override suspend fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String> {
		return emptyList()
	}

	companion object {
		/**
		 * Try to parse numeric input as either a number or a mathematical expression.
		 *
		 * @param input input
		 * @return a number
		 * @throws ArgumentParseException thrown on parse error
		 */
		@JvmStatic
		@Throws(ArgumentParseException::class)
		protected fun parseNumericInput(input: String): Double {
			try {
				return when {
					input.startsWith("0b") -> input.toLong(2).toDouble()
					input.startsWith("0x") -> input.toLong(16).toDouble()
					else -> input.toDouble()
				}
			} catch (ignored: NumberFormatException) {
				throw ArgumentParseException(String.format("Expected '%s' to be a number", input))
			}
		}

		/**
		 * Validate a number value using relevant modifiers.
		 *
		 * @param number the number
		 * @param modifiers the list of modifiers to scan
		 * @throws ArgumentParseException on a validation error
		 */
		@JvmStatic
		@Throws(ArgumentParseException::class)
		protected fun validate(number: Double, modifiers: List<Annotation?>) {
			for (modifier in modifiers) {
				if (modifier is Range) {
					val range = modifier
					if (number < range.min) {
						throw ArgumentParseException(
							String.format(
								"A valid value is greater than or equal to %s (you entered %s)",
								range.min,
								number
							)
						)
					} else if (number > range.max) {
						throw ArgumentParseException(
							String.format(
								"A valid value is less than or equal to %s (you entered %s)",
								range.max,
								number
							)
						)
					}
				}
			}
		}

		/**
		 * Validate a number value using relevant modifiers.
		 *
		 * @param number the number
		 * @param modifiers the list of modifiers to scan
		 * @throws ArgumentParseException on a validation error
		 */
		@JvmStatic
		@Throws(ArgumentParseException::class)
		protected fun validate(number: Long, modifiers: List<Annotation?>) {
			for (modifier in modifiers) {
				if (modifier is Range) {
					val range = modifier
					if (number < range.min) {
						throw ArgumentParseException(
							String.format(
								"A valid value is greater than or equal to %s (you entered %s)",
								range.min,
								number
							)
						)
					} else if (number > range.max) {
						throw ArgumentParseException(
							String.format(
								"A valid value is less than or equal to %s (you entered %s)",
								range.max,
								number
							)
						)
					}
				}
			}
		}
	}
}
