package com.stclair.corlib.math.util;

public class MoreMath {

    public static long factorial(long n) {
        long current = n;
        long result = current;

        while (--current > 1)
            result *= current;

        return result;
    }
}
