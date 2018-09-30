package com.stclair.corlib.validation;

public interface Validation {

    static <X> X neverNull(X x, String name) {

        if (x == null)
            throw new IllegalArgumentException(String.format("Argument %s must not be null", name));

        return x;
    }

    static double inRange(double x, double min, double max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static float inRange(float x, float min, float max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static long inRange(long x, long min, long max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static int inRange(int x, int min, int max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static short inRange(short x, short min, short max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }


    static byte inRange(byte x, byte min, byte max, String name) {

        if (x < min)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is below minimum value of %3$s", name, x, min));

        if (x > max)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is above maximum value of %3$s", name, x, max));

        return x;
    }

    static double equalTo(double x, double requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static float equalTo(float x, float requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static byte equalTo(byte x, byte requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static short equalTo(short x, short requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static int equalTo(int x, int requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static long equalTo(long x, long requiredValue, String name) {
        if (x != requiredValue)
            throw new IllegalArgumentException(String.format("Argument %1$s has value %2$s, which is not equal to required value of %3$s", name, x, requiredValue));

        return x;
    }

    static <T, U> T requireInstanceOf(U that, Class<T> type, String name) {

        neverNull(that, name);

        if (! (type.isInstance(that)))
            throw new IllegalArgumentException(String.format("Argument %1$s is of type %2$s, and is not an instance of %3$s", name, that.getClass().getName(), type.getName()));

        return type.cast(that);
    }
}
