package com.stclair.corlib.permutation;

import com.stclair.corlib.math.util.MoreMath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ReversingPermutationConstructorTest {

    ReversingPermutationIterator iterator = new ReversingPermutationIterator();

    ReversingPermutationConstructor instance = new ReversingPermutationConstructor();

    @Test
    public void first120PermutationsOfFiveElementsNoniterative() {

        Character[] start = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        List<Character[]> expectedResults = Stream
                .iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .collect(Collectors.toList());

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expectedArray = expectedResults.get(index);

            String expected = String.join("", Arrays.stream(expectedArray).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            String actual = String.join("", Arrays.stream(instance.constructPermutation(start, index)).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            assertEquals(expected, actual);
        }

    }

    @Test
    public void first24PermutationsOfFourElementsIterative() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new PermutationGenerator(new ReversingPermutationConstructor(), new ReversingPermutationIteratorFactory());

        List<Character[]> actual = generator.listPermutationsOf(start);

        for (int index = 0; index < actual.size(); index++) {

            Character[] array = actual.get(index);
        }
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation0Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 0);

        assertArrayEquals(start, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation1Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abdc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation2Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "acdb".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 2);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation3Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "acbd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 3);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testReconstructStartStateReconstructsPermutation4Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "adbc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 4);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation5Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "adcb".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 5);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation6Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "bcda".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 6);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation2Of6() {

        Character[] start = "abc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "bca".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 2);

        assertArrayEquals(expected, actual);
    }



    @Test
    public void testReconstructStartStateReconstructsPermutation0Of2() {

        Character[] start = "ab".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "ab".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 0);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testReconstructStartStateReconstructsPermutation1Of2() {

        Character[] start = "ab".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "ba".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(start, 1);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testReconstructStartStateReconstructsFirst24() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        Stream.iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));

        assertEquals(24, permutationIndex[0]);
    }

    @Test
    public void testReconstructStartStateReconstructsFirst120() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        Stream.iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst720() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        Stream.iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }


    @Test
    public void testReconstructStartStateReconstructsFirst5040() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        Stream.iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst40320() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        ReversingPermutationIterator iterator = new ReversingPermutationIterator();

        Stream.iterate(iterator.getSeed(start), iterator::hasNext, iterator::next)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }
}
