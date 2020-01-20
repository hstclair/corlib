package com.stclair.corlib.permutation;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HeapsAlgorithmPermutationIteratorTest {

    PermutationTestTool testTool = new PermutationTestTool();


    @Test
    public void testStartPermutationsOf24AtOffset3() {

        int startIndex = 3;

        String initialString = "abcd";

        Character[] initialValues = initialString.chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new PermutationGenerator(new HeapsAlgorithmPermutationConstructor(), new HeapsAlgorithmPermutationIteratorFactory());

        List<String> expected =
                generator
                        .streamPermutationsOf(initialValues)
                        .map(HeapsAlgorithmPermutationIteratorTest::characterArrayToString)
                        .skip(startIndex)
                .collect(Collectors.toList());

        HeapsAlgorithmPermutationIterator instance = new HeapsAlgorithmPermutationIterator(startIndex);

        List<String> actual = new ArrayList<>();

        for (Character[] current = instance.getSeed(initialValues); instance.hasNext(current); current = instance.next(current))
            actual.add(characterArrayToString(current));

        assertEquals(expected, actual);
        assertEquals(expected.size(), actual.size());
    }

    public static String characterArrayToString(Character[] array) {
        return Arrays.stream(array)
            .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
            .toString();
    }
}