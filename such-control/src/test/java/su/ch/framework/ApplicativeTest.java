package su.ch.framework;

import static su.ch.Maybe.Just;
import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import su.ch.Maybe;

public class ApplicativeTest extends FunctorTest {


    @Override
    protected <A> Maybe<A> create(A value) {

        return Maybe.Just(value);
    }


    @Test
    public void firstApplicativeLaw_Identity() throws Exception {

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

        Assert.assertEquals(create(x), applicative.apply(Maybe.Just(x)));
    }


    @Test
    public void secondApplicativeLaw_Homomorphism() throws Exception {

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

        Assert.assertEquals(
                (create(f)).apply(create(x)),
                create(f.apply(x)));
    }


    @Test
    public void thirdApplicativeLaw_Interchange() throws Exception {

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

        Assert.assertEquals(
                (create(f)).apply(create(x)),
                (create($x)).apply(create(f)));
    }


    @Test
    public void fourthApplicativeLaw_Composition() throws Exception {

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

        Assert.assertEquals(
                u.apply(v.apply(w)),
                (create(_u)).apply(v).apply(w));

    }

}