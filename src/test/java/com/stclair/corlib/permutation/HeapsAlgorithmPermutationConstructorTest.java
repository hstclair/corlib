package com.stclair.corlib.permutation;

import com.stclair.corlib.math.util.MoreMath;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HeapsAlgorithmPermutationConstructorTest {

    HeapsAlgorithmPermutationIterator iterator = new HeapsAlgorithmPermutationIterator();

    HeapsAlgorithmPermutationConstructor instance = new HeapsAlgorithmPermutationConstructor();


    @Test
    public void testReconstructSubpermutationsStartStateEven() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        int permutationIndex = 0;

        int subpermutationIndex = 0;

        long interval = MoreMath.factorial(start.length - 1);

        for (Character[] current = start; iterator.hasNext(current); current = iterator.next(current)) {

            if (permutationIndex % interval == 0) {

                String expected = String.join("", Arrays.stream(current).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                Character[] computed = instance.constructPermutationEven(start.length, subpermutationIndex, start);

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

        int permutationIndex = 0;

        int subpermutationIndex = 0;

        long interval = MoreMath.factorial(start.length - 1);

        for (Character[] current = start; iterator.hasNext(current); current = iterator.next(current)) {

            if (permutationIndex % interval == 0) {

                String expected = String.join("", Arrays.stream(current).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                Character[] computed = instance.constructPermutationOdd(start.length, subpermutationIndex, start);

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

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        List<Character[]> expectedResults = generator.listPermutationsOf(start);

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expected = expectedResults.get(index);

            assertArrayEquals(expected, instance.constructPermutation(start, index));
        }

    }

    @Test
    public void first120PermutationsOfFiveElementsNoniterative() {

        Character[] start = "abcef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        List<Character[]> expectedResults = generator.listPermutationsOf(start);

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

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        List<Character[]> actual = generator.listPermutationsOf(start);

        for (int index = 0; index < actual.size(); index++) {

            Character[] array = actual.get(index);
        }

    }

    @Test
    public void testReconstructSubpermutationEndStateOneOfThreeElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdfhge".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.constructPermutation(4, 1, start);

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

        Character[] actual = instance.constructPermutation(4, 2, start);

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

        Character[] actual = instance.constructPermutation(4, 3, start);

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

        Character[] actual = instance.constructPermutation(5, 1, start);

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

        Character[] actual = instance.constructPermutation(5, 2, start);

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

        Character[] actual = instance.constructPermutation(5, 3, start);

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

        Character[] actual = instance.constructPermutation(5, 4, start);

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

        Character[] actual = instance.constructPermutation(5, 5, start);

        assertArrayEquals(expected, actual);
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

        Character[] expected = "bdca".chars()
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

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        generator.streamPermutationsOf(start)
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

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst720() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }


    @Test
    public void testReconstructStartStateReconstructsFirst5040() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst40320() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        PermutationGenerator generator = new HeapsAlgorithmPermutationGenerator();

        generator.streamPermutationsOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.constructPermutation(start, permutationIndex[0]++)));
    }
}
