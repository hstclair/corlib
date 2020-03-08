package com.stclair.corlib.math.util;

import org.apfloat.Apfloat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApfloatOperationStrategyTest {

    ApfloatOperationStrategy instance = new ApfloatOperationStrategy();

    @Test
    public void significantBitsOf1() {

        long expected = 1;

        Apfloat value = new Apfloat(1);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOfNegative1() {

        long expected = 1;

        Apfloat value = new Apfloat(-1);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf2() {

        long expected = 1;

        Apfloat value = new Apfloat(2);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf4() {

        long expected = 1;

        Apfloat value = new Apfloat(4);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf65536() {

        long expected = 1;

        Apfloat value = new Apfloat(65536);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf3() {

        long expected = 2;

        Apfloat value = new Apfloat(3);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf6() {

        long expected = 2;

        Apfloat value = new Apfloat(6);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void significantBitsOf7() {

        long expected = 3;

        Apfloat value = new Apfloat(7);

        long actual = instance.significantBits(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOf1() {

        long expected = 0;

        Apfloat value = new Apfloat(1);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOf2() {

        long expected = 1;

        Apfloat value = new Apfloat(2);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOf65536() {

        long expected = 16;

        Apfloat value = new Apfloat(65536);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void leastSignificantBitOf1() {

        long expected = 0;

        Apfloat value = new Apfloat(1);

        long actual = instance.leastSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void leastSignificantBitOf2() {

        long expected = 1;

        Apfloat value = new Apfloat(2);

        long actual = instance.leastSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void leastSignificantBitOf5() {

        long expected = 0;

        Apfloat value = new Apfloat(5);

        long actual = instance.leastSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOf5() {

        long expected = 2;

        Apfloat value = new Apfloat(5);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOf65535() {

        long expected = 15;

        Apfloat value = new Apfloat(65535);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void leastSignificantBitOf65535() {

        long expected = 0;

        Apfloat value = new Apfloat(65535);

        long actual = instance.leastSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void leastSignificantBitOfPoint5() {

        long expected = -1;

        Apfloat value = new Apfloat(.5);

        long actual = instance.leastSignificantBit(value);

        assertEquals(expected, actual);
    }

    @Test
    public void mostSignificantBitOfPoint5() {

        long expected = -1;

        Apfloat value = new Apfloat(.5);

        long actual = instance.mostSignificantBit(value);

        assertEquals(expected, actual);
    }
}
