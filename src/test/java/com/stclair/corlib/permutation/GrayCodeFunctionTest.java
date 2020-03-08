package com.stclair.corlib.permutation;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.*;

public class GrayCodeFunctionTest {

    public void testSweepGrayCodeFunction(Function<Long, Long> grayCodeFunction) {
        for (long index = 0; index < 1024; index++) {
            long grayCode = index ^ (index >> 1);

            assertEquals(grayCode, (long) grayCodeFunction.apply(index));
        }
    }

    public void testSweepGrayCodeDecoder(Function<Long, Long> grayCodeDecoder) {
        for (long expected = 0; expected < 1024; expected++) {
            long grayCode = expected ^ (expected >> 1);

            assertEquals(expected, (long) grayCodeDecoder.apply(grayCode));
        }
    }

    @Test
    public void applyProducesGrayCode() {

        testSweepGrayCodeFunction(new GrayCodeFunction());
    }

    @Test
    public void encodeProducesGrayCode() {

        testSweepGrayCodeFunction(GrayCodeFunction::encode);
    }

    @Test
    public void decode() {

        testSweepGrayCodeDecoder(GrayCodeFunction::decode);
    }
}