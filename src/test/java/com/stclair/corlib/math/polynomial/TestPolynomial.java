package com.stclair.corlib.math.polynomial;

import com.stclair.corlib.math.Complex;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author hstclair
 * @since 8/16/15 3:57 PM
 */
public class TestPolynomial {

    void succeed() {}

    @Test
    public void testConstructFromZeroGivesEMPTYCoefficients() {
        Polynomial instance = new Polynomial(0);
        assertTrue(Polynomial.EMPTY == instance.getCoefficients());
    }

    @Test
    public void testConstructFromDoubleGivesConstPolynomialEqualToDouble() {
        double expected = 87;

        Polynomial instance = new Polynomial(expected);

        assertEquals(0, instance.degree());
        assertEquals(expected, instance.getCoefficients()[0], 0);
    }

    @Test
    public void testConstructFromZeroCoefficientsGivesEMPTYCoefficients() {
        double[] coefficients = {0, 0, 0, 0};

        Polynomial instance = new Polynomial(coefficients);

        assertTrue(Polynomial.EMPTY == instance.getCoefficients());
    }

    @Test
    public void testConstructRemovesLeadingZeros() {
        double[] coefficients = {1, 2, 4, 0};
        double[] expected = {1, 2, 4};

        Polynomial instance = new Polynomial(coefficients);

        assertArrayEquals(expected, instance.getCoefficients(), 0);
    }

