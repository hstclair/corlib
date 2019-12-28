package com.stclair.corlib.math.util;

import com.stclair.corlib.permutation.HalsHeapsAlgorithmPermutation;
import com.stclair.corlib.permutation.HeapsAlgorithmNonrecursive;
import com.stclair.corlib.permutation.HeapsAlgorithmRecursive;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HeapsAlgorithmTest {

    HeapsAlgorithmRecursive recursivePermutation = new HeapsAlgorithmRecursive();

    HeapsAlgorithmNonrecursive nonrecursivePermutation = new HeapsAlgorithmNonrecursive();

    HalsHeapsAlgorithmPermutation halsPermutation = new HalsHeapsAlgorithmPermutation();

    @Test
    public void ofReturnsSameResultForRecursiveAndNonRecursive() {

        Integer[] values = { 1, 2, 3, 4 };

        String reportA = report(nonrecursivePermutation.of(values));

        String reportB = report(recursivePermutation.of(values));

        assertEquals(reportA, reportB);
    }

    @Test
    public void ofReturnsSameResultForRecursiveAndHals() {

        Integer[] values = { 1, 2, 3, 4, };


        String reportA = report(recursivePermutation.of(values));

        String reportB = report(halsPermutation.of(values));

        assertEquals(reportA, reportB);
    }

    @Test
    public void reportOutputFromHeapsRecursive() {
        String[] values = { "Here", "I", "Am", "Now" };

        System.out.println(report(recursivePermutation.of(values)));
    }

    public <T> String report(List<T[]> result) {
        StringBuilder sb = new StringBuilder();

        for (T[] permutation : result)
            sb.append(String.format("[%1s]\n", join(permutation)));

        return sb.toString();
    }

    public <T> String join(T[] array) {
        return Arrays.stream(array)
                .map(Object::toString)
                .reduce((a, b) -> String.format("%1s, %2s", a, b))
                .orElse("");
    }

    public String join(int[] array) {
        return Arrays.stream(array)
                .mapToObj(Integer::toString)
                .reduce((a, b) -> String.format("%1s, %2s", a, b))
                .orElse("");
    }


    @Test
    public void testComputeNextPair() {
        int[] register = new int[4];

        int[] pair;

        while ((pair = halsPermutation.computeNextPair(register)) != null) {

            System.out.printf("swap: %s:%s -> [%s]\n", pair[0], pair[1], join(register));
        }
    }

}