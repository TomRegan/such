package su.ch;

import su.ch.framework.Monad;

import org.junit.Assert;
import org.junit.Test;
import su.ch.framework.Monad;

import static su.ch.Maybe.Just;
import static su.ch.Maybe.Nothing;
import static org.junit.Assert.assertEquals;

public class MaybeTest {

    @Test
    public void nothingShouldDoNothing() throws Exception {


        Assert.assertEquals(Maybe.Nothing(),
                Maybe.Nothing()
                        .map((Integer i) -> i + 3)
                        .apply(Maybe.Just(5))
                        .flatMap(Maybe::Just));

    }


    @Test
    public void shouldMapBetweenTypesInCategory() throws Exception {

        Monad<?, ?> justThree = Maybe.Just(3).map((Integer i) -> {
            if (i == 3) { return "three"; } else { return "unknown"; }
        });

        Assert.assertEquals(Maybe.Just("three"), justThree);

    }

}
