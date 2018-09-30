package com.stclair.corlib.topology;

public interface TopologyStrategy<X> {

    TopologySorterContext<X> createContext(TopologySorter sorter, X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats);

    void refillQueue(TopologySorterContext<X> context);

    void processNode(TopologySorterContext<X> context);

    void processNeighbors(TopologySorterContext<X> context);

    boolean done(TopologySorterContext<X> context);
}
