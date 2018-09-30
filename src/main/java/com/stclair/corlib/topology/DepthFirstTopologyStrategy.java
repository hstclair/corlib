package com.stclair.corlib.topology;

import java.util.LinkedList;

import static com.stclair.corlib.validation.Validation.requireInstanceOf;

public class DepthFirstTopologyStrategy<X> implements TopologyStrategy<X> {

    @Override
    public DepthFirstTopologySorterContext<X> createContext(TopologySorter sorter, X node, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {
        DepthFirstTopologySorterContext<X> context = new DepthFirstTopologySorterContext<>(node, navigator, nodeFirst, suppressRepeats);

        sorter.registerFirstNode(context);

        context.getQueue().add(node);

        context.getStack().push(context.getQueue());

        context.setQueue(new LinkedList<>(context.getNeighbors()));

        return context;
    }

    public DepthFirstTopologySorterContext<X> getContext(TopologySorterContext<X> context) {
        return requireInstanceOf(context, DepthFirstTopologySorterContext.class, "context");
    }

    @Override
    public void refillQueue(TopologySorterContext<X> contxt) {

        DepthFirstTopologySorterContext<X> context = getContext(contxt);

        if (! context.getQueue().isEmpty())
            return;

        context.setQueue(new LinkedList<>());

        while (context.getQueue().isEmpty() && ! done(context)) {

            context.setQueue(context.getStack().pop());

            if (context.getAncestors().isEmpty())
                continue;

            context.setNode(context.getAncestors().remove(0));

            if (! context.isNodeFirst())
                context.getResult().add(context.getNode());
        }
    }

    @Override
    public void processNode(TopologySorterContext<X> contxt) {

        DepthFirstTopologySorterContext<X> context = getContext(contxt);

        context.setNode(context.getQueue().remove(0));

        if (context.isNodeFirst())
            context.getResult().add(context.getNode());
    }

    @Override
    public void processNeighbors(TopologySorterContext<X> contxt) {

        DepthFirstTopologySorterContext<X> context = getContext(contxt);

        if (context.getNeighbors().size() != 0) {
            context.getAncestors().addFirst(context.getNode());

            context.getStack().push(context.getQueue());

            context.setQueue(new LinkedList<>(context.getNeighbors()));
        } else {
            if (! context.isNodeFirst())
                context.getResult().add(context.getNode());
        }
    }

    @Override
    public boolean done(TopologySorterContext<X> contxt) {

        DepthFirstTopologySorterContext<X> context = getContext(contxt);

        return context.getStack().isEmpty() && context.getQueue().isEmpty();
    }
}
