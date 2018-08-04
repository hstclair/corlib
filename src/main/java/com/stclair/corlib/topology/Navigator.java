package com.stclair.corlib.topology;

import java.util.List;

@FunctionalInterface
public interface Navigator<T> {

    List<T> neighbors(T t);
}
