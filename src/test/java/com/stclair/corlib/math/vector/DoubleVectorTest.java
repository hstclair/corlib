package com.stclair.corlib.math.vector;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class DoubleVectorTest {

    @Test
    public void dotProductIsZeroForPerpendicularVectors() {
        DoubleVector A = new DoubleVector(1, 0, 0);
        DoubleVector B = new DoubleVector(0, 1, 0);

        double result = A.dotProduct(B);

        assertEquals(0d, result);
    }

    @Test
    public void dotProductEqualsLengthOfSimpleProjection() {

        double expected = .5d;

        DoubleVector A = new DoubleVector(1, expected, 0);
        DoubleVector B = new DoubleVector(0, 1, 0);

        double result = A.dotProduct(B);

        assertEquals(expected, result);
    }

    @Test
    public void dotProductEqualsLengthOfNonorthogonalProjection() {

        double expected = .5d;

        DoubleVector A = new DoubleVector(Math.sqrt(expected), Math.sqrt(expected), 0);
        DoubleVector B = new DoubleVector(0, Math.sqrt(expected), Math.sqrt(expected));

        double result = A.dotProduct(B);

        assertEquals(expected, result, .0000000001);
    }

    @Test
    public void testVectorIsZero() {
        DoubleVector Z = new DoubleVector(0, 0, 0);

        assertTrue(Z.isZero());
    }

    @Test
    public void testNonZero() {
        DoubleVector I = new DoubleVector(1, 0, 0);

        assertFalse(I.isZero());
    }

    @Test
    public void testOrthonormal() {
        DoubleVector I = new DoubleVector(1, 0, 0);
        DoubleVector J = new DoubleVector(0, 1, 0);
        DoubleVector K = new DoubleVector(0, 0, 1);

        assertTrue(I.isOrthonormal());
        assertTrue(J.isOrthonormal());
        assertTrue(K.isOrthonormal());
    }

    @Test
    public void testNonOrthonormal() {
        DoubleVector Z = new DoubleVector(0, 0, 0);
        DoubleVector B = new DoubleVector(1, 1, 1);
        DoubleVector W = new DoubleVector(1/Math.sqrt(3), 1/Math.sqrt(3), 1/Math.sqrt(3));

        assertFalse(Z.isOrthonormal());
        assertFalse(B.isOrthonormal());
        assertFalse(W.isOrthonormal());
    }

    @Test
    public void testIsNormal() {
        DoubleVector I = new DoubleVector(1, 0, 0);
        DoubleVector J = new DoubleVector(0, 1, 0);
        DoubleVector K = new DoubleVector(0, 0, 1);
        DoubleVector W = new DoubleVector(1/Math.sqrt(3), 1/Math.sqrt(3), 1/Math.sqrt(3));

        assertTrue(I.isNormal());
        assertTrue(J.isNormal());
        assertTrue(K.isNormal());
        assertTrue(W.isNormal());
    }

    @Test
    public void testNonNormal() {

        DoubleVector Z = new DoubleVector(0, 0, 0);
        DoubleVector B = new DoubleVector(1, 1, 1);

        assertFalse(Z.isNormal());
        assertFalse(B.isNormal());
    }

    @Test
    public void testNormalize() {

        DoubleVector B = new DoubleVector(1, 1, 1);

        DoubleVector Bnorm = B.normalize();

        assertEquals(1d, Bnorm.magnitude());
    }

    @Test
    public void testVectorEquals() {

        double[] values = new double[] { 13, 12, .5d };
        DoubleVector M = new DoubleVector(values);
        DoubleVector SameAsM = new DoubleVector(values);

        assertNotSame(M, SameAsM);
        assertEquals(M, SameAsM);
    }

    @Test
    public void testCrossProductIsPerpendicular() {

        DoubleVector I = new DoubleVector(1, 0, 0);
        DoubleVector J = new DoubleVector(0, 1, 0);
        DoubleVector K = new DoubleVector(0, 0, 1);

        DoubleVector IcrossJ = I.crossProduct(J);

        assertEquals(K, IcrossJ);
    }

    @Test
    public void testCrossProductMagnitudeIsProductOfMagnitudesTimesSinTheta() {

        DoubleVector M = new DoubleVector( 7, 13, 2);
        DoubleVector N = new DoubleVector(8, 1, 57);

        double magM = M.magnitude();
        double magN = N.magnitude();

        double cosMN = M.dotProduct(N) / (magM * magN);

        double sinMN = Math.sqrt(1 - cosMN*cosMN);

        DoubleVector McrossN = M.crossProduct(N);

        assertEquals(sinMN * magM * magN, McrossN.magnitude(), .0000000001);
    }

    @Test
    public void testCrossProductIsZeroForParallelVectors() {

        DoubleVector M = new DoubleVector(1, 1, 1);
        DoubleVector N = new DoubleVector(10, 10, 10);

        assertTrue(M.crossProduct(N).isZero());
    }

    @Test
    public void testVectorProductWithScalar() {

        double x = 3;
        double y = 14;
        double z = -3;

        double c = 4;

        DoubleVector M = new DoubleVector(x, y, z);

        DoubleVector Expected = new DoubleVector(x*c, y*c, z*c);

        DoubleVector Product = M.product(c);

        assertEquals(Expected, Product);
    }

    @Test
    public void testVectorSum() {
        double x1 = 3;
        double y1 = 14;
        double z1 = -3;

        DoubleVector M = new DoubleVector(x1, y1, z1);

        double x2 = 3;
        double y2 = 14;
        double z2 = -3;

        DoubleVector N = new DoubleVector(x2, y2, z2);

        DoubleVector Expected = new DoubleVector(x1 + x2, y1+y2, z1+z2);

        DoubleVector Actual = M.sum(N);

        assertEquals(Expected, Actual);
    }
}
