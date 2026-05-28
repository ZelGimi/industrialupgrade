package com.denfop.api.storage;

import com.denfop.utils.ModUtils;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;

import java.util.Objects;

public class StorageStack {
    private final PatchedDataComponentMap tag;
    private int count;
    private int slot;

    public StorageStack(DataComponentMap tag, int count, int slot) {
        this.tag = (PatchedDataComponentMap) tag;
        this.count = count;
        this.slot = slot;
    }

    public StorageStack copy() {
        return new StorageStack(tag != null ? tag.copy() : null, count, slot);
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o.getClass() == this.getClass() || o instanceof PatchedDataComponentMap))
            return false;
        if (o instanceof StorageStack) {
            StorageStack that = (StorageStack) o;
            return ModUtils.compareNbt(tag, that.tag, true);
        }
        PatchedDataComponentMap that = (PatchedDataComponentMap) o;
        return ModUtils.compareNbt(tag, that, true);
    }

    public PatchedDataComponentMap getTag() {
        return tag;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void removeCount(int count) {
        this.count -= count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}
