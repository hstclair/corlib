package com.stclair.corlib.topology.testcase;

import com.stclair.corlib.topology.RecursionException;
import com.stclair.corlib.topology.TopologySort;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class TopologyTestCase {


    public abstract TopologyElement getRoot();

    public abstract List<TopologyElement> getExpectedResult(SorterSubtype sorterSubtype);

    public List<TopologyElement> getExpectedFilteredResult(SorterSubtype sorterSubtype) {
        return getExpectedResult(sorterSubtype);
    }

    void apply(TopologySort<?> topologySort, SorterType sorterType, boolean nodeFirst, boolean suppressRepeats, boolean typed) {

        List<TopologyElement> actual;

        topologySort.setNodeFirst(nodeFirst);
        topologySort.setSuppressRepeats(suppressRepeats);

        SorterSubtype subtype = nodeFirst ? sorterType.nodeFirst : sorterType.nodeLast;

        List<TopologyElement> expected = suppressRepeats ? getExpectedFilteredResult(subtype) : getExpectedResult(subtype);

        try {
            if (typed)
                actual = topologySort.setNavigator(TopologyElement::getNeighbors).sort(getRoot());
            else
                actual = topologySort.sort(getRoot(), TopologyElement::getNeighbors);

            assertEquals(expected, actual);

        } catch (RecursionException ex) {
            if (expected != null)
                fail("unexpected recursion exception");
        }
    }

    protected abstract void fail(String unexpected_recursion_exception);

    void apply(TopologySort<?> topologySort, List<TopologyElement> expected) {

        List<TopologyElement> actual = null;

        try {
            actual = topologySort.sort(getRoot(), TopologyElement::getNeighbors);

            assertNotNull(actual);

        } catch (RecursionException ex) {
            if (expected != null)
                fail("unexpected recursion exception");
        }

        if (actual != null)
            assertEquals(expected, actual);

        TopologySort<TopologyElement> typedTest = topologySort.setNavigator(TopologyElement::getNeighbors);

        actual = null;

        try {
            actual = typedTest.sort(getRoot());

            assertNotNull(actual);

        } catch (RecursionException ex) {
            if (expected != null)
                fail("unexpected recursion exception");
        }

        if (actual != null)
            assertEquals(expected, actual);
    }

    public void apply(TopologySort<?> topologySort, SorterType sorterType) {

        apply(topologySort, sorterType, true, true, true);
        apply(topologySort, sorterType, false, true, true);
        apply(topologySort, sorterType, true, false, true);
        apply(topologySort, sorterType, false, false, true);
        apply(topologySort, sorterType, true, true, false);
        apply(topologySort, sorterType, false, true, false);
        apply(topologySort, sorterType, true, false, false);
        apply(topologySort, sorterType, false, false, false);
    }
}
