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

    T[][] matrix(int rows, int cols);

    default T[][] matrix(int rows) {
        return matrix(rows, 0);
    }

    T from(double dblValue);
}
