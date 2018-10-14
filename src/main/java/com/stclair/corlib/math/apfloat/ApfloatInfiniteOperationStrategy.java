package com.stclair.corlib.math.apfloat;

import com.stclair.corlib.math.util.OperationStrategy;
import org.apfloat.Apfloat;

import java.util.Arrays;
import java.util.stream.DoubleStream;


public class ApfloatInfiniteOperationStrategy implements OperationStrategy<ApfloatInfinite> {

    int precision;

    public ApfloatInfiniteOperationStrategy(int precision) {
        this.precision = precision;
    }

    public ApfloatInfiniteOperationStrategy() {
        this(1000);
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
    public ApfloatInfinite[][] matrix(int rows, int cols) {

        ApfloatInfinite[][] result = new ApfloatInfinite[rows][];

        Arrays.fill(result, array(cols));

        return result;
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

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return ApfloatInfinite.Undefined;

        if (a == b)
            return a;

        if (a == ApfloatInfinite.PositiveInfinity || b == ApfloatInfinite.PositiveInfinity)
            return ApfloatInfinite.PositiveInfinity;

        if (a == ApfloatInfinite.NegativeInfinity)
            return b;

        if (b == ApfloatInfinite.NegativeInfinity)
            return a;

        return a.compareTo(b) < 0 ? b : a;
    }

    @Override
    public ApfloatInfinite min(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return ApfloatInfinite.Undefined;

        if (a == b)
            return a;

        if (a == ApfloatInfinite.NegativeInfinity || b == ApfloatInfinite.NegativeInfinity)
            return ApfloatInfinite.NegativeInfinity;

        if (a == ApfloatInfinite.PositiveInfinity)
            return b;

        if (b == ApfloatInfinite.PositiveInfinity)
            return a;

        return a.compareTo(b) > 0 ? b : a;
    }

    @Override
    public boolean greaterThan(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return false;

        if (a == b)
            return false;

        if (isInfinite(a) || isInfinite(b))
            return a == ApfloatInfinite.PositiveInfinity || b == ApfloatInfinite.NegativeInfinity;

        return a.compareTo(b) > 0;
    }

    @Override
    public boolean lessThan(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return false;

        if (a == b)
            return false;

        if (isInfinite(a) || isInfinite(b))
            return a == ApfloatInfinite.NegativeInfinity || b == ApfloatInfinite.PositiveInfinity;

        return a.compareTo(b) < 0;
    }

    @Override
    public boolean greaterThanOrEqual(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return false;

        if (a == b)
            return true;

        if (isInfinite(a) || isInfinite(b))
            return a == ApfloatInfinite.PositiveInfinity || b == ApfloatInfinite.NegativeInfinity;

        return a.compareTo(b) >= 0;
    }

    @Override
    public boolean lessThanOrEqual(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == ApfloatInfinite.Undefined || b == ApfloatInfinite.Undefined)
            return false;

        if (a == b)
            return true;

        if (isInfinite(a) || isInfinite(b))
            return a == ApfloatInfinite.NegativeInfinity || b == ApfloatInfinite.PositiveInfinity;

        return a.compareTo(b) <= 0;
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

    public boolean isInfinite(ApfloatInfinite a) {
        return a.isInfinite;
    }
}
