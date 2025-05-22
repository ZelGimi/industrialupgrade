package com.denfop.api.multiblock;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiBlockStructure {

    public final Map<BlockPos, Class<? extends IMultiElement>> blockPosMap = new HashMap<>();
    public final Map<BlockPos, ItemStack> ItemStackMap = new HashMap<>();
    public final List<ItemStack> itemStackList = new ArrayList<>();

    public final Map<BlockPos, EnumFacing> RotationMap = new HashMap<>();
    public final Map<BlockPos, IBakedModel> bakedModelMap = new HashMap<>();
    public final BlockPos pos;
    private final Map<Class<? extends IMultiElement>, String> reportLaskBlock = new HashMap<>();
    public boolean hasActivatedItem = false;
    public int height;
    public int weight;
    public int length;
    public int maxHeight;
    public int minHeight;
    public int maxWeight;
    public int minWeight;
    public int maxLength;
    public int minLength;
    public boolean ignoreMetadata = false;
    public ItemStack activateItem = ItemStack.EMPTY;

    public boolean hasUniqueModels = false;

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

    public int getHeight() {
        return height + 1;
    }

    public int getLength() {
        return length + 1;
    }

    public int getWeight() {
        return weight + 1;
    }

    public MultiBlockStructure setIgnoreMetadata(final boolean ignoreMetadata) {
        this.ignoreMetadata = ignoreMetadata;
        return this;
    }

    public MultiBlockStructure setUniqueModel() {
        this.hasUniqueModels = true;
        return this;
    }

    public boolean isHasUniqueModels() {
        return hasUniqueModels;
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
        boolean found = false;
        for (ItemStack stack1 : this.itemStackList) {
            if (stack1.isItemEqual(stack)) {
                stack1.grow(stack.getCount());
                found = true;
                break;
            }
        }
        if (!found) {
            this.itemStackList.add(stack.copy());
        }
        this.blockPosMap.put(pos, class1);
        this.ItemStackMap.put(pos, stack);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (y < this.minHeight) {
            this.minHeight = y;
        }
        if (y > this.maxHeight) {
            this.maxHeight = y;
        }
        if (x < this.minLength) {
            this.minLength = x;
        }
        if (x > this.maxLength) {
            this.maxLength = x;
        }
        if (z < this.minWeight) {
            this.minWeight = z;
        }
        if (z > this.maxWeight) {
            this.maxWeight = z;
        }
        this.height = this.maxHeight - this.minHeight;
        this.weight = this.maxWeight - this.minWeight;
        this.length = this.maxLength - this.minLength;
    }

    public List<ItemStack> getItemStackList() {
        return itemStackList;
    }

    public void add(BlockPos pos, Class<? extends IMultiElement> class1, ItemStack stack, EnumFacing rotation) {
        if (this.blockPosMap.containsKey(pos)) {
            return;
        }
        boolean found = false;
        for (ItemStack stack1 : this.itemStackList) {
            if (stack1.isItemEqual(stack)) {
                stack1.grow(stack.getCount());
                found = true;
                break;
            }
        }
        if (!found) {
            this.itemStackList.add(stack.copy());
        }
        this.blockPosMap.put(pos, class1);
        this.ItemStackMap.put(pos, stack);
        this.RotationMap.put(pos, rotation);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (y < this.minHeight) {
            this.minHeight = y;
        }
        if (y > this.maxHeight) {
            this.maxHeight = y;
        }
        if (x < this.minLength) {
            this.minLength = x;
        }
        if (x > this.maxLength) {
            this.maxLength = x;
        }
        if (z < this.minWeight) {
            this.minWeight = z;
        }
        if (z > this.maxWeight) {
            this.maxWeight = z;
        }
        this.height = this.maxHeight - this.minHeight;
        this.weight = this.maxWeight - this.minWeight;
        this.length = this.maxLength - this.minLength;
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

    public List<BlockPos> getPoses(EnumFacing facing, BlockPos pos) {
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
            blockPosList.add(pos1);

        }
        return blockPosList;
    }

    public boolean getFull(EnumFacing facing, BlockPos pos, World world, EntityPlayer player) {
        final IMainMultiBlock mainTile = (IMainMultiBlock) world.getTileEntity(pos);
        if (mainTile == null) {
            return false;
        }
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

                if (report != null && !report.isEmpty()) {
                    if (!world.isRemote) {
                        IUCore.proxy.messagePlayer(
                                player,
                                Localization.translate("iu.not.found") + "x: " + pos1.getX() + " y: " + pos1.getY() + " z: " + pos1.getZ() + " " + Localization.translate(
                                        report)
                        );
                    }
                } else {
                    if (!world.isRemote) {
                        IUCore.proxy.messagePlayer(
                                player,
                                Localization.translate("iu.not.found") + "x: " + pos1.getX() + " y: " + pos1.getY() + " z: " + pos1.getZ() + " " + this.ItemStackMap
                                        .get(entry.getKey())
                                        .getDisplayName()
                        );
                    }
                }
                return false;
            } else {
                IMultiElement element = (IMultiElement) tile;
                if ((element.isMain() && element.getMain() != mainTile) || (element.getBlockLevel() != mainTile.getBlockLevel() && element.getBlockLevel() != -1) || (!element.canCreateSystem(
                        mainTile))) {
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
                if ((element.getMain() != null && element.getMain() != mainTile) || (element.getBlockLevel() != mainTile.getBlockLevel() && element.getBlockLevel() != -1) || !element.canCreateSystem(
                        mainTile)) {
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

    public void markDirty(TileMultiBlockBase tileMultiBlockBase, boolean full) {
        List<ChunkPos> passedChunk = new ArrayList<>();
        if (full) {
            for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : blockPosMap.entrySet()) {
                if (entry.getValue() == main) {
                    continue;
                }
                final World world = tileMultiBlockBase.getWorld();
                BlockPos pos1 = null;
                BlockPos pos = tileMultiBlockBase.getBlockPos();
                switch (tileMultiBlockBase.getFacing()) {
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
                }
                TileEntity tile = world.getTileEntity(pos1);
                if (tile instanceof TileEntityMultiBlockElement) {
                    TileEntityMultiBlockElement te = (TileEntityMultiBlockElement) world.getTileEntity(pos1);
                    te.setMainMultiElement(tileMultiBlockBase);
                    ChunkPos chunkPos = new ChunkPos(pos1);
                    if (!passedChunk.contains(chunkPos)) {
                        world.markChunkDirty(pos1, null);
                        passedChunk.add(chunkPos);
                    }
                }
            }
        } else {
            for (Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : blockPosMap.entrySet()) {
                if (entry.getValue() == main) {
                    continue;
                }
                final World world = tileMultiBlockBase.getWorld();
                BlockPos pos1 = null;
                BlockPos pos = tileMultiBlockBase.getBlockPos();
                switch (tileMultiBlockBase.getFacing()) {
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
                }
                TileEntity tile = world.getTileEntity(pos1);
                if (tile instanceof TileEntityMultiBlockElement) {
                    TileEntityMultiBlockElement te = (TileEntityMultiBlockElement) world.getTileEntity(pos1);
                    te.setMainMultiElement(null);
                    ChunkPos chunkPos = new ChunkPos(pos1);
                    if (!passedChunk.contains(chunkPos)) {
                        world.markChunkDirty(pos1, null);
                        passedChunk.add(chunkPos);
                    }
                }
            }
        }
    }

}
