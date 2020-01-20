package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

import java.util.Arrays;

import static com.stclair.corlib.validation.Validation.inRange;

/**
 * implementation of class to support the Stream.iterate() method
 * to generate a series of permutations via Heap's Algorithm
 */
public class HeapsAlgorithmPermutationIterator {

    BaseFCounter counter = new BaseFCounter();

    HeapsAlgorithmPermutationConstructor permutationConstructor = new HeapsAlgorithmPermutationConstructor();

    /**
     * constructor
     * @param index the offset index at which to start the permutations
     *              returned by this iterator
     */
    public HeapsAlgorithmPermutationIterator(long index) {

        counter.set(index);
    }

    /**
     * constructor
     */
    public HeapsAlgorithmPermutationIterator() {
    }

    // The three arguments to the Stream.iterate() method map to the three arguments
    // required by a for loop, where the test method executes at the top of the loop
    // and the generator method executes at the bottom of the loop.  For this reason,
    // this function must be permitted to generate an "invalid" result that will then
    // be refuted by the hasNext() method.

    /**
     * get the seed value for the offset specified by this iterator
     *
     * @param start the sequence of elements at permutation 0
     * @param <T> the type of elements in the sequence
     * @return the sequence of elements at the permutation offset specified
     * when the constructor was invoked
     */
    public <T> T[] getSeed(T[] start) {

        if (counter.isZero())
            return start;

        return permutationConstructor.constructPermutation(start, counter);
    }

    /**
     * test to see whether the last value returned by <b>next()</b>
     * is a valid member of the result set
     * @param currentValue a value to be validated as a member of the result set
     * @param <T> the type of element in the sequence
     * @return true if the supplied argument is a valid result
     *
     * @implNote
     * the Stream.iterate() method arguments were designed to
     * emulate the arguments to a <b>for</b> loop so that the
     * <b>hasNext(current)</b> function mirrors the test (the second
     * argument to the <b>for</b> statement) and the <b>next(current)</b>
     * function mirrors the update operation (the third argument
     * to the <b>for</b> statement and typically used to increment
     * or decrement a variable).
     *
     * The test (or <b>hasNext(current)</b>) operation occurs at
     * the top of the loop and validates every possible value,
     * including the seed value specified by the first argument
     * to the <b>for</b> statement.
     *
     * The <b>hasNext(current)</b> method has been named to match
     * the corresponding argument to the <b>Stream.iterate()</b>
     * method but this name is poorly-chosen since it suggests
     * that the test is to determine whether the iterator will
     * produce another valid result if the caller subsequently
     * invokes the <b>next(current)</b> method.  Instead, of the
     * behavior implied by its name, the <b>Stream.iterate()</b>
     * method expects the <b>hasNext(current)</b> argument to be
     * a function that will validate that the <i>current value</i>
     * supplied to it is a valid member of the result set.
     *
     * Ugh.  So much debugging because of this...
     *
     * The method, as implemented here, simply tests to see whether
     * the <i>last</i> value returned by <b>next(current)</b>
     * would have been valid and makes no attempt to inspect
     * the contents of the supplied <b>currentValue</b> argument.
     */
    public <T> boolean hasNext(T[] currentValue) {  // per java docs, this method invoked BEFORE currentValue appended to Stream

        return counter.mostSignificantDigit() < currentValue.length - 1;
    }

    /**
     * get the next member in the result sequence
     * @param currentValue the last result returned
     *                     (and the value from which the next
     *                     result may be generated)
     * @param <T> the type of element in the sequence
     * @return a new array containing the next permutation
     * in the sequence following the supplied <b>currentValue</b>
     *
     * @implNote
     * the Stream.iterate() method arguments were designed to
     * emulate the arguments to a <b>for</b> loop so that the
     * <b>hasNext(current)</b> function mirrors the test (the second
     * argument to the <b>for</b> statement) and the <b>next(current)</b>
     * function mirrors the update operation (the third argument
     * to the <b>for</b> statement and typically used to increment
     * or decrement a variable).
     *
     * The test (or <b>hasNext(current)</b>) operation occurs at
     * the top of the loop and validates every possible value,
     * including the seed value specified by the first argument
     * to the <b>for</b> statement.
     *
     * The <b>hasNext(current)</b> method has been named to match
     * the corresponding argument to the <b>Stream.iterate()</b>
     * method but this name is poorly-chosen since it suggests
     * that the test is to determine whether the iterator will
     * produce another valid result if the caller subsequently
     * invokes the <b>next(current)</b> method.  Instead, of the
     * behavior implied by its name, the <b>Stream.iterate()</b>
     * method expects the <b>hasNext(current)</b> argument to be
     * a function that will validate that the <i>current value</i>
     * supplied to it is a valid member of the result set.
     *
     * Ugh.  So much debugging because of this...
     *
     * This method increments the "base F" counter representing
     * the state of the permutation and then uses its value to
     * determine the next pair of elements to swap according to
     * Heap's Algorithm.
     */
    public <T> T[] next(T[] currentValue) {  // per java docs, this method invoked AFTER currentValue appended to stream

        counter.increment();

        int[] pair = computeNextPair(counter);

        if (counter.isZero() || counter.mostSignificantDigit() == currentValue.length - 1)
            return currentValue;

        currentValue = cloneAndSwap(currentValue, pair[0], pair[1]);

        return currentValue;
    }

    /**
     * determine the next pair of elements to swap according to
     * Heap's Algorithm
     *
     * @param counter the "base F" counter representing the
     *                current state of the permutation
     * @return a pair of integers containing the indexes of
     * the two elements to swap
     */
    public int[] computeNextPair(BaseFCounter counter) {

        if (counter.isZero())
            return new int[] { 0, 0 };  // don't change anything

        int leastSignificantDigit = counter.leastSignificantDigit();

        // the element to be "swapped in" to the permutation
        // is the one that lies outside of the permutation that
        // we've just completed
        //
        // For example, if we've just completed a permutation of
        // three elements, the state of the counter will be:
        //
        // (subpermutationIndex), 0, 0
        //
        // The least significant digit has rank 2 so that the
        // element that we want to "swap in" is rank + 1
        // elements from the end of the array.  Therefore,
        // we set elementPos to leastSignificantDigit + 1
        int elementPos = leastSignificantDigit + 1;


        // For even "parity" swap operations, Heap's
        // Algorithm specifies that we must exchange
        // the selected element with the last element
        // (alternateElementPos = 0).  We will start by
        // trying this case because it requires no
        // additional computation...
        if ((elementPos & 1) == 0)
            return new int[] { elementPos, 0 };

        // However, for odd "parity", Heap's Algorithm
        // requires that we swap the last element with
        // element specified by one less than the value
        // of the least significant digit (keep in mind
        // that the value of the least significant digit
        // can never be zero).  Looking again at the
        // state of our "base F" counter, this value is
        // represented as "subpermutationIndex":
        //
        // (subpermutationIndex), 0, 0
        //
        return new int[] { elementPos, counter.getDigit(leastSignificantDigit) - 1 };
    }

    /**
     * construct a copy of the provided sequence of values
     * with the two specified elements exchanged
     *
     * @param values the original sequence of elements
     * @param a the index of the first element to swap
     * @param b the index of the second element to swap
     * @param <T> the type of elements in the sequence
     * @return a new array containing the original sequence
     * but having the two selected elements exchanged
     */
    public <T> T[] cloneAndSwap(T[] values, int a, int b) {

        values = Arrays.copyOf(values, values.length);

        b = values.length - b - 1;

        a = values.length - a - 1;

        T temp = values[a];

        values[a] = values[b];

        values[b] = temp;

        return values;
    }
}
