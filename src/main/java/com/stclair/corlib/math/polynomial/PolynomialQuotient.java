package com.stclair.corlib.math.polynomial;

/**
 * @author hstclair
 * @since 4/9/16 3:16 PM
 */
public class PolynomialQuotient {

    public Polynomial quotient;

    public Polynomial remainder;

    public PolynomialQuotient(Polynomial quotient, Polynomial remainder) {
        this.quotient = quotient;
        this.remainder = remainder;
    }
}
