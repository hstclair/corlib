package com.stclair.corlib.math.polynomial.generic;


import com.stclair.corlib.math.util.OperationStrategy;

/**
 * @author hstclair
 * @since 8/10/15 9:32 PM
 */
public class RealMobiusTransformation<T> {

    // The Mobius Transformation represents the operation:
    // M(x) = (ax + b) / (cx + d)
    // where ad - bc is not zero

    final OperationStrategy<T> op;
    final T a;
    final T b;
    final T c;
    final T d;


    RealMobiusTransformation(T a, T b, T c, T d, OperationStrategy<T> op) {
        if (! validate(a, b, c, d, op))
            throw new IllegalArgumentException("ad - bc must be non-zero");

        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        this.op = op;
    }

    static <T> boolean validate(T a, T b, T c, T d, OperationStrategy<T> op) {
        T ad = op.product(a, d);
        T bc = op.product(b, c);

        return ! ad.equals(bc);
    }

    static public <T> RealMobiusTransformation<T> identity(OperationStrategy<T> op) {
        return new RealMobiusTransformation<>(op.one(), op.zero(), op.zero(), op.one(), op);
    }

    public T transform(T x) {
        if (op.isPositiveInfinity(x)) {
            if (op.isZero(c))
                return op.positiveInfinity();

            return op.quotient(a, c);
        }

        return op.quotient(op.sum(op.product(a, x), b), op.sum(op.product(c, x), d));
    }

    public RealMobiusTransformation<T> budansTheorem() {
        return new RealMobiusTransformation<>(b, op.sum(a, b), d, op.sum(c, d), op);
    }

    public RealMobiusTransformation<T> composeXPlusK(T k) {
        return new RealMobiusTransformation<>(a, op.sum(op.product(a, k), b), c, op.sum(op.product(c, k), d), op);
    }

    public RealMobiusTransformation<T> composeAlphaX(T alpha) {
        return new RealMobiusTransformation<>(op.product(alpha, a), b, op.product(alpha, c), d, op);
    }

    T[] coefficients(T a, T b) {
        T[] coefficients = op.array(2);

        coefficients[0] = b;
        coefficients[1] = a;

        return coefficients;
    }

    public String toString() {

        Polynomial<T> numerator = Polynomial.of(op, coefficients(a, b));
        Polynomial<T> denominator = Polynomial.of(op, coefficients(d, c));

        return String.format("(%s) / (%s)", numerator, denominator);
    }
}
