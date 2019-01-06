package su.ch.framework;

import org.junit.jupiter.api.Test;
import su.ch.Maybe;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MonadTest extends ApplicativeTest {

    @Override protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }

    @Test void firstMonadicLaw_LeftIdentity() {

        // The first law of monads states that if we lift a value a into a monadic context
        // with create, and then flatMap to f, it is the same as just applying f to a
        //
        //   create a >>= f  ≡  f a
        //
        Maybe<Integer> justFive = create(5);
        Maybe<Integer> actual = justFive.flatMap(Maybe.Just::new);
        assertThat(Maybe.Just(5), is(equalTo(actual)));
    }


    @Test void secondMonadicLaw_RightIdentity() {

        // The second law of monads states that when we have a value in a monadic context m, and
        // flatMap to create, the result is the same as the original monad
        //
        //   m >>= create  ≡  m
        //
        Maybe<Integer> justFive = create(5);
        assertThat(justFive, is(equalTo(Maybe.Just(5).flatMap(this::create))));
    }


    @Test void thirdMonadLaw_Associativity() {

        // The third law of monads states that flatMap is associative
        //
        //   (m >>= f) >>= g  ≡  m >>= (\x -> f x >>= g)
        //
        Maybe<Integer> justFive = create(5);
        assertThat(
                justFive.flatMap(Maybe.Just::new).flatMap(Maybe.Just::new),
                is(equalTo(justFive.flatMap(x -> Maybe.Just(x).flatMap(Maybe.Just::new)))));


    }
}