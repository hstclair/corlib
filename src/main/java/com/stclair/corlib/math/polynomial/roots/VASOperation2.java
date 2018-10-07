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

    VASOperation2(VASOperation2 ancestor, Polynomial polynomial, RealMobiusTransformation mobius, double strzebońskiAlpha, double lowerBoundComputed, Interval root, boolean complete) {
        this.ancestor = ancestor;
        this.polynomial = polynomial;
        this.mobius = mobius;
        this.strzebońskiAlpha = strzebońskiAlpha;
        this.lowerBoundComputed = lowerBoundComputed;
        this.root = root;
        this.complete = complete;
    }

    public VASOperation2(Polynomial polynomial, RealMobiusTransformation mobius) {
        this(null, polynomial, mobius, 0, 0, null, false);
    }

    public VASOperation2 createOperationMessage(Polynomial polynomial, RealMobiusTransformation mobius) {
        return new VASOperation2(this, polynomial, mobius, strzebońskiAlpha, 0, null, false);
    }

    public VASOperation2 createOperationMessage(Polynomial polynomial, RealMobiusTransformation mobius, double lowerBoundComputed) {
        return new VASOperation2(this, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, null, false);
    }

    public VASOperation2 createResultMessage(Interval root) {

        if (this.root != null)
            return this;

        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true);
    }

    public VASOperation2 createLowerBoundMessage(double lowerBoundComputed) {
        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, false);
    }

    public VASOperation2 createCompletionMessage() {

        if (complete)
            return this;

        return new VASOperation2(ancestor, polynomial, mobius, strzebońskiAlpha, lowerBoundComputed, root, true);
    }

}

