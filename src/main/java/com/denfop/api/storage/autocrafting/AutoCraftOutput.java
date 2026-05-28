package com.denfop.api.storage.autocrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;

import java.util.Objects;

public class AutoCraftOutput {
    private final Tuple<PatternStack, SameStack> patternStack;
    int index;
    private int amount;
    private int all;
    private int create = 0;

    public AutoCraftOutput(Tuple<PatternStack, SameStack> patternStack, int amount) {
        this.patternStack = patternStack;
        this.amount = amount;
        this.all = patternStack.getB().getAmount() * amount;
    }

    public AutoCraftOutput(Tuple<PatternStack, SameStack> patternStack, int amount, int all, int create) {
        this.patternStack = patternStack;
        this.amount = amount;
        this.all = all;
        this.create = create;
    }

    public static AutoCraftOutput readFromNBT(CompoundTag nbt, HolderLookup.Provider access) {
        PatternStack patternStack = PatternStack.readFromNBT(nbt.getCompound("PatternStack"), access);
        SameStack sameStack = SameStack.readFromNBT(nbt.getCompound("SameStack"), access);
        int amount = nbt.getInt("Amount");
        int all = nbt.getInt("All");
        int create = nbt.getInt("Create");
        AutoCraftOutput autoCraftOutput = new AutoCraftOutput(new Tuple<>(patternStack, sameStack), amount, all, create);
        int index = nbt.getInt("Index");
        autoCraftOutput.setIndex(index);
        return autoCraftOutput;
    }

    public void addCreate(int create) {
        this.create += create;
    }

    public void removeCreate(int create) {
        this.create -= create;
    }

    public int getCreate() {
        return create;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoCraftOutput that = (AutoCraftOutput) o;
        return Objects.equals(patternStack, that.patternStack) && index == that.index;
    }

    public CompoundTag writeToNBT(HolderLookup.Provider access) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("PatternStack", patternStack.getA().writeToNBT(access));
        nbt.put("SameStack", patternStack.getB().writeToNBT(access));
        nbt.putInt("Amount", amount);
        nbt.putInt("All", all);
        nbt.putInt("Create", create);
        nbt.putInt("Index", index);
        return nbt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternStack, index);
    }

    public void removeAmount(int amount) {
        this.amount -= amount;
    }

    public void removeAllAmount(int amount) {
        this.all -= amount;
    }

    public PatternStack getPatternStack() {
        return patternStack.getA();
    }

    public PatternStack patternStack() {
        return patternStack.getA();
    }

    public SameStack getSameStack() {
        return patternStack.getB();
    }

    public int getAmount() {
        return amount;
    }

    public int getAll() {
        return all;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }


    public int count() {
        return amount;
    }
}
