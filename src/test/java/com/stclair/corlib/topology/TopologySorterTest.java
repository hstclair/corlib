package com.stclair.corlib.topology;

import com.stclair.corlib.topology.testcase.*;
import org.junit.Test;

public class TopologySorterTest {

    ParentChildTestCase parentChildTestCase = new ParentChildTestCase();

    ParentTwoChildTestCase parentTwoChildTestCase = new ParentTwoChildTestCase();

    ParentChildGrandchildTestCase parentChildGrandchildTestCase = new ParentChildGrandchildTestCase();

    ParentChildGrandchildChildTestCase parentChildGrandchildChildTestCase = new ParentChildGrandchildChildTestCase();

    SingleElementRecursionTestCase singleElementRecursionTestCase = new SingleElementRecursionTestCase();

    RepeatingNonrecursiveLeafTestCase repeatingNonrecursiveLeafTestCase = new RepeatingNonrecursiveLeafTestCase();

    RepeatingNonrecursiveBranchTestCase repeatingNonrecursiveBranchTestCase = new RepeatingNonrecursiveBranchTestCase();

    TopologySort getBreadthFirstInstance() {
        return new TopologySorter().breadthFirst();
    }

    SorterType getBreadthFirstSorterType() {
        return SorterType.BreadthFirst;
    }

    @Test
    public void parentChildBreadthFirstTest() {

        parentChildTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void parentTwoChildBreadthFirstTest() {

        parentTwoChildTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void parentChildGrandchildBreadthFirstTest() {

        parentChildGrandchildTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void parentChildGrandchildChildBreadthFirstTestCase() {

        parentChildGrandchildChildTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void singleElementRecursionBreadthFirstTestCase() {

        singleElementRecursionTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void repeatingNonrecursiveLeafBreadthFirstTestCase() {

        repeatingNonrecursiveLeafTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }

    @Test
    public void repeatingNonrecursiveBranchBreadthFirstTestCase() {

        repeatingNonrecursiveBranchTestCase.apply(getBreadthFirstInstance(), getBreadthFirstSorterType());
    }


    TopologySort getDepthFirstInstance() {
        return new TopologySorter().depthFirst();
    }

    SorterType getDepthFirstSorterType() {
        return SorterType.DepthFirst;
    }

    @Test
    public void parentChildDepthFirstTest() {

        parentChildTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void parentTwoChildDepthFirstTest() {

        parentTwoChildTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void parentChildGrandchildDepthFirstTest() {

        parentChildGrandchildTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void parentChildGrandchildChildDepthFirstTestCase() {

        parentChildGrandchildChildTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void singleElementRecursionDepthFirstTestCase() {

        singleElementRecursionTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void repeatingNonrecursiveLeafDepthFirstTestCase() {

        repeatingNonrecursiveLeafTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

    @Test
    public void repeatingNonrecursiveBranchDepthFirstTestCase() {

        repeatingNonrecursiveBranchTestCase.apply(getDepthFirstInstance(), getDepthFirstSorterType());
    }

}
