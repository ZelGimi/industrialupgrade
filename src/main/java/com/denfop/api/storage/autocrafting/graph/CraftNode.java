package com.denfop.api.storage.autocrafting.graph;

import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftNode {

    private final int id;
    private final SameStack stack;
    private final CraftNodeType type;
    private final List<Integer> children = new ArrayList<>();
    private final List<Integer> parents = new ArrayList<>();
    private PatternStack pattern;
    private SameStack matchedOutput;
    private int depth;
    private float x;
    private float y;
    private int requiredAmount;
    private int availableAmount;
    private int craftAmount;
    private int craftTime;
    private boolean expanded = true;

    public CraftNode(int id, SameStack stack, CraftNodeType type) {
        this.id = id;
        this.stack = stack;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public SameStack getStack() {
        return stack;
    }

    public CraftNodeType getType() {
        return type;
    }

    public PatternStack getPattern() {
        return pattern;
    }

    public void setPattern(PatternStack pattern) {
        this.pattern = pattern;
    }

    public SameStack getMatchedOutput() {
        return matchedOutput;
    }

    public void setMatchedOutput(SameStack matchedOutput) {
        this.matchedOutput = matchedOutput;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }

    public int getCraftAmount() {
        return craftAmount;
    }

    public void setCraftAmount(int craftAmount) {
        this.craftAmount = craftAmount;
    }

    public int getCraftTime() {
        return craftTime;
    }

    public void setCraftTime(int craftTime) {
        this.craftTime = craftTime;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<Integer> getChildren() {
        return children;
    }

    public List<Integer> getParents() {
        return parents;
    }

    public void addChild(int childId) {
        if (!children.contains(childId)) {
            children.add(childId);
        }
    }

    public void addParent(int parentId) {
        if (!parents.contains(parentId)) {
            parents.add(parentId);
        }
    }

    public boolean isItem() {
        return stack != null && stack.isItem();
    }

    public ItemStack getDisplayItemStack() {
        if (stack == null || !stack.isItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = stack.getStack().copy();
        copy.setCount(Math.max(1, stack.getAmount()));
        return copy;
    }

    public String getCacheKey() {
        return stack == null ? "empty" : stack.getKey() + "|" + stack.getTag();
    }
}