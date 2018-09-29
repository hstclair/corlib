package com.stclair.corlib.validation;

public interface Validation {

    static <X> X neverNull(X x, String name) {

        if (x == null)
            throw new IllegalArgumentException(String.format("Argument %s must not be null", name));

        return x;
    }

    static double inRange(double x, double min, double max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static float inRange(float x, float min, float max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static int inRange(int x, int min, int max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static short inRange(short x, short min, short max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is above maximum value of %3$s", name, x, max));

        return x;
    }


    static byte inRange(byte x, byte min, byte max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s which is above maximum value of %3$s", name, x, max));

        return x;
    }
}
