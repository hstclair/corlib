package com.stclair.corlib.math.matrix;

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

    // X X X
    // 0 X X
    // 0 0 X

    <T> boolean isRowSolveable(T[] members, int column, OperationStrategy<T> op) {
        return ! op.isZero(members[column]);
    }

    <T> int firstMatchingEntry(T[] entries, int start, int end, int alternative, Function<T, Boolean> filter) {
        return IntStream.range(start, end)
                .filter(index -> filter.apply(entries[index]))
                .findFirst()
                .orElse(alternative);
    }

    <T> int firstSolveableRow(T[][] members, int column, OperationStrategy<T> op) {
        return firstMatchingEntry(members, column, members.length, -1, it -> isRowSolveable(it, column, op));
    }

    <T> T[][] switchRows(T[][] members, int rowA, int rowB) {
        T[] temp = members[rowA];
        members[rowA] = members[rowB];
        members[rowB] = temp;

        return members;
    }

    <T> RowSolutionState prepColumn(T[][] members, int column, OperationStrategy<T> op) {
        int row = firstSolveableRow(members, column, op);

        if (row < column) {
            return RowSolutionState.NoSolution;   // column cannot be solved (therefore, matrix cannot be solved)
        }

        if (row == column) {
            return RowSolutionState.Solvable;    // matrix is ready for column to be solved
        }

        switchRows(members, column, row);

        return RowSolutionState.Exchanged;        // matrix is ready for column to be solved but rows were exchanged
    }

    <T> T solveRow(T[] referenceRow, T[] row, int solveColumn, T divisor, OperationStrategy<T> op) {

        return matrixRowSolver.solveRow(referenceRow, row, solveColumn, divisor, op);
    }

    <T> T solveColumn(T[][] upper, T[][] lower, int solveColumn, T divisor, OperationStrategy<T> op) {

        for (int row = solveColumn + 1; row < upper.length; row++) {

            T upperValue = upper[row][solveColumn];

            T factor = solveRow(upper[solveColumn], upper[row], solveColumn, divisor, op);

            if (lower != null)
                lower[row][solveColumn] = op.quotient(upperValue, factor);
        }

        T factor = upper[solveColumn][solveColumn];

        for (int col = solveColumn; col < upper[solveColumn].length; col++)
            upper[solveColumn][col] = op.quotient(upper[solveColumn][col], divisor);

        return factor;
    }

    <T> LUMatrixResult<T> computeUpperLower(T[][] upper, OperationStrategy<T> op, boolean computeLower) {

        boolean negate = false;

        T divisor = op.one();

        int order = upper.length;

        T[][] lower = computeLower ? Matrix.identityArray(order, op) : null;

        for (int column = 0; column < order; column++) {

            RowSolutionState state = prepColumn(upper, column, op);

            if (state == RowSolutionState.NoSolution)
                return null;

            if (state == RowSolutionState.Exchanged)
                negate = ! negate;

            divisor = solveColumn(upper, lower, column, divisor, op);
        }

        if (negate) {
            upper[order - 1][order - 1] = op.negate(upper[order - 1][order - 1]);
            divisor = op.negate(divisor);
        }

        return new LUMatrixResult<>(computeLower ? new Matrix<>(lower, op) : null, new Matrix<>(upper, op), divisor);
    }

    public <T> Matrix<T> computeUpper(Matrix<T> matrix) {
        return computeUpperLower(matrix.cloneMembers(), matrix.op, false).getUpper();
    }

    public <T> LUMatrixResult<T> computeUpperLower(Matrix<T> matrix) {

        T[][] members = matrix.cloneMembers();

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
