package com.stclair.corlib.math.util;

import com.stclair.corlib.math.matrix.Value;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;


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
    public Apfloat[] array(int length) {
        return new Apfloat[length];
    }

    @Override
    public Apfloat[][] matrix(int rows, int columns) {
        if (columns == 0)
            return new Apfloat[rows][];

        return new Apfloat[rows][columns];
    }

    @Override
    public Apfloat from(double value) {
        if (value==0)
            return Apfloat.ZERO;
        if (value==1)
            return Apfloat.ONE;

        return new Apfloat(value, precision);
    }
}
