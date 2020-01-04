package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.math.util.Sequence;

import java.util.ArrayList;
import java.util.List;

import static com.stclair.corlib.validation.Validation.neverNull;

public class Sieve<T> {

    T current;

    T two;

    T square;

    T nextSquare;

    T squareRoot;

    List<T> known;

    Sequence<T> squares;

    OperationStrategy<T> operationStrategy;

    public Sieve(OperationStrategy<T> operationStrategy) {

        this.operationStrategy = neverNull(operationStrategy, "operationStrategy");
        known = new ArrayList<>();

        // the first two primes are 2 and 3 after these, all subsequent primes
        // will be arrive in increments of at least two.  We will pre-populate
        // the list with these so that our least increment to the next candidate
        // will always be 2.
        two = operationStrategy.increment(operationStrategy.one());

        current = null;

        // the greatest factor of the current prime will always be less than
        // or equal to the square root of the greatest integer square that is
        // less than or equal to the current candidate.  Therefore, we will
        // use squares and their roots to box our search for factors
        squares = new SquareSequence<>(operationStrategy);

        // since we will begin our search after 3 and proceeding in increments of 2 (i.e. at 5),
        // our initial greatest integer square will be 4
        square = squares.getMember(2);
        squareRoot = two;

        // we will capture the next square in the sequence so that we can test for the upper limit.
        // when we encounter a candidate that is greater than or equal to this next square, we know
        // that we can extend our search for factors by one.
        nextSquare = squares.nextMember();
    }

    public T foundPrime(T prime) {
        known.add(prime);
        current = prime;
        return prime;
    }

    public T seekNextPrime() {

        if (current == null)
            return foundPrime(two);

        if (current == two)
            return foundPrime(operationStrategy.increment(two));

        T candidate = operationStrategy.sum(current, two);

        while (hasFactors(candidate))
            candidate = operationStrategy.sum(candidate, two);

        return foundPrime(candidate);
    }

    public boolean isKnown(int index) {
        return known.size() > index;
    }

    public T getKnown(int index) {
        return known.get(index);
    }

    public int getLastIndex() {
        return known.size() - 1;
    }

    public boolean hasFactors(T candidate) {

        if (operationStrategy.isEqual(candidate, nextSquare)) {
            computeNextSquare();
            return true;
        }

        if (operationStrategy.greaterThan(candidate, nextSquare))
            computeNextSquare();

        boolean first = true;

        for (T possibleFactor : known) {

            if (first) {
                // we are only testing odd integers so we will never
                // need to test for numbers divisible by two (the first entry
                // in our list list of known primes)
                first = false;
                continue;
            }

            if (operationStrategy.greaterThan(possibleFactor, squareRoot))
                return false;

            T quotient = operationStrategy.quotient(candidate, possibleFactor);

            if (operationStrategy.isEqual(quotient, operationStrategy.floor(quotient)))
                return true;
        }

        return false;
    }

    public void computeNextSquare() {
        square = nextSquare;
        squareRoot = operationStrategy.increment(squareRoot);
        nextSquare = squares.nextMember();
    }
}
