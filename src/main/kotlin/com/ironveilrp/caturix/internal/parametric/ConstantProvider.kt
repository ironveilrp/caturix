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
package com.ironveilrp.caturix.internal.parametric

import com.ironveilrp.caturix.argument.*
import com.ironveilrp.caturix.parametric.*

internal class ConstantProvider<T>(private val value: T) : Provider<T> {
	override val isProvided: Boolean
		get() = true

	override suspend fun get(arguments: CommandArgs, modifiers: List<Annotation>): T {
		return value
	}

	override suspend fun getSuggestions(prefix: String, locals: Namespace, modifiers: List<Annotation>): List<String> {
		return emptyList()
	}
}
