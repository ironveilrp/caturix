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
package com.fablesfantasyrp.caturix.parametric

import com.google.common.base.Preconditions.checkArgument
import com.fablesfantasyrp.caturix.parametric.binder.Binder
import com.fablesfantasyrp.caturix.parametric.binder.BindingBuilder

/**
 * Modules should extend this class and call the bind() functions to
 * add bindings.
 */
abstract class AbstractModule : Module {
	private var binder: Binder? = null

	@Synchronized
	override fun configure(binder: Binder) {
		checkArgument(this.binder == null, "configure(Binder) already called before")
		this.binder = binder
		configure()
	}

	protected abstract fun configure()

	fun <T> bind(clazz: Class<T>): BindingBuilder<T> {
		return binder!!.bind(clazz)
	}

	fun <T> bind(key: Key<T>): BindingBuilder<T> {
		return binder!!.bind(key)
	}
}
