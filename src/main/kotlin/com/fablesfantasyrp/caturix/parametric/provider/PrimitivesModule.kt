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
package com.fablesfantasyrp.caturix.parametric.provider

import com.fablesfantasyrp.caturix.parametric.AbstractModule
import com.fablesfantasyrp.caturix.parametric.annotation.Text

/**
 * Provides values for primitives as well as Strings.
 */
class PrimitivesModule : AbstractModule() {
	override fun configure() {
		bind(Boolean::class.javaObjectType).toProvider(BooleanProvider.INSTANCE)
		bind(Boolean::class.javaObjectType).toProvider(BooleanProvider.INSTANCE)
		bind(Int::class.javaObjectType).toProvider(IntegerProvider.INSTANCE)
		bind(Int::class.javaObjectType).toProvider(IntegerProvider.INSTANCE)
		bind(Short::class.javaObjectType).toProvider(ShortProvider.INSTANCE)
		bind(Short::class.javaObjectType).toProvider(ShortProvider.INSTANCE)
		bind(Double::class.javaObjectType).toProvider(DoubleProvider.INSTANCE)
		bind(Double::class.javaObjectType).toProvider(DoubleProvider.INSTANCE)
		bind(Float::class.javaObjectType).toProvider(FloatProvider.INSTANCE)
		bind(Float::class.javaObjectType).toProvider(FloatProvider.INSTANCE)
		bind(String::class.javaObjectType).toProvider(StringProvider.INSTANCE)
		bind(String::class.javaObjectType).annotatedWith(Text::class.java).toProvider(TextProvider.INSTANCE)

		bind(Boolean::class.java).toProvider(BooleanProvider.INSTANCE)
		bind(Boolean::class.java).toProvider(BooleanProvider.INSTANCE)
		bind(Int::class.java).toProvider(IntegerProvider.INSTANCE)
		bind(Int::class.java).toProvider(IntegerProvider.INSTANCE)
		bind(Short::class.java).toProvider(ShortProvider.INSTANCE)
		bind(Short::class.java).toProvider(ShortProvider.INSTANCE)
		bind(Double::class.java).toProvider(DoubleProvider.INSTANCE)
		bind(Double::class.java).toProvider(DoubleProvider.INSTANCE)
		bind(Float::class.java).toProvider(FloatProvider.INSTANCE)
		bind(Float::class.java).toProvider(FloatProvider.INSTANCE)
		bind(String::class.java).toProvider(StringProvider.INSTANCE)
		bind(String::class.java).annotatedWith(Text::class.java).toProvider(TextProvider.INSTANCE)
	}
}
