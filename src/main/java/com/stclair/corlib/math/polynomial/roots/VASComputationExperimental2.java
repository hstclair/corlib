package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.Interval;
import com.stclair.corlib.math.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.LocalMaxQuadraticLowerBound;
import com.stclair.corlib.math.polynomial.Polynomial;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author hstclair
 * @since 8/22/15 3:33 PM
 */
public class VASComputationExperimental2 {
    static final LocalMaxQuadraticLowerBound lowerBoundEstimator = new LocalMaxQuadraticLowerBound();
    static final Polynomial xPlusOne = Polynomial.of(new double[] { 1, 1 });
    static final double defaultStrzebońskiAlpha = 4;

    public double strzebońskiAlpha = defaultStrzebońskiAlpha;

    public BiFunction<Consumer<VASOperation2>, VASOperation2, VASOperation2> processorFunction;

    public VASComputationExperimental2(double strzebońskiAlpha) {
        this.strzebońskiAlpha = strzebońskiAlpha;

        this.processorFunction = (Consumer<VASOperation2> registerFn, VASOperation2 op) ->
                    constantTest.apply(registerFn)
                    .andThen(verifySignChanges.apply(registerFn))
                    .andThen(estimateLowerBound.apply(registerFn))
//                    .andThen(applyStrzebońskiAlpha.apply(fn))
                    .andThen(testLowerBound.apply(registerFn))
                    .andThen(generateOperations.apply(registerFn))
                    .apply(op);
    }

    public VASComputationExperimental2() {
        this(defaultStrzebońskiAlpha);
    }

    public VASOperation2 processOperation(Consumer<VASOperation2> registerFn, VASOperation2 op) {
        return processorFunction.apply(registerFn, op);
    }

    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> constantTest = registerNewEntry -> frame -> {

        if (frame.complete)
            return frame;

        if (frame.polynomial.constant() != 0)
            return frame;

        // if the constant portion of the current polynomial is zero, we have found one of the roots and
        // it's value is precisely M(0).   Add this root to the list then reduce the degree of the polynomial by
        // dividing by X (of course, this will never result in a non-zero a/x term because we've already determined the
        // constant term to be zero).

        frame = frame.createCompletionMessage();

        // add [b/d, b/d] to rootlist,
        Interval root = new Interval(frame.mobius.transform(0));

        int lowestDegree = frame.polynomial.lowestDegree();

        registerNewEntry.accept(frame.createResultMessage(root));

        if (lowestDegree > 0) {
            // and set p(x) ← p(x)/x
            registerNewEntry.accept(frame.createOperationMessage(frame.polynomial.reduceDegree(lowestDegree), frame.mobius));
        }

        return frame;
    };

    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> verifySignChanges = registerNewEntry -> frame -> {

        if (frame.complete)
            return frame;

        // Count the sign changes in our polynomial.  There must be at least one sign change if the polynomial has
        // any remaining positive roots.
        int signChanges = frame.polynomial.signChanges();

        if (signChanges > 1)
            return frame;

        frame = frame.createCompletionMessage();

        if (signChanges != 0)
            registerNewEntry.accept(frame.createResultMessage(intervalOf(frame.mobius)));

        return frame;
    };

    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> estimateLowerBound = registerNewEntry -> frame -> {

        // Compute a lower bound α ∈ Z on the positive roots of p.
        return frame.createLowerBoundMessage(lowerBound(frame.polynomial));
    };


    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> applyStrzebońskiAlpha = registerNewEntry -> frame -> {

        // if the lower bound of the polynomial's roots is greater than the polynomial's constant term then scale the
        // polynomial (and the associated Mobius Transformation) so that this lower bound coincides with x=1
        //
        //  If α > α0 set p(x) ← p(αx), a ← αa, c ← αc, and α ← 1
        if (frame.lowerBoundComputed < strzebońskiAlpha)
            return frame;

        // Strzeboński's contribution

//        System.out.printf("Lower bound is greater than %f.  Transforming polynomial so that lower bound is exactly 1\n", strzebońskiAlpha);

        Polynomial polynomial = frame.polynomial.apply(Polynomial.of(new double[] { 0, frame.lowerBoundComputed }));
        RealMobiusTransformation mobius = frame.mobius.composeAlphaX(frame.lowerBoundComputed);

        return frame.createOperationMessage(polynomial, mobius, 1);
    };

    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> testLowerBound = registerNewEntry -> frame -> {

//        // Compute a lower bound α ∈ Z on the positive roots of p.
//        double lowerBoundComputed = lowerBound(frame.polynomial);

        if (frame.lowerBoundComputed < 1)
            return frame;

        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
        // the lower bound now coincides with zero
        //
        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
        Polynomial composed = Polynomial.of(new double[]{frame.lowerBoundComputed, 1});

        Polynomial newPolynomial = frame.polynomial.apply(composed);
        RealMobiusTransformation newMobius = frame.mobius.composeXPlusK(frame.lowerBoundComputed);

        frame = frame.createCompletionMessage();

        registerNewEntry.accept(frame.createOperationMessage(newPolynomial, newMobius));

        return frame;
    };

