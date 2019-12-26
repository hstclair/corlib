package com.stclair.corlib.math.array;

import com.stclair.corlib.math.util.OperationStrategy;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Array2D<T> {

    Class<T> getElementClass();

    T get(int x, int y);

    int getWidth();

    int getHeight();

    void traverse(Consumer<Indexor<T>> elementConsumer);

    OperationStrategy<T> getOperationStrategy();
}
