package com.stclair.corlib.topology;

import com.stclair.corlib.topology.testcase.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DepthFirstTopologySortTest {

    ParentChildTestCase parentChildTestCase = new ParentChildTestCase();

    ParentTwoChildTestCase parentTwoChildTestCase = new ParentTwoChildTestCase();

    ParentChildGrandchildTestCase parentChildGrandchildTestCase = new ParentChildGrandchildTestCase();

    ParentChildGrandchildChildTestCase parentChildGrandchildChildTestCase = new ParentChildGrandchildChildTestCase();

    SingleElementRecursionTestCase singleElementRecursionTestCase = new SingleElementRecursionTestCase();

    RepeatingNonrecursiveLeafTestCase repeatingNonrecursiveLeafTestCase = new RepeatingNonrecursiveLeafTestCase();

    RepeatingNonrecursiveBranchTestCase repeatingNonrecursiveBranchTestCase = new RepeatingNonrecursiveBranchTestCase();

    TopologySort getInstance() {
        return new DepthFirstTopologySort();
    }

    SorterType getSorterType() {
        return SorterType.DepthFirst;
    }

    @Test
    public void parentChildTest() {

        parentChildTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void parentTwoChildTest() {

        parentTwoChildTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void parentChildGrandchildTest() {

        parentChildGrandchildTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void parentChildGrandchildChildTestCase() {

        parentChildGrandchildChildTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void singleElementRecursionTestCase() {

        singleElementRecursionTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void repeatingNonrecursiveLeafTestCase() {

        repeatingNonrecursiveLeafTestCase.apply(getInstance(), getSorterType());
    }

    @Test
    public void repeatingNonrecursiveBranchTestCase() {

        repeatingNonrecursiveBranchTestCase.apply(getInstance(), getSorterType());
    }
}
