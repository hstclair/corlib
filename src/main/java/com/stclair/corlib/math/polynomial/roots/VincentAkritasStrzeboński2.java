package com.stclair.corlib.math.polynomial.roots;


import com.stclair.corlib.math.Interval;
import com.stclair.corlib.math.RealMobiusTransformation;
import com.stclair.corlib.math.polynomial.Polynomial;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author hstclair
 * @since 8/10/15 7:20 PM
 */
public class VincentAkritasStrzeboński2 {

    private Function<Polynomial, VASOperation2> vasOperationBuilder;

    VASComputationExperimental2 processor;

    /** Construct standard VincentAkritasStrzeboński instance */
    public VincentAkritasStrzeboński2() {
        this(new VASComputationExperimental2());
    }

    /**
     * Construct VincentAkritasStrzeboński instance with custom implementation of VASComputation
     * @param vasOperationBuilder function to construct the required VASComputation implementation
     */
    public VincentAkritasStrzeboński2(VASComputationExperimental2 vasComputation) {
        this.processor = vasComputation;
    }

    public List<VASOperation2> solveRootIntervals(Polynomial polynomial) {

        // compute s = sgc(f)
        int signs = polynomial.signChanges();

        // if s == 0 return empty list
        if (signs == 0) return Collections.emptyList();

        // if s == 1 return {(0, inf)}
        if (signs == 1) {
            Interval interval = new Interval(0, Double.POSITIVE_INFINITY);

            return Collections.singletonList(new VASOperation2(null, null, null, 0d, 0d, interval, true));
        }

        return process(polynomial);
    }

    public List<VASOperation2> process(Polynomial polynomial) {

        final LinkedList<VASOperation2> queue = new LinkedList<>();

        queue.add(processor.processPolynomial(polynomial));

        LinkedList<VASOperation2> result = new LinkedList<>();

        while (! queue.isEmpty())
            result.add(processor.processOperation(queue::add, queue.removeFirst()));

        return result;
    }

    public List<Interval> findRootIntervals(Polynomial polynomial) {

        return solveRootIntervals(polynomial)
                .stream()
                .map(it -> it.root)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}




