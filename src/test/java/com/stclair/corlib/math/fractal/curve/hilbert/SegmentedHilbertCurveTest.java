package com.stclair.corlib.math.fractal.curve.hilbert;

import org.junit.Test;

import static org.junit.Assert.*;

public class SegmentedHilbertCurveTest {

    @Test
    public void tryDistanceToCoords() {

        long[] expectedCoords = { 8, 7, 6 };

        HilbertCurve h = new SegmentedHilbertCurve(3);

        long distance = 933;

        long[] actualCoords = h.distanceToCoords(distance);

        assertArrayEquals(expectedCoords, actualCoords);
    }

    @Test
    public void tryCoordsToDistance() {

        long expectedDistance = 933;

        long[] coords = { 8, 7, 6 };

        HilbertCurve h = new SegmentedHilbertCurve(3);

        long actualDistance = h.coordsToDistance(coords);

        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void sweepTestDistanceToCoordsAndBack() {

        HilbertCurve h = new SegmentedHilbertCurve(3);

        for (long expectedDistance = 0; expectedDistance < 3000000; expectedDistance += 333) {

            long[] coords = h.distanceToCoords(expectedDistance);

            long actualDistance = h.coordsToDistance(coords);

            assertEquals(expectedDistance, actualDistance);
        }
    }

    @Test
    public void dumpCoordinates3dFirstOrder() {
        HilbertCurve h = new SegmentedHilbertCurve(2);

        for (long distance = 0; distance < 64; distance++) {
            long[] coords = h.distanceToCoords(distance);

            System.out.printf("%s\t%s\n", coords[1], coords[0]);
        }
    }
}