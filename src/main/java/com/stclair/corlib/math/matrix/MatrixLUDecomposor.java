package com.stclair.corlib.math.matrix;

import com.stclair.corlib.collection.Tuple;
import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.Timer;
import java.util.function.Function;


/**
 * class implementing logic to decompose a matrix into the corresponding Upper and Lower matrices
 * (computes the determinant as a by product)
 */
public class MatrixLUDecomposor {

    /** object for sorting the array prior to decomposition */
    Array2DPresortEvaluator presorter = new Array2DPresortEvaluator();

    /**
     * get the function that will generate updated elements for the Lower Matrix based on a given solution column
     * @param operationStrategy the set of arithmetic operations for the target value type
     * @param upper the current elements of the partially-constructed Upper matrix
     * @param solveColumn the target column for which element values are to be generated
     * @param upperColumnValue the current diagonal element value corresponding to the solution column in the Upper Matrix
     * @param <T> the target value type
     * @return the function that will generate the next set of values for the Lower matrix
     */
    <T> Function<Indexor<T>, T> getLowerMatrixBuilder(OperationStrategy<T> operationStrategy, Array2D<T> upper, int solveColumn, T upperColumnValue) {

        return indexor -> {

            if (indexor.getColumn() != solveColumn || indexor.getRow() <= solveColumn)
                return indexor.getValue();

            return operationStrategy.quotient(upper.get(indexor.getColumn(), indexor.getRow()), upperColumnValue);
        };
    }

    /**
     * construct the next version of the Lower matrix
     * @param lower the current elements of the partially-constructed Lower matrix
     * @param upper the current elements of the partially-constructed Upper matrix
     * @param solveColumn the target column for which element values are to be generated
     * @param <T> the target value type
     * @return the updated elements for the Lower matrix
     */
    <T> Array2D<T> solveColumnLower(Array2D<T> lower, Array2D<T> upper, int solveColumn) {

        T upperColumnValue = upper.get(solveColumn, solveColumn);

        OperationStrategy<T> operationStrategy = lower.getOperationStrategy();

        return new Array2DConcrete<T>(operationStrategy, lower, getLowerMatrixBuilder(lower.getOperationStrategy(), upper, solveColumn, upperColumnValue));
    }

    /**
     * get the function that will generate updated elements for the non-reduced Upper matrix based on a given solution column
     * @param op the set of arithmetic operations for the target value type
     * @param upper the current elements of the partially-constructed Upper matrix
     * @param solveColumn the target column for which element values are to be generated
     * @param divisor the divisor value to be applied to the updated values in the Upper matrix
     * @param <T> the target value type
     * @return the function that will generate the next set of element values for the non-reduced Upper matrix
     */
    <T> Function<Indexor<T>, T> getUpperMatrixBuilder(OperationStrategy<T> op, Array2D<T> upper, int solveColumn, T divisor) {

        T zero = op.zero();

        T[] sourceRow = upper.getRow(solveColumn);

        return indexor -> {

            T value = indexor.getValue();

            if (indexor.getRow() <= solveColumn)
                return value;

            if (indexor.getColumn() < solveColumn)
                return zero;

            if (! op.isZero(value))
                value = op.product(value, sourceRow[solveColumn]);

            value = op.difference(value, op.product(upper.get(solveColumn, indexor.getRow()), sourceRow[indexor.getColumn()]));

            if (! (op.isZero(value) || op.isOne(divisor)))
                value = op.quotient(value, divisor);

            return value;
        };
    }

    /**
     * construct the next version of the non-reduced Upper matrix
     * @param upper the current elements of the partially-constructed Upper matrix
     * @param solveColumn the target column for which element values are to be generated
     * @param divisor the divisor value to be applied to the updated elements in the Upper matrix
     * @param <T> the target value type
     * @return the updated elements of the non-reduced Upper matrix
     */
    <T> Array2D<T> solveColumnUpper(Array2D<T> upper, int solveColumn, T divisor) {

        OperationStrategy<T> op = upper.getOperationStrategy();

        return new Array2DConcrete<>(op, upper, getUpperMatrixBuilder(op, upper, solveColumn, divisor));
    }

