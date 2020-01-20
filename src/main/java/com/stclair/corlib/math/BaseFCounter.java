package com.stclair.corlib.math;

import java.util.Arrays;

import static com.stclair.corlib.validation.Validation.inRange;

/**
 * Implementation of a factorial ("base F") counter
 * Each "digit" signifies a multiple of the factorial of its own rank
 * and ranges from 0 to its own rank:
 *
 * rank    multiplier      range
 * 1       1               0-1
 * 2       2               0-2
 * 3       6               0-3
 * 4       24              0-4
 * 5       120             0-5
 * 6       720             0-6
 *
 * The first seventeen digits are packed into a single 64-bit long value
 * in order to increase performance by minimizing array traversal.
 *
 * The primary purpose of this structure is to facilitate the generation
 * of permutation results.
 */
public class BaseFCounter {

    //                                                                  0000 0001
    private static final long digit0Mask =        1L;


    //                                                                  0000 0110
    private static final int  digit1BitOffset = 1;
    private static final long digit1Mask =        3L << digit1BitOffset;
    private static final long digit1Increment =   1L << digit1BitOffset;
    private static final long digit1Overflow =    3L << digit1BitOffset;

    //                                                                  0001 1000
    private static final int  digit2BitOffset = 3;
    private static final long digit2Mask =        3L << digit2BitOffset;
    private static final long digit2Increment =   1L << digit2BitOffset;

    //                                                                  1110 0000
    private static final int  digit3BitOffset = 5;
    private static final long digit3Mask =        7L << digit3BitOffset;
    private static final long digit3Increment =   1L << digit3BitOffset;
    private static final long digit3Overflow =    5L << digit3BitOffset;

    //                                                             0111 0000 0000
    private static final int  digit4BitOffset = 8;
    private static final long digit4Mask =        7L << digit4BitOffset;
    private static final long digit4Increment =   1L << digit4BitOffset;
    private static final long digit4Overflow =    6L << digit4BitOffset;

    //                                                        0011 1000 0000 0000
    private static final int  digit5BitOffset = 11;
    private static final long digit5Mask =        7L << digit5BitOffset;
    private static final long digit5Increment =   1L << digit5BitOffset;
    private static final long digit5Overflow =    7L << digit5BitOffset;

    //                                                   0001 1100 0000 0000 0000
    private static final int  digit6BitOffset = 14;
    private static final long digit6Mask =        7L << digit6BitOffset;
    private static final long digit6Increment =   1L << digit6BitOffset;

    //                                              0001 1110 0000 0000 0000 0000
    private static final int  digit7BitOffset = 17;
    private static final long digit7Mask =       15L << digit7BitOffset;
    private static final long digit7Increment =   1L << digit7BitOffset;
    private static final long digit7Overflow =    9L << digit7BitOffset;

    //                                         0001 1110 0000 0000 0000 0000 0000
    private static final int  digit8BitOffset = 21;
    private static final long digit8Mask =       15L << digit8BitOffset;
    private static final long digit8Increment =   1L << digit8BitOffset;
    private static final long digit8Overflow =   10L << digit8BitOffset;

    //                                    0001 1110 0000 0000 0000 0000 0000 0000
    private static final int  digit9BitOffset = 25;
    private static final long digit9Mask =       15L << digit9BitOffset;
    private static final long digit9Increment =   1L << digit9BitOffset;
    private static final long digit9Overflow =   11L << digit9BitOffset;

    //                               0001 1110 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit10BitOffset = 29;
    private static final long digit10Mask =      15L << digit10BitOffset;
    private static final long digit10Increment =  1L << digit10BitOffset;
    private static final long digit10Overflow =  12L << digit10BitOffset;

    //                          0001 1110 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit11BitOffset = 33;
    private static final long digit11Mask =      15L << digit11BitOffset;
    private static final long digit11Increment =  1L << digit11BitOffset;
    private static final long digit11Overflow =  13L << digit11BitOffset;

    //                     0001 1110 0000 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit12BitOffset = 37;
    private static final long digit12Mask =      15L << digit12BitOffset;
    private static final long digit12Increment =  1L << digit12BitOffset;
    private static final long digit12Overflow =  14L << digit12BitOffset;

