package com.stclair.corlib.topology;

import com.stclair.corlib.collection.EmptyDeque;

import java.util.Deque;
import java.util.LinkedList;

import static com.stclair.corlib.validation.Validation.neverNull;

public class NodeContext<X> {

    public static final NodeContext NullContext = new NodeContext();

    public X node;

    public Navigator<X> navigator;

    public Deque<X> queue;

    private NodeContext() {
        node = null;
        queue = new EmptyDeque<>();
    }

    public NodeContext(X node, Navigator<X> navigator) {

        this.node = neverNull(node, "node");
        this.navigator = neverNull(navigator, "navigator");
    }

    public Deque<X> getQueue() {

        if (queue == null)
            queue = new LinkedList<>(navigator.neighbors(node));

        return queue;
    }

    public X nextMember() {

        return queue.getFirst();
    }

    public X removeFirst() {

        return queue.removeFirst();
    }

    public boolean isEmpty() {

        return queue.isEmpty();
    }

    public static <X> NodeContext<X> nullContext() {
        return (NodeContext<X>) NullContext;
    }
}
