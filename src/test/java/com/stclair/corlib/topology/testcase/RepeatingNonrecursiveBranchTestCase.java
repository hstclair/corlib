package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.List;

public class RepeatingNonrecursiveBranchTestCase extends TopologyTestCase {

    TopologyElement parent;

    TopologyElement branch;

    TopologyElement leaf;

    public RepeatingNonrecursiveBranchTestCase() {

        parent = new TopologyElement("parent");

        branch = new TopologyElement("branch");

        leaf = new TopologyElement("leaf");

        branch.setNeighbors(leaf, leaf);

        parent.setNeighbors(branch, branch, branch);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    public List<TopologyElement> getExpectedFilteredResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:
            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, branch, leaf);


            case NodeLastDepthFirst:
            case NodeLastBreadthFirst:

                return Arrays.asList(leaf, branch, parent);


            default:
                throw new UnsupportedOperationException();
        }
    }

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        switch (sorterSubtype) {

            case NodeFirstDepthFirst:

                return Arrays.asList(parent, branch, leaf, leaf, branch, leaf, leaf, branch, leaf, leaf);

            case NodeFirstBreadthFirst:

                return Arrays.asList(parent, branch, branch, branch, leaf, leaf, leaf, leaf, leaf, leaf);


            case NodeLastDepthFirst:

                return Arrays.asList(leaf, leaf, branch, leaf, leaf, branch, leaf, leaf, branch, parent);

            case NodeLastBreadthFirst:

                return Arrays.asList(leaf, leaf, leaf, leaf, leaf, leaf, branch, branch, branch, parent);

            default:
                throw new UnsupportedOperationException();
        }
    }
}
