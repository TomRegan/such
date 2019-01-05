package su.ch.framework

import su.ch.annotation.Experimental

@Experimental
class Equal<T> {

    fun equals(a: T, b: T): Boolean = a?.equals(b) ?: b === null;
}