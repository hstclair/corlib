package com.stclair.corlib.math.matrix.determinant;

import com.stclair.corlib.math.matrix.Matrix;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hstclair on 4/21/17.
 */
public class MatrixMinorDeterminantSolver implements DeterminantSolver {

    public <T> T determinant(Matrix<T> matrix) {

        LinkedList<MatrixMinorDeterminantSolverTask<T>> results = buildResults(matrix);

        return collectResults(matrix.getOperationStrategy(), results);
    }

    public static <T> LinkedList<MatrixMinorDeterminantSolverTask<T>> buildResults(Matrix<T> matrix) {

        LinkedList<MatrixMinorDeterminantSolverTask<T>> results = new LinkedList<>();
        LinkedList<MatrixMinorDeterminantSolverTask<T>> matrixMinorDeterminantSolverTasks = new LinkedList<>();

        matrixMinorDeterminantSolverTasks.add(new MatrixMinorDeterminantSolverTask<>(matrix.getOperationStrategy().one(), matrix));

        while (! matrixMinorDeterminantSolverTasks.isEmpty()) {
            MatrixMinorDeterminantSolverTask<T> matrixMinorDeterminantSolverTask = matrixMinorDeterminantSolverTasks.removeLast();

            List<MatrixMinorDeterminantSolverTask<T>> newMatrixMinorDeterminantSolverTasks = matrixMinorDeterminantSolverTask.createTasks();

            for (MatrixMinorDeterminantSolverTask<T> newMatrixMinorDeterminantSolverTask : newMatrixMinorDeterminantSolverTasks) {
                if (newMatrixMinorDeterminantSolverTask.isTwoByTwo()) {
                    results.add(newMatrixMinorDeterminantSolverTask);
                } else {
                    matrixMinorDeterminantSolverTasks.addFirst(newMatrixMinorDeterminantSolverTask);
                }
            }
        }

        return results;
    }

    public static <T> T collectResults(OperationStrategy<T> op, LinkedList<MatrixMinorDeterminantSolverTask<T>> results) {

        T accumulator = null;

        while (! results.isEmpty()) {
            MatrixMinorDeterminantSolverTask<T> result = results.remove();

            T evaluation = result.evaluate();

            if (accumulator == null)
                accumulator = evaluation;
            else
                accumulator = op.sum(accumulator, evaluation);
        }

        return accumulator;
    }

}
