package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.polynomial.generic.Interval;
import com.stclair.corlib.math.polynomial.generic.Polynomial;
import com.stclair.corlib.math.polynomial.generic.roots.VincentAkritasStrzeboński2;
import com.stclair.corlib.math.util.ApfloatOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;
import org.apfloat.Apfloat;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 8/22/15 4:30 PM
 */
public class VincentAkritasStrzeboński2Tester<T> {

    OperationStrategy<T> op;

    public VincentAkritasStrzeboński2Tester(OperationStrategy<T> op) {
        this.op = op;
    }

    public void smokeTest() {
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] {7, -7, 0, 1 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(op.from(1), results.get(0).a);
        assertEquals(op.from(1.5), results.get(0).b);
        assertEquals(op.from(1.5), results.get(1).a);
        assertEquals(op.from(2), results.get(1).b);
    }

    @Test
    public void smokeTestExperimentalVASOperation() {
        // X^3 - 7X + 7
        // roots at: X ~= -3.0892 X ~= 1.3569
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 7, -7, 0, 1 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<T>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(op.from(1), results.get(0).a);
        assertEquals(op.from(1.5), results.get(0).b);
        assertEquals(op.from(1.5), results.get(1).a);
        assertEquals(op.from(2), results.get(1).b);
    }

    @Test
    public void vasReturnsSingleRoot() {
        // X^2 - 2X + 1
        // one root:  X = 1
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 1, -2, 1 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(1, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(op.from(1), results.get(0).getExactValue());
    }

    @Test
    public void vasReturnsZeroToOneAndOneToInfinity() {

        // X^2 - 2X + .5
        // Two roots:  X = 1/2(2 - sqrt(2))  and  X = 1/2(2 + sqrt(2))
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 1, -2, .5 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<T>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertEquals(op.one(), results.get(0).a);
        assertEquals(op.positiveInfinity(), results.get(0).b);
        assertEquals(op.zero(), results.get(1).a);
        assertEquals(op.one(), results.get(1).b);
    }

    @Test
    public void vasReturnsZeroToInfinityRangeForFunctionWithOneSignChange() {

        // X^2 - 2X
        // Two roots:  X = 0  and  X = 2  but VAS will only find one of them (by design)
        // The roots are not found because VAS relies on Descartes' Rule of Signs to determine the number of roots
        // Since Decartes' Rule of Signs finds that there is only on sign variation between non-zero coefficients,
        // it reports that there is only one root.
        // note that it is trivial to find the remaining root because X^2 - 2X is obviously X ( X - 2 )
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 1, -2, 0 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(1, results.size());
        assertEquals(op.zero(), results.get(0).a);
        assertEquals(op.positiveInfinity(), results.get(0).b);
    }

    @Test
    public void smokeTest2ExperimentalVASOperation() {
        // X^5 - 3X^4 + 2X^3 = X^3 (X^2 - 3X + 2) = X^3 * (X - 1) * (X - 2)
        // Three roots: X = 0, X = 1, X = 2
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 0, 0, 0, 2, -3, 1 }));  // X^5 - 3X^4 + 2X^3

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(3, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(op.zero(), results.get(0).getExactValue());
        assertTrue(results.get(1).isExactValue());
        assertEquals(op.one(), results.get(1).getExactValue());
        assertEquals(new Interval<T>(op.one(), op.positiveInfinity(), op), results.get(2));
    }

    @Test
    public void factorEquationWithTwoRoots() {
        // X^2 - 6 X + 8
        // with roots 2, 4
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 8, -6, 1 }));

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(2, results.size());
        assertTrue(results.get(0).isExactValue());
        assertEquals(op.from(2), results.get(0).getExactValue());
        assertEquals(new Interval<>(op.from(2), op.from(Double.POSITIVE_INFINITY), op), results.get(1));
    }

    void assertRootsWithinResults(T[] expectedRoots, List<Interval<T>> results) {

        for (T expectedRoot : expectedRoots) {
            boolean found = false;

            for (Interval<T> interval : results) {
                if (interval.contains(expectedRoot)) {
                    found = true;
                    break;
                }
            }

            if (! found)
                fail(String.format("Root %s was not found in result set", expectedRoot));
        }
    }

    @Test
    public void factorEquationWithThreeRoots() {
        // X^3 -7 X^2 + 14 X - 8
        // with roots 1, 2, 4
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { -8, 14, -7, 1 }));
        T[] expectedRoots = op.from(new double[] { 1, 2, 4 });

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(3, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

    @Test
    public void factorEquationWithFourRoots() {
        // X^4 - 19 X^3 + 98 X^2 - 176 X + 96
        // with roots 1, 2, 4, 12
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 96, -176, 98, -19, 1 }));
        T[] expectedRoots = op.from(new double[] { 1, 2, 4, 12 });

        VincentAkritasStrzeboński2<T> vas = new VincentAkritasStrzeboński2<>(op);

        List<Interval<T>> results = vas.findRootIntervals(polynomial);

        assertEquals(4, results.size());
        assertRootsWithinResults(expectedRoots, results);
    }

