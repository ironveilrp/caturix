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

import com.ironveilrp.caturix.CommandCallable
import com.ironveilrp.caturix.CommandException
import com.ironveilrp.caturix.InvalidUsageException
import com.ironveilrp.caturix.InvocationCommandException
import com.ironveilrp.caturix.argument.*
import com.ironveilrp.caturix.parametric.handler.InvokeHandler
import com.ironveilrp.caturix.util.auth.AuthorizationException
import java.util.concurrent.ExecutionException

/**
 * A base class for commands that use [ArgumentParser].
 */
abstract class AbstractParametricCallable protected constructor(
	protected val builder: ParametricBuilder,
	protected val parser: ArgumentParser) : CommandCallable {

	protected var commandAnnotations: List<Annotation> = emptyList()
	/**
	 * Get whether flags that are not used by any parameter should be
	 * ignored so that an [UnusedArgumentException] is not
	 * thrown.
	 *
	 * @return Whether unused flags should be ignored
	 */
	/**
	 * Set whether flags that are not used by any parameter should be
	 * ignored so that an [UnusedArgumentException] is not
	 * thrown.
	 *
	 * @param ignoreUnusedFlags Whether unused flags should be ignored
	 */
	protected var isIgnoreUnusedFlags: Boolean = false

	/**
	 * List of flags that should not cause an
	 * [UnusedArgumentException] to be thrown if they are
	 * not consumed by a parameter.
	 */
	protected var unusedFlags = emptySet<Char>()

	@Throws(CommandException::class, InvocationCommandException::class, AuthorizationException::class)
	override suspend fun call(arguments: String, namespace: Namespace, parentCommands: List<String>): Boolean {
		// Test permission
		if (!testPermission(namespace)) {
			throw AuthorizationException()
		}

		val calledCommand = if (parentCommands.isNotEmpty()) parentCommands[parentCommands.size - 1] else "_"
		val split: Array<String> = CommandContext.split("$calledCommand $arguments")
		val context = CommandContext(split, parser.valueFlags, false, namespace)
		val commandArgs: CommandArgs = Arguments.viewOf(context)
		val handlers: MutableList<InvokeHandler> = ArrayList()

		// Provide help if -? is specified
		if (context.hasFlag('?')) {
			throw InvalidUsageException(null, this, parentCommands, true)
		}

		for (listener in builder.invokeListeners) {
			val handler = listener.createInvokeHandler()
			handlers.add(handler)
		}

		try {
			var invoke = true

			// preProcess
			for (handler in handlers) {
				if (!handler.preProcess(commandAnnotations, parser, commandArgs)) {
					invoke = false
				}
			}

			if (!invoke) {
				return true // Abort early
			}

			val args = parser.parseArguments(commandArgs, isIgnoreUnusedFlags, unusedFlags)

			// preInvoke
			for (handler in handlers) {
				if (!handler.preInvoke(commandAnnotations, parser, args, commandArgs)) {
					invoke = false
				}
			}

			if (!invoke) {
				return true // Abort early
			}

			namespace.put(CommandArgs::class.java, commandArgs)

			// invoke
			try {
				this@AbstractParametricCallable.call(args)
			} catch (e: ExecutionException) {
				throw e.cause!!
			}

			// postInvoke
			for (handler in handlers) {
				handler.postInvoke(commandAnnotations, parser, args, commandArgs)
			}
		} catch (e: MissingArgumentException) {
			if (e.parameter != null) {
				throw InvalidUsageException(
					"Too few arguments! No value found for parameter '" + e.parameter.name + "'",
					this,
					parentCommands,
					false,
					e
				)
			} else {
				throw InvalidUsageException("Too few arguments!", this, parentCommands, false, e)
			}
		} catch (e: UnusedArgumentException) {
			throw InvalidUsageException(
				"Too many arguments! Unused arguments: " + e.unconsumed,
				this,
				parentCommands,
				false,
				e
			)
		} catch (e: ArgumentParseException) {
			if (e.parameter != null) {
				throw InvalidUsageException(
					"For parameter '" + e.parameter.name + "': " + e.message,
					this,
					parentCommands,
					false,
					e
				)
			} else {
				throw InvalidUsageException("Error parsing arguments: " + e.message, this, parentCommands, false, e)
			}
		} catch (e: ArgumentException) { // Something else wrong with an argument
			throw InvalidUsageException("Error parsing arguments: " + e.message, this, parentCommands, false, e)
		} catch (e: CommandException) { // Thrown by commands
			throw e
		} catch (e: ProvisionException) { // Argument binding failed
			throw InvocationCommandException("Internal error occurred: " + e.message, e)
		} catch (e: InterruptedException) { // Thrown by execution
			throw InvocationCommandException("Execution of the command was interrupted", e.cause)
		} catch (e: Throwable) { // Catch all
			for (converter in builder.exceptionConverters) {
				converter.convert(e)
			}

			throw InvocationCommandException(e.message, e)
		}

		return true
	}

	/**
	 * Called with parsed arguments to execute the command.
	 *
	 * @param args The arguments parsed into the appropriate Java objects
	 * @throws Exception on any exception
	 */
	@Throws(Exception::class)
	protected abstract suspend fun call(args: Array<Any?>)

	@Throws(CommandException::class)
	override suspend fun getSuggestions(arguments: String, locals: Namespace): List<String> {
		return parser.parseSuggestions(arguments, locals)
	}
}