    /**
     * build the precision-conserving "non-reduced" Upper matrix elements and the lower matrix elements
     * corresponding to the provided matrix
     * @param original the Array2D representing the elements of the original matrix
     * @param <T> the target value type
     * @return a Tuple containing Array2D instance representing the elements of the "non-reduced" Upper matrix and
     * those of the Lower matrix
     */
    <T> Tuple<Array2D<T>, Array2D<T>> buildUpperAndLowerMatrices(Array2D<T> original) {

        int order = Math.min(original.getHeight(), original.getWidth());

        OperationStrategy<T> operationStrategy = original.getOperationStrategy();

        Array2D<T> lower = Matrix.identityArray(order, operationStrategy);

        T divisor = operationStrategy.one();

        Array2D<T> upper = original;

        for (int column = 0; column < order; column++) {

            if (column > 0)
                divisor = upper.get(column - 1, column - 1);

            lower = solveColumnLower(lower, upper, column);

            upper = solveColumnUpper(upper, column, divisor);
        }

        return new Tuple<>(upper, lower);
    }

    /**
     * reduce the precision-conserving upper matrix by dividing each row by the preceding diagonal value
     * @param upper the Array2D representing the elements of the upper Matrix to be reduced
     * @param <T> the target value type
     * @return the reduced version of the upper matrix
     */
    <T> Array2D<T> reduceUpper(Array2D<T> upper) {

        OperationStrategy<T> operationStrategy = upper.getOperationStrategy();

        return new Array2DConcrete<T>(operationStrategy, upper, indexor -> {
            if (indexor.getRow() == 0)
                return indexor.getValue();

            T div = indexor.getSource().get(indexor.getRow() - 1, indexor.getRow() - 1);

            return operationStrategy.quotient(indexor.getValue(), div);
        });
    }

    /**
     * construct the result object from an Array2D representing elements of a non-reduced Upper matrix
     * and an Array2D representing elements of a Lower matrix
     * @param upper an Array2D representing elements of an unreduced Upper matrix
     * @param lower an Array2D representing elements of a Lower matrix
     * @param <T> the target value type
     * @return a result object containing the reduced Upper matrix, the Lower matrix, and the associated determinant
     */
    <T> LUMatrixResult<T> constructResult(Array2D<T> upper, Array2D<T> lower) {

        T determinate = upper.get(upper.getWidth() - 1, upper.getHeight() - 1);

        upper = reduceUpper(upper);

        return new LUMatrixResult<>(new Matrix<>(lower), new Matrix<>(upper), determinate);
    }

    /**
     * compute a LUMatrixResult providing the Upper and Lower matrices (plus determinant) corresponding to the provided
     * Array2D representing the elements of the original Matrix
     * @param original an Array2D representing the elements of a Matrix to be decomposed into the corresponding Upper and Lower matrices
     * @param <T> the target value type
     * @return a result object containing the Upper Matrix, the Lower Matrix, and the associated determinant
     */
    <T> LUMatrixResult<T> computeUpperLower(Array2D<T> original) {

        original = presorter.presort(original);

        if (original == null)
            return null;

        Tuple<Array2D<T>, Array2D<T>> upperAndLower = buildUpperAndLowerMatrices(original);

        return constructResult(upperAndLower.getA(), upperAndLower.getB());
    }

    /**
     * compute the Upper Matrix corresponding to the provided source Matrix
     * @param matrix the source Matrix
     * @param <T> the target value type
     * @return the corresponding Upper Matrix
     */
    public <T> Matrix<T> computeUpper(Matrix<T> matrix) {
        return computeUpperLower(matrix.members).getUpper();
    }

    /**
     * compute the Upper and Lower matrices and the associated determinant for the provided source Matrix
     * @param matrix the source Matrix
     * @param <T> the target value type
     * @return an LUMatrixResult containing the Upper Matrix, the Lower Matrix, and the determinant for the source Matrix
     */
    public <T> LUMatrixResult<T> computeUpperLower(Matrix<T> matrix) {

        return computeUpperLower(matrix.members);
    }
}
