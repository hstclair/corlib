package com.stclair.corlib.math.polynomial.generic;


import com.stclair.corlib.math.Complex;
import com.stclair.corlib.math.util.OperationStrategy;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a Polynomial in positive integer exponents
 *
 * @author hstclair
 * @since 8/16/15 1:59 PM
 */
public class Polynomial<T> {

    final OperationStrategy<T> op;

    T[] coefficients;

    /**
     * @param coefficients the coefficients of the polynomial alpha 1 * X ^ k1 + alpha 2 * X ^ k2 + ...
     *                     organized such that:
     *                     1. coefficients[0] = alpha max(k1,k2,k3...) -- <i>the "first" element is the leading coefficient</i>
     *                     2. <i>each</i> coefficient is represented, including <b>0</b> value coefficients
     */
    Polynomial(OperationStrategy<T> op, T[] coefficients) {

        this.op = op;

        this.coefficients = trimCoefficients(op, coefficients);
    }

//    /**
//     * Construct a polynomial passing through the supplied points (this is NOT a least squares fit)
//     * @param x the set of X values
//     * @param y the set of Y values
//     */
//    Polynomial(OperationStrategy<T>, T[] x, T[] y) {
//
//    }

    Polynomial(OperationStrategy<T> op, T coefficient) {

        this.op = op;

        if (op.isZero(coefficient))
            coefficients = op.array(0);
        else {
            coefficients = op.array(1);
            coefficients[0] = coefficient;
        }
    }

    public OperationStrategy<T> getOperationStrategy() {
        return op;
    }



    public Polynomial<T> product(Polynomial<T> multiplicand) {

        Objects.requireNonNull(op);
        Objects.requireNonNull(multiplicand);

        if (isIdentity())
            return multiplicand;

        if (multiplicand.isIdentity())
            return this;

        if (this.isZero() || multiplicand.isZero())
            return zero();

        if (degree() == 0)
            return multiplicand.product(coefficients[0]);

        if (multiplicand.degree() == 0)
            return product(multiplicand.getCoefficients()[0]);

        T[] newCoefficients = op.array(degree() + multiplicand.degree() + 1);

        for (int innerTerm = 0; innerTerm <= degree(); innerTerm++) {
            for (int outerTerm = 0; outerTerm <= multiplicand.degree(); outerTerm++)
                newCoefficients[innerTerm + outerTerm] = op.sum(newCoefficients[innerTerm + outerTerm], op.product(coefficients[innerTerm], multiplicand.coefficients[outerTerm]));
        }

        return Polynomial.of(op, newCoefficients);
    }

    private Polynomial<T> zero() {
        return Polynomial.of(op, op.array(0));
    }

    private Polynomial<T> one() {

        T[] coefficients = op.array(1);
        coefficients[0] = op.one();

        return Polynomial.of(op, coefficients);
    }

    public boolean isZero() {
        return coefficients.length == 0;
    }

    public boolean isIdentity() {
        return coefficients.length == 1 && op.isOne(coefficients[0]);
    }

