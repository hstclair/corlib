package com.stclair.corlib.topology;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class TopologySorter<T> implements TopologySort<T> {

    boolean nodeFirst;
    boolean depthFirst;
    boolean suppressRepeats;

    Navigator<T> navigator;

    /**
     * constructor
     */
    public TopologySorter() {
    }

    /**
     * constructor
     * @param navigator implementation of Navigator to be used to identify the neigbors of a graph node
     */
    public TopologySorter(Navigator<T> navigator) {
        this.navigator = navigator;
    }

    /**
     * set the navigator instance to be used to traverse the graph during sorting
     * @param navigator implementation of Navigator to be used to identify the neigbors of a graph node
     * @param <X>
     * @return this topology sort instance
     */
    public <X> TopologySorter<X> setNavigator(Navigator<X> navigator) {
        this.navigator = (Navigator<T>) navigator;

        return (TopologySorter<X>) this;
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

    public TopologySorter<T> depthFirst() {
        depthFirst = true;
        return this;
    }

    public TopologySort<T> breadthFirst() {
        depthFirst = false;

        return this;
    }

    public <X> List<X> sort(X start, Navigator<X> navigator) {

        if (depthFirst)
            return sort(start, navigator, new DepthFirstTopologyStrategy<>());
        else
            return sort(start, navigator, new BreadthFirstTopologyStrategy<>());
    }

    public <X> List<X> sort(X start, Navigator<X> navigator, TopologyStrategy<X> strategy) {

        TopologySorterContext<X> context = strategy.createContext(this, start, navigator, nodeFirst, suppressRepeats);

        while (! strategy.done(context)) {

            strategy.refillQueue(context);

            if (context.getQueue().isEmpty())
                continue;

            strategy.processNode(context);

            findNeighbors(context);

            strategy.processNeighbors(context);
        }

        return context.getResult();
    }

    <X> void findNeighbors(TopologySorterContext<X> context) {

        X node = context.getNode();

        List<X> neighbors = context.getNavigator().neighbors(node);

        context.setNeighbors(new LinkedList<>(neighbors));

        context.setNeighbors(filterRepeats(context));

        LinkedList<X> recursion = new LinkedList<>(context.getNeighbors());

        recursion.retainAll(context.getAncestors());

        if (recursion.size() != 0)
            throw new RecursionException("Recursive element", context.getAncestors().get(0), recursion.get(0));
    }

    <X> void registerFirstNode(TopologySorterContext<X> context) {

        if (! context.suppressRepeats())
            return;

        context.getObserved().add(context.getNode());
    }

    /**
     * remove any previously-observed nodes from the working list of candidates
     * @param <X>
     * @return the list of newly-observed candidates with any repeat observations removed
     */
    <X> LinkedList<X> filterRepeats(TopologySorterContext<X> context) {

        if (! context.suppressRepeats())
            return context.getNeighbors();

        context.setNeighbors(new LinkedList<>(new LinkedHashSet<>(context.getNeighbors())));

        context.getNeighbors().removeAll(context.getObserved());

        context.getObserved().addAll(context.getNeighbors());

        return context.getNeighbors();
    }
}



