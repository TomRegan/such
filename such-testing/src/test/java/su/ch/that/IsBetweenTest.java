package su.ch.that;

import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IsBetweenTest {

    @Test
    public void isInClosedIntervalShouldMatch() throws Exception {

        MatcherAssert.assertThat(1, IsBetween.isBetween(1, 1));

    }

    @Test
    public void isInClosedIntervalShouldNotMatchValueAbove() throws Exception {

        IsBetween bet = IsBetween.isBetween(1, 2);

        Description actualDescription = new StringDescription();
        bet.describeMismatch(3, actualDescription);

        assertThat(actualDescription.toString(),
                is("a value less than or equal to <2> <3> was greater than <2>"));

    }

    @Test
    public void isInClosedIntervalShouldNotMatchValueBelow() throws Exception {

        IsBetween bet = IsBetween.isWithin(1, 2);

        Description actualDescription = new StringDescription();
        bet.describeMismatch(0, actualDescription);

        assertThat(actualDescription.toString(),
                is("a value greater than <1> <0> was less than <1>"));

    }


    @Test
    public void describeTo() throws Exception {
        IsBetween bet = IsBetween.isWithin(3, 5);

        Description actualDescription = new StringDescription();
        bet.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("(a value greater than <3> and a value less than <5>)"));

    }
}
