package com.stclair.corlib.math.fractal.curve.hilbert;

import com.stclair.corlib.permutation.GrayCodeFunction;

/**
 * Class that maps a distance value to an N-dimensional binary coordinate vector along a Hilbert curve
 */
public class HilbertCurveSegment {

    /** a vector representing the reflected axes of this section of the Hilbert curve */
    private final long reflectionsVector;

    /** the axis along which this Hilbert curve segment will progress */
    private final int axisOfProgress;

    /** the set of basis vectors used to map a distance along this segment to the corresponding Hilbert curve vector */
    private final long[] basis;

    /** the axes of progress for each of the vectors along this Hilbert curve segment */
    private int[] axesOfProgress;

    /** the reflection vectors associated with each of the vector intersections along this Hilbert curve segment */
    private long[] reflectionsVectors;

    /**
     * constructor for base Hilbert curve
     * @param dimension the dimension of the Hilbert curve to be generated
     */
    public HilbertCurveSegment(int dimension) {
        this(dimension, 0L, dimension - 1);
    }

    /**
     * constructor
     * @param dimension the dimension of the Hilbert curve to be generated
     * @param reflectionsVector a vector representing the reflected axes of this segment of the Hilbert curve
     * @param axisOfProgress the zero-relative outer progress axis to which this Hilbert curve segment must align
     */
    public HilbertCurveSegment(int dimension, long reflectionsVector, int axisOfProgress) {
        this(reflectionsVector, buildBasis(dimension, axisOfProgress), axisOfProgress);
    }

    /**
     * constructor
     * @param reflectionsVector a vector representing the reflected axes of this segment of the Hilbert curve
     * @param basis the set of basis vectors used to map a distance along this segment to the corresponding coordinate vector
     */
    public HilbertCurveSegment(long reflectionsVector, long[] basis) {
        this(reflectionsVector, basis, basis.length - 1);
    }

    /**
     * constructor
     * @param reflectionsVector a reflections vector representing the reflected axes of this segment of the Hilbert curve
     * @param basis the set of basis vectors used to map a distance along this segment to the corresponding coordinate vector
     * @param axisOfProgress the zero-relative outer progress axis to which this Hilbert curve segment must align
     */
    public HilbertCurveSegment(long reflectionsVector, long[] basis, int axisOfProgress) {

        this.reflectionsVector = reflectionsVector;

        this.basis = basis;

        this.axisOfProgress = axisOfProgress;

        this.axesOfProgress = computeAxesOfProgress(basis);
    }

    /**
     * compute the coordinate vector for a given distance along this segment of a Hilbert curve
     * @param distance the distance to be mapped
     * @return
     */
    public long apply(long distance) {

        long result = applyBasisToDistance(distance, basis);

        return result ^ reflectionsVector;
    }

    /**
     * get the array of reflection vectors associated with each of the coordinate vectors along this Hilbert curve segment
     * @return
     */
    public long[] getReflectionsVectors() {

        if (reflectionsVectors == null)
            reflectionsVectors = computeReflectionsVectors(basis);

        return reflectionsVectors;
    }

    /**
     * map the selected bit of each member of the provided set of coordinates to a single coordinates vector
     * @param coordinates the set of coordinates to be mapped
     * @param degree the position of the bit to be mapped (this is the same as the zero-relative degree of the Hilbert curve)
     * @return
     */
    public long getCoordinatesVector(long[] coordinates, int degree) {

        long rawCoordinatesVector = getRawCoordinatesVector(coordinates, degree);

        long reflectedCoordinatesVector = rawCoordinatesVector ^ reflectionsVector;

        long mappedCoordinatesVector = mapFromVectorSpace(reflectedCoordinatesVector, basis);

        return GrayCodeFunction.decode(mappedCoordinatesVector);
    }

    /**
     * get the raw, unreflected coordinates vector value in the target basis
     * @param coordinates the set of coordinates for the point to be mapped
     * @param degree the degree of the bit in the set of coordinates from which to build the coordinates vector
     */
    public static long getRawCoordinatesVector(long[] coordinates, int degree) {

        long coordinatesVector = 0;

        long sourceBit = 1L << degree;
        long outputBit = 1L;

        for (int index = 0; index < coordinates.length; index++) {
            coordinatesVector |= (coordinates[index] & sourceBit) == 0 ? 0 : outputBit;

            outputBit <<= 1;
        }

        return coordinatesVector;
    }

    /**
     * get the reflection vector associated with a specific coordinate vector along this Hilbert curve segment
     * @param distance the distance value indicating the coordinate vector for which the reflection vector is to be returned
     * @return
     */
    public long getReflectionsVector(long distance) {

        return getReflectionsVectors()[(int) distance];
    }

    /**
     * get the array of axes of progress for each of the coordinate vectors along this Hilbert curve segment
     * @return
     */
    public int[] getAxesOfProgress() {

        if (axesOfProgress == null)
            axesOfProgress = computeAxesOfProgress(basis);

        return axesOfProgress;
    }

