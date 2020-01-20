package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

public class ReversingPermutationIteratorFactory implements PermutationIteratorFactory {

    @Override
    public PermutationIterator getIterator(BaseFCounter counter) {
        return new ReversingPermutationIterator(counter);
    }
}
