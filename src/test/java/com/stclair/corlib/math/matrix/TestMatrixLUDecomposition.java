package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestMatrixLUDecomposition {

    @Test
    public void test2X2Double() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { { 4, 3 }, { 6, 3 } }, op);
        Matrix<Double> expected = new Matrix<>(new double[][] { { 4, 3 }, { 0, -1.5 } }, op);

        MatrixLUDecomposition instance = new MatrixLUDecomposition();

        Matrix<Double> result = instance.computeUpper(value);

        assertEquals(expected, result);
    }


    @Test
    public void test3x3Double() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} }, op);
        Matrix<Double> expected = new Matrix<>(new double[][] { {6, 1, 1}, {0, -16d/6, 26d/6}, {0, 0, 19.125} }, op);

        MatrixLUDecomposition instance = new MatrixLUDecomposition();

        Matrix<Double> result = instance.computeUpper(value);

        assertEquals(expected, result);
    }

    @Test
    public void test3x3DoubleLU() {

        DoubleOperationStrategy op = new DoubleOperationStrategy();

        Matrix<Double> value = new Matrix<>(new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} }, op);
        Matrix<Double> expectedUpper = new Matrix<>(new double[][] { {6, 1, 1}, {0, -16d/6, 26d/6}, {0, 0, 19.125} }, op);

        Matrix<Double> expectedLower = new Matrix<>(new double[][] {{1, 0, 0}, {2d/3d, 1, 0}, {1d/3d, -23d/8d, 1}}, op);

        MatrixLUDecomposition instance = new MatrixLUDecomposition();

        LUMatrixResult<Double> result = instance.computeUpperLower(value);

        assertEquals(expectedUpper, result.getUpper());
        assertEquals(expectedLower, result.getLower());
    }


}
