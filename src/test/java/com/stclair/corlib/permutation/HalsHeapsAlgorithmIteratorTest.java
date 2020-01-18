package com.stclair.corlib.permutation;

import com.stclair.corlib.math.util.MoreMath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class HalsHeapsAlgorithmIteratorTest {

    ReversingPermutationGenerator reversing = new ReversingPermutationGenerator();

    PermutationTestTool testTool = new PermutationTestTool();

    @Test
    public void testReconstructSubpermutationsStartStateEven() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> iterator = new HalsHeapsAlgorithmIterator<>(start);

        int permutationIndex = 0;

        int subpermutationIndex = 0;

        long interval = MoreMath.factorial(start.length - 1);

        for (Character[] current = start; iterator.hasNext(current); current = iterator.next(current)) {

            if (permutationIndex % interval == 0) {

                String expected = String.join("", Arrays.stream(current).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                Character[] computed = iterator.reconstructSubpermutationStartStateEven(start.length, subpermutationIndex, start);

                String actual = String.join("", Arrays.stream(computed).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                assertEquals(expected, actual);

                subpermutationIndex++;
            }

            permutationIndex++;
        }
    }

    @Test
    public void testReconstructSubpermutationsStartStateOdd() {

        Character[] start = "abcdefghi".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> iterator = new HalsHeapsAlgorithmIterator<>(start);

        int permutationIndex = 0;

        int subpermutationIndex = 0;

        long interval = MoreMath.factorial(start.length - 1);

        for (Character[] current = start; iterator.hasNext(current); current = iterator.next(current)) {

            if (permutationIndex % interval == 0) {

                String expected = String.join("", Arrays.stream(current).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                Character[] computed = iterator.reconstructSubpermutationStartStateOdd(start.length, subpermutationIndex, start);

                String actual = String.join("", Arrays.stream(computed).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

//                System.out.printf("%s %s\n", expected, actual);

                assertEquals(expected, actual);

                subpermutationIndex++;
            }

            permutationIndex++;
        }
    }

    @Test
    public void first24PermutationsOfFourElementsNoniterative() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> iterator = new HalsHeapsAlgorithmIterator<>(start);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        List<Character[]> expectedResults = generator.listPermutationsOf(start);

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expected = expectedResults.get(index);

            assertArrayEquals(expected, iterator.reconstructStartState(index, start));
        }

    }

    @Test
    public void first120PermutationsOfFiveElementsNoniterative() {

        Character[] start = "abcef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> iterator = new HalsHeapsAlgorithmIterator<>(start);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        List<Character[]> expectedResults = generator.listPermutationsOf(start);

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expectedArray = expectedResults.get(index);

            String expected = String.join("", Arrays.stream(expectedArray).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            String actual = String.join("", Arrays.stream(iterator.reconstructStartState(index, start)).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            assertEquals(expected, actual);
        }

    }

    @Test
    public void first24PermutationsOfFourElementsIterative() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        List<Character[]> actual = generator.listPermutationsOf(start);

        for (int index = 0; index < actual.size(); index++) {

            Character[] array = actual.get(index);
        }

    }


    @Test
    public void convert23toBaseF() {

        long value = 23;

        int[] expected = { 1, 2, 3 };

        HalsHeapsAlgorithmIterator<Object> instance = new HalsHeapsAlgorithmIterator<>(new Object[1]);

        int[] actual = instance.toBaseF(value);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationsOfTwo() {

        Character[] start = "ab".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "ba".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfThree() {

        Character[] start = "abc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "cba".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfFour() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "dabc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfFive() {

        Character[] start = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "ebcda".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfSix() {

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "fadebc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfSeven() {

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "gbcdefa".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructEndStateAfterPermutationOfEight() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "hadefgbc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructEndState(start.length, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateOneOfThreeElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdfhge".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(4, 1, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateTwoOfThreeElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdgefh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(4, 2, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateThreeOfThreeElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdhgfe".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(4, 3, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateOneOfFourElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcghefd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(5, 1, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateTwoOfFourElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcfdheg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(5, 2, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateThreeOfFourElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcegdhf".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(5, 3, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateFourOfFourElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abchfgde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(5, 4, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateFiveOfFourElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructSubpermutationStartState(5, 5, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation0Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(0, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(1, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(2, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(3, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(4, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(5, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation6Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "bdca".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(6, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(2, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(0, start);

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

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        Character[] actual = instance.reconstructStartState(1, start);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testReconstructStartStateReconstructsFirst24() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst120() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst720() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }


    @Test
    public void testReconstructStartStateReconstructsFirst5040() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst40320() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HalsHeapsAlgorithmPermutationGenerator();

        HalsHeapsAlgorithmIterator<Character> instance = new HalsHeapsAlgorithmIterator<>(start);

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }
}