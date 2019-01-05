@file:Suppress("UNCHECKED_CAST")

package su.ch


class MustThrow {

    companion object {
        @JvmStatic
        fun <T : Throwable> mustThrow(exception: Class<T>, given: Runnable): T {

            try {
                given.run()
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

}