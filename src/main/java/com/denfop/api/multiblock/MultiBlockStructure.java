package com.denfop.api.multiblock;

import com.denfop.IUCore;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.utils.Localization;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class MultiBlockStructure {

    public final Map<BlockPos, Class<? extends IMultiElement>> blockPosMap = new HashMap<>();
    public final Map<BlockPos, ItemStack> ItemStackMap = new HashMap<>();
    public final List<ItemStack> itemStackList = new ArrayList<>();

    public final Map<BlockPos, Direction> RotationMap = new HashMap<>();


    public final Map<BlockPos, BakedModel> bakedModelMap = new HashMap<>();

    public final BlockPos pos;

    private final Map<Class<? extends IMultiElement>, String> reportInfoMap = new HashMap<>();

    public boolean hasActivatedItem = false;
    public int height;
    public int weight;
    public int length;

    public int maxHeight = Integer.MIN_VALUE;
    public int minHeight = Integer.MAX_VALUE;
    public int maxWeight = Integer.MIN_VALUE;
    public int minWeight = Integer.MAX_VALUE;
    public int maxLength = Integer.MIN_VALUE;
    public int minLength = Integer.MAX_VALUE;

    public boolean ignoreMetadata = false;
    public ItemStack activateItem = ItemStack.EMPTY;
    public boolean hasUniqueModels = false;

    private Class<? extends IMainMultiBlock> main;

    public MultiBlockStructure() {
        this.pos = BlockPos.ZERO;
    }

    public Map<BlockPos, ItemStack> getItemStackMap() {
        return this.ItemStackMap;
    }

    public Map<BlockPos, Direction> getRotationMap() {
        return this.RotationMap;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getHeight() {
        return this.blockPosMap.isEmpty() ? 0 : this.height + 1;
    }

    public int getLength() {
        return this.blockPosMap.isEmpty() ? 0 : this.length + 1;
    }

    public int getWeight() {
        return this.blockPosMap.isEmpty() ? 0 : this.weight + 1;
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
        return this.hasUniqueModels;
    }

    public ItemStack getActivateItem() {
        return this.activateItem;
    }

    public MultiBlockStructure setActivateItem(final ItemStack activateItem) {
        this.activateItem = activateItem == null ? ItemStack.EMPTY : activateItem.copy();
        return this;
    }

    public Vec3 getLocalCenterOffset() {
        if (this.blockPosMap.isEmpty()) {
            return Vec3.ZERO;
        }

        return new Vec3(
                (this.minLength + this.maxLength) / 2.0D,
                (this.minHeight + this.maxHeight) / 2.0D,
                (this.minWeight + this.maxWeight) / 2.0D
        );
    }

    public Vec3 getWorldCenter(final Direction facing, final BlockPos mainPos) {
        final Vec3 local = this.getLocalCenterOffset();

        final double originX = mainPos.getX() + 0.5D;
        final double originY = mainPos.getY() + 0.5D;
        final double originZ = mainPos.getZ() + 0.5D;

        return switch (facing) {
            case NORTH -> new Vec3(
                    originX + local.x,
                    originY + local.y,
                    originZ + local.z
            );
            case EAST -> new Vec3(
                    originX - local.z,
                    originY + local.y,
                    originZ + local.x
            );
            case WEST -> new Vec3(
                    originX + local.z,
                    originY + local.y,
                    originZ - local.x
            );
            case SOUTH -> new Vec3(
                    originX - local.x,
                    originY + local.y,
                    originZ - local.z
            );
            default -> throw new IllegalStateException("Unexpected non-horizontal facing: " + facing);
        };
    }

    public BlockPos getWorldCenterBlockPos(final Direction facing, final BlockPos mainPos) {
        return BlockPos.containing(this.getWorldCenter(facing, mainPos));
    }

    public boolean isActivateItem(final ItemStack stack) {
        if (stack == null || stack.isEmpty() || this.activateItem.isEmpty()) {
            return false;
        }
        return this.activateItem.getItem() == stack.getItem();
    }

    public boolean isHasActivatedItem() {
        return this.hasActivatedItem;
    }

    public MultiBlockStructure setHasActivatedItem(final boolean hasActivatedItem) {
        this.hasActivatedItem = hasActivatedItem;
        return this;
    }

    public void add(final BlockPos pos, final Class<? extends IMultiElement> class1, final ItemStack stack) {
        this.addInternal(pos, class1, stack, null);
    }

    public void add(final BlockPos pos, final Class<? extends IMultiElement> class1, final ItemStack stack, final Direction rotation) {
        this.addInternal(pos, class1, stack, rotation);
    }

    public List<ItemStack> getItemStackList() {
        return this.itemStackList;
    }

    public List<BlockPos> getPosFromClass(final Direction facing, final BlockPos pos, final Class<? extends IMultiElement> class1) {
        final List<BlockPos> blockPosList = new ArrayList<>();

        for (final Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.blockPosMap.entrySet()) {
            if (entry.getValue() == class1) {
                blockPosList.add(this.transformPos(facing, pos, entry.getKey()));
            }
        }

        return blockPosList;
    }

    public List<BlockPos> getPoses(final Direction facing, final BlockPos pos) {
        final List<BlockPos> blockPosList = new ArrayList<>();

        for (final Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.blockPosMap.entrySet()) {
            blockPosList.add(this.transformPos(facing, pos, entry.getKey()));
        }

        return blockPosList;
    }

    public boolean getFull(final Direction facing, final BlockPos pos, final Level world, final Player player) {
        final BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof IMainMultiBlock mainTile)) {
            return false;
        }

        for (final Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.blockPosMap.entrySet()) {
            if (entry.getValue() == this.main) {
                continue;
            }

            final BlockPos pos1 = this.transformPos(facing, pos, entry.getKey());
            final Class<? extends IMultiElement> expectedClass = entry.getValue();

            if (expectedClass == null) {
                final BlockState blockState = world.getBlockState(pos1);
                if (!blockState.isAir()) {
                    return false;
                }
                continue;
            }

            final BlockEntity tile = world.getBlockEntity(pos1);
            if (!expectedClass.isInstance(tile)) {
                if (!world.isClientSide) {
                    final String report = this.reportInfoMap.get(expectedClass);
                    final String expectedName;

                    if (report != null && !report.isEmpty()) {
                        expectedName = Localization.translate(report);
                    } else {
                        final ItemStack expectedStack = this.ItemStackMap.get(entry.getKey());
                        if (expectedStack != null && !expectedStack.isEmpty()) {
                            expectedName = com.denfop.utils.ModUtils.cleanComponentString(expectedStack.getHoverName().getString());
                        } else {
                            expectedName = expectedClass.getSimpleName();
                        }
                    }

                    IUCore.proxy.messagePlayer(
                            player,
                            Localization.translate("iu.not.found")
                                    + " x: " + pos1.getX()
                                    + " y: " + pos1.getY()
                                    + " z: " + pos1.getZ()
                                    + " " + expectedName
                    );
                }
                return false;
            }

            final IMultiElement element = (IMultiElement) tile;

            if ((element.isMain() && element.getMain() != mainTile)
                    || (element.getBlockLevel() != mainTile.getBlockLevel() && element.getBlockLevel() != -1)
                    || !element.canCreateSystem(mainTile)) {
                return false;
            }

            if (element.getMain() != null && element.getMain() != mainTile) {
                final IMainMultiBlock oldMain = element.getMain();
                element.setMainMultiElement(mainTile);
                oldMain.updateFull();

                if (oldMain.wasActivated()) {
                    oldMain.setActivated(false);
                }
            } else if (element.getMain() == null) {
                element.setMainMultiElement(mainTile);
            }
        }

        return true;
    }

    public boolean getFull(final Direction facing, final BlockPos pos, final Level world) {
        final BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof IMainMultiBlock mainTile)) {
            return false;
        }

        for (final Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.blockPosMap.entrySet()) {
            if (entry.getValue() == this.main) {
                continue;
            }

            final BlockPos pos1 = this.transformPos(facing, pos, entry.getKey());
            final Class<? extends IMultiElement> expectedClass = entry.getValue();

            if (expectedClass == null) {
                final BlockState blockState = world.getBlockState(pos1);
                if (!blockState.isAir()) {
                    return false;
                }
                continue;
            }

            final BlockEntity tile = world.getBlockEntity(pos1);
            if (!expectedClass.isInstance(tile)) {
                return false;
            }

            final IMultiElement element = (IMultiElement) tile;
            if ((element.getMain() != null && element.getMain() != mainTile)
                    || (element.getBlockLevel() != mainTile.getBlockLevel() && element.getBlockLevel() != -1)
                    || !element.canCreateSystem(mainTile)) {
                return false;
            }

            if (element.getMain() == null) {
                element.setMainMultiElement(mainTile);
            }
        }

        return true;
    }

    public MultiBlockStructure setMain(final Class<? extends IMainMultiBlock> main) {
        this.main = main;
        return this;
    }

    public void addReport(final Class<? extends IMultiElement> name, final String report) {
        this.reportInfoMap.put(name, report);
    }

    public void markDirty(final BlockEntityMultiBlockBase tileMultiBlockBase, final boolean full) {
        if (tileMultiBlockBase == null) {
            return;
        }

        final Level world = tileMultiBlockBase.getLevel();
        if (world == null) {
            return;
        }

        final Set<ChunkPos> touchedChunks = new HashSet<>();
        final BlockPos mainPos = tileMultiBlockBase.getBlockPos();
        final Direction facing = tileMultiBlockBase.getFacing();

        for (final Map.Entry<BlockPos, Class<? extends IMultiElement>> entry : this.blockPosMap.entrySet()) {
            if (entry.getValue() == this.main) {
                continue;
            }

            final BlockPos pos1 = this.transformPos(facing, mainPos, entry.getKey());
            final BlockEntity tile = world.getBlockEntity(pos1);

            if (tile instanceof BlockEntityMultiBlockElement te) {
                te.setMainMultiElement(full ? tileMultiBlockBase : null);
                te.setChanged();
                touchedChunks.add(new ChunkPos(pos1));
            }
        }

        tileMultiBlockBase.setChanged();

    }

    private void addInternal(final BlockPos pos, final Class<? extends IMultiElement> class1, final ItemStack stack, final Direction rotation) {
        if (pos == null || this.blockPosMap.containsKey(pos)) {
            return;
        }

        final BlockPos immutablePos = pos.immutable();
        final ItemStack storedStack = stack == null ? ItemStack.EMPTY : stack.copy();

        this.blockPosMap.put(immutablePos, class1);
        this.ItemStackMap.put(immutablePos, storedStack);

        if (rotation != null) {
            this.RotationMap.put(immutablePos, rotation);
        }

        if (!storedStack.isEmpty()) {
            this.addToItemStackList(storedStack);
        }

        this.updateBounds(immutablePos);
    }

    private void addToItemStackList(final ItemStack stack) {
        for (final ItemStack stack1 : this.itemStackList) {
            final boolean same;

            if (this.ignoreMetadata) {
                same = stack1.getItem() == stack.getItem();
            } else {
                same = ItemStack.isSameItemSameComponents(stack1, stack);
            }

            if (same) {
                stack1.grow(stack.getCount());
                return;
            }
        }

        this.itemStackList.add(stack.copy());
    }

    private void updateBounds(final BlockPos pos) {
        final int x = pos.getX();
        final int y = pos.getY();
        final int z = pos.getZ();

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

    private BlockPos transformPos(final Direction facing, final BlockPos origin, final BlockPos relative) {
        return switch (facing) {
            case NORTH -> origin.offset(relative);
            case EAST -> origin.offset(-relative.getZ(), relative.getY(), relative.getX());
            case WEST -> origin.offset(relative.getZ(), relative.getY(), -relative.getX());
            case SOUTH -> origin.offset(-relative.getX(), relative.getY(), -relative.getZ());
            default -> throw new IllegalStateException("Unexpected non-horizontal facing: " + facing);
        };
    }

}