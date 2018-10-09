package com.stclair.corlib.math.util;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class DoubleOperationStrategy implements OperationStrategy<Double> {

    @Override
    public Double product(Double multiplicand, Double multiplier) {
        return multiplicand * multiplier;
    }

    @Override
    public Double quotient(Double dividend, Double divisor) {
        return dividend / divisor;
    }

    @Override
    public Double difference(Double minuend, Double subtrahend) {
        return minuend - subtrahend;
    }

    @Override
    public Double sum(Double auguend, Double addend) {
        return auguend + addend;
    }

    @Override
    public Double negate(Double value) {
        return -value;
    }

    @Override
    public Double invert(Double value) {
        return 1d/value;
    }

    @Override
    public boolean isZero(Double value) {
        return value == 0d;
    }

    @Override
    public boolean isOne(Double value) {
        return value == 1;
    }

    @Override
    public Double zero() {
        return 0d;
    }

    @Override
    public Double one() {
        return 1d;
    }

    @Override
    public Double[] array(int size) {
        Double[] result = new Double[size];

        Arrays.fill(result, 0d);

        return result;
    }

    @Override
    public Double[] arrayOf(Double... values) {
        return Arrays.stream(values)
                .toArray(Double[]::new);
    }

    @Override
    public Double[][] matrix(int rows, int cols) {

        Double[][] result = new Double[rows][];

        Arrays.fill(result, array(cols));

        return result;
    }

    @Override
    public Double from(double dblValue) {
        return dblValue;
    }

    @Override
    public Double[] from(double[] dblValues) {

        return DoubleStream.of(dblValues)
                .boxed()
                .toArray(Double[]::new);
    }

    @Override
    public Double max(Double a, Double b) {
        return Math.max(a, b);
    }

    @Override
    public Double min(Double a, Double b) {
        return Math.min(a, b);
    }

    @Override
    public Double abs(Double a) {
        return Math.abs(a);
    }

    @Override
    public Double floor(Double a) {
        return Math.floor(a);
    }

    @Override
    public Double mod(Double a, Double b) {
        return a % b;
    }

    @Override
    public boolean negative(Double a) {
        return a < 0;
    }

    @Override
    public boolean positive(Double a) {
        return a > 0;
    }

    @Override
    public boolean greaterThan(Double a, Double b) {
        return a > b;
    }

    @Override
    public boolean lessThan(Double a, Double b) {
        return a < b;
    }

    @Override
    public boolean greaterThanOrEqual(Double a, Double b) {
        return a >= b;
    }

    @Override
    public boolean lessThanOrEqual(Double a, Double b) {
        return a <= b;
    }

    @Override
    public Double pow(Double base, Double exponent) {
        return Math.pow(base, exponent);
    }
}
