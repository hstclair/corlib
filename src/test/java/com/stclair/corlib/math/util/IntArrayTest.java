package com.stclair.corlib.math.util;

import org.junit.Test;

import java.nio.channels.InterruptedByTimeoutException;

import static org.junit.Assert.*;

public class IntArrayTest {

    @Test
    public void testSignumOfNegativeOneIntArrayIsNegativeOne() {
        IntArray instance = new IntArray(512, new int[] { -1 });

        int expected = -1;

        int actual = instance.signum();

        assertEquals(expected, actual);
    }

    @Test
    public void testSignumOfIntArrayEndingInNegativeOneIsNegativeOne() {
        IntArray instance = new IntArray(512, new int[] { 0, 0, 0, -1 });

        int expected = -1;

        int actual = instance.signum();

        assertEquals(expected, actual);
    }

    @Test
    public void testSignumOfEmptyIntArrayIsZero() {
        IntArray instance = new IntArray(512, new int[0]);

        int expected = 0;

        int actual = instance.signum();

        assertEquals(expected, actual);
    }

    @Test
    public void testSignumOfArrayWithAllZerosIsZero() {

        int[] array = { 0, 0, 0, 0, 0, 0 };

        int expected = 0;

        int actual = IntArray.signum(array);

        assertEquals(expected, actual);
    }

    @Test
    public void testSignumOfIntArrayWithOneInLowestPositionIsOne() {

        IntArray instance = new IntArray(512, new int[] { 1, 0, 0, 0 });

        int expected = 1;

        int actual = instance.signum();

        assertEquals(expected, actual);
    }

    @Test
    public void testSignumOfIntArrayWithOneInLeadingPositionIsOne() {

        IntArray instance = new IntArray(512, new int[] { 0, 0, 0, 1 });

        int expected = 1;

        int actual = instance.signum();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetScaleReturnsScale() {

        int scale = 419;

        IntArray instance = new IntArray(scale, new int[0]);

        int expected = scale;

        int actual = instance.getScale();

        assertEquals(expected, actual);
    }

    @Test
    public void testNegateDoesNotAlterSignumOfZeroIntArray() {

        IntArray instance = new IntArray(0, new int[] { 0, 0, 0, 0, 0 } );

        IntArray negated = instance.negate();

        assertEquals(instance.signum(), negated.signum());
    }

    @Test
    public void testNegateNegativeOneIsPositiveOne() {

        IntArray instance = new IntArray(Integer.SIZE, new int[] { -1 });

        IntArray negated = instance.negate();

        int actual = negated.get(Integer.SIZE);

        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    public void testNegateNegativeOneInLeadPositionIsPositiveOneInLeadPositionAndLowerIsUnchanged() {

        IntArray instance = new IntArray(Integer.SIZE, new int[] { 0, -1 });

        IntArray negated = instance.negate();

        int actualLead = negated.get(Integer.SIZE);

        int expectedLead = 1;

        int actualTrail = negated.get(0);

        int expectedTrail = 0;

        assertEquals(expectedLead, actualLead);

        assertEquals(expectedTrail, actualTrail);
    }

    @Test
    public void testNegateNegativeOneInLeadTwoPositionsIsPositiveOneInMidPositionAndZeroElsewhere() {

        IntArray instance = new IntArray(Integer.SIZE << 1, new int[] { 0, -1, -1 });

        IntArray negated = instance.negate();

        int actualLead = negated.get(Integer.SIZE << 1);

        int expectedLead = 0;

        int actualMid = negated.get(Integer.SIZE);

        int expectedMid = 1;

        int actualTrail = negated.get(0);

        int expectedTrail = 0;

        assertEquals(expectedLead, actualLead);
        assertEquals(expectedMid, actualMid);
        assertEquals(expectedTrail, actualTrail);
    }


    @Test
    public void testNegateOneInMidPositionIsNegativeOneInTwoLeadingPositions() {

        IntArray instance = new IntArray(Integer.SIZE << 1, new int[] { 0, 1, 0 });

        IntArray negated = instance.negate();

        int actualLead = negated.get(Integer.SIZE << 1);

        int expectedLead = -1;

        int actualMid = negated.get(Integer.SIZE);

        int expectedMid = -1;

        int actualTrail = negated.get(0);

        int expectedTrail = 0;

        assertEquals(expectedLead, actualLead);
        assertEquals(expectedMid, actualMid);
        assertEquals(expectedTrail, actualTrail);
    }

    @Test
    public void testGetLeadingValue() {

        int expected = 13407;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { 4, 5, expected });

        int actual = instance.get(Integer.SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMidValue() {

        int expected = 13407;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { 3, expected, 17 });

        int actual = instance.get(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTrailingValue() {

        int expected = 13407;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { expected, 3, 17 });

        int actual = instance.get(-Integer.SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetValueWithSixteenBitOffset() {

        int expected = 0x1234567;

        int bitOffset = Integer.SIZE >> 1;

        IntArray instance = new IntArray(Integer.SIZE + (bitOffset), new int[] { 14, (expected << bitOffset) + 0x4321, (0x7654 << bitOffset) + (expected >> bitOffset)});

        int actual = instance.get(Integer.SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testMarchingBitOffset() {

        long testValue = 0x12345678;

        IntArray instance = new IntArray(Integer.SIZE * 3, new int[] { 0, (int)testValue, 0 });

        for (int bits = 0; bits < 96; bits++) {

            long expected = (testValue << 32);

            if (bits > 32)
                expected >>= bits - 32;

            int actual = instance.get(bits);

            assertEquals((int)expected, actual);
        }
    }

    @Test
    public void testGetValueWithNoShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original;

        int actual = instance.get(Integer.SIZE);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithOneBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 1;

        int actual = instance.get(Integer.SIZE + 1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithTwoBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 2;

        int actual = instance.get(Integer.SIZE + 2);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithThreeBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 3;

        int actual = instance.get(Integer.SIZE + 3);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithFourBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 4;

        int actual = instance.get(Integer.SIZE + 4);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithFiveBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 5;

        int actual = instance.get(Integer.SIZE + 5);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithSixBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 6;

        int actual = instance.get(Integer.SIZE + 6);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithSevenBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 7;

        int actual = instance.get(Integer.SIZE + 7);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetUpperValueWithEightBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original >> 8;

        int actual = instance.get(Integer.SIZE + 8);

        assertEquals(expected, actual);
    }






    @Test
    public void testGetLowerValueWithOneBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 1);

        int actual = instance.get(1);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithTwoBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 2);

        int actual = instance.get(2);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithThreeBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 3);

        int actual = instance.get(3);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithFourBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 4);

        int actual = instance.get(4);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithFiveBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 5);

        int actual = instance.get(5);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithSixBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 6);

        int actual = instance.get(6);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithSevenBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 7);

