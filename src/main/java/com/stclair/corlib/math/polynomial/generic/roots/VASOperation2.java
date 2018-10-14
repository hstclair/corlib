package com.stclair.corlib.math.polynomial.generic.roots;

import com.stclair.corlib.math.polynomial.generic.Interval;
import com.stclair.corlib.math.polynomial.generic.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.generic.Polynomial;

public class VASOperation2<T> {

    public final boolean complete;

    public final VASOperation2<T> ancestor;

    public final Polynomial<T> polynomial;

    public final T strzebońskiAlpha;

    public final T lowerBoundComputed;

    public final RealMobiusTransformation<T> mobius;

    public final Interval<T> root;

    public final String annotation;

    VASOperation2(VASOperation2<T> ancestor, Polynomial<T> polynomial, RealMobiusTransformation<T> mobius, T strzebońskiAlpha, T lowerBoundComputed, Interval<T> root, boolean complete, String annotation) {
        this.ancestor = ancestor;
        this.polynomial = polynomial;
        this.mobius = mobius;
        this.strzebońskiAlpha = strzebońskiAlpha;
        this.lowerBoundComputed = lowerBoundComputed;
        this.root = root;
        this.complete = complete;
        this.annotation = annotation;
    }

    @Override
    public String toString() {

        if (root != null)
            return root.toString();

        return String.format("polynomial=%1$s mobius=%2$s (lowerbound=%3$s)\n%4$s\n", polynomial, mobius, lowerBoundComputed, annotation);
    }

    public VASOperation2(Polynomial<T> polynomial, RealMobiusTransformation<T> mobius) {
        this(null, polynomial, mobius, polynomial.getOperationStrategy().zero(), null, null, false, "initial operation");
    }

    public VASOperation2<T> createOperationMessage(Polynomial<T> polynomial, RealMobiusTransformation<T> mobius, String annotation) {
        return new VASOperation2<>(this, polynomial, mobius, strzebońskiAlpha, null, null, false, annotation);
    }

    public VASOperation2<T> createOperationMessage(Polynomial<T> polynomial, RealMobiusTransformation<T> mobius, T lowerBoundComputed, String annotation) {
        return new VASOperation2<>(this, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, null, false, annotation);
    }

    public VASOperation2<T> createResultMessage(Interval<T> root, String annotation) {

        if (this.root != null)
            return this;

        return new VASOperation2<>(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true, annotation);
    }

    public VASOperation2<T> createLowerBoundMessage(T lowerBoundComputed) {
        return new VASOperation2<>(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, false, annotation);
    }

    public VASOperation2<T> createCompletionMessage(String annotation) {

        if (complete)
            return this;

        return new VASOperation2<>(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true, this.annotation + '\n' + annotation);
    }

}

