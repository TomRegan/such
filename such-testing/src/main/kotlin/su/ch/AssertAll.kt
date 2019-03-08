package su.ch

import org.opentest4j.MultipleFailuresError
import su.ch.AssertAll.MoreObjects.notEmpty
import java.util.*
import java.util.stream.Stream


internal object AssertAll {

    internal object MoreObjects {
        internal fun <T> notEmpty(executables: Array<T>): Array<T> =
                if (executables.isEmpty()) throw IllegalArgumentException("$executables is empty")
                else executables
    }

    private object BlacklistedExceptions {

        private val blacklist = listOf(OutOfMemoryError::class.java)

        fun rethrowIfBlacklisted(exception: Throwable) =
                if (blacklist.requireNoNulls().any { it.isInstance(exception) }) throw exception
                else Unit
    }

    fun assertAll(heading: String? = null, vararg runnables: Runnable) =
            assertAll(heading, notEmpty(runnables).asSequence().requireNoNulls())

    fun assertAll(heading: String? = null, runnables: Collection<Runnable>) =
            assertAll(heading, runnables.asSequence().requireNoNulls())

    fun assertAll(heading: String? = null, runnables: Stream<Runnable>) =
            assertAll(heading, runnables.iterator().asSequence().requireNoNulls())

    private fun assertAll(heading: String? = null, runnables: Sequence<Runnable>) {
        runnables.onEach { Objects.requireNonNull(it, "individual runnables must not be null") }
                .map {
                    try {
                        it.run()
                        null
                    } catch (t: Throwable) {
                        BlacklistedExceptions.rethrowIfBlacklisted(t)
                        t
                    }
                }
                .filter { Objects.nonNull(it) }
                .toList()
                .let {
                    MultipleFailuresError(heading, it).apply { it.forEach { throwable -> addSuppressed(throwable) } }
                }
                .apply { if (hasFailures()) throw this }
    }

}