package com.stclair.corlib.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeapsAlgorithmRecursive implements Permutations {



    /*
    procedure generate(k : integer, A : array of any):
    if k = 1 then
        output(A)
    else
        // Generate permutations with kth unaltered
        // Initially k == length(A)
        generate(k - 1, A)

        // Generate permutations for kth swapped with each k-1 initial
        for i := 0; i < k-1; i += 1 do
            // Swap choice dependent on parity of k (even or odd)
            if k is even then
                swap(A[i], A[k-1]) // zero-indexed, the kth is at k-1
            else
                swap(A[0], A[k-1])
            end if
            generate(k - 1, A)

        end for
    end if
     */


    /**
     * translation of pseudocode for recursive implementation of Heap's Algorithm (harvested from wikipedia page)
     */
    public <T> List<T[]> of(T[] values) {

        List<T[]> result = new ArrayList<>();

        generate(values.length - 1, values, result);

        return result;
    }

    public <T> T[] generate(int element, T[] array, List<T[]> result) {

        if (element == 0) {
            result.add(array);
            return array;
        }

        // Generate permutations for kth swapped with each k-1 initial
        for (int index = 0; index < element; index++)
            array = heapsAlgorithmSwap(generate(element - 1, array, result), element, index);

        return generate(element - 1, array, result);
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
