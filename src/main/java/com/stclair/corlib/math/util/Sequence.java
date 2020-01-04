package com.stclair.corlib.math.util;

public interface Sequence<T> {

    T nextMember();

    T prevMember();

    T currentMember();

    long currentIndex();

    T getMember(int n);
}
