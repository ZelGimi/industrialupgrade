package com.denfop.datacomponent;

import com.denfop.IUCore;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.items.bags.BagsDescription;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.bee.Bee;
import com.denfop.utils.CapturedMobUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.io.IOException;
import java.util.List;

public class DataComponentsInit {
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform>>> WIND_UPGRADE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<RotorUpgradeItemInform>>> WATER_UPGRADE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Double>> ENERGY;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<GenomeCrop>> GENOME_CROP;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<GenomeBee>> GENOME_BEE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> DATA;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<String>> SKIN;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CROP;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BEE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> ACTIVE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> SAVE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> BLACK_LIST;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> FLY;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> VERTICAL;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> NIGHT_VISION;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> JETPACK;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> MODE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LEVEL;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LEVEL_MICROCHIP;

    public static DeferredHolder<DataComponentType<?>, DataComponentType<Byte>> DIRECTION;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EXPERIENCE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SWARM;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<Double>> STORAGE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeItem>> UPGRADE_ITEM;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeRover>> UPGRADE_ROVER;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ContainerItem>> CONTAINER;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ContainerAdditionalItem>> CONTAINER_ADDITIONAL;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<Integer>>> LIST_INTEGER;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<String>>> LIST_STRING;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> LIST_STACK;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<CapturedMobUtils>> MOB;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<String>> NAME;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<BagsDescription>>> DESCRIPTIONS_CONTAINER;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<WirelessConnection>> WIRELESS;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<Bee>>> BEE_LIST;

    public static DeferredHolder<DataComponentType<?>, DataComponentType<VeinInfo>> VEIN_INFO;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<UpgradeKit>> UPGRADE_KIT;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ReactorData>> REACTOR_DATA;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<BlockPos>> TELEPORT;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ReactorSchedule>> REACTOR_SCHEDULE;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<BeerInfo>> BEER;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<ItemStack>> PATTERN;
    public static DeferredHolder<DataComponentType<?>, DataComponentType<List<Tuple<Integer, Integer>>>> BOOKMARK;

