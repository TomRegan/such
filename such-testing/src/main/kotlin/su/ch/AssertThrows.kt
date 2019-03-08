@file:Suppress("UNCHECKED_CAST")

package su.ch


internal object AssertThrows {

    internal fun <T : Throwable> assertThrows(exception: Class<T>, runnable: Runnable): T {

        try {
            runnable.run()
        } catch (actualException: Throwable) {
            return when {
                exception.isInstance(actualException) -> actualException as T
                else -> throw AssertionError("Expected $exception to be thrown, " +
                        "but $actualException was thrown.", actualException)
            }
        }
        throw AssertionError("Expected $exception to be thrown, but nothing was thrown.")
    }

}