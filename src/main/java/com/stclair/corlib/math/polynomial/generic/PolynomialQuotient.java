package com.stclair.corlib.math.polynomial.generic;

/**
 * @author hstclair
 * @since 4/9/16 3:16 PM
 */
public class PolynomialQuotient<T> {

    public Polynomial<T> quotient;

    public Polynomial<T> remainder;

    public PolynomialQuotient(Polynomial<T> quotient, Polynomial<T> remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }
}
