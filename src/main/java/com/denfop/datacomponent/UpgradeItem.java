package com.denfop.datacomponent;

import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeModificator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public record UpgradeItem(List<UpgradeItemInform> upgradeItemInforms, boolean isRegistry,
                          List<UpgradeModificator> upgradeModificators, int amount, List<Integer> listUpgrades,
                          List<String> blackList, boolean canupgrade) {
    public static final Codec<UpgradeItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UpgradeItemInform.CODEC.listOf().fieldOf("upgradeItemInforms").forGetter(UpgradeItem::upgradeItemInforms),
            Codec.BOOL.fieldOf("isRegistry").forGetter(UpgradeItem::isRegistry),
            UpgradeModificator.CODEC.listOf().fieldOf("upgradeModificators").forGetter(UpgradeItem::upgradeModificators),
            Codec.INT.fieldOf("amount").forGetter(UpgradeItem::amount),
            Codec.INT.listOf().fieldOf("listUpgrades").forGetter(UpgradeItem::listUpgrades),
            Codec.STRING.listOf().fieldOf("blackList").forGetter(UpgradeItem::blackList),
            Codec.BOOL.fieldOf("canUpgrade").forGetter(UpgradeItem::canupgrade)
    ).apply(instance, UpgradeItem::new));
    public static final StreamCodec<FriendlyByteBuf, UpgradeItem> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeCollection(value.upgradeItemInforms(), (b, v) -> UpgradeItemInform.STREAM_CODEC.encode(b, v));
                buf.writeBoolean(value.isRegistry());
                buf.writeCollection(value.upgradeModificators(), (b, v) -> UpgradeModificator.STREAM_CODEC.encode(b, v));
                buf.writeInt(value.amount());
                buf.writeCollection(value.listUpgrades(), FriendlyByteBuf::writeInt);
                buf.writeCollection(value.blackList(), FriendlyByteBuf::writeUtf);
                buf.writeBoolean(value.isRegistry());
            },
            buf -> {
                UpgradeItem upgradeItem = new UpgradeItem(
                        buf.readList(UpgradeItemInform.STREAM_CODEC::decode),
                        buf.readBoolean(),
                        buf.readList(UpgradeModificator.STREAM_CODEC::decode),
                        buf.readInt(),
                        buf.readList(FriendlyByteBuf::readInt),
                        buf.readList(FriendlyByteBuf::readUtf),
                        buf.readBoolean()
                );
                return upgradeItem;
            }
    );
    public static UpgradeItem EMPTY = new UpgradeItem(new ArrayList<>(), false, new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), true);

    public UpgradeItem copy() {
        return new UpgradeItem(new ArrayList<>(), false, new ArrayList<>(), 0, new ArrayList<>(), new ArrayList<>(), true);
    }


    public UpgradeItem updateUpgrades(ItemStack stack, List<UpgradeItemInform> upgradeItemInforms) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }

    public UpgradeItem updateRegistry(ItemStack stack, boolean isRegistry) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }

    public UpgradeItem updateModificator(ItemStack stack, List<UpgradeModificator> upgradeModificators) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }

    public UpgradeItem updateAmount(ItemStack stack, int amount) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }

    public UpgradeItem updateListUpgrades(ItemStack stack, List<Integer> listUpgrades) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM, updated);
        return updated;
    }

    public UpgradeItem updateBlackList(ItemStack stack, List<String> blackList) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }

    public UpgradeItem updateCanUpgrade(ItemStack stack, boolean canupgrade) {
        UpgradeItem updated = new UpgradeItem(upgradeItemInforms, isRegistry, upgradeModificators, amount, listUpgrades, blackList, canupgrade);
        stack.set(DataComponentsInit.UPGRADE_ITEM.get(), updated);
        return updated;
    }


}
