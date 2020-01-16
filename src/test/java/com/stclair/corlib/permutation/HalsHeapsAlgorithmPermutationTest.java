package com.stclair.corlib.permutation;

import com.stclair.corlib.math.util.MoreMath;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class HalsHeapsAlgorithmPermutationTest {

    Permutations reversing = new ReversingPermutations();

    HalsHeapsAlgorithmPermutation instance = new HalsHeapsAlgorithmPermutation();

    PermutationTestTool testTool = new PermutationTestTool();

    @Test
    public void permutation() {


    }

    @Test
    public void reportOutputFromHeapsRecursive() {

        Permutations permutations = new HeapsAlgorithmNonrecursive();

        String[] values = { "d", "e", "f", "g", "h" };

        System.out.println(testTool.report(permutations.of(values), ""));
    }

    @Ignore
    @Test
    public void reportOutputFromReversing() {


        String[] values = { "a", "b", "c", "d", "e", "f" };

        System.out.println(testTool.reportWithIndex(reversing.of(values), ""));
    }



    @Ignore
    @Test
    public void firstTwentyFour() {

        // permutations of odd: last, (all other elements in original order), first
        // start next sequence by swapping first and last

        //00:  0: abcde
        //23:  1: aebcd => debca
        //47:  2: daebc => caebd
        //71:  3: cdaeb => bdaec
        //95:  4: bcdae => ecdab
        //119:    ebcda


        // permutations of even: last, first, (all other elements in original order), first+1, first+2
        // start next sequence (sequence # n from 1 to elementCount-1) by swapping first and (n-1)th from last

        //00:  0: abcdef
        //119: 1: afcdeb => bfcdea
        //239: 2: bacdef => eacdbf
        //359: 3: efcdba => dfceba
        //479: 4: dacebf => cadebf
        //599: 5: cfdeba => fcdeba
        //719:    fadebc

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        HalsHeapsAlgorithmIterator<Character> iterator = new HalsHeapsAlgorithmIterator<>(start);

        int permutationIndex = 0;

        long interval = MoreMath.factorial(start.length - 1);

        for (Character[] current = start; iterator.hasNext(current); current = iterator.next(current)) {

            if (permutationIndex % interval == 0 || (permutationIndex + 1) % interval == 0) {
                String str = String.join("", Arrays.stream(current).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

                System.out.printf("%02d: %2s\n", permutationIndex, str);

            }

            permutationIndex++;
        }
    }

    @Ignore
    @Test
    public void first24PermutationsOfFourElementsNoniterative() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> expectedResults = instance.of(start);

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expected = expectedResults.get(index);

            assertArrayEquals(expected, instance.permutation(start, index));
        }

    }

    @Ignore
    @Test
    public void first120PermutationsOfFiveElementsNoniterative() {

        Character[] start = "abc".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> expectedResults = instance.of(start);

        for (int index = 0; index < expectedResults.size(); index++) {

            Character[] expected = expectedResults.get(index);

            String correct = String.join("", Arrays.stream(expected).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            String actual = String.join("", Arrays.stream(instance.permutation(start, index)).reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append));

            System.out.printf("%02d: %2s %3s\n", index, correct, actual);

            assertArrayEquals(expected, instance.permutation(start, index));
        }

    }

    @Test
    public void first24PermutationsOfFourElementsIterative() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> actual = instance.of(start);

        for (int index = 0; index < actual.size(); index++) {

            Character[] array = actual.get(index);
        }

    }

    @Ignore
    @Test
    public void rotationsOfSevenElementsIterative() {

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> permutations = instance.of(start);

        long permutationIndex = 0;

        for (Character[] array : permutations) {

            int[] rotations = instance.findRotations(array, start);
            int[] baseF = Arrays.copyOf(instance.toBaseF(permutationIndex), rotations.length);

            String actual = Arrays.stream(rotations)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "));

            String baseFStr = Arrays.stream(baseF)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "));

            System.out.printf("  { %s },   // %s\n", actual, baseFStr);

            permutationIndex++;
        }

    }

    @Ignore
    @Test
    public void differencesInRotationsOfSevenElementsIterative() {

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> permutations = instance.of(start);

        long permutationIndex = 0;

        for (Character[] array : permutations) {

            int[] rotations = instance.findRotations(array, start);
            int[] baseF = Arrays.copyOf(instance.toBaseF(permutationIndex), rotations.length);

            String baseFStr = Arrays.stream(baseF)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "));

            String deltas = IntStream.range(0, baseF.length - 1)
                    .map(index -> (2 + index + rotations[index] - baseF[index]) % (2 + index))
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "));

            System.out.printf("  { %s },   // %s\n", deltas, baseFStr);

            permutationIndex++;
        }

    }

    @Ignore
    @Test
    public void testGreyCode() {
        for (long index = 0; index < 16; index++)
            System.out.println(String.format("%4s", Long.toBinaryString(instance.toGreyCode(index))).replace(' ', '0'));
    }

    @Ignore
    @Test
    public void differencesInRotation2OfNineElementsIterative() {

        Character[] start = "abcdefghi".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> permutations = instance.of(start);

        long permutationIndex = 0;

        for (Character[] array : permutations) {

            int[] rotations = instance.findRotations(array, start);
            int[] baseF = Arrays.copyOf(instance.toBaseF(permutationIndex), rotations.length);

            int value = (2 + rotations[0] - baseF[0]) % 2;

            if (permutationIndex % (2*3*4*5) == 0)
                System.out.println();

            if (permutationIndex % 6 == 0)
                System.out.printf("%s", value);

            permutationIndex++;
        }

        System.out.println();
    }

    @Ignore
    @Test
    public void rotationsOfNineElementsIterativeBackToLong() {

        Character[] start = "abcdefghi".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> permutations = instance.of(start);

        for (long permutationIndex = 0; permutationIndex < 720; permutationIndex++) {

            Character[] array = permutations.get((int) permutationIndex);

            int[] rotations = instance.findRotations(array, start);

            long value = rotations[0] + rotations[1]*2 + rotations[2]*6 + rotations[3]*24 + rotations[4]*120 + rotations[5]*720 + rotations[6]*7*720 + rotations[7]*7*8*720;

            System.out.printf("%s\n", value);
        }

        System.out.println();
    }

    @Ignore
    @Test
    public void rotationsOfNineElementsIterativeToBinary() {

        Character[] start = "abcdefghi".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        List<Character[]> permutations = instance.of(start);

        for (long permutationIndex = 0; permutationIndex < 720; permutationIndex++) {

            Character[] array = permutations.get((int) permutationIndex);

            int[] rotations = instance.findRotations(array, start);

            long value = rotations[0] + rotations[1]*2 + rotations[2]*6 + rotations[3]*24 + rotations[4]*120 + rotations[5]*720 + rotations[6]*7*720 + rotations[7]*7*8*720;

            String valueBinary = String.format("%16s", Long.toBinaryString(value)).replace(' ', '0');

            System.out.printf("%03d\t%03d\t%s\n", permutationIndex, value, valueBinary);
        }

        System.out.println();
    }

    @Test
    public void applyRotations() {
    }

    @Test
    public void convert23toBaseF() {

        long value = 23;

        int[] expected = { 1, 2, 3 };

        int[] actual = instance.toBaseF(value);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentLeftRotatesThreeElementsOneStep() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abdec".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentLeft(chars, 3, 1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentLeftNegativeCountRotatesByModulus() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abecd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentLeft(chars, 3, -1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentLeftCountMultipleOfSegmentLengthRotatesByModulus() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abdec".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentLeft(chars, 3, 4);

        assertArrayEquals(expected, actual);
    }

    public int[] reverse(int[] array) {
        int[] result = new int[array.length];

        for (int index = 0; index < result.length; index++)
            result[index] = array[array.length - index - 1];

        return result;
    }

    @Ignore
    @Test
    public void testBaseFToRotations() {

        for (int permutationIndex = 0; permutationIndex < 50; permutationIndex++) {
            int[] baseF = instance.toBaseF(permutationIndex);

            int[] rotations = instance.baseFToRotations(permutationIndex, baseF);

            rotations = reverse(rotations);

            String actual = Arrays.stream(rotations)
                    .mapToObj(Integer::toString)
                    .collect(Collectors.joining(", "));

            System.out.printf("  { %2s },\n", actual);

        }
    }

    @Test
    public void rotateSegmentRightNegativeCountRotatesByModulus() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abdec".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentRight(chars, 3, -1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentRightCountMultipleOfSegmentLengthRotatesByModulus() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abecd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentRight(chars, 3, 4);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentLeftRotatesFiveElementsTwoSteps() {

        Character[] chars = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcfghde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentLeft(chars, 5, 2);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentRightRotatesThreeElementsOneStep() {

        Character[] chars = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abecd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentRight(chars, 3, 1);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void rotateSegmentRightRotatesFiveElementsTwoSteps() {

        Character[] chars = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcghdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.rotateSegmentRight(chars, 5, 2);

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

        Character[] actual = instance.reconstructSubpermutationStartState(4, 3, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructSubpermutationEndStateFourOfThreeElements() {

        Character[] start = "abcdefgh".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] expected = "abcdhefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        Character[] actual = instance.reconstructSubpermutationStartState(4, 4, start);

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

        Character[] actual = instance.reconstructSubpermutationStartState(5, 5, start);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReconstructStartStateReconstructsPermutation0Of24() {

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

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

        Character[] actual = instance.reconstructStartState(1, start);

        assertArrayEquals(expected, actual);
    }


    @Test
    public void testReconstructStartStateReconstructsFirst24() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcd".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        instance.streamOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst120() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcde".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        instance.streamOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }

    @Test
    public void testReconstructStartStateReconstructsFirst720() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdef".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        instance.streamOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }


    @Test
    public void testReconstructStartStateReconstructsFirst5040() {

        long[] permutationIndex = { 0 };

        Character[] start = "abcdefg".chars()
                .mapToObj(it -> (char) it)
                .toArray(Character[]::new);

        instance.streamOf(start)
                .forEach(it ->
                        assertArrayEquals(it, instance.reconstructStartState(permutationIndex[0]++, start)));
    }
}