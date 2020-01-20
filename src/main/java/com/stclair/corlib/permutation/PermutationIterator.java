package com.stclair.corlib.permutation;

public interface PermutationIterator {

    /**
     * get the seed value for the offset specified by this iterator
     *
     * @param start the sequence of elements at permutation 0
     * @param <T> the type of elements in the sequence
     * @return the sequence of elements at the permutation offset specified
     * when the constructor was invoked
     */
    <T> T[] getSeed(T[] start);

    /**
     * test to see whether the last value returned by <b>next()</b>
     * is a valid member of the result set
     * @param currentValue a value to be validated as a member of the result set
     * @param <T> the type of element in the sequence
     * @return true if the supplied argument is a valid result
     */
    <T> boolean hasNext(T[] currentValue);

    /**
     * get the next member in the result sequence
     * @param currentValue the last result returned
     *                     (and the value from which the next
     *                     result may be generated)
     * @param <T> the type of element in the sequence
     * @return a new array containing the next permutation
     * in the sequence following the supplied <b>currentValue</b>
     */
    <T> T[] next(T[] currentValue);
}
