package su.ch;

import static su.ch.Semigroups.append;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import su.ch.Semigroups.IntegerAddition;
import su.ch.Semigroups.IntegerMaximum;
import su.ch.Semigroups.IntegerMinimum;
import su.ch.Semigroups.IntegerMultiplication;
import su.ch.Semigroups.LongAddition;
import su.ch.Semigroups.LongMaximum;
import su.ch.Semigroups.LongMinimum;
import su.ch.Semigroups.LongMultiplication;

public class SemigroupTest {

    @Test
    public void integerSemigroup() throws Exception {

        Assert.assertEquals(new Semigroups.IntegerAddition(13), Semigroups.append(5, 8));
        // having proved append
        Assert.assertEquals(new Semigroups.IntegerAddition(5).append(8), Semigroups.append(5, 8));
        Assert.assertEquals(new Semigroups.IntegerMultiplication(120),
                new Semigroups.IntegerMultiplication(1)
                        .append(2)
                        .append(3)
                        .append(4)
                        .append(5));
        Assert.assertEquals(new Semigroups.IntegerMinimum(1), new Semigroups.IntegerMinimum(3).append(1).append(2));
        Assert.assertEquals(new Semigroups.IntegerMaximum(3), new Semigroups.IntegerMaximum(1).append(3).append(2));
    }

    @Test
    public void longSemigroup() throws Exception {
        Assert.assertEquals(new Semigroups.LongAddition(13), Semigroups.append(5L, 8L));
        Assert.assertEquals(new Semigroups.LongAddition(5).append(8), Semigroups.append(5L, 8L));
        Assert.assertEquals(new Semigroups.LongMultiplication(120),
                new Semigroups.LongMultiplication(1)
                        .append(2)
                        .append(3)
                        .append(4)
                        .append(5));
        Assert.assertEquals(new Semigroups.LongMinimum(1), new Semigroups.LongMinimum(3).append(1).append(2));
        Assert.assertEquals(new Semigroups.LongMaximum(3), new Semigroups.LongMaximum(1).append(3).append(2));
    }
}
