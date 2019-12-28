package com.stclair.corlib.math.array;

import com.stclair.corlib.math.util.OperationStrategy;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.stclair.corlib.validation.Validation.inRange;

public class Array2DConcrete<T> implements Array2D<T> {

    final Class<T> elementClass;

    final T[] elements;

    final int width;
    final int height;

    final OperationStrategy<T> operationStrategy;

    public Array2DConcrete(OperationStrategy<T> operationStrategy, int size) {
        this(operationStrategy, size, size);
    }

    public Array2DConcrete(OperationStrategy<T> operationStrategy, int width, int height) {
        this.width = width;
        this.height = height;

        this.operationStrategy = operationStrategy;

        elements = operationStrategy.array(width * height);
        elementClass = operationStrategy.getElementClass();
    }

    public Array2DConcrete(OperationStrategy<T> operationStrategy, int width, int height, Function<Indexor<T>, T> initializer) {
        this.operationStrategy = operationStrategy;
        this.width = width;
        this.height = height;

        elements = map(operationStrategy, initializer, width, height);
        elementClass = operationStrategy.getElementClass();
    }

    public <K> Array2DConcrete(OperationStrategy<T> operationStrategy, Array2D<K> source, Function<Indexor<K>, T> mapFunction) {

        width = source.getWidth();
        height = source.getHeight();

        this.operationStrategy = operationStrategy;

        elements = map(operationStrategy, source, mapFunction);

        elementClass = operationStrategy.getElementClass();
    }

    @Override
    public Class<T> getElementClass() {
        return operationStrategy.getElementClass();
    }

    @Override
    public OperationStrategy<T> getOperationStrategy() {
        return operationStrategy;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public T[] getRow(int row) {

        return Arrays.copyOfRange(elements, row * getWidth(), (row+1) * getWidth());
    }

    @Override
    public T[] getColumn(int column) {

        T[] result = operationStrategy.array(getHeight());

        int srcIndex = column;

        for (int dstIndex = 0; dstIndex < getHeight(); dstIndex++, column += getWidth())
            result[dstIndex] = elements[srcIndex];

        return result;
    }

    @Override
    public T get(int column, int row) {

        inRange(column, 0, getWidth() - 1, "column");
        inRange(row, 0, getHeight() - 1, "row");

        int index = row * getWidth() + column;

        return elements[index];
    }

    @Override
    public void traverse(Consumer<Indexor<T>> elementConsumer) {
        for (int column = 0; column < getWidth(); column++) {
            for (int row = 0; row < getHeight(); row++) {
                int currentColumn = column;
                int currentRow = row;

                Indexor<T> indexor = new Indexor<>() {

                    @Override
                    public Array2D<T> getSource() {
                        return null;
                    }

                    @Override
                    public int getColumn() {
                        return currentColumn;
                    }

                    @Override
                    public int getRow() {
                        return currentRow;
                    }

                    @Override
                    public T getValue() {
                        return elements[currentColumn + currentRow * getWidth()];
                    }
                };

                elementConsumer.accept(indexor);
            }
        }
    }

    public static <K, T> T[] map(OperationStrategy<T> operationStrategy, Array2D<K> source, Function<Indexor<K>, T> mapFunction) {

        Function<Indexor<T>, T> innerMapFunction = tIndexor -> {

            int currentColumn = tIndexor.getColumn();
            int currentRow = tIndexor.getRow();

            Indexor<K> indexor = new Indexor<K>() {
                @Override
                public Array2D<K> getSource() {
                    return source;
                }

                @Override
                public int getColumn() {
                    return currentColumn;
                }

                @Override
                public int getRow() {
                    return currentRow;
                }

                @Override
                public K getValue() {
                    return source.get(currentColumn, currentRow);
                }
            };

            return mapFunction.apply(indexor);
        };

        return map(operationStrategy, innerMapFunction, source.getWidth(), source.getHeight());
    }

    public static <T> T[] map(OperationStrategy<T> operationStrategy, Function<Indexor<T>, T> mapFunction, int width, int height) {

        T[] elements = null;

        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {

                int currentColumn = column;
                int currentRow = row;

                Indexor<T> indexor = new Indexor<>() {

                    @Override
                    public Array2D<T> getSource() {
                        return null;
                    }

                    @Override
                    public int getColumn() {
                        return currentColumn;
                    }

                    @Override
                    public int getRow() {
                        return currentRow;
                    }

                    @Override
                    public T getValue() {
                        return null;
                    }
                };

                T value = mapFunction.apply(indexor);

                if (elements == null)
                    elements = operationStrategy.array(height * width);

                elements[currentColumn + currentRow * width] = value;
            }
        }

        return elements;
    }
}
