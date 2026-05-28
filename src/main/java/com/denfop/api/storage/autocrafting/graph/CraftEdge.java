package com.denfop.api.storage.autocrafting.graph;


public class CraftEdge {

    private final int fromNodeId;
    private final int toNodeId;

    public CraftEdge(int fromNodeId, int toNodeId) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
    }

    public int getFromNodeId() {
        return fromNodeId;
    }

    public int getToNodeId() {
        return toNodeId;
    }
}
