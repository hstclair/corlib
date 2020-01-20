package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

public interface PermutationIteratorFactory {

    default PermutationIterator getIterator(long startIndex) {
        return getIterator(new BaseFCounter().set(startIndex));
    }

    PermutationIterator getIterator(BaseFCounter counter);
}
