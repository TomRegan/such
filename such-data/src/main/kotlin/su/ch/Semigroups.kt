package su.ch

import su.ch.annotation.Experimental
import su.ch.annotation.Pure
import su.ch.framework.Semigroup
import java.util.Objects.hash

@Experimental
class Semigroups {

    abstract class IntegerSemigroup<A : Semigroup<Int, A>> protected constructor(val value: Int) : Semigroup<Int, A> {

        @Pure
        protected fun plus(a: Int, b: Int): Int = a.plus(b)
        @Pure
        protected fun max(a: Int, b: Int): Int = Math.max(a, b)
        @Pure
        protected fun min(a: Int, b: Int): Int = Math.min(a, b)
        @Pure
        protected fun multiply(a: Int, b: Int): Int = a.times(b)

        @Pure
        override fun toString(): String =
                ToString(this)
                        .addValue(value)
                        .toString()

        @Pure
        final override fun equals(other: Any?): Boolean =
                this === other
                        || other != null
                        && other is IntegerSemigroup<*>
                        && value == other.value

        @Pure
        final override fun hashCode(): Int = value
    }

    class IntegerAddition(value: Int) : IntegerSemigroup<IntegerAddition>(value) {
        override fun append(value: Int): IntegerAddition = IntegerAddition(plus(this.value, value))
    }

    class IntegerMultiplication(value: Int) : IntegerSemigroup<IntegerMultiplication>(value) {
        override fun append(value: Int): IntegerMultiplication = IntegerMultiplication(multiply(this.value, value))
    }

    class IntegerMinimum(value: Int) : IntegerSemigroup<IntegerMinimum>(value) {
        override fun append(value: Int): IntegerMinimum = IntegerMinimum(min(this.value, value))
    }

    class IntegerMaximum(value: Int) : IntegerSemigroup<IntegerMaximum>(value) {
        override fun append(value: Int): IntegerMaximum = IntegerMaximum(max(this.value, value))
    }

    abstract class LongSemigroup<A : Semigroup<Long, A>> protected constructor(val value: Long) : Semigroup<Long, A> {

        @Pure
        protected fun plus(a: Long, b: Long): Long = a.plus(b)
        @Pure
        protected fun max(a: Long, b: Long): Long = Math.max(a, b)
        @Pure
        protected fun min(a: Long, b: Long): Long = Math.min(a, b)
        @Pure
        protected fun multiply(a: Long, b: Long): Long = a.times(b)

        @Pure
        override fun toString(): String =
                ToString(this)
                        .addValue(value)
                        .toString()

        @Pure
        final override fun equals(other: Any?): Boolean =
                this === other
                        || other != null
                        && other is LongSemigroup<*>
                        && value == other.value

        override fun hashCode(): Int = hash(value)

    }

    class LongAddition(value: Long) : LongSemigroup<LongAddition>(value) {
        override fun append(value: Long): LongAddition = LongAddition(plus(this.value, value))
    }

    class LongMultiplication(value: Long) : LongSemigroup<LongMultiplication>(value) {
        override fun append(value: Long): LongMultiplication = LongMultiplication(multiply(this.value, value))
    }

    class LongMinimum(value: Long) : LongSemigroup<LongMinimum>(value) {
        override fun append(value: Long): LongMinimum = LongMinimum(min(this.value, value))
    }

    class LongMaximum(value: Long) : LongSemigroup<LongMaximum>(value) {
        override fun append(value: Long): LongMaximum = LongMaximum(max(this.value, value))
    }

    companion object {

        @JvmStatic fun append(a: Int, b: Int): IntegerAddition = IntegerAddition(a).append(b)
        @JvmStatic fun append(a: Long, b: Long): LongAddition = LongAddition(a).append(b)
    }

}