package com.stclair.corlib.math.matrix;

import com.stclair.corlib.math.util.*;
import com.stclair.corlib.permutation.HalsHeapsAlgorithmPermutation;
import com.stclair.corlib.permutation.Permutations;
import com.stclair.corlib.permutation.ReversingPermutations;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by hstclair on 4/21/17.
 */
public class TestDeterminantLUSolver {

    @Test
    public void test3x3DeterminantLUDouble() {
        Double expected = -306d;

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Double result = instance.solve(new Matrix<>(members, new DoubleOperationStrategy()));

        assertEquals(expected, result);
    }

    @Test
    public void test3x3DeterminantDouble() {
        Double expected = -306d;

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        Double result = new Matrix<>(members, new DoubleOperationStrategy()).determinant();

        assertEquals(expected, result);
    }

    @Test
    public void test3x3DeterminantLU() {
        Apfloat expected = new Apfloat(-306);

        double[][] members = new double[][] { {6, 1, 1}, {4, -2, 5}, {2, 8, 7} };

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test4x4DeterminantLU() {
        Apfloat expected = new Apfloat(-418);

        double[][] members = new double[][] { {3, 2, -1, 4}, {2, 1, 5, 7}, {0, 5, 2, -6}, {-1, 2, 1, 0} };

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test4x4Determinant() {

        Apfloat expected = new Apfloat(-418);

        double[][] members = new double[][] { {3, 2, -1, 4}, {2, 1, 5, 7}, {0, 5, 2, -6}, {-1, 2, 1, 0} };

        Apfloat result = new Matrix<>(members, new ApfloatOperationStrategy()).determinant();

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void testPermutationsOf4() {

        Integer[] sequence = {1, 2, 3, 4};

        Permutations instance = new HalsHeapsAlgorithmPermutation();

        List<Integer[]> permutations = instance.of(sequence);

        System.out.println();
    }


    @Test
    public void test5x5DeterminantLU() {
        double[][] members = new double[][] {
                { -5, 36, 38, 6, 11 },
                { -33, 3, 24, 22, -9 },
                { 36, -26, -33, -43, -47 },
                { 0, 27, -28, -26, 23 },
                { 7, -39, 33, 3, -43 },
        };

        Apfloat expected = new Apfloat(-61089854);

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test6x6DeterminantDoubleLU() {
        double[][] members = new double[][] {
                { -3, 0, -5, -4, -1, -5 },
                { -4, 2, -2, 3, 2, -3 },
                { 0, 4, 0, -5, -2, -2 },
                { -4, -4, 0, -3, 1, -3 },
                { 3, 4, -2, -1, 4, -2 },
                { -1, -5, -5, 3, -3, -5 },
        };

        Double expected = 36027d;

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Double result = instance.solve(new Matrix<>(members, new DoubleOperationStrategy()));

        assertEquals(expected, result);
    }

    @Test
    public void test6x6DeterminantLU() {
        double[][] members = new double[][] {
                { 3, 36, 16, 2, 38, -41 },
                { -34, -39, 1, -44, 20, 14 },
                { -9, 28, -49, 2, 26, 29 },
                { -21, -49, -42, -36, 2, -25 },
                { -29, 25, -18, 28, -17, -13 },
                { -6, -47, 19, 24, 6, 19 },
        };

        Apfloat expected = new Apfloat(40433588418l);

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test7x7DeterminantDoubleLU() {
        double[][] members = new double[][] {
                {0,-5,-4,2,-2,-5,-5},
                {-4,-1,0,-1,-4,0,-3},
                {2,-3,2,-3,4,0,-5},
                {3,0,-4,-2,-3,-5,-5},
                {-2,-2,-2,2,-4,3,3},
                {4,0,0,0,2,-5,-3},
                {-2,-5,0,-3,-2,-5,-2},
        };

        Double expected = 90344d;

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Double result = instance.solve(new Matrix<>(members, new DoubleOperationStrategy()));

        assertEquals(expected, result);
    }

    @Test
    public void test7x7DeterminantLU() {
        double[][] members = new double[][] {
                {0,-5,-4,2,-2,-5,-5},
                {-4,-1,0,-1,-4,0,-3},
                {2,-3,2,-3,4,0,-5},
                {3,0,-4,-2,-3,-5,-5},
                {-2,-2,-2,2,-4,3,3},
                {4,0,0,0,2,-5,-3},
                {-2,-5,0,-3,-2,-5,-2},
        };

        Apfloat expected = new Apfloat(90344);

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test10x10DeterminantLU() {
        double[][] members = new double[][] {
                {1,0,0,-1,-1,1,-1,0,3,-3},
                {2,3,-4,-2,2,3,-2,-4,0,4},
                {-1,-3,3,0,0,-3,4,4,-3,-2},
                {3,-3,4,3,-1,0,-2,-1,-4,1},
                {-5,-5,1,4,0,2,-2,-2,0,4},
                {-5,-5,-4,-4,1,-4,-4,-5,3,2},
                {-1,-5,-4,2,-2,-4,-4,0,0,-5},
                {1,3,-3,0,0,-5,1,-4,1,0},
                {-2,0,-5,4,3,3,-5,1,4,-2},
                {-2,-2,-2,-5,0,4,-3,-3,3,-5},
        };
        Apfloat expected = new Apfloat(-39656706);

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Apfloat result = instance.solve(new Matrix<>(members, new ApfloatOperationStrategy()));

        result = ApfloatMath.round(result, result.precision()-1, RoundingMode.HALF_EVEN);

        assertEquals(expected, result);
    }

    @Test
    public void test10x10DeterminantLUDouble() {
        double[][] members = new double[][] {
                {1,0,0,-1,-1,1,-1,0,3,-3},
                {2,3,-4,-2,2,3,-2,-4,0,4},
                {-1,-3,3,0,0,-3,4,4,-3,-2},
                {3,-3,4,3,-1,0,-2,-1,-4,1},
                {-5,-5,1,4,0,2,-2,-2,0,4},
                {-5,-5,-4,-4,1,-4,-4,-5,3,2},
                {-1,-5,-4,2,-2,-4,-4,0,0,-5},
                {1,3,-3,0,0,-5,1,-4,1,0},
                {-2,0,-5,4,3,3,-5,1,4,-2},
                {-2,-2,-2,-5,0,4,-3,-3,3,-5},
        };
        Double expected = -39656706d;

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Double result = instance.solve(new Matrix<>(members, new DoubleOperationStrategy()));

        assertEquals(expected, result);
    }

    @Test
    public void testAnother10X10DeterminantLU() {
        double[][] members = new double[][] {
                { 21, 45, 5, 17, 60, 47, 52, 87, 14, 36 },
                { 81, 20, 84, 48, 66, 10, 86, 74, 5, 0 },
                { 72, 99, 47, 96, 20, 64, 94, 22, 5, 91 },
                { 0, 58, 96, 89, 27, 2, 91, 61, 4, 63 },
                { 24, 25, 53, 83, 5, 45, 18, 42, 20, 7 },
                { 21, 6, 52, 97, 47, 80, 5, 50, 23, 40 },
                { 29, 44, 90, 96, 36, 38, 44, 16, 26, 72 },
                { 49, 58, 99, 75, 96, 99, 10, 20, 53, 28 },
                { 77, 3, 17, 60, 60, 14, 60, 15, 71, 16 },
                { 61, 82, 45, 16, 29, 39, 65, 75, 98, 22 }
        };
        double expected = -2583406973511121400l;

        DeterminantLUSolver instance = new DeterminantLUSolver();

        Double result = instance.solve(new Matrix<>(members, new DoubleOperationStrategy()));

        assertEquals(expected, result);
    }

}
