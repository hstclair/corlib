package com.stclair.corlib.math.matrix.determinant;

import com.stclair.corlib.math.matrix.Matrix;
import com.stclair.corlib.math.util.ApfloatOperationStrategy;
import com.stclair.corlib.math.util.DoubleOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;
import org.apfloat.Apfloat;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by hstclair on 4/17/17.
 */
public class TestLUMatrixDeterminantSolver {

    TestDeterminantSolver<Apfloat> apfloatTests = new TestDeterminantSolverImpl<>(new ApfloatOperationStrategy(), new LUDeterminantSolver());

    TestDeterminantSolver<Double> doubleTests = new TestDeterminantSolverImpl<>(new DoubleOperationStrategy(), new LUDeterminantSolver());

    @Test
    public void test3x3DeterminantDouble() {

        doubleTests.test3x3Determinant();
    }

    @Test
    public void test3x3DeterminantApfloat() {

        apfloatTests.test3x3Determinant();
    }

    @Test
    public void test4x4DeterminantDouble() {

        doubleTests.test4x4Determinant();
    }

    @Test
    public void test4x4DeterminantApfloat() {

        apfloatTests.test4x4Determinant();
    }

    @Test
    public void test5x5DeterminantDouble() {

        doubleTests.test5x5Determinant();
    }

    @Test
    public void test5x5DeterminantApfloat() {

        apfloatTests.test5x5Determinant();
    }

    @Test
    public void test6x6DeterminantADouble() {

        doubleTests.test6x6DeterminantA();
    }

    @Test
    public void test6x6DeterminantAApfloat() {

        apfloatTests.test6x6DeterminantA();
    }

    @Test
    public void test6x6DeterminantBDouble() {

        doubleTests.test6x6DeterminantB();
    }

    @Test
    public void test6x6DeterminantBApfloat() {

        apfloatTests.test6x6DeterminantB();
    }

    @Test
    public void test7x7DeterminantDouble() {

        doubleTests.test7x7Determinant();
    }

    @Test
    public void test7x7DeterminantApfloat() {

        apfloatTests.test7x7Determinant();
    }

    @Test
    public void test10x10DeterminantADouble() {

        doubleTests.test10x10DeterminantA();
    }

    @Test
    public void test10x10DeterminantAApfloat() {

        apfloatTests.test10x10DeterminantA();
    }

    @Test
    public void test10X10DeterminantBDouble() {

        doubleTests.test10X10DeterminantB(45000d);
    }

    @Test
    public void test10X10DeterminantBApfloat() {

        // wicked fast
        // dead-on, balls accurate
        apfloatTests.test10X10DeterminantB(null);
    }
}
