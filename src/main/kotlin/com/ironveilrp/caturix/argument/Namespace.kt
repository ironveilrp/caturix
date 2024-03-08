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
 * This object holds contextual data for a command execution.
 *
 *
 * The purpose of a namespace is to pass non-argument data to
 * commands such as current session data and so on.
 */
class Namespace {
	private val locals: MutableMap<Any, Any?> = HashMap()

	/**
	 * Test whether the given key exists.
	 *
	 * @param key The key
	 * @return true If the key exists
	 */
	fun containsKey(key: Any): Boolean {
		return locals.containsKey(key)
	}

	/**
	 * Test whether the given value exists.
	 *
	 * @param value The value
	 * @return true If the value exists
	 */
	fun containsValue(value: Any?): Boolean {
		return locals.containsValue(value)
	}

	/**
	 * Returns the value specified by the given key.
	 *
	 * @param key The key
	 * @return The value, which may be null, including when the key doesn't exist
	 */
	fun get(key: Any): Any? {
		return locals[key]
	}

	/**
	 * Get an object whose key will be the object's class.
	 *
	 * @param key The key
	 * @param <T> The type of object
	 * @return The value
	</T> */
	fun <T> get(key: Class<T>): T? {
		return locals[key] as T?
	}

	/**
	 * Set an contextual value.
	 *
	 * @param key Key with which the specified value is to be associated
	 * @param value Value to be associated with the specified key
	 * @return The previous value associated with `key`, or
	 * `null` if there was no mapping for `key`.
	 * (A `null` return can also indicate that the map
	 * previously associated `null` with `key`,
	 * if the implementation supports `null` values.)
	 * @throws UnsupportedOperationException if the `put` operation
	 * is not supported by this map
	 * @throws ClassCastException if the class of the specified key or value
	 * prevents it from being stored in this map
	 */
	fun put(key: Any, value: Any?): Any? {
		return locals.put(key, value)
	}
}
