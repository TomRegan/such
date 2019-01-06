package su.ch;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import su.ch.framework.Monad;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class MaybeTest {

    @Test void nothingShouldDoNothing() {

        assertThat(Maybe.Nothing(),
                is(equalTo(Maybe.Nothing()
                        .map((Integer i) -> i + 3)
                        .apply(Maybe.Just(5))
                        .flatMap(Maybe::Just))));
    }

    @Test void shouldMapBetweenTypesInCategory() {

        Monad<?, ?> justThree = Maybe.Just(3).map((Integer i) -> {
            if (i == 3) { return "three"; } else { return "unknown"; }
        });

        assertThat(Maybe.Just("three"), is(equalTo(justThree)));
    }

}
