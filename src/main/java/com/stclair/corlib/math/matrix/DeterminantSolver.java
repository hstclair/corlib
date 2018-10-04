package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.OperationStrategy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hstclair on 4/21/17.
 */
public class DeterminantSolver<T> {
    class Task {
        final T coefficient;

        final Matrix<T> matrix;

        final OperationStrategy<T> op;

        Task(T coefficient, Matrix<T> matrix) {
            this.coefficient = coefficient;
            this.matrix = matrix;
            this.op = matrix.op;
        }

        boolean canSolve() {
            return matrix.order == 2;
        }

        T evaluate() {
            return op.product(coefficient,
                    op.difference(
                            op.product(matrix.member(0,0), matrix.member(1, 1)),
                            op.product(matrix.member(0, 1), matrix.member(1, 0))));
        }

        List<Task> createTasks() {
            List<Task> result = new ArrayList<>();

            if (canSolve()) {   // handle the edge case that occurs when initial matrix is 2x2
                result.add(this);
                return result;
            }

            boolean negate = true;

            for (int col = 0; col < matrix.order; col++) {
                T newCoefficient = matrix.member(0, col);

                negate = !negate;

                if (op.isZero(newCoefficient))
                    continue;

                if (negate)
                    newCoefficient = op.negate(newCoefficient);

                result.add(new Task(op.product(newCoefficient, coefficient), matrix.minor(0, col)));
            }

            return result;
        }
    }

    final LinkedList<Task> tasks = new LinkedList<>();
    final LinkedList<Task> results = new LinkedList<>();
    final OperationStrategy<T> op;

    public DeterminantSolver(Matrix<T> matrix) {
        tasks.add(new Task(matrix.op.one(), matrix));
        op = matrix.op;
    }

    public T solve() {
        while (! tasks.isEmpty()) {
            Task task = tasks.removeLast();

            List<Task> newTasks = task.createTasks();

            for (Task newTask : newTasks) {
                if (newTask.canSolve()) {
                    results.add(newTask);
                } else {
                    tasks.addFirst(newTask);
                }
            }
        }

        T accumulator = null;

        while (! results.isEmpty()) {
            Task result = results.remove();

            T evaluation = result.evaluate();

            if (accumulator == null)
                accumulator = evaluation;
            else
                accumulator = op.sum(accumulator, evaluation);
        }

        return accumulator;
    }

}
