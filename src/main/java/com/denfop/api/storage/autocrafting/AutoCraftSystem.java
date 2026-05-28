package com.denfop.api.storage.autocrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.*;

public class AutoCraftSystem {
    private final AutoCraftOutput autoCraftOutput;
    private final Map<String, Map<SameStack, Integer>> reservedItemSlots = new HashMap<>();
    private final Map<String, Map<SameStack, Integer>> reservedFluidSlots = new HashMap<>();
    List<AutoCraftStack> autoStacks = new LinkedList<>();
    boolean isEnd = false;
    int count;
    private int index = -1;

    public AutoCraftSystem(AutoCraftOutput autoCraftOutput, List<AutoCraftStack> autoStacks) {
        this.autoCraftOutput = autoCraftOutput;
        this.count = autoCraftOutput.count();
        this.autoStacks = autoStacks;

    }

    private static CompoundTag writeReservedMap(Map<String, Map<SameStack, Integer>> src, HolderLookup.Provider access) {
        CompoundTag out = new CompoundTag();
        if (src == null || src.isEmpty()) return out;

        for (var cellEntry : src.entrySet()) {
            String cellKey = cellEntry.getKey();
            Map<SameStack, Integer> inner = cellEntry.getValue();
            if (inner == null || inner.isEmpty()) continue;

            ListTag list = new ListTag();
            for (var e : inner.entrySet()) {
                SameStack sameStack = e.getKey();
                int amount = e.getValue() == null ? 0 : e.getValue();
                if (sameStack == null || amount <= 0) continue;

                CompoundTag el = new CompoundTag();
                el.put("SameStack", sameStack.writeToNBT(access));
                el.putInt("Amount", amount);
                list.add(el);
            }

            CompoundTag cellTag = new CompoundTag();
            cellTag.put("List", list);
            out.put(cellKey, cellTag);
        }

        return out;
    }

    public static AutoCraftSystem readFromTag(CompoundTag tag, HolderLookup.Provider access) {
        AutoCraftOutput output = AutoCraftOutput.readFromNBT(tag.getCompound("AutoCraftOutput"), access);
        int count = tag.getInt("Count");
        int index = tag.getInt("Index");
        AutoCraftSystem system = new AutoCraftSystem(output, new LinkedList<>());
        system.setIndex(index);
        system.count = count;
        system.isEnd = tag.getBoolean("IsEnd");

        ListTag listTag = tag.getList("PatternStacks", 10);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag stackTag = listTag.getCompound(i);
            system.autoStacks.add(AutoCraftStack.readFromNBT(stackTag, access));
        }
        if (tag.contains("ReservedItemSlots", 10)) {
            readReservedMap(tag.getCompound("ReservedItemSlots"), system.reservedItemSlots, access);
        }


        if (tag.contains("ReservedFluidSlots", 10)) {
            readReservedMap(tag.getCompound("ReservedFluidSlots"), system.reservedFluidSlots, access);
        }
        return system;
    }

    private static void readReservedMap(CompoundTag in, Map<String, Map<SameStack, Integer>> dst, HolderLookup.Provider access) {

        for (String cellKey : in.getAllKeys()) {
            CompoundTag cellTag = in.getCompound(cellKey);
            ListTag list = cellTag.getList("List", 10);

            Map<SameStack, Integer> inner = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                CompoundTag el = list.getCompound(i);

                SameStack sameStack = SameStack.readFromNBT(el.getCompound("SameStack"), access);
                int amount = el.getInt("Amount");

                if (amount > 0) {
                    inner.merge(sameStack, amount, Integer::sum);
                }
            }

            if (!inner.isEmpty()) {
                dst.put(cellKey, inner);
            }
        }
    }

    public Map<String, Map<SameStack, Integer>> getReservedItemSlots() {
        return reservedItemSlots;
    }

    public Map<String, Map<SameStack, Integer>> getReservedFluidSlots() {
        return reservedFluidSlots;
    }

    public void addReserve(SameStack slot, int amount) {
        if (amount <= 0) return;
        String cellKey;
        Map<String, Map<SameStack, Integer>> target;
        if (slot.isFluid()) {
            cellKey = BuiltInRegistries.FLUID.getKey(slot.getFluidStack().getFluid()).toString();
            target = getReservedFluidSlots();
        } else {
            cellKey = BuiltInRegistries.ITEM.getKey(slot.getStack().getItem()).toString();
            target = getReservedItemSlots();
        }
        target.computeIfAbsent(cellKey, k -> new HashMap<>())
                .merge(slot, amount, Integer::sum);
    }

    public void addReserve(Map<String, Map<SameStack, Integer>> target, String cellKey, SameStack slot, int amount) {
        if (amount <= 0) return;
        target.computeIfAbsent(cellKey, k -> new HashMap<>())
                .merge(slot, amount, Integer::sum);
    }

    public void removeReserve(SameStack slot,
                              int amount) {

        if (amount <= 0) return;
        String cellKey;
        Map<String, Map<SameStack, Integer>> target;
        if (slot.isFluid()) {
            cellKey = BuiltInRegistries.FLUID.getKey(slot.getFluidStack().getFluid()).toString();
            target = getReservedFluidSlots();
        } else {
            cellKey = BuiltInRegistries.ITEM.getKey(slot.getStack().getItem()).toString();
            target = getReservedItemSlots();
        }
        Map<SameStack, Integer> inner = target.get(cellKey);
        if (inner == null) return;

        Integer current = inner.get(slot);
        if (current == null) return;

        int newValue = current - amount;

        if (newValue > 0) {
            inner.put(slot, newValue);
        } else {
            inner.remove(slot);
        }

        if (inner.isEmpty()) {
            target.remove(cellKey);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoCraftSystem that = (AutoCraftSystem) o;
        return index == that.index && isEnd == that.isEnd && count == that.count && Objects.equals(autoCraftOutput, that.autoCraftOutput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autoCraftOutput, index, isEnd, count);
    }

    public CompoundTag writeToTag(HolderLookup.Provider access) {
        CompoundTag tag = new CompoundTag();


        tag.put("AutoCraftOutput", autoCraftOutput.writeToNBT(access));


        ListTag listTag = new ListTag();
        for (AutoCraftStack stack : autoStacks) {
            listTag.add(stack.writeToNBT(access));
        }
        tag.put("PatternStacks", listTag);


        tag.putBoolean("IsEnd", isEnd);
        tag.putInt("Count", count);
        tag.putInt("Index", index);
        tag.put("ReservedItemSlots", writeReservedMap(this.reservedItemSlots, access));


        tag.put("ReservedFluidSlots", writeReservedMap(this.reservedFluidSlots, access));


        return tag;

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public AutoCraftOutput getAutoCraftOutput() {
        return autoCraftOutput;
    }

    public List<AutoCraftStack> getAutoStacks() {
        return autoStacks;
    }


    public void removeCountAutoCraftStack(int amount) {
        this.count -= Math.min(amount, count);
        this.isEnd = this.count == 0;
    }


}