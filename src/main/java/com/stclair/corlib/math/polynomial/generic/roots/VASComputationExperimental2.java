package com.stclair.corlib.math.polynomial.generic.roots;

import com.stclair.corlib.math.polynomial.generic.Interval;
import com.stclair.corlib.math.polynomial.generic.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.generic.LocalMaxQuadraticLowerBound;
import com.stclair.corlib.math.polynomial.generic.Polynomial;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hstclair
 * @since 8/22/15 3:33 PM
 */
public class VASComputationExperimental2<T> {

    final LocalMaxQuadraticLowerBound<T> lowerBoundEstimator = new LocalMaxQuadraticLowerBound<>();

    OperationStrategy<T> op;

    T defaultStrzebońskiAlpha;

    T strzebońskiAlpha;

    Polynomial<T> xPlusOne;

    public BiFunction<Consumer<VASOperation2<T>>, VASOperation2<T>, VASOperation2<T>> processorFunction;

    public VASComputationExperimental2(OperationStrategy<T> op, T strzebońskiAlpha) {

        this.op = op;

        this.xPlusOne = Polynomial.of(op, coefficients(op.one(), op.one()));

        this.strzebońskiAlpha = strzebońskiAlpha;

        this.processorFunction = (Consumer<VASOperation2<T>> registerFn, VASOperation2<T> vasOp) ->
                    constantTest.apply(registerFn)
                    .andThen(verifySignChanges.apply(registerFn))
                    .andThen(estimateLowerBound.apply(registerFn))
//                    .andThen(applyStrzebońskiAlpha.apply(fn))
                    .andThen(testLowerBound.apply(registerFn))
                    .andThen(generateOperations.apply(registerFn))
                    .apply(vasOp);
    }

    public VASComputationExperimental2(OperationStrategy<T> op) {
        this(op, op.from(4));
    }

    T[] coefficients(T a, T b) {
        T[] coefficients = op.array(2);

        coefficients[0] = b;
        coefficients[1] = a;

        return coefficients;
    }

    public VASOperation2<T> processPolynomial(Polynomial<T> polynomial) {
        return new VASOperation2<>(polynomial, RealMobiusTransformation.identity(polynomial.getOperationStrategy()));
    }

    public VASOperation2<T> processOperation(Consumer<VASOperation2<T>> registerFn, VASOperation2<T> op) {
        return processorFunction.apply(registerFn, op);
    }

    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> constantTest = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

        if (! op.isZero(vasOp.polynomial.constant()))
            return vasOp;

        // if the constant portion of the current polynomial is zero, we have found one of the roots and
        // it's value is precisely M(0).   Add this root to the list then reduce the degree of the polynomial by
        // dividing by X (of course, this will never result in a non-zero a/x term because we've already determined the
        // constant term to be zero).

        vasOp = vasOp.createCompletionMessage("constant is zero - root found");

        // add [b/d, b/d] to rootlist,
        Interval<T> root = new Interval<>(vasOp.mobius.transform(op.zero()), op);

        int lowestDegree = vasOp.polynomial.lowestDegree();

        registerNewEntry.accept(vasOp.createResultMessage(root, "found precise value of root"));

        if (lowestDegree > 0) {
            // and set p(x) ← p(x)/x
            registerNewEntry.accept(vasOp.createOperationMessage(vasOp.polynomial.reduceDegree(lowestDegree), vasOp.mobius, "not fully solved after precise root"));
        }

