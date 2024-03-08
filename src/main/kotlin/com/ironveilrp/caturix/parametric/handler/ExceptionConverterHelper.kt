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
package com.ironveilrp.caturix.parametric.handler

import com.ironveilrp.caturix.CommandException
import com.ironveilrp.caturix.InvocationCommandException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * An implementation of an [ExceptionConverter] that calls methods
 * defined in subclasses that have been annotated with
 * [ExceptionMatch].
 *
 *
 * Only public methods will be used. Methods will be called in order of decreasing
 * levels of inheritance (between classes where one inherits the other). For two
 * different inheritance branches, the order between them is undefined.
 */
abstract class ExceptionConverterHelper protected constructor() : ExceptionConverter {
	private val handlers: List<ExceptionHandler>

	init {
		val handlers: MutableList<ExceptionHandler> = ArrayList()

		for (method in this.javaClass.methods) {
			if (method.getAnnotation(ExceptionMatch::class.java) == null) {
				continue
			}

			val parameters = method.parameterTypes
			if (parameters.size == 1) {
				val cls = parameters[0]
				if (Throwable::class.java.isAssignableFrom(cls)) {
					handlers.add(ExceptionHandler(cls as Class<out Throwable>, method))
				}
			}
		}

		handlers.sort()

		this.handlers = handlers
	}

	@Throws(CommandException::class, InvocationCommandException::class)
	override fun convert(t: Throwable) {
		val throwableClass: Class<*> = t.javaClass
		for (handler in handlers) {
			if (handler.type.isAssignableFrom(throwableClass)) {
				try {
					handler.method.invoke(this, t)
				} catch (e: InvocationTargetException) {
					if (e.cause is CommandException) {
						throw (e.cause as CommandException?)!!
					}
					throw InvocationCommandException(e)
				} catch (e: IllegalArgumentException) {
					throw InvocationCommandException(e)
				} catch (e: IllegalAccessException) {
					throw InvocationCommandException(e)
				}
			}
		}
	}

	private class ExceptionHandler(val type: Class<out Throwable>, val method: Method) : Comparable<ExceptionHandler> {
		override fun compareTo(other: ExceptionHandler): Int {
			return if (type == other.type) {
				0
			} else if (type.isAssignableFrom(other.type)) {
				1
			} else {
				-1
			}
		}
	}
}
