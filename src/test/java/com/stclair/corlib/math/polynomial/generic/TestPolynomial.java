package com.stclair.corlib.math.polynomial.generic;

import com.stclair.corlib.math.Complex;
import com.stclair.corlib.math.util.DoubleOperationStrategy;
import com.stclair.corlib.math.util.OperationStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

/**
 * @author hstclair
 * @since 8/16/15 3:57 PM
 */
public class TestPolynomial<T extends Double> {

    OperationStrategy<T> op = (OperationStrategy<T>) new DoubleOperationStrategy();

    void succeed() {}

    @Test
    public void testConstructFromZeroGivesEMPTYCoefficients() {
        Polynomial<T> instance = new Polynomial<>(op, op.from(0d));
        Assert.assertEquals(0, instance.getCoefficients().length);
    }

    @Test
    public void testConstructFromDoubleGivesConstPolynomialEqualToDouble() {
        T expected = op.from(87d);

        Polynomial<T> instance = new Polynomial<>(op, expected);

        assertEquals(0, instance.degree());
        assertEquals(expected, instance.getCoefficients()[0], 0);
    }

    @Test
    public void testConstructFromZeroCoefficientsGivesEMPTYCoefficients() {
        T[] coefficients = op.from(new double[] {0, 0, 0, 0});

        Polynomial<T> instance = new Polynomial<>(op, coefficients);

        Assert.assertEquals(0, instance.getCoefficients().length);
    }

