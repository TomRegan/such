package su.ch.that

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeDiagnosingMatcher

/**
 * Matchers for ISO 31-11 / ISO-80000-2 intervals.
 */
class IsBetween private constructor(val matcher: Matcher<*>) : TypeSafeDiagnosingMatcher<Comparable<*>>() {

    companion object {

        /**
         * Matcher for a [java.lang.Comparable] in `[a,b]`, defined as a *closed interval* from
         * a (included) to b (included)
         */
        @JvmStatic fun <T : Comparable<T>> isInClosedInterval(start: T, end: T): IsBetween =
                IsBetween(both(greaterThanOrEqualTo(start)).and(lessThanOrEqualTo(end)))

        /**
         * A convenience method which calls [isInClosedInterval].
         *
         * Matcher for a [java.lang.Comparable] in `[a,b]`, defined as a *closed interval* from
         * a (included) to b (included)
         */
        @JvmStatic fun <T : Comparable<T>> isBetween(start: T, end: T): IsBetween =
                isInClosedInterval(start, end)

        /**
         * Matcher for a [java.lang.Comparable] in `(a,b)`, defined as an *open interval* from
         * a (excluded) to b (excluded)
         */
        @JvmStatic fun <T : Comparable<T>> isInOpenInterval(start: T, end: T): IsBetween =
                IsBetween(both(greaterThan(start)).and(lessThan(end)))

        /**
         * A convenience method which calls [isInOpenInterval].
         *
         * Matcher for a [java.lang.Comparable] in `(a,b)`, defined as an *open interval* from
         * a (excluded) to b (excluded)
         */
        @JvmStatic fun <T : Comparable<T>> isWithin(start: T, end: T): IsBetween =
                isInOpenInterval(start, end)

        /**
         * Matcher for a [java.lang.Comparable] in `[a,b)`, defined as a *right half-open interval*
         * from  a (included) to b (excluded)
         */
        @JvmStatic fun <T : Comparable<T>> isInRightHalfOpenInterval(start: T, end: T): IsBetween =
                IsBetween(both(greaterThanOrEqualTo(start)).and(lessThan(end)))

        /**
         * Matcher for a [java.lang.Comparable] in `(a,b]`, defined as a *left half-open interval*
         * from  a (excluded) to b (included)
         */
        @JvmStatic fun <T : Comparable<T>> isInLeftHalfOpenInterval(start: T, end: T): IsBetween =
                IsBetween(both(greaterThan(start)).and(lessThanOrEqualTo(end)))

    }

    override fun matchesSafely(item: Comparable<*>?, mismatchDescription: Description?): Boolean =
            if (!matcher.matches(item)) {
                matcher.describeMismatch(item, mismatchDescription)
                false
            } else true

    override fun describeTo(description: Description?) {
        description!!.appendDescriptionOf(matcher)
    }

}