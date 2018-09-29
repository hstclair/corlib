package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class RepeatingNonrecursiveLeafTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement child;

    public RepeatingNonrecursiveLeafTestCase() {

        parent = new TopologyElement("parent");

        child = new TopologyElement("child");

        parent.setNeighbors(child, child, child);
    }

    @Override
    protected void fail(String unexpected_recursion_exception) {
        fail(unexpected_recursion_exception);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    public List<TopologyElement> getExpectedFilteredResult(SorterSubtype sorterSubtype) {

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

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:
            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, child, child, child);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(child, child, child, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }
}
