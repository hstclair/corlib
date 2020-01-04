package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import com.stclair.corlib.math.util.Sequence;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrimeSequenceTest {

    @Test
    public void currentMember() {
    }

    @Test
    public void currentIndex() {
    }

    @Test
    public void nextMember() {
    }

    @Test
    public void prevMember() {
    }

    @Test
    public void getMember() {
    }

    @Test
    public void find() {
    }

    @Test
    public void findFirstPrime() {

        Sequence<Double> instance = new PrimeSequence<>(new DoubleOperationStrategy());

        double expected = 2;
        double actual = instance.getMember(0);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void findSecondPrime() {

        Sequence<Double> instance = new PrimeSequence<>(new DoubleOperationStrategy());

        double expected = 3;
        double actual = instance.getMember(1);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void findThirdPrime() {

        Sequence<Double> instance = new PrimeSequence<>(new DoubleOperationStrategy());

        double expected = 5;
        double actual = instance.getMember(2);

        assertEquals(expected, actual, 0);
    }

    @Test
    public void findFirstTenPrimes() {
        Sequence<Double> instance = new PrimeSequence<>(new DoubleOperationStrategy());

        double[] actual = new double[10];

        double[] expected = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29 };

        for (int index = 0; index < actual.length; index++)
            actual[index] = instance.nextMember();

        assertArrayEquals(expected, actual, 0);
    }
}