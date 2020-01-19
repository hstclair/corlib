package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.stclair.corlib.validation.Validation.inRange;

public class HalsHeapsAlgorithmIterator<T> {

    public int[] pair;

    long index;

    public int[] register;

    boolean depleted;

    boolean finalValue;

    public HalsHeapsAlgorithmIterator(T[] values, long index) {

        this.register = Arrays.copyOf(toBaseF(index), values.length);

        this.index = index;

        pair = computeNextPair(register);
    }

    public HalsHeapsAlgorithmIterator(T[] values) {

        this.register = new int[values.length];

        pair = computeNextPair(register);
    }

    /**
     * Construct a "base F" representation of the provided index
     * This is simply an array of values representing a series
     * of digits whose values are denominated in a variable base
     * determined by the factorial of digit order (i.e. 1, 2, 3...)
     *
     * For example, the following mappings show the "Base F"
     * equivalent for various numbers:
     *
     * 1     int[] { 1 }
     * 2     int[] { 0, 1 }
     * 3     int[] { 1, 1 }
     * 4     int[] { 0, 2 }
     * 5     int[] { 1, 2 }
     * 6     int[] { 0, 0, 1 }
     *
     * @param value the value to be converted to "base F"
     * @return
     */
    public int[] toBaseF(long value) {

        long divisor = 2;

        List<Integer> digits = new ArrayList<>();

        while (value > 0) {

            int modulus = (int) (value % divisor);

            digits.add(modulus);

            value = value / divisor;

            divisor++;
        }

        return digits.stream()
                .mapToInt(digit -> digit)
                .toArray();
    }

    public T[] getSeed(T[] start) {

        if (index == 0)
            return start;

        return reconstructStartState(index, start);
    }

    public boolean hasNext(T[] currentValue) {  // per java docs, this method invoked BEFORE currentValue appended to Stream

        return ! depleted;
    }

