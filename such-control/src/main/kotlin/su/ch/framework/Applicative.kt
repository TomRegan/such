@file:Suppress("UNCHECKED_CAST")

package su.ch.framework

import su.ch.annotation.Pure

import java.util.function.Function

/**
 * A [Category] that can be mapped over by a function.
 *
 * A functor with application, providing operations to
 *
 *  * wrap pure expressions (create)
 *  * sequence computations and combine their results (apply)
 *
 * # Applicative Laws
 *
 * ### First Law
 *
 * The first law of applicative functors states that if we apply the pure identity function,
 * the applicative functor we get back should be the same as the original applicative
 * functor; the Identity law.
 *
 * ```
 * let f := ● -> ●
 * in A(f) -> ●  ≡  A(●)
 * ```
 *
 * ### Second Law
 *
 * The second law of applicative functors states that if we apply a pure function to a pure
 * value is the same as applying the function to the value and then using [.create] on the
 * result.
 *
 * ```
 * let f := ● -> ○
 * in A(f) -> A(●)  ≡  A(f(●))
 * ```
 *
 * Intuitively, applying a non-effectful function to a non-effectful argument in an
 * effectful context is the same as just applying the function to the argument and then
 * injecting the result into the context with [.create]; the law of Homomorphism.
 *
 * ### Sidebar: Values and effects
 *
 * In a pure `Applicative`, A(●), there is a value, ●. Anything else is the effect; for
 * example, in the Maybe applicative, Just represents the result of a non-effectful computation,
 * and Nothing is the effect whereby there is no value and the computation is aborted.
 *
 * This is not a feature of the Applicative interface however, it is a property of the data
 * type, for instance `Maybe`.
 *
 * ### Third Law
 *
 * The third law of applicative functors states that applying a morphism to a pure value is
 * the same as applying [.create] to the morphism; the law of Interchange.
 *
 * ```
 * let f := ● -> ○
 * let g := (● -> ○) -> ○
 * in A(f) -> A(●)  ≡  A(g) -> A(f)
 * ```
 *
 * Intuitively, this says that when evaluating the application of an effectful function to
 * a pure argument the order in which we evaluate the function and its argument doesn't
 * matter.
 *
 * The fourth law of applicative functors states that if apply is used to compose morphisms
 * the composition is associative, like plain function composition; the law of FunctionComposition.
 *
 * ```
 * let f := ● -> ○
 * let g := ○ -> △
 * ∴ f . g  ≡  ● -> (○ -> △)  ≡  ● -> △
 * in A(f) -> (A(g) -> A(○))  ≡  A(A(f) -> (A(g) -> A(○)))
 * ```
 *
 * ### Applicative Laws in Respect of Functors
 *
 * Mapping a function over a functor is equivalent to lifting the function into an applicative
 * functor using [.create], and then applying it to the value using apply.
 *
 * ```
 * let f := ● -> ○
 * in  F(●) -> f  ≡  A(f) -> F(●)
 * ```
 *
 * @param <T> The 'domain' value type.
 * @author Tom Regan
 * @since 1.0.0
 */
abstract class Applicative<A, R : Applicative<*, R>> protected constructor(value: A) : Functor<A, Applicative<*, *>>(value) {

    @Pure
    abstract fun <A> create(value: A): R

    @Pure
    open fun <B> apply(functor : Applicative<B, R>): R =
            create((value as Function<B, *>).apply(functor.value))

    override fun <B> map(mapper: Function<B, *>): R = create(mapper).apply(this)

}
