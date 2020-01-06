package com.stclair.corlib.math.util;

import com.stclair.corlib.math.array.Array2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public interface OperationStrategy<T> {

    T product(T multiplicand, T multiplier);

    T quotient(T dividend, T divisor);

    T difference(T minuend, T subtrahend);

    T sum(T auguend, T addend);

    T increment(T value);

    T decrement(T value);

    T negate(T value);

    T invert(T value);

    boolean isZero(T value);

    boolean isOne(T value);

    T zero();

    T one();

    T[] array(int size);

    Class<T> getElementClass();

//    T[] arrayOf(T... values);

    Array2D<T> matrix(int rows, int cols);

    default Array2D<T> matrix(int rows) {
        return matrix(rows, 0);
    }

    T from(double dblValue);

    T from(long longValue);

    T from(String stringValue);

    T[] from(double[] dblValues);

    T max(T a, T b);

    T min(T a, T b);

    T abs(T a);

    T floor(T a);

    T mod(T a, T b);

    boolean isNegative(T a);

    boolean isPositive(T a);

    boolean isEqual(T a, T b);

    boolean greaterThan(T a, T b);

    boolean lessThan(T a, T b);

    boolean greaterThanOrEqual(T a, T b);

    boolean lessThanOrEqual(T a, T b);

    T pow(T base, T exponent);

    double value(T a);

    boolean isPositiveInfinity(T a);

    T positiveInfinity();

    boolean isNegativeInfinity(T a);

    T negativeInfinity();

    String toIntegerString(T value);

    String toDecimalString(T value);

    /**
     * returns the number of significant bits in the provided value
     * (analogous to significant digits)
     * @param value the value to be examined
     * @return a natural number (positive) value indicating the
     * number of bits required to represent the <i>mantissa</i>
     * component of the value supplied
     */
    long significantBits(T value);

    default String toBaseF(T value) {

        final String digits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        if (isZero(value))
            return "0";

        T currentBase = one();

        T currentFactor = one();

        Stack<T> bases = new Stack<>();

        while (lessThanOrEqual(currentBase, value)) {
            bases.push(currentBase);

            currentFactor = sum(currentFactor, one());

            currentBase = product(currentBase, currentFactor);
        }

        StringBuilder result = new StringBuilder();

        T workingValue = value;

        while (! bases.empty()) {

            currentBase = bases.pop();

            T currentValue = workingValue;

            workingValue = mod(currentValue, currentBase);

            currentValue = difference(currentValue, workingValue);

            T digit = floor(quotient(currentValue, currentBase));

            result.append(digits.charAt((int) value(digit)));
        }

        return result.toString();
    }

    default T[] sequenceOf(long count) {

        T counter = zero();

        T[] result = array((int) count);

        for (int index = 0; index < count; index++) {
            result[index] = counter;
            counter = this.sum(counter, one());
        }

        return result;
    }
}
