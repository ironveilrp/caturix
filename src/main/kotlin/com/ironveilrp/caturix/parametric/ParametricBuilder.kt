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

import com.ironveilrp.caturix.Command
import com.ironveilrp.caturix.CommandCallable
import com.ironveilrp.caturix.completion.CommandCompleter
import com.ironveilrp.caturix.completion.NullCompleter
import com.ironveilrp.caturix.dispatcher.Dispatcher
import com.ironveilrp.caturix.parametric.handler.ExceptionConverter
import com.ironveilrp.caturix.parametric.handler.InvokeListener
import com.ironveilrp.caturix.util.auth.Authorizer
import com.ironveilrp.caturix.util.auth.NullAuthorizer
import java.lang.reflect.Method

/**
 * Keeps a mapping of types to bindings and generates commands from classes
 * with appropriate annotations.
 */
class ParametricBuilder(val injector: Injector) {
	internal val invokeListeners: MutableList<InvokeListener> = ArrayList()
	internal val exceptionConverters: MutableList<ExceptionConverter> = ArrayList()

	var authorizer: Authorizer = NullAuthorizer()

	/**
	 * Get the default command suggestions provider that will be used if
	 * no suggestions are available.
	 *
	 * @return The default command completer
	 */
	var defaultCompleter: CommandCompleter = NullCompleter()

	/**
	 * Attach an invocation listener.
	 *
	 *
	 * Invocation handlers are called in order that their listeners are
	 * registered with a ParametricBuilder. It is not guaranteed that
	 * a listener may be called, in the case of a [CommandException] being
	 * thrown at any time before the appropriate listener or handler is called.
	 *
	 * @param listener The listener
	 * @see InvokeHandler tThe handler
	 */
	fun addInvokeListener(listener: InvokeListener) {
		invokeListeners.add(listener)
	}

	/**
	 * Attach an exception converter to this builder in order to wrap unknown
	 * [Throwable]s into known [CommandException]s.
	 *
	 *
	 * Exception converters are called in order that they are registered.
	 *
	 * @param converter The converter
	 * @see ExceptionConverter for an explanation
	 */
	fun addExceptionConverter(converter: ExceptionConverter) {
		exceptionConverters.add(converter)
	}

	/**
	 * Build a list of commands from methods specially annotated with [Command]
	 * (and other relevant annotations) and register them all with the given
	 * [Dispatcher].
	 *
	 * @param dispatcher The dispatcher to register commands with
	 * @param object The object contain the methods
	 * @throws ParametricException thrown if the commands cannot be registered
	 */
	@Throws(ParametricException::class)
	fun registerMethodsAsCommands(dispatcher: Dispatcher, `object`: Any) {

		for (method in `object`.javaClass.declaredMethods) {
			val definition = method.getAnnotation(Command::class.java)
			if (definition != null) {
				val callable: CommandCallable = build(`object`, method)
				dispatcher.registerCommand(callable, *definition.aliases)
			}
		}
	}

	/**
	 * Build a [CommandCallable] for the given method.
	 *
	 * @param object The object to be invoked on
	 * @param method The method to invoke
	 * @return The command executor
	 * @throws ParametricException Thrown on an error
	 */
	@Throws(ParametricException::class)
	fun build(`object`: Any, method: Method): CommandCallable {
		return MethodCallable.create(this, `object`, method)
	}

	/**
	 * Get a list of invocation listeners.
	 *
	 * @return A list of invocation listeners
	 */
	fun getInvokeListeners(): List<InvokeListener> {
		return invokeListeners
	}

	/**
	 * Get the list of exception converters.
	 *
	 * @return A list of exception converters
	 */
	fun getExceptionConverters(): List<ExceptionConverter> {
		return exceptionConverters
	}
}
