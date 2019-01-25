@file:Suppress("UNCHECKED_CAST")

package su.ch.framework

import su.ch.annotation.Pure
import java.util.function.Function

abstract class Monad<A, M : Monad<A, M>> protected constructor(value: A) : Applicative<A, Monad<*, *>>(value) {

    @Pure open fun <V> flatMap(mapper: Function<A, Monad<V, *>>): M =
            mapper.apply(value) as M

    @Pure open fun get(): A = value

}
