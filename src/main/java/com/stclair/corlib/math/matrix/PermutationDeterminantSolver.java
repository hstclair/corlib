package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.matrix.determinant.DeterminantSolver;
import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.permutation.HalsHeapsAlgorithmPermutation;
import com.stclair.corlib.permutation.Permutations;

import java.util.stream.IntStream;

public class PermutationDeterminantSolver implements DeterminantSolver {

    private static final Permutations permutations = new HalsHeapsAlgorithmPermutation();

    @Override
    public <T> T determinant(Matrix<T> matrix) {

        OperationStrategy<T> operationStrategy = matrix.getOperationStrategy();

        T accumulator = operationStrategy.zero();

        Integer[] sequence = IntStream.range(0, matrix.getElements().getHeight()).boxed().toArray(Integer[]::new);

        int permutationNum = 0;

        for (Integer[] permutation : permutations.of(sequence)) {

            int row = 0;

            T product = operationStrategy.one();

            for (int column: permutation) {
                product = operationStrategy.product(product, matrix.getElements().get(column, row++));
            }

            if ((permutationNum & 1) == 0)
                accumulator = operationStrategy.sum(accumulator, product);
            else
                accumulator = operationStrategy.difference(accumulator, product);

            permutationNum++;
        }

        return accumulator;
    }
}
