package com.stclair.corlib.topology;

import java.util.LinkedList;

public class DepthFirstTopologySorterContext<X> extends TopologySorterContext<X> {

    LinkedList<LinkedList<X>> stack = new LinkedList<>();

    public DepthFirstTopologySorterContext(X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {
        super(node, navigator, nodeFirst, suppressRepeats);
    }

    public LinkedList<LinkedList<X>> getStack() {
        return stack;
    }
}
