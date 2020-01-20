package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

public interface PermutationConstructor {

    <T> T[] constructPermutation(T[] initialSequence, long iteration);

    <T> T[] constructPermutation(T[] initialSequence, BaseFCounter baseF);

}
