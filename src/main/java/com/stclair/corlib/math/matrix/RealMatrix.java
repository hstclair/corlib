package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.DoubleOperationStrategy;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by hstclair on 4/17/17.
 */
public class RealMatrix {

    final double[][] members;

    final int rows;
    final int columns;

    public RealMatrix(double[][] members) {
        this.members = members;
        rows = members.length;
        columns = columns(members);
    }

    private int columns(double[][] members) {
        int columns = 0;

        for (double[] member : members) {
            columns = Math.max(columns, member.length);
        }

        return columns;
    }

    private double[][] clone(double[][] original) {
        double[][] clone=new double[original.length][];

        int columns = columns(original);

        for (int row=0; row < original.length; row++) {
            clone[row] = new double[columns];
            System.arraycopy(original[row], 0,clone[row],0, columns);
        }

        return clone;
    }

    private Double[][] cloneDouble(double[][] original) {

        return Arrays.stream(original)
                .map(doubles ->
                        Arrays.stream(doubles)
                                .boxed()
                            .toArray(Double[]::new))
                    .toArray(Double[][]::new);
    }

    private double subtract(double[] subtrahend, double[] minuend, int column) {
        double subtrahendCoefficient = minuend[column];
        double minuendCoefficient = subtrahend[column];

        for (int index = 0; index < subtrahend.length; index++) {
            subtrahend[index] = subtrahendCoefficient * subtrahend[index] - minuendCoefficient * minuend[index];
        }

        return subtrahendCoefficient;
    }

    public double determinant() {
        MatrixLUDecomposition decomposer = new MatrixLUDecomposition();

        LUMatrixResult<Double> result = decomposer.computeUpperLower(cloneDouble(this.members), new DoubleOperationStrategy(), true);

        return result.determinant();
    }
}