        int actual = instance.get(7);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLowerValueWithEightBitShift() {
        int original = 0b01001100011100001111000001111100;

        IntArray instance = new IntArray(Integer.SIZE, new int[] { original });

        int expected = original << (Integer.SIZE - 8);

        int actual = instance.get(8);

        assertEquals(expected, actual);
    }

    @Test
    public void testMostSignificantBitOfOneInLowestOrderIsScaleMinusTotalBitsPlusTwo() {

        int[] arrayInstance = { 1, 0, 0, 0 };

        int scale = 17;
        int expected = scale - (arrayInstance.length << 5) + 2;

        IntArray instance = new IntArray(scale, arrayInstance);

        int actual = instance.mostSignificantBit();

        assertEquals(expected, actual);
    }


    @Test
    public void testLeastSignificantBitOfOneInLowestOrderIsScaleMinusTotalBits() {

        int[] arrayInstance = { 1, 0, 0, 0 };

        int scale = 148;
        int expected = scale - (arrayInstance.length << 5) + 2;

        IntArray instance = new IntArray(scale, arrayInstance);

        int actual = instance.leastSignificantBit();

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareNegativeFourToNegativeTen() {

        int[] negativeFourArray = { -4, -1, -1 };
        int[] negativeTenArray = { -10, -1, -1 };

        IntArray negativeFour = new IntArray(96, negativeFourArray);
        IntArray negativeTen = new IntArray(96, negativeTenArray);

        int expected = -1;

        int actual = negativeTen.compare(negativeFour);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareNegativeFourToNegativeFive() {

        int[] negativeFourArray = { -4, -1, -1 };
        int[] negativeFiveArray = { -5, -1, -1 };

        IntArray negativeFour = new IntArray(96, negativeFourArray);
        IntArray negativeFive = new IntArray(96, negativeFiveArray);

        int expected = -1;

        int actual = negativeFive.compare(negativeFour);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareNegativeTenToNegativeFour() {

        int[] negativeFourArray = { -4, -1, -1 };
        int[] negativeTenArray = { -10, -1, -1 };

        IntArray negativeFour = new IntArray(96, negativeFourArray);
        IntArray negativeTen = new IntArray(96, negativeTenArray);

        int expected = 1;

        int actual = negativeFour.compare(negativeTen);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareNegativeFiveToNegativeFour() {

        int[] negativeFourArray = { -4, -1, -1 };
        int[] negativeFiveArray = { -5, -1, -1 };

        IntArray negativeFour = new IntArray(96, negativeFourArray);
        IntArray negativeFive = new IntArray(96, negativeFiveArray);

        int expected = -1;

        int actual = negativeFive.compare(negativeFour);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareTwoToThree() {

        int[] twoArray = { 2, 0, 0 };
        int[] threeArray = { 3, 0, 0 };

        IntArray two = new IntArray(96, twoArray);
        IntArray three = new IntArray(96, threeArray);

        int expected = -1;

        int actual = two.compare(three);

        assertEquals(expected, actual);
    }


    @Test
    public void testCompareThreeToTwo() {

        int[] twoArray = { 2, 0, 0 };
        int[] threeArray = { 3, 0, 0 };

        IntArray two = new IntArray(96, twoArray);
        IntArray three = new IntArray(96, threeArray);

        int expected = 1;

        int actual = three.compare(two);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareOneToNegativeOne() {

        int[] negativeOneArray = { -1, -1, -1 };
        int[] oneArray = { 1, 0, 0 };

        IntArray negativeOne = new IntArray(96, negativeOneArray);
        IntArray one = new IntArray(96, oneArray);

        int expected = 1;

        int actual = one.compare(negativeOne);

        assertEquals(expected, actual);
    }

    @Test
    public void testCompareNegativeOneToOne() {

        int[] negativeOneArray = { -1, -1, -1 };
        int[] oneArray = { 1, 0, 0 };

        IntArray negativeOne = new IntArray(96, negativeOneArray);
        IntArray one = new IntArray(96, oneArray);

        int expected = -1;

        int actual = negativeOne.compare(one);

        assertEquals(expected, actual);
    }

    @Test
    public void testSumFiveAndFour() {
        int[] fiveArray = { 5 };
        int[] fourArray = { 4 };

        IntArray five = new IntArray(Integer.SIZE, fiveArray);
        IntArray four = new IntArray(Integer.SIZE, fourArray);

        int expected = 9;

        IntArray sum = five.sum(four);

        int actual = sum.get(32);

        assertEquals(expected, actual);
    }

    @Test
    public void testIntArrayFromDouble() {

        long value = 0x123456789ABCDL;

        int[] expected = { (int) (value << 14), (int) (value >> (Integer.SIZE - 14)) };

        IntArray instance = new IntArray((double) value);

        int[] actual = instance.getArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testDoubleFromIntArray() {

        double expected = 0x123456789ABCDL;

        IntArray instance = new IntArray(expected);

        double actual = instance.toDouble();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testReconstituteFractionalValue() {

        double expected = .123456789101112;

        IntArray instance = new IntArray(expected);

        double actual = instance.toDouble();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testDoubleValuesSweeping() {

        double baseValue = 0x123456789ABCDEL;

        for (int exponent = -500; exponent < 500; exponent++) {
            for (int index = -5000; index < 5000; index++) {

                double expected = (baseValue + index) * Math.pow(2, exponent);

                IntArray instance = new IntArray(expected);

                double actual = instance.toDouble();

                assertEquals(expected, actual, 0);
            }
        }
    }

    @Test
    public void testSumOfFractionalValues() {

        IntArray pi = new IntArray(Math.PI);

        IntArray e = new IntArray(Math.E);

        double expected = Math.PI + Math.E;

        double actual = e.sum(pi).toDouble();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testSimpleFractionalProduct() {

        double firstDbl = 0.125;

        int multiplier = 8000;

        IntArray first = new IntArray(firstDbl);

        // TODO: Is this right????
        double actual = first.product(multiplier, 32).toDouble();

        double expected = firstDbl * multiplier;

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testFractionalIntArrayProduct() {

        IntArray pi = new IntArray(Math.PI);

        IntArray e = new IntArray(Math.E);

        double expected = Math.PI * Math.E;

        IntArray[] components = e.productComponents(pi);

        for (int index = 0; index < components.length; index++) {

            IntArray component = components[index];

            System.out.printf("component %d = %f\n", index, component.toDouble());
        }

        IntArray result = IntArray.Zero;

        for (IntArray addend : components) {
            result = result.sum(addend);

            System.out.printf("%f\n", result.toDouble());
        }

        double actual = result.toDouble();

        assertEquals(expected, actual, 0);
    }

    @Test
    public void testMostSignificantBitOfPi() {

        IntArray pi = new IntArray(Math.PI);

        int actual = pi.mostSignificantBit();

        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    public void testMostSignificantBitOfFive() {

        IntArray five = new IntArray(5);

        int actual = five.mostSignificantBit();

        int expected = 2;

        assertEquals(expected, actual);
    }
}