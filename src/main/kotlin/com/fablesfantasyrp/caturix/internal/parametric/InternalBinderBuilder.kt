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

import com.fablesfantasyrp.caturix.parametric.Key
import com.fablesfantasyrp.caturix.parametric.Provider
import com.fablesfantasyrp.caturix.parametric.annotation.Classifier
import com.fablesfantasyrp.caturix.parametric.binder.BindingBuilder
import java.lang.annotation.Retention

internal class InternalBinderBuilder<T>(private val bindings: BindingList, private var key: Key<T>) : BindingBuilder<T> {

	override fun annotatedWith(annotation: Class<out Annotation>): BindingBuilder<T> {
		requireNotNull(annotation.getAnnotation(Classifier::class.java)) { "The annotation type " + annotation.name + " must be marked with @" + Classifier::class.java.name + " to be used as a classifier" }

		requireNotNull(annotation.getAnnotation(Retention::class.java)) { "The annotation type " + annotation.name + " must be marked with @" + Retention::class.java.name + " to appear at runtime" }
		key = key.setClassifier(annotation)
		return this
	}

	override fun toProvider(provider: Provider<T>) {
		bindings.addBinding(key, provider)
	}

	override fun toInstance(instance: T) {
		toProvider(ConstantProvider(instance))
	}
}
