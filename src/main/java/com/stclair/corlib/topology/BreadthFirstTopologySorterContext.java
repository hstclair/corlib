package com.stclair.corlib.topology;

import java.util.LinkedList;

public class BreadthFirstTopologySorterContext<X> extends TopologySorterContext<X> {

    LinkedList<LinkedList<X>> neighborsQueue = new LinkedList<>();

    LinkedList<LinkedList<X>> ancestorsQueue = new LinkedList<>();

    public BreadthFirstTopologySorterContext(X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {
        super(node, navigator, nodeFirst, suppressRepeats);
    }

    public LinkedList<LinkedList<X>> getNeighborsQueue() {
        return neighborsQueue;
    }

    public LinkedList<LinkedList<X>> getAncestorsQueue() {
        return ancestorsQueue;
    }

}
