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

import java.lang.reflect.Type

/**
 * Represents a parameter that a binding can provide a value for.
 */
class Key<T> private constructor(
	val type: Type?,
	val classifier: Class<out Annotation>?
) : Comparable<Key<*>> {
	fun matches(key: Key<T>): Boolean {
		return type == key.type && (classifier == null || classifier == key.classifier)
	}

	fun setClassifier(classifier: Class<out Annotation>?): Key<T> {
		return Key(type, classifier)
	}

	override fun compareTo(o: Key<*>): Int {
		return if (classifier != null && o.classifier == null) {
			-1
		} else if (classifier == null && o.classifier != null) {
			1
		} else if (classifier != null) {
			if (type != null && o.type == null) {
				-1
			} else if (type == null && o.type != null) {
				1
			} else {
				//allow for different classifiers in the same bindings list
				classifier.name.compareTo(o.classifier!!.name)
			}
		} else {
			0
		}
	}

	override fun equals(o: Any?): Boolean {
		if (this === o) return true
		if (o !is Key<*>) return false
		val key = o
		if (if (type != null) type != key.type else key.type != null) return false
		return if (classifier != null) classifier == key.classifier else key.classifier == null
	}

	override fun hashCode(): Int {
		var result = type?.hashCode() ?: 0
		result = 31 * result + (classifier?.hashCode() ?: 0)
		return result
	}

	override fun toString(): String {
		return "Key{" +
				"type=" + type +
				", classifier=" + classifier +
				'}'
	}

	companion object {
		fun <T> get(type: Class<T>?): Key<T> {
			return Key(type, null)
		}

		fun <T> get(type: Class<T>?, classifier: Class<out Annotation>?): Key<T> {
			return Key(type, classifier)
		}

		fun <T> get(type: Type?): Key<T> {
			return Key(type, null)
		}

		fun <T> get(type: Type?, classifier: Class<out Annotation>?): Key<T> {
			return Key(type, classifier)
		}
	}
}
