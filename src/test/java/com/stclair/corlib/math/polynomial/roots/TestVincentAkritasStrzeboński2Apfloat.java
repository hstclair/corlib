package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.apfloat.ApfloatInfinite;
import com.stclair.corlib.math.apfloat.ApfloatInfiniteOperationStrategy;
import com.stclair.corlib.math.util.ApfloatOperationStrategy;
import org.apfloat.Apfloat;
import org.junit.Test;

public class TestVincentAkritasStrzeboński2Apfloat {

    VincentAkritasStrzeboński2Tester<ApfloatInfinite> test = new VincentAkritasStrzeboński2Tester<>(new ApfloatInfiniteOperationStrategy());

    public TestVincentAkritasStrzeboński2Apfloat() {

    }

    @Test
    public void smokeTest() {
        test.smokeTest();
    }

    @Test
    public void smokeTestExperimentalVASOperation() {
        test.smokeTestExperimentalVASOperation();
    }

    @Test
    public void vasReturnsSingleRoot() {
        test.vasReturnsSingleRoot();
    }

    @Test
    public void vasReturnsZeroToOneAndOneToInfinity() {
        test.vasReturnsZeroToOneAndOneToInfinity();
    }

    @Test
    public void vasReturnsZeroToInfinityRangeForFunctionWithOneSignChange() {
        test.vasReturnsZeroToInfinityRangeForFunctionWithOneSignChange();
    }

    @Test
    public void smokeTest2ExperimentalVASOperation() {
        test.smokeTest2ExperimentalVASOperation();
    }

    @Test
    public void factorEquationWithTwoRoots() {
        test.factorEquationWithTwoRoots();
    }

    @Test
    public void factorEquationWithThreeRoots() {
        test.factorEquationWithThreeRoots();
    }

    @Test
    public void factorEquationWithFourRoots() {
        test.factorEquationWithFourRoots();
    }

    @Test
    public void factorEquationWithFiveRoots() {
        test.factorEquationWithFiveRoots();
    }

    @Test
    public void factorEquationWithSixRoots() {
        test.factorEquationWithSixRoots();
    }

    @Test
    public void factorEquationWithFifteenRoots() {
        test.factorEquationWithFifteenRoots();
    }

    @Test
    public void factorEquationWithTwentyTwoRoots() {
        test.factorEquationWithTwentyTwoRoots();
    }

    @Test
    public void factorEquationWithTwentyTwoPrimeRoots() {
        test.factorEquationWithTwentyTwoPrimeRoots();
    }

    @Test
    public void factorEquationWithTwentyTwoPrimeRootsShowWork() {
        test.factorEquationWithTwentyTwoPrimeRootsShowWork();
    }
}

