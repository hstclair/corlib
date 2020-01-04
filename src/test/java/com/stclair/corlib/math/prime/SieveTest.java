package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;

import static org.junit.Assert.*;

public class SieveTest {

    @Test
    public void seekNextPrimeFromStart() {

        Sieve<Double> sieve = new Sieve<>(new DoubleOperationStrategy());

        double result = sieve.seekNextPrime();

        assertEquals(2, result, 0);
    }

    @Test
    public void seekFirstTenPrimesAfterThree() {

        Sieve<Double> sieve = new Sieve<>(new DoubleOperationStrategy());

        double[] actual = new double[10];

        double[] expected = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, };

        for (int index = 0; index < actual.length; index++)
            actual[index] = sieve.seekNextPrime();

        assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void seekFirstTwentyPrimesAfterThree() {

        Sieve<Double> sieve = new Sieve<>(new DoubleOperationStrategy());

        double[] actual = new double[20];

        double[] expected = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, };

        for (int index = 0; index < actual.length; index++)
            actual[index] = sieve.seekNextPrime();

        assertArrayEquals(expected, actual, 0);
    }
}