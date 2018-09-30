package com.stclair.corlib.topology;

import java.util.Collections;
import java.util.LinkedList;

import static com.stclair.corlib.validation.Validation.requireInstanceOf;

public class BreadthFirstTopologyStrategy<X> implements TopologyStrategy<X> {

    @Override
    public TopologySorterContext<X> createContext(TopologySorter sorter, X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {

        BreadthFirstTopologySorterContext<X> context = new BreadthFirstTopologySorterContext<>(node, navigator, nodeFirst, suppressRepeats);

        sorter.registerFirstNode(context);

        context.getNeighbors().add(node);

        context.getNeighborsQueue().add(context.getNeighbors());
        context.getAncestorsQueue().add(new LinkedList<>());

        return context;
    }


    BreadthFirstTopologySorterContext<X> getContext(TopologySorterContext<X> context) {
        return requireInstanceOf(context, BreadthFirstTopologySorterContext.class, "context");
    }

    @Override
    public void refillQueue(TopologySorterContext<X> contxt) {

        BreadthFirstTopologySorterContext<X> context = getContext(contxt);

        while (context.getQueue().isEmpty() && ! context.getNeighborsQueue().isEmpty()) {
            context.setQueue(context.getNeighborsQueue().remove(0));
            context.setAncestors(context.getAncestorsQueue().remove(0));
        }
    }

    @Override
    public void processNode(TopologySorterContext<X> contxt) {

        BreadthFirstTopologySorterContext<X> context = getContext(contxt);

        context.setNode(context.getQueue().remove(0));

        if (context.isNodeFirst()) {
            context.getResult().add(context.getNode());
            context.getAncestors().addFirst(context.getNode());
        } else {
            context.getResult().addFirst(context.getNode());
            context.getAncestors().addFirst(context.getNode());
        }
    }

    @Override
    public void processNeighbors(TopologySorterContext<X> contxt) {

        BreadthFirstTopologySorterContext<X> context = getContext(contxt);

        if (! context.isNodeFirst()) {
            Collections.reverse(context.getNeighbors());

            context.getNeighborsQueue().add(0, context.getNeighbors());
            context.getAncestorsQueue().add(0, context.getAncestors());

        } else {
            context.getNeighborsQueue().add(context.getNeighbors());
            context.getAncestorsQueue().add(context.getAncestors());
        }

        context.getAncestors().remove(0);
    }

    @Override
    public boolean done(TopologySorterContext<X> contxt) {

        BreadthFirstTopologySorterContext<X> context = getContext(contxt);

        return context.getNeighborsQueue().isEmpty() && context.getQueue().isEmpty();
    }
}
