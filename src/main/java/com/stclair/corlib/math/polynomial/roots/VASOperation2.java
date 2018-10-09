package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.Interval;
import com.stclair.corlib.math.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.Polynomial;

public class VASOperation2 {

    public final boolean complete;

    public final VASOperation2 ancestor;

    public final Polynomial polynomial;

    public final double strzebońskiAlpha;

    public final double lowerBoundComputed;

    public final RealMobiusTransformation mobius;

    public final Interval root;

    public final String annotation;

    VASOperation2(VASOperation2 ancestor, Polynomial polynomial, RealMobiusTransformation mobius, double strzebońskiAlpha, double lowerBoundComputed, Interval root, boolean complete, String annotation) {
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

    public VASOperation2(Polynomial polynomial, RealMobiusTransformation mobius) {
        this(null, polynomial, mobius, 0, 0, null, false, "initial operation");
    }

    public VASOperation2 createOperationMessage(Polynomial polynomial, RealMobiusTransformation mobius, String annotation) {
        return new VASOperation2(this, polynomial, mobius, strzebońskiAlpha, 0, null, false, annotation);
    }

    public VASOperation2 createOperationMessage(Polynomial polynomial, RealMobiusTransformation mobius, double lowerBoundComputed, String annotation) {
        return new VASOperation2(this, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, null, false, annotation);
    }

    public VASOperation2 createResultMessage(Interval root, String annotation) {

        if (this.root != null)
            return this;

        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true, annotation);
    }

    public VASOperation2 createLowerBoundMessage(double lowerBoundComputed) {
        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, false, annotation);
    }

    public VASOperation2 createCompletionMessage(String annotation) {

        if (complete)
            return this;

        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true, this.annotation + '\n' + annotation);
    }

}

