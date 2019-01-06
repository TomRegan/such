package su.ch.that;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

class IsObjectTest {

    @Test void shouldDescribeObject() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("objectMember",
                        IsObject.isInstanceOf(TestClass.class)
                                .whereProperty("integer", is(3))
                                .whereProperty("string", is("aString")))
                .whereProperty("integer", is(3))
                .whereProperty("string", is("aString"));

        Description actualDescription = new StringDescription();
        obj.describeTo(actualDescription);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  getObjectMember: TestClass(\n" +
                        "    getInteger: is <3>\n" +
                        "    getString: is \"aString\"\n" +
                        "  )\n" +
                        "  getInteger: is <3>\n" +
                        "  getString: is \"aString\"\n" +
                        ")"));

    }

    @Test void shouldNotMatchWrongType() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("integer", is(3));

        Description actualDescription = new StringDescription();
        obj.describeMismatch(new Object(), actualDescription);

        assertThat(actualDescription.toString(),
                is("not an instance of su.ch.that.IsObjectTest.TestClass"));
    }


    @Test void shouldDescribeNestedProperties() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("objectMember",
                        IsObject.isInstanceOf(TestClass.class)
                                .whereProperty("string", is("Jabberwocky")))
                .whereProperty("integer", is(5));

        Description actualDescription = checkMismatch(obj);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  getObjectMember: TestClass(\n" +
                        "    getString: was \"aString\"\n" +
                        "  )\n" +
                        "  getInteger: was <3>\n" +
                        ")"));
    }


    @Test void shouldDescribeMultipleProperties() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("integer", is(5))
                .whereProperty("string", is("Jabberwocky"));

        Description actualDescription = checkMismatch(obj);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  getInteger: was <3>\n" +
                        "  getString: was \"aString\"\n" +
                        ")"));
    }


    @Test void shouldMatchExistentProperty() {

        assertThat(new TestClass(),
                IsObject.isInstanceOf(TestClass.class)
                        .whereProperty("integer", is(3)));

    }


    @Test void shouldNotMatchNonExistentProperty() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("integer", is(5));

        Description actualDescription = checkMismatch(obj);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  getInteger: was <3>\n" +
                        ")"));
    }


    @Test void shouldNotMatchMissingMethod() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereProperty("missingProperty", is(anything()));

        Description actualDescription = checkMismatch(obj);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  getMissingProperty: was missing\n" +
                        ")"));

    }


    @Test void shouldNotMatchWhenExceptionThrown() {

        IsObject<TestClass> obj = IsObject.isInstanceOf(TestClass.class)
                .whereMethod("throwsException", is(anything()));

        Description actualDescription = checkMismatch(obj);

        assertThat(actualDescription.toString(),
                is("TestClass(\n" +
                        "  throwsException: threw java.lang.RuntimeException, anException!\n" +
                        ")"));

    }


    @NotNull private Description checkMismatch(IsObject<TestClass> obj) {

        Description actualDescription = new StringDescription();
        obj.describeMismatch(new TestClass(), actualDescription);
        return actualDescription;
    }


    @SuppressWarnings("unused")
    class TestClass {

        Integer getInteger() {

            return 3;
        }

        String getString() {

            return "aString";
        }

        TestClass getObjectMember() {

            return new TestClass();

        }

        String throwsException() {

            throw new RuntimeException("anException!");
        }

    }
}
