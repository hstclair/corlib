package com.stclair.corlib.math.vector;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.stclair.corlib.validation.Validation.equalTo;
import static com.stclair.corlib.validation.Validation.inRange;
import static com.stclair.corlib.validation.Validation.neverNull;

public class DoubleVector {

    double[] values;

    public DoubleVector(double... values) {
        this.values = neverNull(values, "values").clone();
    }

    public double dotProduct(DoubleVector that) {

        return IntStream.range(0, Math.min(this.order(), that.order()))
                        .mapToDouble(it -> this.element(it) * that.element(it))
                        .sum();
    }

    public DoubleVector crossProduct(DoubleVector that) {

        equalTo(this.order(), 3, "vector1.order()");
        equalTo(that.order(), 3, "vector2.order()");

        return new DoubleVector(
                crossTerm(that, 1, 2),
                crossTerm(that, 2, 0),
                crossTerm(that, 0, 1)
            );
    }

    double crossTerm(DoubleVector that, int rank1, int rank2) {
        return this.element(rank1) * that.element(rank2) - this.element(rank2) * that.element(rank1);
    }

    public boolean isZero() {
        for (double element: this.values)
            if (element != 0)
                return false;

        return true;
    }

    public boolean isOrthonormal() {
        int ones = 0;
        int nonZero = 0;

        for (double element : this.values) {
            if (element == 1)
                ones++;
            else if (element != 0)
                nonZero++;
        }

        return ones == 1 && nonZero == 0;
    }

    public DoubleVector normalize() {

        double magnitude = magnitude();

        if (magnitude == 0)
            throw new IllegalArgumentException("cannot normalize the zero vector");

        return new DoubleVector(
                DoubleStream.of(values)
                .map(it -> it/magnitude)
                .toArray()
        );
    }

    public boolean isNormal() {
        return magnitude() == 1;
    }

    public double magnitude() {

        double sumOfSquares = DoubleStream.of(values)
                .map( x -> x*x)
                .sum();

        return Math.sqrt(sumOfSquares);
    }

    public DoubleVector sum(DoubleVector that) {

        neverNull(that, "that");

        return new DoubleVector(
                IntStream.range(0, Math.min(this.order(), that.order()))
                .mapToDouble(it -> this.element(it) + that.element(it))
                .toArray()
        );
    }

    public DoubleVector product(double multiplicand) {
        return new DoubleVector(
                IntStream.range(0, this.order())
                        .mapToDouble(it -> this.element(it) * multiplicand)
                        .toArray()
        );
    }


    public int order() {
        return values.length;
    }

    public double element(int index) {

        inRange(index,0, order() - 1, "index");

        return values[index];
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (obj == this)
            return true;

        if (! (obj instanceof DoubleVector))
            return false;

        DoubleVector that = (DoubleVector) obj;

        if (this.order() != that.order())
            return false;

        return IntStream.range(0, order())
                .mapToObj(index -> this.element(index) == that.element(index))
                .reduce(true, (a, b) -> a && b);
    }
}
