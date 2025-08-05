package com.denfop.items.energy;

import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.tile.IWrenchable;
import com.denfop.audio.EnumSound;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemToolWrench extends Item implements IItemTab {
    String nameItem;

    public ItemToolWrench() {
        super(new Properties().durability(120).setNoRepair());
    }

    public static WrenchResult wrenchBlock(Level level, BlockPos pos, Direction side, Player player, boolean remove) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!block.defaultBlockState().isAir()) {
            if (block instanceof IWrenchable wrenchable) {
                Direction currentFacing = wrenchable.getFacing(level, pos);
                Direction newFacing = currentFacing;
                BlockEvent.BreakEvent experience;

                if (!IUCore.keyboard.isChangeKeyDown(player)) {
                    newFacing = player.isShiftKeyDown() ? side.getOpposite() : side;
                } else {
                    Direction.Axis axis = side.getAxis();
                    if ((side.getAxisDirection() == Direction.AxisDirection.POSITIVE && !player.isShiftKeyDown()) ||
                            (side.getAxisDirection() == Direction.AxisDirection.NEGATIVE && player.isShiftKeyDown())) {
                        newFacing = currentFacing.getClockWise(axis);
                    } else {
                        for (int i = 0; i < 3; ++i) {
                            newFacing = newFacing.getClockWise(axis);
                        }
                    }
                }

                if (newFacing != currentFacing && wrenchable.setFacing(level, pos, newFacing, player)) {
                    return WrenchResult.Rotated;
                }

                if (remove && wrenchable.wrenchCanRemove(level, pos, player)) {
                    if (!level.isClientSide) {
                        BlockEntity te = level.getBlockEntity(pos);
                        if (player instanceof ServerPlayer serverPlayer) {
                            experience = CommonHooks.fireBlockBreak(level, serverPlayer.gameMode.getGameModeForPlayer(), serverPlayer, pos, state);
                            if (experience.isCanceled()) {
                                return WrenchResult.Nothing;
                            }
                        }

                        block.playerWillDestroy(level, pos, state, player);
                        if (!block.onDestroyedByPlayer(state, level, pos, player, true, level.getFluidState(pos))) {
                            return WrenchResult.Nothing;
                        }

                        block.destroy(level, pos, state);
                        List<ItemStack> drops = wrenchable.getWrenchDrops(level, pos, state, te, player, RandomSource.create().nextInt(100));
                        if (drops != null && !drops.isEmpty()) {
                            for (ItemStack drop : drops) {
                                ModUtils.dropAsEntity(level, pos, drop);
                            }
                        }

                        wrenchable.wrenchBreak(level, pos);

                    }

                    return WrenchResult.Removed;
                }
            }
        }
        return WrenchResult.Nothing;
    }

    public @NotNull InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        InteractionHand hand = context.getHand();

        if (!this.canTakeDamage(stack, 1)) {
            return InteractionResult.FAIL;
        } else {
            WrenchResult result = wrenchBlock(level, pos, side, player, this.canTakeDamage(stack, 10));
            if (result != WrenchResult.Nothing) {
                if (!level.isClientSide) {
                    this.damage(stack, result == WrenchResult.Rotated ? 1 : 10, player);
                } else {
                    player.playSound(EnumSound.wrench.getSoundEvent(), 1F, 1);
                }

                return level.isClientSide ? InteractionResult.PASS : InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        InteractionHand hand = context.getHand();
        ItemStack stack = ModUtils.get(player, hand);

        if (!this.canTakeDamage(stack, 1)) {
            return InteractionResult.FAIL;
        } else {
            WrenchResult result = wrenchBlock(level, pos, side, player, this.canTakeDamage(stack, 10));
            if (result != WrenchResult.Nothing) {
                if (!level.isClientSide) {
                    this.damage(stack, result == WrenchResult.Rotated ? 1 : 10, player);
                } else {
                    player.playSound(EnumSound.wrench.getSoundEvent(), 1F, 1);
                }

                return level.isClientSide ? InteractionResult.PASS : InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        }
    }

    public boolean canTakeDamage(ItemStack stack, int amount) {
        return true;
    }


    public void damage(ItemStack stack, int amount, Player player) {
        stack.hurtAndBreak(amount, player, EquipmentSlot.MAINHAND);
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
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    private enum WrenchResult {
        Rotated,
        Removed,
        Nothing;

        WrenchResult() {
        }
    }
}
