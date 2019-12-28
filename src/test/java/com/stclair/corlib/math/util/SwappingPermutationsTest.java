package com.stclair.corlib.math.util;

import com.stclair.corlib.permutation.HalsHeapsAlgorithmPermutation;
import com.stclair.corlib.permutation.Permutations;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SwappingPermutationsTest {

    @Test
    public void of() {

        Integer[] array = { 1, 2, 3, 4, 5, 6, 7 };

        Permutations permutations = new HalsHeapsAlgorithmPermutation();

        List<Integer[]> result = permutations.of(array);

        for (Integer[] permutation : result)
            System.out.printf("[%1s]\n", join(permutation));
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


    @Test
    public void swap() {
    }
}