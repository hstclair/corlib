package com.stclair.corlib.math.apfloat;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.util.OperationStrategy;
import org.apfloat.Apfloat;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Formatter;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;


public class ApfloatInfiniteOperationStrategy implements OperationStrategy<ApfloatInfinite> {

    int precision;

    public ApfloatInfiniteOperationStrategy(int precision) {
        this.precision = precision;
    }

    public ApfloatInfiniteOperationStrategy() {
        this(128);
    }

    @Override
    public ApfloatInfinite product(ApfloatInfinite multiplicand, ApfloatInfinite multiplier) {
        return multiplicand.multiply(multiplier);
    }

    @Override
    public ApfloatInfinite quotient(ApfloatInfinite dividend, ApfloatInfinite divisor) {
        return dividend.divide(divisor);
    }

    @Override
    public ApfloatInfinite difference(ApfloatInfinite minuend, ApfloatInfinite subtrahend) {
        return minuend.subtract(subtrahend);
    }

    @Override
    public ApfloatInfinite sum(ApfloatInfinite auguend, ApfloatInfinite addend) {
        return auguend.add(addend);
    }

    @Override
    public ApfloatInfinite negate(ApfloatInfinite value) {
        return value.negate();
    }

    @Override
    public ApfloatInfinite invert(ApfloatInfinite value) {
        return value.invert();
    }

    @Override
    public boolean isZero(ApfloatInfinite value) {
        return value.equals(ApfloatInfinite.ZERO);
    }

    @Override
    public boolean isOne(ApfloatInfinite value) {
        return value.equals(ApfloatInfinite.ONE);
    }

    @Override
    public ApfloatInfinite zero() {
        return ApfloatInfinite.ZERO;
    }

    @Override
    public ApfloatInfinite one() {
        return ApfloatInfinite.ONE;
    }

    @Override
    public ApfloatInfinite[] array(int size) {
        ApfloatInfinite[] result = new ApfloatInfinite[size];

        Arrays.fill(result, ApfloatInfinite.ZERO);

        return result;
    }

    @Override
    public Array2D<ApfloatInfinite> matrix(int rows, int cols) {

        return new Array2DConcrete<>(cols, rows, indexor -> ApfloatInfinite.ZERO);
    }

    @Override
    public double value(ApfloatInfinite a) {
        return a.doubleValue();
    }

    @Override
    public ApfloatInfinite from(double value) {
        return ApfloatInfinite.from(value, precision);
    }

    @Override
    public ApfloatInfinite[] from(double[] dblValues) {
        return DoubleStream.of(dblValues)
                .mapToObj(this::from)
                .toArray(ApfloatInfinite[]::new);
    }

    @Override
    public ApfloatInfinite abs(ApfloatInfinite a) {
        return a.abs();
    }

    @Override
    public ApfloatInfinite floor(ApfloatInfinite a) {
        return a.floor();
    }

    @Override
    public ApfloatInfinite mod(ApfloatInfinite a, ApfloatInfinite b) {
        return a.mod(b);
    }

    @Override
    public boolean isNegative(ApfloatInfinite a) {
        return a.signum() == -1;
    }

    @Override
    public boolean isPositive(ApfloatInfinite a) {
        return a.signum() == 1;
    }

    @Override
    public ApfloatInfinite max(ApfloatInfinite a, ApfloatInfinite b) {
        return ApfloatInfinite.max(a, b);
    }

    @Override
    public ApfloatInfinite min(ApfloatInfinite a, ApfloatInfinite b) {
        return ApfloatInfinite.min(a, b);
    }

    @Override
    public boolean greaterThan(ApfloatInfinite a, ApfloatInfinite b) {
        return a.greaterThan(b);
    }

    @Override
    public boolean lessThan(ApfloatInfinite a, ApfloatInfinite b) {
        return a.lessThan(b);
    }

    @Override
    public boolean greaterThanOrEqual(ApfloatInfinite a, ApfloatInfinite b) {
        return a.greaterThanOrEqual(b);
    }

    @Override
    public boolean lessThanOrEqual(ApfloatInfinite a, ApfloatInfinite b) {
        return a.lessThanOrEqual(b);
    }

    @Override
    public ApfloatInfinite pow(ApfloatInfinite base, ApfloatInfinite exponent) {
        return base.pow(exponent);
    }

    @Override
    public boolean isPositiveInfinity(ApfloatInfinite a) {
        return a == ApfloatInfinite.PositiveInfinity;
    }

    @Override
    public ApfloatInfinite positiveInfinity() {
        return ApfloatInfinite.PositiveInfinity;
    }

    @Override
    public boolean isNegativeInfinity(ApfloatInfinite a) {
        return a == ApfloatInfinite.NegativeInfinity;
    }

    @Override
    public ApfloatInfinite negativeInfinity() {
        return ApfloatInfinite.NegativeInfinity;
    }

    @Override
    public String toIntegerString(ApfloatInfinite value) {
        return String.format("%#s", value.value);
    }

    @Override
    public String toDecimalString(ApfloatInfinite value) {
        return String.format("%#s", value.value);
    }
}
