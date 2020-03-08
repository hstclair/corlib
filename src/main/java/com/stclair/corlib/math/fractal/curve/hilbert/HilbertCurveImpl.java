package com.stclair.corlib.math.fractal.curve.hilbert;

import com.stclair.corlib.permutation.GrayCodeFunction;

/**
 * Somewhat more efficient implementation of Hilbert curve
 */
public class HilbertCurveImpl implements HilbertCurve {

    /** the dimension of this Hilbert curve - determines the number of coordinates generated or accepted */
    public final int dimension;

    /** the number of steps required to reduce a Long value to coordinates or to transform coordinates to a Long value */
    public final int steps;

    /** the number of bits by which a distance value will be shifted right in order to access the first set of distance bits */
    public final int initialShift;

    /** the bitmask value used to obtain a group of distance bits */
    public final long mask;

    /** the GrayCodeFunction instance used to convert each group of distance bits to its corresponding Gray code value */
    public final GrayCodeFunction grayCodeFunction;

    /**
     * constructor
     * @param dimension the dimension of this Hilbert curve - this value determines the number of coordinates generated or accepted
     */
    public HilbertCurveImpl(int dimension) {
        this.dimension = dimension;
        this.mask = (1 << dimension) - 1;
        this.grayCodeFunction = new GrayCodeFunction();
        this.steps = 1 + Long.SIZE / dimension;
        this.initialShift = (steps - 1) * dimension;
    }

    @Override
    public long[] distanceToCoords(long distance) {

        long reflection = 0L;

        int shift = initialShift;

        int rotation = 0;

        long[] coords = new long[dimension];

        for (int order = 0; order < steps; order++) {

            long vertexRank = getVertexRank(distance, shift);

            long originalBitVector = grayCodeFunction.apply(vertexRank);

            long unreflectedBitVector = applyRotation(originalBitVector, rotation);

            long contextualizedBitVector = unreflectedBitVector ^ reflection;


            shift -= dimension;

            reflection = getReflection(vertexRank, reflection, rotation);

            rotation = getRotation(vertexRank, rotation);

            applyBitVector(contextualizedBitVector, coords);
        }

        return coords;
    }

    @Override
    public long coordsToDistance(long[] coords) {

        long reflection = 0L;
        int rotation = 0;
        int bitMask = 1 << (steps - 1);

        long distance = 0L;

        for (int order = 0; order < steps; order++) {

            long contextualizedBitVector = getBitVector(coords, bitMask);

            long unreflectedBitVector = contextualizedBitVector ^ reflection;

            long originalBitVector = removeRotation(unreflectedBitVector, rotation);

            long vertexRank = GrayCodeFunction.decode(originalBitVector);

            distance = (distance << dimension) | vertexRank;

            bitMask >>= 1;

            reflection = getReflection(vertexRank, reflection, rotation);

            rotation = getRotation(vertexRank, rotation);
        }

        return distance;
    }

    /**
     * get the current vertexRank
     * @param distance the original distance value
     * @param shift the bit number of the lowest-order bit in the vertex rank within the distance value
     * @return the selected vertex rank taken from the distance value
     */
    public long getVertexRank(long distance, int shift) {
        return mask & (distance >>> shift);
    }

    /**
     * rotate a bit vector representing an N-cube vertex to conform with the axes of the current frame of reference
     * @param bitVector the bit vector representing a vertex on an N-cube
     * @param rotation the bit rotation required to align with the axes of the current frame of reference
     * @return the vector representing a vertex on an N-cube relative to the current axes
     */
    public long applyRotation(long bitVector, int rotation) {
        return mask & ((bitVector << rotation) | (bitVector >> (dimension - rotation)));
    }

    /**
     * rotate a bit vector representing an N-cube vertex in the current frame of reference to restore the original bitVector
     * @param bitVector the bit vector representing a vertex on an N-cube in the current frame of reference
     * @param rotation the bit rotation required to align with the axes of the current frame of reference
     * @return the original bit vector
     */
    public long removeRotation(long bitVector, int rotation) {
        return mask & ((bitVector << (dimension - rotation)) | (bitVector >> rotation));
    }

    /**
     * compute the rotation required to align a bit vector to the frame of reference for the given vertex
     * @param vertexRank the rank of the current N-cube vertex
     * @return the number of bits by which all associated N-cube bit vectors must be rotated
     */
    public int getRotation(long vertexRank, int rotation) {

        if (vertexRank == 0)
            return (rotation + 1) % dimension;

        long dist1 = (vertexRank + 1) ^ (vertexRank - 1);
        long dist2 = dist1 ^ (dist1 >> 1);

        return (1 + rotation + Long.numberOfTrailingZeros(dist2 & ~1)) % dimension;
    }

    /**
     * compute an updated reflections bit vector value
     * @param vertexRank the rank of the current N-cube vertex
     * @param currentReflectionBitVector the current value of the reflection bit vector
     * @param rotation the rotation required to align the new reflection bit vector with the current frame of reference
     * @return an updated reflections bit vector encoding current reflections as well as prior reflections
     */
    public long getReflection(long vertexRank, long currentReflectionBitVector, int rotation) {

        long reflections = getReflectionBitVector(vertexRank);

        long rotatedReflections = applyRotation(reflections, rotation);

        return rotatedReflections ^ currentReflectionBitVector;
    }

    /**
     * get the reflection bit vector associated with the given vertex rank
     * @param vertexRank the rank of the current N-cube vertex
     * @return a bit vector representing the base set of reflections required to align with the given vertexRank
     */
    public long getReflectionBitVector(long vertexRank) {
        if (vertexRank == 0)
            return 0L;

        vertexRank--;

        return (vertexRank & ~1) ^ (vertexRank >> 1);
    }

    /**
     * apply a bit vector value to the current array of coordinate values
     * @param bitVector a bit vector representing a set of low-order coordinate bits to be appended
     * @param coords the array of coordinate values
     * @return the original coordinate values with the low-order bits appended
     */
    public long[] applyBitVector(long bitVector, long[] coords) {

        for (int index = 0; index < coords.length; index++) {

            coords[index] = (coords[index] << 1) | (bitVector & 1);

            bitVector >>= 1;
        }

        return coords;
    }

    /**
     * extract a bit vector value from the value of the selected bit position in the array of coordinate values
     * @param coords the array of coordinate values
     * @param bitMask the bit mask to apply to extract the bit value from each coordinate value
     * @return the bit vector corresponding to the selected bit
     */
    public long getBitVector(long[] coords, int bitMask) {

        long bits = 0;
        long bit = 1;

        for (int index = 0; index < coords.length; index++) {

            if ((coords[index] & bitMask) != 0)
                bits |= bit;

            bit <<= 1;
        }

        return bits;
    }
}
