package com.stclair.corlib.math;

import com.stclair.corlib.math.util.MoreMath;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BaseFCounterTest {


    @Test
    public void testSetValue1() {

        BaseFCounter instance = new BaseFCounter()
                .set(1);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(1, digit0);
        assertEquals(0, digit1);
        assertEquals(0, digit2);
    }


    @Test
    public void testSetValue2() {

        BaseFCounter instance = new BaseFCounter()
                .set(2);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(0, digit0);
        assertEquals(1, digit1);
        assertEquals(0, digit2);
    }


    @Test
    public void testSetValue3() {

        BaseFCounter instance = new BaseFCounter()
                .set(3);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(1, digit0);
        assertEquals(1, digit1);
        assertEquals(0, digit2);
    }


    @Test
    public void testSetValue4() {

        BaseFCounter instance = new BaseFCounter()
                .set(4);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(0, digit0);
        assertEquals(2, digit1);
        assertEquals(0, digit2);
    }

    @Test
    public void testSetValue5() {

        BaseFCounter instance = new BaseFCounter()
                .set(5);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(1, digit0);
        assertEquals(2, digit1);
        assertEquals(0, digit2);
    }

    @Test
    public void testSetValue6() {

        BaseFCounter instance = new BaseFCounter()
                .set(6);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(0, digit0);
        assertEquals(0, digit1);
        assertEquals(1, digit2);
    }

    @Test
    public void testSetValue7() {

        BaseFCounter instance = new BaseFCounter()
                .set(7);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(1, digit0);
        assertEquals(0, digit1);
        assertEquals(1, digit2);
    }

    @Test
    public void testSetValue8() {

        BaseFCounter instance = new BaseFCounter()
                .set(8);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(0, digit0);
        assertEquals(1, digit1);
        assertEquals(1, digit2);
    }

    @Test
    public void testSetValue9() {

        BaseFCounter instance = new BaseFCounter()
                .set(9);

        int digit0 = instance.getDigit(0);

        int digit1 = instance.getDigit(1);

        int digit2 = instance.getDigit(2);

        assertEquals(1, digit0);
        assertEquals(1, digit1);
        assertEquals(1, digit2);
    }


    public int[] toBaseF(long value) {

        long divisor = 2;

        List<Integer> digits = new ArrayList<>();

        while (value > 0) {

            int modulus = (int) (value % divisor);

            digits.add(modulus);

            value = value / divisor;

            divisor++;
        }

        return digits.stream()
                .mapToInt(digit -> digit)
                .toArray();
    }


    @Test
    public void testSetValue() {

        int[] digits;

        for (int factorialArg = 2; factorialArg < 21; factorialArg++) {

            long value = Math.max(MoreMath.factorial(factorialArg) - 3, 0);

            BaseFCounter counter = new BaseFCounter()
                    .set(value);

            digits = toBaseF(value);

            for (int index = 0; index < digits.length; index++) {

                if (digits[index] != counter.getDigit(index))
                    System.out.printf("mismatch at digit %d of %d", index, value);

                assertEquals(digits[index], counter.getDigit(index));
            }
        }
    }



    @Test
    public void testIncrementRollover() {

        int[] digits;

        for (int factorialArg = 2; factorialArg < 21; factorialArg++) {

            long start = Math.max(MoreMath.factorial(factorialArg) - 5, 0);

            long end = start + 10;


            BaseFCounter counter = new BaseFCounter()
                    .set(start);

            for (long count = start; count < end; count++) {
                digits = toBaseF(count);

                for (int index = 0; index < digits.length; index++) {

                    if (digits[index] != counter.getDigit(index))
                        System.out.printf("mismatch at digit %d of %d", index, count);

                    assertEquals(digits[index], counter.getDigit(index));
                }

                counter.increment();
            }
        }
    }
}