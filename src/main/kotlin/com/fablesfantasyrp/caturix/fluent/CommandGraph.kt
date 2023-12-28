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

import com.fablesfantasyrp.caturix.dispatcher.Dispatcher
import com.fablesfantasyrp.caturix.dispatcher.SimpleDispatcher
import com.fablesfantasyrp.caturix.parametric.ParametricBuilder

/**
 * A fluent interface to creating a command graph.
 *
 *
 * A command graph may have multiple commands, and multiple sub-commands below that,
 * and possibly below that.
 */
class CommandGraph {
	private val rootDispatcher: DispatcherNode

	/**
	 * Get the [ParametricBuilder].
	 *
	 * @return the builder, or null.
	 */
	var builder: ParametricBuilder? = null
		private set

	/**
	 * Create a new command graph.
	 */
	init {
		val dispatcher = SimpleDispatcher()
		rootDispatcher = DispatcherNode(this, null, dispatcher)
	}

	/**
	 * Get the root dispatcher node.
	 *
	 * @return the root dispatcher node
	 */
	fun commands(): DispatcherNode {
		return rootDispatcher
	}

	/**
	 * Set the [ParametricBuilder] used for calls to
	 * [DispatcherNode.registerMethods].
	 *
	 * @param builder the builder, or null
	 * @return this object
	 */
	fun builder(builder: ParametricBuilder?): CommandGraph {
		this.builder = builder
		return this
	}

	val dispatcher: Dispatcher
		/**
		 * Get the root dispatcher.
		 *
		 * @return the root dispatcher
		 */
		get() = rootDispatcher.dispatcher
}
