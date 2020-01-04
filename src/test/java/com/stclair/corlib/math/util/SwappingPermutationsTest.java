package com.stclair.corlib.math.util;

import com.stclair.corlib.permutation.HalsHeapsAlgorithmPermutation;
import com.stclair.corlib.permutation.Permutations;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SwappingPermutationsTest {

    @Test
    public void of() {

        Integer[] array = { 1, 2, 3, };

        List<Integer[]> expected = Arrays.asList(
                new Integer[]{ 1, 2, 3 },
                new Integer[]{ 1, 3, 2 },
                new Integer[]{ 2, 3, 1 },
                new Integer[]{ 2, 1, 3 },
                new Integer[]{ 3, 1, 2 },
                new Integer[]{ 3, 2, 1 });

        Permutations permutations = new HalsHeapsAlgorithmPermutation();

        List<Integer[]> actual = permutations.of(array);

        for (int index = 0; index < actual.size(); index++)
            assertArrayEquals(expected.get(index), actual.get(index));
    }

    public <T> String report(List<Integer[]> result) {
        StringBuilder sb = new StringBuilder();

        for (Integer[] permutation : result)
            sb.append(String.format("[%1s]\n", join(permutation)));

        return sb.toString();
    }

    public <T> String join(T[] array) {
        return Arrays.stream(array)
                .map(Object::toString)
                .reduce((a, b) -> String.format("%1s, %2s", a, b))
                .orElse("");
    }
}