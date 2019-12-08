package com.stclair.corlib.math.array;

public interface Indexor<T> {

    Array2D<T> getSource();

    int getColumn();

    int getRow();

    T getValue();
}
