package su.ch.that;

import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.Optional;

import static su.ch.that.IsOptional.isOptionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IsOptionalTest {


    @Test
    public void emptyOptionalShouldMatch() throws Exception {

        MatcherAssert.assertThat(Optional.empty(), IsOptional.isEmptyOptional());
    }


    @Test
    public void optionalWithValueShouldNotMatch() throws Exception {

        IsOptional opt = IsOptional.isEmptyOptional();

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.of(3), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an Optional with the value 3"));
    }


    @Test
    public void shouldDescribeEmptyOptional() throws Exception {

        IsOptional opt = IsOptional.isEmptyOptional();

        Description actualDescription = new StringDescription();
        opt.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("an empty Optional"));

    }


    @Test
    public void optionalShouldMatch() throws Exception {

        MatcherAssert.assertThat(Optional.of(3), IsOptional.isOptionalWithValue(3));

    }


    @Test
    public void optionalWithWrongValueShouldNotMatch() throws Exception {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.of(5), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an Optional with the value 5"));
    }


    @Test
    public void optionalWithEmptyValueShouldNotMatch() throws Exception {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeMismatch(Optional.empty(), actualDescription);

        assertThat(actualDescription.toString(),
                is("was an empty Optional"));
    }


    @Test
    public void shouldDescribeOptional() throws Exception {

        IsOptional opt = IsOptional.isOptionalWithValue(3);

        Description actualDescription = new StringDescription();
        opt.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("an Optional with the value <3>"));

    }


    @Test
    public void optionalShouldMatchAnything() throws Exception {

        MatcherAssert.assertThat(Optional.of(Integer.MAX_VALUE), IsOptional.isOptionalWithAnyValue());
    }
}
