package com.stclair.corlib.permutation;

import com.stclair.corlib.math.BaseFCounter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PermutationGenerator {

    PermutationIteratorFactory iteratorFactory;

    PermutationConstructor constructor;

    public PermutationGenerator(PermutationConstructor constructor, PermutationIteratorFactory iteratorFactory) {

        this.iteratorFactory = iteratorFactory;
        this.constructor = constructor;
    }

    public <T> List<T[]> listPermutationsOf(T[] values) {
        return streamPermutationsOf(values)
                .collect(Collectors.toList());
    }

    public <T> List<T[]> listPermutationsOf(T[] values, long startIndex, long count) {
        return streamPermutationsOf(values, startIndex, count)
                .collect(Collectors.toList());
    }

    public <T> Stream<T[]> streamPermutationsOf(T[] values) {

        PermutationIterator iterator = iteratorFactory.getIterator(new BaseFCounter());

        return Stream.iterate(values, iterator::hasNext, iterator::next);
    }

    public <T> Stream<T[]> streamPermutationsOf(T[] values, long startIndex, long count) {

        return streamPermutationsOf(values, new BaseFCounter().set(startIndex), count);
    }

    public <T> Stream<T[]> streamPermutationsOf(T[] values, BaseFCounter counter, long count) {

        PermutationIterator iterator = iteratorFactory.getIterator(counter);

        return Stream.iterate(values, iterator::hasNext, iterator::next)
                .limit(count);
    }
}
