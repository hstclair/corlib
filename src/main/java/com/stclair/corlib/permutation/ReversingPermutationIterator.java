package com.stclair.corlib.permutation;

import com.stclair.corlib.collection.Tuple;
import com.stclair.corlib.math.BaseFCounter;

import java.sql.Ref;
import java.util.*;
import java.util.function.Function;

public class ReversingPermutationIterator implements PermutationIterator {

    BaseFCounter counter = new BaseFCounter();

    PermutationConstructor permutationConstructor = new ReversingPermutationConstructor();

    public ReversingPermutationIterator() {
    }

    public ReversingPermutationIterator(long count) {
        counter.set(count);
    }

    public ReversingPermutationIterator(BaseFCounter counter) {
        this.counter = counter;
    }

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
    public <T> boolean hasNext(T[] currentValue) {

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

        if (! hasNext(currentValue))
            return currentValue;

        counter.increment();

        if (! hasNext(currentValue))
            return currentValue;

        return reverse(currentValue, counter.leastSignificantDigit() + 2);
    }

    /**
     * create a copy of the provided sequence of values with
     * the last (length) elements reversed
     *
     * @param values the original sequence
     * @param length the number of trailing elements to reverse
     * @param <T> the type of element
     * @return a copy of the original values with the last (length) elements reversed
     */
    public <T> T[] reverse(T[] values, int length) {

        values = Arrays.copyOf(values, values.length);

        int count = length >> 1;

        int a = values.length - 1;
        int b = values.length - length;

        while (count-- != 0) {
            T temp = values[a];
            values[a] = values[b];
            values[b] = temp;

            a--;
            b++;
        }

        return values;
    }
}