    //                0001 1110 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit13BitOffset = 41;
    private static final long digit13Mask =      15L << digit13BitOffset;
    private static final long digit13Increment =  1L << digit13BitOffset;
    private static final long digit13Overflow =  15L << digit13BitOffset;

    //           0001 1110 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit14BitOffset = 45;
    private static final long digit14Mask =      15L << digit14BitOffset;
    private static final long digit14Increment =  1L << digit14BitOffset;

    //      0011 1110 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit15BitOffset = 49;
    private static final long digit15Mask =      31L << digit15BitOffset;
    private static final long digit15Increment =  1L << digit15BitOffset;
    private static final long digit15Overflow =  17L << digit15BitOffset;

    // 0111 1100 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
    private static final int  digit16BitOffset = 54;
    private static final long digit16Mask =      31L << digit16BitOffset;
    private static final long digit16Increment =  1L << digit16BitOffset;
    private static final long digit16Overflow =  18L << digit16BitOffset;

    private static final int maxDigits = 254;

    private static final int byteOverflow = 256;

    private static final int byteArrayRank = 17;


    /** first 17 digits packed into a long */
    long leastSignificantDigits;

    /** the remaining 238 digits, each represented as a byte */
    byte[] mostSignificantDigits;

    /**
     * estimate the total number of "base F" digits
     * representing the current state of this counter
     * @return
     */
    public int estimateDigits() {
        if (mostSignificantDigits == null)
            return byteArrayRank;

        return mostSignificantDigits.length + byteArrayRank;
    }

    /**
     * compute the rank of the most significant digit
     * in this counter
     * @return the 0-based rank of the most significant "base F" digit
     */
    public int mostSignificantDigit() {

        for (int rank = estimateDigits(); rank >= 0; rank--) {
            if (getDigit(rank) != 0)
                return rank;
        }

        return 0;
    }

    /** test the value of this counter for zero */
    public boolean isZero() {
        return (leastSignificantDigits == 0 && mostSignificantDigits == null);
    }

    /**
     * compute the rank of the least significant digit
     * in this counter
     * @return the 0-based rank of the least significant "base F" digit
     */
    public int leastSignificantDigit() {

        if (leastSignificantDigits == 0 && mostSignificantDigits == null)
            return 0;

        for (int rank = 0; rank < estimateDigits(); rank++)
            if (getDigit(rank) != 0)
                return rank;

        throw new IllegalStateException();
    }

    /**
     * set the value of this counter
     * @param value
     * @return this counter
     */
    public BaseFCounter set(long value) {

        long reduced = value;

        for (int rank = 0; rank < estimateDigits() || reduced != 0 && rank < maxDigits; rank++)
            reduced = setDigitInternal(reduced, rank);

        if (mostSignificantDigits == null)
            return this;

        if (mostSignificantDigit() < byteArrayRank)
            mostSignificantDigits = null;

        return this;
    }

    /**
     * Internal method to support the public <b>set()</b> method.
     * Computes and sets the value of the next digit.
     * @param value the value from which to extract the next digit
     * @param rank the rank of the digit whose value is to be set
     * @return the reduced value after the selected digit has been "removed"
     *        (the integer portion of the value divided by the highest-order
     *        factorial component corresponding to the next digit - this is
     *        simply the current digit's rank plus two)
     */
    long setDigitInternal(long value, int rank) {

        if (value == 0) {
            setDigit(rank, 0);
            return 0;
        }

        int factor = rank + 2;

        if (value < factor) {
            setDigit(rank, value);
            return 0;
        }

        int digitValue = (int) (value % factor);

        setDigit(rank, digitValue);

        return value / factor;
    }

