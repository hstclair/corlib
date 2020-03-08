package com.stclair.corlib.permutation;

import java.util.function.Function;

public class GrayCodeFunction implements Function<Long, Long> {

    public static final GrayCodeFunction Instance = new GrayCodeFunction();

    public Long apply(Long value) {

        return encode(value);
    }

    public static long encode(long value) {

        return value ^ (value >> 1);
    }

    public static long decode(long value) {

        if (value == 0 || value == 1)
            return value;

        long mask = Long.highestOneBit(value);

        long bit = mask >> 1;

        long result = value;

        while (mask != 0) {

            result ^= bit;

            mask >>= 1;

            bit = (result & mask) >> 1;

        }

        return result;
    }
}
