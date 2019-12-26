package com.stclair.corlib.math.matrix;

import com.stclair.corlib.collection.Tuple;
import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.LongOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.math.util.Permutation;

import java.util.*;
import java.util.function.Function;

/**
 * A class implementing
 */
public class Array2DPresortEvaluator<T> {

    OperationStrategy<T> operationStrategy;

    public Array2DPresortEvaluator(OperationStrategy<T> operationStrategy) {
        this.operationStrategy = operationStrategy;
    }

    public Array2D<T> presort(Array2D<T> original) {

        Array2D<Long> significantBits = new Array2DConcrete<>(new LongOperationStrategy(), original, (indexor) -> operationStrategy.significantBits(indexor.getValue()) );

        Integer[] sequence = sequenceOf(significantBits.getWidth());

        List<Integer[]> permutations = Permutation.of(sequence);

        int selectedPermutation = selectPermutation(significantBits, permutations);

        if (selectedPermutation == 0)
            return original;

        Integer[] permutation = permutations.get(selectedPermutation);

        Function<Indexor<T>, T> baseAccessor = (indexor) -> original.get(permutation[indexor.getRow()], indexor.getColumn());

        Function<Indexor<T>, T> accessor = baseAccessor;

        if ((selectedPermutation & 1) != 0)
            accessor = (indexor) -> indexor.getRow() == indexor.getColumn() && indexor.getRow() == permutation.length - 1 ?
                    operationStrategy.negate(baseAccessor.apply(indexor)) : baseAccessor.apply(indexor);

        return new Array2DConcrete<T>(operationStrategy, original, accessor);
    }


    public int selectPermutation(Array2D<Long> significantBits, List<Integer[]> permutations) {

        long magnitude = Long.MAX_VALUE;

        int selectedPermutation = -1;

        for (int index = 0; index < permutations.size(); index++) {

            Integer[] permutation = permutations.get(index);

            long currentMagnitude = magnitudeOf(significantBits, permutation);

            if (magnitude > currentMagnitude) {
                magnitude = currentMagnitude;
                selectedPermutation = index;
            }
        }

        return selectedPermutation;
    }

    public Integer[] sequenceOf(int count) {

        Integer[] sequence = new Integer[count];

        for (int index = 0; index < sequence.length; index++)
            sequence[index] = index;

        return sequence;
    }

    public long magnitudeOf(Array2D<Long> significantBits, Integer[] permutation) {

        long magnitude = 0;

        for (int index = 0; index < permutation.length; index++) {

            int column = permutation[index];

            int row = index;

            long value = significantBits.get(column, row);

            if (value == 0)
                return Long.MAX_VALUE;

            magnitude += value;
        }

        return magnitude;
    }
}
