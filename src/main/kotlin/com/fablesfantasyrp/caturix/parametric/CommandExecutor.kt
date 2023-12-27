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
package com.fablesfantasyrp.caturix.parametric

import com.fablesfantasyrp.caturix.argument.CommandArgs
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * Accepts commands as callables and executes them.
 */
interface CommandExecutor {
	/**
	 * Execute the given task.
	 *
	 * @param task The task
	 * @param args The arguments
	 * @param <T> The type of the task return value
	 * @return A future
	</T> */
	fun <T> submit(task: Callable<T>, args: CommandArgs): Future<T>
}
