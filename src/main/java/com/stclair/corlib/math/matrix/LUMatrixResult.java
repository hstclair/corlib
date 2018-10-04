package com.stclair.corlib.math.matrix;

public class LUMatrixResult<T> {

    Matrix<T> lower;

    Matrix<T> upper;

    T determinant;

    public LUMatrixResult(Matrix<T> lower, Matrix<T> upper, T determinant) {
        this.lower = lower;
        this.upper = upper;
        this.determinant = determinant;
    }

    public Matrix<T> getLower() {
        return lower;
    }

    public Matrix<T> getUpper() {
        return upper;
    }

    public T determinant() {
        return this.determinant;
    }
}
