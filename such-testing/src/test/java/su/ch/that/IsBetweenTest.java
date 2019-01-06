package su.ch.that;

import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IsBetweenTest {

    @Test void isInClosedIntervalShouldMatch() {

        MatcherAssert.assertThat(1, IsBetween.isBetween(1, 1));

    }

    @Test void isInClosedIntervalShouldNotMatchValueAbove() {

        IsBetween bet = IsBetween.isBetween(1, 2);

        Description actualDescription = new StringDescription();
        bet.describeMismatch(3, actualDescription);

        assertThat(actualDescription.toString(),
                is("a value less than or equal to <2> <3> was greater than <2>"));

    }

    @Test void isInClosedIntervalShouldNotMatchValueBelow() {

        IsBetween bet = IsBetween.isWithin(1, 2);

        Description actualDescription = new StringDescription();
        bet.describeMismatch(0, actualDescription);

        assertThat(actualDescription.toString(),
                is("a value greater than <1> <0> was less than <1>"));

    }

    @Test void describeTo() {
        IsBetween bet = IsBetween.isWithin(3, 5);

        Description actualDescription = new StringDescription();
        bet.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("(a value greater than <3> and a value less than <5>)"));

    }
}
