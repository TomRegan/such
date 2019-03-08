package su.ch

import java.util.stream.Stream

object Assertions {

    //
    // AssertThrows
    //

    @JvmStatic fun <T : Throwable> assertThrows(exception: Class<T>, runnable: Runnable) =
            AssertThrows.assertThrows(exception, runnable)

    //
    // AssertAll
    //

    // varargs

    @JvmStatic fun assertAll(vararg runnables: Runnable) =
            AssertAll.assertAll(runnables=*runnables)

    @JvmStatic fun assertAll(message: String, vararg runnables: Runnable) =
            AssertAll.assertAll(message, *runnables)

    // java.util.Collection

    @JvmStatic fun assertAll(runnables: Collection<Runnable>) =
            AssertAll.assertAll(runnables=runnables)

    @JvmStatic fun assertAll(heading: String, runnables: Collection<Runnable>) =
            AssertAll.assertAll(heading, runnables)

    // java.util.Stream

    @JvmStatic fun assertAll(runnables: Stream<Runnable>) =
            AssertAll.assertAll(runnables=runnables)

    @JvmStatic fun assertAll(heading: String, runnables: Stream<Runnable>) =
            AssertAll.assertAll(heading, runnables)
}

