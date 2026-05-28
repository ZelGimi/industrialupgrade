package com.denfop.api.storage;

import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class StorageStack {
    private final CompoundTag tag;
    private int count;
    private int slot;

    public StorageStack(CompoundTag tag, int count, int slot) {
        this.tag = tag;
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
        if (!(o.getClass() == this.getClass() || o instanceof CompoundTag))
            return false;
        if (o instanceof StorageStack) {
            StorageStack that = (StorageStack) o;
            return ModUtils.compareNbt(tag, that.tag, true);
        }
        CompoundTag that = (CompoundTag) o;
        return ModUtils.compareNbt(tag, that, true);
    }

    public CompoundTag getTag() {
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
