package com.stclair.corlib.topology.testcase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TopologyElement {

    String name;

    List<TopologyElement> neighbors = new LinkedList<>();

    TopologyElement(String name) {
        this.name = name;
    }

    public List<TopologyElement> getNeighbors() {

        return neighbors;
    }

    public void setNeighbors(TopologyElement... neighbors) {

        this.neighbors = Arrays.asList(neighbors);
    }

    @Override
    public String toString() {
        return name;
    }
}

