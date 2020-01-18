package com.stclair.corlib.math.util;

import com.stclair.corlib.permutation.ReversingPermutationGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class ReversingPermutationGeneratorTest {

    ReversingPermutationGenerator instance = new ReversingPermutationGenerator();

    @Test
    public void ofTwoValues() {

        Integer[] original = new Integer[] { 1, 2 };

        Integer[][] expected = new Integer[][] {
                { 1, 2 },
                { 2, 1 },
        };

        List<Integer[]> result = instance.listPermutationsOf(original);

        assertEquals(expected.length, result.size());

        for (int index = 0; index < result.size(); index++)
            assertArrayEquals(expected[index], result.get(index));
    }

    @Test
    public void ofThreeValues() {

        Integer[] original = new Integer[] { 1, 2, 3 };

        Integer[][] expected = new Integer[][] {
                { 1, 2, 3 },
                { 1, 3, 2 },
                { 2, 3, 1 },
                { 2, 1, 3 },
                { 3, 1, 2 },
                { 3, 2, 1 }
        };

        List<Integer[]> result = instance.listPermutationsOf(original);

        assertEquals(expected.length, result.size());

        for (int index = 0; index < result.size(); index++)
            assertArrayEquals(expected[index], result.get(index));
    }

    @Test
    public void ofFourValues() {

        Integer[] original = new Integer[] { 1, 2, 3, 4 };

        Integer[][] expected = new Integer[][] {
                { 1, 2, 3, 4 },
                { 1, 2, 4, 3 },
                { 1, 3, 4, 2 },
                { 1, 3, 2, 4 },
                { 1, 4, 2, 3 },
                { 1, 4, 3, 2 },
                { 2, 3, 4, 1 },
                { 2, 3, 1, 4 },
                { 2, 4, 1, 3 },
                { 2, 4, 3, 1 },
                { 2, 1, 3, 4 },
                { 2, 1, 4, 3 },
                { 3, 4, 1, 2 },
                { 3, 4, 2, 1 },
                { 3, 1, 2, 4 },
                { 3, 1, 4, 2 },
                { 3, 2, 4, 1 },
                { 3, 2, 1, 4 },
                { 4, 1, 2, 3 },
                { 4, 1, 3, 2 },
                { 4, 2, 3, 1 },
                { 4, 2, 1, 3 },
                { 4, 3, 1, 2 },
                { 4, 3, 2, 1 }
        };

        List<Integer[]> result = instance.listPermutationsOf(original);

        assertEquals(expected.length, result.size());

        for (int index = 0; index < result.size(); index++)
            assertArrayEquals(expected[index], result.get(index));
    }

    @Test
    public void permutation2ReversesLastTwoValues() {

        Integer[] original = new Integer[] {1, 2, 3};

        Function<Integer[], Integer[]> func = ReversingPermutationGenerator.permutation(2);

        Integer[] result = func.apply(original);

        Assert.assertEquals(original[2], result[1]);
        Assert.assertEquals(original[1], result[2]);
    }

    @Test
    public void permutation3ReversesLastThreeValues() {

        Integer[] original = new Integer[] {1, 2, 3, 4};

        Function<Integer[], Integer[]> func = ReversingPermutationGenerator.permutation(3);

        Integer[] result = func.apply(original);

        Assert.assertEquals(original[3], result[1]);
        Assert.assertEquals(original[1], result[3]);
    }

    @Test
    public void permutation4ReversesLastFourValues() {

        Integer[] original = new Integer[] {1, 2, 3, 4, 5};

        Function<Integer[], Integer[]> func = ReversingPermutationGenerator.permutation(4);

        Integer[] result = func.apply(original);

        Assert.assertEquals(original[4], result[1]);
        Assert.assertEquals(original[3], result[2]);
        Assert.assertEquals(original[2], result[3]);
        Assert.assertEquals(original[1], result[4]);
    }

    @Test
    public void permutation5ReversesLastFiveValues() {

        Integer[] original = new Integer[] {1, 2, 3, 4, 5, 6};

        Function<Integer[], Integer[]> func = ReversingPermutationGenerator.permutation(5);

        Integer[] result = func.apply(original);

        Assert.assertEquals(original[5], result[1]);
        Assert.assertEquals(original[4], result[2]);
        Assert.assertEquals(original[2], result[4]);
        Assert.assertEquals(original[1], result[5]);
    }
}