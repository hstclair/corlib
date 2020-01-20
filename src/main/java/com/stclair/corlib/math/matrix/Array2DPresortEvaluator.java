package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.LongOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.permutation.HeapsAlgorithmPermutationConstructor;
import com.stclair.corlib.permutation.HeapsAlgorithmPermutationIteratorFactory;
import com.stclair.corlib.permutation.PermutationGenerator;

import java.util.*;
import java.util.function.Function;

/**
 * A class implementing presort logic to be applied prior to solving a matrix.
 * Orders rows to ensure that matrix can be solved (or returns null if it is not)
 * and to attempt to minimize loss of precision during its solution
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

        Array2D<Long> significantBits = buildSignificantBitsArray(original);

        List<Integer[]> permutations = buildPermutations(original.getHeight());

        int selectedPermutation = selectPermutation(significantBits, permutations);

        if (selectedPermutation == -1)
            return null;

        if (selectedPermutation == 0)
            return original;

        return applyPermutation(original, permutations, selectedPermutation);
    }

    /**
     * build a list of integer arrays representing each of the possible permutation matrices
     * @param matrixOrder the Order of the matrix to be solved
     * @return list of permutation arrays
     */
    public List<Integer[]> buildPermutations(int matrixOrder) {

        Integer[] sequence = sequenceOf(matrixOrder);

        return this.permutationGenerator.listPermutationsOf(sequence);
    }

    /**
     * build a 2D array representing the number of significant bits in each cell of the source array
     * @param sourceArray the array of values to be sorted
     * @return
     */
    public <T> Array2D<Long> buildSignificantBitsArray(Array2D<T> sourceArray) {
        return new Array2DConcrete<>(new LongOperationStrategy(), sourceArray, (indexor) -> sourceArray.getOperationStrategy().significantBits(indexor.getValue()) );
    }

    /**
     * apply the selected permutation to the source array
     * @param original the source array that will be re-ordered
     * @param permutations the list of permutation arrays
     * @param selectedPermutation the index of the permutation array selected
     * @return the source array with rows re-ordered
     */
    public <T> Array2D<T> applyPermutation(Array2D<T> original, List<Integer[]> permutations, int selectedPermutation) {

        Integer[] permutation = permutations.get(selectedPermutation);

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
     * @param significantBits the 2D array representing the significant bits of the 2D array to be sorted
     * @param permutations the list of all possible permutations
     * @return the index of the "optimal" permutation
     */
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

    /**
     * estimate the precision required to solve a particular permutation (by summing the significant bits
     * in each cell)
     * @param significantBits the 2D array representing the significant bits in each cell
     * @param permutation the permutation whose required precision is to be estimated
     * @return an estimate of the precision required (smaller values suggest lower precision required)
     */
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
