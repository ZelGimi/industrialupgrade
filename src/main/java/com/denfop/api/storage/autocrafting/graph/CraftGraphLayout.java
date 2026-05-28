package com.denfop.api.storage.autocrafting.graph;

import java.util.*;

public class CraftGraphLayout {
    public static final float NODE_WIDTH = 18.0f;
    public static final float NODE_HEIGHT = 18.0f;
    public static final float H_SPACING = 10.0f;
    public static final float V_SPACING = 20.0f;

    public void layout(CraftGraph graph) {
        if (graph == null || !graph.isLayoutDirty()) {
            return;
        }
        Map<Integer, List<CraftNode>> layers = new TreeMap<>();
        for (CraftNode node : graph.getNodes()) {
            layers.computeIfAbsent(node.getDepth(), d -> new ArrayList<>()).add(node);
        }
        for (Map.Entry<Integer, List<CraftNode>> entry : layers.entrySet()) {
            int depth = entry.getKey();
            List<CraftNode> layer = entry.getValue();
            layer.sort(Comparator.comparingInt(CraftNode::getId));
            float totalWidth = layer.size() * NODE_WIDTH + Math.max(0, layer.size() - 1) * H_SPACING;
            float startX = -totalWidth / 2.0f + NODE_WIDTH / 2.0f;
            float y = depth * V_SPACING;
            for (int i = 0; i < layer.size(); i++) {
                CraftNode node = layer.get(i);
                node.setX(startX + i * (NODE_WIDTH + H_SPACING));
                node.setY(y);
            }
        }
        relaxToParents(graph, layers, 2);
        graph.setLayoutDirty(false);
    }

    private void relaxToParents(CraftGraph graph, Map<Integer, List<CraftNode>> layers, int passes) {
        for (int pass = 0; pass < passes; pass++) {
            for (Map.Entry<Integer, List<CraftNode>> entry : layers.entrySet()) {
                List<CraftNode> layer = entry.getValue();
                for (CraftNode node : layer) {
                    if (node.getParents().isEmpty()) {
                        continue;
                    }
                    float avgParentX = 0.0f;
                    int count = 0;
                    for (Integer parentId : node.getParents()) {
                        CraftNode parent = graph.getNode(parentId);
                        if (parent != null) {
                            avgParentX += parent.getX();
                            count++;
                        }
                    }
                    if (count > 0) {
                        avgParentX /= count;
                        node.setX((node.getX() * 0.65f) + (avgParentX * 0.35f));
                    }
                }
                enforceSpacing(layer);
            }
        }
    }

    private void enforceSpacing(List<CraftNode> layer) {
        layer.sort(Comparator.comparingDouble(CraftNode::getX));
        float minDist = NODE_WIDTH + H_SPACING;
        for (int i = 1; i < layer.size(); i++) {
            CraftNode prev = layer.get(i - 1);
            CraftNode curr = layer.get(i);
            float wanted = prev.getX() + minDist;
            if (curr.getX() < wanted) {
                curr.setX(wanted);
            }
        }
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        for (CraftNode node : layer) {
            min = Math.min(min, node.getX());
            max = Math.max(max, node.getX());
        }
        float center = (min + max) * 0.5f;
        for (CraftNode node : layer) {
            node.setX(node.getX() - center);
        }
    }
}