package com.denfop.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record UpgradeKit(ItemStack input, ItemStack output) {

    public static final Codec<UpgradeKit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ContainerItem.CUSTOM_ITEMSTACK_CODEC.fieldOf("input").forGetter(UpgradeKit::input),
            ContainerItem.CUSTOM_ITEMSTACK_CODEC.fieldOf("output").forGetter(UpgradeKit::output)
    ).apply(instance, UpgradeKit::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeKit> STREAM_CODEC = StreamCodec.composite(
            ContainerItem.CUSTOM_ITEMSTACK_STREAM_CODEC,
            UpgradeKit::input,
            ContainerItem.CUSTOM_ITEMSTACK_STREAM_CODEC,
            UpgradeKit::output,
            UpgradeKit::new
    );

    public UpgradeKit {
        input = copyStack(input);
        output = copyStack(output);
    }

    private static ItemStack copyStack(ItemStack stack) {
        return stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy();
    }

    private static boolean sameStack(ItemStack a, ItemStack b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        if (a.isEmpty() != b.isEmpty()) {
            return false;
        }
        return a.getCount() == b.getCount() && ItemStack.isSameItemSameComponents(a, b);
    }

    private static int stackHash(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }
        return Objects.hash(
                stack.getItem(),
                stack.getCount(),
                stack.getComponents()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UpgradeKit other)) {
            return false;
        }

        return sameStack(this.input, other.input)
                && sameStack(this.output, other.output);
    }

    @Override
    public int hashCode() {
        int result = stackHash(this.input);
        result = 31 * result + stackHash(this.output);
        return result;
    }
}