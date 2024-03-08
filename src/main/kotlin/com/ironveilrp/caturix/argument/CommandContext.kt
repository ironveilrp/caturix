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
package com.ironveilrp.caturix.argument

import com.ironveilrp.caturix.CommandException

class CommandContext(
	args: Array<String>,
	expectedValueFlags: Set<Char>,
	allowHangingFlag: Boolean,
	namespace: Namespace?
) {
	val command: String? = args[0]
	private val parsedArgs: List<String>
	private val originalArgIndices: List<Int>
	private val originalArgs: Array<String> = args
	val flags: Set<Char>
	val valueFlags: Map<Char, String>
	val flagsMap: Map<Char, String>
	val suggestionContext: SuggestionContext
	val namespace: Namespace

	constructor(args: String) : this(args.split(" ".toRegex()).toTypedArray(), emptySet())

	constructor(args: String, valueFlags: Set<Char>) : this(args.split(" ".toRegex()).toTypedArray(), valueFlags)

	constructor(args: String, valueFlags: Set<Char>, allowHangingFlag: Boolean) : this(
		args.split(" ".toRegex()).toTypedArray(), valueFlags, allowHangingFlag, Namespace()
	)

	constructor(args: Array<String>, valueFlags: Set<Char>) : this(args, valueFlags, false, null)

	/**
	 * Parse the given array of arguments.
	 *
	 *
	 * Empty arguments are removed from the list of arguments.
	 *
	 * @param args             an array with arguments
	 * @param expectedValueFlags       a set containing all value flags (pass null to disable value flag parsing)
	 * @param allowHangingFlag true if hanging flags are allowed
	 * @param namespace        the locals, null to create empty one
	 * @throws CommandException thrown on a parsing error
	 */
	init {
		this.namespace = namespace ?: Namespace()
		var isHanging = false
		var suggestionContext: SuggestionContext = SuggestionContext.hangingValue()

		// Eliminate empty args and combine multiword args first
		val argIndexList: MutableList<Int> = ArrayList(args.size)
		val argList: MutableList<String> = ArrayList(args.size)
		var i = 1
		while (i < args.size) {
			isHanging = false

			var arg = args[i]
			if (arg.isEmpty()) {
				isHanging = true
				++i
				continue
			}

			argIndexList.add(i)

			when (arg[0]) {
				'\'', '"' -> {
					val build = StringBuilder()
					val quotedChar = arg[0]
					var endIndex = i
					while (endIndex < args.size) {
						val arg2 = args[endIndex]
						if (arg2!![arg2.length - 1] == quotedChar && arg2.length > 1) {
							if (endIndex != i) build.append(' ')
							build.append(arg2.substring(if (endIndex == i) 1 else 0, arg2.length - 1))
							break
						} else if (endIndex == i) {
							build.append(arg2.substring(1))
						} else {
							build.append(' ').append(arg2)
						}
						++endIndex
					}

					if (endIndex < args.size) {
						arg = build.toString()
						i = endIndex
					}

					// In case there is an empty quoted string
					if (arg.isEmpty()) {
						++i
						continue
					}
				}
			}
			argList.add(arg)
			++i
		}

		// Then flags
		val originalArgIndices: MutableList<Int> = ArrayList(argIndexList.size)
		val parsedArgs: MutableList<String> = ArrayList(argList.size)
		val valueFlags: MutableMap<Char, String> = HashMap()
		val booleanFlags: MutableList<Char> = ArrayList()

		var nextArg = 0
		while (nextArg < argList.size) {
			// Fetch argument
			val arg = argList[nextArg++]
			suggestionContext = SuggestionContext.hangingValue()

			// Not a flag?
			if (arg!![0] != '-' || arg.length == 1 || !arg.matches("^-[a-zA-Z\\?]+$".toRegex())) {
				if (!isHanging) {
					suggestionContext = SuggestionContext.lastValue()
				}

				originalArgIndices.add(argIndexList[nextArg - 1])
				parsedArgs.add(arg)
				continue
			}

			// Handle flag parsing terminator --
			if (arg == "--") {
				while (nextArg < argList.size) {
					originalArgIndices.add(argIndexList[nextArg])
					parsedArgs.add(argList[nextArg++])
				}
				break
			}

			// Go through the flag characters
			for (i in 1 until arg.length) {
				val flagName = arg[i]

				if (expectedValueFlags.contains(flagName)) {
					if (valueFlags.containsKey(flagName)) {
						throw CommandException("Value flag '$flagName' already given")
					}

					if (nextArg >= argList.size) {
						if (allowHangingFlag) {
							suggestionContext = SuggestionContext.Companion.flag(flagName)
							break
						} else {
							throw CommandException("No value specified for the '-$flagName' flag.")
						}
					}

					// If it is a value flag, read another argument and add it
					valueFlags[flagName] = argList[nextArg++]
					if (!isHanging) {
						suggestionContext = SuggestionContext.Companion.flag(flagName)
					}
				} else {
					booleanFlags.add(flagName)
				}
			}
		}

		val allFlags = HashMap<Char, String>()
		allFlags.putAll(valueFlags)

		for (flag in booleanFlags) {
			allFlags[flag] = "true"
		}

		this.parsedArgs = parsedArgs.toList()
		this.originalArgIndices = originalArgIndices.toList()
		this.flags = booleanFlags.toSet()
		this.valueFlags = valueFlags.toMap()
		this.flagsMap = allFlags
		this.suggestionContext = suggestionContext
	}

	fun matches(command: String?): Boolean {
		return this.command.equals(command, ignoreCase = true)
	}

	fun getString(index: Int): String {
		return parsedArgs[index]
	}

	fun getString(index: Int, def: String?): String {
		return if (index < parsedArgs.size) parsedArgs[index] else def!!
	}

	fun getJoinedStrings(initialIndex: Int): String {
		var index = initialIndex
		index = originalArgIndices[index]
		val buffer = StringBuilder(originalArgs[index])
		for (i in index + 1 until originalArgs.size) {
			buffer.append(" ").append(originalArgs[i])
		}
		return buffer.toString()
	}

	fun getRemainingString(start: Int): String {
		return getString(start, parsedArgs.size - 1)
	}

	fun getString(start: Int, end: Int): String {
		val buffer = StringBuilder(parsedArgs[start])
		for (i in start + 1 until end + 1) {
			buffer.append(" ").append(parsedArgs[i])
		}
		return buffer.toString()
	}

	@Throws(NumberFormatException::class)
	fun getInteger(index: Int): Int {
		return parsedArgs[index].toInt()
	}

	@Throws(NumberFormatException::class)
	fun getInteger(index: Int, def: Int): Int {
		return if (index < parsedArgs.size) parsedArgs[index].toInt() else def
	}

	@Throws(NumberFormatException::class)
	fun getDouble(index: Int): Double {
		return parsedArgs[index].toDouble()
	}

	@Throws(NumberFormatException::class)
	fun getDouble(index: Int, def: Double): Double {
		return if (index < parsedArgs.size) parsedArgs[index].toDouble() else def
	}

	fun getSlice(index: Int): Array<String?> {
		val slice = arrayOfNulls<String>(originalArgs.size - index)
		System.arraycopy(originalArgs, index, slice, 0, originalArgs.size - index)
		return slice
	}

	fun getPaddedSlice(index: Int, padding: Int): Array<String?> {
		val slice = arrayOfNulls<String>(originalArgs.size - index + padding)
		System.arraycopy(originalArgs, index, slice, padding, originalArgs.size - index)
		return slice
	}

	fun getParsedSlice(index: Int): Array<String?> {
		val slice = arrayOfNulls<String>(parsedArgs.size - index)
		System.arraycopy(parsedArgs.toTypedArray<String>(), index, slice, 0, parsedArgs.size - index)
		return slice
	}

	fun getParsedPaddedSlice(index: Int, padding: Int): Array<String?> {
		val slice = arrayOfNulls<String>(parsedArgs.size - index + padding)
		System.arraycopy(parsedArgs.toTypedArray<String>(), index, slice, padding, parsedArgs.size - index)
		return slice
	}

	fun hasFlag(ch: Char): Boolean {
		return flags.contains(ch) || valueFlags.containsKey(ch)
	}

	fun getFlag(ch: Char): String? {
		return valueFlags[ch]
	}

	fun getFlag(ch: Char, def: String): String {
		val value = valueFlags[ch] ?: return def

		return value
	}

	@Throws(NumberFormatException::class)
	fun getFlagInteger(ch: Char): Int {
		return valueFlags[ch]!!.toInt()
	}

	@Throws(NumberFormatException::class)
	fun getFlagInteger(ch: Char, def: Int): Int {
		val value = valueFlags[ch] ?: return def

		return value.toInt()
	}

	@Throws(NumberFormatException::class)
	fun getFlagDouble(ch: Char): Double {
		return valueFlags[ch]!!.toDouble()
	}

	@Throws(NumberFormatException::class)
	fun getFlagDouble(ch: Char, def: Double): Double {
		val value = valueFlags[ch] ?: return def

		return value.toDouble()
	}

	fun argsLength(): Int {
		return parsedArgs.size
	}

	class Builder {
		private var arguments = arrayOf<String>()
		var expectedValueFlags: Set<Char> = setOf()
			private set
		var isAllowHangingFlag: Boolean = false
			private set
		var namespace: Namespace = Namespace()
			private set

		fun getArguments(): Array<String> {
			return arguments.copyOf()
		}

		fun setArguments(arguments: Array<String>) = apply {
			this.arguments = arrayOf("_", *arguments)
		}

		fun setArguments(arguments: String) = apply {
			setArguments(split(arguments))
		}

		fun setCommandAndArguments(arguments: Array<String>) = apply {
			this.arguments = arguments.copyOf()
		}

		fun setCommandAndArguments(arguments: String) = apply {
			setCommandAndArguments(split(arguments))
		}

		fun setExpectedValueFlags(expectedValueFlags: Set<Char>) = apply {
			this.expectedValueFlags = expectedValueFlags.toSet()
		}

		fun setAllowHangingFlag(allowHangingFlag: Boolean) = apply {
			this.isAllowHangingFlag = allowHangingFlag
		}

		fun setNamespace(namespace: Namespace) = apply {
			this.namespace = namespace
		}

		@Throws(CommandException::class)
		fun build(): CommandContext {
			return CommandContext(arguments, expectedValueFlags, isAllowHangingFlag, namespace)
		}
	}

	companion object {
		fun split(args: String): Array<String> {
			return args.split(" ".toRegex()).toTypedArray()
		}
	}
}