    public static StreamCodec<ByteBuf, List<Integer>> INT_ARRAY = new StreamCodec<>() {
        public List<Integer> decode(ByteBuf p_320167_) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(p_320167_, IUCore.registry);
            try {
                return (List<Integer>) DecoderHandler.decode(packetBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void encode(ByteBuf p_320240_, List<Integer> p_341316_) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(p_320240_, IUCore.registry);
            try {
                EncoderHandler.encode(packetBuffer, p_341316_);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    };
    public static final Codec<Tuple<Integer, Integer>> INT_TUPLE_CODEC =
            Codec.INT.listOf()
                    .comapFlatMap(
                            list -> list.size() == 2
                                    ? DataResult.success(new Tuple<>(list.get(0), list.get(1)))
                                    : DataResult.error(() -> "Expected list of 2 integers"),
                            tuple -> List.of(tuple.getA(), tuple.getB())
                    );

    public static void init(DeferredRegister<DataComponentType<?>> dataComponentType) {

        ENERGY = dataComponentType.register("energy", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).build());
        GENOME_CROP = dataComponentType.register("genome_crop", () -> DataComponentType.<GenomeCrop>builder().persistent(GenomeCrop.CODEC).networkSynchronized(GenomeCrop.STREAM_CODEC).build());
        GENOME_BEE = dataComponentType.register("genome_bee", () -> DataComponentType.<GenomeBee>builder().persistent(GenomeBee.CODEC).networkSynchronized(GenomeBee.STREAM_CODEC).build());
        DATA = dataComponentType.register("data", () -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.TRUSTED_COMPOUND_TAG).cacheEncoding().build());
        CROP = dataComponentType.register("crop", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        BEE = dataComponentType.register("bee", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        ACTIVE = dataComponentType.register("active", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        SAVE = dataComponentType.register("save", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        BLACK_LIST = dataComponentType.register("black_list", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        MODE = dataComponentType.register("mode", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        LEVEL = dataComponentType.register("level", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        LEVEL_MICROCHIP = dataComponentType.register("level_microchip", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        EXPERIENCE = dataComponentType.register("experience", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        SWARM = dataComponentType.register("swarm", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build());
        STORAGE = dataComponentType.register("storage", () -> DataComponentType.<Double>builder().persistent(Codec.DOUBLE).networkSynchronized(ByteBufCodecs.DOUBLE).build());
        FLUID = dataComponentType.register("fluid", () -> DataComponentType.<SimpleFluidContent>builder().persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC).build());
        SKIN = dataComponentType.register("skin", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
        UPGRADE_ITEM = dataComponentType.register("upgrade_item", () -> DataComponentType.<UpgradeItem>builder().persistent(UpgradeItem.CODEC).networkSynchronized(UpgradeItem.STREAM_CODEC).build());
        UPGRADE_ROVER = dataComponentType.register("upgrade_rover", () -> DataComponentType.<UpgradeRover>builder().persistent(UpgradeRover.CODEC).networkSynchronized(UpgradeRover.STREAM_CODEC).build());
        CONTAINER = dataComponentType.register("container", () -> DataComponentType.<ContainerItem>builder().persistent(ContainerItem.CODEC).networkSynchronized(ContainerItem.STREAM_CODEC).build());
        CONTAINER_ADDITIONAL = dataComponentType.register("container_additional", () -> DataComponentType.<ContainerAdditionalItem>builder().persistent(ContainerAdditionalItem.CODEC).networkSynchronized(ContainerAdditionalItem.STREAM_CODEC).build());
        LIST_INTEGER = dataComponentType.register("integers", () -> DataComponentType.<List<Integer>>builder().persistent(Codec.INT.listOf()).networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list())).build());
        LIST_STRING = dataComponentType.register("strings", () -> DataComponentType.<List<String>>builder().persistent(Codec.STRING.listOf()).networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list())).build());
        FLY = dataComponentType.register("fly", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        VERTICAL = dataComponentType.register("vertical", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        NIGHT_VISION = dataComponentType.register("night_vision", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        JETPACK = dataComponentType.register("jetpack", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
        DIRECTION = dataComponentType.register("direction", () -> DataComponentType.<Byte>builder().persistent(Codec.BYTE).networkSynchronized(ByteBufCodecs.BYTE).build());
        MOB = dataComponentType.register("mob", () -> DataComponentType.<CapturedMobUtils>builder().persistent(CapturedMobUtils.CODEC).networkSynchronized(CapturedMobUtils.STREAM_CODEC).build());
        LIST_STACK = dataComponentType.register("stacks", () -> DataComponentType.<List<ItemStack>>builder().persistent(ItemStack.CODEC.listOf()).networkSynchronized(ItemStack.LIST_STREAM_CODEC).build());
        NAME = dataComponentType.register("name", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());
        DESCRIPTIONS_CONTAINER = dataComponentType.register("description_container", () -> DataComponentType.<List<BagsDescription>>builder().persistent(BagsDescription.CODEC.listOf()).networkSynchronized(BagsDescription.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
        WATER_UPGRADE = dataComponentType.register("water_upgrade", () -> DataComponentType.<List<RotorUpgradeItemInform>>builder().persistent(RotorUpgradeItemInform.CODEC.listOf()).networkSynchronized(RotorUpgradeItemInform.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
        WIND_UPGRADE = dataComponentType.register("wind_upgrade", () -> DataComponentType.<List<com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform>>builder().persistent(com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform.CODEC.listOf()).networkSynchronized(com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
        WIRELESS = dataComponentType.register("wireless", () -> DataComponentType.<WirelessConnection>builder().persistent(WirelessConnection.CODEC).networkSynchronized(WirelessConnection.STREAM_CODEC).build());
        BEE_LIST = dataComponentType.register("bee_list", () -> DataComponentType.<List<Bee>>builder().persistent(Bee.CODEC.listOf()).networkSynchronized(Bee.STREAM_CODEC.apply(ByteBufCodecs.list())).build());
        VEIN_INFO = dataComponentType.register("vein_info", () -> DataComponentType.<VeinInfo>builder().persistent(VeinInfo.CODEC).networkSynchronized(VeinInfo.STREAM_CODEC).build());
        UPGRADE_KIT = dataComponentType.register("upgrade_kit", () -> DataComponentType.<UpgradeKit>builder().persistent(UpgradeKit.CODEC).networkSynchronized(UpgradeKit.STREAM_CODEC).build());
        REACTOR_DATA = dataComponentType.register("reactor_data", () -> DataComponentType.<ReactorData>builder().persistent(ReactorData.CODEC).networkSynchronized(ReactorData.STREAM_CODEC).build());
        TELEPORT = dataComponentType.register("teleport", () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).networkSynchronized(BlockPos.STREAM_CODEC).build());

        REACTOR_SCHEDULE = dataComponentType.register("reactor_schedule", () -> DataComponentType.<ReactorSchedule>builder().persistent(ReactorSchedule.CODEC).networkSynchronized(ReactorSchedule.STREAM_CODEC).build());
        BEER = dataComponentType.register("beer", () -> DataComponentType.<BeerInfo>builder().persistent(BeerInfo.CODEC).networkSynchronized(BeerInfo.STREAM_CODEC).build());
        PATTERN = dataComponentType.register("pettern", () -> DataComponentType.<ItemStack>builder().persistent(ItemStack.CODEC).networkSynchronized(ItemStack.STREAM_CODEC).build());
        BOOKMARK = dataComponentType.register("bookmark", () -> DataComponentType.<List<Tuple<Integer, Integer>>>builder().persistent( INT_TUPLE_CODEC.listOf()).networkSynchronized(LIST_TUPLE_STREAM_CODEC).build());

    }
    public static final StreamCodec<RegistryFriendlyByteBuf, Tuple<Integer, Integer>> INT_TUPLE_STREAM_CODEC =
            StreamCodec.of(
                    (buf, tuple) -> {
                        buf.writeVarInt(tuple.getA());
                        buf.writeVarInt(tuple.getB());
                    },
                    buf -> new Tuple<>(buf.readVarInt(), buf.readVarInt())
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<Tuple<Integer, Integer>>> LIST_TUPLE_STREAM_CODEC =
            INT_TUPLE_STREAM_CODEC.apply(ByteBufCodecs.list());

}
