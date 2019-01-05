/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package su.ch

import su.ch.annotation.Beta
import java.util.Arrays
import java.util.Objects

/**
 * Such.ToString is a helper for printing a String representation of a category
 */
@Beta
class ToString internal constructor(private val className: String) {
    private val holderHead = ValueHolder()
    private var holderTail = holderHead
    private var omitNullValues = false

    constructor(cls: Class<*>) : this(cls.simpleName)

    constructor(obj: Any) : this(ToClassName(obj))

    /**
     * Configures the [ToString] so [.toString] will ignore properties with
     * null value. The order of calling this method, relative to the `add()`/`addValue()` methods, is not significant.
     *
     * @since 18.0 (since 12.0 as `Objects.ToString.omitNullValues()`).
     */
    fun omitNullValues(): ToString {

        omitNullValues = true
        return this
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format. If `value` is `null`, the string `"null"` is used, unless [ ][.omitNullValues] is called, in which case this name/value pair will not be added.
     */
    fun add(name: String, value: Any?): ToString {

        return addHolder(name, value!!)
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Boolean): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Char): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Double): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Float): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Int): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds a name/value pair to the formatted output in `name=value` format.
     */
    fun add(name: String, value: Long): ToString {

        return addHolder(name, value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     */
    fun addValue(value: Any?): ToString {

        return addHolder(value!!)
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value
     * a readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Boolean): ToString {

        return addHolder(value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Char): ToString {

        return addHolder(value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Double): ToString {

        return addHolder(value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Float): ToString {

        return addHolder(value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Int): ToString {

        return addHolder(value.toString())
    }

    /**
     * Adds an unnamed value to the formatted output.
     *
     *
     * It is strongly encouraged to use [.add] instead and give value a
     * readable name.
     *
     * @since 18.0 (since 11.0 as `Objects.ToString.addValue()`).
     */
    fun addValue(value: Long): ToString {

        return addHolder(value.toString())
    }

    /**
     * After calling this method, you can keep adding more properties to later call
     * toString() again and get a more complete representation of the same object; but
     * properties cannot be removed, so this only allows limited reuse of the helper instance.
     * The helper allows duplication of properties (multiple name/value pairs with the same name
     * can be added).
     */
    override fun toString(): String {
        // create a copy to keep it consistent in case value changes
        val omitNullValuesSnapshot = omitNullValues
        var nextSeparator = ""
        val builder = StringBuilder(32).append(className).append('(')
        var valueHolder = holderHead.next
        while (valueHolder != null) {
            val value = valueHolder.value
            if (!omitNullValuesSnapshot || value != null) {
                builder.append(nextSeparator)
                nextSeparator = ", "

                if (valueHolder.name != null) {
                    builder.append(valueHolder.name).append('=')
                }
                if (value != null && value.javaClass.isArray) {
                    val objectArray = arrayOf(value)
                    val arrayString = Arrays.deepToString(objectArray)
                    builder.append(arrayString, 1, arrayString.length - 1)
                } else {
                    if (value != null && value.javaClass.isAssignableFrom(String::class.java)) {
                        builder.append("\"")
                        builder.append(value)
                        builder.append("\"")
                    } else {
                        builder.append(value)
                    }
                    if (value != null) {
                        builder.append(":")
                        builder.append(ToClassName.toClassName(value))
                    }
                }
            }
            valueHolder = valueHolder.next
        }
        return builder.append(')').toString()
    }

    private fun addHolder(): ValueHolder {

        val valueHolder = ValueHolder()
        holderTail.next = valueHolder
        holderTail = holderTail.next!!
        return valueHolder
    }

    private fun addHolder(value: Any): ToString {

        val valueHolder = addHolder()
        valueHolder.value = value
        return this
    }

    private fun addHolder(name: String, value: Any): ToString {

        val valueHolder = addHolder()
        valueHolder.value = value
        valueHolder.name = Objects.requireNonNull(name)
        return this
    }

    private class ValueHolder {
        internal var name: String? = null
        internal var value: Any? = null
        internal var next: ValueHolder? = null
    }
}
