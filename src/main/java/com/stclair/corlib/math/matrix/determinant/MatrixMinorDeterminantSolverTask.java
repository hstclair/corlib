package com.stclair.corlib.math.matrix.determinant;

import com.stclair.corlib.math.matrix.Matrix;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.ArrayList;
import java.util.List;

public class MatrixMinorDeterminantSolverTask<T> {
    final T coefficient;

    final Matrix<T> matrix;

    final OperationStrategy<T> op;

    MatrixMinorDeterminantSolverTask(T coefficient, Matrix<T> matrix) {

        if (matrix.getColumns() != matrix.getRows())
            throw new IllegalArgumentException("Cannot compute determinant for non-square matrix");

        this.coefficient = coefficient;
        this.matrix = matrix;
        this.op = matrix.getOperationStrategy();
    }

    boolean isTwoByTwo() {
        return matrix.getRows() == 2 && matrix.getColumns() == 2;
    }

    T evaluate() {
        return op.product(coefficient,
                op.difference(
                        op.product(matrix.member(0,0), matrix.member(1, 1)),
                        op.product(matrix.member(0, 1), matrix.member(1, 0))));
    }

    List<MatrixMinorDeterminantSolverTask<T>> createTasks() {
        List<MatrixMinorDeterminantSolverTask<T>> result = new ArrayList<>();

        if (isTwoByTwo()) {   // handle the edge case that occurs when initial matrix is 2x2
            result.add(this);
            return result;
        }

        boolean negate = true;

        for (int col = 0; col < matrix.getColumns(); col++) {
            T newCoefficient = matrix.member(0, col);

            negate = !negate;

            if (op.isZero(newCoefficient))
                continue;

            if (negate)
                newCoefficient = op.negate(newCoefficient);

            result.add(new MatrixMinorDeterminantSolverTask<>(op.product(newCoefficient, coefficient), matrix.minor(0, col)));
        }

        return result;
    }
}
