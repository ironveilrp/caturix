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
package com.ironveilrp.caturix

import com.ironveilrp.caturix.ImmutableDescription.Builder

/**
 * An immutable implementation of a Description.
 *
 *
 * Use [Builder] to create instances.
 */
class ImmutableDescription private constructor(
	override val parameters: List<Parameter>,
	override val permissions: List<String>,
	override val shortDescription: String?,
	override val help: String?,
	private val usageOverride: String?
) : Description {

	override val usage: String
		get() {
			if (usageOverride != null) {
				return usageOverride
			}

			val builder = StringBuilder()
			var first = true

			for (parameter in parameters) {
				if (!first) {
					builder.append(" ")
				}
				builder.append(parameter.name)
				first = false
			}

			return builder.toString()
		}

	override fun toString(): String {
		return usage
	}

	/**
	 * Builds instances of [ImmutableDescription].
	 *
	 *
	 * By default, the list of parameters and permissions will
	 * be empty lists.
	 */
	class Builder {
		/**
		 * Get the list of parameters.
		 *
		 * @return The list of parameters
		 */
		var parameters: List<Parameter> = listOf()
			private set

		/**
		 * Get a list of permissions.
		 *
		 * @return The list of permissions
		 */
		var permissions: List<String> = listOf()
			private set

		/**
		 * Get the short description.
		 *
		 * @return The builder
		 */
		var shortDescription: String? = null
			private set

		/**
		 * Get the help text.
		 *
		 * @return The help text
		 */
		var help: String? = null
			private set

		/**
		 * Get the usage override string.
		 *
		 *
		 * If null, then usage information will be generated
		 * automatically.
		 *
		 * @return The usage override
		 */
		var usageOverride: String? = null
			private set

		/**
		 * Set the list of parameters.
		 *
		 * @param parameters The list of parameters
		 * @return The builder
		 */
		fun setParameters(parameters: List<Parameter>): Builder {
			this.parameters = parameters
			return this
		}

		/**
		 * Set the list of permissions.
		 *
		 * @param permissions The list of permissions
		 * @return The builder
		 */
		fun setPermissions(permissions: List<String>): Builder {
			this.permissions = permissions
			return this
		}

		/**
		 * Set the short description.
		 *
		 * @param shortDescription The short description.
		 * @return The builder
		 */
		fun setShortDescription(shortDescription: String?): Builder {
			this.shortDescription = shortDescription
			return this
		}

		/**
		 * Set the help text.
		 *
		 * @param help The help text
		 * @return The builder
		 */
		fun setHelp(help: String?): Builder {
			this.help = help
			return this
		}

		/**
		 * Set the usage override string.
		 *
		 *
		 * If null, then usage information will be generated
		 * automatically.
		 *
		 * @param usageOverride The usage override
		 * @return The builder
		 */
		fun setUsageOverride(usageOverride: String?): Builder {
			this.usageOverride = usageOverride
			return this
		}

		/**
		 * Build an instance of the description.
		 *
		 * @return The description
		 */
		fun build(): ImmutableDescription {
			return ImmutableDescription(parameters, permissions, shortDescription, help, usageOverride)
		}
	}
}
