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
package com.ironveilrp.caturix.internal.parametric

import com.ironveilrp.caturix.parametric.Binding
import com.ironveilrp.caturix.parametric.Key
import com.ironveilrp.caturix.parametric.Provider
import com.google.common.base.Supplier
import com.google.common.collect.Multimap
import com.google.common.collect.Multimaps
import java.lang.reflect.Type
import java.util.*

class BindingList {
	private val providers: Multimap<Type, BindingEntry<*>> = Multimaps.newMultimap(HashMap(), CollectionSupplier())

	fun <T> addBinding(key: Key<T>, provider: Provider<T>) {
		providers.put(key.type, BindingEntry(key, provider))
	}

	fun <T> getBinding(key: Key<T>): Binding<T>? {
		for (binding in providers.get(key.type)) {
			if (binding.key.compareTo(key) == 0) {
				return binding as Binding<T>
			}
		}

		return null
	}

	private class CollectionSupplier : Supplier<Collection<BindingEntry<*>>> {
		override fun get(): Collection<BindingEntry<*>> {
			return TreeSet()
		}
	}

	private class BindingEntry<T>(override val key: Key<T>,
								  override val provider: Provider<T>) : Binding<T>, Comparable<BindingEntry<*>> {

		override fun compareTo(other: BindingEntry<*>): Int {
			return key.compareTo(other.key)
		}

		override fun toString(): String {
			return "BindingEntry{" +
					"key=" + key +
					", provider=" + provider +
					'}'
		}
	}

}
