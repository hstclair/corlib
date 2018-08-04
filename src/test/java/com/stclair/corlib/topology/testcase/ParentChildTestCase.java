package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class ParentChildTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement child;

    public ParentChildTestCase() {

        child = new TopologyElement("child");

        parent = new TopologyElement("parent");

        parent.setNeighbors(child);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:
            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, child);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(child, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }
}
