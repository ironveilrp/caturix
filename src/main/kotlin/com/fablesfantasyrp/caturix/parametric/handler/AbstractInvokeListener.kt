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

import com.fablesfantasyrp.caturix.ImmutableDescription
import com.fablesfantasyrp.caturix.parametric.ArgumentParser

/**
 * An abstract implementation of [InvokeListener] with some
 * no-operation methods implemented to assist in creating subclasses.
 */
abstract class AbstractInvokeListener : InvokeListener {
	override fun updateDescription(
		annotations: Set<Annotation>,
		parser: ArgumentParser,
		descriptionBuilder: ImmutableDescription.Builder
	) {
	}
}
