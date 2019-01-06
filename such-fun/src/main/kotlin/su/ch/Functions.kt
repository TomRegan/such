package su.ch

import su.ch.annotation.Pure
import java.util.function.Function

object Functions {

    @JvmStatic fun <A> id() = FunctionIdentity<A>()

    class FunctionIdentity<A> : Function<A, A> {

        @Pure override fun apply(a: A): A = a

        @Pure override fun equals(other: Any?): Boolean = other is FunctionIdentity<*>

        @Pure override fun hashCode(): Int = 0
    }

    @JvmStatic fun <A, B, C> compose(f: Function<in A, out B>, g: Function<in B, out C>): Function<A, C> = FunctionComposition(g, f)

    class FunctionComposition<A, B, C> internal constructor(
            val g: Function<in B, out C>,
            val f: Function<in A, out B>)
        : Function<A, C> {

        @Pure override fun apply(a: A): C = g.apply(f.apply(a))

        @Pure override fun equals(other: Any?): Boolean =
                other is FunctionComposition<*, *, *>
                        && f == other.f
                        && g == other.g

        @Pure override fun hashCode(): Int = f.hashCode() xor g.hashCode()
    }

    @JvmStatic fun <A, B, C> ap(f: Function<C, A>, g: Function<C, Function<A, B>>): Function<C, B> = FunctionAp(f, g)

    class FunctionAp<A, B, C>(val f: Function<C, A>, val g: Function<C, Function<A, B>>): Function<C, B> {

        @Pure override fun apply(c: C): B = compose(f, g.apply(c)).apply(c)

        @Pure override fun equals(other: Any?): Boolean =
            other is FunctionAp<*, *, *>
                    && f == other.f
                    && g == other.g

        @Pure override fun hashCode(): Int = f.hashCode() xor g.hashCode()

    }
}