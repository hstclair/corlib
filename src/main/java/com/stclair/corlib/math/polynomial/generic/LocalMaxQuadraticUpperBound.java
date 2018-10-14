package com.stclair.corlib.math.polynomial.generic;

import com.stclair.corlib.math.util.OperationStrategy;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * This class implements the "Local Max" Quadratic algorithm for providing an upper bound estimate
 * for the roots of a polynomial
 *
 * @author hstclair
 * @since 8/16/15 10:58 AM
 */
public class LocalMaxQuadraticUpperBound<T> {

    /**
     * constructor
     *
     */
    public LocalMaxQuadraticUpperBound() { }


    public T estimateUpperBound(Polynomial<T> polynomial) {
        Objects.requireNonNull(polynomial);

        OperationStrategy<T> op = polynomial.op;

        if (polynomial.degree() < 2)
            throw new IllegalArgumentException("polynomial degree must be at least 2");

        if (op.isNegative(polynomial.getCoefficients()[polynomial.degree()]))
            polynomial = polynomial.negate();

        if (polynomial.signChanges() == 0)
            throw new IllegalArgumentException("polynomial must contain at least one negative coefficient");

        Optional<Supplier<T>>[] decomposedPositiveCoefficients = decomposePositiveCoefficients(op, polynomial.coefficients);

        return computeEstimate(op, polynomial.degree(), polynomial.getCoefficients(), decomposedPositiveCoefficients);
    }

    T computeEstimate(OperationStrategy<T> op, int degree, T[] coefficients, Optional<Supplier<T>>[] decomposedPositiveCoefficients) {
        T estimate = null;

        for (int index = degree - 1; index >= 0; index--) {
            if (op.isNegative(coefficients[index])) {
                if (estimate == null)
                    estimate = estimateNegativeCoefficient(op, index, coefficients, decomposedPositiveCoefficients);
                else
                    estimate = op.max(estimate, estimateNegativeCoefficient(op, index, coefficients, decomposedPositiveCoefficients));

            }
        }

        return estimate;
    }

    T estimateNegativeCoefficient(OperationStrategy<T> op, int negativeCoefficientIndex, T[] coefficients, Optional<Supplier<T>>[] decomposedPositiveCoefficients) {
        T estimate = null;

        int degree = coefficients.length - 1;

        for (int index = negativeCoefficientIndex + 1; index <= degree; index++) {
            if (decomposedPositiveCoefficients[index].isPresent()) {
                if (estimate == null)
                    estimate = estimateCoefficientPair(op, negativeCoefficientIndex, index, coefficients, decomposedPositiveCoefficients);
                else
                    estimate = op.min(estimate, estimateCoefficientPair(op, negativeCoefficientIndex, index, coefficients, decomposedPositiveCoefficients));
            }
        }

        return estimate;
    }

    /**
     * Compute the radical for a pairing of a negative coefficient with a positive coefficient
     *
     * @param negativeCoefficientIndex the array index of the negative coefficient
     * @param positiveCoefficientIndex the array index of the positive coefficient
     * @return the "Local Max" Quadratic radical for the paired coefficients
     */
    T estimateCoefficientPair(OperationStrategy<T> op, int negativeCoefficientIndex, int positiveCoefficientIndex, T[] coefficients, Optional<Supplier<T>>[] decomposedPositiveCoefficients) {

        return op.pow(op.quotient(op.negate(coefficients[negativeCoefficientIndex]), decomposedPositiveCoefficients[positiveCoefficientIndex].get().get()), op.from(positiveCoefficientIndex - negativeCoefficientIndex));
    }

    public Optional<Supplier<T>>[] decomposePositiveCoefficients(OperationStrategy<T> op, T[] coefficients) {
        Optional<Supplier<T>>[] result = new Optional[coefficients.length];

        for (int index = 0; index < coefficients.length; index++) {
            if (op.isNegative(coefficients[index]) || op.isZero(coefficients[index]))
                result[index] = Optional.empty();
            else
                result[index] = Optional.of(new DecomposedCoefficientSupplier<T>(op, coefficients[index]));
        }

        return result;
    }
}
