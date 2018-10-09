package com.stclair.corlib.math.polynomial.generic;


import com.stclair.corlib.math.util.OperationStrategy;

/**
 * @author hstclair
 * @since 8/20/15 6:21 PM
 */
public class LocalMaxQuadraticLowerBound<T> {

    LocalMaxQuadraticUpperBound<T> upperBound = new LocalMaxQuadraticUpperBound<>();

    public LocalMaxQuadraticLowerBound() {
    }

    public T estimateLowerBound(Polynomial<T> polynomial) {

        OperationStrategy<T> op = polynomial.op;

        T[] coefficients = polynomial.getCoefficients().clone();

        for (int low = 0, high = coefficients.length - 1; low < high; low++, high--) {
            T tmp = coefficients[low];
            coefficients[low] = coefficients[high];
            coefficients[high] = tmp;
        }

        Polynomial<T> reversed = Polynomial.of(op, coefficients);

        T ub = upperBound.estimateUpperBound(reversed);

        return op.quotient(op.one(), ub);
    }
}
