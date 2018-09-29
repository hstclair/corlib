package com.stclair.corlib.math.vector;

import com.stclair.corlib.math.evaluatable.Evaluatable;

import static com.stclair.corlib.validation.Validation.*;

public class EvaluatableVector<T extends Evaluatable<U>, U> implements Evaluatable<EvaluatableVector<T, U>> {

    Evaluatable<T>[] members;

    public EvaluatableVector(Evaluatable<T>[] members) {
        this.members = members; // TODO: clone?
    }

    public Evaluatable<T> getMember(int rank) {

        inRange(rank, 1, getOrder(), "rank");

        return members[rank];
    }

    public void setMember(int rank, Evaluatable<T> value) {

        inRange(rank, 1, getOrder(), "rank");

        members[rank] = value;
    }

    public int getOrder() {
        return members.length;
    }

    @Override
    public boolean requiresEvaluation() {

        for (Evaluatable<T> evaluatable : members)
            if (evaluatable.requiresEvaluation())
                return true;

        return false;
    }

    @Override
    public Class<EvaluatableVector<T, U>> getReturnType() {
        return null;
    }

    @Override
    public EvaluatableVector<T, U> evaluate() {

        for (Evaluatable<T> evaluatable : members)
            evaluatable.evaluate();

        return this;
    }

    @Override
    public EvaluatableVector<T, U> getValue() {
        return this;
    }

    @Override
    public boolean valueIsSumIdentity() {

        for (Evaluatable<T> evaluatable : members)
            if (! evaluatable.valueIsSumIdentity())
                return false;

        return true;
    }

    @Override
    public boolean valueIsProductIdentity() {

        for (Evaluatable<T> evaluatable : members)
            if (! evaluatable.valueIsProductIdentity())
                return false;

        return true;
    }

//    public EvaluatableVector<T, U> crossProduct(EvaluatableVector<T, U> that) {
//
//    }
//
//    public T dotProduct(EvaluatableVector<T, U> that) {
//
//    }
}
