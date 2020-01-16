package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HalsHeapsAlgorithmPermutation implements Permutations {

    int[][] rotations = {
            { 0, 0, 0, 0, 0, 0 },   // 0, 0, 0, 0, 0, 0
            { 1, 0, 0, 0, 0, 0 },   // 1, 0, 0, 0, 0, 0
    };

    public <T> T[] permutationSav(T[] values, long index) {

        int[] baseF = toBaseF(index);

        return  applyRotations(index, values, baseF);
//        return applyRotations(values, baseF);
    }

    public long toGreyCode(long value) {
        return value ^ (value >> 1);
    }


    public <T> T[] permutation(T[] values, long index) {

        switch ((int) index) {
            case 0:
                return values;

            case 1:
                return rotateSegmentLeft(values, 2, 1);

            case 2:
                return rotateSegmentLeft(values, 3, 1);

            case 3:
                return rotateSegmentLeft(rotateSegmentLeft(values, 3, 1), 2, 1);

            case 4:
                return rotateSegmentLeft(values, 3, 2);

            case 5:
                return rotateSegmentLeft(rotateSegmentLeft(values, 3, 2), 2, 1);

            case 6:
                // BDCA
                return rotateSegmentLeft(rotateSegmentLeft(rotateSegmentLeft(values, 4, 1), 3, 1), 2, 1);
//                return rotateSegmentLeft(rotateSegmentLeft(values,2, 1), 4, 1);
//                return rotateSegmentRight(rotateSegmentRight(rotateSegmentLeft(values, 3, 1), 4, 1), 2, 1);

            case 7:
                // BDAC
                return rotateSegmentLeft(rotateSegmentLeft(rotateSegmentLeft(values, 4, 1), 3, 1), 2, 0);
//                return rotateSegmentLeft(rotateSegmentLeft(rotateSegmentLeft(values, 2, 1), 4, 1), 2, 1);

            case 8:
                // BCAD

            case 9:
                // BCDA

            case 10:
                // BADC

            case 11:
                // BACD

            case 12:
                // CABD

            case 13:
                // CADB

            case 14:
                // CBDA

            case 15:
                // CBAD

            case 16:
                // CDAB

            case 17:
                // CDBA

            case 18:
                // DCBA

            case 19:
                // DCAB

            case 20:
                // DBAC

            case 21:
                // DBCA

            case 22:
                // DACB

            case 23:
                // DABC

            default:
                return values;
        }
    }


    // ABCD
    // ABDC
    // BDCA


    // keyser soze

    // keys rozees
    // see keys zor




    public <T> int[] findRotations(T[] values, T[] original) {

        T[] rotated = original;

        int[] rotations = new int[values.length - 1];

        for (int index = values.length - 1; index > 0; index--) {

            while (! values[values.length - index - 1].equals(rotated[rotated.length - index - 1])) {
                rotations[index - 1]++;
                rotated = rotateSegmentLeft(rotated, index + 1, 1);
            }
        }

        return rotations;
    }

    public <T> T[] applyRotation(long permutationIndex, T[] values, int[] baseF, int index) {

        if (index >= baseF.length)
            return values;

        int rotateCount = baseF[index];
        int segmentLength = index + 2;


        if (rotateCount == 0)
            return values;

        return rotateSegmentLeft(values, segmentLength, rotateCount);
    }

    public int[] baseFToRotations(long permutationIndex, int[] baseF) {

        if (permutationIndex < rotations.length)
            return rotations[(int)permutationIndex];

        int f4 = 0;
        int f3 = 0;
        int f2 = 0;
        int f1 = 0;

        if (baseF.length >= 1)
            f1 = baseF[0];

        if (baseF.length >= 2)
            f2 = baseF[1];

        if (baseF.length >= 3)
            f3 = baseF[2];

        if (baseF.length >= 4)
            f4 = baseF[3];

        int parityValueF3 = f3 & 1;
        int paritySign = 1 - (parityValueF3 << 1);

        int rotation5 = 0;
        int rotation4 = f3;
        int rotation3 = (f3 >> 1) + parityValueF3 + 3 + paritySign * f2;
        int rotation2 = (f1 + parityValueF3) & 1;

        if (permutationIndex > 23) {
            if (permutationIndex < 48) {
                rotation5 = 3;

                if (permutationIndex < 30) {
                    rotation3 = rotation3 + 1;
                } else if (permutationIndex < 36) {
                    rotation4 = 2;
                    rotation3 = 3 - rotation3;
                    rotation2 = (2 - (rotation2 + 1) & 1);
                } else if (permutationIndex < 42) {
                    rotation4 = 3;
                    rotation3 = 1 - rotation3;
                    rotation2 = (2 - (rotation2 + 1) & 1);
                } else {
                    rotation4 = 1;
                    rotation3 = rotation3 + 2;
                }
            } else if (permutationIndex < 72) {
                rotation5 = 2;
                rotation4 = 2;
                rotation3 = 2;
            }
        }

        rotation2 = rotation2 & 1;
        rotation3 = ((rotation3 % 3) + 3) % 3;
        rotation4 = ((rotation4 % 4) + 4) % 4;
        rotation5 = ((rotation5 % 5) + 5) % 5;

        return new int[] { rotation2, rotation3, rotation4, rotation5 };
    }

    public <T> T[] applyRotations(long permutationIndex, T[] values, int[] baseF) {

//        int f4 = 0;
//        int f3 = 0;
//        int f2 = 0;
//        int f1 = 0;
//
//        if (baseF.length >= 1)
//            f1 = baseF[0];
//
//        if (baseF.length >= 2)
//            f2 = baseF[1];
//
//        if (baseF.length >= 3)
//            f3 = baseF[2];
//
//        if (baseF.length >= 4)
//            f4 = baseF[3];
//
//        int parityValueF3 = f3 & 1;
//        int paritySign = 1 - (parityValueF3 << 1);
//
//        int rotation5 = 0;
//        int rotation4 = f3;
//        int rotation3 = (f3 >> 1) + parityValueF3 + 3 + paritySign * f2;
//        int rotation2 = (f1 + parityValueF3) & 1;
//
//        if (permutationIndex > 23) {
//            if (permutationIndex < 48) {
//                rotation5 = 3;
//
//                if (permutationIndex < 30) {
//                    rotation3 = rotation3 + 1;
//                } else if (permutationIndex < 36) {
//                    rotation4 = 2;
//                    rotation3 = 3 - rotation3;
//                    rotation2 = (2 - (rotation2 + 1) & 1);
//                } else if (permutationIndex < 42) {
//                    rotation4 = 3;
//                    rotation3 = 1 - rotation3;
//                    rotation2 = (2 - (rotation2 + 1) & 1);
//                } else {
//                    rotation4 = 1;
//                    rotation3 = rotation3 + 2;
//                }
//            } else if (permutationIndex < 72) {
//                rotation5 = 2;
//                rotation4 = 2;
//                rotation3 = 2;
//            }
//        }


        int[] rotations = baseFToRotations(permutationIndex, baseF);


        if (values.length >= 5)
            values = rotateSegmentLeft(values, 5, rotations[3]);

        if (values.length >= 4)
            values = rotateSegmentLeft(values, 4, rotations[2]);

        if (values.length >= 3)
            values = rotateSegmentLeft(values, 3, rotations[1]);

        if (values.length >= 2)
            values = rotateSegmentLeft(values, 2, rotations[0]);

        return values;
    }

    public <T> T[] applyRotations(T[] values, int[] baseF) {

        int last = 0;



        for (int index = baseF.length - 1; index >= 0; index--) {

            int rotateCount = baseF[index];
            int segmentLength = index + 2;

            if (baseF.length != 3 || baseF[2] != 1) {
                if (rotateCount != 0)
                    values = rotateSegmentLeft(values, segmentLength, rotateCount);
            } else {
                if (segmentLength == 4)
                    values = rotateSegmentLeft(values, segmentLength, rotateCount);
                else
                    values = rotateSegmentRight(values, segmentLength, segmentLength - rotateCount - 1);
            }



//            boolean evenLength = (count & 1) == 0;
//            boolean oddCount = (last & 1) != 0;

//            if (rotateCount != 0) {
//                if (! ((evenLength || oddCount) && (! (evenLength && oddCount))))
//                if (evenLength)
//                    values = rotateSegmentLeft(values, segmentLength, rotateCount);
//                else
//                    values = rotateSegmentRight(values, count, steps);
//            }

            last = rotateCount;
        }

        return values;
    }

    public int[] toBaseF(long index) {

        long divisor = 2;

        List<Integer> digits = new ArrayList<>();

        while (index > 0) {

            int modulus = (int) (index % divisor);

            digits.add(modulus);

            index = index / divisor;

            divisor++;
        }

        return digits.stream()
                .mapToInt(digit -> digit)
                .toArray();
    }

    public <T> T[] rotateSegmentLeft(T[] values, int segmentLength, int count) {

        T[] result = Arrays.copyOf(values, values.length);

        if (count > segmentLength)
            count = count % segmentLength;
        else if (count < 0)
            count = count % segmentLength + segmentLength;

        System.arraycopy(values, (values.length - segmentLength) + count, result, result.length - segmentLength, segmentLength - count);
        System.arraycopy(values, values.length - segmentLength, result, result.length - count, count);

        return result;
    }

    public <T> T[] rotateSegmentRight(T[] values, int segmentLength, int count) {

        T[] result = Arrays.copyOf(values, values.length);

        if (count > segmentLength)
            count = count % segmentLength;
        else if (count < 0)
            count = count % segmentLength + segmentLength;

        System.arraycopy(values, values.length - segmentLength, result, (result.length - segmentLength) + count, segmentLength - count);
        System.arraycopy(values, values.length - count, result, result.length - segmentLength, count);

        return result;
    }

    @Override
    public <T> List<T[]> of(T[] values) {

        return streamOf(values).collect(Collectors.toList());
    }

    public <T> Stream<T[]> streamOf(T[] values) {

        HalsHeapsAlgorithmIterator<T> iterator = new HalsHeapsAlgorithmIterator<>(values);

        return Stream.iterate(values, iterator::hasNext, iterator::next);
    }

    public int[] computeNextPair(int[] alternateElementPosRegister) {

        int index = 0;

        while (index < alternateElementPosRegister.length) {

            int alternateElementPos = alternateElementPosRegister[index];

            int elementPos = index + 1;

            if (alternateElementPos < elementPos) {
                alternateElementPosRegister[index] = alternateElementPos + 1;

                return new int[] {elementPos, alternateElementPos};
            }

            alternateElementPosRegister[index++] = 0;
        }

        return null;
    }


    /**
     * reconstruct the state at the end of a single set of permutations
     * for the given length
     * @param length the length of the series of elements in the permutation
     * @param sequence the sequence of elements before the permutation
     * @param <T> the type of element
     * @return the sequence of elements following the permutation
     */
    public <T> T[] reconstructEndState(int length, T[] sequence) {

        // permutations of odd-length subsequence: last, (all other elements in original order), first
        // start next outer sequence (sequence # n from 0 to elementCount - 1) by swapping first and nth
        //
        // after first iteration, outer sequence is:
        //   inner first, last, (all other inner elements in original order), outer first
        //
        // after second iteration, outer sequence is:
        //   last - 1, outer first, (all other inner elements in original order), inner first, last
        //
        // after third iteration, outer sequence is:
        //   last - 2, last, (all other inner elements in original order), last - 1, inner first, outer first
        //
        // after fourth iteration, outer sequence is:
        //   last - 3, outer first, (all other inner elements in original order), last - 2, last - 1, inner first, last
        //
        //



        // TODO: VERIFY THIS AND IMPLEMENT IF TRUE!
        //
        // after first iteration over odd-length(?) sequence, outer sequence is:
        //   inner first, last, (all other inner elements in original order), outer first
        //
        // after nth iteration (for n: 1 -> elementCount), outer sequence is:
        // (for n==1):
        //   inner first, last, (all other inner elements in original order), outer first
        // (for odd n > 1):
        //   last - (n - 1), last, (first elementCount - (n+1) inner elements in original order), all other inner elements in original order), inner first, outer first
        // (and for even n > 1):
        //   last - (n - 1), outer first, (first elementCount - (n+1) inner elements in original order), all other inner elements in original order), inner first, last



        // permutations of even-length subsequence: last, first, (all other elements in original order), first+1, first+2
        // start next outer sequence by swapping first and last

        // TODO: VERIFY THIS AND IMPLEMENT IF TRUE!
        //
        // for even-length subsequence:
        //
        // after first iteration:
        //   first+2, last, first, (all other elements in original order), first+1, outer first
        //
        // after second iteration:
        //   first+3, outer first, last, (all other elements in original order), first+1, first, first+2
        //
        // after third iteration:
        //   first+4, first+2, outer first, (all other elements in original order), first+1, first, last, first+3



        int lastPos = sequence.length - 1;
        int firstPos = lastPos - (length-1);

        T[] result = Arrays.copyOf(sequence, sequence.length);

        result[firstPos] = sequence[lastPos];

        if ((length & 1) != 0) {
            result[lastPos] = sequence[firstPos];

            return result;
        }

        result[firstPos+1] = sequence[firstPos];

        if (lastPos == firstPos + 1)
            return result;

        result[lastPos-1] = sequence[firstPos+1];
        result[lastPos] = sequence[firstPos+2];

        if (lastPos == firstPos + 3)
            return result;

        for (int index = firstPos+2; index < lastPos - 1; index++)
            result[index] = sequence[index+1];

        return result;
    }

    /**
     * reconstruct the start state for the nth subpermutation over the given number of elements
     * @param length the number of elements in the subpermutation
     * @param permutationCount the number of the subpermutation whose start state is to be reconstructed
     * @param sequence the starting sequence
     * @param <T> the element type
     * @return the updated sequence
     */
    public <T> T[] reconstructSubpermutationStartState(int length, int permutationCount, T[] sequence) {

        int outerElement = sequence.length - length;
        int innerElement = sequence.length - 1;

        for (int count = 0; count < permutationCount; count++ ) {

            // compute end state of inner subpermutation (e.g. bc => cb)
            sequence = reconstructEndState(length - 1, sequence);

            // advance to the start state prior to the next inner subpermutation (e.g. acb => bca)
            T temp = sequence[outerElement];

            if ((length & 1) == 0)
                innerElement = sequence.length - 1 - count;

            sequence[outerElement] = sequence[innerElement];
            sequence[innerElement] = temp;
        }

        return sequence;
    }

    public <T> T[] reconstructStartState(long permutationIndex, T[] sequence) {

        // limit to valid permutation index:
        // permutationIndex = permutationIndex % MoreMath.factorial(sequence.length) ?
        //
        // probably less expensive to simply apply some sort of truncation to the baseF value in startRegister

        int[] startRegister = toBaseF(permutationIndex);

        for (int index = 0; index < startRegister.length; index++)
            sequence = reconstructSubpermutationStartState(startRegister.length + 1 - index, startRegister[startRegister.length - 1 - index], sequence);

        return sequence;
    }
}
