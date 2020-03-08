package com.stclair.corlib.math.fractal.curve.hilbert;

import com.stclair.corlib.permutation.GrayCodeFunction;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HilbertCurveSegmentTest {

    @Test
    public void applyBaseSegment() {

        // vanilla contact just generates a Gray code

        long[] expected = new long[] {
                0, 1, 3, 2, 6, 7, 5, 4
        };

        HilbertCurveSegment context = new HilbertCurveSegment(3);

        long[] actual = new long[expected.length];

        for (int index = 0; index < actual.length; index++)
            actual[index] = context.apply(index);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void applyBaseSegmentWithE0E1Reflected() {

        long[] expected = new long[] {
                3, 2, 0, 1, 5, 4, 6, 7
        };

        HilbertCurveSegment context = new HilbertCurveSegment(3, 3L,  2);

        long[] actual = new long[expected.length];

        for (int index = 0; index < actual.length; index++)
            actual[index] = context.apply(index);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void applyBaseSegmentWithE0E1Exchanged() {

        long[] basis = new long[] { 2, 1, 4 };

        long[] expected = new long[] {
                0, 2, 3, 1, 5, 7, 6, 4
        };

        HilbertCurveSegment context = new HilbertCurveSegment(0L, basis, 2);

        long[] actual = new long[expected.length];

        for (int index = 0; index < actual.length; index++)
            actual[index] = context.apply(index);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void applyBasis() {

        long[] basis = new long[] {
                16, 32, 64, 128, 1, 2, 4, 8
        };


        long[] inputValues = new long[] {
                GrayCodeFunction.decode(0x95), 
                GrayCodeFunction.decode(0x56),
                GrayCodeFunction.decode(0x37), 
                GrayCodeFunction.decode(0xc2), 
                GrayCodeFunction.decode(0x02),
                GrayCodeFunction.decode(0x96),
                GrayCodeFunction.decode(0x47),
                GrayCodeFunction.decode(0x02),
                GrayCodeFunction.decode(0x77),
                GrayCodeFunction.decode(0xf6),
                GrayCodeFunction.decode(0x27),
                GrayCodeFunction.decode(0xb6),
                GrayCodeFunction.decode(0x37)
        };

        long[] expected = new long[] {
                0x59, 0x65, 0x73, 0x2c, 0x20, 0x69, 0x74, 0x20, 0x77, 0x6f, 0x72, 0x6b, 0x73
        };

        HilbertCurveSegment hilbertCurveSegment = new HilbertCurveSegment(0L, basis);

        long[] actual = Arrays.stream(inputValues)
                .map(hilbertCurveSegment::apply)
                .toArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void buildBasis() {

        long[] expected = new long[] {
                1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536
        };

        long[] actual = HilbertCurveSegment.buildBasis(17, 16);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void buildBasisAlignedToE0() {

        long[] expected = new long[] {
                65536, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
        };

        long[] actual = HilbertCurveSegment.buildBasis(17, 0);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeAxesOfProgress() {

        long[] basis = new long[] { 1, 2, 4 };

        int[] expected = new int[] { 0, 1, 1, 2, 2, 1, 1, 0 };

        int[] actual = HilbertCurveSegment.computeAxesOfProgress(basis);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeAxesOfProgressE1AndE0Exchanged() {

        long[] basis = new long[] { 2, 1, 4 };

        int[] expected = new int[] { 1, 0, 0, 2, 2, 0, 0, 1 };

        int[] actual = HilbertCurveSegment.computeAxesOfProgress(basis);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeAxesOfProgressE2AndE0Exchanged() {

        long[] basis = new long[] { 4, 2, 1 };

        int[] expected = new int[] { 2, 1, 1, 0, 0, 1, 1, 2 };

        int[] actual = HilbertCurveSegment.computeAxesOfProgress(basis);

        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void mapToSameVectorSpace() {
        
        long[] basis = new long[] { 1, 2, 4, 8, 16, 32, 64, 128 };

        long[] expected = new long[256];
        
        long[] actual = new long[expected.length];
        
        for (int index = 0; index < expected.length; index++) {
            expected[index] = index;

            actual[index] = HilbertCurveSegment.mapToVectorSpace(index, basis);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void mapToReversedVectorSpace() {

        long[] basis = new long[] { 0x80000000L, 0x40000000, 0x20000000, 0x10000000, 0x8000000, 0x4000000, 0x2000000, 0x1000000, 0x800000, 0x400000, 0x200000, 0x100000, 0x80000, 0x40000, 0x20000, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1 };

        long[] expected = new long[256];

        long[] actual = new long[expected.length];

        for (int index = 0; index < expected.length; index++) {

            expected[index] = Integer.reverse(index) & 0xFFFFFFFFL;

            actual[index] = HilbertCurveSegment.mapToVectorSpace(index, basis);
        }

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVector3dAt0() {
        long[] basis = { 1, 2, 4 };

        long expected = 0;

        long actual = HilbertCurveSegment.computeReflectionsVector(0, basis);

        assertEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVector3dAt1() {

        long[] basis = { 1, 2, 4 };

        long expected = 0;

        long actual = HilbertCurveSegment.computeReflectionsVector(1, basis);

        assertEquals(expected, actual);
    }


    @Test
    public void computeReflectionsVector3dAt2() {

        long[] basis = { 1, 2, 4 };

        long expected = 0;

        long actual = HilbertCurveSegment.computeReflectionsVector(2, basis);

        assertEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVector3dAt3() {

        long[] basis = { 1, 2, 4 };

        long expected = 3;

        long actual = HilbertCurveSegment.computeReflectionsVector(3, basis);

        assertEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVectors3d() {

        long[] basis = { 1, 2, 4, };

        long[] expected = { 0, 0, 0, 3, 3, 6, 6, 5 };

        long[] actual = HilbertCurveSegment.computeReflectionsVectors(basis);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVectors3dRotated() {

        long[] basis = { 2, 4, 1 };

        long[] expected = { 0, 0, 0, 5, 5, 3, 3, 6 };

        long[] actual = HilbertCurveSegment.computeReflectionsVectors(basis);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void computeReflectionsVectors3dE0AndE2Exchanged() {

        long[] basis = { 4, 2, 1 };

        long[] expected = { 0, 0, 0, 6, 6, 3, 3, 5 };

        long[] actual = HilbertCurveSegment.computeReflectionsVectors(basis);

        assertArrayEquals(expected, actual);
    }
}