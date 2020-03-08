package com.stclair.corlib.math.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntArray {

    public static final IntArray Zero = new IntArray(0, new int[0]);

    public static final int[] ZeroArray = new int[0];

    private int scale;

    private int[] value;

    private int signExtension;

    private Integer computedMostSignificantBit;

    private Integer computedLeastSignificantBit;

    private Integer computedSignum;

    public IntArray(double dbl) {
        long doubleBits = Double.doubleToLongBits(dbl);

        long exponent = (doubleBits >> 52 ) & 0x7FF;

        if (exponent == 0 || exponent == 0x7ff) {
            scale = 0;
            value = ZeroArray;

            return;
        }

        value = new int[2];

        long longValue =  ((doubleBits & 0x000fffffffffffffL) | 0x0010000000000000L) << 10;

        value[0] = (int) longValue;

        value[1] = (int) (longValue >> Integer.SIZE);

        scale = (int) (exponent - 1023);

        if ( Math.signum(dbl) < 0)
            value = negate(value);
    }

    public IntArray(int scale, int[] value) {
        this.scale = scale;
        this.value = value;

        if (value.length == 0 || signum(value) == 0)
            this.value = ZeroArray;
        else if (value[value.length - 1] < 0)
            signExtension = -1;
    }

    public int getExponent() {
        return scale - Integer.SIZE;
    }

    public int signum() {

        if (this == Zero)
            return 0;

        if (computedSignum == null)
            computedSignum = signum(value);

        return computedSignum;
    }

    public int getScale() {
        return scale;
    }

    public IntArray negate() {

        if (this == Zero)
            return this;

        return new IntArray(scale, negate(value));
    }

    public IntArray abs() {

        if (isNegative(value))
            return negate();

        return this;
    }

    public int compare(IntArray thatOne) {

        IntArray thisOne = this;

        if (thisOne == thatOne)
            return 0;

        if (thatOne == Zero)
            return signum();

        if (thisOne == Zero)
            return -thatOne.signum();

        boolean thisIsNegative = isNegative(thisOne.value);
        boolean thatIsNegative = isNegative(thatOne.value);

        if (thisIsNegative && ! thatIsNegative)
            return -1;

        if (thatIsNegative && ! thisIsNegative)
            return 1;

        if (thisIsNegative) {
            thisOne = thisOne.negate();
            thatOne = thatOne.negate();
        }

        int thisIsGreaterThanThat = thisIsNegative ? -1 : 1;
        int thisIsLessThanThat = thisIsNegative ? 1 : -1;

        int thisMsb = thisOne.mostSignificantBit();
        int thatMsb = thatOne.mostSignificantBit();

        if (thisMsb > thatMsb)
            return thisIsGreaterThanThat;

        if (thatMsb > thisMsb)
            return thisIsLessThanThat;

        int leadingBit = Math.max(this.scale, thatOne.scale);
        int trailingBit = Math.min(this.scale - (this.value.length << 5), thatOne.scale - (thatOne.value.length << 5));


        for (int index = leadingBit; index > trailingBit; index -= Integer.SIZE) {

            long thisValue = ((long) thisOne.get(index)) & 0xFFFFFFFFL;
            long thatValue = ((long) thatOne.get(index)) & 0xFFFFFFFFL;

            long xored = thisValue ^ thatValue;

            if (xored != 0) {
                thisValue &= xored;
                thatValue &= xored;

                if (thisValue > thatValue)
                    return thisIsGreaterThanThat;

                if (thisValue < thatValue)
                    return thisIsLessThanThat;
            }
        }

        return 0;
    }

    /*
    If I'm at 32 bits and you request a value starting at 64 bits,
    that value lies beyond the <b>end</b> of my array so I must
    return my sign value;

    If I'm at 32 bits and you request a value starting at 48 bits,
    that value lies <i>partially</i> beyond the end of my array so
    I must return my sign value shifted left by 48-32 bits and
    combined with the last entry in my array shifted right by
    32 - (48 - 32) bits

    If I'm at 32 bits and you request a value starting at 32 bits,
    I must return the last value in my array

    If I'm at 32 bits and you request a value starting at 16 bits,
    I must return the last value in my array shifted left by
    32 - 16 bits combined with the next-to-last value in my array
    shifted right by 32 - (32 - 16) bits (if there is no lower
    value in my array, then I replace it with zero

    If you request a value that lies completely below the first
    entry in my array, I must simply return zero
     */

    public int get(int leadingBitPosition) {

        int scaleOffset = leadingBitPosition - scale;

        int intOffset = (scaleOffset >> 5) + value.length;

        long register = signExtension;

        if (intOffset > value.length || value.length == 0)
            return (int) register;

        if (intOffset < value.length)
            register = value[intOffset];

        register <<= Integer.SIZE;

        if (intOffset > 0)
            register |= ((long) value[intOffset - 1]) & 0xFFFFFFFFL;

        int bitOffset = scaleOffset & (Integer.SIZE - 1);

        return (int) (register >> bitOffset);
    }

    public IntArray sum(IntArray that) {

        if (this == Zero)
            return that;

        if (that == Zero)
            return this;

        if (this.compare(that) >= 0)
            return sum(this.value, this.scale, that.value, that.scale);

        return sum(that.value, that.scale, this.value, this.scale);
    }

    public void computeMsbLsb() {

        int[] value = makePositive(this.value);

        this.computedLeastSignificantBit = computeLeastSignificantBit(value, scale);
        this.computedMostSignificantBit = computeMostSignificantBit(value, scale);
    }

    public int leastSignificantBit() {

        if (computedLeastSignificantBit == null)
            computeMsbLsb();

        return computedLeastSignificantBit;
    }

    public int mostSignificantBit() {

        if (computedMostSignificantBit == null)
            computeMsbLsb();

        return computedMostSignificantBit;
    }

    public int significantBits() {

        if (value.length == 0 || signum() == 0)
            return 0;

        return mostSignificantBit() - leastSignificantBit() + 1;
    }

    public double toDouble() {

        if (signum() == 0)
            return 0;

        boolean negative = isNegative(value);

        int msb = mostSignificantBit();

        int doubleOffset = 10;

        long h = ((long) get(msb)) << Integer.SIZE;

        long l = ((long) get(msb - 32)) & 0xFFFFFFFFL;

        long value = (h+l) >> doubleOffset;

        if (negative)
            value = -value;

        value &= 0x000FFFFFFFFFFFFFL;

        long exponent = ((long) (msb + 1023)) << 52;
        long doubleBits = exponent | value;

        return Double.longBitsToDouble(doubleBits);
    }

    public int[] getArray() {
        return Arrays.copyOf(value, value.length);
    }

    public IntArray product(int multiplier, int scale) {

        if (signum() == 0)
            return Zero;

        int[] result = new int[value.length + 1];

        long register = 0;

        for (int index = 0; index < result.length; index++) {

            if (index < value.length)
                register += ((long) multiplier) * value[index];

            result[index] = (int) register;

            register >>= Integer.SIZE;
        }

        return new IntArray(this.mostSignificantBit() + scale, result);
    }

    public IntArray[] productComponents(IntArray multiplier) {

        List<IntArray> products = new ArrayList<>();

        for (int index = multiplier.value.length - 1, scale = multiplier.mostSignificantBit()+1; index >= 0; index--, scale -= Integer.SIZE)
            products.add(product(multiplier.value[index], scale));

//        IntArray result = Zero;
//
//        for (IntArray addend : products)
//            result = result.sum(addend);

        return products.toArray(new IntArray[0]);
    }





    public static IntArray sum(int[] greaterScaleValue, int greaterScale, int[] lesserScaleValue, int lesserScale) {

        int leadingBit = greaterScale;
        int trailingBit = lesserScale - (lesserScaleValue.length << 5);

        int bitCount = leadingBit - trailingBit;

        int intCount = (bitCount >> 5) + 2;

        int scaleOffset = greaterScale - lesserScale;

        int scaleIntOffset = (scaleOffset + Integer.SIZE - 1) >> 5;

        int bitShiftCount = scaleOffset & (Integer.SIZE - 1);

        int[] result = new int[greaterScaleValue.length + scaleIntOffset + 1];

        long lesserSignExtension = (lesserScaleValue[lesserScaleValue.length - 1] < 0) ? 0xFFFFFFFFL : 0;
        long greaterSignExtension = (greaterScaleValue[greaterScaleValue.length - 1] < 0) ? 0xFFFFFFFFL : 0;

        long register = 0;

        for (int index = 0, lesserIntPos = 0, greaterIntPos = scaleIntOffset; index < result.length; index++, lesserIntPos++, greaterIntPos++) {

            if (lesserIntPos < lesserScaleValue.length)
                register += (((long) lesserScaleValue[lesserIntPos]) & 0xFFFFFFFFL) << bitShiftCount;
            else
                register += lesserSignExtension;

            if (greaterIntPos >= 0) {

                if (greaterIntPos < greaterScaleValue.length)
                    register += ((long) greaterScaleValue[greaterIntPos]) & 0xFFFFFFFFL;
                else
                    register += greaterSignExtension;
            }

            result[index] = (int) register;

            register >>= Integer.SIZE;
        }

        return new IntArray(greaterScale + Integer.SIZE, result);
    }

    public static boolean isNegative(int[] value) {
        return value[value.length - 1] < 0;
    }

    public static int[] makePositive(int[] value) {

        if (! isNegative(value))
            return value;

        return negate(value);
    }

    public static int[] negate(int[] value) {

        int[] result = Arrays.copyOf(value, value.length);

        long register = 1;

        for (int index = 0; index < result.length; index++) {
            register += (~result[index]) & 0xFFFFFFFFL;

            result[index] = (int) register;

            register >>= Integer.SIZE;
        }

        return result;
    }

    public static int computeMostSignificantBit(int[] value, int scale) {

        value = makePositive(value);

        // TODO: why is it necessary to add two in order for this to square???
        int position = scale + 2;

        for (int index = value.length - 1; index >= 0; index--) {
            if (value[index] == 0) {
                position -= Integer.SIZE;
                continue;
            }

            return position - Integer.numberOfLeadingZeros(value[index]) - 1;
        }

        return position;
    }

    public static int computeLeastSignificantBit(int[] value, int scale) {

        value = makePositive(value);

        int position = scale - (value.length << 5) + 2;

        for (int index = 0; index < value.length; index++) {
            if (value[index] == 0) {
                position += Integer.SIZE;
                continue;
            }

            return position + Integer.numberOfTrailingZeros(value[index]);
        }

        return position;
    }

    public static int signum(int[] value) {

        if (value.length == 0)
            return 0;

        if (value[value.length - 1] < 0)
            return -1;

        for (int i : value)
            if (i != 0)
                return 1;

        return 0;
    }
}
