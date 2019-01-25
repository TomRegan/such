@file:Suppress("unused")

package su.ch.time

import su.ch.annotation.Beta
import su.ch.annotation.Impure
import su.ch.annotation.Pure
import su.ch.framework.Monoid
import su.ch.time.Stopwatch.Nanoseconds.oneDay
import su.ch.time.Stopwatch.Nanoseconds.oneHour
import su.ch.time.Stopwatch.Nanoseconds.oneMicrosecond
import su.ch.time.Stopwatch.Nanoseconds.oneMillisecond
import su.ch.time.Stopwatch.Nanoseconds.oneMinute
import su.ch.time.Stopwatch.Nanoseconds.oneSecond
import su.ch.time.Stopwatch.StoppedStopwatch
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*
import java.util.function.Consumer
import javax.annotation.concurrent.ThreadSafe

/**
 * Provides a means of measuring elapsed time. `Stopwatch` is
 * meant to be thread-safe.
 */
@ThreadSafe @Beta sealed class Stopwatch : Monoid<Stopwatch, StoppedStopwatch> {

    private val timeUnitSymbol = arrayOf("ns", "μs", "ms", "s", "m", "h", "d", "\u25a1")

    /**
     * Returns `true` if this Stopwatch is running.
     */
    @Pure abstract fun isRunning(): Boolean

    /**
     * Sets the elapsed time for this Stopwatch to zero and places it in a stopped state.
     */
    @Pure fun reset(): ReadyStopwatch = ReadyStopwatch()

    /**
     * Returns the current elapsed time shown on this timer.
     *
     * Note that the overhead of measurement can be more than a microsecond.
     */
    @Pure abstract fun elapsed(): Duration

    // Monoidal members

    /**
     * The identity function. The identity for a given stopwatch is zero.
     */
    @Pure override fun id(): StoppedStopwatch = StoppedStopwatch()

    /**
     * The associative operation which advances elapsed time.
     * Be aware, the result is a [StoppedStopwatch] representing a duration at a (usually notional) instant.
     */
    @Pure override fun append(value: Stopwatch): StoppedStopwatch = StoppedStopwatch(elapsed() + value.elapsed())

    // Members for displaying elapsed time

    /**
     * A string representation of this duration using ISO-8601 seconds
     * based representation, such as `PT8H6M12.345S`.
     *
     * The format of the returned string will be `PTnHnMnS`, where n is
     * the relevant hours, minutes or seconds part of the duration.
     * Any fractional seconds are placed after a decimal point in the seconds section.
     * If a section has a zero value, it is omitted.
     * The hours, minutes and seconds will all have the same sign.
     *
     * Examples:
     * ```
     * "20.345 seconds"                 -- "PT20.345S
     * "15 minutes" (15 * 60 seconds)   -- "PT15M"
     * "10 hours"   (10 * 3600 seconds) -- "PT10H"
     * "2 days"     (2 * 86400 seconds) -- "PT48H"
     * ```
     * Note that multiples of 24 hours are not output as days to avoid confusion
     * with `Period`.
     *
     * @return an ISO-8601 representation of this duration, not null
     */
    @Pure override fun toString(): String = with(elapsed()) { toString() }

    @Pure open fun humanReadable(): String = with(elapsed()) {
        val timeUnit = timeUnitFromNanoseconds(toNanos())
        val duration = toNanos().toDouble() / NANOSECONDS.convert(1L, timeUnit).toDouble()
        String.format("%.4g %s", duration, timeUnit.symbol())
    }

    private fun timeUnitFromNanoseconds(nanoseconds: Long): TimeUnit =
            when {
                nanoseconds < oneMicrosecond -> NANOSECONDS
                nanoseconds < oneMillisecond -> MICROSECONDS
                nanoseconds < oneSecond -> MILLISECONDS
                nanoseconds < oneMinute -> SECONDS
                nanoseconds < oneHour -> SECONDS
                nanoseconds < oneDay -> HOURS
                else -> DAYS
            }

    private fun TimeUnit.symbol(): String = timeUnitSymbol[minOf(timeUnitSymbol.size - 1, this.ordinal)]

    companion object {

        /**
         * Creates (but does not start) a new Stopwatch.
         */
        @JvmStatic fun readyStopwatch(): ReadyStopwatch = ReadyStopwatch()

        /**
         * Creates (but does not start) a new Stopwatch with the given offset.
         * The `offset` value must be a positive duration. The `abs` function will be
         * applied to a negative argument.
         */
        @JvmStatic fun offsetStopwatch(offset: Duration): ReadyStopwatch = ReadyStopwatch(offset = offset)

        /**
         * Creates (and starts) a new Stopwatch using [System.nanoTime] as its time source.
         */
        @JvmStatic fun runningStopwatch(): RunningStopwatch = RunningStopwatch()
    }

    class ReadyStopwatch(private val offset: Duration = Duration.ZERO) : Stopwatch() {
        @Pure fun start(): RunningStopwatch = RunningStopwatch(offset = offset)
        @Pure override fun elapsed(): Duration = offset.abs()
        @Pure override fun isRunning(): Boolean = false
        @Pure fun apply(function: Consumer<Duration>): ReadyStopwatch = apply { function.accept(elapsed()) }
    }

    class RunningStopwatch(offset: Duration = Duration.ZERO) : Stopwatch() {
        // due to the calculation of a new offset from `startTime` for every `elapsed` call
        // functions in a `RunningStopwatch` are usually impure
        private val startTime = System.nanoTime() - offset.abs().toNanos()

        /**
         * @inheritDoc
         * The effect of calling `elapsed` on a `RunningStopwatch` is [Impure]
         */
        @Impure override fun elapsed(): Duration = Duration.ofNanos(System.nanoTime() - startTime)

        @Impure fun stop(): StoppedStopwatch = StoppedStopwatch(elapsed())
        @Pure override fun isRunning(): Boolean = true
        /**
         * @inheritDoc
         * The effect of calling `append` on a `RunningStopwatch` is [Impure]
         */
        @Impure override fun append(value: Stopwatch): StoppedStopwatch = super.append(value)

        /**
         * @inheritDoc
         * The effect of calling `toString` on a `RunningStopwatch` is [Impure]
         */
        @Impure override fun toString(): String = super.toString()

        /**
         * @inheritDoc
         * The effect of calling `humanReadable` on a `RunningStopwatch` is [Impure]
         */
        @Impure override fun humanReadable(): String = super.humanReadable()

        @Impure fun apply(function: Consumer<Duration>): RunningStopwatch = apply { function.accept(elapsed()) }
    }

    class StoppedStopwatch(private val elapsed: Duration = Duration.ZERO) : Stopwatch() {
        @Pure override fun isRunning(): Boolean = false
        @Pure override fun elapsed(): Duration = elapsed
        @Pure fun apply(function: Consumer<Duration>): StoppedStopwatch = apply { function.accept(elapsed()) }
    }

    private object Nanoseconds {
        const val oneMicrosecond = 1000
        const val oneMillisecond = 1000000
        const val oneSecond = 1e+9
        const val oneMinute = 6e+10
        const val oneHour = 3.6e+12
        const val oneDay = 8.64e+13

    }

}
