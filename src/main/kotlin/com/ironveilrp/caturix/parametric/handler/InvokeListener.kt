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

import com.ironveilrp.caturix.ImmutableDescription
import com.ironveilrp.caturix.parametric.*

/**
 * Listens to events related to [ParametricBuilder].
 */
interface InvokeListener {
	/**
	 * Create a new invocation handler.
	 *
	 *
	 * An example use of an [InvokeHandler] would be to verify permissions
	 * added by the [Require] annotation.
	 *
	 *
	 * For simple [InvokeHandler], an object can implement both this
	 * interface and [InvokeHandler].
	 *
	 * @return A new invocation handler
	 */
	fun createInvokeHandler(): InvokeHandler

	/**
	 * Called to update the description of a command.
	 *
	 * @param annotations Annotations on the command
	 * @param parser The parser containing parameter information
	 * @param descriptionBuilder The description builder
	 */
	fun updateDescription(
		annotations: Set<Annotation>,
		parser: ArgumentParser,
		descriptionBuilder: ImmutableDescription.Builder
	)
}
