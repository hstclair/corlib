package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class ParentChildGrandchildChildTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement childA;

    TopologyElement childB;

    TopologyElement grandchild;

    public ParentChildGrandchildChildTestCase() {

        grandchild = new TopologyElement("grandchild");

        childA = new TopologyElement("kidA");

        childB = new TopologyElement("kidB");

        parent = new TopologyElement("parent");

        childA.setNeighbors(grandchild);

        parent.setNeighbors(childA, childB);
    }

    @Override
    protected void fail(String unexpected_recursion_exception) {
        fail(unexpected_recursion_exception);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:

                return Arrays.asList(parent, childA, grandchild, childB);

            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, childA, childB, grandchild);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(grandchild, childA, childB, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }
}