    /**
     * increment the value of this counter by one
     */
    public void increment() {
        leastSignificantDigits++;

        // digit 0 is single bit with max value of 1 so it overflows naturally into digit 1

        if (leastSignificantDigits < digit1Overflow)
            return;

        if ((leastSignificantDigits & digit1Mask) == digit1Overflow)
            leastSignificantDigits += (digit2Increment - digit1Overflow);

        // digit 2 is two bits with max value of 3 so it overflows naturally into digit 3

        if (leastSignificantDigits < digit3Overflow)
            return;

        if ((leastSignificantDigits & digit3Mask) == digit3Overflow)
            leastSignificantDigits += (digit4Increment - digit3Overflow);

        if (leastSignificantDigits < digit4Overflow)
            return;

        if ((leastSignificantDigits & digit4Mask) == digit4Overflow)
            leastSignificantDigits += (digit5Increment - digit4Overflow);

        if (leastSignificantDigits < digit5Overflow)
            return;

        if ((leastSignificantDigits & digit5Mask) == digit5Overflow)
            leastSignificantDigits += (digit6Increment - digit5Overflow);

        // digit 6 is three bits with max value of 7 so it overflows naturally into digit 7

        if (leastSignificantDigits < digit7Overflow)
            return;

        if ((leastSignificantDigits & digit7Mask) == digit7Overflow)
            leastSignificantDigits += (digit8Increment - digit7Overflow);

        if (leastSignificantDigits < digit8Overflow)
            return;

        if ((leastSignificantDigits & digit8Mask) == digit8Overflow)
            leastSignificantDigits += (digit9Increment - digit8Overflow);

        if (leastSignificantDigits < digit9Overflow)
            return;

        if ((leastSignificantDigits & digit9Mask) == digit9Overflow)
            leastSignificantDigits += (digit10Increment - digit9Overflow);

        if (leastSignificantDigits < digit10Overflow)
            return;

        if ((leastSignificantDigits & digit10Mask) == digit10Overflow)
            leastSignificantDigits += (digit11Increment - digit10Overflow);

        if (leastSignificantDigits < digit11Overflow)
            return;

        if ((leastSignificantDigits & digit11Mask) == digit11Overflow)
            leastSignificantDigits += (digit12Increment - digit11Overflow);

        if (leastSignificantDigits < digit12Overflow)
            return;

        if ((leastSignificantDigits & digit12Mask) == digit12Overflow)
            leastSignificantDigits += (digit13Increment - digit12Overflow);

        if (leastSignificantDigits < digit13Overflow)
            return;

        if ((leastSignificantDigits & digit13Mask) == digit13Overflow)
            leastSignificantDigits += (digit14Increment - digit13Overflow);

        // digit 14 is four bits with max value of 15 so it overflows naturally into digit 15

        if (leastSignificantDigits < digit15Overflow)
            return;

        if ((leastSignificantDigits & digit15Mask) == digit15Overflow)
            leastSignificantDigits += (digit16Increment - digit15Overflow);

        if ((leastSignificantDigits & digit16Mask) == digit16Overflow) {
            leastSignificantDigits &= ~digit16Mask;

            incrementByteArray();
        }
    }

    /**
     * increment the value of this counter's byte array
     * (following overflow of the "leastSignificantDigits" register)
     */
    void incrementByteArray() {

        if (mostSignificantDigits == null)
            mostSignificantDigits = new byte[1];

        for (int index = 0; index < mostSignificantDigits.length; index++) {

            if (++mostSignificantDigits[index] < index + byteArrayRank + 2)
                return;

            mostSignificantDigits[index] -= index + byteArrayRank + 2;

            if (index + 1 >= mostSignificantDigits.length && mostSignificantDigits.length < maxDigits - byteArrayRank)
                mostSignificantDigits = Arrays.copyOf(mostSignificantDigits, mostSignificantDigits.length + 1);
        }
    }

