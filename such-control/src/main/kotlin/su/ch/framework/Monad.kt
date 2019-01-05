@file:Suppress("UNCHECKED_CAST")

package su.ch.framework

import su.ch.annotation.Pure
import java.util.function.Function

abstract class Monad<A, R : Monad<*, R>> protected constructor(value: A) : Applicative<A, Monad<*, *>>(value) {

    @Pure
    open fun <V> flatMap(mapper: Function<A, Monad<V, *>>): R =
            mapper.apply(value) as R

    @Pure
    open fun get(): A = value

}
