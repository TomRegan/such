package su.ch.that

import su.ch.annotation.Beta
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.lang.reflect.InvocationTargetException

@Beta
class IsObject<T> private constructor(
        val cls: Class<*>,
        val matchers: Map<String, Matcher<*>> = emptyMap())
    : TypeSafeDiagnosingMatcher<T>() {

    companion object {

        @JvmStatic fun <T> isInstanceOf(cls: Class<T>): IsObject<T> = IsObject(cls)
    }

    fun whereProperty(property: String, matcher: Matcher<*>): IsObject<T> =
            whereMethod("get${property.capitalize()}", matcher)

    fun whereMethod(property: String, matcher: Matcher<*>): IsObject<T> =
            IsObject(cls, matchers + (property to matcher))

    override fun matchesSafely(obj: T, mismatchDescription: Description?): Boolean =
            if (!cls.isInstance(obj)) {
                mismatchDescription!!.appendText("not an instance of ${cls.canonicalName}")
                false
            } else {
                val descriptions = mismatchDescriptions(obj)
                if (!descriptions.isEmpty()) {
                    mismatchDescription!!.appendText("${(obj as Any).javaClass.simpleName}(")
                    descriptions.forEach {
                        description ->
                        mismatchDescription.appendText("\n  $description")
                    }
                    mismatchDescription.appendText("\n)")
                    false
                } else true
            }

    private fun mismatchDescriptions(obj: T): List<String> {
        return matchers.entries.fold(listOf()) { errors, matchersEntry ->
            val (methodName, matcher) = matchersEntry
            try {
                val result = cls.getDeclaredMethod(methodName).invoke(obj)
                when (matcher.matches(result)) {
                    false -> errors + matcher.describeMismatch(methodName, result)
                    else -> errors
                }
            } catch (e: NoSuchMethodException) {
                errors + "$methodName: was missing"
            } catch (e: InvocationTargetException) {
                val exceptionName = (e.cause as Any).javaClass.canonicalName
                val message = (e.cause as Exception).message
                errors + "$methodName: threw $exceptionName, $message"
            } catch (e: Exception) {
                val exceptionName = e.javaClass.canonicalName
                errors + "calling $methodName caused a test framework exception: $exceptionName, ${e.message}"
            }
        }
    }

    private fun indentDescription(description: StringDescription) =
            description.toString().split('\n').joinToString("\n  ")

    override fun describeTo(description: Description?) {
        // I am—yet what I am none cares or knows;
        // My friends forsake me like a memory lost:
        // I am the self-consumer of my woes
        description!!.appendText("${cls.simpleName}(")
        for ((methodName, matcher) in matchers) {
            description.appendText(matcher.describeTo(methodName))
        }
        description.appendText("\n)")
    }

    private fun Matcher<*>.describeTo(methodName: String): String {
        val description = StringDescription()
        describeTo(description)
        return "\n  $methodName: ${indentDescription(description)}"
    }

    private fun Matcher<*>.describeMismatch(methodName: String, result: Any): String {
        val description = StringDescription()
        describeMismatch(result, description)
        return "$methodName: ${indentDescription(description)}"
    }

}