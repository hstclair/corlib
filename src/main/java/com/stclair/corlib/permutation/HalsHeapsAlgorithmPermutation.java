package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HalsHeapsAlgorithmPermutation implements Permutations {

    @Override
    public <T> List<T[]> of(T[] values) {

        List<T[]> result = new ArrayList<>();

        result.add(Arrays.copyOf(values, values.length));

        int[] register = new int[values.length - 1];

        int[] pair;

        while ((pair = computeNextPair(register)) != null)
            values = heapsAlgorithmSwapAndStore(result, values, pair[0], pair[1]);

        return result;
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

    public <T> T[] heapsAlgorithmSwapAndStore(List<T[]> result, T[] values, int a, int b) {

        values = Arrays.copyOf(values, values.length);

        if ((a & 1) == 0)
            b = values.length - 1;
        else
            b = values.length - b - 1;

        a = values.length - a - 1;

        T temp = values[a];

        values[a] = values[b];

        values[b] = temp;

        result.add(values);

        return values;
    }
}
