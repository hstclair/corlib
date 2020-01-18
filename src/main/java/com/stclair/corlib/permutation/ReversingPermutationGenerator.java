package com.stclair.corlib.permutation;

import com.stclair.corlib.collection.Tuple;

import java.util.*;
import java.util.function.Function;

public class ReversingPermutationGenerator {

    public <T> List<T[]> listPermutationsOf(T[] values) {
        List<Function<T[], T[]>> functions = new ArrayList<>();

        functions = permutations(values.length);

        List<T[]> result = new ArrayList<>();

        for (Function<T[], T[]> function: functions)
            result.add(values = function.apply(values));

        return result;
    }

    public static <T> List<Function<T[], T[]>> permutations(int count) {

        Stack<Tuple<Integer, Integer>> stack = new Stack<>();
        List<Function<T[], T[]>> result = new ArrayList<>();

        result.add((values) -> values);

        buildStack(stack, count, count);

        while (! stack.empty()) {
            Tuple<Integer, Integer> state = stack.pop();

            if (state.getB() == 0)
                continue;

            result.add(permutation(state.getA()));

            buildStack(stack, state.getA(), state.getB());
        }

        return result;
    }

    public static void buildStack(Stack<Tuple<Integer, Integer>> stack, int frame, int count) {

        while (frame > 1) {

            if (--count > 0)
                stack.push(new Tuple<>(frame, count));

            frame--;
            count = frame;
        }
    }

    public static <T> Function<T[], T[]> permutation(int length) {

        return (values) -> {

            values = Arrays.copyOf(values, values.length);

            int count = length >> 1;

            int a = values.length - 1;
            int b = values.length - length;

            while (count-- != 0) {
                T temp = values[a];
                values[a] = values[b];
                values[b] = temp;

                a--;
                b++;
            }

            return values;
        };
    }
}
