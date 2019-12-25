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

    static <T> int firstSolveableRow(Array2D<T> members, int column, OperationStrategy<T> op) {

        int noSolveableRow = -1;
        int[] solveableRow = new int[] { noSolveableRow };

        members.traverse(indexor -> {
            if (solveableRow[0] == noSolveableRow && indexor.getColumn() == column && indexor.getRow() >= column && ! op.isZero(indexor.getValue()))
                solveableRow[0] = indexor.getRow();
        } );

        return solveableRow[0];
    }

    <T> Function<Indexor<T>, T> switchRows(int rowA, int rowB) {

        return (indexor) -> {

            if (indexor.getRow() == rowA)
                return indexor.getSource().get(indexor.getColumn(), rowB);

            if (indexor.getRow() == rowB)
                return indexor.getSource().get(indexor.getColumn(), rowA);

            return indexor.getValue();
        };
    }

    <T> SolutionState<T> prepColumn(Array2D<T> members, int column, OperationStrategy<T> op) {
        int row = firstSolveableRow(members, column, op);

        if (row < column) {
            return new SolutionState<>(RowSolutionState.NoSolution, members);   // column cannot be solved (therefore, matrix cannot be solved)
        }

        if (row == column) {
            return new SolutionState<>(RowSolutionState.Solvable, members);    // matrix is ready for column to be solved
        }

        // matrix is ready for column to be solved but rows were exchanged
        return new SolutionState<>(RowSolutionState.Exchanged, new Array2DConcrete<T>(members, switchRows(column, row)));
    }

    <T> T solveRow(T[] referenceRow, T[] row, int solveColumn, T divisor, OperationStrategy<T> op) {

        return matrixRowSolver.solveRow(referenceRow, row, solveColumn, divisor, op);
    }

//    <T> T solveColumnOrg(Array2D<T> upper, Array2D<T> lower, int solveColumn, T divisor, OperationStrategy<T> op) {
//
//        for (int row = solveColumn + 1; row < upper.getHeight(); row++) {
//
//            T upperValue = upper.get(solveColumn, row);
//
//            T factor = solveRow(upper[solveColumn], upper[row], solveColumn, divisor, op);
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

    <T> ColumnSolution<T> solveColumn(Array2D<T> upper, Array2D<T> lower, int solveColumn, T divisor, OperationStrategy<T> op) {

        T factor = upper.get(solveColumn, solveColumn);

        Function<Indexor<T>, T> upperSolver = indexor -> {

            if (indexor.getRow() < solveColumn + 1)
                return indexor.getValue();

            if (indexor.getColumn() == solveColumn)
                return op.zero();

            if (indexor.getColumn() < solveColumn + 1)
                return indexor.getValue();

            T value = indexor.getValue();

            if (! op.isZero(indexor.getValue()))
                value = op.product(value, /* upper.get(solveColumn, solveColumn) */ factor);

            value = op.difference(value, op.product(upper.get(solveColumn, indexor.getRow()), upper.get(indexor.getColumn(), solveColumn)));

            T upperValue = upper.get(solveColumn, indexor.getRow());

            if (! (op.isZero(value) || op.isOne(divisor)))
                value = op.quotient(value, upperValue);

            return value;
        };

        Array2D<T> solvedUpper = new Array2DConcrete<T>(upper, upperSolver);

        Function<Indexor<T>, T> lowerSolver = indexor -> {

            if (indexor.getColumn() != solveColumn)
                return indexor.getValue();

            return op.quotient(solvedUpper.get(solveColumn, indexor.getRow()), factor);
        };

        if (lower != null)
            lower = new Array2DConcrete<T>(lower, lowerSolver);

        return new ColumnSolution<T>(factor, lower, solvedUpper);
    }

    <T> LUMatrixResult<T> computeUpperLower(Array2D<T> upper, OperationStrategy<T> op, boolean computeLower) {

        boolean negate = false;

        T divisor = op.one();

        ColumnSolution<T> columnSolution;

        int order = Math.min(upper.getHeight(), upper.getWidth());

        Array2D<T> lower = computeLower ? Matrix.identityArray(order, op) : null;

        for (int column = 0; column < order; column++) {

            SolutionState<T> state = prepColumn(upper, column, op);

            if (state.rowSolutionState == RowSolutionState.NoSolution)
                return null;

            if (state.rowSolutionState == RowSolutionState.Exchanged)
                negate = ! negate;

            columnSolution = solveColumn(upper, lower, column, divisor, op);

            divisor = columnSolution.factor;
            upper = columnSolution.upper;
            lower = columnSolution.lower;
        }

        if (negate) {
            upper = new Array2DConcrete<T>(upper, indexor -> {
                if (indexor.getRow() == indexor.getColumn() && indexor.getRow() == order - 1)
                    return op.negate(indexor.getValue());

                return indexor.getValue();
            });
            divisor = op.negate(divisor);
        }

        return new LUMatrixResult<>(computeLower ? new Matrix<>(lower, op) : null, new Matrix<>(upper, op), divisor);
    }

    public <T> Matrix<T> computeUpper(Matrix<T> matrix) {
        return computeUpperLower(matrix.members, matrix.op, false).getUpper();
    }

    public <T> LUMatrixResult<T> computeUpperLower(Matrix<T> matrix) {

        Array2D<T> members = matrix.cloneMembers();

        return computeUpperLower(members, matrix.op, true);
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
