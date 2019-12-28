package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MatrixLUDecomposition {

    MatrixRowSolver matrixRowSolver = new MatrixRowSolver();

    // TODO Use Presort to arrange rows before solving L & U

    public MatrixLUDecomposition() {
    }

    enum RowSolutionState {
        NoSolution,     // matrix has no solution
        Solvable,       // row may be solved
        Exchanged       // row may be solved but rows were transposed
    }

    static class SolutionState<T> {
        public final RowSolutionState rowSolutionState;

        public final Array2D<T> values;

        public SolutionState(RowSolutionState rowSolutionState, Array2D<T> values) {
            this.rowSolutionState = rowSolutionState;
            this.values = values;
        }
    }

    static class ColumnSolution<T> {
        public final T factor;

        public final Array2D<T> lower;

        public final Array2D<T> upper;

        public ColumnSolution(T factor, Array2D<T> lower, Array2D<T> upper) {
            this.factor = factor;
            this.lower = lower;
            this.upper = upper;
        }
    }


    // X X X
    // 0 X X
    // 0 0 X

    <T> T solveRow(T[] referenceRow, T[] row, int solveColumn, T divisor, OperationStrategy<T> op) {

        return matrixRowSolver.solveRow(referenceRow, row, solveColumn, divisor, op);
    }

//    <T> T solveColumnOrgOrg(Array2D<T> upper, Array2D<T> lower, int solveColumn, T divisor, OperationStrategy<T> op) {
//
//        for (int row = solveColumn + 1; row < upper.getHeight(); row++) {
//
//            T upperValue = upper.get(solveColumn, row);
//
//            T factor = solveRow(upper.getRow(solveColumn), upper.getRow(row), solveColumn, divisor, op);
//
//            if (lower != null)
//                lower[row][solveColumn] = op.quotient(upperValue, factor);
//        }
//
//        T factor = upper[solveColumn][solveColumn];
//
//        for (int col = solveColumn; col < upper[solveColumn].length; col++)
//            upper[solveColumn][col] = op.quotient(upper[solveColumn][col], divisor);
//
//        return factor;
//    }

    <T> Array2D<T> solveColumnUpper(Array2D<T> upper, int solveColumn, T divisor) {

        OperationStrategy<T> op = upper.getOperationStrategy();

        T zero = op.zero();

        T[] sourceRow = upper.getRow(solveColumn);

        return new Array2DConcrete<T>(op, upper, indexor -> {

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
        });
    }

//    <T> Array2D<T> solveColumnLower(Array2D<T> upper, Array2D<T> lower, int solveColumn, T divisor) {
//
//    }

//    <T> ColumnSolution<T> solveColumnOrg(Array2D<T> upper, Array2D<T> lower, int solveColumn, T divisor, OperationStrategy<T> op) {
//
//        T factor = upper.get(solveColumn, solveColumn);
//
//        Function<Indexor<T>, T> upperSolver = indexor -> {
//
//            if (indexor.getRow() < solveColumn + 1)
//                return indexor.getValue();
//
//            if (indexor.getColumn() == solveColumn)
//                return op.zero();
//
//            if (indexor.getColumn() < solveColumn + 1)
//                return indexor.getValue();
//
//            T value = indexor.getValue();
//
//            if (! op.isZero(indexor.getValue()))
//                value = op.product(value, /* upper.get(solveColumn, solveColumn) */ factor);
//
//            value = op.difference(value, op.product(upper.get(solveColumn, indexor.getRow()), upper.get(indexor.getColumn(), solveColumn)));
//
//            T upperValue = upper.get(solveColumn, indexor.getRow());
//
//            if (! (op.isZero(value) || op.isOne(divisor)))
//                value = op.quotient(value, upperValue);
//
//            return value;
//        };
//
//        Array2D<T> solvedUpper = new Array2DConcrete<T>(upper.getOperationStrategy(), upper, upperSolver);
//
//        Function<Indexor<T>, T> lowerSolver = indexor -> {
//
//            if (indexor.getColumn() != solveColumn)
//                return indexor.getValue();
//
//            return op.quotient(solvedUpper.get(solveColumn, indexor.getRow()), factor);
//        };
//
//        if (lower != null)
//            lower = new Array2DConcrete<T>(lower.getOperationStrategy(), lower, lowerSolver);
//
//        return new ColumnSolution<T>(factor, lower, solvedUpper);
//    }

    <T> LUMatrixResult<T> computeUpperLower(Array2D<T> upper, OperationStrategy<T> op, boolean computeLower) {

        Array2DPresortEvaluator<T> presorter = new Array2DPresortEvaluator<>(op);

        upper = presorter.presort(upper);

        if (upper == null)
            return null;

        T divisor = op.one();

        ColumnSolution<T> columnSolution;

        int order = Math.min(upper.getHeight(), upper.getWidth());

        Array2D<T> lower = computeLower ? Matrix.identityArray(order, op) : null;

        for (int column = 0; column < order; column++) {

            upper = solveColumnUpper(upper, column, divisor);

//            columnSolution = solveColumn(upper, lower, column, divisor, op);

//            divisor = columnSolution.factor;
//            upper = columnSolution.upper;
//            lower = columnSolution.lower;
        }

        return new LUMatrixResult<>(computeLower ? new Matrix<>(lower, op) : null, new Matrix<>(upper, op), upper.get(upper.getWidth()-1, upper.getHeight()-1));
    }

    public <T> Matrix<T> computeUpper(Matrix<T> matrix) {
        return computeUpperLower(matrix.members, matrix.op, false).getUpper();
    }

    public <T> LUMatrixResult<T> computeUpperLower(Matrix<T> matrix) {

        return computeUpperLower(matrix.members, matrix.op, true);
    }


    // for an nXn matrix M:
    // for each column i of matrix
    //    1. Make sure that the value of M[i][i] is non-zero (swap rows if needed)
    //    2. For each subsequent row j for which M[j][i] is non-zero:
    //       1. For each column k (including column i):
    //          1. Set M[j][k] equal to M[j][k]*M[i][i]-M[j][i]*M[i][k]
    //
    // (i.e. first row has first column non-zero, second row has first column zero and second column non-zero,
    //   third row has first and second columns zero and third column non-zero, etc.)
    // Add the number of rows exchanged during this process to the total rows exchanged
    // Reduce all subsequent rows (using initial rows) so that initial k column(s) are zero for each row below row k
    // Repeat sort

    // when reducing any row R in the nXn matrix M by a row k T:
    // 1. set the first k+1 columns of result set to 0
    // 2. for each remaining column j (in k through n) of the reduced row R:
    //    set result column j to (R[j]*T[k] - R[k]*T[j])/M[k-1][k-1]

    // when all rows have been exhausted:
    // If the number of rows exchanged during all sorting steps is odd, negate the value in M[n][n]
    // The value in M[n][n] is the determinant of the matrix

}
