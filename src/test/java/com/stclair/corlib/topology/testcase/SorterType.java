package com.stclair.corlib.topology.testcase;

public enum SorterType {

    DepthFirst(SorterSubtype.NodeFirstDepthFirst, SorterSubtype.NodeLastDepthFirst),
    BreadthFirst(SorterSubtype.NodeFirstBreadthFirst, SorterSubtype.NodeLastBreadthFirst);

    SorterSubtype nodeFirst;

    SorterSubtype nodeLast;

    SorterType(SorterSubtype nodeFirst, SorterSubtype nodeLast) {
        this.nodeFirst = nodeFirst;
        this.nodeLast = nodeLast;
    }
}
