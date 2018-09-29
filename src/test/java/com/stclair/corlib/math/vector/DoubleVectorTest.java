package com.stclair.corlib.math.vector;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DoubleVectorTest {

    @Test
    public void dotProductIsZeroForPerpendicularVectors() {
        DoubleVector A = new DoubleVector(1, 0, 0);
        DoubleVector B = new DoubleVector(0, 1, 0);

        double result = A.dotProduct(B);

        assertEquals(0d, result);
    }
}