    @Test
    public void testConstructRemovesLeadingZeros() {
        T[] coefficients = op.from(new double[]{1d, 2d, 4d, 0d});
        T[] expected = op.from(new double[]{1d, 2d, 4d});

        Polynomial<T> instance = new Polynomial<>(op, coefficients);

        assertArrayEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testConstructPreservesCoefficientsWithoutLeadingZeros() {
        T[] expected = op.from(new double[]{1d, 2d, 3d, 5d, 7d, 11d});

        Polynomial<T> instance = new Polynomial<>(op, expected);

        assertEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testPascalOfZeroIsIdentity() {
        Polynomial<T> instance = Polynomial.pascal(op, 0);

        assertTrue(instance.isIdentity());
    }

    @Test
    public void testPascalRejectsNegativeDegree() {

        try {
            Polynomial.pascal(op, -1);
            fail();
        } catch (IllegalArgumentException ex) {
            succeed();
        }
    }

    @Test
    public void testPascalOfOneIsXPlusOne() {
        Polynomial<T> instance = Polynomial.pascal(op, 1);

        assertEquals(1, instance.degree());

        assertEquals(op.from(1d), instance.getCoefficients()[0], 0);
        assertEquals(op.from(1d), instance.getCoefficients()[1], 0);
    }

    @Test
    public void testGetCoefficients() {
        Polynomial<T> instance = new Polynomial<>(op, op.from(1));

        T[] expected = instance.coefficients;

        assertEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testPascalOfNine() {
        T[] expected = op.from(new double[]{ 1, 9, 36, 84, 126, 126, 84, 36, 9, 1});

        Polynomial<T> instance = Polynomial.pascal(op, 9);

        assertEquals(9, instance.degree());

        assertArrayEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testProductOfDouble() {
        T[] coefficients = op.from(new double[] { 1, 2, 3, 4, 5 });

        T[] expected = op.from(new double[] { 2, 4, 6, 8, 10 });

        T multiplier = op.from(2);

        Polynomial<T> multiplicand = new Polynomial<>(op, coefficients);

        Polynomial<T> instance = multiplicand.product(multiplier);

        assertArrayEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testSquareOfPascalOneIsPascalTwo() {
        Polynomial<T> pascalOne = Polynomial.pascal(op, 1);

        Polynomial<T> pascalTwo = Polynomial.pascal(op, 2);

        Polynomial<T> instance = pascalOne.product(pascalOne);

        assertArrayEquals(pascalTwo.getCoefficients(), instance.getCoefficients());
    }

    @Test
    public void testPolynomialEquals() {
        T[] coefficients1 = op.from(new double[]{ 1, 2, 0, Math.PI, 0 });

        T[] coefficients2 = Arrays.copyOf(coefficients1, coefficients1.length);

        Polynomial<T> polynomial1 = new Polynomial<T>(op, coefficients1);

        Polynomial<T> polynomial2 = new Polynomial<T>(op, coefficients2);

        assertEquals(polynomial1, polynomial2);
    }

    @Test
    public void testProductRejectsNullMultiplicand() {
        try {
            Polynomial.pascal(op, 10).product((Polynomial<T>) null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testProductOfAAndIdentityIsA() {
        Polynomial<T> a = Polynomial.pascal(op,10);

        Polynomial<T> product = a.product(Polynomial.identity(op));

        assertEquals(a, product);
    }

    @Test
    public void testProductOfIdentityAndAIsA() {
        Polynomial<T> a = Polynomial.pascal(op,10);

        Polynomial<T> product = Polynomial.identity(op).product(a);

        assertEquals(a, product);
    }

    @Test
    public void testProductOfZeroDoubleIsZERO() {
        Polynomial<T> a = Polynomial.pascal(op, 10);

        Polynomial<T> product = a.product(op.from(0));

        Assert.assertEquals(Polynomial.zero(op), product);
    }

    @Test
    public void testProductOfAAndZEROIsZERO() {
        Polynomial<T> a = Polynomial.pascal(op, 10);

        Polynomial<T> product = a.product(Polynomial.zero(op));

        Assert.assertEquals(Polynomial.zero(op), product);
    }

    @Test
    public void testProductOfZEROAndAIsZERO() {
        Polynomial<T> a = Polynomial.pascal(op, 10);

        Polynomial<T> product = Polynomial.zero(op).product(a);

        Assert.assertEquals(Polynomial.zero(op), product);
    }

    @Test
    public void testProductOfEmptyCoefficientsAndDoubleIsZERO() {
        Polynomial<T> q = Polynomial.pascal(op, 1);
        q.coefficients = op.array(0);

        Polynomial<T> product = q.product(op.from(87));

        Assert.assertEquals(Polynomial.zero(op), product);
    }

    @Test
    public void testProductOfIDENTITYAndDoubleIsConstPolynomialOfDouble() {
        T expected = op.from(87);

        Polynomial<T> product = Polynomial.identity(op).product(expected);

        assertEquals(0, product.degree());
        assertEquals(expected, product.getCoefficients()[0]);
    }

    @Test
    public void testProductOfPolynomialAndZeroDegreePolynomialIsProductOfZDPolynomialA0() {
        T expected = op.from(87);
        T[] coefficients = op.from(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1} );

        Polynomial<T> polynomial = Polynomial.of(op, coefficients);

        Polynomial<T> zeroDegreePolynomial = Polynomial.of(op, expected);

        Polynomial<T> product = polynomial.product(zeroDegreePolynomial);

        T[] productCoefficients = product.getCoefficients();

        for (T coefficient : productCoefficients)
            assertEquals(expected, coefficient);
    }

    @Test
    public void testProductOfZeroDegreePolynomialAndPolynomialIsProductOfZDPolynomialA0() {
        T expected = op.from(87);
        T[] coefficients = op.from(new double[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        Polynomial<T> polynomial = Polynomial.of(op, coefficients);

        Polynomial<T> zeroDegreePolynomial = Polynomial.of(op, expected);

        Polynomial<T> product = zeroDegreePolynomial.product(polynomial);

        T[] productCoefficients = product.getCoefficients();

        for (T coefficient : productCoefficients)
            assertEquals(expected, coefficient, 0);
    }

    @Test
    public void testPowerRejectsNegativeExponent() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 3);

        try {
            polynomial.power(-1);
            fail();
        } catch (IllegalArgumentException ex) {
            succeed();
        }
    }

    @Test
    public void testPowerOfZeroIsIDENTITY() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 3);

        Polynomial<T> instance = polynomial.power(0);

        Assert.assertEquals(Polynomial.identity(op), instance);
    }

    @Test
    public void testPowerOfOneIsSelf() {
        Polynomial<T> expected = Polynomial.pascal(op, 3);

        Polynomial<T> instance = expected.power(1);

        assertEquals(expected, instance);
    }

    @Test
    public void testXPlusOneSquaredIsPascalOfTwo() {
        Polynomial<T> xPlusOne = new Polynomial<T>(op, op.from(new double[] {1, 1}));

        assertEquals(xPlusOne.power(2), Polynomial.pascal(op, 2));
    }

    @Test
    public void testNegatedPlusOriginalIsZERO() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 5);
        Polynomial<T> negative = polynomial.negate();

        Polynomial<T> sum = polynomial.sum(negative);

        assertEquals(Polynomial.zero(op), sum);
    }

    @Test
    public void testApplyDoubleOneIsSumOfCoefficients() {
        T expected = op.from(25);

        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[]{1, 3, 5, 7, 9}));

        T result = polynomial.apply(op.from(1));

        assertEquals(expected, result, 0);
    }

    @Test
    public void testApplyDoubleZeroYieldsA0() {
        T expected = op.from(18);
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[]{expected, -1, 1, 187, 214}));

