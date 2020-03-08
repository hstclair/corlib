package com.stclair.corlib.geometry;

import java.util.Arrays;

import static com.stclair.corlib.validation.Validation.inRange;
import static com.stclair.corlib.validation.Validation.neverNull;

public class Point<T> {

    private T[] coordinates;

    public Point(T... coordinates) {

        neverNull(coordinates, "coordinates");

        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
    }

    public int getDimension() {
        return coordinates.length;
    }

    public T getX() {

        if (coordinates.length < 1)
            throw new IllegalStateException("Point has no X coordinate");

        return coordinates[0];
    }

    public T getY() {

        if (coordinates.length < 2)
            throw new IllegalStateException("Point has no Y coordinate");

        return coordinates[1];
    }

    public T getZ() {

        if (coordinates.length < 3)
            throw new IllegalStateException("Point has no Z coordinate");

        return coordinates[2];
    }

    public T getCoordinate(int index) {

        inRange(index, 0, coordinates.length - 1, "index");

        return coordinates[index];
    }

    public T[] copyCoordinates() {
        return Arrays.copyOf(coordinates, coordinates.length);
    }
}
