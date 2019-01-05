package su.ch.framework

import su.ch.ToString
import su.ch.annotation.Pure
import java.util.Objects.hash

/**
 * ```
 * Object: C(●)
 * Domain:   ●
 * ```
 * @param <T> The 'domain' value type
 * @author Tom Regan
 * @since 1.0.0
 */
abstract class Category<out T> internal constructor(protected val value: T) {

    @Pure
    override fun toString(): String =
            ToString(this)
                    .addValue(value)
                    .toString()


    @Pure
    final override fun equals(other: Any?): Boolean =
            other != null
                    && other is Category<*>
                    && value == other.value


    @Pure
    final override fun hashCode(): Int = hash(value)
}
