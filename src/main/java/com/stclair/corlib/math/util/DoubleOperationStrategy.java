package com.stclair.corlib.math.util;

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
        return value == 0;
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
        return new Double[size];
    }

    @Override
    public Double[][] matrix(int rows, int cols) {

        if (cols == 0)
            return new Double[rows][];

        return new Double[rows][cols];
    }

    @Override
    public Double from(double dblValue) {
        return dblValue;
    }
}
