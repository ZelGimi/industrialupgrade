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
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public record ContainerItem(boolean open, int slot_inventory, List<ItemStack> listItem, int uid) {
    public static final ContainerItem EMPTY = new ContainerItem(false, -1, new ArrayList<>(), 0);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStack> CUSTOM_ITEMSTACK_STREAM_CODEC = StreamCodec.of(
            (buf, stack) -> {
                if (stack.isEmpty()) {
                    buf.writeBoolean(true);
                } else {
                    buf.writeBoolean(false);
                    ItemStack.STREAM_CODEC.encode(buf, stack);
                }
            },
            buf -> {
                boolean isEmpty = buf.readBoolean();
                if (isEmpty) {
                    return ItemStack.EMPTY;
                } else {
                    return ItemStack.STREAM_CODEC.decode(buf);
                }
            }
    );
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
    public static final Codec<ContainerItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("open").forGetter(ContainerItem::open),
            Codec.INT.fieldOf("slot_inventory").forGetter(ContainerItem::slot_inventory),
            CUSTOM_ITEMSTACK_CODEC.listOf().fieldOf("listItem").forGetter(ContainerItem::listItem),
            Codec.INT.fieldOf("uid").forGetter(ContainerItem::uid)
    ).apply(instance, ContainerItem::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerItem> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeBoolean(value.open());
                buf.writeInt(value.slot_inventory());
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                try {
                    EncoderHandler.encode(packetBuffer, value.listItem());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                buf.writeInt(value.uid());
            },
            buf -> {
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer(buf);
                try {
                    return new ContainerItem(
                            packetBuffer.readBoolean(),
                            packetBuffer.readInt(),
                            (List<ItemStack>) DecoderHandler.decode(packetBuffer),
                            packetBuffer.readInt());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
    );

    public static ContainerItem getContainer(ItemStack containerStack) {
        @Nullable ContainerItem containerItem = containerStack.get(DataComponentsInit.CONTAINER);
        if (containerItem == null) {
            containerItem = ContainerItem.EMPTY.updateOpen(containerStack, false);
            containerStack.set(DataComponentsInit.CONTAINER, containerItem);
        }
        return containerItem;
    }

    public ContainerItem updateOpen(ItemStack stack, boolean open) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, new ArrayList<>(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateSlot(ItemStack stack, int slot_inventory) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, new ArrayList<>(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateItems(ItemStack stack, List<ItemStack> listItem) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, listItem, uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }

    public ContainerItem updateUUID(ItemStack stack, int uid) {
        ContainerItem containerItem = new ContainerItem(open, slot_inventory, new ArrayList<>(listItem), uid);
        stack.set(DataComponentsInit.CONTAINER, containerItem);
        return containerItem;
    }
}
