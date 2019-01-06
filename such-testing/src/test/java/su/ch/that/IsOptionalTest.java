package su.ch.that;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IsOptionalTest {

    @Test void emptyOptionalShouldMatch() {

        assertThat(Optional.empty(), IsOptional.isEmptyOptional());
    }

    @Test void optionalWithValueShouldNotMatch() {

        IsOptional opt = IsOptional.isEmptyOptional();

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.of(3), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an Optional with the value 3"));
    }

    @Test void shouldDescribeEmptyOptional() {

        IsOptional opt = IsOptional.isEmptyOptional();

        Description actualDescription = new StringDescription();
        opt.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("an empty Optional"));
    }

    @Test void optionalShouldMatch() {

        assertThat(Optional.of(3), IsOptional.isOptionalWithValue(3));
    }


    @Test void optionalWithWrongValueShouldNotMatch() {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.of(5), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an Optional with the value 5"));
    }


    @Test void optionalWithEmptyValueShouldNotMatch() {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.empty(), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an empty Optional"));
    }


    @Test void shouldDescribeOptional() {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("an Optional with the value <3>"));
    }


    @Test void optionalShouldMatchAnything() {

        assertThat(Optional.of(Integer.MAX_VALUE), IsOptional.isOptionalWithAnyValue());
    }
}
