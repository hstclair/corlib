package com.stclair.corlib.math.polynomial.generic.roots;


import com.stclair.corlib.math.polynomial.generic.Interval;
import com.stclair.corlib.math.polynomial.generic.Polynomial;
import com.stclair.corlib.math.util.OperationStrategy;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hstclair
 * @since 8/10/15 7:20 PM
 */
public class VincentAkritasStrzeboński2<T> {

    OperationStrategy<T> op;

    private Function<Polynomial<T>, VASOperation2<T>> vasOperationBuilder;

    VASComputationExperimental2<T> processor;

    /** Construct standard VincentAkritasStrzeboński instance */
    public VincentAkritasStrzeboński2(OperationStrategy<T> op) {
        this(new VASComputationExperimental2<>(op), op);
    }

    /**
     * Construct VincentAkritasStrzeboński instance with custom implementation of VASComputation
     * @param vasOperationBuilder function to construct the required VASComputation implementation
     */
    public VincentAkritasStrzeboński2(VASComputationExperimental2<T> vasComputation, OperationStrategy<T> op) {
        this.processor = vasComputation;
        this.op = op;
    }

    public List<VASOperation2<T>> solveRootIntervals(Polynomial<T> polynomial) {

        // compute s = sgc(f)
        int signs = polynomial.signChanges();

        // if s == 0 return empty list
        if (signs == 0) return Collections.emptyList();

        // if s == 1 return {(0, inf)}
        if (signs == 1) {
            Interval<T> interval = new Interval<>(op.zero(), op.positiveInfinity(), op);

            return Collections.singletonList(new VASOperation2<>(null, null, null, null, null, interval, true, "fixed result"));
        }

        return process(polynomial);
    }

    public List<VASOperation2<T>> process(Polynomial<T> polynomial) {

        final LinkedList<VASOperation2<T>> queue = new LinkedList<>();

        queue.add(processor.processPolynomial(polynomial));

        LinkedList<VASOperation2<T>> result = new LinkedList<>();

        while (! queue.isEmpty())
            result.add(processor.processOperation(queue::add, queue.removeFirst()));

        return result;
    }

    public List<Interval<T>> findRootIntervals(Polynomial<T> polynomial) {

        return solveRootIntervals(polynomial)
                .stream()
                .map(it -> it.root)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}