    /**
     * set the value of the given digit
     * @param rank the 0-based rank of the digit whose value is to be set
     * @param value the value to assign to the specified digit
     *              (must be zero or a positive value that is
     *              less than or equal to <b>rank + 1</b>)
     */
    public void setDigit(int rank, long value) {

        inRange(rank, 0, maxDigits, "rank");
        inRange(value, 0, rank+1, "value");

        switch (rank) {
            case 0:
                leastSignificantDigits |= (value & digit0Mask);
                break;

            case 1:
                leastSignificantDigits |= ((value << digit1BitOffset) & digit1Mask);
                break;

            case 2:
                leastSignificantDigits |= ((value << digit2BitOffset) & digit2Mask);
                break;

            case 3:
                leastSignificantDigits |= ((value << digit3BitOffset) & digit3Mask);
                break;

            case 4:
                leastSignificantDigits |= ((value << digit4BitOffset) & digit4Mask);
                break;

            case 5:
                leastSignificantDigits |= ((value << digit5BitOffset) & digit5Mask);
                break;

            case 6:
                leastSignificantDigits |= ((value << digit6BitOffset) & digit6Mask);
                break;

            case 7:
                leastSignificantDigits |= ((value << digit7BitOffset) & digit7Mask);
                break;

            case 8:
                leastSignificantDigits |= ((value << digit8BitOffset) & digit8Mask);
                break;

            case 9:
                leastSignificantDigits |= ((value << digit9BitOffset) & digit9Mask);
                break;

            case 10:
                leastSignificantDigits |= ((value << digit10BitOffset) & digit10Mask);
                break;

            case 11:
                leastSignificantDigits |= ((value << digit11BitOffset) & digit11Mask);
                break;

            case 12:
                leastSignificantDigits |= ((value << digit12BitOffset) & digit12Mask);
                break;

            case 13:
                leastSignificantDigits |= ((value << digit13BitOffset) & digit13Mask);
                break;

            case 14:
                leastSignificantDigits |= ((value << digit14BitOffset) & digit14Mask);
                break;

            case 15:
                leastSignificantDigits |= ((value << digit15BitOffset) & digit15Mask);
                break;

            case 16:
                leastSignificantDigits |= ((value << digit16BitOffset) & digit16Mask);
                break;

            default:

                int index = rank - byteArrayRank;

                if (mostSignificantDigits == null)
                    mostSignificantDigits = new byte[1];

                if (this.mostSignificantDigits.length <= index)
                    this.mostSignificantDigits = Arrays.copyOf(this.mostSignificantDigits, index + 1);

                this.mostSignificantDigits[index] = (byte) value;
        }
    }

    /**
     * get the value of the specified digit
     * @param rank the zero-based rank of the digit whose value is to be retrieved
     * @return
     */
    public int getDigit(int rank) {

        inRange(rank, 0, maxDigits, "rank");

        switch (rank) {
            case 0:
                return (int) (leastSignificantDigits & digit0Mask);

            case 1:
                return (int) (leastSignificantDigits & digit1Mask) >> digit1BitOffset;

            case 2:
                return (int) (leastSignificantDigits & digit2Mask) >> digit2BitOffset;

            case 3:
                return (int) (leastSignificantDigits & digit3Mask) >> digit3BitOffset;

            case 4:
                return (int) (leastSignificantDigits & digit4Mask) >> digit4BitOffset;

            case 5:
                return (int) (leastSignificantDigits & digit5Mask) >> digit5BitOffset;

            case 6:
                return (int) (leastSignificantDigits & digit6Mask) >> digit6BitOffset;

            case 7:
                return (int) (leastSignificantDigits & digit7Mask) >> digit7BitOffset;

            case 8:
                return (int) (leastSignificantDigits & digit8Mask) >> digit8BitOffset;

            case 9:
                return (int) (leastSignificantDigits & digit9Mask) >> digit9BitOffset;

            case 10:
                return (int) ((leastSignificantDigits & digit10Mask) >> digit10BitOffset);

            case 11:
                return (int) ((leastSignificantDigits & digit11Mask) >> digit11BitOffset);

            case 12:
                return (int) ((leastSignificantDigits & digit12Mask) >> digit12BitOffset);

            case 13:
                return (int) ((leastSignificantDigits & digit13Mask) >> digit13BitOffset);

            case 14:
                return (int) ((leastSignificantDigits & digit14Mask) >> digit14BitOffset);

            case 15:
                return (int) ((leastSignificantDigits & digit15Mask) >> digit15BitOffset);

            case 16:
                return (int) ((leastSignificantDigits & digit16Mask) >> digit16BitOffset);

            default:

                int index = rank - byteArrayRank;

                if (mostSignificantDigits == null)
                    return 0;

                if (this.mostSignificantDigits.length <= index)
                    return 0;

                int value = this.mostSignificantDigits[index];

                if (value < 0)
                    value = byteOverflow - value;

                return value;
        }
    }

}
