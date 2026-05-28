package com.denfop.datacomponent;

import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record ContainerAdditionalItem(List<ItemStack> listItem) {

    public static final ContainerAdditionalItem EMPTY = new ContainerAdditionalItem(new ArrayList<>());

    public static final Codec<ItemStack> CUSTOM_ITEMSTACK_CODEC = Codec.either(
            Codec.STRING,
            ItemStack.CODEC
    ).xmap(
            either -> {
                if (either.left().isPresent() && either.left().get().equals("empty")) {
                    return ItemStack.EMPTY;
                }
                return either.right().orElse(ItemStack.EMPTY);
            },
            stack -> stack.isEmpty() ? Either.left("empty") : Either.right(stack)
    );

    public static final Codec<ContainerAdditionalItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CUSTOM_ITEMSTACK_CODEC.listOf().fieldOf("listItem").forGetter(ContainerAdditionalItem::listItem)
    ).apply(instance, ContainerAdditionalItem::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerAdditionalItem> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                try {
                    EncoderHandler.encode(packetBuffer, value.listItem());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            buf -> {
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                try {
                    return new ContainerAdditionalItem((List<ItemStack>) DecoderHandler.decode(packetBuffer));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    );

    private static List<ItemStack> copyStacks(List<ItemStack> source) {
        List<ItemStack> result = new ArrayList<>(source.size());
        for (ItemStack stack : source) {
            result.add(stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }
        return result;
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

    private static boolean sameStackList(List<ItemStack> left, List<ItemStack> right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.size() != right.size()) {
            return false;
        }

        for (int i = 0; i < left.size(); i++) {
            if (!sameStack(left.get(i), right.get(i))) {
                return false;
            }
        }
        return true;
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

    private static int stackListHash(List<ItemStack> stacks) {
        if (stacks == null) {
            return 0;
        }

        int result = 1;
        for (ItemStack stack : stacks) {
            result = 31 * result + stackHash(stack);
        }
        return result;
    }

    public ContainerAdditionalItem updateItems(ItemStack stack, List<ItemStack> listItem) {
        ContainerAdditionalItem containerItem = new ContainerAdditionalItem(copyStacks(listItem));
        stack.set(DataComponentsInit.CONTAINER_ADDITIONAL, containerItem);
        return containerItem;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContainerAdditionalItem other)) {
            return false;
        }

        return sameStackList(this.listItem, other.listItem);
    }

    @Override
    public int hashCode() {
        return stackListHash(this.listItem);
    }
}