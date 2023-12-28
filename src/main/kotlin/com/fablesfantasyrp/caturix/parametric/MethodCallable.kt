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

import com.fablesfantasyrp.caturix.*
import com.google.common.collect.ImmutableList
import com.fablesfantasyrp.caturix.argument.Namespace
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.full.callSuspend
import kotlin.reflect.jvm.kotlinFunction

/**
 * The implementation of a [CommandCallable] for the
 * [ParametricBuilder].
 */
internal class MethodCallable private constructor(
	builder: ParametricBuilder,
	parser: ArgumentParser,
	private val `object`: Any,
	private val method: Method,
	override val description: Description?,
	private val permissions: List<String>?
) : AbstractParametricCallable(builder, parser) {
	@Throws(Exception::class)
	override suspend fun call(args: Array<Any?>) {
		try {
			val kotlinFunction = method.kotlinFunction
			if (kotlinFunction != null) {
				kotlinFunction.callSuspend(`object`, *args)
			} else {
				method.invoke(`object`, *args)
			}
		} catch (e: IllegalAccessException) {
			throw InvocationCommandException("Could not invoke method '$method'", e)
		} catch (e: InvocationTargetException) {
			if (e.cause is Exception) {
				throw (e.cause as Exception?)!!
			} else {
				throw InvocationCommandException("Could not invoke method '$method'", e)
			}
		}
	}

	override fun testPermission(namespace: Namespace): Boolean {
		if (permissions != null) {
			for (perm in permissions) {
				if (builder.authorizer.testPermission(namespace, perm)) {
					return true
				}
			}

			return false
		} else {
			return true
		}
	}

	companion object {
		@Throws(IllegalParameterException::class)
		fun create(builder: ParametricBuilder, `object`: Any, method: Method): MethodCallable {
			val commandAnnotations: Set<Annotation> = method.annotations.toSet()

			val definition = method.getAnnotation(Command::class.java)
			checkNotNull(definition) { "Method lacks a @Command annotation" }

			val ignoreUnusedFlags = definition.anyFlags
			val unusedFlags: Set<Char> = definition.flags.toCharArray().toSet()

			val annotations = method.parameterAnnotations
			val types = method.genericParameterTypes

			val parserBuilder = ArgumentParser.Builder(builder.injector)

			// When the method is a suspend fun, the last parameter is a Continuation and should be ignored.
			val lastIndex = if (method.kotlinFunction?.isSuspend == true) types.size - 2 else types.size - 1
			for (i in 0..lastIndex) {
				parserBuilder.addParameter(types[i], listOf(*annotations[i]))
			}
			val parser = parserBuilder.build()

			val descBuilder = ImmutableDescription.Builder()
				.setParameters(parser.userParameters)
				.setShortDescription(definition.desc.ifEmpty { null })
				.setHelp(definition.help.ifEmpty { null })
				.setUsageOverride(definition.usage.ifEmpty { null })

			val permHint = method.getAnnotation(Require::class.java)
			var permissions: List<String>? = null
			if (permHint != null) {
				descBuilder.setPermissions(listOf(*permHint.value))
				permissions = listOf(*permHint.value)
			}

			for (listener in builder.invokeListeners) {
				listener.updateDescription(commandAnnotations, parser, descBuilder)
			}

			val description: Description = descBuilder.build()

			val callable = MethodCallable(builder, parser, `object`, method, description, permissions)
			callable.commandAnnotations = ImmutableList.copyOf(method.annotations)
			callable.isIgnoreUnusedFlags = ignoreUnusedFlags
			callable.unusedFlags = unusedFlags
			return callable
		}
	}
}
