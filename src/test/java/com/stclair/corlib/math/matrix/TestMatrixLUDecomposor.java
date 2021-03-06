package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestMatrixLUDecomposor {

    @Test
    public void test2X2Double() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { { 4, 3 }, { 6, 3 } }, op);
        Matrix<Double> expected = new Matrix<>(new double[][] { { 4, 3 }, { 0, -1.5 } }, op);

        // 4*6, 3*6
        // 6*4, 3*4

        // 4, 3
        // 0, 18-12

        // 4, 3
        // 0, 6

        MatrixLUDecomposor instance = new MatrixLUDecomposor();

        Matrix<Double> result = instance.computeUpper(value);

        assertEquals(expected, result);
    }


    @Test
    public void test3x3Double() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} }, op);
        Matrix<Double> expected = new Matrix<>(new double[][] { {6, 1, 1}, {0, -8d/3, 13d/3}, {-0d, -0d, 153d/8} }, op);

        MatrixLUDecomposor instance = new MatrixLUDecomposor();

        Matrix<Double> actual = instance.computeUpper(value);

        assertEquals(expected, actual);
    }

    @Test
    public void test3x3DoubleLU() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} }, op);
        Matrix<Double> expectedUpper = new Matrix<>(new double[][] { {6, 1, 1}, {0, -8d/3, 13d/3}, {-0d, -0d, 153d/8d} }, op);

        Matrix<Double> expectedLower = new Matrix<>(new double[][] {{1, 0, 0}, {2d/3, 1, 0}, {1d/3, -23d/8, 1}}, op);

        MatrixLUDecomposor instance = new MatrixLUDecomposor();

        LUMatrixResult<Double> result = instance.computeUpperLower(value);

        assertEquals(expectedUpper, result.getUpper());
        assertEquals(expectedLower, result.getLower());
    }


}
