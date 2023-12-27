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
package com.fablesfantasyrp.caturix.parametric.annotation

/**
 * Specifies a range of values for numbers.
 *
 * @see PrimitivesModule a user of this annotation as a modifier
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
annotation class Range(
	/**
	 * The minimum value that the number can be at, inclusive.
	 *
	 * @return The minimum value
	 */
	val min: Double = Double.MIN_VALUE,
	/**
	 * The maximum value that the number can be at, inclusive.
	 *
	 * @return The maximum value
	 */
	val max: Double = Double.MAX_VALUE
)
