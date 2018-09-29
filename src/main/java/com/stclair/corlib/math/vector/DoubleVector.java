package com.stclair.corlib.math.vector;

import static com.stclair.corlib.validation.Validation.inRange;
import static com.stclair.corlib.validation.Validation.neverNull;

public class DoubleVector {

    double[] values;

    public DoubleVector(double... values) {
        this.values = neverNull(values, "values").clone();
    }

    public double dotProduct(DoubleVector that) {

        double result = 0;

        int maxOrder = Math.max(this.order(), that.order());

        for (int index = 0; index < maxOrder; index++)
            result += this.element(index) * that.element(index);

        return result;
    }

    public DoubleVector crossProduct(DoubleVector that) {

        inRange(this.order(), 3,3, "order(v1)");
        inRange(that.order(), 3, 3, "order(v2)");

        return new DoubleVector(
                crossTerm(that, 1, 2),
                crossTerm(that, 0, 2),
                crossTerm(that, 0, 1)
            );
    }

    double crossTerm(DoubleVector that, int rank1, int rank2) {
        return this.element(rank1) * that.element(rank2) + this.element(rank2) * that.element(rank1);
    }


    public int order() {
        return values.length;
    }

    public double element(int index) {

        inRange(index,0, order() - 1, "index");

        return values[index];
    }
}
