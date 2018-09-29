package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class ParentChildGrandchildTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement child;

    TopologyElement grandchild;

    public ParentChildGrandchildTestCase() {

        grandchild = new TopologyElement("grandchild");

        child = new TopologyElement("child");

        child.setNeighbors(grandchild);

        parent = new TopologyElement("parent");

        parent.setNeighbors(child);
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
            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, child, grandchild);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(grandchild, child, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }
}
