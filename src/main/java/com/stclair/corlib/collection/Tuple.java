package com.stclair.corlib.collection;

public class Tuple<A, B> {

    Object[] values;

    public Tuple(A a, B b) {
        values = new Object[] { a, b };
    }

    public A getA() {
        return (A) values[0];
    }

    public B getB() {
        return (B) values[1];
    }
}

