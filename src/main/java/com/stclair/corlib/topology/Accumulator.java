package com.stclair.corlib.topology;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class Accumulator<X> {

    /** the Navigator instance that will provide the list of a graph node's neighbors */
    Navigator<X> navigator;

    /** true if the sort result is to be provided in "node-first" order */
    boolean nodeFirst;

    /** true if only the first observation of a given node is to appear in the result set */
    boolean suppressRepeats;

    /** an object providing the current ancestor node and the list of decendants remaining to process */
    NodeContext<X> context;

    /** the stack containing each of the parent contexts awaiting processing */
    LinkedList<NodeContext<X>> stack = new LinkedList<>();

    /** the sorted list of graph nodes under construction */
    LinkedList<X> result = new LinkedList<>();

    /** the set of all previously-observed nodes */
    LinkedHashSet<X> observed = new LinkedHashSet<>();

    /** the list of all ancestors of the current Context */
    LinkedList<X> ancestors = new LinkedList<>();


    /**
     * constructor
     * @param root the root node of the graph to be sorted
     * @param navigator the Navigator instance that will provide the list of a graph node's neighbors
     * @param nodeFirst true if the sort result is to be provided in "node-first" order
     *                  (i.e. a parent node precedes its descendants)
     * @param suppressRepeats true if only the first observation of a given node is to appear in the result set
     */
    public Accumulator(X root, Navigator<X> navigator, boolean nodeFirst, boolean suppressRepeats) {

        this.navigator = navigator;
        this.nodeFirst = nodeFirst;
        this.suppressRepeats = suppressRepeats;

        context = new NodeContext<>(root, this::filterValidateAndNavigate);
    }

    /**
     * push a context onto the stack
     * @param context the context to be pushed
     */
    public void pushContext(NodeContext<X> context) {

        stack.addLast(this.context);

        this.context = context;
    }

    /**
     * prepare the list of neighbors to be used for a new Context
     * @return the filtered and validated list of neighbor nodes
     * @throws RecursionException if the list of neighbors includes an ancestor node
     */
    public LinkedList<X> filterValidateAndNavigate(X node) {

        LinkedList<X> neighborList = new LinkedList<>(navigator.neighbors(node));

        if (suppressRepeats)
            neighborList = filterRepeats(neighborList);

        if (neighborList.contains(node))
            throw new RecursionException("Node identifies itself as neighbor", node, node);

        if (!Collections.disjoint(ancestors, neighborList))
            reportRecursion(neighborList, ancestors);

        return neighborList;
    }

    public boolean hasNeighbors() {

        return ! context.isEmpty();
    }

    public boolean moveToFirstNeighbor() {

        if (context.isEmpty())
            return false;

        NodeContext<X> newContext = new NodeContext<>(context.removeFirst(), this::filterValidateAndNavigate);

        pushContext(newContext);

        return true;
    }

    public boolean moveToSibling() {
        if (stack.isEmpty())
            return false;

        if (stack.peek().isEmpty())
            return false;

        context = new NodeContext<>(stack.element().removeFirst(), this::filterValidateAndNavigate);

        return true;
    }

//    public boolean restoreContext() {
//
//    }


    // depth first,
    // if node has neighbors
    //   take first neighbor as new node
    //   push node and remaining neighbors
    //     (add node to output if node first)
    //   set node to new node
    // else
    //   restore node and remaining neighbors from stack
    //     (add node to output if node last)
    //
    // repeat

    // breadth first.
    // if node has no neighbors
    //   if node last, add node to output
    //   restore node and remaining neighbors from stack
    // else
    //   if node first, add node to output
    //   take first neighbor as new node
    // repeat



    public boolean advance() {

        if (context.isEmpty())
            return false;

        context.removeFirst();

        return ! context.isEmpty();
    }

    public void push() {
        // push active context
    }

//    public boolean pop() {
//        // restore parent context
//        // return true if successful
//    }
//
//    public boolean isEmptyNew() {
//        // return true if both stack and queue are empty
//    }

    /**
     * process a node, creating a new Context if needed
     * @param node the node to be processed
     * @return the new Context
     * @throws RecursionException if the node's neighbors include the node or one of its ancestors
     */
//    public NodeContext<X> processNode(X node, NodeContext<X> currentContext) {
//
//        LinkedList<X> neighbors = new LinkedList<>(navigator.neighbors(node));
//
//        neighbors = filterAndValidateNeighbors(node, neighbors);
//
//        if (neighbors.size() == 0) {
//
//            result.addLast(node);
//
//            return currentContext;
//        }
//
//        if (nodeFirst)
//            result.addLast(node);
//
//        ancestors.addLast(node);
//
//        pushContext(currentContext);
//
//        return new NodeContext<>(node, neighbors);
//    }

    /**
     * identify the first recursive node and report it by throwing a RecursionException
     * @param neighbors the list of the node's neighbors
     * @param ancestors a list containing the node and each of its ancestors
     */
    public void reportRecursion(LinkedList<X> neighbors, LinkedList<X> ancestors) {

        LinkedList<X> recursion = new LinkedList<>(neighbors);

        recursion.retainAll(ancestors);

        throw new RecursionException("Recursive element", ancestors.get(0), recursion.get(0));
    }

    /**
     * restore a previous context from the stack
     * @return the restored context
     */
    public NodeContext<X> restoreContextFromStack() {

        if (! nodeFirst)
            result.addLast(context.node);

        if (stack.isEmpty())
            return NodeContext.NullContext;

        ancestors.removeLast();

        return stack.removeLast();
    }

    /**
     * remove any previously-observed nodes from the working list of candidates
     * @param candidates the list of newly-observed candidates
     * @return the list of newly-observed candidates with any repeat observations removed
     */
    public LinkedList<X> filterRepeats(LinkedList<X> candidates) {

        observed.addAll(candidates);

        candidates.removeAll(observed);

        return candidates;
    }

    /**
     * test whether this accumulator has no additional elements to process
     * @return true if there are no additional elements to process
     */
    public boolean isEmpty() {
        return stack.isEmpty() && context.isEmpty();
    }

}
