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

import com.fablesfantasyrp.caturix.argument.ArgumentException
import com.fablesfantasyrp.caturix.argument.CommandArgs
import com.google.common.collect.ImmutableList
import com.fablesfantasyrp.caturix.argument.Namespace
import com.fablesfantasyrp.caturix.parametric.Provider
import com.fablesfantasyrp.caturix.parametric.ProvisionException

internal class CommandArgsProvider : Provider<CommandArgs> {
	override val isProvided: Boolean
		get() = true

	@Throws(ArgumentException::class, ProvisionException::class)
	override suspend fun get(arguments: CommandArgs, modifiers: List<Annotation>): CommandArgs {
		val commandArgs = arguments.namespace.get(CommandArgs::class.java)
		if (commandArgs != null) {
			commandArgs.markConsumed()
			return commandArgs
		} else {
			throw ProvisionException("CommandArgs object not found in Namespace")
		}
	}

	override suspend fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String> {
		return emptyList()
	}
}
