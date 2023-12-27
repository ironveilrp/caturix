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
package com.fablesfantasyrp.caturix.parametric.binder

import com.fablesfantasyrp.caturix.parametric.Provider

/**
 * Part of the fluent binding creation interface.
 *
 * @param <T> The type being provided for
</T> */
interface BindingBuilder<T> {
	/**
	 * Indicates a classifier that the binding will listen for.
	 *
	 * @param annotation The classifier annotation class
	 * @return The same class
	 */
	fun annotatedWith(annotation: Class<out Annotation>): BindingBuilder<T>

	/**
	 * Creates a binding that is provided by the given provider class.
	 *
	 * @param provider The provider
	 */
	fun toProvider(provider: Provider<T>)

	/**
	 * Creates a binding that is provided by the given static instance.
	 *
	 * @param instance The instance
	 */
	fun toInstance(instance: T)
}
