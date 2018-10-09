package com.stclair.corlib.math.polynomial.generic;

import com.stclair.corlib.math.util.OperationStrategy;

import java.util.function.Supplier;

/**
 * @author hstclair
 * @since 8/16/15 11:38 AM
 */
public class DecomposedCoefficientSupplier<T> implements Supplier<T> {

    OperationStrategy<T> op;

    T coefficient;

    int timesUsed;

    DecomposedCoefficientSupplier(OperationStrategy<T> op, T coefficient) {
        this.coefficient = coefficient;
        this.op = op;
        timesUsed = 0;
    }

    @Override
    public T get() {
        timesUsed++;

        return op.quotient(coefficient, op.from (1 << timesUsed));
    }
}
