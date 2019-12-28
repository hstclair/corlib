package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapsAlgorithmNonrecursive implements Permutations {


    /**
     * translation of pseudocode for non-recursive implementation of Heap's Algorithm (harvested from wikipedia page)
     */
    public <T> List<T[]> of(T[] values) {

        List<T[]> result = new ArrayList<>();

        //c is an encoding of the stack state. c[k] encodes the for-loop counter for when generate(k+1, A) is called
        int[] stack = new int[values.length];

        result.add(values);

        //i acts similarly to the stack pointer
        int stackIndex = 0;

        while (stackIndex < values.length) {

            if (stack[stackIndex] < stackIndex) {

                values = heapsAlgorithmSwap(values, stackIndex, stack[stackIndex]);

                result.add(values);
                //Swap has occurred ending the for-loop. Simulate the increment of the for-loop counter
                stack[stackIndex] += 1;
                //Simulate recursive call reaching the base case by bringing the pointer to the base case analog in the array
                stackIndex=0;
            } else {
                //Calling generate(i+1, A) has ended as the for-loop terminated. Reset the state and simulate popping the stack by incrementing the pointer.
                stack[stackIndex] =0;
                stackIndex += 1;
            }
        }

        return result;
    }

    public <T> T[] heapsAlgorithmSwap(T[] values, int a, int b) {

        if ((a & 1) == 0)
            b = values.length - 1;
        else
            b = values.length - b - 1;

        a = values.length - a - 1;

        values = Arrays.copyOf(values, values.length);

        T temp = values[a];

        values[a] = values[b];

        values[b] = temp;

        return values;
    }
}
