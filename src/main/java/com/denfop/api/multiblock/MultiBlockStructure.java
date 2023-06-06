package com.denfop.api.multiblock;

import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiBlockStructure {

    public final Map<BlockPos, Class<? extends IMultiElement>> blockPosMap = new HashMap<>();
    public final Map<BlockPos, ItemStack> ItemStackMap = new HashMap<>();
    public final Map<BlockPos, EnumFacing> RotationMap = new HashMap<>();
    public final BlockPos pos;
    private final Map<Class<? extends IMultiElement>, String> reportLaskBlock = new HashMap<>();
    public boolean hasActivatedItem = false;

    public boolean ignoreMetadata = false;
    public ItemStack activateItem = ItemStack.EMPTY;

    private Class<? extends IMainMultiBlock> main;

    public MultiBlockStructure() {
        this.pos = BlockPos.ORIGIN;
        // TODO: EAST -> (z -> x) (x -> z) z < 0 x < 0
        // TODO: NORTH DEFAULT z > 0
        // TODO: WEST -> (z -> x) (x -> z)
        // TODO: SOUTH DEFAULT z < 0
    }

    public BlockPos getPos() {
        return pos;
    }


    public MultiBlockStructure setIgnoreMetadata(final boolean ignoreMetadata) {
        this.ignoreMetadata = ignoreMetadata;
        return this;
    }

    public ItemStack getActivateItem() {
        return activateItem;
    }

    public MultiBlockStructure setActivateItem(final ItemStack activateItem) {
        this.activateItem = activateItem;
        return this;
    }

    public boolean isActivateItem(ItemStack stack) {
        assert stack != null;
        assert !stack.isEmpty();
        return this.ignoreMetadata ? this.activateItem.getItem() == stack.getItem() : this.activateItem.isItemEqual(stack);
    }

    public boolean isHasActivatedItem() {
        return hasActivatedItem;
    }

    public MultiBlockStructure setHasActivatedItem(final boolean hasActivatedItem) {
        this.hasActivatedItem = hasActivatedItem;
        return this;
    }

    public void add(BlockPos pos, Class<? extends IMultiElement> class1, ItemStack stack) {
        if (this.blockPosMap.containsKey(pos)) {
            return;
        }
        this.blockPosMap.put(pos, class1);
        this.ItemStackMap.put(pos, stack);

    }

    public void add(BlockPos pos, Class<? extends IMultiElement> class1, ItemStack stack, EnumFacing rotation) {
        if (this.blockPosMap.containsKey(pos)) {
            return;
        }
        this.blockPosMap.put(pos, class1);
        this.ItemStackMap.put(pos, stack);
        this.RotationMap.put(pos, rotation);
    }

    public List<BlockPos> getPosFromClass(EnumFacing facing, BlockPos pos, Class<? extends IMultiElement> class1) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : blockPosMap.entrySet()) {
            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            if (entry.getValue() == class1) {
                blockPosList.add(pos1);
            }

        }
        return blockPosList;
    }

    public boolean getFull(EnumFacing facing, BlockPos pos, World world, EntityPlayer player) {
        final IMainMultiBlock mainTile = (IMainMultiBlock) world.getTileEntity(pos);
        for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : blockPosMap.entrySet()) {
            if (entry.getValue() == main) {
                continue;
            }

            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            final TileEntity tile = world.getTileEntity(pos1);
            if (entry.getValue() == null) {
                final IBlockState blockstate = world.getBlockState(pos1);
                if (blockstate.getMaterial() != Material.AIR) {
                    return false;
                }
            } else if (!entry.getValue().isInstance(tile)) {
                String report = this.reportLaskBlock.get(entry.getValue());
                if (!report.isEmpty()) {
                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.not.found") + pos1 + " " + Localization.translate(report)
                        );
                    }
                } else {
                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                Localization.translate("iu.not.found") + pos1
                        );
                    }
                }
                return false;
            } else {
                IMultiElement element = (IMultiElement) tile;
                if (element.isMain() && element.getMain() != mainTile) {
                    return false;
                }
                if (element.getMain() != null && element.getMain() != mainTile) {
                    final IMainMultiBlock mainTwo = element.getMain();
                    element.setMainMultiElement(mainTile);
                    mainTwo.updateFull();
                    if (mainTwo.wasActivated()) {
                        mainTwo.setActivated(false);
                    }
                } else if (element.getMain() == null) {
                    element.setMainMultiElement(mainTile);
                }
            }
        }
        return true;
    }

    public boolean getFull(EnumFacing facing, BlockPos pos, World world) {
        final IMainMultiBlock mainTile = (IMainMultiBlock) world.getTileEntity(pos);
        for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : blockPosMap.entrySet()) {
            if (entry.getValue() == main) {
                continue;
            }
            BlockPos pos1;
            switch (facing) {
                case NORTH:
                    pos1 = pos.add(entry.getKey());
                    break;
                case EAST:
                    pos1 = pos.add(entry.getKey().getZ() * -1, entry.getKey().getY(), entry.getKey().getX());
                    break;
                case WEST:
                    pos1 = pos.add(entry.getKey().getZ(), entry.getKey().getY(), entry.getKey().getX() * -1);
                    break;
                case SOUTH:
                    pos1 = pos.add(entry.getKey().getX() * -1, entry.getKey().getY(), entry.getKey().getZ() * -1);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + facing);
            }
            final TileEntity tile = world.getTileEntity(pos1);
            if (entry.getValue() == null) {
                final IBlockState blockstate = world.getBlockState(pos1);
                if (blockstate.getMaterial() != Material.AIR) {
                    return false;
                }
            } else if (!entry.getValue().isInstance(tile)) {
                return false;
            } else {
                IMultiElement element = (IMultiElement) tile;
                if (element.getMain() != null && element.getMain() != mainTile) {
                    return false;
                } else if (element.getMain() == null) {
                    element.setMainMultiElement(mainTile);
                }

            }
        }

        return true;
    }


    public MultiBlockStructure setMain(Class<? extends IMainMultiBlock> main) {
        this.main = main;
        return this;
    }

    public void addReport(Class<? extends IMultiElement> name, String report) {
        reportLaskBlock.put(name, report);
    }

}
