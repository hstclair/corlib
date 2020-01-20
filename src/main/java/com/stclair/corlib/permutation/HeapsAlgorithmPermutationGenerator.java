package com.stclair.corlib.permutation;

import java.util.stream.Stream;

import static com.stclair.corlib.validation.Validation.inRange;

public class HeapsAlgorithmPermutationGenerator implements PermutationGenerator {

    @Override
    public <T> Stream<T[]> streamPermutationsOf(T[] values, long startIndex, long count) {

        HeapsAlgorithmPermutationConstructor constructor = new HeapsAlgorithmPermutationConstructor();

        HeapsAlgorithmPermutationIterator iterator = new HeapsAlgorithmPermutationIterator(startIndex);

        return Stream.iterate(constructor.constructPermutation(values, startIndex), iterator::hasNext, iterator::next)
                .limit(count);
    }

    public <T> Stream<T[]> streamPermutationsOf(T[] values) {

        HeapsAlgorithmPermutationIterator iterator = new HeapsAlgorithmPermutationIterator();

        return Stream.iterate(values, iterator::hasNext, iterator::next);
    }
}
