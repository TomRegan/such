package su.ch.framework;

import org.junit.jupiter.api.Test;
import su.ch.Maybe;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ApplicativeTest extends FunctorTest {

    @Override protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }


    @Test void firstApplicativeLaw_Identity() {

        // The first law of applicative functors states that if we apply the create identity function,
        // the applicative functor we get back should be the same as the original applicative
        // functor.
        //
        //   create id <*> Just x  ≡  Just x
        //
        //   let f := ● -> ●
        //    in A(f) -> ●  ≡  A(●)
        //

        int x = 5;
        Function<Integer, Integer> f = Function.identity();
        Maybe<Function<Integer, Integer>> applicative = create(f);

        assertThat(create(x), is(equalTo(applicative.apply(Maybe.Just(x)))));

    }


    @Test void secondApplicativeLaw_Homomorphism() {

        // The second law of applicative functors states that if we apply a pure function to a pure
        // value is the same as applying the function to the value and then using create on the
        // result.
        //
        //   create f <*> create x  ≡  create (f x)
        //
        //   let f := ● -> ○
        //    in A(f) -> A(●)  ≡  A(f(●))
        //
        // Intuitively, applying a non-effectful function to a non-effectful argument in an
        // effectful context is the same as just applying the function to the argument and then
        // injecting the result into the context with create.

        int x = 5;
        Function<Integer, Integer> f = i -> i + x;

        assertThat(
                create(f).apply(create(x)),
                is(equalTo(create(f.apply(x)))));
    }


    @Test void thirdApplicativeLaw_Interchange() {

        // The third law of applicative functors states that applying a morphism to a pure value is
        // the same as applying pure ($x) to the morphism - ($x) means a function that supplies x
        // as an argument to another function.
        //
        //    u <*> create x  ≡  create ($x) <*> u
        //
        //    let  f := ● -> ○
        //    let $● := (● -> ○) -> ● -> ○
        //     in A(f) -> F(●)  ≡  A($●) -> A(f)
        //
        // Intuitively, this says that when evaluating the application of an effectful function to
        // a pure argument, the order in which we evaluate the function and its argument doesn't
        // matter.

        int x = 5;
        Function<Integer, Integer> f = i -> i + x;
        Function<Function<Integer, Integer>, Integer> $x = it -> it.apply(x);

        assertThat(
                create(f).apply(create(x)),
                is(equalTo(create($x).apply(create(f)))));
    }


    @Test void fourthApplicativeLaw_Composition() {

        // The fourth law of applicative functors states that if apply is used to compose morphisms
        // the composition is associative, like plain function composition.
        //
        //   create (.) <*> u <*> v <*> w  ≡  u <*> (v <*> w)
        //
        // (.)apply(u).apply(v).apply(w) = (u).apply((v).apply(w))
        //

        Function<Integer, Integer> fu = i -> i + 5;
        Function<Integer, Integer> fv = i -> i + 8;
        Maybe<Integer> w = create(13);

        Maybe<Function<Integer, Integer>> u = create(fu);
        Maybe<Function<Integer, Integer>> v = create(fv);
        Function<Function<Integer, Integer>, Function<Integer, Integer>> _u = it -> it.compose(fu);

        assertThat(
                u.apply(v.apply(w)),
                is(equalTo(create(_u).apply(v).apply(w))));

    }

}