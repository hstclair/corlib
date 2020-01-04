package com.stclair.corlib.math.prime;

import com.stclair.corlib.math.util.OperationStrategy;
import com.stclair.corlib.math.util.Sequence;

import java.util.ArrayList;
import java.util.List;

import static com.stclair.corlib.validation.Validation.inRange;
import static com.stclair.corlib.validation.Validation.neverNull;

public class SquareSequence<T> implements Sequence<T> {

    OperationStrategy<T> operationStrategy;

    List<T> known;

    T squareRoot;

    T currentSquare;

    T oddInt;

    T two;

    int currentIndex = -1;

    public SquareSequence(OperationStrategy<T> operationStrategy) {
        this.operationStrategy = neverNull(operationStrategy, "operationStrategy");
        known = new ArrayList<>();

        two = operationStrategy.increment(operationStrategy.one());
        oddInt = operationStrategy.one();

        currentSquare = operationStrategy.zero();
        squareRoot = operationStrategy.zero();
    }

    public T nextMember() {

        currentIndex++;

        if (known.size() > currentIndex)
            return known.get(currentIndex);

        if (currentIndex == 0)
            return currentSquare;

        currentSquare = operationStrategy.sum(currentSquare, oddInt);

        squareRoot = operationStrategy.increment(squareRoot);

        oddInt = operationStrategy.sum(oddInt, two);

        return currentSquare;
    }

    public T prevMember() {

        inRange(currentIndex, 1, currentIndex, "currentIndex");

        currentIndex--;
        return known.get(currentIndex);
    }

    public T getMember(int index) {

        inRange(index, 0, index, "index");

        if (index < known.size()) {
            currentIndex = index;
            return known.get(currentIndex);
        }

        while (currentIndex < index)
            nextMember();

        return currentMember();
    }

    @Override
    public T currentMember() {
        return currentSquare;
    }

    @Override
    public long currentIndex() {
        return currentIndex;
    }
}
