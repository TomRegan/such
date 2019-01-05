package su.ch.framework

import su.ch.annotation.Pure
import java.util.function.Function

/**
 * A [Category] that can be mapped over. It is made up of an object and a morphism, represented
 * by the enclosed value, and the method `map` respectively.
 *
 * A category *C* consists of two classes, one of objects and the other of morphisms. There are two
 * objects associated with every morphism, the domain (source) and the codomain (target).
 *
 * ```
 * Object: C(●)
 * Domain:   ●
 * Codomain:        ○
 * Morphism: C(● -> ○)
 * ```
 *
 * In the Java implementation, Objects are naturally represented by a `java.lang.Object`
 * member; Morphisms are a little more abstract, but they can be thought of as the difference
 * between the argument and the return type in the signature of [Functor#map].
 * The morphism is not the function passed into map. The morphism declares a domain and codomain,
 * it doesn't define how to get from the domain to the codomain.
 *
 * # Functor Laws
 *
 * ### First Law
 *
 * The first functor law states that if we map the id function over a functor, the functor that
 * we get back should be the same as the original functor (identity).
 *
 * ```
 * let f := ● -> ●
 * in   F(●) -> f = F(●)
 * ```
 *
 * ### Second Law
 *
 * The second law says that composing two functions and then mapping the resulting function over
 * a functor should be the same as first mapping one function over the functor and then mapping
 * the other one (composition).
 *
 * ```
 * let f := ● -> ○
 * let g := ○ -> △
 * ∴   f . g = ● -> (○ -> △) = ● -> △
 * in   F(●) -> f . g = F(△)
 * ```
 *
 * @param <A> The 'domain' value type
 * @param <R> The 'codomain' object type.
 * @author Tom Regan
 * @since 1.0.0
 */
abstract class Functor<out A, R : Functor<*, R>> protected constructor(value: A) : Category<A>(value) {

    /**
     * Takes a function, `mapper`, and applies it to the value contained by this [Functor].
     * The result is a brand new `Functor` containing the value returned by `mapper`.
     * The new `Functor` instance need not be of the same type as the original.
     *
     * # Implementation Details
     *
     * [Functor.map] expects a [Function] which accepts an input that is the same
     * type as the value this functor is mapping over. You should return a new Functor instance
     * appropriate to the type of the return value.
     *
     * @param <B> The 'codomain' value type.
     */
    @Pure
    abstract fun <B> map(mapper: Function<B, *>): R

}