    public T[] next(T[] currentValue) {  // per java docs, this method invoked AFTER currentValue appended to stream

        // this logic looks odd but it is here to support the Stream.iterate() behavior
        // required by java.  The method expects a seed value (provided by getSeed()),
        // a test method (provided by hasNext()), and a generator method (provided by this function).
        //
        // The three arguments to the Stream.iterate() method map to the three arguments
        // required by a for loop, where the test method executes at the top of the loop
        // and the generator method executes at the bottom of the loop.  For this reason,
        // this function must be permitted to generate an "invalid" result that will then
        // be refuted by the hasNext() method.
        //
        // As an extra precaution, this implementation does not attempt to validated the
        // supplied value (in case it is subsequently mutated by the caller) and instead
        // simply tests to see whether all available results have been emitted and
        // accepted before allowing the hasNext() method to return false.
        if (register[register.length - 1] != 0) {

            depleted = true;

            return currentValue;
        }

        currentValue = heapsAlgorithmSwapAndStore(currentValue, pair[0], pair[1]);

        pair = computeNextPair(register);

        index++;

        return currentValue;
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

    public T[] heapsAlgorithmSwapAndStore(T[] values, int a, int b) {

        values = Arrays.copyOf(values, values.length);

        if ((a & 1) == 0)
            b = values.length - 1;
        else
            b = values.length - b - 1;

        a = values.length - a - 1;

        T temp = values[a];

        values[a] = values[b];

        values[b] = temp;

        return values;
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
     * Generate an array containing the starting state of the requested subpermutation
     * using Heap's Algorithm
     *
     * @param length
     * the number of array elements involved in the subpermutation
     * (this must be equal to the number of elements in the implied
     * inner subpermutation plus one) and must be less than
     * or equal to the length of the provided <b>sequence</b>
     * @param subpermutationIndex
     * the zero-relative index of the desired subpermutation
     * (this must be less than or equal to length - 1)
     * @param sequence
     * the sequence of elements to which the subpermutation is to be applied
     * @param <T> the type of element
     *
     * @return a new array containing the state of the original sequence following
     * <b>subpermutationIndex * factorial(length - 1)</b> iterations of
     * Heap's Algorithm
     */
    public <T> T[] reconstructSubpermutationStartState(int length, int subpermutationIndex, T[] sequence) {

        if (subpermutationIndex < 0)
            subpermutationIndex = length - (subpermutationIndex % length);

        if (subpermutationIndex >= length)
            subpermutationIndex %= length;

        if ((length & 1) == 0)
            return reconstructSubpermutationStartStateEven(length, subpermutationIndex, sequence);

        return reconstructSubpermutationStartStateOdd(length, subpermutationIndex, sequence);
    }


    /**
     * Generate an array containing the starting state of the requested subpermutation
     * using Heap's Algorithm
     *
     * @param length
     * the number of array elements involved in the subpermutation
     * (this must be an <b>odd</b> number equal to the number of elements
     * in the implied inner subpermutation plus one) and must be less than
     * or equal to the length of the provided <b>sequence</b>
     * @param subpermutationIndex
     * the zero-relative index of the desired subpermutation
     * (this must be less than or equal to length - 1)
     * @param sequence
     * the sequence of elements to which the subpermutation is to be applied
     * @param <T> the type of element
     *
     * @return a new array containing the state of the original sequence following
     * <b>subpermutationIndex * factorial(length - 1)</b> iterations of
     * Heap's Algorithm
     */
    public <T> T[] reconstructSubpermutationStartStateOdd(int length, int subpermutationIndex, T[] sequence) {

        inRange(length, 0, Integer.MAX_VALUE, "length");

        inRange(subpermutationIndex, 0, length - 1, "subpermutationIndex");

        if ((length & 1) != 1)
            throw new IllegalArgumentException("length must be odd");

        T[] result = Arrays.copyOf(sequence, sequence.length);

        if (subpermutationIndex == 0)
            return result;

        T[] generator = Arrays.copyOfRange(sequence, sequence.length - (length - 2), sequence.length);

        if (generator.length != length)
            generator = Arrays.copyOf(generator, length);

        if (length == 3) {
            generator[0] = sequence[sequence.length - 3];
            generator[1] = sequence[sequence.length - 2];
            generator[2] = sequence[sequence.length - 1];
        } else {  // adcbe => debca
            generator[0] = sequence[sequence.length - length];
            generator[generator.length - 1] = sequence[sequence.length - 1];
            generator[generator.length - 2] = sequence[sequence.length - (length - 1)];
            generator[generator.length - 3] = sequence[sequence.length - (length - 2)];
        }

        Function<Integer, Integer> generatorIndex = (index) -> {

            if (length == 3) {
                switch (index) {
                    case 0:
                        return subpermutationIndex;

                    case 1:
                        return (subpermutationIndex + 1) % length;

                    case 2:
                        return (subpermutationIndex + 2) % length;
                }
            }

            switch(index) {

                case 0:
                    return subpermutationIndex;

                case 1:
                    return (subpermutationIndex + length - 2) % length;

                case 2:
                    return (subpermutationIndex + length - 3) % length;
            }

            if (index == length - 1)
                return (subpermutationIndex + length - 1) % length;

            return (subpermutationIndex + index - 2) % length;
        };

        for (int index = 0; index < length; index++)
            result[sequence.length - length + index] = generator[generatorIndex.apply(index)];

        return result;
    }

    /**
     * Generate an array containing the starting state of the requested subpermutation
     * using Heap's Algorithm
     *
     * @param length
     * the number of array elements involved in the subpermutation
     * (this must be an <b>even</b> number equal to the number of elements
     * in the implied inner subpermutation plus one) and must be less than
     * or equal to the length of the provided <b>sequence</b>
     * @param subpermutationIndex
     * the zero-relative index of the desired subpermutation
     * (this must be less than or equal to length - 1)
     * @param sequence
     * the sequence of elements to which the subpermutation is to be applied
     * @param <T> the type of element
     *
     * @return a new array containing the state of the original sequence following
     * <b>subpermutationIndex * factorial(length - 1)</b> iterations of
     * Heap's Algorithm
     */
    public <T> T[] reconstructSubpermutationStartStateEven(int length, int subpermutationIndex, T[] sequence) {

        inRange(length, 0, Integer.MAX_VALUE, "length");

        if ((length & 1) != 0)
            throw new IllegalArgumentException("argument \"length\" must be even");

        T[] result = Arrays.copyOf(sequence, sequence.length);

        // if we're seeking the zeroth subpermutation, then there is nothing to change
        // just return the submitted sequence as-is
        if (subpermutationIndex == 0)
            return result;

        // if the subpermutation index is greater than the length then something is badly wrong
        inRange(subpermutationIndex, 0, length - 1, "subpermutationIndex");

        // handle permutation of two elements explicitly
        if (length == 2) {
            result[result.length - 1] = sequence[sequence.length - 2];
            result[result.length - 2] = sequence[sequence.length - 1];

            return result;
        }

        // for the purposes of the reconstruction algorithm, we can decompose an incoming sequence, into the following:
        //
        // outerFirst, innerFirst, innerElement[0], innerElement[1], ... , innerElement[length - 4], last
        //


        // to simplify this algorithm, we will consider the innerElement array to be defined
        // as have a leading element (at position 0) whose value is set to that of the last element
        // of the starting array, the set of all inner elements from the starting sequence
        // (defined by sequence[i]: i=2->length-1), and then a final trailing element,
        // whose value is set to that of the element at position 1 of the starting array.
        // The resulting innerElement array contains length - 2 elements.
        //
        // This definition will somewhat simplify the implementation that follows because it
        // represents the ordered list of elements that will be "restored" to the updated sequence
        // following the selected permutation and, in fact, the positions to which they will be
        // restored as the permutation progresses.
        //
        // this embedded function implements this mapping for us
        Function<Integer, Integer> innerElementIndex = (index) -> {

            inRange(index, 0, sequence.length - 2, "index");

            if (index == 0)
                return sequence.length - 1;

            if (index < length - 2)
                return sequence.length - length + index + 1;

            return sequence.length - length + 1;
        };

        // every intermediate subpermutation follows this general pattern  (given the
        // "natural" definition of innerElement[] as the ordered set of elements
        // following "outerFirst, innerFirst" and preceding "last"):
        //
        // (subpermutationIndex == length - 1) ? last : innerElement[innerElement.length - subpermutationIndex], (subpermutationIndex == length - 1) ? innerElement[0] : (subpermutationIndex & 1) == 1 ? last : outerFirst, innerElement[0], ... , innerElement[length-4-subpermutationIndex], innerFirst, (subpermutationIndex & 1) == 0 ? last : outerFirst
        //
        // our redefinition of the innerElement sequence as:
        //
        // sequence[length-1], sequence[i: 2 -> length - 2], sequence[1]
        //
        // allows us to declare our initial result state to be a copy of the original sequence
        // having the following substitutions:
        //
        // result[0] = innerElement[innerElement.length - subpermutationIndex]
        //
        // for even-valued subpermutationIndex:
        //   result[1] = outerFirst
        //   result[length - 1] = last
        // otherwise:
        //   result[1] = last
        //   result[length - 1] = outerFirst
        //
        // the remaining changes to the result set can be generated by copying the last
        // permutationIndex - 1 entries from our redefined innerElement array into their
        // corresponding positions in the result array, replacing any values already present.
        // following this operation, the result array will contain exactly the values
        // that we desire.


        result[result.length - length] = sequence[innerElementIndex.apply(length - 1 - subpermutationIndex)];

        result[result.length - length + 1] = (subpermutationIndex & 1) == 1 ? sequence[sequence.length - 1] : sequence[sequence.length - length];
        result[result.length - 1] = (subpermutationIndex & 1) == 0 ? sequence[sequence.length - 1] : sequence[sequence.length - length];

        for (int index = length - subpermutationIndex; index < length - 1; index++)
            result[result.length - length + index] = sequence[innerElementIndex.apply(index)];

        return result;
    }

    /**
     * Compute a Heap's Algorithm result non-iteratively
     * (this method can be used to compute the output of Heap's Algorithm after any arbitrary number of iterations)
     *
     * @param permutationIndex the number of iterations for which a result is to be computed
     * @param sequence the starting sequence
     * @param <T> the type of result
     * @return a new array containing the Heap's Algorithm output corresponding to the specified number of iterations.
     */
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
