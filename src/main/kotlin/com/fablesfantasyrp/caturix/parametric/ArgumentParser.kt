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

import com.google.common.base.Joiner
import com.fablesfantasyrp.caturix.ImmutableParameter
import com.fablesfantasyrp.caturix.OptionType
import com.fablesfantasyrp.caturix.Parameter
import com.fablesfantasyrp.caturix.argument.*
import com.fablesfantasyrp.caturix.parametric.annotation.Classifier
import com.fablesfantasyrp.caturix.parametric.annotation.Optional
import com.fablesfantasyrp.caturix.parametric.annotation.Switch
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

/**
 * An argument parser takes in a list of tokenized arguments and parses
 * them, converting them into appropriate Java objects using a provided
 * [Injector].
 */
class ArgumentParser private constructor(
	parameters: Map<Parameter, ParameterEntry>,
	userParams: List<Parameter>,
	valueFlags: Set<Char>
) {
	private val parameters: Map<Parameter, ParameterEntry> = parameters.toMap()

	/**
	 * Get a list of parameters that are user-provided and not provided.
	 *
	 * @return A list of user parameters
	 */
	val userParameters: List<Parameter> = userParams.toList()

	/**
	 * Get a list of value flags that have been requested by the parameters.
	 *
	 * @return A list of value flags
	 */
	val valueFlags: Set<Char> = valueFlags.toSet()

	/**
	 * Parse the given arguments into Java objects.
	 *
	 * @param args The tokenized arguments
	 * @param ignoreUnusedFlags Whether unused flags should not throw an exception
	 * @param unusedFlags List of flags that can be unconsumed
	 * @return The list of Java objects
	 * @throws ArgumentException If there is a problem with the provided arguments
	 * @throws ProvisionException If there is a problem with the binding itself
	 */
	/**
	 * Parse the given arguments into Java objects.
	 *
	 * @param args The tokenized arguments
	 * @return The list of Java objects
	 * @throws ArgumentException If there is a problem with the provided arguments
	 * @throws ProvisionException If there is a problem with the binding itself
	 */
	@JvmOverloads
	@Throws(ArgumentException::class, ProvisionException::class)
	fun parseArguments(
		args: CommandArgs,
		ignoreUnusedFlags: Boolean = false,
		unusedFlags: Set<Char> = emptySet()
	): Array<Any?> {
		val parsedObjects = ArrayList<Any?>(parameters.size)

		var i = -1 // Simulate the old for loop TODO: Maybe pretty this up?
		for (entry in parameters.values) {
			i++
			val optionType: OptionType = entry.parameter.optionType
			val argsForParameter  = optionType.transform(args)

			try {
				parsedObjects[i] = entry.binding.provider.get(argsForParameter, entry.modifiers)
			} catch (e: ArgumentParseException) {
				throw ArgumentParseException(e.message, e, entry.parameter)
			} catch (e: MissingArgumentException) {
				if (!optionType.isOptional) {
					throw MissingArgumentException(e, entry.parameter)
				}

				parsedObjects[i] = getDefaultValue(entry, args)
			}
		}

		// Check for unused arguments
		checkUnconsumed(args, ignoreUnusedFlags, unusedFlags)

		return parsedObjects.toArray()
	}

	/**
	 * Parse the given arguments into a list of suggestions.
	 *
	 * @param arguments What the user has typed so far
	 * @param locals The namespace to send to providers
	 * @return The list of suggestions
	 */
	fun parseSuggestions(arguments: String, locals: Namespace): List<String> {
		val split: Array<String> = CommandContext.split(arguments)

		val argId = split.size - 1
		val arg = split[argId]

		if (argId >= userParameters.size) return emptyList()
		val parameter = userParameters[argId] ?: return emptyList()

		val entry = parameters[parameter]!!
		return entry.binding.provider.getSuggestions(arg, locals, entry.modifiers)
	}

	private fun getDefaultValue(entry: ParameterEntry, arguments: CommandArgs): Any? {
		val provider: Provider<*> = entry.binding.provider

		val defaultValue: List<String> = entry.parameter.defaultValue
		return if (defaultValue.isEmpty()) {
			null
		} else {
			try {
				provider.get(Arguments.copyOf(
					defaultValue,
					arguments.flags,
					arguments.namespace,
				), entry.modifiers)
			} catch (e: ArgumentException) {
				throw IllegalParameterException(
					("No value was specified for the '" + entry.parameter.name + "' parameter " +
							"so the default value '" + Joiner.on(" ")
						.join(defaultValue)).toString() + "' was used, but this value doesn't work due to an error: " + e.message
				)
			} catch (e: ProvisionException) {
				throw IllegalParameterException(
					("No value was specified for the '" + entry.parameter.name + "' parameter " +
							"so the default value '" + Joiner.on(" ")
						.join(defaultValue)).toString() + "' was used, but this value doesn't work due to an error: " + e.message
				)
			}
		}
	}

	@Throws(UnusedArgumentException::class)
	private fun checkUnconsumed(arguments: CommandArgs, ignoreUnusedFlags: Boolean, unusedFlags: Set<Char>) {
		val unconsumedArguments: MutableList<String> = ArrayList()

		if (!ignoreUnusedFlags) {
			var unconsumedFlags: MutableSet<Char>? = null

			for (flag in arguments.flags.keys) {
				var found = false

				if (unusedFlags.contains(flag)) {
					break
				}

				for (parameter in parameters.values) {
					val paramFlag: Char? = parameter.parameter?.optionType?.flag
					if (paramFlag != null && flag == paramFlag) {
						found = true
						break
					}
				}

				if (!found) {
					if (unconsumedFlags == null) {
						unconsumedFlags = HashSet()
					}
					unconsumedFlags.add(flag)
				}
			}

			if (unconsumedFlags != null) {
				for (flag in unconsumedFlags) {
					unconsumedArguments.add("-$flag")
				}
			}
		}

		while (true) {
			try {
				unconsumedArguments.add(arguments.next())
			} catch (ignored: MissingArgumentException) {
				break
			}
		}

		if (!unconsumedArguments.isEmpty()) {
			throw UnusedArgumentException(Joiner.on(" ").join(unconsumedArguments))
		}
	}

	/**
	 * Builds instances of ArgumentParser.
	 */
	class Builder(private val injector: Injector) {
		private val parameters: MutableMap<Parameter, ParameterEntry> = LinkedHashMap() // Need to preserve order at all times
		private val userProvidedParameters: MutableList<Parameter> = ArrayList()
		private val valueFlags: MutableSet<Char> = HashSet()
		private var seenOptionalParameter = false

		/**
		 * Add a parameter to parse.
		 *
		 * @param type The type of the parameter
		 * @throws IllegalParameterException If there is a problem with the parameter
		 */
		@JvmOverloads
		@Throws(IllegalParameterException::class)
		fun addParameter(
			type: Type,
			annotations: List<Annotation> = emptyList()
		) {
			val index = parameters.size
			var optionType: OptionType? = null
			var defaultValue: List<String> = listOf()
			var classifier: Annotation? = null
			val modifiers: MutableList<Annotation> = ArrayList()

			for (annotation in annotations) {
				if (annotation.javaClass.getAnnotation(Classifier::class.java) != null) {
					classifier = annotation
				} else {
					modifiers.add(annotation)

					if (annotation is Switch) {
						if (optionType != null) {
							throw IllegalParameterException("Both @Optional and @Switch were found on the same element for parameter #$index")
						}

						optionType =
							if ((type === Boolean::class.javaPrimitiveType || type === Boolean::class.java)) OptionType.flag(
								annotation.value
							) else OptionType.valueFlag(
								annotation.value
							)
					} else if (annotation is Optional) {
						if (optionType != null) {
							throw IllegalParameterException("Both @Optional and @Switch were found on the same element for parameter #$index")
						}

						seenOptionalParameter = true

						optionType = OptionType.optionalPositional()

						val value = annotation.value
						if (value.isNotEmpty()) {
							defaultValue = value.toList()
						}
					}
				}
			}

			if (optionType == null) {
				optionType = OptionType.positional()
			}

			if (seenOptionalParameter && !optionType.isOptional) {
				throw IllegalParameterException("An non-optional parameter followed an optional parameter at #$index")
			}

			val builder = ImmutableParameter.Builder()
			builder.setName(getFriendlyName(type, classifier, index))
			builder.setOptionType(optionType)
			builder.setDefaultValue(defaultValue)
			val parameter = builder.build()

			val key: Key<*> = Key.get<Any>(type, classifier?.javaClass)
			val binding = injector.getBinding(key)
				?: throw IllegalParameterException("Can't finding a binding for the parameter type '$type'")

			val entry = ParameterEntry(parameter, key, binding, modifiers)

			if (optionType.isValueFlag) {
				valueFlags.add(optionType.flag!!)
			}

			if (!binding.provider.isProvided) {
				userProvidedParameters.add(parameter)
			}

			parameters[parameter] = entry
		}

		/**
		 * Create a new argument parser.
		 *
		 * @return A new argument parser
		 */
		fun build(): ArgumentParser {
			return ArgumentParser(parameters, userProvidedParameters, valueFlags)
		}

		companion object {
			private fun getFriendlyName(type: Type, classifier: Annotation?, index: Int): String {
				return classifier?.javaClass?.simpleName?.lowercase(Locale.getDefault())
					?: if (type is Class<*>) type.simpleName.lowercase(Locale.getDefault()) else "unknown$index"
			}
		}
	}

	private class ParameterEntry(
		val parameter: Parameter,
		val key: Key<*>,
		val binding: Binding<*>,
		val modifiers: List<Annotation>
	)
}
