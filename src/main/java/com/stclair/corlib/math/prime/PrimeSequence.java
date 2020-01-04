package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.math.util.Sequence;

import java.util.ArrayList;
import java.util.List;

import static com.stclair.corlib.validation.Validation.neverNull;

public class PrimeSequence<T> implements Sequence<T> {

    int index;

    OperationStrategy<T> operationStrategy;

    Sieve<T> sieve;

    public PrimeSequence(OperationStrategy<T> operationStrategy) {

        neverNull(operationStrategy, "operationStrategy");

        this.operationStrategy = operationStrategy;

        sieve = new Sieve<>(operationStrategy);

        index = 0;
    }

    @Override
    public T currentMember() {
        return sieve.getKnown(index);
    }

    @Override
    public long currentIndex() {
        return index;
    }

    @Override
    public T nextMember() {

        int next = index+1;

        if (sieve.isKnown(next)) {
            index = next;

            return sieve.getKnown(index);
        }

        T result = sieve.seekNextPrime();

        index = next;

        return result;
    }

    @Override
    public T prevMember() {

        index--;

        return sieve.getKnown(index);
    }

    @Override
    public T getMember(int index) {

        if (sieve.isKnown(index)) {
            this.index = index;

            return sieve.getKnown(index);
        }

        T result = find(index);

        this.index = index;

        return result;
    }

    public T find(int index) {

        T prime = null;

        this.index = sieve.getLastIndex();

        while (this.index++ < index)
            prime = sieve.seekNextPrime();

        return prime;
    }
}
