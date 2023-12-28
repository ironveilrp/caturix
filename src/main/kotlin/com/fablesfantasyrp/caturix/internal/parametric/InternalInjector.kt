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
package com.fablesfantasyrp.caturix.internal.parametric

import com.fablesfantasyrp.caturix.argument.ArgumentException
import com.fablesfantasyrp.caturix.argument.CommandArgs
import com.fablesfantasyrp.caturix.parametric.*
import com.fablesfantasyrp.caturix.parametric.provider.DefaultModule

class InternalInjector : Injector {
	private val bindings = BindingList()

	init {
		install(DefaultModule())
	}

	override fun install(module: Module) {
		module.configure(InternalBinder(bindings))
	}

	override fun <T> getBinding(key: Key<T>): Binding<T>? {
		return bindings.getBinding(key)
	}

	override fun <T> getBinding(type: Class<T>): Binding<T>? {
		return getBinding(Key.get(type))
	}

	override fun <T> getProvider(key: Key<T>): Provider<T>? {
		val binding: Binding<T>? = getBinding(key)
		return binding?.provider
	}

	override fun <T> getProvider(type: Class<T>): Provider<T>? {
		return getProvider(Key.get(type))
	}

	@Throws(ArgumentException::class, ProvisionException::class)
	override suspend fun <T> getInstance(key: Key<T>, arguments: CommandArgs, modifiers: List<Annotation>): T {
		val provider = getProvider(key)
		if (provider != null) {
			return provider.get(arguments, modifiers)
		} else {
			throw ProvisionException("No binding was found for $key")
		}
	}

	@Throws(ArgumentException::class, ProvisionException::class)
	override suspend fun <T> getInstance(type: Class<T>, arguments: CommandArgs, modifiers: List<Annotation>): T {
		return getInstance(Key.get(type), arguments, modifiers)
	}
}
