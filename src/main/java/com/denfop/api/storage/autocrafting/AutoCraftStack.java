package com.denfop.api.storage.autocrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tuple;

import java.util.Objects;

public class AutoCraftStack {
    private final Tuple<PatternStack, SameStack> patternStack;

    private int all;
    private int create = 0;

    public AutoCraftStack(Tuple<PatternStack, SameStack> patternStack, int amount) {
        this.patternStack = patternStack;

        this.all = patternStack.getB().getAmount() * amount;
    }

    public AutoCraftStack(Tuple<PatternStack, SameStack> patternStack, int all, int create) {
        this.patternStack = patternStack;
        this.all = all;
        this.create = create;
    }

    public static AutoCraftStack readFromNBT(CompoundTag nbt, HolderLookup.Provider access) {
        PatternStack patternStack = PatternStack.readFromNBT(nbt.getCompound("PatternStack"), access);
        SameStack sameStack = SameStack.readFromNBT(nbt.getCompound("SameStack"), access);
        int all = nbt.getInt("All");
        int create = nbt.getInt("Create");
        return new AutoCraftStack(new Tuple<>(patternStack, sameStack), all, create);
    }

    public void addAmountAll(int amount) {
        this.all += patternStack.getB().getAmount() * amount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoCraftStack that = (AutoCraftStack) o;
        return Objects.equals(patternStack.getA(), that.patternStack.getA()) && Objects.equals(patternStack.getB(), that.patternStack.getB());
    }

    public CompoundTag writeToNBT(HolderLookup.Provider access) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("PatternStack", patternStack.getA().writeToNBT(access));
        nbt.put("SameStack", patternStack.getB().writeToNBT(access));
        nbt.putInt("All", all);
        nbt.putInt("Create", create);
        return nbt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternStack);
    }


    public void removeAllAmount(int amount) {
        this.all -= amount;
        if (this.all < 0)
            this.all = 0;
    }

    public SameStack getSameStack() {
        return patternStack.getB();
    }

    public Tuple<PatternStack, SameStack> getAllPart() {
        return patternStack;
    }

    public PatternStack getPatternStack() {
        return patternStack.getA();
    }


    public int getAll() {
        return all;
    }


}
