package com.denfop.items.bags;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class BagsDescription {

    public static final Codec<BagsDescription> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.fieldOf("stack").forGetter(BagsDescription::getStack),
                    Codec.INT.fieldOf("count").forGetter(BagsDescription::getCount)
            ).apply(instance, BagsDescription::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, BagsDescription> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                ItemStack.STREAM_CODEC.encode(buf, value.getStack());
                buf.writeInt(value.getCount());
            },
            buf -> {
                ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
                int count = buf.readInt();
                return new BagsDescription(stack, count);
            }
    );

    private final ItemStack stack;
    private int count;

    public BagsDescription(ItemStack stack) {
        this(stack, stack.isEmpty() ? 0 : stack.getCount());
    }

    public BagsDescription(ItemStack stack, int count) {
        this.stack = normalizePreviewStack(stack);
        this.count = Math.max(0, count);
    }

    public BagsDescription(CompoundTag tagCompound, HolderLookup.Provider registries) {
        this(
                ItemStack.parseOptional(registries, tagCompound.getCompound("item")),
                tagCompound.getInt("count")
        );
    }

    private static ItemStack normalizePreviewStack(ItemStack original) {
        if (original == null || original.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = original.copy();
        int max = Math.max(1, copy.getMaxStackSize());
        int safeCount = copy.getCount();

        if (safeCount < 1) {
            safeCount = 1;
        } else if (safeCount > max) {
            safeCount = max;
        }

        copy.setCount(safeCount);
        return copy;
    }

    public CompoundTag write(CompoundTag tagCompound, HolderLookup.Provider registries) {
        tagCompound.put("item", this.stack.save(registries));
        tagCompound.putInt("count", this.count);
        return tagCompound;
    }

    public ItemStack getStack() {
        return this.stack.copy();
    }

    public ItemStack getDisplayStack() {
        return this.stack.copy();
    }

    public void addCount(int count) {
        this.count += count;
    }

    public int getCount() {
        return this.count;
    }

    public String getDisplayCountText() {
        return "x" + this.count;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BagsDescription that)) {
            return false;
        }
        return this.stack.getItem() == that.stack.getItem();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.stack.getItem());
    }
}