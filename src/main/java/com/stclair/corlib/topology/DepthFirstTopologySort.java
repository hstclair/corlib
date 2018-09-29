package com.stclair.corlib.topology;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class DepthFirstTopologySort<T> implements TopologySort<T> {

    boolean nodeFirst;
    boolean suppressRepeats;

    Navigator<T> navigator;

    /**
     * constructor
     */
    public DepthFirstTopologySort() {
    }

    /**
     * constructor
     * @param navigator implementation of Navigator to be used to identify the neigbors of a graph node
     */
    public DepthFirstTopologySort(Navigator<T> navigator) {
        this.navigator = navigator;
    }

    /**
     * set the navigator instance to be used to traverse the graph during sorting
     * @param navigator implementation of Navigator to be used to identify the neigbors of a graph node
     * @param <X>
     * @return this topology sort instance
     */
    public <X> DepthFirstTopologySort<X> setNavigator(Navigator<X> navigator) {
        this.navigator = (Navigator<T>) navigator;

        return (DepthFirstTopologySort<X>) this;
    }

    /**
     * set the value of the Suppress Repeats flag
     * @param suppressRepeats true if the sorted list of graph nodes is to exclude repeat appearances of member nodes
     */
    public void setSuppressRepeats(boolean suppressRepeats) {
        this.suppressRepeats = suppressRepeats;
    }

    /**
     * set the value of the Node First flag
     * @param nodeFirst true if a given Node must precede its descendants in the result set
     */
    public void setNodeFirst(boolean nodeFirst) {
        this.nodeFirst = nodeFirst;
    }

    /**
     * sort a directed graph from the provided start (i.e. root) node
     * @param start the graph node from which the sort is to begin
     * @return a list of the graph's elements sorted according to the Depth-First algorithm
     */
    public List<T> sort(T start) {
        if (navigator == null)
            throw new IllegalStateException("Navigator not specified");

        return sort(start, navigator);
    }

    public <X> void postprocessAncestor(LinkedList<X> ancestors, LinkedList<X> result) {

        X instance = ancestors.remove(0);

        if (! nodeFirst)
            result.add(instance);
    }


    public <X> LinkedList<X> getQueueFromStack(LinkedList<LinkedList<X>> stack, LinkedList<X> ancestors, LinkedList<X> result) {

        LinkedList<X> queue = new LinkedList<>();

        while ((! stack.isEmpty()) && queue.isEmpty()) {

            queue = stack.remove(0);

            if (! ancestors.isEmpty())
                postprocessAncestor(ancestors, result);
        }

        return queue;
    }

    public <X> X preprocessAncestor(LinkedList<X> queue, LinkedList<X> result) {

        X instance = queue.remove(0);

        if (nodeFirst)
            result.add(instance);

        return instance;
    }

    public <X> LinkedList<X> procesNeighbors(X instance, Navigator<X> navigator, LinkedList<X> ancestors, LinkedList<LinkedList<X>> stack, LinkedList<X> queue, LinkedList<X> observed, LinkedList<X> result) {

        List<X> neighbors = navigator.neighbors(instance);

        neighbors = filterRepeats(observed, neighbors);

        LinkedList<X> recursion = new LinkedList<>(neighbors);

        recursion.retainAll(ancestors);

        if (recursion.size() != 0)
            throw new RecursionException("Recursive element", ancestors.get(0), recursion.get(0));

        if (neighbors.size() != 0) {
            ancestors.add(0, instance);

            stack.add(0, queue);

            queue = new LinkedList<>(neighbors);
        } else {
            if (! nodeFirst)
                result.add(instance);
        }

        return queue;
    }

    public <X> List<X> orgSort(X start, Navigator<X> navigator) {

        LinkedList<X> queue = new LinkedList<>();
        LinkedList<LinkedList<X>> stack = new LinkedList<>();
        LinkedList<X> result = new LinkedList<>();
        LinkedList<X> observed = new LinkedList<>();
        LinkedList<X> ancestors = new LinkedList<>();

        queue.add(start);
        filterRepeats(observed, queue);

        stack.add(0, queue);

        queue = new LinkedList<>();


        while (! stack.isEmpty()) {

            if (queue.isEmpty())
                queue = getQueueFromStack(stack, ancestors, result);

            if (queue.isEmpty())
                continue;

            X instance = preprocessAncestor(queue, result);

            List<X> neighbors = navigator.neighbors(instance);

            neighbors = filterRepeats(observed, neighbors);

            LinkedList<X> recursion = new LinkedList<>(neighbors);

            recursion.retainAll(ancestors);

            if (recursion.size() != 0)
                throw new RecursionException("Recursive element", ancestors.get(0), recursion.get(0));

            if (neighbors.size() != 0) {
                ancestors.add(0, instance);

                stack.add(0, queue);

                queue = new LinkedList<>(neighbors);
            } else {
                if (! nodeFirst)
                    result.add(instance);
            }
        }

        return result;
    }

//    public <X> List<X> newSort(X start, Navigator<X> navigator) {
//
//        Accumulator<X> accumulator = new Accumulator<>(start, navigator, this.nodeFirst, this.suppressRepeats);
//
//        while (! accumulator.isEmpty()) {
//
//            accumulator.processNode();
//
//            // take node from queue and process
//        }
//    }

    public <X> List<X> sort(X start, Navigator<X> navigator) {
        return orgSort(start, navigator);
    }

    /**
     * remove any previously-observed nodes from the working list of candidates
     * @param observed the list of previously-observed nodes
     * @param candidates the list of newly-observed candidates
     * @param <X>
     * @return the list of newly-observed candidates with any repeat observations removed
     */
    <X> List<X> filterRepeats(List<X> observed, List<X> candidates) {
        if (! suppressRepeats)
            return candidates;

        candidates = new LinkedList<>(new LinkedHashSet<>(candidates));

        candidates.removeAll(observed);

        observed.addAll(candidates);

        return candidates;
    }
}