        T result = polynomial.apply(op.from(0));

        assertEquals(expected, result, 0);
    }

    @Test
    public void testZEROApplyXIsZero() {
        T result = Polynomial.zero(op).apply(op.from(1000));

        assertEquals(op.from(0), result, 0);
    }

    @Test
    public void testApplyDouble() {
        // valueOf http://mathworld.wolfram.com/MaclaurinSeries.html
        // f(x) = 1/(1-x) = sigma[n=0 to inf] of x^n
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }));

        T x = op.from(.5);

        T expected = op.from(2);

        T result = polynomial.apply(op.from(x));

        assertEquals(expected, result, .0002);
    }

    @Test
    public void testApplyPolynomialIDENTITYToAnyPolynomialIsDegreeZeroPolynomialOfSumOfCoefficients() {
        Polynomial<T> anyPolynomial = Polynomial.pascal(op, 10);

        Polynomial<T> result = anyPolynomial.apply(Polynomial.identity(op));

        assertEquals(0, result.degree());
        assertEquals(anyPolynomial.sumOfCoefficients(), result.getCoefficients()[0], 0);
    }

    @Test
    public void testApplyPolynomialToZEROIsZERO() {
        Polynomial<T> anyPolynomial = Polynomial.pascal(op, 10);

        Polynomial<T> result = Polynomial.zero(op).apply(anyPolynomial);

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testApplyPolynomialZEROIsDegree0PolynomialOfA0() {
        T expected = op.from(87);
        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[]{expected, 33, 22, 77, 19}));

        Polynomial<T> result = polynomial.apply(Polynomial.zero(op));

        assertEquals(0, result.degree());
        assertEquals(expected, polynomial.getCoefficients()[0], 0);
    }

    @Test
    public void testApplyAnyPolynomialToZeroDegreePolynomialEqualsZeroDegreePolynomial() {
        Polynomial<T> anyPolynomial = Polynomial.pascal(op, 10);
        Polynomial<T> zeroDegree = Polynomial.of(op, op.from(87));

        Polynomial<T> result = zeroDegree.apply(anyPolynomial);

        assertEquals(zeroDegree, result);
    }

    @Test
    public void testApplyPolynomial() {
        Polynomial<T> polynomial1 = Polynomial.pascal(op, 1);
        Polynomial<T> polynomial2 = Polynomial.pascal(op, 2);

        Polynomial<T> expected = Polynomial.of(op, op.from(new double[]{4, 4, 1}));

        Polynomial<T> result = polynomial2.apply(polynomial1);

        assertEquals(expected, result);
    }

    @Test
    public void testZEROApplyComplexIsComplexZERO() {
        Complex result = Polynomial.zero(op).apply(Complex.of(87, 41));

        assertEquals(Complex.ZERO, result);
    }

    @Test
    public void testApplyComplexZEROIsA0() {
        Complex expected = Complex.of(87);

        Polynomial<T> polynomial = Polynomial.of(op, op.from(new double[] {expected.real, 112, 19, 31 }));

        Complex result = polynomial.apply(Complex.ZERO);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyComplexONEIsSumOfCoefficients() {
        T[] coefficients = op.from(new double[] { 87, 15, 32, 99, 17, 888, 31 });
        Polynomial<T> polynomial = Polynomial.of(op, coefficients);

        double sumOfCoefficients = 0;

        for (double coefficient : coefficients)
            sumOfCoefficients += coefficient;

        Complex expected = Complex.of(sumOfCoefficients);

        Complex result = polynomial.apply(Complex.ONE);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyComplexRealRoots() {
        Complex root1 = Complex.of(-1);
        Complex root2 = Complex.of(-2);
        Complex[] root = new Complex[] { root1, root2 };

        Polynomial<T> polynomial = new Polynomial<T>(op, op.from(new double[] {-root2.real, 1})).product(new Polynomial<T>(op, op.from(new double[]{-root1.real, 1})));

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertSame(Complex.ZERO, pOfRoot[index]);
        }

    }

    @Test
    public void testApplyComplexComplexRoots() {
        Complex root1 = Complex.of(0, 2);
        Complex root2 = Complex.of(0, -2);
        Complex[] root = new Complex[] { root1, root2 };

        Polynomial<T> polynomial = new Polynomial<T>(op, op.from(new double[] {4, 0, 1}));

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertEquals(Complex.ZERO, pOfRoot[index]);
        }
    }

    @Test
    public void testApplyComplexToDegreeZero() {
        Complex root1 = Complex.of(0, 2);
        Complex root2 = Complex.of(0, -2);
        Complex[] root = new Complex[] { root1, root2 };

        Polynomial<T> polynomial = new Polynomial<T>(op, op.from(new double[] {1}));

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertEquals(Complex.ZERO, pOfRoot[index]);
        }
    }

    @Test
    public void testDerivativeOfZEROIsZERO() {
        Polynomial<T> result = Polynomial.zero(op).derivative();

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testDerivativeOfIDENTITYIsZERO() {
        Polynomial<T> result = Polynomial.identity(op).derivative();

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testIntegralOfZEROIsZERO() {
        Polynomial<T> result = Polynomial.zero(op).integral();

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testIntegral() {
        double earthAccellerationDueToGravityMetersPerSecondSquared = 9.80665;
        double earthVelocityInFreefallAtTSeconds = earthAccellerationDueToGravityMetersPerSecondSquared;
        double earthFreefallDistanceAtTSeconds = earthVelocityInFreefallAtTSeconds / 2;

        Polynomial<T> accelleration = new Polynomial<T>(op, op.from(new double[] { earthAccellerationDueToGravityMetersPerSecondSquared }));

        Polynomial<T> velocity = accelleration.integral();

        Polynomial<T> distanceTravelled = velocity.integral();

        assertEquals(0, accelleration.degree());
        assertEquals(earthAccellerationDueToGravityMetersPerSecondSquared, accelleration.getCoefficients()[0], 0);

        assertEquals(1, velocity.degree());
        assertEquals(earthVelocityInFreefallAtTSeconds, velocity.getCoefficients()[1], 0);
        assertEquals(0, velocity.getCoefficients()[0], 0);

        assertEquals(2, distanceTravelled.degree());
        assertEquals(earthFreefallDistanceAtTSeconds, distanceTravelled.getCoefficients()[2], 0);
        assertEquals(0, distanceTravelled.getCoefficients()[1], 0);
        assertEquals(0, distanceTravelled.getCoefficients()[0], 0);
    }

    @Test
    public void testDerivative() {
        double earthAccellerationDueToGravityMetersPerSecondSquared = 9.80665;
        double earthVelocityInFreefallAtTSeconds = earthAccellerationDueToGravityMetersPerSecondSquared;
        double earthFreefallDistanceAtTSeconds = earthVelocityInFreefallAtTSeconds / 2;

        Polynomial<T> distanceTravelled = new Polynomial<T>(op, op.from(new double[] { 0, 0, earthFreefallDistanceAtTSeconds }));

        Polynomial<T> velocity = distanceTravelled.derivative();

        Polynomial<T> accelleration = velocity.derivative();


        assertEquals(0, accelleration.degree());
        assertEquals(earthAccellerationDueToGravityMetersPerSecondSquared, accelleration.getCoefficients()[0], 0);

        assertEquals(1, velocity.degree());
        assertEquals(earthVelocityInFreefallAtTSeconds, velocity.getCoefficients()[1], 0);
        assertEquals(0, velocity.getCoefficients()[0], 0);

        assertEquals(2, distanceTravelled.degree());
        assertEquals(earthFreefallDistanceAtTSeconds, distanceTravelled.getCoefficients()[2], 0);
        assertEquals(0, distanceTravelled.getCoefficients()[1], 0);
        assertEquals(0, distanceTravelled.getCoefficients()[0], 0);
    }


    @Test
    public void testDegreeIsArrayLengthMinusOne() {
        T[] coefficients = op.from(new double[] { 0, 0, 0 });

        Polynomial<T> instance = new Polynomial<T>(op, op.from(1));

        instance.coefficients = coefficients;

        int expected = coefficients.length - 1;

        assertEquals(expected, instance.degree());
    }

    @Test
    public void testSumOfZeroAndConstantIsPolynomialOfConstant() {
        T constant = op.from(100);
        Polynomial<T> sum = Polynomial.zero(op).sum(constant);

        assertEquals(Polynomial.of(op, constant), sum);
    }

    @Test
    public void testSumRejectsNullAddend() {
        try {
            Polynomial.pascal(op, 10).sum((Polynomial<T>)null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testSumOfPolynomialAndZEROIsPolynomial() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 10);

        Polynomial<T> sum = polynomial.sum(Polynomial.zero(op));

        assertEquals(polynomial, sum);
    }

    @Test
    public void testSumOfZEROAndPolynomialIsPolynomial() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 10);

        Polynomial<T> sum = Polynomial.zero(op).sum(polynomial);

        assertEquals(polynomial, sum);
    }

    @Test
    public void testSum() {
        T[] coefficients = op.from(new double[] { 1, 2, 3, 4, 5 });

        T[] expected = op.from(new double[] { 2, 4, 6, 8, 10 });

        Polynomial<T> addend = new Polynomial<T>(op, coefficients);

        Polynomial<T> instance = addend.sum(addend);

        assertArrayEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testDifferenceRejectsNullSubtrahend() {
        try {
            Polynomial.pascal(op, 10).difference(null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testDifferenceOfPolynomialAndZEROIsPolynomial() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 10);

        Polynomial<T> difference = polynomial.difference(Polynomial.zero(op));

        assertEquals(polynomial, difference);
    }

    @Test
    public void testDifferenceOfZEROAndPolynomialIsPolynomial() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 10);

        Polynomial<T> expected = polynomial.negate();

        Polynomial<T> difference = Polynomial.zero(op).difference(polynomial);

        assertEquals(expected, difference);
    }


    @Test
    public void testDifference() {
        T[] expected = op.from(new double[] { 1, 2, 3, 4, 5 });

        T[] coefficients = op.from(new double[] { 2, 4, 6, 8, 10 });

        Polynomial<T> minuend = new Polynomial<>(op, coefficients);

        Polynomial<T> subtrahend = new Polynomial<>(op, expected);

        Polynomial<T> instance = minuend.difference(subtrahend);

        assertArrayEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testTrimCoefficientsRemovesLeadingZero() {
        T[] array = op.from(new double[] { 0, 1, 0 });
        T[] expected = op.from(new double[] { 0, 1 });
        T[] instance = Polynomial.trimCoefficients(op, array);

        assertArrayEquals(expected, instance);
    }

    @Test
    public void testTrimCoefficientsReturnsEMPTYForAllZeroCoefficients() {
        T[] array = op.from(new double[] { 0, 0, 0, 0 });

        T[] expected = op.array(0);

        T[] instance = Polynomial.trimCoefficients(op, array);

        assertArrayEquals(expected, instance);
    }

    @Test
    public void testTrimCoefficientsReturnsOriginalArrayIfNoZeros() {
        T[] expected = op.from(new double[] { 1, 2, 3 });

        T[] result = Polynomial.trimCoefficients(op, expected);

        assertEquals(expected, result);
    }

    @Test
    public void testSigmaRejectsNegativeIncrement() {
        try {
            Polynomial.sigma(op, it -> Polynomial.pascal(op, it), 0, 1, -1);
            fail();
        } catch (IllegalArgumentException iae) {
            succeed();
        }
    }

    @Test
    public void testSigmaRejectsZeroIncrement() {
        try {
            Polynomial.sigma(op, it -> Polynomial.pascal(op, it), 0, 1, 0);
            fail();
        } catch (IllegalArgumentException iae) {
            succeed();
        }
    }

    @Test
    public void testSigmaReturnsZEROWhenEndLessThanStart() {
        Polynomial<T> result = Polynomial.sigma(op, it -> Polynomial.pascal(op, it), 2, 1, 1);

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testSigmaInvokesFunction() {
        boolean[] invoked = {false};

        Function<Integer, Polynomial<T>> function = (it) -> { invoked[0] = true; return Polynomial.zero(op); };

        Polynomial<T> result = Polynomial.sigma(op, function, 0, 1, 1);

        assertTrue(invoked[0]);
    }

    @Test
    public void testSigmaStartEnd() {
        int start = 2;
        int end = 5;
        int[] counter = new int[1];
        int[] indexValues = new int[end - start + 1];

        Function<Integer, Polynomial<T>> function = (it) -> {indexValues[counter[0]++] = it; return Polynomial.pascal(op, it); };

        Polynomial.sigma(op, function, start, end);

        for (int index = 0; index < indexValues.length; index++) {
            assertEquals(index + start, indexValues[index]);
        }
    }

    @Test
    public void testSigmaReturnsSum() {

        T[] coefficients1 = op.from(new double[] { 1, 2, 3, 5, 0 });
        T[] coefficients2 = op.from(new double[] { 3, 2, 1, 0 });
        T[] coefficients3 = op.from(new double[] { 1, 1, 1, 0, 0, 5, 0 });

        T[][] coefficients = op.matrix(3);

        coefficients[0] = coefficients1;
        coefficients[1] = coefficients2;
        coefficients[2] = coefficients3;

        T[] expectedCoefficients = op.from(new double[] { 5, 5, 5, 5, 0, 5 });

        Polynomial<T> expected = new Polynomial<>(op, expectedCoefficients);

        Function<Integer, Polynomial<T>> function = (it) -> new Polynomial<>(op, coefficients[it]);

        Polynomial<T> instance = Polynomial.sigma(op, function, 0, coefficients.length - 1, 1);

        assertEquals(expected, instance);

        assertArrayEquals(expectedCoefficients, instance.getCoefficients());
    }

    @Test
    public void testVincentsReduction() {
        T[] coefficients = op.from(new double[] { 1, -4, 3, 1 });
        T[] expectedCoefficients = op.from(new double[] { 1, -2, -1, 1 });

        Polynomial<T> initial = new Polynomial<>(op, coefficients);

        Polynomial<T> expected = new Polynomial<>(op, expectedCoefficients);

        Polynomial<T> result = initial.budansTheorem();

        assertEquals(expected, result);

        assertArrayEquals(expectedCoefficients, result.getCoefficients());
    }

    @Test
    public void testSignChangesWithAlternatingSign() {
        T[] coefficients = op.from(new double[] { 1, -1, 1, -1 });
        Polynomial<T> polynomial = Polynomial.of(op, coefficients);

        assertEquals(3, polynomial.signChanges());
    }

    @Test
    public void testSignChangesWithNoChange() {
        Polynomial<T> polynomial = Polynomial.pascal(op, 10);

        assertEquals(0, polynomial.signChanges());
    }

    @Test
    public void testSignChangesOnZERO() {
        assertEquals(0, Polynomial.zero(op).signChanges());
    }

    @Test
    public void testPolynomialOfZeroIsZERO() {
        Polynomial<T> result = Polynomial.of(op, op.from(0));

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testPolynomialOfOneIsIdentity() {
        Polynomial<T> result = Polynomial.of(op, op.from(1));

        assertEquals(Polynomial.identity(op), result);
    }

    @Test
    public void testPolynomialOfEmptyCoefficientsArrayIsZERO() {
        Polynomial<T> result = Polynomial.of(op, op.from(new double[0]));

        assertEquals(Polynomial.zero(op), result);
    }

    @Test
    public void testPolynomialOfSingleCoefficientOneIsIdentity() {
        Polynomial<T> result = Polynomial.of(op, op.from(1));

        assertEquals(Polynomial.identity(op), result);
    }

    @Test
    public void testToStringGeneratesZeroConstantForZeroPolynomial() {
        String value = Polynomial.zero(op).toString();

        assertEquals("0", value);
    }

    @Test
    public void testToStringGeneratesLeadingNegativeSign() {
        String value = Polynomial.of(op, op.from(-1)).toString();

        assertEquals("-1", value);
    }

    @Test
    public void testToStringDoesNotGenerateLeadingPositiveSign() {
        String value = Polynomial.of(op, op.from(new double[] { 1 })).toString();

        assertEquals("1", value);
    }

    @Test
    public void testToStringDoesNotGenerateExponentForDegreeOneTerm() {
        String value = Polynomial.of(op, op.from( new double[] { 0, 1 })).toString();

        assertEquals("X", value);
    }

    @Test
    public void testToStringDoesNotGenerateCoefficientForNonzeroDegreeTerms() {
        String value = Polynomial.of(op, op.from(new double[] { 1, 1, 1, 1, 1 })).toString();

        assertEquals("X^4 + X^3 + X^2 + X + 1", value);
    }

    @Test
    public void testToStringDoesNotReportForZeroCoefficients() {
        String value = Polynomial.of(op, op.from(new double[] { 0, 1, 0, 1 })).toString();

        assertEquals("X^3 + X", value);
    }

    @Test
    public void testToStringDoesNotAddNegativeTerms() {
        String value = Polynomial.of(op, op.from(new double[] {1, -1, 1, -1, 1 })).toString();

        assertEquals("X^4 - X^3 + X^2 - X + 1", value);
    }

    @Test
    public void testToStringGeneratesDecimalCoefficients() {
        String value = Polynomial.of(op, op.from(1.11)).toString();

        assertEquals("1.11", value);
    }

    @Test
    public void testFromTwoRoots() {
        T[] expectedCoefficients = op.from(new double[] { 2, -3, 1 });
        T[] roots = op.from(new double[] { 1, 2 });
        Polynomial<T> value = Polynomial.fromRoots(op, roots);

        assertArrayEquals(expectedCoefficients, value.coefficients);
    }

    @Test
    public void testFromThreeRoots() {
        T[] expectedCoefficients = op.from(new double[] { -8, 14, -7, 1 });
        T[] roots = op.from(new double[] { 1, 2, 4 });
        Polynomial<T> value = Polynomial.fromRoots(op, roots);

        assertArrayEquals(expectedCoefficients, value.coefficients);
    }

    @Test
    public void testFromFourRoots() {
        T[] expectedCoefficients = op.from(new double[] { 96, -176, 98, -19, 1 });
        T[] roots = op.from(new double[] { 1, 2, 4, 12 });
        Polynomial<T> value = Polynomial.fromRoots(op, roots);

        assertArrayEquals(expectedCoefficients, value.coefficients);
    }

    @Test
    public void testQuotientReducesDegree() {
        T[] dividendCoefficients = op.from(new double[] { 0, 0, 1 });
        T[] divisorCoefficients = op.from(new double[] { 0, 1 });
        T[] quotientCoefficients = op.from(new double[] { 0, 1 });
        T[] remainderCoefficients = op.from(new double[] { 0 });

        Polynomial<T> dividend = Polynomial.of(op, dividendCoefficients);
        Polynomial<T> divisor = Polynomial.of(op, divisorCoefficients);
        Polynomial<T> expectedQuotient = Polynomial.of(op, quotientCoefficients);
        Polynomial<T> expectedRemainder = Polynomial.of(op, remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }


    @Test
    public void testQuotientPerformsLongDivision() {
        T[] dividendCoefficients = op.from(new double[] { -4, 0, -2, 1 });
        T[] divisorCoefficients = op.from(new double[] { -3, 1 });
        T[] quotientCoefficients = op.from(new double[] { 3, 1, 1 });
        T[] remainderCoefficients = op.from(new double[] { 5 });

        Polynomial<T> dividend = Polynomial.of(op, dividendCoefficients);
        Polynomial<T> divisor = Polynomial.of(op, divisorCoefficients);
        Polynomial<T> expectedQuotient = Polynomial.of(op, quotientCoefficients);
        Polynomial<T> expectedRemainder = Polynomial.of(op, remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }


    @Test
    public void testQuotientPerformsLongDivisionAgain() {
        T[] dividendCoefficients = op.from(new double[] { 5, 13, 7, 2 });
        T[] divisorCoefficients = op.from(new double[] { 1, 2 });
        T[] quotientCoefficients = op.from(new double[] { 5, 3, 1 });
        T[] remainderCoefficients = op.from(new double[] { 0 });

        Polynomial<T> dividend = Polynomial.of(op, dividendCoefficients);
        Polynomial<T> divisor = Polynomial.of(op, divisorCoefficients);
        Polynomial<T> expectedQuotient = Polynomial.of(op, quotientCoefficients);
        Polynomial<T> expectedRemainder = Polynomial.of(op, remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }

    @Test
    public void testQuotientOfProductAndMultiplicandIsOtherMultiplicand() {
        Polynomial<T> multiplicandA = Polynomial.of(op, op.from(new double[] { 12, 7, 5, 3, 8, 22, -3, -8, 1 }));
        Polynomial<T> multiplicandB = Polynomial.of(op, op.from(new double[] { -3, 4, 7, -8, 12 }));

        Polynomial<T> product = multiplicandA.product(multiplicandB);

        PolynomialQuotient result = product.quotient(multiplicandB);

        assertEquals(Polynomial.zero(op), result.remainder);
        assertEquals(multiplicandA, result.quotient);
    }
}
