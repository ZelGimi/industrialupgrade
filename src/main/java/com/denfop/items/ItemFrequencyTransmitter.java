package com.denfop.items;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityTeleporter;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

        if (!level.isClientSide) {
            CompoundTag nbtData = stack.getOrCreateTag();
            boolean hadJustSet = nbtData.getBoolean("targetJustSet");

            if (nbtData.getBoolean("targetSet") && !hadJustSet) {
                nbtData.putBoolean("targetSet", false);
                player.displayClientMessage(Component.translatable("Frequency Transmitter unlinked"), true);
            }

            if (hadJustSet) {
                nbtData.putBoolean("targetJustSet", false);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult useOn(
            UseOnContext context
    ) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (level.isClientSide || player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

         if (!(blockEntity instanceof TileEntityTeleporter teleporter)) {
             return InteractionResult.PASS;
          }

        ItemStack stack = context.getItemInHand();
        CompoundTag nbtData = stack.getOrCreateTag();

        boolean targetSet = nbtData.getBoolean("targetSet");
        boolean justSetTarget = true;

        BlockPos target = new BlockPos(
                nbtData.getInt("targetX"),
                nbtData.getInt("targetY"),
                nbtData.getInt("targetZ")
        );

      if (!targetSet) {
            targetSet = true;
            target = teleporter.getPos();
            player.displayClientMessage(Component.translatable("Frequency Transmitter linked to Teleporter."), true);
        } else if (teleporter.getPos().equals(target)) {
            player.displayClientMessage(Component.translatable("Can't link Teleporter to itself."), true);
        } else if (teleporter.hasTarget() && teleporter.getTarget().equals(target)) {
            player.displayClientMessage(Component.translatable("Teleportation link unchanged."), true);
        } else {
            BlockEntity targetEntity = level.getBlockEntity(target);
            if (targetEntity instanceof TileEntityTeleporter targetTeleporter) {
                teleporter.setTarget(target);
                targetTeleporter.setTarget(pos);
                player.displayClientMessage(Component.translatable("Teleportation link established."), true);
            } else {
                justSetTarget = false;
                targetSet = false;
            }
        }

        nbtData.putBoolean("targetSet", targetSet);
        nbtData.putBoolean("targetJustSet", justSetTarget);
        nbtData.putInt("targetX", target.getX());
        nbtData.putInt("targetY", target.getY());
        nbtData.putInt("targetZ", target.getZ());

        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag nbtData = stack.getOrCreateTag();

        if (nbtData.getBoolean("targetSet")) {
            tooltip.add(Component.translatable(
                    "frequency_transmitter.tooltip.target",
                    nbtData.getInt("targetX"),
                    nbtData.getInt("targetY"),
                    nbtData.getInt("targetZ")
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
