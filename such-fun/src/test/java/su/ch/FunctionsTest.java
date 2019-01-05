package su.ch;

import static su.ch.IsComparable.isComparable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static su.ch.MustThrow.mustThrow;

import java.util.function.Function;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class FunctionsTest {

    // identity

    @Test
    public void idEqualsAndHashCode() {
        isComparable(Functions.FunctionIdentity.class);
    }

    @Test
    public void identity() {
        MatcherAssert.assertThat(Functions.id().apply(1), is(equalTo(1)));
    }

    // compose

    @Test
    public void composeIdentity() {
        MatcherAssert.assertThat(Functions.compose(Functions.id(), Functions.id()).apply(1), is(equalTo(1)));
    }

    @Test
    public void composeTransform() {
        Function<String, String> function = Functions.compose(Integer::parseInt, (Integer it) -> Integer.toString(it));
        assertThat(function.apply("12"), is("12"));
    }

    @Test
    public void composeParametersError() {
        mustThrow(IllegalArgumentException.class, () -> Functions.compose(Functions.id(), null));
        mustThrow(IllegalArgumentException.class, () -> Functions.compose(null, Functions.id()));
    }

    @Test
    public void composeEqualsAndHashcode() {
        isComparable(Functions.FunctionComposition.class);
    }

    // ap

    private final Function<Integer, String> f = Object::toString;
    private final Function<Integer, Function<String, Boolean>> g = i -> s -> i == 1
            ? s.length() == 1
            : s.startsWith("2");

    @Test
    public void functionApplication() {
        MatcherAssert.assertThat(Functions.ap(f, g).apply(1), is(true));
    }

    @Test
    public void functionApplicationSwitching() {
        MatcherAssert.assertThat(Functions.ap(f, g).apply(2), is(true));
    }

    @Test
    public void functionApplicationNullContext() {
        mustThrow(IllegalArgumentException.class, () -> Functions.ap(f, null).apply(2));
    }

    @Test
    public void functionApplicationNullStart() {
        mustThrow(IllegalArgumentException.class, () -> Functions.ap(null, g).apply(2));
    }

    @Test
    public void functionApplicationAllNull() {
        final Function<Integer, String> f = null;
        final Function<Integer, Function<String, Boolean>> g = null;
        mustThrow(IllegalArgumentException.class, () -> Functions.ap(f, g).apply(2));
    }
}
