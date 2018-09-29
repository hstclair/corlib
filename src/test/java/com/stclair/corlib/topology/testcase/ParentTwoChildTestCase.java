package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class ParentTwoChildTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement childA;

    TopologyElement childB;

    public ParentTwoChildTestCase() {

        childA = new TopologyElement("kidA");

        childB = new TopologyElement("kidB");

        parent = new TopologyElement("parent");

        parent.setNeighbors(childA, childB);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    @Override
    protected void fail(String unexpected_recursion_exception) {
        fail(unexpected_recursion_exception);
    }

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:
            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, childA, childB);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(childA, childB, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }
}
