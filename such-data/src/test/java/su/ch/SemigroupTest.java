package su.ch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemigroupTest {

    @Test
    void integerSemigroup() {

        assertEquals(new Semigroups.IntegerAddition(13), Semigroups.append(5, 8));
        // having proved append
        assertEquals(new Semigroups.IntegerAddition(5).append(8), Semigroups.append(5, 8));
        assertEquals(new Semigroups.IntegerMultiplication(120),
                new Semigroups.IntegerMultiplication(1)
                        .append(2)
                        .append(3)
                        .append(4)
                        .append(5));
        assertEquals(new Semigroups.IntegerMinimum(1), new Semigroups.IntegerMinimum(3).append(1).append(2));
        assertEquals(new Semigroups.IntegerMaximum(3), new Semigroups.IntegerMaximum(1).append(3).append(2));
    }

    @Test
    void longSemigroup() {
        assertEquals(new Semigroups.LongAddition(13), Semigroups.append(5L, 8L));
        assertEquals(new Semigroups.LongAddition(5).append(8), Semigroups.append(5L, 8L));
        assertEquals(new Semigroups.LongMultiplication(120),
                new Semigroups.LongMultiplication(1)
                        .append(2)
                        .append(3)
                        .append(4)
                        .append(5));
        assertEquals(new Semigroups.LongMinimum(1), new Semigroups.LongMinimum(3).append(1).append(2));
        assertEquals(new Semigroups.LongMaximum(3), new Semigroups.LongMaximum(1).append(3).append(2));
    }
}