    public Polynomial<T> product(T multiplicand) {
        if (op.isZero(multiplicand))
            return zero();

        if (op.isOne(multiplicand))
            return this;

        if (this.coefficients.length == 0)
            return zero();

        if (this.isIdentity())
            return Polynomial.of(op, multiplicand);

        T[] newCoefficients = op.array(degree() + 1);

        for (int index = 0; index <= degree(); index++) {
            newCoefficients[index] = op.product(coefficients[index], multiplicand);
        }

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> divide(T divisor) {
        if (op.isZero(divisor))
            throw new IllegalArgumentException("Cannot divide by zero");

        if (op.isOne(divisor))
            return this;

        T[] newCoefficients = op.array(degree() + 1);

        for (int index = 0; index <= degree(); index++) {
            newCoefficients[index] = op.quotient(coefficients[index], divisor);
        }

        return Polynomial.of(op, newCoefficients);
    }

    public PolynomialQuotient<T> quotient(Polynomial<T> divisor) {
        if (divisor.isZero())
            throw new IllegalArgumentException("Cannot divide by Zero");

        if (divisor.isIdentity())
            new PolynomialQuotient<T>(this, zero());

        if (divisor.degree() > this.degree())
            return new PolynomialQuotient<T>(zero(), this);

        Polynomial<T> remainder = this;

        T[] quotient = op.array(degree() - divisor.degree() + 1);

        while (remainder.degree() >= divisor.degree()) {
            T divisorTerm = op.quotient(remainder.leadingCoefficient(), divisor.leadingCoefficient());

            quotient[remainder.degree() - divisor.degree()] = divisorTerm;

            remainder = remainder.difference(divisor.product(divisorTerm).degree(remainder.degree()));
        }

        return new PolynomialQuotient<T>(Polynomial.of(op, quotient), remainder);
    }

    public Polynomial<T> sum(Polynomial<T> addend) {
        Objects.requireNonNull(addend);

        if (addend.isZero())
            return this;

        if (this.isZero())
            return addend;

        T[] newCoefficients = op.array(Math.max(degree(), addend.degree()) + 1);

        if (degree() + 1 >= 0) System.arraycopy(coefficients, 0, newCoefficients, 0, degree() + 1);

        for (int index = 0; index <= addend.degree(); index++) {
            newCoefficients[index] = op.sum(newCoefficients[index], addend.coefficients[index]);
        }

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> sum(T addend) {
        if (this.isZero())
            return Polynomial.of(op, addend);

        T[] newCoefficients = coefficients.clone();

        newCoefficients[0] = op.sum(newCoefficients[0], addend);

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> difference(Polynomial<T> subtrahend) {
        Objects.requireNonNull(subtrahend);

        if (subtrahend.isZero())
            return this;

        if (this.isZero())
            return subtrahend.negate();

        T[] newCoefficients = op.array(Math.max(degree(), subtrahend.degree()) + 1);

        if (degree() + 1 >= 0) System.arraycopy(coefficients, 0, newCoefficients, 0, degree() + 1);

        for (int index = 0; index <= subtrahend.degree(); index++) {
            newCoefficients[index] = op.difference(newCoefficients[index], subtrahend.coefficients[index]);
        }

        return Polynomial.of(op, newCoefficients);
    }

    /**
     * return a polynomial constructed by raising this polynomial to the specified power
     *
     * @param exponent
     * @return
     */
    public Polynomial<T> power(int exponent) {

        if (exponent < 0)
            throw new IllegalArgumentException("exponent must be positive");

        if (exponent == 0)
            return one();

        if (exponent == 1)
            return this;

        Polynomial<T> value = this;

        for (int count = 1; count < exponent; count++) {
            value = value.product(this);
        }

        return value;
    }

    /**
     * return the additive inverse of this polynomial
     * @return
     */
    public Polynomial<T> negate() {
        T[] coefficients = this.coefficients.clone();

        for (int index = 0; index < coefficients.length; index++)
            coefficients[index] = op.negate(coefficients[index]);

        return Polynomial.of(op, coefficients);
    }

    /**
     * Apply this polynomial to a value
     *
     * @param x
     * @return
     */
    public T apply(T x) {
        T result;

        if (this.isZero())
            return op.zero();

        if (op.isZero(x) || degree() == 0)
            return coefficients[0];

        if (op.isOne(x))
            return sumOfCoefficients();

        // use Horner's Rule:
        result = op.product(coefficients[degree()], x);

        for (int index = degree() - 1; index > 0; index--) {
            result = op.product(op.sum(result, coefficients[index]), x);
        }

        result = op.sum(result, coefficients[0]);

        return result;
    }

    /**
     * Apply this polynomial to a complex number.
     *
     * @param complex
     * @return
     */
    public Complex apply(Complex complex) {

        Objects.requireNonNull(complex);

        if (this.isZero())
            return Complex.ZERO;

        if (complex == Complex.ZERO || degree() == 0)
            return Complex.of(op.value(coefficients[0]));

        if (complex == Complex.ONE)
            return Complex.of(op.value(sumOfCoefficients()));

        // use Horner's Rule:
        Complex result = complex.product(op.value(coefficients[degree()]));

        for (int index = degree() - 1; index > 0; index--) {
            result = result.sum(op.value(coefficients[index])).product(complex);
        }

        return result.sum(op.value(coefficients[0]));
    }

    /**
     * Apply this polynomial to another polynomial expression.
     * This will effectively return a polynomial representing the substitution of <i>expression</i> for the variable
     * in the polynomial.
     *
     * e.g. given  <b>f(x) = x^2 + 2x + 1</b> and a substitute expression of <b>x - 1</b>, the result will be:
     *   <b>f(x) = x^2 + 1 + 2x -2 + 1</b>  <i>or</i>
     *   <b>f(x) = x^2 + 2x</b>
     *
     * @param polynomial
     * @return
     */
    public Polynomial<T> apply(Polynomial<T> polynomial) {
        Objects.requireNonNull(polynomial);

        if (this.isZero())
            return this;

        if (polynomial.isZero() || degree() == 0)
            return Polynomial.of(op, coefficients[0]);

        if (polynomial.isIdentity())
            return Polynomial.of(op, sumOfCoefficients());

        // use Horner's Rule:
        Polynomial<T> result = polynomial.product(coefficients[degree()]);

        for (int index = degree() - 1; index > 0; index--) {
            result = result.sum(coefficients[index]).product(polynomial);
        }

        return result.sum(coefficients[0]);
    }

    T sumOfCoefficients() {
        T result = op.zero();

        for (T coefficient : coefficients)
            result = op.sum(result, coefficient);

        return result;
    }

    public Polynomial<T> derivative() {
        if (degree() <= 0)
            return zero();

        T[] newCoefficients = op.array(coefficients.length - 1);

        for (int index = 0; index < newCoefficients.length; index++) {
            newCoefficients[index] = op.product(coefficients[index+1], op.from(index+1));
        }

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> integral() {
        if (degree() < 0)
            return zero();

        T[] newCoefficients = op.array(coefficients.length + 1);

        for (int index = 1; index < newCoefficients.length; index++) {
            newCoefficients[index] = op.quotient(coefficients[index-1], op.from(index));
        }

        return Polynomial.of(op, newCoefficients);
    }

    public static <T> T[] sumCoefficients(OperationStrategy<T> op, T[] a, T[] b) {

        if (a.length < b.length) {

            T[] tmp;

            // ensure that a is the longest coefficients array

            tmp = b;
            b = a;
            a = tmp;
        }


        T[] result = a.clone();

        for (int index = 0; index < b.length; index++)
            result[index] = op.sum(a[index], b[index]);

        return result;
    }

    public static <T> Polynomial<T> sigma(OperationStrategy<T> op, Function<Integer, Polynomial<T>> function, int rangeStart, int rangeEnd, int increment) {
        Objects.requireNonNull(function);

        if (increment <= 0)
            throw new IllegalArgumentException("Increment must be positive");

        List<Polynomial<T>> resultList = new LinkedList<>();

        if (rangeStart > rangeEnd)
            return Polynomial.of(op, op.array(0));

        for (int argument = rangeStart; argument <= rangeEnd; argument += increment) {
            resultList.add(function.apply(argument));
        }

        if (resultList.size() == 0)
            return Polynomial.of(op, op.array(0));

        T[] result = resultList
                .stream()
                .map(it -> it.coefficients)
                .reduce(op.array(0), (a, b) -> sumCoefficients(op, a, b));

        return Polynomial.of(op, result);
    }

    public static <T> Polynomial<T> sigma(OperationStrategy<T> op, Function<Integer, Polynomial<T>> function, int rangeStart, int rangeEnd) {
        return sigma(op, function, rangeStart, rangeEnd, 1);
    }

    public static <T> Polynomial<T> sigma(OperationStrategy<T> op, Function<Integer, Polynomial<T>> function, int rangeEnd) {
        return sigma(op, function, 0, rangeEnd, 1);
    }

    /**
     * returns a polynomial representing (x+1)^n
     * @param degree
     * @return
     */
    public static <T> Polynomial<T> pascal(OperationStrategy<T> op, int degree) {
        if (degree == 0) {

            T[] coefficients = op.array(1);
            coefficients[0] = op.one();

            return Polynomial.of(op, coefficients);
        }

        if (degree < 0) throw new IllegalArgumentException("degree must be positive");

        T[] coefficients = op.array(degree+1);

        coefficients[0] = op.one();

        for (int index = 0; index < degree; index++) {
            coefficients[index+1] = op.quotient(op.product(coefficients[index], op.difference(op.from(degree), op.from(index))), op.from(index+1));
        }

        return Polynomial.of(op, coefficients);
    }

    /**
     * Transform Polynomial to test for roots in interval (0, 1) according to Budan's Theorem
     *
     * returns the equivalent of:
     *
     *   f(1/(x+1)) * (x + 1)^degree(f(x))
     *
     *   Budan's Theorem (https://en.wikipedia.org/wiki/Budan%27s_theorem#Early_applications_of_Budan.27s_theorem) states that
     *   if a polynomial is transformed in this manner, the number of roots within interval (0, 1) of the original
     *   polynomial is bounded by the number of sign changes (Descartes Rule of Signs).
     *
     * @return
     */
    public Polynomial<T> budansTheorem() {
        Function<Integer, Polynomial<T>> function = (k) -> pascal(op, degree() - k).product(coefficients[k]);

        return Polynomial.sigma(op, function, degree());
    }

    public T leadingCoefficient() {
        if (this.isZero())
            return op.zero();

        return coefficients[degree()];
    }

    public T[] getCoefficients() {
        return coefficients;
    }

    public int degree() {
        return coefficients.length - 1;
    }

    public T constant() {
        if (this.isZero())
            return op.zero();       // should this instead be undefined???  Based on the rest of implementation, I think ZERO is correct...

        return coefficients[0];
    }

    public int lowestDegree() {
        int lowestDegree = 0;

        while (lowestDegree < coefficients.length && op.isZero(coefficients[lowestDegree]))
            lowestDegree++;

        if (lowestDegree == coefficients.length)
            return 0;

        return lowestDegree;
    }

    public Polynomial<T> degree(int newDegree) {
        if (newDegree < 0)
            throw new IllegalArgumentException("Negative degree");

        if (newDegree == 0)
            return Polynomial.of(op, leadingCoefficient());

        if (newDegree < degree())
            return reduceDegree(degree() - newDegree);
        else
            return increaseDegree(newDegree - degree());

    }

    public Polynomial<T> increaseDegree(int increase) {
        if (increase < 0)
            throw new IllegalArgumentException("Increase by negative amount");

        if (increase == 0)
            return this;

        T[] newCoefficients = op.array(degree() + increase + 1);

        System.arraycopy(coefficients, 0, newCoefficients, increase, degree() + 1);

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> reduceDegree(int reduction) {
        if (reduction < 0)
            throw new IllegalArgumentException("Reduction by negative amount");

        if (reduction == 0)
            return this;

        if (degree() <= reduction)
            return zero();

        T[] newCoefficients = Arrays.copyOfRange(coefficients, reduction, coefficients.length);

        return Polynomial.of(op, newCoefficients);
    }

    public Polynomial<T> reduceDegree() {
        return reduceDegree(1);
//
//        if (degree() <= 0)
//            return ZERO;
//
//        double[] newCoefficients = Arrays.copyOfRange(coefficients, 1, coefficients.length);
//
//        return Polynomial.of(newCoefficients);
    }

    /**
     * Compute the number of sign changes using Descartes' Rule of Signs
     *
     * @return the number of sign changes in this polynomial
     */
    public int signChanges() {
        int count = -1;
        T lastSign = op.zero();

        for (T coefficient : coefficients) {
            if ((op.isZero(coefficient)) || (op.isNegative(lastSign) && op.isNegative(coefficient)) || (op.isPositive(lastSign) && op.isPositive(coefficient)))
                continue;

            count++;

            lastSign = coefficient;
        }

        return Math.max(count, 0);
    }

    public static <T> Polynomial<T> fromRoots(OperationStrategy<T> op, T[] roots) {
        Polynomial<T> result = Polynomial.of(op, op.one());

        for (T root : roots) {
            T[] coefficients = op.array(2);

            coefficients[0] = op.negate(root);
            coefficients[1] = op.one();

            result = result.product(Polynomial.of(op, coefficients));
        }

        return result;
    }

    public static <T> Polynomial<T> of(OperationStrategy<T> op, T coefficient) {
        if (op.isZero(coefficient))
            return new Polynomial<>(op, op.array(0));

        T[] coefficients = op.array(1);

        coefficients[0] = coefficient;

        return new Polynomial<>(op, coefficients);
    }

    public static <T> Polynomial<T> of(OperationStrategy<T> op, T[] coefficients) {
        Objects.requireNonNull(coefficients);

        coefficients = trimCoefficients(op, coefficients);

        if (coefficients.length == 0)
            return new Polynomial<>(op, op.array(0));

        return new Polynomial<>(op, coefficients);
    }

//    public static T[] sumOfCoefficientArrays(double[][] coefficientArrays) {
//        Objects.requireNonNull(coefficientArrays);
//
//        int length = 0;
//
//        for (int index = 0; index < coefficientArrays.length; index++) {
//            length = Math.max(length, coefficientArrays[index].length);
//        }
//
//        double[] newCoefficients = new double[length];
//
//        for (int termIndex = 0; termIndex < newCoefficients.length; termIndex++) {
//            double sum = 0;
//
//            for (int addendIndex = 0; addendIndex < coefficientArrays.length; addendIndex++) {
//                if (coefficientArrays[addendIndex].length > termIndex)
//                    sum += coefficientArrays[addendIndex][termIndex];
//            }
//
//            newCoefficients[termIndex] = sum;
//        }
//
//        return newCoefficients;
//    }

    public static <T> T[] trimCoefficients(OperationStrategy<T> op, T[] coefficients) {
        Objects.requireNonNull(coefficients);

        int zeros = 0;

        if (coefficients.length == 0)
            return coefficients;

        if (! op.isZero(coefficients[coefficients.length - 1]))
            return coefficients;

        for (int index = coefficients.length - 1; index >= 0; index--) {
            if (op.isZero(coefficients[index]))
                zeros++;
            else
                break;
        }

        if (zeros == coefficients.length)
            return op.array(0);

        return Arrays.copyOf(coefficients, coefficients.length - zeros);
    }

    public static <T> Polynomial<T> identity(OperationStrategy<T> op) {
        T[] coefficients = op.array(1);

        coefficients[0] = op.one();

        return new Polynomial<T>(op, coefficients);
    }

    public static <T> Polynomial<T> zero(OperationStrategy<T> op) {
        T[] coefficients = op.array(0);

        return new Polynomial<T>(op, coefficients);
    }

    @Override
    public String toString() {
        if (this.isZero())
            return "0";

        DecimalFormat integerFormat = new DecimalFormat();
        integerFormat.setMaximumFractionDigits(0);

        DecimalFormat floatFormat = new DecimalFormat();
        floatFormat.setMaximumFractionDigits(17);
        floatFormat.setMinimumFractionDigits(0);

        StringBuilder sb = new StringBuilder();

        boolean first = true;

        for (int degree = coefficients.length - 1; degree >= 0; degree--) {
            T coefficient = coefficients[degree];

            if (op.isZero(coefficient))
                continue;


            if (first) {
                if (op.lessThan(coefficient, op.zero()))
                    sb.append("-");
            } else {
                if (op.greaterThanOrEqual(coefficient, op.zero())) {
                    sb.append(" + ");
                } else
                    sb.append(" - ");
            }

            coefficient = op.abs(coefficient);

            if ((! op.isOne(coefficient)) || degree == 0) {
                if (op.floor(coefficient).equals(coefficient))
                    sb.append(integerFormat.format(coefficient));
                else
                    sb.append(floatFormat.format(coefficient));
            }

            if (degree > 1)
                sb.append(String.format("X^%s", degree));
            else if (degree == 1)
                sb.append(String.format("X"));

            first = false;
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coefficients);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other.getClass() != Polynomial.class)
            return false;

        Polynomial otherPoly = (Polynomial) other;

        if (coefficients == otherPoly.coefficients)
            return true;

        return Arrays.equals(otherPoly.getCoefficients(), getCoefficients());
    }

}