    /**
     * get the axis of progress for a specific coordinate vector along this Hilbert curve segment
     * @param distance the distance value indicating the coordinate vector for which the progress axis is to be returned
     * @return
     */
    public int getAxisOfProgress(long distance) {

        return getAxesOfProgress()[(int)distance];
    }

    /**
     * get the dimension of this Hilbert curve segment
     * @return
     */
    public int getDimension() {
        return basis.length;
    }

    /**
     * get the inner (i.e. next higher degree) HilbertCurveSegment instance
     * associated with the coordinate vector at the specified distance
     * @param distance the distance value identifying the coordinate vector for which the HilbertCurveSegment is needed
     * @return
     */
    public HilbertCurveSegment getInnerFrame(long distance) {

        return new HilbertCurveSegment(getDimension(), getReflectionsVector(distance) ^ reflectionsVector, getAxisOfProgress(distance));
    }

    /**
     * compute the coordinate vector mapping for a given distance along this non-reflected segment of a Hilbert curve
     * @param distance the distance to be mapped
     * @return the corresponding bit-mapped coordinate vector
     */
    public static long applyBasisToDistance(long distance, long[] basis) {

        long grayCode = distance ^ (distance >> 1);

        return mapToVectorSpace(grayCode, basis);
    }

    /**
     * Map the provided vector to a new vector space defined by the set of basis vectors
     * @param vector the vector to be mapped
     * @param basis the set of basis vectors defining the vector space
     * @return the value in the new vector space
     */
    public static long mapToVectorSpace(long vector, long[] basis) {

        long bit = 1L;

        long result = 0L;

        for (int index = 0; index < basis.length; index++) {
            if ((vector & basis[index]) != 0)
                result |= bit;

            bit <<= 1;
        }

        return result;
    }

    /**
     * Map the provided vector back to the "standard" basis from the space defined by the basis vectors
     * @param vector the vector to be mapped
     * @param basis the set of basis vectors defining the vector space in which the vector is currently expressed
     * @return the corresponding vector in the "standard" basis
     */
    public static long mapFromVectorSpace(long vector, long[] basis) {

        long bit = 1L;
        long result = 0L;

        for (int index = 0; index < basis.length; index++) {

            if ((vector & bit) != 0)
                result |= basis[index];

            bit <<= 1;
        }

        return result;
    }

    /**
     * compute the array of reflections vectors associated with each of the coordinate vectors along the Hilbert curve
     * segment that result from the provided basis set
     * @param basis the set of basis vectors from which the Hilbert curve segment is constructed
     * @return
     */
    public static long[] computeReflectionsVectors(long[] basis) {

        int maxDistance = 1 << basis.length;

        long[] reflections = new long[maxDistance];

        for (int distance = 0; distance < maxDistance; distance++)
            reflections[distance] = computeReflectionsVector(distance, basis);

        return reflections;
    }

    /**
     * compute the reflections vector associated with the coordinate vector at the specified distance along a Hilbert curve segment
     * @param distance the distance to the vector for which the a reflections vector is to be computed
     * @param basis the set of basis vectors defining the Hilbert curve segment
     * @return
     */
    public static long computeReflectionsVector(long distance, long[] basis) {

        if (distance == 0)
            return 0;

        long reflectionsVector = computeRawReflectionsVector(distance);

        return mapToVectorSpace(reflectionsVector, basis);
    }


    /**
     * compute the reflections vector associated with the coordinate vector at the specified distance along a Hilbert curve segment
     * using the "standard" basis
     * @param distance the distance to the vector for which the a reflections vector is to be computed
     * @return
     */
    public static long computeRawReflectionsVector(long distance) {

        if (distance == 0)
            return 0;

        distance--;

        return (distance & ~1) ^ (distance >> 1);
    }

    /**
     * Build the array of integers identifying the axes of progress for each of the coordinate vectors along the
     * Hilbert curve segment generated by the provided set of basis vectors
     * @param basis the array of basis vectors defining the Hilbert curve segment
     * @return
     */
    public static int[] computeAxesOfProgress(long[] basis) {

        int maxDistance = (1 << basis.length);

        int[] axesOfProgress = new int[maxDistance];

        int mask = (1 << basis.length) - 1;

        for (int distance = 0; distance < axesOfProgress.length; distance++)
            axesOfProgress[distance] = computeAxisOfProgress(distance, mask, basis);

        return axesOfProgress;
    }

    /**
     * compute a single axis of progress value and map it to the provided set
     * of basis vectors
     * @param distance the distance travelled along the current Hilbert curve segment
     * @param basis the array of basis vectors defining the Hilbert curve segment
     * @return
     */
    public static int computeAxisOfProgress(int distance, int mask, long[] basis) {

        int baseAxis = computeRawAxisOfProgressValue(distance) & mask;

        if (baseAxis == 0)
            baseAxis = 1;

        for (int index = 0; index < basis.length; index++)
            if (basis[index] == baseAxis)
                return index;

        throw new IllegalStateException("Unable to find matching vector");
    }

