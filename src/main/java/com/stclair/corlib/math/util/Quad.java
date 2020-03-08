package com.stclair.corlib.math.util;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Quad {

    short exp;
    byte sign;
    long[] value = new long[2];

    public Quad(double dbl) {

        long bits = Double.doubleToLongBits(dbl);

        long exponent = (bits >> 52 ) & 0x7FF;

        if (exponent == 0 || exponent == 0x7ff) {
            sign = 0;
            exp = 0;

            return;
        }

        value[1] = ((bits & 0x000fffffffffffffL) | 0x0010000000000000L) << 10;

        sign = (byte) Math.signum(dbl);

        exp = (short) (exponent - 1023);
    }

    public Quad(long[] value, int sign, long exp) {
        this.value = value;
        this.sign = (byte) Math.signum(sign);
        this.exp = (short) exp;
    }

    public Quad product(Quad that) {
        int[] theseChunks = toInts(value);
        int[] thoseChunks = toInts(that.value);

        int[] intProduct = product(theseChunks, thoseChunks);

        int leadingZeroBits = numberOfLeadingZeros(intProduct);

        intProduct = normalize(intProduct, leadingZeroBits, 4);

        long[] longProduct = toLongs(intProduct);

        return new Quad(longProduct, this.sign * that.sign, (this.exp + 1) + (that.exp + 1) - leadingZeroBits + 1);
    }

    public Quad sum(Quad that) {

        if (that.isZero())
            return this;

        int headroomInts = 1;

        int headroomBits = headroomInts * Integer.SIZE;

        int[] intSum = sum(headroomInts, this, that);

        int resultSign = 1;

        if (intSum[intSum.length - 1] < 0) {
            intSum = negate(intSum);
            resultSign = -1;
        }

        int leadingZeroBits = numberOfLeadingZeros(intSum);

        intSum = normalize(intSum, leadingZeroBits, 4);

        long[] longSum = toLongs(intSum);

        int exp = this.exp;

        if (! that.isZero())
            exp = Math.max((this.exp + 1), that.exp + 1) - leadingZeroBits + headroomBits;

//        System.out.printf("numLeadingZeroBits=%d  this.exp=%d  that.exp=%d  result.exp=%d\n", leadingZeroBits, this.exp, that.exp, exp);

        return new Quad(longSum, resultSign, exp);
    }

    public boolean isZero() {
        return sign == 0;
    }

    public static int[] toInts(long[] longs) {

        return IntStream.range(0, longs.length << 1)
                .map(index -> ((index & 1) == 0) ? (int) longs[index >> 1] : (int) (longs[index >> 1] >> Integer.SIZE))
                .toArray();
    }

    public static long[] toLongs(int[] ints) {
        return IntStream.range(0, (ints.length >> 1))
                .mapToLong(it -> ((long) ints[it << 1] & 0xffffffffL) | ((((it << 1) + 1) < ints.length) ? ((long) ints[(it << 1) + 1]) << Integer.SIZE : 0))
                .toArray();
    }

    public int[] product(int[] chunksA, int[] chunksB) {

        int[][] results = new int[chunksB.length][];

        for (int index = 0; index < chunksB.length; index++)
            results[index] = product(chunksA, chunksB[index]);

        return productSum(results);
    }

    public int[] product(int[] chunks, long multiplier) {

        int[] result = new int[chunks.length + 1];

        long register = 0;

        for (int index = 0; index < chunks.length; index++) {

            register += chunks[index] * multiplier;

            result[index] = (int) register;

            register >>= Integer.SIZE;
        }

        result[result.length - 1] = (int) register;

        return result;
    }

    public int[] productSum(int[]... products) {

        int memberLen = Arrays.stream(products)
                .mapToInt(it -> it.length)
                .max()
                .orElse(0);

        int[] result = new int[products.length + memberLen - 1];

        long register = 0;

        for (int resultIndex = 0; resultIndex < result.length; resultIndex++) {

            int initial = Math.max(resultIndex - memberLen, 0);

            int limit = Math.min(resultIndex + 1, memberLen);

            for (int offset = initial; offset < limit; offset++) {
                if (resultIndex - offset < products.length && offset < products[resultIndex - offset].length)
                    register += products[resultIndex - offset][offset];
            }

            result[resultIndex] = (int) register;

            register >>= Integer.SIZE;
        }

        return result;
    }

    public int[] sum(int headroom, Quad... addends) {

        int[][] addendsArrays = new int[addends.length][];
        int[] addendIntOffset = new int[addends.length];


        int maxExp = Integer.MIN_VALUE;
        int minExp = Integer.MAX_VALUE;
        int minAddendIndex = Integer.MAX_VALUE;
        int maxAddendIndex = Integer.MIN_VALUE;
        int maxIntLength = 0;

        for (int index = 0; index < addends.length; index++) {

            if (addends[index].isZero()) {
                addendsArrays[index] = new int[0];
                addendIntOffset[index] = 0;
                continue;
            }

            int exp = addends[index].exp;

            int bitOffset = Integer.SIZE - (exp & (Integer.SIZE - 1));

            addendsArrays[index] = shiftIntRight(toInts(addends[index].value), bitOffset);

            if (addends[index].sign < 0)
                addendsArrays[index] = negate(addendsArrays[index]);

            int intOffset = ((exp + bitOffset) >> 5) - addendsArrays[index].length;

            addendIntOffset[index] = intOffset;

            minAddendIndex = Math.min(minAddendIndex, intOffset);
            maxAddendIndex = Math.max(maxAddendIndex, intOffset + addendsArrays[index].length);
            maxIntLength = Math.max(maxIntLength, addendsArrays[index].length);

            maxExp = Math.max(maxExp, exp);
            minExp = Math.min(minExp, exp);

//            System.out.printf("addend%d: exp=%d bitOffset=%d intOffset=%d length=%d\n", index, exp, bitOffset, intOffset, addendsArrays[index].length);
        }


        int[] result = new int[maxAddendIndex - minAddendIndex + headroom];

        long register = 0;

        for (int resultIndex = 0; resultIndex < result.length; resultIndex++) {

            for (int addendIndex = 0; addendIndex < addends.length; addendIndex++) {

                int adjustedIndex = resultIndex - addendIntOffset[addendIndex] + minAddendIndex + maxIntLength - addendsArrays[addendIndex].length;

//                System.out.printf("addend%d: resultIndex=%d addendIntOffset[addendIndex]=%d minAddendIndex=%d maxIntLength=%d addendsArrays[addendIndex].length=%d adjustedIndex=%d\n", addendIndex, resultIndex, addendIntOffset[addendIndex], minAddendIndex, maxIntLength, addendsArrays[addendIndex].length, adjustedIndex);

                if (adjustedIndex >= 0 && adjustedIndex < addendsArrays[addendIndex].length)
                        register += addendsArrays[addendIndex][adjustedIndex];
            }

            result[resultIndex] = (int) register;

            register >>= Integer.SIZE;
        }

        result = shiftIntLeft(result, Integer.SIZE - (maxExp & (Integer.SIZE - 1)));

        return result;
    }

    public int[] shiftIntRight(int[] value, int shiftCount) {

        shiftCount &= (Integer.SIZE - 1);

        if (shiftCount == 0)
            return value;

        long maskValue = 0xffffffffL << (Integer.SIZE - shiftCount);

        long register = 0;

        int[] shifted = new int[value.length + 1];

        for (int index = 0; index < shifted.length; index++) {

            if (index < value.length)
                register |= ((((long) value[index]) << (Integer.SIZE - shiftCount)) & maskValue);

            shifted[index] = (int) register;

            register >>= Integer.SIZE;
        }

        return shifted;
    }

    public int[] shiftIntLeft(int[] value, int shiftCount) {

        shiftCount &= Integer.SIZE - 1;

        if (shiftCount == 0)
            return value;

        long maskValue = 0xFFFFFFFFL << shiftCount;

        long register = 0;

        int[] shifted = new int[value.length];

        for (int index = 0; index < shifted.length; index++) {

            register |= ((((long) value[index]) << shiftCount) & maskValue);

            shifted[index] = (int) register;

            register >>= Integer.SIZE;
        }

        return shifted;
    }

    public long[] shiftLongRight(long[] value, int shiftCount) {

        shiftCount &= 63;

        if (shiftCount == 0)
            return value;

        long maskValue = 0xffffffffffffffffL << shiftCount;

        long register = 0;

        long[] shifted = new long[value.length + 1];

        for (int index = shifted.length - 1; index >= 0; index--) {

            long current = 0;

            if (index < value.length)
                current = value[index];

            shifted[index] = register | (current >> shiftCount);

            register = (current << (64 - shiftCount)) & maskValue;
        }

        return shifted;
    }

    public long[] shiftLongLeft(long[] value, int shiftCount) {

        shiftCount &= 63;

        if (shiftCount == 0)
            return value;

        long maskValue = 0x7fffffffffffffffL >> shiftCount - 1;

        long register = 0;

        long[] shifted = new long[value.length + 1];

        for (int index = 0; index < shifted.length; index++) {

            long current = 0;

            if (index < value.length)
                current = value[index];

            shifted[index] = register | (current << shiftCount);

            register = (current >> (64 - shiftCount)) & maskValue;
        }

        return shifted;
    }

    public int[] alignLeadBit(int[] value, int leadingZeroBits) {

        leadingZeroBits &= Integer.SIZE - 1;

        if (leadingZeroBits == 0)
            return shiftIntRight(value, 1);

        if (leadingZeroBits > 1)
            return shiftIntLeft(value, leadingZeroBits - 1);

        return value;
    }

    public int[] negate(int[] value) {
        int[] negated = new int[value.length];

        long register = 1;

        // 0 negated is 0      0 inverted is -1
        // -1 negated is 1    -1 inverted is 0
        // 1 negated is -1     1 inverted is -2

        for (int index = 0; index < negated.length; index++) {
            register += ((long) ~value[index]) & 0xFFFFFFFFL;
            negated[index] = (int) register;
            register >>= 32;
        }

        return negated;
    }

    public int[] normalize(int[] value, int leadingZeroBits, int size) {

        int[] result = new int[size];

        if (leadingZeroBits == -1)  // result is zero
            return result;

        int leadingZeroInts = leadingZeroBits >> 5;

        value = alignLeadBit(value, leadingZeroBits);

        int srcStart = value.length - leadingZeroInts - size;

        if (srcStart < 0) {
            size += srcStart;
            srcStart = 0;
        }

        System.arraycopy(value, srcStart, result, 0, size);

        return result;
    }

    public int numberOfLeadingZeros(long[] value) {

        int result = 0;

        for (int index = value.length - 1; index >= 0; index--) {

            if (value[index] != 0)
                return result + Long.numberOfLeadingZeros(value[index]);

            result += Long.SIZE;
        }

        return value.length << 6;
    }

    public int numberOfLeadingZeros(int[] value) {

        int result = 0;

        for (int index = value.length - 1; index >= 0; index--) {

            if (value[index] != 0)
                return result + Integer.numberOfLeadingZeros(value[index]);

            result += Integer.SIZE;
        }

        return value.length << 5;
    }

    public int intValue() {
        return (int) doubleValue();
    }

    public long longValue() {
        return (long) doubleValue();
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public double doubleValue() {

        long shifted = value[1] >> 11;

        double scale = Math.pow(2, exp - 51);

        double scaled = (shifted) * scale;

        return sign * scaled;
    }
}
