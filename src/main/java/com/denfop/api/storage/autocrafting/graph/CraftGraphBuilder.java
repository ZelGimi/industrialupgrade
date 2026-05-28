package com.denfop.api.storage.autocrafting.graph;

import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.utils.ModUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CraftGraphBuilder {
    private final Map<String, List<PatternStack>> patternCacheItem = new HashMap<>();
    private final Map<String, List<PatternStack>> patternCacheFluid = new HashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    public static String getNodeTitle(SameStack stack) {
        if (stack == null) return "Empty";
        if (stack.isItem()) {
            return com.denfop.utils.ModUtils.cleanComponentString(stack.getStack().getHoverName().getString());
        }
        if (stack.isFluid()) {
            return com.denfop.utils.ModUtils.cleanComponentString(stack.getFluidStack().getDisplayName().getString());
        }
        return "Unknown";
    }

    public static ItemStack getNodeIcon(SameStack stack) {
        if (stack != null && stack.isItem()) {
            return stack.getStack().copy();
        }
        return ItemStack.EMPTY;
    }

    public CraftGraph buildGraph(List<PatternStack> patterns, SameStack target) {
        buildPatternCache(patterns);
        CraftGraph graph = new CraftGraph();
        Map<String, Integer> createdNodes = new HashMap<>();
        Deque<String> recursionPath = new ArrayDeque<>();
        int rootId = buildNodeRecursive(graph, target.copy(), target.getAmount(), 0, createdNodes, recursionPath, true);
        graph.setRootNodeId(rootId);
        return graph;
    }

    private int buildNodeRecursive(CraftGraph graph, SameStack target, int requiredAmount, int depth, Map<String, Integer> createdNodes, Deque<String> recursionPath, boolean root) {
        String logicalKey = makeLogicalKey(target, depth, root);
        Integer existing = createdNodes.get(logicalKey);
        if (existing != null) {
            CraftNode node = graph.getNode(existing);
            if (node != null) {
                node.setRequiredAmount(node.getRequiredAmount() + requiredAmount);
            }
            return existing;
        }
        String stackKey = makeStackKey(target);
        if (recursionPath.contains(stackKey)) {
            CraftNode cycleNode = new CraftNode(nextId.getAndIncrement(), target.copy(), CraftNodeType.CYCLE);
            cycleNode.setRequiredAmount(requiredAmount);
            cycleNode.setDepth(depth);
            graph.addNode(cycleNode);
            createdNodes.put(logicalKey, cycleNode.getId());
            return cycleNode.getId();
        }
        Tuple<PatternStack, SameStack> match = findPatternFor(target);
        CraftNodeType type;
        if (root) {
            type = CraftNodeType.TARGET;
        } else if (match == null) {
            type = CraftNodeType.RESOURCE;
        } else {
            type = CraftNodeType.CRAFTABLE;
        }
        CraftNode node = new CraftNode(nextId.getAndIncrement(), target.copy(), type);
        node.setDepth(depth);
        node.setRequiredAmount(requiredAmount);
        if (match != null) {
            node.setPattern(match.getA());
            node.setMatchedOutput(match.getB());
            int outputPerCraft = Math.max(1, match.getB().getAmount());
            int crafts = (int) Math.ceil(requiredAmount / (double) outputPerCraft);
            node.setCraftAmount(crafts * outputPerCraft);
            graph.addNode(node);
            createdNodes.put(logicalKey, node.getId());
            recursionPath.push(stackKey);
            for (SameStack input : match.getA().inputs()) {
                SameStack childTarget = input.copy();
                int childRequired = input.getAmount() * crafts;
                int childId = buildNodeRecursive(graph, childTarget, childRequired, depth + 1, createdNodes, recursionPath, false);
                graph.addEdge(new CraftEdge(node.getId(), childId));
            }
            recursionPath.pop();
        } else {
            graph.addNode(node);
            createdNodes.put(logicalKey, node.getId());
        }
        return node.getId();
    }

    private String makeLogicalKey(SameStack stack, int depth, boolean root) {
        return (root ? "root|" : "node|") + depth + "|" + makeStackKey(stack);
    }

    private String makeStackKey(SameStack stack) {
        if (stack == null) return "null";
        return stack.getKey() + "|" + stack.getTag();
    }

    private void buildPatternCache(List<PatternStack> patterns) {
        patternCacheItem.clear();
        patternCacheFluid.clear();
        for (PatternStack ps : patterns) {
            for (SameStack s : ps.output()) {
                if (s.isItem()) {
                    String key = BuiltInRegistries.ITEM.getKey(s.getStack().getItem()).toString();
                    patternCacheItem.computeIfAbsent(key, k -> new ArrayList<>()).add(ps);
                } else if (s.isFluid()) {
                    String key = BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).toString();
                    patternCacheFluid.computeIfAbsent(key, k -> new ArrayList<>()).add(ps);
                }
            }
        }
    }

    private Tuple<PatternStack, SameStack> findPatternFor(SameStack stack) {
        if (stack.isItem()) {
            List<PatternStack> list = patternCacheItem.get(BuiltInRegistries.ITEM.getKey(stack.getStack().getItem()).toString());
            if (list == null) return null;
            for (PatternStack ps : list) {
                for (SameStack out : ps.output()) {
                    if (out.isItem() && out.getStack().is(stack.getStack().getItem()) && ModUtils.compareNbt(out.getStack().getComponents(), stack.getStack().getComponents(), true)) {
                        return new Tuple<>(ps, out);
                    }
                }
            }
        }
        if (stack.isFluid()) {
            List<PatternStack> list = patternCacheFluid.get(BuiltInRegistries.FLUID.getKey(stack.getFluidStack().getFluid()).toString());
            if (list == null) return null;
            for (PatternStack ps : list) {
                for (SameStack out : ps.output()) {
                    if (out.isFluid() && FluidStack.isSameFluidSameComponents(out.getFluidStack(), stack.getFluidStack())) {
                        return new Tuple<>(ps, out);
                    }
                }
            }
        }
        return null;
    }
}