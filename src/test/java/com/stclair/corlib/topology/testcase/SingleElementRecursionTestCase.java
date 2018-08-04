package com.stclair.corlib.topology.testcase;

import java.util.List;

public class SingleElementRecursionTestCase extends TopologyTestCase {

    TopologyElement parent;

    public SingleElementRecursionTestCase() {

        parent = new TopologyElement("parent");

        parent.setNeighbors(parent);
    }

    @Override
    public TopologyElement getRoot() {
        return parent;
    }

    @Override
    public List<TopologyElement> getExpectedFilteredResult(SorterSubtype sorterSubtype) {

        return List.of(parent);
    }

    public List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype) {

        return null;
    }
}
