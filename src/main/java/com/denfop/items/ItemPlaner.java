package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.multiblock.MainMultiBlock;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.blockitem.ItemBlockTileEntity;
import com.denfop.utils.Localization;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ItemPlaner extends Item {
    private String nameItem;

    public ItemPlaner() {
        super(new Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(Localization.translate("iu.planner.info")));
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
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getLevel().isClientSide()) {
            return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();
        Direction side = context.getClickedFace();

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile instanceof MainMultiBlock) {
            MainMultiBlock mainMultiBlock = (MainMultiBlock) tile;
            if (!mainMultiBlock.isFull()) {
                for (Map.Entry<BlockPos, ItemStack> entry : mainMultiBlock.getMultiBlockStucture().ItemStackMap.entrySet()) {
                    BlockPos pos1;
                    if (entry.getValue().isEmpty()) {
                        continue;
                    }
                    Direction rotation = mainMultiBlock.getMultiBlockStucture().RotationMap.get(entry.getKey());


                    pos1 = switch (((BlockEntityMultiBlockBase) mainMultiBlock).getFacing()) {
                        case NORTH -> new BlockPos(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getZ());
                        case EAST ->
                                new BlockPos(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                        case WEST ->
                                new BlockPos(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                        case SOUTH ->
                                new BlockPos(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                        default -> throw new IllegalStateException("Unexpected value: ");
                    };

                    final ItemStack item = entry.getValue();
                    if (!player.isCreative()) {
                        for (ItemStack stack1 : player.getInventory().items) {
                            if (stack1.is(item.getItem())) {
                                BlockPos pos2 = pos.offset(pos1);
                                ItemBlockTileEntity item1 = (ItemBlockTileEntity) stack1.getItem();
                                BlockEntity tileEntity = world.getBlockEntity(pos2);
                                if (tileEntity == null && canPlace(world.getBlockState(pos2))) {
                                    BlockPlaceContext placeContext = new BlockPlaceContext(context.getLevel(), context.getPlayer(), context.getHand(), stack1, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos2, false));
                                    if (item1.place(placeContext) == InteractionResult.CONSUME) {
                                        stack1.shrink(1);
                                        BlockEntityMultiBlockElement tileEntity2 = (BlockEntityMultiBlockElement) world.getBlockEntity(pos2);
                                        Direction facing = ((BlockEntityMultiBlockBase) mainMultiBlock).getFacing();
                                        rotation = adjustRotation(facing, rotation);
                                        tileEntity2.setFacing(rotation.getOpposite());
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        BlockPos pos2 = pos.offset(pos1);
                        ItemBlockTileEntity item1 = (ItemBlockTileEntity) item.getItem();
                        BlockEntity tileEntity = world.getBlockEntity(pos2);
                        if (tileEntity == null && canPlace(world.getBlockState(pos2))) {
                            BlockPlaceContext placeContext = new BlockPlaceContext(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(), new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos2, false));
                            if (item1.place(placeContext) == InteractionResult.SUCCESS) {
                                BlockEntityMultiBlockElement tileEntity2 = (BlockEntityMultiBlockElement) world.getBlockEntity(pos2);
                                Direction facing = ((BlockEntityMultiBlockBase) mainMultiBlock).getFacing();
                                rotation = adjustRotation(facing, rotation);
                                tileEntity2.setFacing(rotation.getOpposite());
                            }
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    private Direction adjustRotation(Direction facing, Direction rotation) {
        if (facing == Direction.NORTH) {
            if (rotation == Direction.EAST || rotation == Direction.WEST) {
                rotation = rotation.getOpposite();
            }
        } else if (facing == Direction.SOUTH) {
            if (rotation == Direction.SOUTH || rotation == Direction.NORTH) {
                rotation = rotation.getOpposite();
            }
        } else if (facing == Direction.EAST) {
            if (rotation == Direction.EAST || rotation == Direction.WEST) {
                if (rotation == Direction.EAST) {
                    rotation = Direction.NORTH;
                } else {
                    rotation = Direction.SOUTH;
                }
            } else {
                if (rotation == Direction.SOUTH) {
                    rotation = Direction.WEST;
                } else {
                    rotation = Direction.EAST;
                }
            }
        } else if (facing == Direction.WEST) {
            if (rotation == Direction.EAST || rotation == Direction.WEST) {
                if (rotation == Direction.WEST) {
                    rotation = Direction.NORTH;
                } else {
                    rotation = Direction.SOUTH;
                }
            } else {
                if (rotation == Direction.SOUTH) {
                    rotation = Direction.EAST;
                } else {
                    rotation = Direction.WEST;
                }
            }
        }
        return rotation == null ? Direction.NORTH : rotation;
    }

    private boolean canPlace(BlockState state) {
        return state.getMaterial() == Material.AIR || state.getBlock() == Blocks.TALL_GRASS || state.getMaterial().isLiquid() || state.getMaterial().isReplaceable();
    }

}
