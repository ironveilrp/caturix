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
package com.fablesfantasyrp.caturix.fluent

import com.fablesfantasyrp.caturix.CommandCallable
import com.fablesfantasyrp.caturix.dispatcher.Dispatcher
import com.fablesfantasyrp.caturix.dispatcher.SimpleDispatcher
import com.fablesfantasyrp.caturix.parametric.ParametricBuilder

/**
 * A collection of commands.
 */
class DispatcherNode
/**
 * Create a new instance.
 *
 * @param graph the root fluent graph object
 * @param parent the parent node, or null
 * @param dispatcher the dispatcher for this node
 */ internal constructor(
	val graph: CommandGraph,
	private val parent: DispatcherNode?,
	val dispatcher: Dispatcher
) {
	/**
	 * Register a command with this dispatcher.
	 *
	 * @param callable the executor
	 * @param alias the list of aliases, where the first alias is the primary one
	 */
	fun register(callable: CommandCallable, vararg alias: String) {
		dispatcher.registerCommand(callable, *alias)
	}

	/**
	 * Build and register a command with this dispatcher using the
	 * [ParametricBuilder] assigned on the root [CommandGraph].
	 *
	 * @param object the object provided to the [ParametricBuilder]
	 * @return this object
	 * @see ParametricBuilder.registerMethodsAsCommands
	 */
	fun registerMethods(`object`: Any): DispatcherNode {
		val builder = graph.builder ?: throw RuntimeException("No ParametricBuilder set")
		builder.registerMethodsAsCommands(dispatcher, `object`)
		return this
	}

	/**
	 * Create a new command that will contain sub-commands.
	 *
	 *
	 * The object returned by this method can be used to add sub-commands. To
	 * return to this "parent" context, use [graph].
	 *
	 * @param alias the list of aliases, where the first alias is the primary one
	 * @return an object to place sub-commands
	 */
	fun group(vararg alias: String): DispatcherNode {
		val command = SimpleDispatcher()
		dispatcher.registerCommand(command, *alias)
		return DispatcherNode(graph, this, command)
	}

	/**
	 * Return the parent node.
	 *
	 * @return the parent node
	 * @throws RuntimeException if there is no parent node.
	 */
	fun parent(): DispatcherNode {
		if (parent != null) {
			return parent
		}

		throw RuntimeException("This node does not have a parent")
	}
}
