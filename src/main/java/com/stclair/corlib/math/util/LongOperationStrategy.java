package com.stclair.corlib.math.util;

import com.stclair.corlib.math.array.Array2D;

public class LongOperationStrategy implements OperationStrategy<Long> {
    @Override
    public Long product(Long multiplicand, Long multiplier) {
        return multiplicand * multiplier;
    }

    @Override
    public Long quotient(Long dividend, Long divisor) {
        return dividend / divisor;
    }

    @Override
    public Long difference(Long minuend, Long subtrahend) {
        return minuend - subtrahend;
    }

    @Override
    public Long sum(Long auguend, Long addend) {
        return auguend + addend;
    }

    @Override
    public Long negate(Long value) {
        return -value;
    }

    @Override
    public Long invert(Long value) {

        if (value == 0L)
            throw new ArithmeticException("Divide by zero");

        return 0L;
    }

    @Override
    public boolean isZero(Long value) {
        return value == 0L;
    }

    @Override
    public boolean isOne(Long value) {
        return value == 1;
    }

    @Override
    public Long zero() {
        return 0L;
    }

    @Override
    public Long one() {
        return 1L;
    }

    @Override
    public Long[] array(int size) {
        return new Long[size];
    }

    @Override
    public Class<Long> getElementClass() {
        return Long.class;
    }

    @Override
    public Array2D<Long> matrix(int rows, int cols) {
        return null;
    }

    @Override
    public Long from(double dblValue) {
        return (long) dblValue;
    }

    @Override
    public Long[] from(double[] dblValues) {
        Long[] longs =  new Long[dblValues.length];

        for (int index = 0; index < longs.length; index++)
            longs[index] = (long) dblValues[index];

        return longs;
    }

    @Override
    public Long max(Long a, Long b) {
        return Math.max(a, b);
    }

    @Override
    public Long min(Long a, Long b) {
        return Math.min(a, b);
    }

    @Override
    public Long abs(Long a) {
        return Math.abs(a);
    }

    @Override
    public Long floor(Long a) {
        return a;
    }

    @Override
    public Long mod(Long a, Long b) {
        return a % b;
    }

    @Override
    public boolean isNegative(Long a) {
        return a < 0L;
    }

    @Override
    public boolean isPositive(Long a) {
        return a > 0L;
    }

    @Override
    public boolean greaterThan(Long a, Long b) {
        return a > b;
    }

    @Override
    public boolean lessThan(Long a, Long b) {
        return a < b;
    }

    @Override
    public boolean greaterThanOrEqual(Long a, Long b) {
        return a >= b;
    }

    @Override
    public boolean lessThanOrEqual(Long a, Long b) {
        return a <= b;
    }

    @Override
    public Long pow(Long base, Long exponent) {
        return (long) Math.pow(base, exponent);
    }

    @Override
    public double value(Long a) {
        return a;
    }

    @Override
    public boolean isPositiveInfinity(Long a) {
        return false;
    }

    @Override
    public Long positiveInfinity() {
        throw new UnsupportedOperationException("No infinite value supported for Long");
    }

    @Override
    public boolean isNegativeInfinity(Long a) {
        return false;
    }

    @Override
    public Long negativeInfinity() {
        throw new UnsupportedOperationException("No infinite value supported for Long");
    }

    @Override
    public String toIntegerString(Long value) {
        return value.toString();
    }

    @Override
    public String toDecimalString(Long value) {
        return value.toString();
    }

    @Override
    public long significantBits(Long value) {
        return Long.highestOneBit(value) - Long.numberOfTrailingZeros(value);
    }
}
