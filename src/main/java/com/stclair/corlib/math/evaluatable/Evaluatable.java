package com.stclair.corlib.math.evaluatable;

public interface Evaluatable<T> {

    boolean requiresEvaluation();

    Class<T> getReturnType();

    T evaluate();

    T getValue();

    boolean valueIsSumIdentity();

    boolean valueIsProductIdentity();
}
