package com.stclair.corlib.topology;

import java.util.LinkedList;

public class TopologySorterContext<X> {

    X node;

    boolean nodeFirst;

    boolean suppressRepeats;

    Navigator<X> navigator;

    LinkedList<X> neighbors = new LinkedList<>();

    LinkedList<X> queue = new LinkedList<>();

    LinkedList<X> observed = new LinkedList<>();

    LinkedList<X> ancestors = new LinkedList<>();

    LinkedList<X> result = new LinkedList<>();

    public TopologySorterContext(X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {

        this.node = node;
        this.navigator = navigator;
        this.nodeFirst = nodeFirst;
        this.suppressRepeats = suppressRepeats;
    }

    public X getNode() {
        return node;
    }

    public void setNode(X node) {
        this.node = node;
    }

    public boolean isNodeFirst() {
        return this.nodeFirst;
    }

    public boolean suppressRepeats() {
        return this.suppressRepeats;
    }

    public Navigator<X> getNavigator() {
        return this.navigator;
    }

    public LinkedList<X> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(LinkedList<X> neighbors) {
        this.neighbors = neighbors;
    }

    public LinkedList<X> getQueue() {
        return this.queue;
    }

    public void setQueue(LinkedList<X> queue) {
        this.queue = queue;
    }

    public LinkedList<X> getObserved() {
        return this.observed;
    }

    public LinkedList<X> getAncestors() {
        return this.ancestors;
    }

    public void setAncestors(LinkedList<X> ancestors) {
        this.ancestors = ancestors;
    }

    public LinkedList<X> getResult() {
        return result;
    }
}
