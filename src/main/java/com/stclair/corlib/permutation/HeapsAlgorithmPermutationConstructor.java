package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;
import java.util.Arrays;
import java.util.function.Function;

import static com.stclair.corlib.validation.Validation.inRange;

/**
 * Implementation of PermutationConstructor to generate a specific permutation
 * result using Heap's Algorithm
 */
public class HeapsAlgorithmPermutationConstructor implements PermutationConstructor {

    @Override
    public <T> T[] constructPermutation(T[] initialSequence, long iteration) {
        return construct(new BaseFCounter().set(iteration), initialSequence);
    }

    @Override
    public <T> T[] constructPermutation(T[] initialSequence, BaseFCounter baseF) {
        return construct(baseF, initialSequence);
    }


    /**
     * returns a function that maps an index to the corresponding value in the
     * "generator" permutation for the selected number of trailing elements of
     * the supplied sequence
     *
     * @param length the length of the segment for which the generator function will map
     * @param sequence the original sequence of elements for which the generator function
     *                 will be created
     * @param <T> the type of object in the array
     * @return a function that accepts an index and returns the corresponding element
     *         of the "generator" permutation
     */
    public <T> Function<Integer, T> getGeneratorFunction(int length, T[] sequence) {

        // for an odd-length sequence, each top-level permutation consists of a simple
        // rotation of each of the constituent values through a specific, easily-constructed
        // "generator" which is a permutation of the original values (though the behavior
        // is slightly different when the initial sequence has length three).
        //
        // In general, given a sequence:
        //
        //    first, second, third, others..., last
        //
        // the generator follows the pattern:
        //
        //    first, others..., second, third, last
        //
        // However, for a sequence containing only three elements, the generator is:
        //
        //    first, second, last
        //
        // the generator function below has been carefully constructed to handle
        // both cases correctly.

        inRange(length, 0, sequence.length, "length");

        int generatorOffset = sequence.length - length;

        return (index) -> {

            inRange(index, 0, length - 1, "index");

            if (index == 0)
                return sequence[generatorOffset];

            if (index == length - 1)
                return sequence[sequence.length - 1];

            if (index == length - 2)
                return sequence[generatorOffset + 1];

            if (index < length - 3)
                return sequence[generatorOffset + index + 2];

            if (index == length - 3)
                return sequence[generatorOffset + 2];

            throw new IllegalArgumentException();
        };
    }

    /**
     * get a function that will map the index in a result sequence to the
     * corresponding element in the generator sequence for the selected
     * permutation index.
     *
     * @param subpermutationIndex the index of the subpermutation to be applied
     * @param length the number of elements affected by the subpermutation
     * @return the mapped index
     */
    public Function<Integer, Integer> getGeneratorIndexFunction(int subpermutationIndex, int length) {

        // each element in the result maps to a rotation of the elements in
        // the corresponding "generator" sequence.
        //
        // To illustrate, for the sequence:
        //
        //   abcde
        //
        // the corresponding generator is:
        //
        //   adbce
        //
        // for subpermutation (0), the index must map to the positions of
        // the original values in the generator so this function must
        // implement the following mapping:
        //
        //   0=>0, 1=>2, 2=>2, 3=>1, 4=>4
        //
        // each subsequent subpermutation effectively rotates through the
        // members of the generator so that the series of subpermutation
        // results for our original sequence is:
        //
        //    abcde
        //    dceba
        //    beacd
        //    cadeb
        //    edbac
        //
        // Observe that the sequence in the first column is the sequence
        // in our generator: adbce
        // Likewise, the sequence in the second column is the sequence
        // in our generator if we were to start at 'b' and to wrap at the
        // end of the generator: bcead
        // This same pattern can be observed in each of the other columns
        // and holds true for all odd-length subpermutations
        //
        // the function that follows simply implements this idea by first
        // computing the reverse mapping used to construct the "generator"
        // (in order to find the original element's position within the
        // generator sequence) and then adds the offset for the selected
        // subpermutation

        return (index) -> {

            if (index == length - 1)
                return (subpermutationIndex + length - 1) % length;

            switch(index) {

                case 0:
                    return subpermutationIndex;

                case 1:
                    return (subpermutationIndex + length - 2) % length;

                case 2:
                    return (subpermutationIndex + length - 3) % length;
            }

            return (subpermutationIndex + index - 2) % length;
        };
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
    public <T> T[] constructPermutationOdd(int length, int subpermutationIndex, T[] sequence) {

        inRange(length, 0, Integer.MAX_VALUE, "length");

        inRange(subpermutationIndex, 0, length - 1, "subpermutationIndex");

        if ((length & 1) != 1)
            throw new IllegalArgumentException("length must be odd");

        T[] result = Arrays.copyOf(sequence, sequence.length);

        if (subpermutationIndex == 0)
            return result;

        Function<Integer, T> generatorFunction = getGeneratorFunction(length, sequence);


        Function<Integer, Integer> generatorIndex = getGeneratorIndexFunction(subpermutationIndex, length);

        for (int index = 0; index < length; index++)
            result[sequence.length - length + index] = generatorFunction.apply(generatorIndex.apply(index));

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
    public <T> T[] constructPermutationEven(int length, int subpermutationIndex, T[] sequence) {

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
    public <T> T[] constructPermutation(int length, int subpermutationIndex, T[] sequence) {

        if (subpermutationIndex < 0)
            subpermutationIndex = length - (subpermutationIndex % length);

        if (subpermutationIndex >= length)
            subpermutationIndex %= length;

        if ((length & 1) == 0)
            return constructPermutationEven(length, subpermutationIndex, sequence);

        return constructPermutationOdd(length, subpermutationIndex, sequence);
    }

    /**
     * Compute a Heap's Algorithm result non-iteratively
     * (this method can be used to compute the output of Heap's Algorithm after any arbitrary number of iterations)
     *
     * @param counter the "base F" encoding of the number of iterations for which a result is to be computed
     * @param sequence the starting sequence
     * @param <T> the type of result
     * @return a new array containing the Heap's Algorithm output corresponding to the specified number of iterations.
     */
    public <T> T[] construct(BaseFCounter counter, T[] sequence) {

        if (counter.isZero())
            return sequence;

        if (counter.mostSignificantDigit() + 2 > sequence.length)
            throw new IllegalArgumentException("permutationIndex is outside the range of possible values for the provided sequence");

        for (int index = counter.mostSignificantDigit(); index >= 0; index--)
            sequence = constructPermutation(index + 2, counter.getDigit(index), sequence);

        return sequence;
    }
}
