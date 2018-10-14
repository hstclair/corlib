package com.stclair.corlib.math.apfloat;

import com.stclair.corlib.math.polynomial.generic.Interval;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.ApfloatRuntimeException;
import org.apfloat.Apint;

import java.io.IOException;
import java.io.PushbackReader;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ApfloatInfinite {

    public static final ApfloatInfinite ZERO = new ApfloatInfinite(0);

    public static final ApfloatInfinite ONE = new ApfloatInfinite(1);

    public static final ApfloatInfinite Undefined = new ApfloatInfinite("Undefined", false);

    public static final ApfloatInfinite PositiveInfinity = new ApfloatInfinite("Positive Infinity", true);

    public static final ApfloatInfinite NegativeInfinity = new ApfloatInfinite("Negative Infinity", true);

    public final Apfloat value;

    public final boolean isInfinite;

    final String name;

    public ApfloatInfinite(Apfloat value) {
        this.value = value;
        this.name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(float value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(float value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(float value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(double value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(double value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(double value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(int value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(int value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(int value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(long value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(long value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(long value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(String value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(String value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(String value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(BigInteger value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(BigInteger value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(BigInteger value, long precision, int radix) {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(BigDecimal value) {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(BigDecimal value, long precision) {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(PushbackReader value) throws IOException {
        this.value = new Apfloat(value);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(PushbackReader value, long precision) throws IOException {
        this.value = new Apfloat(value, precision);
        name = null;
        isInfinite = false;
    }

    public ApfloatInfinite(PushbackReader value, long precision, int radix) throws IOException {
        this.value = new Apfloat(value, precision, radix);
        name = null;
        isInfinite = false;
    }


    ApfloatInfinite(String name, boolean isInfinite) {
        this.name = name;
        this.value = new Apfloat(0);
        this.isInfinite = isInfinite;
    }

    public ApfloatInfinite negate() throws ApfloatRuntimeException {

        if (this == Undefined)
            return this;

        if (this.isInfinite)
            return this == NegativeInfinity ? PositiveInfinity : NegativeInfinity;

        return new ApfloatInfinite(value.negate());
    }

    public ApfloatInfinite add(ApfloatInfinite x) throws ApfloatRuntimeException {

        if (this == Undefined || x == Undefined)
            return Undefined;

        if (this.isInfinite || x.isInfinite)
            return infinteSum(this, x);

        return new ApfloatInfinite(value.add(x.value));
    }

    public ApfloatInfinite infinteSum(ApfloatInfinite auguend, ApfloatInfinite addend) {

        if (auguend == addend)
            return auguend;

        if (auguend.isInfinite && addend.isInfinite)
            return Undefined;

        if (auguend.isInfinite)
            return auguend;

        return addend;
    }

    public ApfloatInfinite subtract(ApfloatInfinite x) throws ApfloatRuntimeException {

        if (this == Undefined || x == Undefined)
            return Undefined;

        if (this.isInfinite || x.isInfinite)
            return infiniteDifference(this, x);

        return new ApfloatInfinite(value.subtract(x.value));
    }

    public ApfloatInfinite infiniteDifference(ApfloatInfinite minuend, ApfloatInfinite subtrahend) {

        if (minuend.isInfinite && subtrahend.isInfinite)
            return Undefined;

        if (minuend.isInfinite)
            return minuend;

        return subtrahend.negate();
    }

    public ApfloatInfinite multiply(ApfloatInfinite x) throws ApfloatRuntimeException {

        if (this == ApfloatInfinite.Undefined || x == ApfloatInfinite.Undefined)
            return ApfloatInfinite.Undefined;

        if (this.isInfinite || x.isInfinite)
            return infiniteProduct(this, x);

        return new ApfloatInfinite(value.multiply(x.value));
    }

    public ApfloatInfinite infiniteProduct(ApfloatInfinite multiplicand, ApfloatInfinite multiplier) {

        if (multiplicand.equals(ApfloatInfinite.ZERO) || multiplier.equals(ApfloatInfinite.ZERO))
            return ApfloatInfinite.ZERO;

        if (multiplicand == multiplier || multiplicand.signum() == multiplier.signum())
            return ApfloatInfinite.PositiveInfinity;

        return ApfloatInfinite.NegativeInfinity;
    }

    public ApfloatInfinite divide(ApfloatInfinite x) throws ArithmeticException, ApfloatRuntimeException {

        if (this == ApfloatInfinite.Undefined || x == ApfloatInfinite.Undefined)
            return ApfloatInfinite.Undefined;

        if (this.isInfinite || x.isInfinite)
            return infiniteQuotient(this, x);

        if (x.equals(ApfloatInfinite.ZERO)) {
            return signum() == -1 ? NegativeInfinity : PositiveInfinity;
        }

        return new ApfloatInfinite(value.divide(x.value));
    }

    public ApfloatInfinite infiniteQuotient(ApfloatInfinite dividend, ApfloatInfinite divisor) {

        if (dividend.isInfinite && divisor.isInfinite)
            return ApfloatInfinite.Undefined;

        if (divisor.isInfinite)
            return ApfloatInfinite.ZERO;

        return divisor.signum() == -1 ? NegativeInfinity : PositiveInfinity;
    }

    public ApfloatInfinite invert() {


        if (this == Undefined)
            return Undefined;

        if (this == ZERO)
            return PositiveInfinity;

        if (this.isInfinite)
            return ZERO;

        return new ApfloatInfinite(Apfloat.ONE.divide(value));
    }

    public ApfloatInfinite mod(ApfloatInfinite x) throws ApfloatRuntimeException {

        if (this == Undefined || x == Undefined)
            return Undefined;

        if (this.isInfinite)
            return Undefined;

        if (x.isInfinite)
            return this;

        return new ApfloatInfinite(value.mod(x.value));
    }

    public ApfloatInfinite floor() throws ApfloatRuntimeException {

        if (this == Undefined)
            return Undefined;

        if (this.isInfinite)
            return this;

        return new ApfloatInfinite(value.floor());
    }

    public ApfloatInfinite ceil() throws ApfloatRuntimeException {

        if (this == Undefined)
            return Undefined;

        if (this.isInfinite)
            return this;

        return new ApfloatInfinite(value.ceil());
    }

    public double doubleValue() {

        if (this == ApfloatInfinite.Undefined)
            return Double.NaN;

        if (this == ApfloatInfinite.PositiveInfinity)
            return Double.POSITIVE_INFINITY;

        if (this == ApfloatInfinite.NegativeInfinity)
            return Double.NEGATIVE_INFINITY;

        return value.doubleValue();
    }

    public boolean greaterThan(ApfloatInfinite that) {

        if (this == Undefined || that == Undefined)
            return false;

        if (this.equals(that))
            return false;

        if (this.isInfinite || that.isInfinite)
            return this == PositiveInfinity || that == NegativeInfinity;

        return this.value.compareTo(that.value) > 0;
    }

    public boolean greaterThanOrEqual(ApfloatInfinite that) {

        if (this == Undefined || that == Undefined)
            return false;

        if (this.equals(that))
            return true;

        if (this.isInfinite || that.isInfinite)
            return this == PositiveInfinity || that == NegativeInfinity;

        return this.value.compareTo(that.value) > 0;
    }

    public boolean lessThan(ApfloatInfinite that) {

        if (this == Undefined || that == Undefined)
            return false;

        if (this.equals(that))
            return false;

        if (this.isInfinite || that.isInfinite)
            return this == NegativeInfinity || that == PositiveInfinity;

        return this.value.compareTo(that.value) < 0;
    }

    public boolean lessThanOrEqual(ApfloatInfinite that) {

        if (this == Undefined || that == Undefined)
            return false;

        if (this.equals(that))
            return true;

        if (this.isInfinite || that.isInfinite)
            return this == NegativeInfinity || that == PositiveInfinity;

        return this.value.compareTo(that.value) < 0;
    }

    public static ApfloatInfinite max(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == Undefined || b == Undefined)
            return Undefined;

        if (a.isInfinite || b.isInfinite) {
            if (a == PositiveInfinity || b == PositiveInfinity)
                return PositiveInfinity;

            return a == NegativeInfinity ? b : a;
        }

        return a.value.compareTo(b.value) >= 0 ? a : b;
    }

    public static ApfloatInfinite min(ApfloatInfinite a, ApfloatInfinite b) {

        if (a == Undefined || b == Undefined)
            return Undefined;

        if (a.isInfinite || b.isInfinite) {
            if (a == NegativeInfinity || b == NegativeInfinity)
                return NegativeInfinity;

            return a == PositiveInfinity ? b : a;
        }

        return a.value.compareTo(b.value) <= 0 ? a : b;
    }

    public int compareTo(ApfloatInfinite x) {

        return value.compareTo(x.value);
    }

    public int signum() {

        if (this == Undefined)
            return Integer.MAX_VALUE;

        if (this == PositiveInfinity)
            return 1;

        if (this == NegativeInfinity)
            return -1;

        return value.signum();
    }

    public ApfloatInfinite abs() {

        if (this == Undefined)
            return Undefined;

        if (this.isInfinite)
            return PositiveInfinity;

        return new ApfloatInfinite(ApfloatMath.abs(value));
    }

    public ApfloatInfinite pow(ApfloatInfinite exp) {

        if (this == Undefined || exp == Undefined)
            return Undefined;

        if (this.isInfinite || exp.isInfinite)
            return infinitePower(this, exp);

        return new ApfloatInfinite(ApfloatMath.pow(value, exp.value));
    }

    public ApfloatInfinite infinitePower(ApfloatInfinite base, ApfloatInfinite exponent) {

        if (exponent.isInfinite) {
            if (exponent == PositiveInfinity)
                return PositiveInfinity;

            return ZERO;
        }

        return exponent.mod(new ApfloatInfinite(2)).equals(ONE) ? base : base.abs();
    }

    @Override
    public String toString() {
        return name != null ? name : value.toString(true);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj == Undefined)
            return false;

        if (obj == this)
            return true;

        if (obj instanceof Apfloat)
            return obj.equals(value);

        if (! (obj instanceof ApfloatInfinite))
            return false;

        return value != null && value.equals(((ApfloatInfinite)obj).value);
    }

    @Override
    public int hashCode() {
        return value.hashCode() + (name != null ? name.hashCode() * 17 : 0);
    }

    public static ApfloatInfinite from(double value, long precision) {

        if (Double.isNaN(value))
            return Undefined;

        if (value == Double.POSITIVE_INFINITY)
            return PositiveInfinity;

        if (value == Double.NEGATIVE_INFINITY)
            return NegativeInfinity;

        if (value==0)
            return ZERO;
        if (value==1)
            return ONE;

        return new ApfloatInfinite(value, precision);
    }

}
