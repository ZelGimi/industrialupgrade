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
            ).apply(instance, (stack, count) -> {
                ItemStack copy = stack.copy();
                copy.setCount(count);
                return new BagsDescription(copy);
            })
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, BagsDescription> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                ItemStack.STREAM_CODEC.encode(buf, value.getStack());
                buf.writeInt(value.getCount());
            },
            buf -> {
                ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
                int count = buf.readInt();
                stack.setCount(count);
                return new BagsDescription(stack);
            }
    );
    private final ItemStack stack;
    int count;

    public BagsDescription(ItemStack stack) {
        this.stack = stack;
        this.count = stack.getCount();
    }

    public BagsDescription(CompoundTag tagCompound, HolderLookup.Provider registries) {
        this.stack = ItemStack.parseOptional(registries, tagCompound.getCompound("item"));
        this.count = tagCompound.getInt("count");
    }

    public CompoundTag write(CompoundTag tagCompound, HolderLookup.Provider registries) {
        tagCompound.put("item", this.stack.save(registries));
        tagCompound.putInt("count", this.count);
        return tagCompound;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BagsDescription that = (BagsDescription) o;
        return stack.getItem() == that.stack.getItem();
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }

}
