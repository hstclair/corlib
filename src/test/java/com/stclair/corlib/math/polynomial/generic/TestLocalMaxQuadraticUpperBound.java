package com.stclair.corlib.math.polynomial.generic;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author hstclair
 * @since 8/16/15 12:57 PM
 */
public class TestLocalMaxQuadraticUpperBound {

    @Test
    public void smokeTest() {
        Double[] coefficients = { -1d, -10d, 10d, 1d };

        LocalMaxQuadraticUpperBound<Double> instance = new LocalMaxQuadraticUpperBound<>();

        Double estimate = instance.estimateUpperBound(Polynomial.of(new DoubleOperationStrategy(), coefficients));

        assertEquals(2d, estimate);
    }
}