    @Test
    public void testConstructPreservesCoefficientsWithoutLeadingZeros() {
        double[] expected = {1, 2, 3, 5, 7, 11};

        Polynomial instance = new Polynomial(expected);

        assertEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testPascalOfZeroIsIdentity() {
        Polynomial instance = Polynomial.pascal(0);

        assertTrue(Polynomial.IDENTITY == instance);
    }

    @Test
    public void testPascalRejectsNegativeDegree() {

        try {
            Polynomial.pascal(-1);
            fail();
        } catch (IllegalArgumentException ex) {
            succeed();
        }
    }

    @Test
    public void testPascalOfOneIsXPlusOne() {
        Polynomial instance = Polynomial.pascal(1);

        assertEquals(1, instance.degree());

        assertEquals(1d, instance.getCoefficients()[0], 0);
        assertEquals(1d, instance.getCoefficients()[1], 0);
    }

    @Test
    public void testGetCoefficients() {
        Polynomial instance = new Polynomial(1);

        double[] expected = instance.coefficients;

        assertEquals(expected, instance.getCoefficients());
    }

    @Test
    public void testPascalOfNine() {
        double[] expected = { 1, 9, 36, 84, 126, 126, 84, 36, 9, 1};

        Polynomial instance = Polynomial.pascal(9);

        assertEquals(9, instance.degree());

        assertArrayEquals(expected, instance.getCoefficients(), 0);
    }

    @Test
    public void testProductOfDouble() {
        double[] coefficients = { 1, 2, 3, 4, 5 };

        double[] expected = { 2, 4, 6, 8, 10 };

        double multiplier = 2;

        Polynomial multiplicand = new Polynomial(coefficients);

        Polynomial instance = multiplicand.product(multiplier);

        assertArrayEquals(expected, instance.getCoefficients(), 0);
    }

    @Test
    public void testSquareOfPascalOneIsPascalTwo() {
        Polynomial pascalOne = Polynomial.pascal(1);

        Polynomial pascalTwo = Polynomial.pascal(2);

        Polynomial instance = pascalOne.product(pascalOne);

        assertArrayEquals(pascalTwo.getCoefficients(), instance.getCoefficients(), 0);
    }

    @Test
    public void testPolynomialEquals() {
        double[] coefficients1 = { 1, 2, 0, Math.PI, 0 };

        double[] coefficients2 = Arrays.copyOf(coefficients1, coefficients1.length);

        Polynomial polynomial1 = new Polynomial(coefficients1);

        Polynomial polynomial2 = new Polynomial(coefficients2);

        assertEquals(polynomial1, polynomial2);
    }

    @Test
    public void testProductRejectsNullMultiplicand() {
        try {
            Polynomial.pascal(10).product(null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testProductOfAAndIdentityIsA() {
        Polynomial a = Polynomial.pascal(10);

        Polynomial product = a.product(Polynomial.IDENTITY);

        assertEquals(a, product);
    }

    @Test
    public void testProductOfIdentityAndAIsA() {
        Polynomial a = Polynomial.pascal(10);

        Polynomial product = Polynomial.IDENTITY.product(a);

        assertEquals(a, product);
    }

    @Test
    public void testProductOfZeroDoubleIsZERO() {
        Polynomial a = Polynomial.pascal(10);

        Polynomial product = a.product(0);

        assertTrue(Polynomial.ZERO == product);
    }

    @Test
    public void testProductOfAAndZEROIsZERO() {
        Polynomial a = Polynomial.pascal(10);

        Polynomial product = a.product(Polynomial.ZERO);

        assertTrue(Polynomial.ZERO == product);
    }

    @Test
    public void testProductOfZEROAndAIsZERO() {
        Polynomial a = Polynomial.pascal(10);

        Polynomial product = Polynomial.ZERO.product(a);

        assertTrue(Polynomial.ZERO == product);
    }

    @Test
    public void testProductOfEMPTYAndDoubleIsZERO() {
        Polynomial q = Polynomial.pascal(1);
        q.coefficients = Polynomial.EMPTY;

        Polynomial product = q.product(87);

        assertTrue(Polynomial.ZERO == product);
    }

    @Test
    public void testProductOfEmptyCoefficientsAndDoubleIsZERO() {
        Polynomial q = Polynomial.pascal(1);
        q.coefficients = new double[0];

        Polynomial product = q.product(87);

        assertTrue(Polynomial.ZERO == product);
    }

    @Test
    public void testProductOfIDENTITYAndDoubleIsConstPolynomialOfDouble() {
        double expected = 87;

        Polynomial product = Polynomial.IDENTITY.product(expected);

        assertEquals(0, product.degree());
        assertEquals(expected, product.getCoefficients()[0], 0);
    }

    @Test
    public void testProductOfPolynomialAndZeroDegreePolynomialIsProductOfZDPolynomialA0() {
        double expected = 87;
        double[] coefficients = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Polynomial polynomial = Polynomial.of(coefficients);

        Polynomial zeroDegreePolynomial = Polynomial.of(expected);

        Polynomial product = polynomial.product(zeroDegreePolynomial);

        double[] productCoefficients = product.getCoefficients();

        for (double coefficient : productCoefficients)
            assertEquals(expected, coefficient, 0);
    }

    @Test
    public void testProductOfZeroDegreePolynomialAndPolynomialIsProductOfZDPolynomialA0() {
        double expected = 87;
        double[] coefficients = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        Polynomial polynomial = Polynomial.of(coefficients);

        Polynomial zeroDegreePolynomial = Polynomial.of(expected);

        Polynomial product = zeroDegreePolynomial.product(polynomial);

        double[] productCoefficients = product.getCoefficients();

        for (double coefficient : productCoefficients)
            assertEquals(expected, coefficient, 0);
    }

    @Test
    public void testPowerRejectsNegativeExponent() {
        Polynomial polynomial = Polynomial.pascal(3);

        try {
            polynomial.power(-1);
            fail();
        } catch (IllegalArgumentException ex) {
            succeed();
        }
    }

    @Test
    public void testPowerOfZeroIsIDENTITY() {
        Polynomial polynomial = Polynomial.pascal(3);

        Polynomial instance = polynomial.power(0);

        assertTrue(Polynomial.IDENTITY == instance);
    }

    @Test
    public void testPowerOfOneIsSelf() {
        Polynomial expected = Polynomial.pascal(3);

        Polynomial instance = expected.power(1);

        assertEquals(expected, instance);
    }

    @Test
    public void testXPlusOneSquaredIsPascalOfTwo() {
        Polynomial xPlusOne = new Polynomial(new double[] {1, 1});

        assertEquals(xPlusOne.power(2), Polynomial.pascal(2));
    }

    @Test
    public void testNegatedPlusOriginalIsZERO() {
        Polynomial polynomial = Polynomial.pascal(5);
        Polynomial negative = polynomial.negate();

        Polynomial sum = polynomial.sum(negative);

        assertTrue(Polynomial.ZERO == sum);
    }

    @Test
    public void testApplyDoubleOneIsSumOfCoefficients() {
        double expected = 25;

        Polynomial polynomial = Polynomial.of(new double[]{1, 3, 5, 7, 9});

        double result = polynomial.apply(1);

        assertEquals(expected, result, 0);
    }

    @Test
    public void testApplyDoubleZeroYieldsA0() {
        double expected = 18;
        Polynomial polynomial = Polynomial.of(new double[]{expected, -1, 1, 187, 214});

        double result = polynomial.apply(0);

        assertEquals(expected, result, 0);
    }

    @Test
    public void testZEROApplyXIsZero() {
        double result = Polynomial.ZERO.apply(1000);

        assertEquals(0, result, 0);
    }

    @Test
    public void testApplyDouble() {
        // valueOf http://mathworld.wolfram.com/MaclaurinSeries.html
        // f(x) = 1/(1-x) = sigma[n=0 to inf] of x^n
        Polynomial polynomial = Polynomial.of(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });

        double x = .5;

        double expected = 2;

        double result = polynomial.apply(x);

        assertEquals(expected, result, .0002);
    }

    @Test
    public void testApplyPolynomialIDENTITYToAnyPolynomialIsDegreeZeroPolynomialOfSumOfCoefficients() {
        Polynomial anyPolynomial = Polynomial.pascal(10);

        Polynomial result = anyPolynomial.apply(Polynomial.IDENTITY);

        assertEquals(0, result.degree());
        assertEquals(result.getCoefficients()[0], anyPolynomial.sumOfCoefficients(), 0);
    }

    @Test
    public void testApplyPolynomialToZEROIsZERO() {
        Polynomial anyPolynomial = Polynomial.pascal(10);

        Polynomial result = Polynomial.ZERO.apply(anyPolynomial);

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testApplyPolynomialZEROIsDegree0PolynomialOfA0() {
        double expected = 87;
        Polynomial polynomial = Polynomial.of(new double[]{expected, 33, 22, 77, 19});

        Polynomial result = polynomial.apply(Polynomial.ZERO);

        assertEquals(0, result.degree());
        assertEquals(expected, polynomial.getCoefficients()[0], 0);
    }

    @Test
    public void testApplyAnyPolynomialToZeroDegreePolynomialEqualsZeroDegreePolynomial() {
        Polynomial anyPolynomial = Polynomial.pascal(10);
        Polynomial zeroDegree = Polynomial.of(87);

        Polynomial result = zeroDegree.apply(anyPolynomial);

        assertEquals(zeroDegree, result);
    }

    @Test
    public void testApplyPolynomial() {
        Polynomial polynomial1 = Polynomial.pascal(1);
        Polynomial polynomial2 = Polynomial.pascal(2);

        Polynomial expected = Polynomial.of(new double[]{4, 4, 1});

        Polynomial result = polynomial2.apply(polynomial1);

        assertEquals(expected, result);
    }

    @Test
    public void testZEROApplyComplexIsComplexZERO() {
        Complex result = Polynomial.ZERO.apply(Complex.of(87, 41));

        assertEquals(Complex.ZERO, result);
    }

    @Test
    public void testApplyComplexZEROIsA0() {
        Complex expected = Complex.of(87);

        Polynomial polynomial = Polynomial.of(new double[] {expected.real, 112, 19, 31 });

        Complex result = polynomial.apply(Complex.ZERO);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyComplexONEIsSumOfCoefficients() {
        double[] coefficients = { 87, 15, 32, 99, 17, 888, 31 };
        Polynomial polynomial = Polynomial.of(coefficients);

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

        Polynomial polynomial = new Polynomial(new double[] {-root2.real, 1}).product(new Polynomial(new double[]{-root1.real, 1}));

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertTrue(Complex.ZERO == pOfRoot[index]);
        }

    }

    @Test
    public void testApplyComplexComplexRoots() {
        Complex root1 = Complex.of(0, 2);
        Complex root2 = Complex.of(0, -2);
        Complex[] root = new Complex[] { root1, root2 };

        Polynomial polynomial = new Polynomial(new double[] {4, 0, 1});

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertTrue(Complex.ZERO == pOfRoot[index]);
        }
    }

    @Test
    public void testApplyComplexToDegreeZero() {
        Complex root1 = Complex.of(0, 2);
        Complex root2 = Complex.of(0, -2);
        Complex[] root = new Complex[] { root1, root2 };

        Polynomial polynomial = new Polynomial(new double[] {4, 0, 1});

        Complex[] pOfRoot = new Complex[root.length];

        for (int index = 0; index < root.length; index++) {
            pOfRoot[index] = polynomial.apply(root[index]);

            assertTrue(Complex.ZERO == pOfRoot[index]);
        }
    }

    @Test
    public void testDerivativeOfZEROIsZERO() {
        Polynomial result = Polynomial.ZERO.derivative();

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testDerivativeOfIDENTITYIsZERO() {
        Polynomial result = Polynomial.IDENTITY.derivative();

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testIntegralOfZEROIsZERO() {
        Polynomial result = Polynomial.ZERO.integral();

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testIntegral() {
        double earthAccellerationDueToGravityMetersPerSecondSquared = 9.80665;
        double earthVelocityInFreefallAtTSeconds = earthAccellerationDueToGravityMetersPerSecondSquared;
        double earthFreefallDistanceAtTSeconds = earthVelocityInFreefallAtTSeconds / 2;

        Polynomial accelleration = new Polynomial(new double[] { earthAccellerationDueToGravityMetersPerSecondSquared });

        Polynomial velocity = accelleration.integral();

        Polynomial distanceTravelled = velocity.integral();

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

        Polynomial distanceTravelled = new Polynomial(new double[] { 0, 0, earthFreefallDistanceAtTSeconds });

        Polynomial velocity = distanceTravelled.derivative();

        Polynomial accelleration = velocity.derivative();


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
        double[] coefficients = { 0, 0, 0 };

        Polynomial instance = new Polynomial(1);

        instance.coefficients = coefficients;

        int expected = coefficients.length - 1;

        assertEquals(expected, instance.degree());
    }

    @Test
    public void testSumOfZeroAndConstantIsPolynomialOfConstant() {
        double constant = 100;
        Polynomial sum = Polynomial.ZERO.sum(constant);

        assertEquals(Polynomial.of(constant), sum);
    }

    @Test
    public void testSumRejectsNullAddend() {
        try {
            Polynomial.pascal(10).sum(null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testSumOfPolynomialAndZEROIsPolynomial() {
        Polynomial polynomial = Polynomial.pascal(10);

        Polynomial sum = polynomial.sum(Polynomial.ZERO);

        assertEquals(polynomial, sum);
    }

    @Test
    public void testSumOfZEROAndPolynomialIsPolynomial() {
        Polynomial polynomial = Polynomial.pascal(10);

        Polynomial sum = Polynomial.ZERO.sum(polynomial);

        assertEquals(polynomial, sum);
    }

    @Test
    public void testSum() {
        double[] coefficients = { 1, 2, 3, 4, 5 };

        double[] expected = { 2, 4, 6, 8, 10 };

        Polynomial addend = new Polynomial(coefficients);

        Polynomial instance = addend.sum(addend);

        assertArrayEquals(expected, instance.getCoefficients(), 0);
    }

    @Test
    public void testDifferenceRejectsNullSubtrahend() {
        try {
            Polynomial.pascal(10).difference(null);
            fail();
        } catch (NullPointerException npe) {
            succeed();
        }
    }

    @Test
    public void testDifferenceOfPolynomialAndZEROIsPolynomial() {
        Polynomial polynomial = Polynomial.pascal(10);

        Polynomial difference = polynomial.difference(Polynomial.ZERO);

        assertEquals(polynomial, difference);
    }

    @Test
    public void testDifferenceOfZEROAndPolynomialIsPolynomial() {
        Polynomial polynomial = Polynomial.pascal(10);

        Polynomial expected = polynomial.negate();

        Polynomial difference = Polynomial.ZERO.difference(polynomial);

        assertEquals(expected, difference);
    }


    @Test
    public void testDifference() {
        double[] expected = { 1, 2, 3, 4, 5 };

        double[] coefficients = { 2, 4, 6, 8, 10 };

        Polynomial minuend = new Polynomial(coefficients);

        Polynomial subtrahend = new Polynomial(expected);

        Polynomial instance = minuend.difference(subtrahend);

        assertArrayEquals(expected, instance.getCoefficients(), 0);
    }

    @Test
    public void testSumOfCoefficientArrays() {
        double[] array1 = { 1, 2, 3, 4 };
        double[] array2 = { 4, 3, 2, 1 };
        double[] array3 = { 1, 1, 1, 1 };

        double[] expected = { 6, 6, 6, 6 };

        double[] result = Polynomial.sumOfCoefficientArrays(new double[][]{array1, array2, array3});

        assertArrayEquals(expected, result, 0);
    }

    @Test
    public void testTrimCoefficientsRemovesLeadingZero() {
        double[] array = { 0, 1, 0 };
        double[] expected = { 0, 1 };
        double[] instance = Polynomial.trimCoefficients(array);

        assertArrayEquals(expected, instance, 0);
    }

    @Test
    public void testTrimCoefficientsReturnsEMPTYForAllZeroCoefficients() {
        double[] array = { 0, 0, 0, 0 };

        double[] expected = Polynomial.EMPTY;

        double[] instance = Polynomial.trimCoefficients(array);

        assertEquals(expected, instance);
    }

    @Test
    public void testTrimCoefficientsReturnsOriginalArrayIfNoZeros() {
        double[] expected = { 1, 2, 3 };

        double[] result = Polynomial.trimCoefficients(expected);

        assertEquals(expected, result);
    }

    @Test
    public void testSigmaRejectsNegativeIncrement() {
        try {
            Polynomial.sigma(Polynomial::pascal, 0, 1, -1);
            fail();
        } catch (IllegalArgumentException iae) {
            succeed();
        }
    }

    @Test
    public void testSigmaRejectsZeroIncrement() {
        try {
            Polynomial.sigma(Polynomial::pascal, 0, 1, 0);
            fail();
        } catch (IllegalArgumentException iae) {
            succeed();
        }
    }

    @Test
    public void testSigmaReturnsZEROWhenEndLessThanStart() {
        Polynomial result = Polynomial.sigma(Polynomial::pascal, 2, 1, 1);

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testSigmaInvokesFunction() {
        boolean[] invoked = {false};

        Function<Integer, Polynomial> function = (it) -> { invoked[0] = true; return Polynomial.ZERO; };

        Polynomial result = Polynomial.sigma(function, 0, 1, 1);

        assertTrue(invoked[0]);
    }

    @Test
    public void testSigmaStartEnd() {
        int start = 2;
        int end = 5;
        int[] counter = new int[1];
        int[] indexValues = new int[end - start + 1];

        Function<Integer, Polynomial> function = (it) -> {indexValues[counter[0]++] = it; return Polynomial.pascal(it); };

        Polynomial.sigma(function, start, end);

        for (int index = 0; index < indexValues.length; index++) {
            assertEquals(index + start, indexValues[index]);
        }
    }

    @Test
    public void testSigmaReturnsSum() {

        double[] coefficients1 = { 1, 2, 3, 5, 0 };
        double[] coefficients2 = { 3, 2, 1, 0 };
        double[] coefficients3 = { 1, 1, 1, 0, 0, 5, 0 };

        double[][] coefficients = { coefficients1, coefficients2, coefficients3 };

        double[] expectedCoefficients = { 5, 5, 5, 5, 0, 5 };

        Polynomial expected = new Polynomial(expectedCoefficients);

        Function<Integer, Polynomial> function = (it) -> new Polynomial(coefficients[it]);

        Polynomial instance = Polynomial.sigma(function, 0, coefficients.length - 1, 1);

        assertEquals(expected, instance);

        assertArrayEquals(expectedCoefficients, instance.getCoefficients(), 0);
    }

    @Test
    public void testVincentsReduction() {
        double[] coefficients = { 1, -4, 3, 1 };
        double[] expectedCoefficients = { 1, -2, -1, 1 };

        Polynomial initial = new Polynomial(coefficients);

        Polynomial expected = new Polynomial(expectedCoefficients);

        Polynomial result = initial.budansTheorem();

        assertEquals(expected, result);

        assertArrayEquals(expectedCoefficients, result.getCoefficients(), 0);
    }

    @Test
    public void testSignChangesWithAlternatingSign() {
        double[] coefficients = { 1, -1, 1, -1 };
        Polynomial polynomial = Polynomial.of(coefficients);

        assertEquals(3, polynomial.signChanges());
    }

    @Test
    public void testSignChangesWithNoChange() {
        Polynomial polynomial = Polynomial.pascal(10);

        assertEquals(0, polynomial.signChanges());
    }

    @Test
    public void testSignChangesOnZERO() {
        assertEquals(0, Polynomial.ZERO.signChanges());
    }

    @Test
    public void testPolynomialOfZeroIsZERO() {
        Polynomial result = Polynomial.of(0);

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testPolynomialOfOneIsIdentity() {
        Polynomial result = Polynomial.of(1);

        assertTrue(Polynomial.IDENTITY == result);
    }

    @Test
    public void testPolynomialOfEmptyCoefficientsArrayIsZERO() {
        Polynomial result = Polynomial.of(new double[0]);

        assertTrue(Polynomial.ZERO == result);
    }

    @Test
    public void testPolynomialOfSingleCoefficientOneIsIdentity() {
        Polynomial result = Polynomial.of(1);

        assertTrue(Polynomial.IDENTITY == result);
    }

    @Test
    public void testToStringGeneratesZeroConstantForZeroPolynomial() {
        String value = Polynomial.ZERO.toString();

        assertEquals("0", value);
    }

    @Test
    public void testToStringGeneratesLeadingNegativeSign() {
        String value = Polynomial.of(-1).toString();

        assertEquals("-1", value);
    }

    @Test
    public void testToStringDoesNotGenerateLeadingPositiveSign() {
        String value = Polynomial.of( new double[] { 1 }).toString();

        assertEquals("1", value);
    }

    @Test
    public void testToStringDoesNotGenerateExponentForDegreeOneTerm() {
        String value = Polynomial.of( new double[] { 0, 1 }).toString();

        assertEquals("X", value);
    }

    @Test
    public void testToStringDoesNotGenerateCoefficientForNonzeroDegreeTerms() {
        String value = Polynomial.of( new double[] { 1, 1, 1, 1, 1 }).toString();

        assertEquals("X^4 + X^3 + X^2 + X + 1", value);
    }

    @Test
    public void testToStringDoesNotReportForZeroCoefficients() {
        String value = Polynomial.of( new double[] { 0, 1, 0, 1 }).toString();

        assertEquals("X^3 + X", value);
    }

    @Test
    public void testToStringDoesNotAddNegativeTerms() {
        String value = Polynomial.of(new double[] {1, -1, 1, -1, 1 }).toString();

        assertEquals("X^4 - X^3 + X^2 - X + 1", value);
    }

    @Test
    public void testToStringGeneratesDecimalCoefficients() {
        String value = Polynomial.of(1.11).toString();

        assertEquals("1.11", value);
    }

    @Test
    public void testFromTwoRoots() {
        double[] expectedCoefficients = new double[] { 2, -3, 1 };
        double[] roots = new double[] { 1, 2 };
        Polynomial value = Polynomial.fromRoots(roots);

        assertArrayEquals(expectedCoefficients, value.coefficients, 0);
    }

    @Test
    public void testFromThreeRoots() {
        double[] expectedCoefficients = new double[] { -8, 14, -7, 1 };
        double[] roots = new double[] { 1, 2, 4 };
        Polynomial value = Polynomial.fromRoots(roots);

        assertArrayEquals(expectedCoefficients, value.coefficients, 0);
    }

    @Test
    public void testFromFourRoots() {
        double[] expectedCoefficients = new double[] { 96, -176, 98, -19, 1 };
        double[] roots = new double[] { 1, 2, 4, 12 };
        Polynomial value = Polynomial.fromRoots(roots);

        assertArrayEquals(expectedCoefficients, value.coefficients, 0);
    }

    @Test
    public void testQuotientReducesDegree() {
        double[] dividendCoefficients = new double[] { 0, 0, 1 };
        double[] divisorCoefficients = new double[] { 0, 1 };
        double[] quotientCoefficients = new double[] { 0, 1 };
        double[] remainderCoefficients = new double[] { 0 };

        Polynomial dividend = Polynomial.of(dividendCoefficients);
        Polynomial divisor = Polynomial.of(divisorCoefficients);
        Polynomial expectedQuotient = Polynomial.of(quotientCoefficients);
        Polynomial expectedRemainder = Polynomial.of(remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }


    @Test
    public void testQuotientPerformsLongDivision() {
        double[] dividendCoefficients = new double[] { -4, 0, -2, 1 };
        double[] divisorCoefficients = new double[] { -3, 1 };
        double[] quotientCoefficients = new double[] { 3, 1, 1 };
        double[] remainderCoefficients = new double[] { 5 };

        Polynomial dividend = Polynomial.of(dividendCoefficients);
        Polynomial divisor = Polynomial.of(divisorCoefficients);
        Polynomial expectedQuotient = Polynomial.of(quotientCoefficients);
        Polynomial expectedRemainder = Polynomial.of(remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }


    @Test
    public void testQuotientPerformsLongDivisionAgain() {
        double[] dividendCoefficients = new double[] { 5, 13, 7, 2 };
        double[] divisorCoefficients = new double[] { 1, 2 };
        double[] quotientCoefficients = new double[] { 5, 3, 1 };
        double[] remainderCoefficients = new double[] { 0 };

        Polynomial dividend = Polynomial.of(dividendCoefficients);
        Polynomial divisor = Polynomial.of(divisorCoefficients);
        Polynomial expectedQuotient = Polynomial.of(quotientCoefficients);
        Polynomial expectedRemainder = Polynomial.of(remainderCoefficients);

        PolynomialQuotient result = dividend.quotient(divisor);

        assertEquals(expectedQuotient, result.quotient);
        assertEquals(expectedRemainder, result.remainder);
    }

    @Test
    public void testQuotientOfProductAndMultiplicandIsOtherMultiplicand() {
        Polynomial multiplicandA = Polynomial.of(new double[] { 12, 7, 5, 3, 8, 22, -3, -8, 1 });
        Polynomial multiplicandB = Polynomial.of(new double[] { -3, 4, 7, -8, 12 });

        Polynomial product = multiplicandA.product(multiplicandB);

        PolynomialQuotient result = product.quotient(multiplicandB);

        assertEquals(Polynomial.ZERO, result.remainder);
        assertEquals(multiplicandA, result.quotient);
    }
}
