package com.stclair.corlib.topology;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class BreadthFirstTopologySort<T> implements TopologySort<T> {

    boolean nodeFirst;
    boolean suppressRepeats;

    Navigator<T> navigator;

    public BreadthFirstTopologySort() {
    }

    public BreadthFirstTopologySort(Navigator<T> navigator) {
        this.navigator = navigator;
    }

    public <X> BreadthFirstTopologySort<X> setNavigator(Navigator<X> navigator) {
        this.navigator = (Navigator<T>) navigator;

        return (BreadthFirstTopologySort<X>) this;
    }

    public void setSuppressRepeats(boolean suppressRepeats) {
        this.suppressRepeats = suppressRepeats;
    }

    public void setNodeFirst(boolean nodeFirst) {
        this.nodeFirst = nodeFirst;
    }

    public List<T> sort(T start) {
        if (navigator == null)
            throw new IllegalStateException("Navigator not specified");

        return sort(start, navigator);
    }

    public <X> List<X> sort(X start, Navigator<X> navigator) {

        LinkedList<LinkedList<X>> ancestorsQueue = new LinkedList<>();
        LinkedList<LinkedList<X>> neighborsQueue = new LinkedList<>();

        LinkedList<X> queue = new LinkedList<>();
        LinkedList<X> result = new LinkedList<>();
        LinkedList<X> observed = new LinkedList<>();
        LinkedList<X> ancestors = new LinkedList<>();

        queue.add(start);
        filterRepeats(observed, queue);
        neighborsQueue.add(queue);
        queue = new LinkedList<>();
        ancestorsQueue.add(new LinkedList<>());


        while (! (neighborsQueue.isEmpty() && queue.isEmpty())) {

            while (queue.isEmpty() && !neighborsQueue.isEmpty()) {
                queue = neighborsQueue.remove(0);
                ancestors = ancestorsQueue.remove(0);
            }

            if (!queue.isEmpty()) {

                X instance = queue.remove(0);

                if (nodeFirst) {
                    result.add(instance);
                } else {
                    result.add(0, instance);
                }

                ancestors.add(0, instance);

                List<X> neighbors = navigator.neighbors(instance);

                neighbors = filterRepeats(observed, neighbors);

                LinkedList<X> recursion = new LinkedList<>(neighbors);

                recursion.retainAll(ancestors);

                if (recursion.size() != 0)
                    throw new RecursionException("Recursive element", ancestors.get(0), recursion.get(0));

                LinkedList<X> filteredNeighbors = new LinkedList<>(neighbors);

                if (!nodeFirst) {
                    Collections.reverse(filteredNeighbors);

                    neighborsQueue.add(0, filteredNeighbors);
                    ancestorsQueue.add(0, ancestors);

                } else {
                    neighborsQueue.add(filteredNeighbors);
                    ancestorsQueue.add(ancestors);
                }

                ancestors.remove(0);
            }
        }

        return result;
    }

    <X> List<X> filterRepeats(List<X> observed, List<X> candidates) {
        if (! suppressRepeats)
            return candidates;

        candidates = new LinkedList<>(new LinkedHashSet<>(candidates));

        candidates.removeAll(observed);

        observed.addAll(candidates);

        return candidates;
    }
}



