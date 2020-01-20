package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

import java.util.Arrays;

import static com.stclair.corlib.validation.Validation.inRange;
import static com.stclair.corlib.validation.Validation.neverNull;

public class ReversingPermutationConstructor implements PermutationConstructor {

    @Override
    public <T> T[] constructPermutation(T[] initialSequence, long iteration) {
        neverNull(initialSequence, "initialSequence");

        return constructPermutation(initialSequence, new BaseFCounter().set(iteration));
    }

    @Override
    public <T> T[] constructPermutation(T[] initialSequence, BaseFCounter baseF) {

        T[] sequence = neverNull(initialSequence, "initialSequence");

        if (baseF.isZero())
            return initialSequence;

        inRange(baseF.mostSignificantDigit(), 0, initialSequence.length - 1, "most significant \"base F\" digit of counter");

        for (int index = baseF.mostSignificantDigit(); index >= 0; index--)
            sequence = rotateLeft(sequence, index + 2, baseF.getDigit(index));

        return sequence;
    }

    public <T> T[] rotateLeft(T[] initialSequence, int length, int count) {

        if (count == 0)
            return initialSequence;

        inRange(length, 0, initialSequence.length, "length");
        inRange(count, 0, length - 1, "count");

        T[] result = Arrays.copyOf(initialSequence, initialSequence.length);

        System.arraycopy(initialSequence, initialSequence.length - length + count, result, result.length - length, length - count);
        System.arraycopy(initialSequence, initialSequence.length - length, result, result.length - count, count);

        return result;
    }
}
