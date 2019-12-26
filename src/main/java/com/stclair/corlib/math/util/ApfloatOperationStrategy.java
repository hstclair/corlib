package com.stclair.corlib.math.util;

import com.stclair.corlib.math.array.Array2D;
import com.stclair.corlib.math.array.Array2DConcrete;
import com.stclair.corlib.math.matrix.Value;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Formatter;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;


public class ApfloatOperationStrategy implements OperationStrategy<Apfloat> {

    int precision;

    public ApfloatOperationStrategy(int precision) {
        this.precision = precision;
    }

    public ApfloatOperationStrategy() {
        this(1000);
    }

    @Override
    public Apfloat product(Apfloat multiplicand, Apfloat multiplier) {
        return multiplicand.multiply(multiplier);
    }

    @Override
    public Apfloat quotient(Apfloat dividend, Apfloat divisor) {
        return dividend.divide(divisor);
    }

    @Override
    public Apfloat difference(Apfloat minuend, Apfloat subtrahend) {
        return minuend.subtract(subtrahend);
    }

    @Override
    public Apfloat sum(Apfloat auguend, Apfloat addend) {
        return auguend.add(addend);
    }

    @Override
    public Apfloat negate(Apfloat value) {
        return value.negate();
    }

    @Override
    public Apfloat invert(Apfloat value) {
        return Apfloat.ONE.divide(value);
    }

    @Override
    public boolean isZero(Apfloat value) {
        return value.equals(Apfloat.ZERO);
    }

    @Override
    public boolean isOne(Apfloat value) {
        return value.equals(Apfloat.ONE);
    }

    @Override
    public Apfloat zero() {
        return Apfloat.ZERO;
    }

    @Override
    public Apfloat one() {
        return Apfloat.ONE;
    }

    @Override
    public Apfloat[] array(int size) {
        Apfloat[] result = new Apfloat[size];

        Arrays.fill(result, Apfloat.ZERO);

        return result;
    }

    @Override
    public Array2D<Apfloat> matrix(int rows, int cols) {

        return new Array2DConcrete<>(this, cols, rows, indexor -> Apfloat.ZERO);
    }

    @Override
    public Class<Apfloat> getElementClass() {
        return Apfloat.class;
    }

    @Override
    public double value(Apfloat a) {
        return a.doubleValue();
    }

    @Override
    public Apfloat from(double value) {
        if (value==0)
            return Apfloat.ZERO;
        if (value==1)
            return Apfloat.ONE;

        return new Apfloat(value, precision);
    }

    @Override
    public Apfloat[] from(double[] dblValues) {
        return DoubleStream.of(dblValues)
                .mapToObj(Apfloat::new)
                .toArray(Apfloat[]::new);
    }

    @Override
    public Apfloat max(Apfloat a, Apfloat b) {
        return a.compareTo(b) < 0 ? b : a;
    }

    @Override
    public Apfloat min(Apfloat a, Apfloat b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    @Override
    public Apfloat abs(Apfloat a) {
        return a.signum() >= 0 ? a : a.negate();
    }

    @Override
    public Apfloat floor(Apfloat a) {
        return a.floor();
    }

    @Override
    public Apfloat mod(Apfloat a, Apfloat b) {
        return a.mod(b);
    }

    @Override
    public boolean isNegative(Apfloat a) {
        return a.signum() == -1;
    }

    @Override
    public boolean isPositive(Apfloat a) {
        return a.signum() == 1;
    }

    @Override
    public boolean greaterThan(Apfloat a, Apfloat b) {
        return a.compareTo(b) > 0;
    }

    @Override
    public boolean lessThan(Apfloat a, Apfloat b) {
        return a.compareTo(b) < 0;
    }

    @Override
    public boolean greaterThanOrEqual(Apfloat a, Apfloat b) {
        return a.compareTo(b) >= 0;
    }

    @Override
    public boolean lessThanOrEqual(Apfloat a, Apfloat b) {
        return a.compareTo(b) <= 0;
    }

    @Override
    public Apfloat pow(Apfloat base, Apfloat exponent) {
        return ApfloatMath.pow(base, exponent);
    }

    @Override
    public boolean isPositiveInfinity(Apfloat a) {
        return false;
    }

    @Override
    public Apfloat positiveInfinity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isNegativeInfinity(Apfloat a) {
        return false;
    }

    @Override
    public Apfloat negativeInfinity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toIntegerString(Apfloat value) {
        return String.format("%#s", value);
    }

    @Override
    public String toDecimalString(Apfloat value) {
        return String.format("%#s", value);
    }

    @Override
    public long significantBits(Apfloat value) {
        return value.toRadix(2).size();
    }
}
