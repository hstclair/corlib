package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.stclair.corlib.validation.Validation.inRange;

public class HalsHeapsAlgorithmPermutationGenerator implements PermutationGenerator {

    @Override
    public <T> Stream<T[]> streamPermutationsOf(T[] values, long startIndex, long count) {

        HalsHeapsAlgorithmIterator<T> iterator = new HalsHeapsAlgorithmIterator<>(values, startIndex);

        return Stream.iterate(iterator.getSeed(values), iterator::hasNext, iterator::next)
                .limit(count);
    }

    public <T> Stream<T[]> streamPermutationsOf(T[] values) {

        HalsHeapsAlgorithmIterator<T> iterator = new HalsHeapsAlgorithmIterator<>(values);

        return Stream.iterate(values, iterator::hasNext, iterator::next);
    }
}
