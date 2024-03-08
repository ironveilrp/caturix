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

import com.ironveilrp.caturix.Command
import com.ironveilrp.caturix.CommandException
import com.ironveilrp.caturix.ImmutableDescription
import com.ironveilrp.caturix.argument.ArgumentException
import com.ironveilrp.caturix.argument.CommandArgs
import com.ironveilrp.caturix.argument.MissingArgumentException
import com.ironveilrp.caturix.argument.UnusedArgumentException
import com.ironveilrp.caturix.parametric.ArgumentParser

/**
 * Handles legacy properties on [Command] such as [Command.min] and
 * [Command.max].
 */
class LegacyCommandsHandler : AbstractInvokeListener(), InvokeHandler {
	override fun createInvokeHandler(): InvokeHandler {
		return this
	}

	@Throws(CommandException::class, ArgumentException::class)
	override fun preProcess(
		annotations: List<Annotation>,
		parser: ArgumentParser,
		commandArgs: CommandArgs
	): Boolean {
		return true
	}

	@Throws(CommandException::class, ArgumentException::class)
	override fun preInvoke(
		annotations: List<Annotation>,
		parser: ArgumentParser,
		args: Array<Any?>,
		commandArgs: CommandArgs
	): Boolean {
		for (annotation in annotations) {
			if (annotation is Command) {
				val command = annotation

				if (commandArgs.size() < command.min) {
					throw MissingArgumentException()
				}

				if (command.max != -1 && commandArgs.size() > command.max) {
					val unconsumedArguments: MutableList<String> = ArrayList()

					while (true) {
						try {
							val value: String = commandArgs.next()
							if (commandArgs.position() >= command.max) {
								unconsumedArguments.add(value)
							}
						} catch (ignored: MissingArgumentException) {
							break
						}
					}

					throw UnusedArgumentException((unconsumedArguments.joinToString(" ")))
				}
			}
		}

		return true
	}

	@Throws(CommandException::class, ArgumentException::class)
	override fun postInvoke(
		annotations: List<Annotation>,
		parser: ArgumentParser,
		args: Array<Any?>,
		commandArgs: CommandArgs
	) {
	}

	override fun updateDescription(
		annotations: Set<Annotation>,
		parser: ArgumentParser,
		descriptionBuilder: ImmutableDescription.Builder
	) {
		for (annotation in annotations) {
			if (annotation is Command) {

				// Handle the case for old commands where no usage is set and all of its
				// parameters are provider bindings, so its usage information would
				// be blank and would imply that there were no accepted parameters
				if (annotation.usage.isEmpty() && (annotation.min > 0 || annotation.max > 0)) {
					if (parser.userParameters.isNotEmpty()) {
						descriptionBuilder.setUsageOverride("(unknown usage information)")
					}
				}
			}
		}
	}
}
