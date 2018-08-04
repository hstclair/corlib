package com.stclair.corlib.validation;

public interface Validation {

    static <X> X neverNull(X x, String name) {

        if (x == null)
            throw new IllegalArgumentException(String.format("Argument %s must not be null", name));

        return x;
    }
}
