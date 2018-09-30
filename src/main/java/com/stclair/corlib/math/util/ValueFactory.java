package com.stclair.corlib.math.util;

import com.stclair.corlib.math.matrix.Value;

/**
 * Created by hstclair on 4/22/17.
 */
public interface ValueFactory<T> {
    Value<T> valueOf(double value);

    Value<T> valueOfOne();

    Value<T> valueOfZero();

    Value<T>[] vectorArray(int length);

    Value<T>[][] matrixArray(int rows, int columns);
}
