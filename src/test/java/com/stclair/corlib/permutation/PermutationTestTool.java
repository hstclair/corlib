package com.stclair.corlib.permutation;

import com.stclair.corlib.math.util.LongOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PermutationTestTool {

    public <T> String reportSpecial(List<T[]> result) {

        OperationStrategy<Long> operationStrategy = new LongOperationStrategy();

        String max = operationStrategy.toBaseF((long) result.size());

        int width = max.length();

        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < result.size(); index++)
            sb.append(String.format("%1s: [%2s]\n", zeroPad(operationStrategy.toBaseF((long) index), width), join(result.get(index), ", ")));

        return sb.toString();
    }


    public <T> String report(List<T[]> result) {
        return report(result, ", ");
    }

    public <T> String reportWithIndex(List<T[]> result) {
        return reportWithIndex(result, ", ");
    }



    public <T> String report(List<T[]> result, String separator) {
        StringBuilder sb = new StringBuilder();

        for (T[] permutation : result)
            sb.append(String.format("[%1s]\n", join(permutation, separator)));

        return sb.toString();
    }

    public <T> String reportWithIndex(List<T[]> result, String separator) {
        StringBuilder sb = new StringBuilder();

        long permutationIndex = 0;

        for (T[] permutation : result)
            sb.append(String.format("%s: [%s]\n", permutationIndex++, join(permutation, separator)));

        return sb.toString();
    }



    public String zeroPad(String str, int width) {

        char[] padChars = new char[Math.max(0, width - str.length())];

        Arrays.fill(padChars, '0');

        String padString = new String(padChars);

        return padString.concat(str.substring(0, Math.min(str.length(), width)));
    }


    public <T> String join(T[] array, String separator) {
        return Arrays.stream(array)
                .map(Object::toString)
                .reduce((a, b) -> a.concat(separator.concat(b)))
                .orElse("");
    }

    public String join(int[] array) {
        return Arrays.stream(array)
                .mapToObj(Integer::toString)
                .reduce((a, b) -> String.format("%1s, %2s", a, b))
                .orElse("");
    }

    public <T> String reportExtraSpecial(List<T[]> permutations) {

        OperationStrategy<Long> operationStrategy = new LongOperationStrategy();

        String max = operationStrategy.toBaseF((long) permutations.size());

        int width = max.length();

        T[] original = permutations.get(0);

        StringBuilder sb = new StringBuilder();

        for (int index = 0; index < permutations.size(); index++)
            sb.append(String.format("%1s: [%2s] [%3s]\n", zeroPad(operationStrategy.toBaseF((long) index), width), join(permutations.get(index), ", "), describeRings(original, permutations.get(index))));

        return sb.toString();
    }

    public <T> String describeRings(T[] original, T[] permutation) {

        List<Integer[]> rings = mapRings(original, permutation);

        return describe(rings);
    }

    public String describe(List<Integer[]> rings) {

        List<String> descriptions = new ArrayList<>();

        for (Integer[] ring : rings)
            descriptions.add(join(ring, "->"));

        return join(descriptions.toArray(new String[0]), "  ");
    }

    public <T> List<Integer[]> mapRings(T[] original, T[] permutation) {

        List<Integer[]> rings = new ArrayList<>();

        boolean done = false;

        List<Integer> available = IntStream.range(0, original.length)
                .map(it -> original.length - it - 1)
                .boxed()
                .collect(Collectors.toList());

        while (! done) {
            Integer start = findRing(available, original, permutation);

            if (start == null)
                done = true;
            else
                mapRing(start, rings, available, original, permutation);
        }

        return rings;
    }

    public <T> Integer findRing(List<Integer> available, T[] original, T[] permutation) {

        while (! available.isEmpty()) {
            Integer start = available.remove(0);

            if (! original[start].equals(permutation[start]))
                return start;
        }

        return null;
    }



    public <T> void mapRing(Integer start, List<Integer[]> rings, List<Integer> available, T[] original, T[] permutation) {

        int max = permutation.length - 1;

        List<Integer> elements = new ArrayList<>();

        Integer current = start;

        while (! elements.contains(max - current)) {

            available.remove(current);
            elements.add(max - current);

            current = find(original[current], permutation);
        }

        Integer[] ring = elements.toArray(new Integer[0]);

        rings.add(ring);
    }

    public <T> Integer find(T value, T[] array) {
        for (int index = 0; index < array.length; index++) {
            if (array[index].equals(value))
                return index;
        }

        return -1;
    }
}
