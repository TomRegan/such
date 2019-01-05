package su.ch

import su.ch.annotation.Pure
import java.util.function.Function


class Functions private constructor() {

    companion object {

        @JvmStatic fun <A> id() = FunctionIdentity<A>()

        @JvmStatic fun <A, B, C> compose(f: Function<in A, out B>, g: Function<in B, out C>): Function<A, C> =
                FunctionComposition(g, f)

        @JvmStatic fun <A, B, C> ap(f: Function<C, A>, g: Function<C, Function<A, B>>): Function<C, B> =
                Function { it -> compose(f, g.apply(it)).apply(it) }
    }

    class FunctionIdentity<A> : Function<A, A> {

        @Pure
        override fun apply(a: A): A = a

        @Pure
        override fun equals(other: Any?): Boolean = other is FunctionIdentity<*>

        @Pure
        override fun hashCode(): Int = 0
    }

    class FunctionComposition<A, B, C> internal constructor(val g: Function<in B, out C>, val f: Function<in A, out B>) : Function<A, C> {

        @Pure
        override fun apply(a: A): C = g.apply(f.apply(a))

        @Pure
        override fun equals(other: Any?): Boolean =
                other is FunctionComposition<*, *, *>
                        && f == other.f
                        && g == other.g

        @Pure
        override fun hashCode(): Int = f.hashCode() xor g.hashCode()
    }
}