package su.ch.framework;

import su.ch.Maybe;

import org.junit.Assert;
import org.junit.Test;
import su.ch.Maybe;

import java.util.function.Function;

import static su.ch.Maybe.Just;
import static org.junit.Assert.assertEquals;


public class FunctorTest {


    @SuppressWarnings("unchecked")
    protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }


    @Test
    public void firstFunctorLaw_Identity() throws Exception {

        // The first law of functors states that if we map the id function over a functor, the
        // functor that we get back should be the same as the original functor.
        //
        //   let id := ● -> ●
        //   F(id) == id(F)

        Maybe<Integer> three = create(3);

        Assert.assertEquals(Function.identity().apply(three), three.map(Function.identity()));

    }


    @Test
    public void secondFunctorLaw_Composition() throws Exception {

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

        assertEquals(byAssociation, byComposition);

    }

}