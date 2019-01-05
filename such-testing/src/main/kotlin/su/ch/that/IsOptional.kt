package su.ch.that

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeDiagnosingMatcher
import java.util.*

sealed class IsOptional : TypeSafeDiagnosingMatcher<Optional<*>>() {

    companion object {

        @JvmStatic fun isEmptyOptional(): IsOptional = IsEmpty()

        @JvmStatic fun isOptionalWithValue(matcher: Matcher<*>) = IsPresent(matcher)

        @JvmStatic fun <T> isOptionalWithValue(matcher: T) = IsPresent(Matchers.equalTo(matcher))

        @JvmStatic fun isOptionalWithAnyValue() = IsPresent(Matchers.anything())
    }

    class IsEmpty : IsOptional() {

        override fun matchesSafely(item: Optional<*>?, mismatchDescription: Description?): Boolean =
                if (item!!.isPresent) {
                    mismatchDescription!!.appendText("was an Optional with the value ${item.get()}")
                    false
                } else true

        override fun describeTo(description: Description?) {
            description!!.appendText("an empty Optional")
        }

    }

    class IsPresent constructor(val matcher: Matcher<*>) : IsOptional() {

        override fun matchesSafely(item: Optional<*>?, mismatchDescription: Description?): Boolean =
                if (!item!!.isPresent) {
                    mismatchDescription!!.appendText("was an empty Optional")
                    false
                } else if (!matcher.matches(item.get())) {
                    mismatchDescription!!.appendText("was an Optional with the value ${item.get()}")
                    false
                } else true

        override fun describeTo(description: Description?) {
            description!!.appendText("an Optional with the value ").appendDescriptionOf(matcher)
        }

    }

}