package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.sound.EnumSound;
import com.denfop.utils.DamageHandler;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTreetap extends Item {
    private String nameItem;

    public ItemTreetap() {
        super(new Properties().tab(IUCore.EnergyTab).durability(16).defaultDurability(0).setNoRepair());
    }

    public static boolean attemptExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks
    ) {
        if (state.getBlock() != IUItem.rubWood.getBlock().get()) return false;

        BlockRubWood.RubberWoodState rwState = state.getValue(BlockRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        ejectResin(world, pos, side, world.random.nextInt(3) + 1);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        ejectResin(world, pos, side, 1);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean attemptTropicalExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks
    ) {
        if (state.getBlock() != IUItem.tropicalRubWood.getBlock().get()) return false;

        BlockTropicalRubWood.RubberWoodState rwState = state.getValue(BlockTropicalRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockTropicalRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        ejectResin(world, pos, side, world.random.nextInt(3) + 1);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockTropicalRubWood.stateProperty, BlockTropicalRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        ejectResin(world, pos, side, 1);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean attemptSwampExtract(
            Player player,
            Level world,
            BlockPos pos,
            Direction side,
            BlockState state,
            List<ItemStack> stacks
    ) {
        if (state.getBlock() != IUItem.swampRubWood.getBlock().get()) return false;

        BlockSwampRubWood.RubberWoodState rwState = state.getValue(BlockSwampRubWood.stateProperty);
        if (!rwState.isPlain() && rwState.facing == side) {
            if (rwState.wet) {
                if (!world.isClientSide) {
                    world.setBlock(pos, state.setValue(BlockSwampRubWood.stateProperty, rwState.getDry()), 3);
                    if (stacks == null) {
                        ejectResin(world, pos, side, world.random.nextInt(3) + 1);
                    }
                }
                if (world.isClientSide && player != null) {
                    player.playSound(EnumSound.Treetap.getSoundEvent(), 1F, 1F);
                }
                return true;
            } else {
                if (!world.isClientSide && world.random.nextInt(5) == 0) {
                    world.setBlock(pos, state.setValue(BlockSwampRubWood.stateProperty, BlockSwampRubWood.RubberWoodState.plain_y), 3);
                }

                if (world.random.nextInt(5) == 0) {
                    if (!world.isClientSide) {
                        ejectResin(world, pos, side, 1);
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private static void ejectResin(Level world, BlockPos pos, Direction side, int quantity) {
        double ejectX = pos.getX() + 0.5 + side.getStepX() * 0.3;
        double ejectY = pos.getY() + 0.5 + side.getStepY() * 0.3;
        double ejectZ = pos.getZ() + 0.5 + side.getStepZ() * 0.3;

        ItemEntity itemEntity = new ItemEntity(
                world,
                ejectX,
                ejectY,
                ejectZ,
                new ItemStack(IUItem.rawLatex.getItem(), quantity)
        );
        world.addFreshEntity(itemEntity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.treetap.info")));
    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
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
    public InteractionResult useOn(
            UseOnContext context
    ) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction side = context.getClickedFace();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() == IUItem.rubWood.getBlock().get()) {
            if (attemptExtract(player, world, pos, side, state, null)) {
                if (!world.isClientSide) {
                    DamageHandler.damage(player.getItemInHand(context.getHand()), 1, player);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (state.getBlock() == IUItem.swampRubWood.getBlock().get()) {
            if (attemptSwampExtract(player, world, pos, side, state, null)) {
                if (!world.isClientSide) {
                    DamageHandler.damage(player.getItemInHand(context.getHand()), 1, player);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        if (state.getBlock() == IUItem.tropicalRubWood.getBlock().get()) {
            if (attemptTropicalExtract(player, world, pos, side, state, null)) {
                if (!world.isClientSide) {
                    DamageHandler.damage(player.getItemInHand(context.getHand()), 1, player);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }


}
