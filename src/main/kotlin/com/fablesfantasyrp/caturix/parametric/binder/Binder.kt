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

import com.fablesfantasyrp.caturix.parametric.AbstractModule
import com.fablesfantasyrp.caturix.parametric.Key

/**
 * A binder is a fluent interface for creating bindings.
 *
 *
 * Users should be extending [AbstractModule] in order to
 * access a binder.
 */
interface Binder {
	/**
	 * Start a binding with a class type.
	 *
	 * @param type The class
	 * @param <T> The type of the class
	 * @return The binding builder
	</T> */
	fun <T> bind(type: Class<T>): BindingBuilder<T>

	/**
	 * Start a binding with a key.
	 *
	 * @param key The key
	 * @param <T> The type of the key
	 * @return The binding builder
	</T> */
	fun <T> bind(key: Key<T>): BindingBuilder<T>
}
