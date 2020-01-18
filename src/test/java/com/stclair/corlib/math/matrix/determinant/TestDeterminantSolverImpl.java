package com.stclair.corlib.math.matrix.determinant;

import com.stclair.corlib.math.matrix.Matrix;
import com.stclair.corlib.math.util.*;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by hstclair on 4/21/17.
 */
public class TestDeterminantSolverImpl<T> implements TestDeterminantSolver<T> {

    OperationStrategy<T> operationStrategy;

    DeterminantSolver instance;

    public TestDeterminantSolverImpl(OperationStrategy<T> operationStrategy, DeterminantSolver instance) {
        this.operationStrategy = operationStrategy;
        this.instance = instance;
    }

    @Override
    public void test3x3Determinant() {
        T expected = operationStrategy.from(-306d);

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test4x4Determinant() {

        T expected = operationStrategy.from(-418d);

        double[][] members = new double[][] { {3, 2, -1, 4}, {2, 1, 5, 7}, {0, 5, 2, -6}, {-1, 2, 1, 0} };

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test5x5Determinant() {
        double[][] members = new double[][] {
                { -5, 36, 38, 6, 11 },
                { -33, 3, 24, 22, -9 },
                { 36, -26, -33, -43, -47 },
                { 0, 27, -28, -26, 23 },
                { 7, -39, 33, 3, -43 },
        };

        T expected = operationStrategy.from(-61089854);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test6x6DeterminantA() {
        double[][] members = new double[][] {
                { -3, 0, -5, -4, -1, -5 },
                { -4, 2, -2, 3, 2, -3 },
                { 0, 4, 0, -5, -2, -2 },
                { -4, -4, 0, -3, 1, -3 },
                { 3, 4, -2, -1, 4, -2 },
                { -1, -5, -5, 3, -3, -5 },
        };

        T expected = operationStrategy.from(36027);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test6x6DeterminantB() {
        double[][] members = new double[][] {
                { 3, 36, 16, 2, 38, -41 },
                { -34, -39, 1, -44, 20, 14 },
                { -9, 28, -49, 2, 26, 29 },
                { -21, -49, -42, -36, 2, -25 },
                { -29, 25, -18, 28, -17, -13 },
                { -6, -47, 19, 24, 6, 19 },
        };

        T expected = operationStrategy.from(40433588418L);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test7x7Determinant() {
        double[][] members = new double[][] {
                {0,-5,-4,2,-2,-5,-5},
                {-4,-1,0,-1,-4,0,-3},
                {2,-3,2,-3,4,0,-5},
                {3,0,-4,-2,-3,-5,-5},
                {-2,-2,-2,2,-4,3,3},
                {4,0,0,0,2,-5,-3},
                {-2,-5,0,-3,-2,-5,-2},
        };

        T expected = operationStrategy.from(90344);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test10x10DeterminantA() {
        double[][] members = new double[][] {
                {1,0,0,-1,-1,1,-1,0,3,-3},
                {2,3,-4,-2,2,3,-2,-4,0,4},
                {-1,-3,3,0,0,-3,4,4,-3,-2},
                {3,-3,4,3,-1,0,-2,-1,-4,1},
                {-5,-5,1,4,0,2,-2,-2,0,4},
                {-5,-5,-4,-4,1,-4,-4,-5,3,2},
                {-1,-5,-4,2,-2,-4,-4,0,0,-5},
                {1,3,-3,0,0,-5,1,-4,1,0},
                {-2,0,-5,4,3,3,-5,1,4,-2},
                {-2,-2,-2,-5,0,4,-3,-3,3,-5},
        };

        T expected = operationStrategy.from(-39656706);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        assertEquals(expected, result);
    }

    @Override
    public void test10X10DeterminantB(T tolerance) {
        double[][] members = new double[][] {
                { 21, 45, 5, 17, 60, 47, 52, 87, 14, 36 },
                { 81, 20, 84, 48, 66, 10, 86, 74, 5, 0 },
                { 72, 99, 47, 96, 20, 64, 94, 22, 5, 91 },
                { 0, 58, 96, 89, 27, 2, 91, 61, 4, 63 },
                { 24, 25, 53, 83, 5, 45, 18, 42, 20, 7 },
                { 21, 6, 52, 97, 47, 80, 5, 50, 23, 40 },
                { 29, 44, 90, 96, 36, 38, 44, 16, 26, 72 },
                { 49, 58, 99, 75, 96, 99, 10, 20, 53, 28 },
                { 77, 3, 17, 60, 60, 14, 60, 15, 71, 16 },
                { 61, 82, 45, 16, 29, 39, 65, 75, 98, 22 }
        };

        T expected = operationStrategy.from(-2583406973511127022L);

        Matrix<T> matrix = new Matrix<>(members, operationStrategy);

        T result = instance.determinant(matrix);

        if (tolerance != null) {
            assertTrue(operationStrategy.lessThanOrEqual(result, operationStrategy.sum(expected, tolerance)));
            assertTrue(operationStrategy.greaterThanOrEqual(result, operationStrategy.difference(expected, tolerance)));
        } else
            assertEquals(expected, result);



//        assertEquals(expected, result); // 4500 tolerance for double on LU determinant computation
    }
}