        return vasOp;
    };

    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> verifySignChanges = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

        // Count the sign changes in our polynomial.  There must be at least one sign change if the polynomial has
        // any remaining positive roots.
        int signChanges = vasOp.polynomial.signChanges();

        if (signChanges > 1)
            return vasOp;

        vasOp = vasOp.createCompletionMessage("found zero or one remaining sign changes");

        if (signChanges != 0)
            registerNewEntry.accept(vasOp.createResultMessage(intervalOf(vasOp.mobius), "single sign change - at least one root in range"));

        return vasOp;
    };

    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> estimateLowerBound = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

        // Compute a lower bound α ∈ Z on the positive roots of p.
        return vasOp.createLowerBoundMessage(lowerBound(vasOp.polynomial));
    };


    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> applyStrzebońskiAlpha = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

        // if the lower bound of the polynomial's roots is greater than the polynomial's constant term then scale the
        // polynomial (and the associated Mobius Transformation) so that this lower bound coincides with x=1
        //
        //  If α > α0 set p(x) ← p(αx), a ← αa, c ← αc, and α ← 1
        if (op.lessThan(vasOp.lowerBoundComputed, strzebońskiAlpha))
            return vasOp;

        // Strzeboński's contribution

        Polynomial<T> polynomial = vasOp.polynomial.apply(Polynomial.of(op, coefficients(vasOp.lowerBoundComputed, op.zero())));
        RealMobiusTransformation<T> mobius = vasOp.mobius.composeAlphaX(vasOp.lowerBoundComputed);

        return vasOp.createOperationMessage(polynomial, mobius, op.one(), "scaled polynomial per Strzeboński");
    };

    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> testLowerBound = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

//        // Compute a lower bound α ∈ Z on the positive roots of p.
//        double lowerBoundComputed = lowerBound(frame.polynomial);

        if (op.lessThan(vasOp.lowerBoundComputed, op.one()))
            return vasOp;

        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
        // the lower bound now coincides with zero
        //
        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
        Polynomial<T> composed = Polynomial.of(op, coefficients(op.one(), vasOp.lowerBoundComputed));

        Polynomial<T> newPolynomial = vasOp.polynomial.apply(composed);
        RealMobiusTransformation<T> newMobius = vasOp.mobius.composeXPlusK(vasOp.lowerBoundComputed);

        vasOp = vasOp.createCompletionMessage("lower bound >= 1");

        registerNewEntry.accept(vasOp.createOperationMessage(newPolynomial, newMobius, "composed with 'y = x + lowerBound' so that lower bound is now 0"));

        return vasOp;
    };

    public Function<Consumer<VASOperation2<T>>, Function<VASOperation2<T>, VASOperation2<T>>> generateOperations = registerNewEntry -> vasOp -> {

        if (vasOp.complete)
            return vasOp;

        // prior transformations ensure there are now zero or more roots in (0, 1) and zero or more roots in (1, infinity)
        // (5)
        // Prepare for identification of roots in interval (1, infinity):
        // Use Taylor shift polynomial so that roots within (1, infinity) now lie within interval (0, infinity)
        // then re-apply VAS
        // Compute p1(x) ← p(x + 1)
        Polynomial<T> polynomial1 = vasOp.polynomial.apply(xPlusOne);

        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
        RealMobiusTransformation<T> mobius1 = vasOp.mobius.composeXPlusK(op.one());

        // Prepare for identification of roots within interval (0, 1)
        // Use Budan's Theorem to transform polynomial then re-apply VAS
        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
        RealMobiusTransformation<T> mobius2 = vasOp.mobius.budansTheorem();
        Polynomial<T> polynomial2 = vasOp.polynomial.budansTheorem();

        vasOp = vasOp.createCompletionMessage("one or more roots in (0-1) and one or more roots in (1-infinity)");

        registerNewEntry.accept(vasOp.createOperationMessage(polynomial1, mobius1, "Taylor-shifted to place roots in (1 - infinity) in new range (0 - infinity)"));
        registerNewEntry.accept(vasOp.createOperationMessage(polynomial2, mobius2, "Budan's theorem applied - roots in original (0-1) range are now bounded by number of sign changes"));

        return vasOp;
    };


    T lowerBound(Polynomial<T> polynomial) {
        return lowerBoundEstimator.estimateLowerBound(polynomial);
    }

    T idealLowerBound(Polynomial<T> polynomial) {
        return lowerBound(polynomial);
    }

    Interval<T> intervalOf(RealMobiusTransformation<T> mobius) {
        return new Interval<T>(op.min(mobius.transform(op.zero()), mobius.transform(op.positiveInfinity())), op.max(mobius.transform(op.zero()), mobius.transform(op.positiveInfinity())), op);
    }
}
