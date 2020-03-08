package com.stclair.corlib.math.fractal.curve.hilbert;

/**
 * Interface for an object that can map between distance and Hilbert curve coordinates
 */
public interface HilbertCurve {

    /**
     * Convert a distance value to a set of Hilbert curve coordinates
     * @param distance the distance value to convert
     * @return the corresponding set of Hilbert curve coordinates
     */
    long[] distanceToCoords(long distance);

    /**
     * Convert a set of coordinates to a distance along a Hilbert curve
     * @param coords the set of coordinates to convert
     * @return the corresponding distance along a Hilbert curve
     */
    long coordsToDistance(long[] coords);
}
