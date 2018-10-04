package com.stclair.corlib.math.matrix;


import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.math.util.ValueFactory;

/**
 * Created by hstclair on 4/22/17.
 */
public class DeterminantLUSolver {

//    final T ZERO;
//
//    final T ONE;

    MatrixLUDecomposition decomposor = new MatrixLUDecomposition();

//    final Matrix<T> matrix;
//
//    public DeterminantLUSolver(Matrix<T> matrix) {
//        this.matrix = matrix;
//        OperationStrategy<T> op = matrix.op;
//        ZERO = op.zero();
//        ONE = op.one();
//    }
//
//    enum RowSolutionState {
//        NoSolution,     // matrix has no solution
//        Solvable,      // row may be solved
//        Exchanged       // row may be solved but rows were transposed
//    }
//
//    int firstSolveableRow(Value<T>[][] members, int column) {
//        for (int row = column; row < members.length; row++) {
//            if (! members[row][column].isZero()) {
//                return row;
//            }
//        }
//
//        return -1;
//    }
//
//    Value<T>[][] switchRows(Value<T>[][] members, int rowA, int rowB) {
//        Value<T>[] temp = members[rowA];
//        members[rowA] = members[rowB];
//        members[rowB] = temp;
//
//        return members;
//    }
//
//    RowSolutionState prepColumn(Value<T>[][] members, int column) {
//        int row = firstSolveableRow(members, column);
//
//        if (row < column) {
//            return RowSolutionState.NoSolution;   // column cannot be solved (therefore, matrix cannot be solved)
//        }
//
//        if (row == column) {
//            return RowSolutionState.Solvable;    // matrix is ready for column to be solved
//        }
//
//        switchRows(members, column, row);
//        return RowSolutionState.Exchanged;        // matrix is ready for column to be solved but rows were exchanged
//    }
//
//    void solveRow(T[] referenceRow, T[] row, int solveColumn, T divisor) {
//
//        for (int column = solveColumn+1; column < row.length; column++) {
//
//            if (! row[column].isZero())
//                row[column] = row[column].multiply(referenceRow[solveColumn]);
//
//            row[column] = row[column].subtract(row[solveColumn].multiply(referenceRow[column]));
//
//            if (! (row[column].isZero() || divisor == ONE))
//                row[column] = row[column].divide(divisor);
//        }
//
//        row[solveColumn] = ZERO;
//    }
//
//    Value<T> solveColumn(Value<T>[][] members, int solveColumn, Value<T> divisor) {
//        for (int row = solveColumn + 1; row < members.length; row++) {
//            solveRow(members[solveColumn], members[row], solveColumn, divisor);
//        }
//
//        return members[solveColumn][solveColumn];
//    }
//
//    T solveMatrix(Value<T>[][] members) {
//        boolean negate = false;
//
//        Value<T> divisor = ONE;
//
//        int order = members.length;
//
//        for (int column = 0; column < order; column++) {
//            RowSolutionState state = prepColumn(members, column);
//
//            if (state == RowSolutionState.NoSolution)
//                return null;
//
//            if (state == RowSolutionState.Exchanged)
//                negate = ! negate;
//
//            divisor = solveColumn(members, column, divisor);
//        }
//
//        if (negate)
//            members[order-1][order-1] = members[order-1][order-1].negate();
//
//        return members[order-1][order-1].value();
//    }

    <T> T solve(Matrix<T> matrix) {

        return decomposor.computeUpperLower(matrix).determinant();
    }
}
