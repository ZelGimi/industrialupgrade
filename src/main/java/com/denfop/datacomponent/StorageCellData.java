package com.denfop.datacomponent;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record StorageCellData(int storage, List<FluidStack> fluids, List<Integer> stacksForCraft) {

    public static final StorageCellData EMPTY = new StorageCellData(0, List.of(), List.of());

    public static final Codec<FluidStack> CUSTOM_FLUID_CODEC = Codec.either(
            Codec.STRING,
            FluidStack.OPTIONAL_CODEC
    ).xmap(
            either -> {
                if (either.left().isPresent() && "empty".equals(either.left().get())) {
                    return FluidStack.EMPTY;
                }
                return either.right().orElse(FluidStack.EMPTY);
            },
            stack -> stack == null || stack.isEmpty() ? Either.left("empty") : Either.right(stack)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> CUSTOM_FLUID_STREAM_CODEC = StreamCodec.of(
            (buf, stack) -> {
                if (stack == null || stack.isEmpty()) {
                    buf.writeBoolean(true);
                } else {
                    buf.writeBoolean(false);
                    FluidStack.STREAM_CODEC.encode(buf, stack);
                }
            },
            buf -> {
                boolean empty = buf.readBoolean();
                if (empty) {
                    return FluidStack.EMPTY;
                }
                return FluidStack.STREAM_CODEC.decode(buf);
            }
    );
    public static final Codec<StorageCellData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("storage").forGetter(StorageCellData::storage),
            CUSTOM_FLUID_CODEC.listOf().fieldOf("fluids").forGetter(StorageCellData::fluids),
            Codec.INT.listOf().fieldOf("stacksForCraft").forGetter(StorageCellData::stacksForCraft)
    ).apply(instance, StorageCellData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, StorageCellData> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer((ByteBuf) buf, IUCore.registry);
                try {
                    packetBuffer.writeInt(value.storage());
                    EncoderHandler.encode(packetBuffer, value.fluids());
                    EncoderHandler.encode(packetBuffer, value.stacksForCraft());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            buf -> {
                CustomPacketBuffer packetBuffer = new CustomPacketBuffer((ByteBuf) buf, IUCore.registry);
                try {
                    return new StorageCellData(
                            packetBuffer.readInt(),
                            (List<FluidStack>) DecoderHandler.decode(packetBuffer),
                            (List<Integer>) DecoderHandler.decode(packetBuffer)
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
    );

    public static StorageCellData createEmpty(int size) {
        List<FluidStack> fluids = new ArrayList<>(size);
        List<Integer> craft = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            fluids.add(FluidStack.EMPTY);
            craft.add(0);
        }

        return new StorageCellData(0, fluids, craft);
    }

    private static List<FluidStack> copyFluids(List<FluidStack> source) {
        List<FluidStack> result = new ArrayList<>(source.size());
        for (FluidStack fluid : source) {
            result.add(fluid == null ? FluidStack.EMPTY : fluid.copy());
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StorageCellData other)) {
            return false;
        }

        return this.storage == other.storage
                && Objects.equals(this.stacksForCraft, other.stacksForCraft)
                && ComponentEquality.sameFluidStackList(this.fluids, other.fluids);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(this.storage);
        result = 31 * result + ComponentEquality.fluidStackListHash(this.fluids);
        result = 31 * result + Objects.hashCode(this.stacksForCraft);
        return result;
    }

    public StorageCellData normalize(int size) {
        List<FluidStack> newFluids = new ArrayList<>(this.fluids);
        List<Integer> newCraft = new ArrayList<>(this.stacksForCraft);

        while (newFluids.size() < size) {
            newFluids.add(FluidStack.EMPTY);
        }
        while (newCraft.size() < size) {
            newCraft.add(0);
        }

        if (newFluids.size() > size) {
            newFluids = new ArrayList<>(newFluids.subList(0, size));
        }
        if (newCraft.size() > size) {
            newCraft = new ArrayList<>(newCraft.subList(0, size));
        }

        return new StorageCellData(this.storage, newFluids, newCraft);
    }

    public StorageCellData updateStorage(ItemStack stack, int storage) {
        StorageCellData data = new StorageCellData(storage, new ArrayList<>(fluids), new ArrayList<>(stacksForCraft));
        stack.set(DataComponentsInit.STORAGE_CELL, data);
        return data;
    }

    public StorageCellData updateFluids(ItemStack stack, List<FluidStack> fluids) {
        StorageCellData data = new StorageCellData(storage, copyFluids(fluids), new ArrayList<>(stacksForCraft));
        stack.set(DataComponentsInit.STORAGE_CELL, data);
        return data;
    }

    public StorageCellData updateStacksForCraft(ItemStack stack, List<Integer> stacksForCraft) {
        StorageCellData data = new StorageCellData(storage, new ArrayList<>(fluids), new ArrayList<>(stacksForCraft));
        stack.set(DataComponentsInit.STORAGE_CELL, data);
        return data;
    }

    public StorageCellData updateAll(ItemStack stack, int storage, List<FluidStack> fluids, List<Integer> stacksForCraft) {
        StorageCellData data = new StorageCellData(storage, copyFluids(fluids), new ArrayList<>(stacksForCraft));
        stack.set(DataComponentsInit.STORAGE_CELL, data);
        return data;
    }
}