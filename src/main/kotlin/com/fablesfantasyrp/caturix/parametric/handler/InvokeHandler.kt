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
package com.fablesfantasyrp.caturix.parametric.handler

import com.fablesfantasyrp.caturix.CommandException
import com.fablesfantasyrp.caturix.argument.ArgumentException
import com.fablesfantasyrp.caturix.argument.CommandArgs
import com.fablesfantasyrp.caturix.parametric.ArgumentParser
import com.fablesfantasyrp.caturix.parametric.ParametricBuilder

/**
 * An invoke handler can be registered on a [ParametricBuilder] to
 * listen in on commands being executed.
 *
 *
 * Invoke handlers have access to three different stages of command
 * execution and can view the annotations, parameters, and arguments of the
 * command. An invoke handler could be used to implement command logging,
 * for example.
 */
interface InvokeHandler {
	/**
	 * Called before arguments have been parsed.
	 *
	 * @param annotations The list of annotations on the command
	 * @param parser The argument parser with parameter information
	 * @param commandArgs The arguments provided by the user
	 * @return Whether command execution should continue
	 * @throws CommandException Thrown if there is a general command problem
	 * @throws ArgumentException Thrown is there is an error with the arguments
	 */
	@Throws(CommandException::class, ArgumentException::class)
	fun preProcess(annotations: List<Annotation>, parser: ArgumentParser, commandArgs: CommandArgs): Boolean

	/**
	 * Called after arguments have been parsed but the command has yet
	 * to be executed.
	 *
	 * @param annotations The list of annotations on the command
	 * @param parser The argument parser with parameter information
	 * @param args The result of the parsed arguments: Java objects to be passed to the command
	 * @param commandArgs The arguments provided by the user
	 * @return Whether command execution should continue
	 * @throws CommandException Thrown if there is a general command problem
	 * @throws ArgumentException Thrown is there is an error with the arguments
	 */
	@Throws(CommandException::class, ArgumentException::class)
	fun preInvoke(
		annotations: List<Annotation>,
		parser: ArgumentParser,
		args: Array<Any?>,
		commandArgs: CommandArgs
	): Boolean

	/**
	 * Called after the command has been executed.
	 *
	 * @param annotations The list of annotations on the command
	 * @param parser The argument parser with parameter information
	 * @param args The result of the parsed arguments: Java objects to be passed to the command
	 * @param commandArgs The arguments provided by the user
	 * @throws CommandException Thrown if there is a general command problem
	 * @throws ArgumentException Thrown is there is an error with the arguments
	 */
	@Throws(CommandException::class, ArgumentException::class)
	fun postInvoke(
		annotations: List<Annotation>,
		parser: ArgumentParser,
		args: Array<Any?>,
		commandArgs: CommandArgs
	)
}
