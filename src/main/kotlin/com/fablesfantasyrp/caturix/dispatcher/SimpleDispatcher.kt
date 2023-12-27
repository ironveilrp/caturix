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
package com.fablesfantasyrp.caturix.dispatcher

import com.fablesfantasyrp.caturix.*
import com.fablesfantasyrp.caturix.argument.CommandContext
import com.fablesfantasyrp.caturix.argument.Namespace
import com.fablesfantasyrp.caturix.util.auth.AuthorizationException
import java.util.*

/**
 * A simple implementation of [Dispatcher].
 */
class SimpleDispatcher : Dispatcher {
	private val commandMap: MutableMap<String, CommandMapping> = HashMap<String, CommandMapping>()
	override val description: Description?

	/**
	 * Create a new instance.
	 */
	init {
		val parameters: MutableList<Parameter> = arrayListOf()

		parameters.add(
			ImmutableParameter.Builder()
				.setName("subcommand")
				.setOptionType(OptionType.positional())
				.build()
		)

		parameters.add(
			ImmutableParameter.Builder()
				.setName("...")
				.setOptionType(OptionType.optionalPositional())
				.build()
		)

		description = ImmutableDescription.Builder()
			.setParameters(parameters)
			.build()
	}

	override fun registerCommand(callable: CommandCallable, vararg alias: String) {
		val mapping: CommandMapping = ImmutableCommandMapping(callable, *alias)


		// Check for replacements
		for (a in alias) {
			val lower = a.lowercase(Locale.getDefault())
			require(!commandMap.containsKey(lower)) { "Can't add the command '$a' because SimpleDispatcher does not support replacing commands" }
		}

		for (a in alias) {
			val lower = a.lowercase(Locale.getDefault())
			commandMap[lower] = mapping
		}
	}

	override val commands: Set<CommandMapping> get() = commandMap.values.toHashSet()

	override val aliases: Set<String>
		get() = commandMap.keys.toSet()

	override val primaryAliases: Set<String>
		get() {
			val aliases: MutableSet<String> = HashSet()
			for (mapping in commands) {
				aliases.add(mapping.primaryAlias)
			}
			return Collections.unmodifiableSet(aliases)
		}

	override fun contains(alias: String): Boolean {
		return commandMap.containsKey(alias.lowercase(Locale.getDefault()))
	}

	override fun get(alias: String): CommandMapping? {
		return commandMap[alias.lowercase(Locale.getDefault())]
	}

	@Throws(CommandException::class, InvocationCommandException::class, AuthorizationException::class)
	override fun call(arguments: String, namespace: Namespace, parentCommands: List<String>): Boolean {
		// We have permission for this command if we have permissions for subcommands
		if (!testPermission(namespace)) {
			throw AuthorizationException()
		}

		val split: Array<String> = CommandContext.Companion.split(arguments)
		val aliases: Set<String> = primaryAliases.toSet()

		if (aliases.isEmpty()) {
			throw InvalidUsageException("This command has no sub-commands.", this, parentCommands)
		} else if (split.isNotEmpty()) {
			val subCommand = split[0]
			val subArguments: String = Arrays.copyOfRange(split, 1, split.size).joinToString(" ")
			val subParents: List<String> = listOf<String>().plus(parentCommands).plus(subCommand)
			val mapping: CommandMapping? = get(subCommand)

			if (mapping != null) {
				try {
					mapping.callable.call(subArguments, namespace, subParents)
				} catch (e: AuthorizationException) {
					throw e
				} catch (e: CommandException) {
					throw e
				} catch (e: InvocationCommandException) {
					throw e
				} catch (t: Throwable) {
					throw InvocationCommandException(t)
				}

				return true
			}
		}

		throw InvalidUsageException("Please choose a sub-command.", this, parentCommands, true)
	}

	@Throws(CommandException::class)
	override fun getSuggestions(arguments: String, locals: Namespace): List<String> {
		val split: Array<String> = CommandContext.split(arguments)

		if (split.size <= 1) {
			val prefix = if (split.isNotEmpty()) split[0] else ""

			val suggestions: MutableList<String> = ArrayList()

			for (mapping in commands) {
				if (mapping.callable.testPermission(locals)) {
					for (alias in mapping.allAliases) {
						if (prefix.isEmpty() || alias.startsWith(arguments!!)) {
							suggestions.add(mapping.primaryAlias)
							break
						}
					}
				}
			}

			return suggestions
		} else {
			val subCommand = split[0]
			val mapping: CommandMapping? = get(subCommand)
			val passedArguments: String = Arrays.copyOfRange(split, 1, split.size).joinToString(" ")

			return mapping?.callable?.getSuggestions(passedArguments, locals) ?: emptyList()
		}
	}

	override fun testPermission(namespace: Namespace): Boolean {
		return commands.any { it.callable.testPermission(namespace) }
	}
}
