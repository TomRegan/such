package su.ch.framework;

import org.junit.jupiter.api.Test;
import su.ch.Maybe;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


class FunctorTest {

    protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }

    @Test void firstFunctorLaw_Identity() {

        // The first law of functors states that if we map the id function over a functor, the
        // functor that we get back should be the same as the original functor.
        //
        //   let id := ● -> ●
        //   F(id) == id(F)

        Maybe<Integer> three = create(3);

        assertThat(Function.identity().apply(three), is(equalTo(three.map(Function.identity()))));

    }


    @Test void secondFunctorLaw_Composition() {

        // The second law of functors that composing two functions and then mapping the resulting
        // function over a functor should be the same as first mapping one function over the
        // functor and then mapping the other one.
        //
        //   let f := ● -> ○
        //   let g := ○ -> △
        //   (f . g) == ((● -> ○) -> △) == ● -> △
        //
        //   F(● -> △) == F(● -> ○) . F(○ -> △)

        Function<Integer, Integer> f = (i -> i - 5);
        Function<Integer, Integer> g = (i -> i * 8);
        Function<Integer, Integer> fDotG = g.compose(f);

        Functor<?, ?> byAssociation = create(13)
                .map(f)
                .map(g);

        Functor<?, ?> byComposition = create(13)
                .map(fDotG);

        assertThat(byAssociation, is(equalTo(byComposition)));

    }

}