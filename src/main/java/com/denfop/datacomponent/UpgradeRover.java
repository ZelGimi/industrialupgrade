package com.denfop.datacomponent;

import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public record UpgradeRover(List<SpaceUpgradeItemInform> upgradeItemInforms, boolean isRegistry, int amount,
                           boolean canupgrade) {
    public static final Codec<UpgradeRover> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SpaceUpgradeItemInform.CODEC.listOf().fieldOf("upgradeItemInforms").forGetter(UpgradeRover::upgradeItemInforms),
            Codec.BOOL.fieldOf("isRegistry").forGetter(UpgradeRover::isRegistry),
            Codec.INT.fieldOf("amount").forGetter(UpgradeRover::amount),
            Codec.BOOL.fieldOf("canUpgrade").forGetter(UpgradeRover::canupgrade)
    ).apply(instance, UpgradeRover::new));
    public static final StreamCodec<FriendlyByteBuf, UpgradeRover> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeCollection(value.upgradeItemInforms(), (b, v) -> SpaceUpgradeItemInform.STREAM_CODEC.encode(b, v));
                buf.writeBoolean(value.isRegistry());
                buf.writeInt(value.amount());
                buf.writeBoolean(value.isRegistry());
            },
            buf -> {
                UpgradeRover upgradeItem = new UpgradeRover(
                        buf.readList(SpaceUpgradeItemInform.STREAM_CODEC::decode),
                        buf.readBoolean(),
                        buf.readInt(),
                        buf.readBoolean()
                );
                return upgradeItem;
            }
    );
    public static UpgradeRover EMPTY = new UpgradeRover(new ArrayList<>(), false, 0, true);

    public UpgradeRover copy() {
        return new UpgradeRover(new ArrayList<>(), false, 0, true);
    }


    public UpgradeRover updateUpgrades(ItemStack stack, List<SpaceUpgradeItemInform> upgradeItemInforms) {
        UpgradeRover updated = new UpgradeRover(upgradeItemInforms, isRegistry, amount, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ROVER.get(), updated);
        return updated;
    }

    public UpgradeRover updateRegistry(ItemStack stack, boolean isRegistry) {
        UpgradeRover updated = new UpgradeRover(upgradeItemInforms, isRegistry, amount, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ROVER.get(), updated);
        return updated;
    }


    public UpgradeRover updateAmount(ItemStack stack, int amount) {
        UpgradeRover updated = new UpgradeRover(upgradeItemInforms, isRegistry, amount, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ROVER.get(), updated);
        return updated;
    }


    public UpgradeRover updateCanUpgrade(ItemStack stack, boolean canupgrade) {
        UpgradeRover updated = new UpgradeRover(upgradeItemInforms, isRegistry, amount, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ROVER.get(), updated);
        return updated;
    }


}
