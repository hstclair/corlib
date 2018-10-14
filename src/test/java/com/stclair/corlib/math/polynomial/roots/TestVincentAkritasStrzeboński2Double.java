package com.stclair.corlib.math.polynomial.roots;

import com.stclair.corlib.math.util.DoubleOperationStrategy;
import org.junit.Test;

public class TestVincentAkritasStrzeboński2Double {

    VincentAkritasStrzeboński2Tester<Double> test = new VincentAkritasStrzeboński2Tester<>(new DoubleOperationStrategy());

    public TestVincentAkritasStrzeboński2Double() {

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
}
