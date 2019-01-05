@file:Suppress("UNCHECKED_CAST")

package su.ch

import su.ch.Either.Left
import su.ch.Either.Right
import su.ch.Maybe.Just
import su.ch.Maybe.Nothing
import su.ch.annotation.Beta
import su.ch.annotation.Pure
import su.ch.framework.Monad
import java.util.function.Function

/**
 * The [Either] type represents values with two possibilities: a value of type `Either` is
 * either [Left] or [Right].
 *
 * `Either` is commonly used to express success / failure cases. By convention, the `Right` value
 * represents success (right = correct), while the `Left` value represents some kind of failure.
 */
@Beta
sealed class Either<L, R> constructor(val error: L, value: R) : Monad<R, Either<L, R>>(value) {

    override fun <A> create(value: A): Either<L, R> = Right<A, A>(value) as Either<L, R>

    override fun <B> map(mapper: Function<B, *>): Either<L, R> = super.map(mapper) as Either<L, R>

    /**
     * Instance of [Either] which by convention represents failure.
     */
    class Left<L, R>(error: L) : Either<L, R>(error, Unit.Unit as R) {

        override fun toString(): String =
                ToString(this)
                        .addValue(error)
                        .toString()
    }

    /**
     * Instance of [Either] which by convention represents success.
     */
    class Right<L, R>(value: R) : Either<L, R>(Unit.Unit as L, value)

    companion object {

        /**
         * Returns a new [Left] value which by convention represents failure.
         */
        @JvmStatic fun <L, R> Left(error: L): Either<L, R> = Either.Left(error)

        /**
         * Returns a new [Right] value which by convention represents success.
         */
        @JvmStatic fun <L, R> Right(value: R): Either<L, R> = Either.Right(value)

        /**
         * Partitions a [Collection] of [Either] into two lists. All the [Left] elements are
         * extracted, in order, to the left branch of the output [Pair]. Similarly the [Right]
         * elements are extracted to the right branch.
         *
         * # Example Usage
         *
         * ```
         * val list = listOf(Left("foo"), Right(3), Left("bar"), Right(7), Left("baz"))
         * assert(partition(list) == Pair(listOf(Left("foo"), Left("bar"), Left("baz")), listOf(Right(3), Right(7)))
         * ```
         */
        @JvmStatic @Pure
        fun partition(eithers: Collection<Either<*, *>>): Pair<List<Left<*, *>>, List<Right<*, *>>> =
                Pair(lefts(eithers), rights(eithers))

        private fun equalize(partition: Pair<List<Maybe<Left<*, *>>>, List<Maybe<Right<*, *>>>>): Pair<List<Maybe<Left<*, *>>>, List<Maybe<Right<*, *>>>> {
            val (lefts, rights) = partition
            return when {
                lefts.size == rights.size -> Pair(lefts, rights)
                lefts.size < rights.size -> Pair(lefts + Nothing(), rights)
                else -> Pair(lefts, rights + Nothing())
            }
        }

        /**
         * Partitions a [Collection] of [Maybe] ([Either]) into two lists of equal length. All the
         * [Just] ([Left]) elements are extracted, in order, to the left branch of the output [Pair].
         * Similarly the [Just] ([Right]) elements are extracted to the right branch. Each `Left`
         * element will be paired with the preceding `Right` element of the list, or [Nothing] if
         * it was preceded by another `Left` element. If the first element in the list is a `Left`
         * element, the right branch will be `Nothing`. If the last element in the list is a
         * `Right` element, the final element in the left branch will be `Nothing`.
         *
         * # Example Usage
         *
         * ```
         * val list = listOf(Left("foo"), Right(3),  Right(7), Left("bar"))
         * assert(partition(list) == Pair(listOf(Just(Left("foo")), Nothing(), Just(Left("bar"))), listOf(Nothing(), Just(Right(3)), Just(Right(7))))
         * ```
         */
        @JvmStatic @Pure
        fun partitionLeft(eithers: Collection<Either<*, *>>): Pair<List<Maybe<Left<*, *>>>, List<Maybe<Right<*, *>>>> =
                equalize(eithers.fold(Pair(listOf(), listOf()), { es, e ->
                    when (e) {
                        is Right -> when {
                            es.left.size == es.right.size -> Pair(es.left, es.right + Just(e))
                            else -> Pair(es.left + Nothing(), es.right + Just(e))
                        }
                        is Left -> when {
                            es.left.size == es.right.size -> Pair(es.left + Just(e), es.right + Nothing())
                            else -> Pair(es.left + Just(e), es.right)
                        }
                    }
                }))

        /**
         * Partitions a [Collection] of [Maybe] ([Either]) into two lists of equal length. All the
         * [Just] ([Left]) elements are extracted, in order, to the left branch of the output [Pair].
         * Similarly the [Just] ([Right]) elements are extracted to the right branch. Each `Right`
         * element will be paired with the preceding `Left` element of the list, or [Nothing] if
         * it was preceded by another `Right` element. If the first element in the list is a `Right`
         * element, the left branch will be `Nothing`. If the last element in the list is a
         * `Left` element, the final element in the right branch will be `Nothing`
         *
         * # Example Usage
         *
         * ```
         * val list = listOf(Left("foo"), Right(3),  Right(7), Left("bar"))
         * assert(partition(list) == Pair(listOf(Just(Left("foo")), Nothing(), Just(Left("bar"))), listOf(Just(Right(3)), Just(Right(7)), Nothing()))
         * ```
         */
        @JvmStatic @Pure
        fun partitionRight(eithers: Collection<Either<*, *>>): Pair<List<Maybe<Left<*, *>>>, List<Maybe<Right<*, *>>>> =
                equalize(eithers.fold(Pair(listOf(), listOf()), { es, e ->
                    when (e) {
                        is Left -> when {
                            es.left.size == es.right.size -> Pair(es.left + Just(e), es.right)
                            else -> Pair(es.left + Just(e), es.right + Nothing())
                        }
                        is Right -> when {
                            es.left.size == es.right.size -> Pair(es.left + Nothing(), es.right + Just(e))
                            else -> Pair(es.left, es.right + Just(e))
                        }
                    }
                }))

        /**
         * Extracts from a [Collection] of [Either] all the [Left] elements.
         * All the 'Left' elements are extracted in order.
         *
         * # Example Usage
         *
         * ```
         * val list = listOf(Left("foo"), Right(3), Left("bar"), Right(7), Left("baz"))
         * assert(lefts(list) == listOf(Left("foo"), Left("bar"), Left("baz")))
         * ```
         */
        @JvmStatic @Pure
        fun lefts(eithers: Collection<Either<*, *>>): List<Left<*, *>> =
                eithers.filter { it -> it is Left<*, *> } as List<Left<*, *>>

        /**
         * Extracts from a [Collection] of [Either] all the [Right] elements.
         * All the 'Right' elements are extracted in order.
         *
         * # Example Usage
         *
         * ```
         * val list = listOf(Left("foo"), Right(3), Left("bar"), Right(7), Left("baz"))
         * assert(rights(list) == listOf(Right(3), Right(7)))
         * ```
         */
        @JvmStatic @Pure
        fun rights(eithers: Collection<Either<*, *>>): List<Right<*, *>> =
                eithers.filter { it -> it is Right<*, *> } as List<Right<*, *>>

    }
}