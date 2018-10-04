package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.OperationStrategy;

public class MatrixRowSolver {

    public <T> T solveRow(T[] referenceRow, T[] row, int solveColumn, T divisor, OperationStrategy<T> op) {

        T factor = referenceRow[solveColumn];

        for (int column = solveColumn+1; column < row.length; column++) {

            if (! op.isZero(row[column]))
                row[column] = op.product(row[column], referenceRow[solveColumn]);

            row[column] = op.difference(row[column], op.product(row[solveColumn], referenceRow[column]));

            if (! (op.isZero(row[column]) || op.isOne(divisor)))
                row[column] = op.quotient(row[column], divisor);
        }

        row[solveColumn] = op.zero();

        return factor;
    }
}
