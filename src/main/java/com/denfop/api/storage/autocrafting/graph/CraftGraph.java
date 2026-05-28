package com.denfop.api.storage.autocrafting.graph;

import java.util.*;

public class CraftGraph {
    private final Map<Integer, CraftNode> nodes = new LinkedHashMap<>();
    private final List<CraftEdge> edges = new ArrayList<>();
    private int rootNodeId = -1;
    private boolean layoutDirty = true;

    public void addNode(CraftNode node) {
        nodes.put(node.getId(), node);
        layoutDirty = true;
    }

    public void addEdge(CraftEdge edge) {
        edges.add(edge);
        CraftNode from = nodes.get(edge.getFromNodeId());
        CraftNode to = nodes.get(edge.getToNodeId());
        if (from != null && to != null) {
            from.addChild(to.getId());
            to.addParent(from.getId());
        }
        layoutDirty = true;
    }

    public CraftNode getNode(int id) {
        return nodes.get(id);
    }

    public Collection<CraftNode> getNodes() {
        return nodes.values();
    }

    public List<CraftEdge> getEdges() {
        return edges;
    }

    public int size() {
        return nodes.size();
    }

    public int getRootNodeId() {
        return rootNodeId;
    }

    public void setRootNodeId(int rootNodeId) {
        this.rootNodeId = rootNodeId;
    }

    public boolean isLayoutDirty() {
        return layoutDirty;
    }

    public void setLayoutDirty(boolean layoutDirty) {
        this.layoutDirty = layoutDirty;
    }

    public Set<Integer> collectDescendantsInclusive(int startId) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> deque = new ArrayDeque<>();
        deque.add(startId);
        while (!deque.isEmpty()) {
            int id = deque.poll();
            if (!visited.add(id)) {
                continue;
            }
            CraftNode node = getNode(id);
            if (node != null) {
                deque.addAll(node.getChildren());
            }
        }
        return visited;
    }

    public Set<Integer> collectAncestorsInclusive(int startId) {
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> deque = new ArrayDeque<>();
        deque.add(startId);
        while (!deque.isEmpty()) {
            int id = deque.poll();
            if (!visited.add(id)) {
                continue;
            }
            CraftNode node = getNode(id);
            if (node != null) {
                deque.addAll(node.getParents());
            }
        }
        return visited;
    }
}