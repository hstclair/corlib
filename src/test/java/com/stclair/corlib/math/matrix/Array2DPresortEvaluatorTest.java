package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.array.Indexor;
import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

import java.time.chrono.MinguoDate;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.*;

public class Array2DPresortEvaluatorTest {

    double[][] sortedValues = new double[][] { { 1, 3, 7 }, { 7, 1, 3 }, { 3, 7, 1} };

    Array2DPresortEvaluator presorter = new Array2DPresortEvaluator();

    @Test
    public void presortDoesNotChangeProperlySortedMatrix() {

        double[][] values = sortedValues;

        Array2D<Double> matrix = new Array2DConcrete<>(new DoubleOperationStrategy(), 3, 3, indexor -> values[indexor.getRow()][indexor.getColumn()]);

        Array2D<Double> result = presorter.presort(matrix);

        double[][] actual = extract(result);

        assertArrayEquals(sortedValues, actual);
    }

    @Test
    public void presortTransposesFirstTwoRowsAndNegatesLastRow() {

        double[] finalRow = { -sortedValues[2][0], -sortedValues[2][1], -sortedValues[2][2] };

        double[][] values = new double[][] { sortedValues[1], sortedValues[0], finalRow };

        Array2D<Double> matrix = new Array2DConcrete<>(new DoubleOperationStrategy(), 3, 3, indexor -> values[indexor.getRow()][indexor.getColumn()]);

        Array2D<Double> result = presorter.presort(matrix);

        double[][] actual = extract(result);

        assertArrayEquals(sortedValues, actual);
    }

    @Test
    public void presortDoesStuff() {
        double[][] values = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        Array2D<Double> matrix = new Array2DConcrete<>(new DoubleOperationStrategy(), 3, 3, indexor -> values[indexor.getRow()][indexor.getColumn()]);

        Array2D<Double> result = presorter.presort(matrix);

        double[][] actual = extract(result);
    }

    @Test
    public void selectPermutation() {
    }

    @Test
    public void sequenceOf() {
    }

    @Test
    public void magnitudeOf() {
    }


    public double[][] extract(Array2D<Double> array) {
        double[][] result = new double[array.getHeight()][array.getWidth()];

        Consumer<Indexor<Double>> consumer = indexor -> Consumer(result, indexor);

        array.traverse(consumer);

        return result;
    }

    public void Consumer(double[][] result, Indexor<Double> indexor) {

        int row = indexor.getRow();

        int column = indexor.getColumn();

        double value = indexor.getValue();

        result[row][column] = value;
    }

}