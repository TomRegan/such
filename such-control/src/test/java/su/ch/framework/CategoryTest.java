package su.ch.framework;

import org.junit.jupiter.api.Test;

import static su.ch.IsComparable.isComparable;

class CategoryTest {

    @Test void equalsAndHashcode() {
        isComparable(Category.class);
    }

}