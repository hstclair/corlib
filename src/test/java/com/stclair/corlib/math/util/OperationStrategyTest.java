package com.stclair.corlib.math.util;

import com.stclair.corlib.math.apfloat.ApfloatInfinite;
import com.stclair.corlib.math.apfloat.ApfloatInfiniteOperationStrategy;
import com.stclair.corlib.math.prime.PrimeSequence;
import com.stclair.corlib.math.prime.SquareSequence;
import org.apfloat.Apfloat;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OperationStrategyTest {

    OperationStrategy<Apfloat> operationStrategy = new ApfloatOperationStrategy();

    OperationStrategy<Double> doubleOp = new DoubleOperationStrategy();

    @Test
    public void toBaseF() {

        String expected = "54321";

        Double dblValue = (double) MoreMath.factorial(6) - 1;

        String actual = doubleOp.toBaseF(dblValue);

        assertEquals(expected, actual);
    }

    @Ignore
    @Test
    public void firstHundredPrimesToBaseF() {

        Sequence<Double> primes = new PrimeSequence<>(doubleOp);

        for (int count = 0; count < 100; count++) {
            double prime = primes.nextMember();
            System.out.printf("%1s: %2s -> %3s\n", count, prime, doubleOp.toBaseF(prime));
        }
    }


    @Ignore
    @Test
    public void firstHundredSquaresToBaseF() {

        Sequence<Double> squares = new SquareSequence<>(doubleOp);

        for (int count = 0; count < 100; count++) {
            double square = squares.nextMember();
            System.out.printf("%1s (%2s) -> %3s (%4s)\n", count, doubleOp.toBaseF((double) count), (int) square, doubleOp.toBaseF(square));
        }
    }

    @Ignore
    @Test
    public void firstTwoHundredPrimeDeltas() {

        String emptyString = "";

        PrimeSequence<Double> primes = new PrimeSequence<>(doubleOp);

//        List<Integer> deltas = new ArrayList<>();
//        List<Integer> delta2s = new ArrayList<>();

        double lastPrime = 1;
        int delta = 0;

        for (int count = 0; count < 1000; count++) {

            double prime = primes.nextMember();

            int lastDelta = delta;
            delta = ((int)prime - (int)lastPrime);
            int delta2 = delta - lastDelta;

            String sequence = ""; // delta2s.stream().map(it -> Integer.toString(it)).reduce(emptyString, (a, b) -> a == emptyString ? b : b == emptyString ? a: String.format("%s, %s", a, b));

            System.out.printf("%s: (%s:%s)\n", (int) prime, delta, delta2);

            lastPrime = prime;
        }
    }
}