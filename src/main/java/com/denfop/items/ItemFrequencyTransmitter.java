package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityTeleporter;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

public class ItemFrequencyTransmitter extends Item implements IItemTab {
    private String nameItem;

    public ItemFrequencyTransmitter() {
        super(new Properties());
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide && player.isShiftKeyDown()) {

            boolean hadJustSet = stack.has(DataComponentsInit.TELEPORT);

            if (hadJustSet) {
                stack.remove(DataComponentsInit.TELEPORT);
                player.displayClientMessage(Component.translatable("Frequency Transmitter unlinked"), true);
            }


        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack,
                                            UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof BlockEntityTeleporter teleporter)) {
            return InteractionResult.PASS;
        }


        boolean targetSet = stack.has(DataComponentsInit.TELEPORT);
        BlockPos target = stack.getOrDefault(DataComponentsInit.TELEPORT, BlockPos.ZERO);


        if (!targetSet) {
            target = teleporter.getPos();
            stack.set(DataComponentsInit.TELEPORT, target);
            player.displayClientMessage(Component.translatable("Frequency Transmitter linked to Teleporter."), true);
        } else if (teleporter.getPos().equals(target)) {
            player.displayClientMessage(Component.translatable("Can't link Teleporter to itself."), true);
        } else if (teleporter.hasTarget() && teleporter.getTarget().equals(target)) {
            player.displayClientMessage(Component.translatable("Teleportation link unchanged."), true);
        } else {
            BlockEntity targetEntity = level.getBlockEntity(target);
            if (targetEntity instanceof BlockEntityTeleporter targetTeleporter) {
                teleporter.setTarget(target);
                targetTeleporter.setTarget(pos);
                player.displayClientMessage(Component.translatable("Teleportation link established."), true);
            }
        }

        stack.set(DataComponentsInit.TELEPORT, target);

        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> tooltip, TooltipFlag flag) {

        if (stack.has(DataComponentsInit.TELEPORT)) {
            BlockPos pos = stack.get(DataComponentsInit.TELEPORT);
            tooltip.add(Component.translatable(
                    "frequency_transmitter.tooltip.target",
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
            ));
        } else {
            tooltip.add(Component.translatable("frequency_transmitter.tooltip.blank"));
        }
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "item.frequency_transmitter";
        }

        return this.nameItem;
    }
}
