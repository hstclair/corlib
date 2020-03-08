package com.stclair.corlib.math.matrix;

import com.stclair.corlib.collection.Tuple;
import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.permutation.HeapsAlgorithmPermutationConstructor;
import com.stclair.corlib.permutation.HeapsAlgorithmPermutationIteratorFactory;
import com.stclair.corlib.permutation.PermutationGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A class implementing presort logic to be applied prior to solving a matrix.
 * Orders rows to ensure that matrix can be solved (or returns null if it is not)
 */
public class Array2DPresortEvaluator {

    /** implementation of Permutations interface that will be used to synthesize the permutation matrices to be tested */
    PermutationGenerator permutationGenerator = new PermutationGenerator(new HeapsAlgorithmPermutationConstructor(), new HeapsAlgorithmPermutationIteratorFactory());

    /**
     * sort the rows of the provided Array2D instance (representing elements of a Matrix)
     * in order to ensure that it can be solved and to minimize loss of precision.
     *
     * @param original the Array2D instance whose rows are to be sorted
     * @return a new, sorted Array2D or, if the matrix cannot be solved, null
     */
    public <T> Array2D<T> presort(Array2D<T> original) {

        Stream<Integer[]> permutations = buildPermutations(original.getHeight());

        Tuple<Integer, Integer[]> result = selectPermutation(original, permutations);

        int selectedPermutationIndex = result.getA();

        if (selectedPermutationIndex == -1)
            return null;

        if (selectedPermutationIndex == 0)
            return original;

        Integer[] selectedPermutationArray = result.getB();

        return applyPermutation(original, selectedPermutationArray, selectedPermutationIndex);
    }

    /**
     * build a list of integer arrays representing each of the possible permutation matrices
     * @param matrixOrder the Order of the matrix to be solved
     * @return list of permutation arrays
     */
    public Stream<Integer[]> buildPermutations(int matrixOrder) {

        Integer[] sequence = sequenceOf(matrixOrder);

        return this.permutationGenerator.streamPermutationsOf(sequence);
    }

    /**
     * apply the selected permutation to the source array
     * @param original the source array that will be re-ordered
     * @param permutation the selected permutation array
     * @param selectedPermutation the index of the permutation array selected
     * @return the source array with rows re-ordered
     */
    public <T> Array2D<T> applyPermutation(Array2D<T> original, Integer[] permutation, int selectedPermutation) {

        Function<Indexor<T>, T> baseAccessor = (indexor) -> original.get(indexor.getColumn(), permutation[indexor.getRow()]);

        Function<Indexor<T>, T> accessor = baseAccessor;

        OperationStrategy<T> operationStrategy = original.getOperationStrategy();


        // if the selected permutation has odd parity (requires an odd number of row swaps)
        // then we will negate the last row to ensure that the determinant is still valid.
        if ((selectedPermutation & 1) != 0)
            accessor = (indexor) -> indexor.getRow() == permutation.length - 1 ?
                    operationStrategy.negate(baseAccessor.apply(indexor)) : baseAccessor.apply(indexor);

        return new Array2DConcrete<T>(operationStrategy, original, accessor);
    }

    /**
     * choose the permutation that requires the least estimated precision
     * @param original the 2D array representing the elements of the original matrix
     * @param permutations the list of all possible permutations
     * @return a Tuple containing the index of the first valid permutation and the values of that permutation
     */
    public <T> Tuple<Integer, Integer[]> selectPermutation(Array2D<T> original, Stream<Integer[]> permutations) {

        Integer[] selectedPermutationArray = null;
        int selectedPermutationIndex = -1;
        int index = 0;
        int cells = original.getWidth();

        for (Iterator<Integer[]> it = permutations.iterator(); it.hasNext(); ) {

            Integer[] permutation = it.next();

            boolean valid = true;

            for (int rank = 0; rank < cells; rank++) {
                if (original.getOperationStrategy().isZero(original.get(rank, permutation[rank]))) {
                    valid = false;
                    break;
                }
            }

            if (valid)
                return new Tuple<>(index, permutation);

            index++;
        }

        return new Tuple<>(selectedPermutationIndex, selectedPermutationArray);
    }

    /**
     * generate an Integer array containing a sequence of values ranging from zero to the given target count - 1
     * @param count the number of entries requested for the sequence
     * @return an ordered (ascending) array containing Integer values ranging from zero to count - 1
     */
    public Integer[] sequenceOf(int count) {

        Integer[] sequence = new Integer[count];

        for (int index = 0; index < sequence.length; index++)
            sequence[index] = index;

        return sequence;
    }
}
