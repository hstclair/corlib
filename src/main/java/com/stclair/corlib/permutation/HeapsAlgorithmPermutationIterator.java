package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static com.stclair.corlib.validation.Validation.inRange;

public class HeapsAlgorithmPermutationIterator {

    public int[] pair;

    long index;

    BaseFCounter counter;

    HeapsAlgorithmPermutationConstructor permutationConstructor = new HeapsAlgorithmPermutationConstructor();

    public HeapsAlgorithmPermutationIterator(long index) {

        counter = new BaseFCounter()
            .set(index);

        this.index = index;

        pair = computeNextPair(counter);
    }

    public HeapsAlgorithmPermutationIterator() {

        counter = new BaseFCounter();

        pair = computeNextPair(counter);
    }

    // The three arguments to the Stream.iterate() method map to the three arguments
    // required by a for loop, where the test method executes at the top of the loop
    // and the generator method executes at the bottom of the loop.  For this reason,
    // this function must be permitted to generate an "invalid" result that will then
    // be refuted by the hasNext() method.

    public <T> T[] getSeed(T[] start) {

        if (index == 0)
            return start;

        return permutationConstructor.constructPermutation(start, index);
    }

    public <T> boolean hasNext(T[] currentValue) {  // per java docs, this method invoked BEFORE currentValue appended to Stream

        return counter.mostSignificantDigit() < currentValue.length - 1;
    }

    public <T> T[] next(T[] currentValue) {  // per java docs, this method invoked AFTER currentValue appended to stream

        counter.increment();

        pair = computeNextPair(counter);

        if (counter.isZero() || counter.mostSignificantDigit() == currentValue.length - 1)
            return currentValue;

        currentValue = heapsAlgorithmSwapAndStore(currentValue, pair[0], pair[1]);

        pair = computeNextPair(counter);

        index++;

        return currentValue;
    }

    public int[] computeNextPair(BaseFCounter counter) {

        if (counter.isZero())
            return new int[] { 0, 0 };  // don't change anything

        int leastSignificantDigit = counter.leastSignificantDigit();

        int elementPos = leastSignificantDigit + 1;

        int alternateElementPos = counter.getDigit(leastSignificantDigit) - 1;

        return new int[] { elementPos, alternateElementPos };
    }

    public <T> T[] heapsAlgorithmSwapAndStore(T[] values, int a, int b) {

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


}
