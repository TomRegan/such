package su.ch.framework

import su.ch.annotation.Experimental
import su.ch.annotation.Pure

@Experimental interface Monoid<A, R : Semigroup<A, R>> : Semigroup<A, R> {

    @Pure fun id(): R
}