    /**
     * Compute the dimension- and basis-independent value for the axis of progress
     * @param distance the distance travelled along the current Hilbert curve segment
     * @return
     * @implNote
     *
     * It is helpful to remember that the movement of the Hilbert curve among the
     * vertices of a unit metric form (e.g. a square, a cube, a tesseract, etc.) is
     * dictated by a series of Gray code values.  Each given bit represents a single
     * coordinate value in a vector that defines a single vertex.  For example, in a
     * cube, bit 0 might represent "away" (1) or "toward" (0) while bit 1 might
     * represent "up" (1) or "down" (0) and bit 2 might represent "right" (1) or
     * "left" (0).
     *
     * The "axis of progress" identifies the element that changes during a transition
     * from one coordinate vector to the next.  This value is important because
     * each degree N+1 Hilbert curve resides between two adjacent coordinate vectors
     * in the degree N Hilbert curve above it.  In order to ensure the required
     * continuity of the aggregate Hilbert curve, the entry and exit points of
     * the degree N+1 Hilbert curve must align with the changing coordinate value
     * between the two coordinate vectors between which it sits.
     *
     * The situation becomes slightly more complicated, however, because the
     * curve demands that each movement along a particular axis must repeat
     * to ensure that the resulting shape is symmetrical and that all of the
     * vertices within the enclosing space have been reached.  So, while the
     * actual axes of progress for the three-dimensional degree 1 Hilbert curve
     * are "away", "up", "toward", "right", "away", "down", "toward" during
     * its movement through the vertices 000, 001, 011, 010, 110, 111, 101, 100,
     * the movements of the degree 2 Hilbert curves that we must attach to
     * it must instead progress along the path "away", "up", "up", "right",
     * "right", "down", "down", "toward"
     *
     * This odd behavior may make more sense if we consider the synthesis of the
     * degree 1 curve with the degree 2 curve.  When we join these together, we
     * multiply the coordinates of the degree 1 curve by two and then add the
     * coordinates from the degree 2 curve.  So the vertices along the outer
     * curve become 000, 002, 022, 020, 220, 222, 202, 200.  By ensuring that
     * our inner curve overall progress aligns with the axis of progress
     * required by the outer curve, we receive the following sequence for the
     * first three vertices of the outer curve:
     *
     * 000, 010, 110, 100, 101, 111, 011, 001,
     * 002, 102, 103, 003, 013, 113, 112, 012,
     * 022, 122, 123, 023, 033, 133, 132, 032,
     * ...
     *
     * If we directly followed the progress of the outer curve, the final
     * point in this sequence would not align with the section that follows
     * it.
     *
     * The value for the direct axis of progress can be obtained by taking
     * the XOR of the two adjacent Gray code values representing the start
     * and end coordinate vectors (this works because there is always exactly
     * one bit that changes state any two between adjacent Gray code values)
     *
     * For the axis that we need to align to, we back up one step more
     * and compute the XOR of the two Gray code values that <i>bracket</i>
     * the requested distance and discard the value of bit 0.
     *
     * The result of this calculation yields an unending sequence which is
     * valid for all Hilbert curve segments using a "standard" basis.
     * However, this means that it <b>does not</b> naturally return to 1 at
     * any point.
     *
     * Since the progress along a given Hilbert curve segment is always
     * symmetrical, the recommended of this method is to fetch the first
     * 2^(dimension - 1) values in order and then to reflect the sequence
     * back on itself to generate the latter half:
     *
     * e.g., for a three-dimensional curve, retrieve the first four values:
     *
     * 1, 2, 2, 4
     *
     * then synthesize the latter half by either retrieving these values in
     * reverse order or simply storing them as an array and accessing in reverse.
     *
     * Alternatively, note that the full sequence will always be symmetrical
     * except for the terminal entry:
     *
     * The first eight return values:
     *     1, 2, 2, 4, 2, 2, 8
     *
     * The values required for the degree N+1 three dimensional curve:
     *     1, 2, 2, 4, 2, 2, 1
     *
     * In any event, the computational cost of generating these values is
     * negligible so that it is quite possibly less expensive to generate
     * them on demand rather than retrieve them from an array.
     */
    public static int computeRawAxisOfProgressValue(int distance) {

        if (distance == 0)
            return 1;

        int axisDistanceA = distance + 1;
        int axisDistanceB = distance - 1;

        int valueA = (axisDistanceA ^ (axisDistanceA >> 1));
        int valueB = (axisDistanceB ^ (axisDistanceB >> 1));

        return (valueA ^ valueB) & ~1;
    }

    /**
     * Build the set of bit-mapped basis vectors used to construct the coordinate vector mapping for a Hilbert curve segment
     * @param dimension the dimension of the basis vectors to be generated
     * @param progressAxis the index of the axis along which the resulting Hilbert curve segment must progress
     *                     between its start point and end point
     * @return an array of bit-mapped basis vectors
     */
    public static long[] buildBasis(int dimension, int progressAxis) {

        long[] basis = new long[dimension];

        long mask = (1L << dimension) - 1;

        long bit = 1L << (dimension - 1 - progressAxis);

        for (int index = 0; index < dimension; index++) {
            basis[index] = bit;

            bit = (bit << 1) & mask;

            if (bit == 0L)
                bit = 1L;
        }

        return basis;
    }
}
