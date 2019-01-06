package su.ch;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static su.ch.IsComparable.isComparable;
import static su.ch.MustThrow.mustThrow;

@SuppressWarnings("ConstantConditions")
class FunctionsTest {

    @Nested class IdentityTest {

        @Test void equalsAndHashCode() {
            isComparable(Functions.FunctionIdentity.class);
        }

        @Test void identity() {
            assertThat(Functions.id().apply(1), is(equalTo(1)));
        }
    }

    @Nested class ComposeTest {

        @Test void equalsAndHashcode() {
            isComparable(Functions.FunctionComposition.class);
        }

        @Test void identity() {
            assertThat(Functions.compose(Functions.id(), Functions.id()).apply(1), is(equalTo(1)));
        }

        @Test void transform() {
            Function<String, String> function = Functions.compose(Integer::parseInt, (Integer it) -> Integer.toString(it));
            assertThat(function.apply("12"), is("12"));
        }

        @Test void parametersError() {
            mustThrow(IllegalArgumentException.class, () -> Functions.compose(Functions.id(), null));
            mustThrow(IllegalArgumentException.class, () -> Functions.compose(null, Functions.id()));
        }
    }

    @Nested class ApTest {

        private final Function<Integer, String> f = Object::toString;
        private final Function<Integer, Function<String, Boolean>> g = i -> s -> i == 1
                ? s.length() == 1
                : s.startsWith("2");

        @Test void equalsAndHashcode() {
            isComparable(Functions.FunctionAp.class);
        }

        @Test void functionApplication() {
            assertThat(Functions.ap(f, g).apply(1), is(true));
        }

        @Test void functionApplicationSwitching() {
            assertThat(Functions.ap(f, g).apply(2), is(true));
        }

        @Test void functionApplicationNullContext() {
            mustThrow(IllegalArgumentException.class, () -> Functions.ap(f, null).apply(2));
        }

        @Test void functionApplicationNullStart() {
            mustThrow(IllegalArgumentException.class, () -> Functions.ap(null, g).apply(2));
        }

        @Test void functionApplicationAllNull() {
            Function<Integer, String> f = null;
            Function<Integer, Function<String, Boolean>> g = null;
            mustThrow(IllegalArgumentException.class, () -> Functions.ap(f, g).apply(2));
        }

    }
}
