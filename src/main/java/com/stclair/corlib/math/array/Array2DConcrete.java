package com.stclair.corlib.math.array;

import java.lang.reflect.Array;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.stclair.corlib.validation.Validation.inRange;

public class Array2DConcrete<T> implements Array2D<T> {

    final Class<T> elementClass;

    final T[] elements;

    final int width;
    final int height;

    public Array2DConcrete(Class<T> cls, int size) {
        this(cls, size, size);
    }

    public Array2DConcrete(Class<T> cls, int width, int height) {
        this.width = width;
        this.height = height;

        elements = buildArray(cls, width * height);
        elementClass = cls;
    }

    public Array2DConcrete(int width, int height, Function<Indexor<T>, T> initializer) {
        this.width = width;
        this.height = height;

        elements = map(initializer, width, height);
        elementClass = getElementClass(elements);
    }

    public <K> Array2DConcrete(Array2D<K> source, Function<Indexor<K>, T> mapFunction) {

        width = source.getWidth();
        height = source.getHeight();
        elements = map(source, mapFunction);

        elementClass = getElementClass(elements);
    }

    @Override
    public Class<T> getElementClass() {
        return elementClass;
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
    public T get(int x, int y) {

        inRange(x, 0, width - 1, "x");
        inRange(y, 0, height - 1, "y");

        int index = y * width + x;

        return elements[index];
    }

    @Override
    public void traverse(Consumer<Indexor<T>> elementConsumer) {
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
                        return elements[currentColumn + currentColumn * width];
                    }
                };

                elementConsumer.accept(indexor);
            }
        }
    }

    public static <K, T> T[] map(Array2D<K> source, Function<Indexor<K>, T> mapFunction) {

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

        return map(innerMapFunction, source.getWidth(), source.getHeight());
    }

    public static <T> T[] map(Function<Indexor<T>, T> mapFunction, int width, int height) {

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

                T result = mapFunction.apply(indexor);

                Class<T> elementClass = getClass(result);

                if (elements == null)
                    elements = buildArray(elementClass, height * width);
            }
        }

        return elements;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getElementClass(T[] instance) {
        return (Class<T>) instance.getClass().getComponentType();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(T instance) {
        return (Class<T>) instance.getClass();
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] buildArray(Class<T> cls, int size) {
        return (T[]) Array.newInstance(cls, size);
    }
}
