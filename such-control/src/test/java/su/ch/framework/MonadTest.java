package su.ch.framework;

import su.ch.Maybe;

import org.junit.Assert;
import org.junit.Test;

import static su.ch.Maybe.Just;
import static org.junit.Assert.assertEquals;

public class MonadTest extends ApplicativeTest {


    @SuppressWarnings("unchecked")
    @Override
    protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }


    @Test
    public void firstMonadicLaw_LeftIdentity() throws Exception {

        // The first law of monads states that if we lift a value a into a monadic context
        // with create, and then flatMap to f, it is the same as just applying f to a
        //
        //   create a >>= f  ≡  f a
        //
        Maybe<Integer> justFive = create(5);
        Maybe<Integer> actual = justFive.flatMap(Maybe.Just::new);
        Assert.assertEquals(Maybe.Just(5), actual);
    }


    @Test
    public void secondMonadicLaw_RightIdentity() throws Exception {

        // The second law of monads states that when we have a value in a monadic context m, and
        // flatMap to create, the result is the same as the original monad
        //
        //   m >>= create  ≡  m
        //
        Maybe<Integer> justFive = create(5);
        Assert.assertEquals(justFive, Maybe.Just(5).flatMap(i -> create(i)));
    }


    @Test
    public void thirdMonadLaw_Associativity() throws Exception {

        // The third law of monads states that flatMap is associative
        //
        //   (m >>= f) >>= g  ≡  m >>= (\x -> f x >>= g)
        //
        Maybe<Integer> justFive = create(5);
        Assert.assertEquals(
                justFive.flatMap(Maybe.Just::new).flatMap(Maybe.Just::new),
                justFive.flatMap(x -> Maybe.Just(x).flatMap(Maybe.Just::new)));


    }
}