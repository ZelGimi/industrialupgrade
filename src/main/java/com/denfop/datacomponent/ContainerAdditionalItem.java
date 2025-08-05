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
                    return new ContainerAdditionalItem(
                            (List<ItemStack>) DecoderHandler.decode(packetBuffer));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
    );

    public ContainerAdditionalItem updateItems(ItemStack stack, List<ItemStack> listItem) {
        ContainerAdditionalItem containerItem = new ContainerAdditionalItem(listItem);
        stack.set(DataComponentsInit.CONTAINER_ADDITIONAL, containerItem);
        return containerItem;
    }

}