//    @Test
//    public void factorEquationWithFiveRoots() {
//        // X^5 - 26 X^4 + 231 X^3 - 862 X^2 + 1328 X - 672
//        // with roots 1, 2, 4, 12, 7
//        Polynomial polynomial = Polynomial.of(new double[] { -672, 1328, -862, 231, -26, 1 });
//        double[] expectedRoots = new double[] { 1, 2, 4, 12, 7 };
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        List<Interval> results = vas.findRootIntervals(polynomial);
//
//        assertEquals(5, results.size());
//        assertRootsWithinResults(expectedRoots, results);
//    }
//
//
//
//    @Test
//    public void factorEquationWithSixRoots() {
//        // x^6 - 29 X^5 + 309 X^4 - 1555 X^3 + 3914 X^2 - 4656 X + 2016
//        // with roots 1, 2, 3, 4, 7, 12
//        Polynomial polynomial = Polynomial.of(new double[] { 2016, -4656, 3914, -1555, 309, -29, 1 });
//        double[] expectedRoots = new double[] { 1, 2, 3, 4, 7, 12 };
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        List<Interval> results = vas.findRootIntervals(polynomial);
//
//        assertEquals(6, results.size());
//        assertRootsWithinResults(expectedRoots, results);
//    }
//
//    @Test
//    public void factorEquationWithFifteenRoots() {
//        double[] expectedRoots = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
//        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        List<Interval> results = vas.findRootIntervals(polynomial);
//
//        assertEquals(expectedRoots.length, results.size());
//        assertRootsWithinResults(expectedRoots, results);
//    }
//
//    @Test
//    public void factorEquationWithTwentyTwoRoots() {
//        double[] expectedRoots = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22 };
//        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        List<Interval> results = vas.findRootIntervals(polynomial);
//
//        assertEquals(expectedRoots.length, results.size());
//        assertRootsWithinResults(expectedRoots, results);
//    }
//
//
//    // Higher-order polynomials (those with larger roots or additional, distinct roots) appear to exceed the precision
//    // of a Double so they cannot be supported in the current implementation.  To carry this implementation beyond
//    // this limitation will require a BigDecimal implementation or a Rational implementation (or something more exotic)
//    // For example, this test fails:
//    @Test
//    public void factorEquationWithTwentyTwoPrimeRoots() {
//        double[] expectedRoots = new double[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71 };
//        Polynomial polynomial = Polynomial.fromRoots(expectedRoots);
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        List<Interval> results = vas.findRootIntervals(polynomial);
//
//        assertEquals(expectedRoots.length, results.size());
//        assertRootsWithinResults(expectedRoots, results);
//    }
//
//
//    public static Polynomial fromRoots(double[] roots) {
//        Polynomial result = Polynomial.of(1);
//
//        for (double root : roots) {
//            Polynomial multiplier = Polynomial.of(new double[] { -root, 1});
//
//            System.out.printf("product of %1$s and %2$s\n", multiplier, result);
//
//            result = result.product(multiplier);
//        }
//
//        System.out.printf("returning %1$s", result);
//
//        return result;
//    }
//
//    // Higher-order polynomials (those with larger roots or additional, distinct roots) appear to exceed the precision
//    // of a Double so they cannot be supported in the current implementation.  To carry this implementation beyond
//    // this limitation will require a BigDecimal implementation or a Rational implementation (or something more exotic)
//    // For example, this test fails:
//    @Test
//    public void factorEquationWithTwentyTwoPrimeRootsShowWork() {
//        double[] expectedRoots = new double[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71 };
//        Polynomial polynomial = fromRoots(expectedRoots);
//
//        VincentAkritasStrzeboński2 vas = new VincentAkritasStrzeboński2();
//
//        vas.solveRootIntervals(polynomial)
//                .forEach(System.out::println);
//    }
}
