package com.stclair.corlib.math.matrix.determinant;

import org.junit.Test;

public interface TestDeterminantSolver<T> {
    @Test
    void test3x3Determinant();

    @Test
    void test4x4Determinant();

    @Test
    void test5x5Determinant();

    @Test
    void test6x6DeterminantA();

    @Test
    void test6x6DeterminantB();

    @Test
    void test7x7Determinant();

    @Test
    void test10x10DeterminantA();

    @Test
    void test10X10DeterminantB(T tolerance);
}
