package su.ch.framework;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class CategoryTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Category.class).verify();
    }

}