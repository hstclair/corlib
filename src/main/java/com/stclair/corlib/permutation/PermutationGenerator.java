package com.stclair.corlib.permutation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface PermutationGenerator {

    default <T> List<T[]> listPermutationsOf(T[] values) {
        return streamPermutationsOf(values)
                .collect(Collectors.toList());
    }

    default  <T> List<T[]> listPermutationsOf(T[] values, long startIndex, long count) {
        return streamPermutationsOf(values, startIndex, count)
                .collect(Collectors.toList());
    }

    <T> Stream<T[]> streamPermutationsOf(T[] values);

    <T> Stream<T[]> streamPermutationsOf(T[] values, long startIndex, long count);
}
