package com.stclair.corlib.permutation;

import java.util.Arrays;

public class HalsHeapsAlgorithmIterator<T> {

    public int[] pair;

    public int[] register;

    public HalsHeapsAlgorithmIterator(T[] values) {

        this.register = new int[values.length - 1];

        pair = computeNextPair(register);
    }

    public boolean hasNext(T[] currentValue) {  // per java docs, this method invoked BEFORE currentValue appended to Stream

        return currentValue != null;
    }

    public T[] next(T[] currentValue) {  // per java docs, this method invoked AFTER currentValue appended to stream

        if (pair == null)
            return null;

        currentValue = heapsAlgorithmSwapAndStore(currentValue, pair[0], pair[1]);

        pair = computeNextPair(register);

        return currentValue;
    }

    public int[] computeNextPair(int[] alternateElementPosRegister) {

        int index = 0;

        while (index < alternateElementPosRegister.length) {

            int alternateElementPos = alternateElementPosRegister[index];

            int elementPos = index + 1;

            if (alternateElementPos < elementPos) {
                alternateElementPosRegister[index] = alternateElementPos + 1;

                return new int[] {elementPos, alternateElementPos};
            }

            alternateElementPosRegister[index++] = 0;
        }

        return null;
    }

    public T[] heapsAlgorithmSwapAndStore(T[] values, int a, int b) {

        values = Arrays.copyOf(values, values.length);

        if ((a & 1) == 0)
            b = values.length - 1;
        else
            b = values.length - b - 1;

        a = values.length - a - 1;

        T temp = values[a];

        values[a] = values[b];

        values[b] = temp;

        return values;
    }
}
