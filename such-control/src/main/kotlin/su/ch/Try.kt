@file:Suppress("UNCHECKED_CAST")

package su.ch

import su.ch.Either.Companion.partitionLeft
import su.ch.Either.Left
import su.ch.Either.Right
import su.ch.Maybe.Just
import su.ch.Try.Success.Success0
import su.ch.annotation.Experimental
import su.ch.framework.Monad
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

@Experimental
sealed class Try<A>(value: A, val functions: List<Either<BiFunction<Exception, A, *>, Function<A, *>>> = listOf()) : Monad<A, Try<A>>(value) {

    override fun <A> create(value: A): Success<A> = Success0(value)

    fun doAction(function: Function<A, *>): Success0<A> =
            Success0(value, functions + listOf(Right<BiFunction<Exception, A, *>, Function<A, *>>(function)))

    open fun isSuccess(): Boolean = when {
        functions.isEmpty() -> true
        else -> {
            val (failure, success) = partitionLeft(functions)
            Do(value, failure, success).isSuccess()
        }
    }

    fun ifSuccessful(consumer: Consumer<A>): Boolean {
        if (isSuccess()) {
            map(Function { it: A -> consumer.accept(it); it })
            return true
        }
        return false
    }

    fun ifFailed(consumer: Consumer<A>): Boolean {
        if (!isSuccess()) {
            map(Function { it: A -> consumer.accept(it); it })
            return false
        }
        return true
    }

    class Do<A>(value: A, val failure: List<Maybe<Left<*, *>>>, val success: List<Maybe<Right<*, *>>>)
        : Try<A>(value) {

        override fun isSuccess(): Boolean = when {
            success.isEmpty() -> true
            else -> {
                try {
                    success.first().get().apply(this)
                    Do(value, failure, success.drop(1)).isSuccess()
                } catch (e: Exception) {
                    Failure(value, e, failure.drop(success.size - 1).reversed()).isSuccess()
                }
            }
        }
    }

    sealed class Success<A>(value: A, functions: List<Either<BiFunction<Exception, A, *>, Function<A, *>>> = listOf())
        : Try<A>(value, functions) {

        class Success0<A>(value: A, functions: List<Either<BiFunction<Exception, A, *>, Function<A, *>>> = listOf())
            : Success<A>(value, functions) {
            fun orRecover(function: BiFunction<Exception, A, *>): Success1<A> =
                    Success1(value, functions + Left(function))

        }

        class Success1<A>(value: A, functions: List<Either<BiFunction<Exception, A, *>, Function<A, *>>> = listOf())
            : Success<A>(value, functions)

    }

    class Failure<A>(value: A, val e: Exception, val failure: List<Maybe<Left<*, *>>>)
        : Try<A>(value) {

        override fun isSuccess(): Boolean {
            if (failure.isEmpty()) {
                return false
            }
            if (failure.first() is Just) try {
                (failure.first().get().error as BiFunction<Exception, A, *>).apply(e, value)
            } catch (e: Exception) {
                println("ohnoes $e")
            }
            return Failure(value, e, failure.drop(1)).isSuccess()
        }
    }

    companion object {

        @JvmStatic fun <A> with(value: A): Success<A> = Success0(value)
    }
}