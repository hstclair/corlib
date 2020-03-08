package com.stclair.corlib.math.fractal.curve.hilbert;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HilbertCurveImplTest {

    @Test
    public void tryDistanceToCoords() {

        long[] expectedCoords = { 8, 7, 6 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 933;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords0() {

        long[] expectedCoords = { 0, 0, 0 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 0L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords1() {

        long[] expectedCoords = { 1, 0, 0 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 1L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords2() {

        long[] expectedCoords = { 1, 1, 0 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 2L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords3() {

        long[] expectedCoords = { 0, 1, 0 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 3L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords4() {

        long[] expectedCoords = { 0, 1, 1 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 4L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords5() {

        long[] expectedCoords = { 1, 1, 1 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 5L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords6() {

        long[] expectedCoords = { 1, 0, 1 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 6L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords7() {

        long[] expectedCoords = { 0, 0, 1 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 7L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryDistanceToCoords8() {

        long[] expectedCoords = { 0, 0, 2 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long distance = 8L;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryCoordsToDistance() {

        long expectedDistance = 933;

        long[] coords = { 8, 7, 6 };

        HilbertCurve h = new HilbertCurveImpl(3);

        long actualDistance = h.coordsToDistance(coords);

        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void testDistanceToCoords24() {

        HilbertCurve h = new HilbertCurveImpl(3);

        long[] actualCoords = h.distanceToCoords(24);

        long[] expectedCoords = { 3, 0, 1 };

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void testDistanceFromCoords24() {

        HilbertCurve h = new HilbertCurveImpl(3);

        long expectedDistance = 24;

        long[] coords = { 3, 0, 1 };

        long actualDistance = h.coordsToDistance(coords);

        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void sweepTestDistanceToCoordsAndBack() {

        HilbertCurve h = new HilbertCurveImpl(3);

        for (long expectedDistance = 0; expectedDistance < 128; expectedDistance += 1) {

            long[] coords = h.distanceToCoords(expectedDistance);

            long actualDistance = h.coordsToDistance(coords);

            assertEquals(expectedDistance, actualDistance);
        }
    }

    @Test
    public void dumpCoordinates3dFirstOrder() {
        HilbertCurve h = new HilbertCurveImpl(3);

        for (long distance = 0; distance < 64; distance++) {
            long[] coords = h.distanceToCoords(distance);

            System.out.printf("%s\t%s\t%s\n", coords[2], coords[1], coords[0]);
        }
    }

}
