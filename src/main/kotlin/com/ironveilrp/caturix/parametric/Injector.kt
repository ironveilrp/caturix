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
package com.ironveilrp.caturix.parametric

import com.ironveilrp.caturix.argument.ArgumentException
import com.ironveilrp.caturix.argument.CommandArgs

/**
 * An injector knows a list of "bindings" that map types to a provider.
 *
 *
 * For example, a command might accept an integer as an argument,
 * and so an appropriate binding for that parameter would have a provider
 * that parsed the argument as an integer and returned it.
 */
interface Injector {
	/**
	 * Install a module into the injector. Modules define bindings.
	 *
	 * @param module The module
	 */
	fun install(module: Module)

	/**
	 * Get the binding for the given key, if one exists.
	 *
	 * @param key The key
	 * @param <T> The type provided for
	 * @return The binding, or null if one does not exist
	</T> */
	fun <T> getBinding(key: Key<T>): Binding<T>?

	/**
	 * Get the binding for the given class, if one exists.
	 *
	 * @param type The class
	 * @param <T> The type provided for
	 * @return The binding, or null if one does not exist
	</T> */
	fun <T> getBinding(type: Class<T>): Binding<T>?

	/**
	 * Get the provider for the given key, if one exists.
	 *
	 * @param key The key
	 * @param <T> The type provided for
	 * @return The binding, or null if one does not exist
	</T> */
	fun <T> getProvider(key: Key<T>): Provider<T>?

	/**
	 * Get the provider for the given class, if one exists.
	 *
	 * @param type The class
	 * @param <T> The type provided for
	 * @return The binding, or null if one does not exist
	</T> */
	fun <T> getProvider(type: Class<T>): Provider<T>?

	/**
	 * Attempt to provide a value for the given key using the given
	 * arguments.
	 *
	 * @param key The key
	 * @param arguments The arguments
	 * @param modifiers The modifier annotations on the parameter
	 * @param <T> The type provided
	 * @return An instance
	 * @throws ArgumentException If there is a problem with the argument
	 * @throws ProvisionException If there is a problem with the provider
	</T> */
	@Throws(ArgumentException::class, ProvisionException::class)
	suspend fun <T> getInstance(key: Key<T>, arguments: CommandArgs, modifiers: List<Annotation>): T

	/**
	 * Attempt to provide a value for the given class using the given
	 * arguments.
	 *
	 * @param type The class
	 * @param arguments The arguments
	 * @param modifiers The modifier annotations on the parameter
	 * @param <T> The type provided
	 * @return An instance
	 * @throws ArgumentException If there is a problem with the argument
	 * @throws ProvisionException If there is a problem with the provider
	</T> */
	@Throws(ArgumentException::class, ProvisionException::class)
	suspend fun <T> getInstance(type: Class<T>, arguments: CommandArgs, modifiers: List<Annotation>): T
}
