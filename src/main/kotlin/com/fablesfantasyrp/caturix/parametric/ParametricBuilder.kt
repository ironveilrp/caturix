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

import com.google.common.collect.Lists
import com.fablesfantasyrp.caturix.Command
import com.fablesfantasyrp.caturix.CommandCallable
import com.fablesfantasyrp.caturix.completion.CommandCompleter
import com.fablesfantasyrp.caturix.completion.NullCompleter
import com.fablesfantasyrp.caturix.dispatcher.Dispatcher
import com.fablesfantasyrp.caturix.parametric.handler.ExceptionConverter
import com.fablesfantasyrp.caturix.parametric.handler.InvokeListener
import com.fablesfantasyrp.caturix.util.auth.Authorizer
import com.fablesfantasyrp.caturix.util.auth.NullAuthorizer
import com.google.common.util.concurrent.MoreExecutors
import java.lang.reflect.Method
import java.util.concurrent.ExecutorService

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
	 * Get the executor service used to invoke the actual command.
	 *
	 *
	 * Bindings will still be resolved in the thread in which the
	 * callable was called.
	 *
	 * @return The command executor
	 */
	var commandExecutor: CommandExecutor = CommandExecutorWrapper(MoreExecutors.newDirectExecutorService())
		private set

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
	 * Set the executor service used to invoke the actual command.
	 *
	 *
	 * Bindings will still be resolved in the thread in which the
	 * callable was called.
	 *
	 * @param commandExecutor The executor
	 */
	fun setCommandExecutor(commandExecutor: ExecutorService) {
		setCommandExecutor(CommandExecutorWrapper(commandExecutor))
	}

	/**
	 * Set the executor service used to invoke the actual command.
	 *
	 *
	 * Bindings will still be resolved in the thread in which the
	 * callable was called.
	 * a
	 * @param commandExecutor The executor
	 */
	fun setCommandExecutor(commandExecutor: CommandExecutor) {
		this.commandExecutor = commandExecutor
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
		return MethodCallable.Companion.create(this, `object`, method)
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
