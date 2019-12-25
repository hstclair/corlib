package com.stclair.corlib.math.util;

import java.text.DecimalFormat;
import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

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
    public Array2D<Double> matrix(int rows, int cols) {

        return new Array2DConcrete<Double>(cols, rows, indexor -> 0d);
    }

    @Override
    public double value(Double a) {
        return a;
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
    public boolean isNegative(Double a) {
        return a < 0;
    }

    @Override
    public boolean isPositive(Double a) {
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

    @Override
    public boolean isPositiveInfinity(Double a) {
        return a == Double.POSITIVE_INFINITY;
    }

    @Override
    public Double positiveInfinity() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean isNegativeInfinity(Double a) {
        return a == Double.NEGATIVE_INFINITY;
    }

    @Override
    public Double negativeInfinity() {
        return Double.NEGATIVE_INFINITY;
    }

    @Override
    public long significantBits(Double value) {
        long bits = Double.doubleToLongBits(value);

        long mantissa = (bits & 0x000fffffffffffffL) | 0x0010000000000000L;

        return 64 - Long.numberOfLeadingZeros(mantissa) - Long.numberOfTrailingZeros(mantissa);
    }

    @Override
    public String toIntegerString(Double value) {

        DecimalFormat integerFormat = new DecimalFormat();
        integerFormat.setMaximumFractionDigits(0);

        return integerFormat.format(value);
    }

    @Override
    public String toDecimalString(Double value) {

        DecimalFormat floatFormat = new DecimalFormat();
        floatFormat.setMaximumFractionDigits(17);
        floatFormat.setMinimumFractionDigits(0);

        return floatFormat.format(value);
    }
}
