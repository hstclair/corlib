package com.stclair.corlib.math.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class DoubleOperationStrategyTest {

    DoubleOperationStrategy instance = new DoubleOperationStrategy();

    @Test
    public void significantBitsOf1() {

        long expected = 1;

        double value = 1;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOfNegative1() {

        long expected = 1;

        double value = -1;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf2() {

        long expected = 1;

        double value = 2;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf4() {

        long expected = 1;

        double value = 4;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf65536() {

        long expected = 1;

        double value = 65536;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf3() {

        long expected = 2;

        double value = 3;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf6() {

        long expected = 2;

        double value = 6;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf7() {

        long expected = 3;

        double value = 7;

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

}