package su.ch.framework

import su.ch.annotation.Experimental
import su.ch.annotation.Pure

@Experimental interface Semigroup<A, R : Semigroup<A, R>> {

    @Pure fun append(value: A): R
}