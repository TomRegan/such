@file:Suppress("UNCHECKED_CAST")

package su.ch

import su.ch.annotation.Beta
import su.ch.framework.Applicative
import su.ch.framework.Monad
import java.util.function.Function

@Beta
sealed class Maybe<A> constructor(value: A) : Monad<A, Maybe<A>>(value) {

    override fun <A> create(value: A): Monad<*, *> {
        return Just(value)
    }

    class Just<A>(value: A) : Maybe<A>(value)

    class Nothing<A> : Maybe<A>(Unit.Unit as A) {

        override fun <B> map(mapper: Function<B, *>): Maybe<A> = Nothing()

        override fun <B> apply(functor: Applicative<B, Monad<*, *>>): Maybe<A> = Nothing()

        override fun <B> flatMap(mapper: Function<A, Monad<B, *>>): Maybe<A> = Nothing()

        override fun toString(): String = ToString(this).toString()
    }

    companion object {
        @JvmStatic fun <A> Just(value: A?): Maybe<A> =
                when (value) {
                    null -> Maybe.Nothing()
                    else -> Maybe.Just(value)
                }

        @JvmStatic fun <A> Nothing() : Nothing<A> = Maybe.Nothing()
    }

}