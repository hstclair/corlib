package com.stclair.corlib.math.util;

public interface OperationStrategy<T> {

    T product(T multiplicand, T multiplier);

    T quotient(T dividend, T divisor);

    T difference(T minuend, T subtrahend);

    T sum(T auguend, T addend);

    T negate(T value);

    T invert(T value);

    boolean isZero(T value);

    boolean isOne(T value);

    T zero();

    T one();

    T[] array(int size);

    T[] arrayOf(T... values);

    T[][] matrix(int rows, int cols);

    default T[][] matrix(int rows) {
        return matrix(rows, 0);
    }

    T from(double dblValue);

    T[] from(double[] dblValues);

    T max(T a, T b);

    T min(T a, T b);

    T abs(T a);

    T floor(T a);

    T mod(T a, T b);

    boolean negative(T a);

    boolean positive(T a);

    boolean greaterThan(T a, T b);

    boolean lessThan(T a, T b);

    boolean greaterThanOrEqual(T a, T b);

    boolean lessThanOrEqual(T a, T b);

    T pow(T base, T exponent);

}