    public Function<Consumer<VASOperation2>, Function<VASOperation2, VASOperation2>> generateOperations = registerNewEntry -> frame -> {

        // prior transformations ensure there are now one or more roots in (0, 1) and one or more roots in (1, infinity)
        // (5)
        // Prepare for identification of roots in interval (1, infinity):
        // Use Taylor shift polynomial so that roots within (1, infinity) now lie within interval (0, infinity)
        // then re-apply VAS
        // Compute p1(x) ← p(x + 1)
        Polynomial polynomial1 = frame.polynomial.apply(xPlusOne);

        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
        RealMobiusTransformation mobius1 = frame.mobius.composeXPlusK(1);

        // Prepare for identification of roots within interval (0, 1)
        // Use Budan's Theorem to transform polynomial then re-apply VAS
        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
        RealMobiusTransformation mobius2 = frame.mobius.budansTheorem();
        Polynomial polynomial2 = frame.polynomial.budansTheorem();

        frame.createCompletionMessage();

        registerNewEntry.accept(frame.createOperationMessage(polynomial1, mobius1));
        registerNewEntry.accept(frame.createOperationMessage(polynomial2, mobius2));

        return frame;
    };

//    public List<VASOperation> evaluate() {
//        List<Interval> roots = new LinkedList<>();
//        List<VASOperation> operations = new LinkedList<>();
//
//        Polynomial polynomial = this.polynomial;
//        RealMobiusTransformation mobius = this.mobius;
//
//        // Operations:
//        // (1) Reduce Degree if Const == 0
//        // (2) Test Sign Changes
//        // (3) Adjust to Lower Bound
//        // (4) Align Lower Bound With X == 0
//        // (5) Bisect at X==1
//
//        // (1)
//        // if the constant portion of the current polynomial is zero, we have found one of the roots and
//        // it's value is precisely M(0).   Add this root to the list then reduce the degree of the polynomial by
//        // dividing by X (of course, this will never result in a non-zero a/x term because we've already determined the
//        // constant term to be zero).
//        if (polynomial.constant() == 0) {
//            // add [b/d, b/d] to rootlist,
//            roots.add(new Interval(mobius.transform(0)));
//
//            int lowestDegree = polynomial.lowestDegree();
//
//            if (lowestDegree == 0)
//                return buildResult(roots, operations);
//
//            // and set p(x) ← p(x)/x
//            operations.add(createOperation(polynomial.reduceDegree(lowestDegree), mobius));
//
//            return buildResult(roots, operations);
//        }
//
//
//        // (2)
//        // Count the sign changes in our polynomial.  There must be at least one sign change if the polynomial has
//        // any remaining positive roots.
//        int signChanges = polynomial.signChanges();
//
//        if (signChanges == 0) { // If there are no sign changes, report our results
//            return buildResult(roots, operations);
//        } else if (signChanges == 1) {
//            // If s = 1 add intrv(a, b, c, d) to rootlist and go to Step 2.
//
//            roots.add(intervalOf(mobius));
//
//            return buildResult(roots, operations);
//        }
//
//        // (3)
//        // Compute a lower bound α ∈ Z on the positive roots of p.
//        double lowerBoundComputed = lowerBound(polynomial);
//
////        // if the lower bound of the polynomial's roots is greater than the polynomial's constant term then scale the
////        // polynomial (and the associated Mobius Transformation) so that this lower bound coincides with x=1
////        //
////        //  If α > α0 set p(x) ← p(αx), a ← αa, c ← αc, and α ← 1
////        if (lowerBoundComputed > strzebońskiAlpha) {       // Strzeboński's contribution
////
////            System.out.printf("Lower bound is greater than %f.  Transforming polynomial so that lower bound is exactly 1\n", strzebońskiAlpha);
////
////            polynomial = polynomial.apply(Polynomial.of(new double[] { 0, lowerBoundComputed }));
////            mobius = mobius.composeAlphaX(lowerBoundComputed);
////
////            lowerBoundComputed = 1;
////        }
//
//        // (4)
//        // if the lower bound is greater than or equal to 1, compose the polynomial with (x + lowerBound) so that
//        // the lower bound now coincides with zero
//        //
//        // If α ≥ 1, set p(x) ← p(x + α), b ← αa + b, and d ← αc + d
//        if (lowerBoundComputed >= 1) {
//            Polynomial composed = Polynomial.of(new double[]{lowerBoundComputed, 1});
//
//            polynomial = polynomial.apply(composed);
//            mobius = mobius.composeXPlusK(lowerBoundComputed);
//
//            operations.add(createOperation(polynomial, mobius));
//
//            return buildResult(roots, operations);
//        }
//
//        // prior transformations ensure there are now one or more roots in (0, 1) and one or more roots in (1, infinity)
//        // (5)
//        // Prepare for identification of roots in interval (1, infinity):
//        // Use Taylor shift polynomial so that roots within (1, infinity) now lie within interval (0, infinity)
//        // then re-apply VAS
//        // Compute p1(x) ← p(x + 1)
//        Polynomial polynomial1 = polynomial.apply(xPlusOne);
//        // set a1 ← a, b1 ← a + b, c1 ← c, d1 ← c + d
//        RealMobiusTransformation mobius1 = mobius.composeXPlusK(1);
//
//        operations.add(createOperation(polynomial1, mobius1));
//
//        // Prepare for identification of roots within interval (0, 1)
//        // Use Budan's Theorem to transform polynomial then re-apply VAS
//        // a2 ← b, b2 ← a + b, c2 ← d, and d2 ← c + d
//        RealMobiusTransformation mobius2 = mobius.budansTheorem();
//        Polynomial polynomial2 = polynomial.budansTheorem();
//
//        operations.add(createOperation(polynomial2, mobius2));
//
//        return buildResult(roots, operations);
//    }

    double lowerBound(Polynomial polynomial) {
        return lowerBoundEstimator.estimateLowerBound(polynomial);
    }

    double idealLowerBound(Polynomial polynomial) {
        return (int) lowerBound(polynomial);
    }

//    @Override
//    public List<Interval> getResults() {
//        return null;
//    }

    Interval intervalOf(RealMobiusTransformation mobius) {
        return new Interval(Math.min(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)), Math.max(mobius.transform(0), mobius.transform(Double.POSITIVE_INFINITY)));
    }
}
