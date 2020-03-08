package com.stclair.corlib.math.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuadTest {

    @Test
    public void testProductOfOneAndOne() {

        double one = 1;

        Quad oneQuad = new Quad(one);

        Quad product = oneQuad.product(oneQuad);

        double actual = product.doubleValue();

        assertEquals(one, actual, 0);
    }

    @Test
    public void testProductOfFiveAndFive() {

        double five = 5;

        double expected = 25;

        Quad fiveQuad = new Quad(five);

        Quad twentyFiveQuad = fiveQuad.product(fiveQuad);

        double actual = twentyFiveQuad.doubleValue();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOfPointFiveAndPointFive() {

        double five = .5;

        double expected = .25;

        Quad fiveQuad = new Quad(five);

        Quad twentyFiveQuad = fiveQuad.product(fiveQuad);

        double actual = twentyFiveQuad.doubleValue();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOf2Exp32AndOne() {

        double twoExp32 = (2L << 32);

        double one = 1;

        Quad oneQuad = new Quad(1);

        Quad twoExp32Quad = new Quad(twoExp32);

        Quad product = twoExp32Quad.product(oneQuad);

        double actual = product.doubleValue();

        double expected = twoExp32 * one;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOf2Exp32AndTwoExp32() {

        double twoExp32 = (2L << 32);

        Quad twoExp32Quad = new Quad(twoExp32);

        Quad product = twoExp32Quad.product(twoExp32Quad);

        double actual = product.doubleValue();

        double expected = twoExp32 * twoExp32;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOf2Exp52AndTwoExp52() {

        double twoExp52 = (2L << 52);

        Quad twoExp32Quad = new Quad(twoExp52);

        Quad product = twoExp32Quad.product(twoExp32Quad);

        double actual = product.doubleValue();

        double expected = twoExp52 * twoExp52;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOfPiAndOne() {

        double pi = Math.PI;

        double one = 1;

        Quad piQuad = new Quad(pi);

        Quad oneQuad = new Quad(one);

        Quad onePiQuad = piQuad.product(oneQuad);

        double actual = onePiQuad.doubleValue();

        double expected = pi * one;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testProductOfPiAndE() {

        double pi = Math.PI;

        double e = Math.E;

        Quad piQuad = new Quad(pi);

        Quad eQuad = new Quad(e);

        Quad ePiQuad = piQuad.product(eQuad);

        double actual = ePiQuad.doubleValue();

        double expected = pi * e;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfZeroAndOne() {

        double addend1Dbl = 0;
        double addend2Dbl = 1;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfOneAndOne() {

        double addend1Dbl = 1;
        double addend2Dbl = 1;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfHalfMaxLongAndSelf() {

        double addend1Dbl = Long.MAX_VALUE >> 1;
        double addend2Dbl = Long.MAX_VALUE >> 1;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfMaxLongAndSelf() {

        double addend1Dbl = Long.MAX_VALUE;
        double addend2Dbl = Long.MAX_VALUE;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfMaxLongAndOne() {

        double addend1Dbl = Long.MAX_VALUE;
        double addend2Dbl = 1;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfFiveAndNegativeFour() {

        double addend1Dbl = 5;
        double addend2Dbl = -4;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSumOfFourAndNegativeFive() {

        double addend1Dbl = -5;
        double addend2Dbl = 4;

        Quad addend1 = new Quad(addend1Dbl);
        Quad addend2 = new Quad(addend2Dbl);

        Quad sum = addend1.sum(addend2);

        double actual = sum.doubleValue();

        double expected = addend1Dbl + addend2Dbl;

        assertEquals(expected, actual, 0);
    }

}