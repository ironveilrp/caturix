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

/**
 * An immutable implementation of [Parameter].
 */
class ImmutableParameter private constructor(
	override val name: String,
	override val optionType: OptionType,
	override val defaultValue: List<String>
) : Parameter {
	/**
	 * Creates instances of [ImmutableParameter].
	 *
	 *
	 * By default, the default value will be an empty list.
	 */
	class Builder {
		/**
		 * Get the name of the parameter.
		 *
		 * @return The name of the parameter
		 */
		var name: String? = null
			private set

		/**
		 * Get the type of parameter.
		 *
		 * @return The type of parameter
		 */
		var optionType: OptionType? = null
			private set

		/**
		 * Get the default value as a list of arguments.
		 *
		 *
		 * An empty list implies that there is no default value.
		 *
		 * @return The default value (one value) as a list
		 */
		var defaultValue: List<String> = emptyList()
			private set

		/**
		 * Set the name of the parameter.
		 *
		 * @param name The name of the parameter
		 * @return The builder
		 */
		fun setName(name: String): Builder {
			this.name = name
			return this
		}

		/**
		 * Set the type of parameter.
		 *
		 * @param optionType The type of parameter
		 * @return The builder
		 */
		fun setOptionType(optionType: OptionType): Builder {
			this.optionType = optionType
			return this
		}

		/**
		 * Set the default value as a list of arguments.
		 *
		 *
		 * An empty list implies that there is no default value.
		 *
		 * @param defaultValue The default value (one value) as a list
		 * @return The builder
		 */
		fun setDefaultValue(defaultValue: List<String>): Builder {
			this.defaultValue = defaultValue
			return this
		}

		/**
		 * Create an instance.
		 *
		 *
		 * Neither `name` nor `optionType` can be null.
		 *
		 * @return The instance
		 */
		fun build(): ImmutableParameter {
			checkNotNull(name)
			checkNotNull(optionType)
			return ImmutableParameter(name!!, optionType!!, defaultValue)
		}
	}
}
