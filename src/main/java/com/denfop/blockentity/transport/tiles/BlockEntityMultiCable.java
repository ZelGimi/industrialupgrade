package com.denfop.blockentity.transport.tiles;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.transport.ITransportConductor;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.transport.types.ICableItem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCable;
import com.denfop.datacomponent.ContainerItem;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.transport.DataCable;
import com.denfop.screen.ScreenCable;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.denfop.blocks.BlockTileEntity.*;

public class BlockEntityMultiCable extends BlockEntityInventory implements IUpdatableTileEvent {

    public static final NumberFormat lossFormat = new DecimalFormat("0.00#");
    private static final String NBT_BLACKLIST = "list";
    private static final String NBT_FACADE_SIDES = "facadeSides";
    private static final String NBT_FACADE_SIDE = "side";
    private static final String NBT_FACADE_STACK = "stack";
    private static final String NBT_LEGACY_STACK_FACADE = "stackFacade";
    private static final double FACADE_AABB_THICKNESS = 1.0D / 16.0D;
    public static List<BlockEntityType<? extends BlockEntityMultiCable>> list = new ArrayList<>();
    private final ItemStack[] facadeStacks = new ItemStack[Direction.values().length];
    public ICableItem cableItem;
    public byte connectivity;
    public ItemStack stackFacade = ItemStack.EMPTY;
    @OnlyIn(Dist.CLIENT)
    public DataCable dataCable;

    private ResourceLocation texture;
    private List<Direction> blackList = new ArrayList<>();

    public BlockEntityMultiCable(ICableItem name, MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);
        this.cableItem = name;
        this.connectivity = 0;

        for (int i = 0; i < this.facadeStacks.length; i++) {
            this.facadeStacks[i] = ItemStack.EMPTY;
        }

        if (list != null && pos.equals(BlockPos.ZERO)) {
            list.add((BlockEntityType<? extends BlockEntityMultiCable>) tileBlock.getBlockType());
        }
    }

    public List<Direction> getBlackList() {
        return blackList;
    }

    public ICableItem getCableItem() {
        return cableItem;
    }

    public ResourceLocation getTexture() {
        if (this.texture == null) {
            this.texture = ResourceLocation.tryBuild(
                    Constants.MOD_ID,
                    "blocks/wiring/" + getCableItem().getMainPath() + "/" + getCableItem().getNameCable()
            );
        }
        return this.texture;
    }

    public void removeConductor() {
        this.getWorld().setBlock(this.pos, Blocks.AIR.defaultBlockState(), 3);
    }

    public ItemStack getFacade(Direction side) {
        if (side == null) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = this.facadeStacks[side.ordinal()];
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public boolean hasFacade(Direction side) {
        return !this.getFacade(side).isEmpty();
    }

    public boolean hasAnyFacade() {
        for (Direction direction : Direction.values()) {
            if (this.hasFacade(direction)) {
                return true;
            }
        }
        return false;
    }

    public List<ItemStack> getFacadeStacksForSync() {
        List<ItemStack> ret = new ArrayList<>(Direction.values().length);
        for (Direction direction : Direction.values()) {
            ret.add(this.getFacade(direction).copy());
        }
        return ret;
    }

    private void clearFacadesInternal() {
        for (int i = 0; i < this.facadeStacks.length; i++) {
            this.facadeStacks[i] = ItemStack.EMPTY;
        }
        this.stackFacade = ItemStack.EMPTY;
    }

    private void updateLegacyFacadeSnapshot() {
        this.stackFacade = ItemStack.EMPTY;

        for (Direction direction : Direction.values()) {
            ItemStack stack = this.getFacade(direction);
            if (!stack.isEmpty()) {
                this.stackFacade = stack.copy();
                return;
            }
        }
    }

    private void applyFacadePayload(List<ItemStack> stacks) {
        this.clearFacadesInternal();

        if (stacks == null) {
            return;
        }

        int max = Math.min(Direction.values().length, stacks.size());
        for (int i = 0; i < max; i++) {
            ItemStack stack = stacks.get(i);
            this.facadeStacks[i] = this.normalizeFacadeStack(stack);
        }

        this.updateLegacyFacadeSnapshot();
    }

    private void applyLegacyFacadePayload(ItemStack stack) {
        ItemStack normalized = this.normalizeFacadeStack(stack);
        this.clearFacadesInternal();

        if (!normalized.isEmpty()) {
            for (Direction direction : Direction.values()) {
                this.facadeStacks[direction.ordinal()] = normalized.copy();
            }
        }

        this.updateLegacyFacadeSnapshot();
    }

    private ItemStack normalizeFacadeStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return ItemStack.EMPTY;
        }

        Block block = blockItem.getBlock();
        if (block == Blocks.AIR) {
            return ItemStack.EMPTY;
        }

        BlockState state = block.defaultBlockState();
        if (state.getRenderShape() != RenderShape.MODEL) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    private ItemStack extractFacadeFromFacadeItem(ItemStack facadeToolStack) {

        if (facadeToolStack == null || facadeToolStack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (facadeToolStack.getItem() != IUItem.facadeItem.getItem()) {
            return ItemStack.EMPTY;
        }

        ContainerItem containerItem = ContainerItem.getContainer(facadeToolStack);
        if (containerItem == ContainerItem.EMPTY)
            return ItemStack.EMPTY;
        ItemStack extracted = containerItem.listItem().get(0).copy();
        if (extracted.isEmpty()) {
            return ItemStack.EMPTY;
        }


        return this.normalizeFacadeStack(extracted);
    }

    private boolean setFacade(Direction side, ItemStack facadeStack) {
        if (side == null) {
            return false;
        }

        ItemStack normalized = this.normalizeFacadeStack(facadeStack);
        if (normalized.isEmpty()) {
            return false;
        }

        ItemStack current = this.getFacade(side);
        if (ItemStack.isSameItemSameComponents(current, normalized)) {
            return false;
        }

        this.facadeStacks[side.ordinal()] = normalized.copy();
        this.onFacadeStorageChanged();
        return true;
    }

    private boolean fillEmptyFacades(ItemStack facadeStack) {
        ItemStack normalized = this.normalizeFacadeStack(facadeStack);
        if (normalized.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (Direction direction : Direction.values()) {
            if (this.getFacade(direction).isEmpty()) {
                this.facadeStacks[direction.ordinal()] = normalized.copy();
                changed = true;
            }
        }

        if (changed) {
            this.onFacadeStorageChanged();
        }

        return changed;
    }

    private boolean clearAllFacades() {
        if (!this.hasAnyFacade()) {
            return false;
        }

        this.clearFacadesInternal();
        this.onFacadeStorageChanged();
        return true;
    }

    private void onFacadeStorageChanged() {
        this.updateLegacyFacadeSnapshot();
        this.setChanged();

        if (this.level != null && !this.level.isClientSide) {
            new PacketUpdateFieldTile(this, "facadeStacks", this.getFacadeStacksForSync());
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);

        if (!this.blackList.isEmpty()) {
            final CompoundTag listTag = new CompoundTag();
            listTag.putInt("size", this.blackList.size());

            for (int i = 0; i < this.blackList.size(); i++) {
                listTag.putInt(String.valueOf(i), this.blackList.get(i).ordinal());
            }

            nbt.put(NBT_BLACKLIST, listTag);
        }

        ListTag facadeList = new ListTag();
        for (Direction direction : Direction.values()) {
            ItemStack facade = this.getFacade(direction);
            if (facade.isEmpty()) {
                continue;
            }

            CompoundTag facadeEntry = new CompoundTag();
            facadeEntry.putInt(NBT_FACADE_SIDE, direction.ordinal());

            CompoundTag stackTag = new CompoundTag();
            facade.save(provider, stackTag);
            facadeEntry.put(NBT_FACADE_STACK, stackTag);

            facadeList.add(facadeEntry);
        }

        if (!facadeList.isEmpty()) {
            nbt.put(NBT_FACADE_SIDES, facadeList);
        }

        return nbt;
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK) {
            return null;
        }
        return super.getCapability(cap, side);
    }


    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        this.blackList.clear();
        this.clearFacadesInternal();

        if (nbtTagCompound.contains(NBT_BLACKLIST)) {
            final CompoundTag tagList = nbtTagCompound.getCompound(NBT_BLACKLIST);
            final int size = tagList.getInt("size");
            for (int i = 0; i < size; i++) {
                this.blackList.add(Direction.values()[tagList.getInt(String.valueOf(i))]);
            }
        }

        if (nbtTagCompound.contains(NBT_FACADE_SIDES, 9)) {
            ListTag facadeList = nbtTagCompound.getList(NBT_FACADE_SIDES, 10);

            for (int i = 0; i < facadeList.size(); i++) {
                CompoundTag facadeEntry = facadeList.getCompound(i);
                int sideOrdinal = facadeEntry.getInt(NBT_FACADE_SIDE);

                if (sideOrdinal < 0 || sideOrdinal >= Direction.values().length) {
                    continue;
                }

                if (!facadeEntry.contains(NBT_FACADE_STACK, 10)) {
                    continue;
                }

                ItemStack facadeStack = ItemStack.parseOptional(provider, facadeEntry.getCompound(NBT_FACADE_STACK));
                this.facadeStacks[sideOrdinal] = this.normalizeFacadeStack(facadeStack);
            }

            this.updateLegacyFacadeSnapshot();
        } else if (nbtTagCompound.contains(NBT_LEGACY_STACK_FACADE, 10)) {

            ItemStack legacyFacade = ItemStack.parseOptional(provider, nbtTagCompound.getCompound(NBT_LEGACY_STACK_FACADE));
            this.applyLegacyFacadePayload(legacyFacade);
        } else {
            this.stackFacade = ItemStack.EMPTY;
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        if (!this.getWorld().isClientSide) {
            new PacketUpdateFieldTile(this, "facadeStacks", this.getFacadeStacksForSync());
            this.updateConnectivity();
        }
    }

    public List<ItemStack> getAuxDrops(int fortune) {
        return Collections.emptyList();
    }

    public SoundType getBlockSound(Entity entity) {
        return SoundType.WOOL;
    }

    public AABB getVisualBoundingBox() {
        return super.getVisualBoundingBox();
    }

    private void addFacadeAabb(List<AABB> boxes, Direction side) {
        switch (side) {
            case DOWN -> boxes.add(new AABB(0.0D, 0.0D, 0.0D, 1.0D, FACADE_AABB_THICKNESS, 1.0D));
            case UP -> boxes.add(new AABB(0.0D, 1.0D - FACADE_AABB_THICKNESS, 0.0D, 1.0D, 1.0D, 1.0D));
            case NORTH -> boxes.add(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, FACADE_AABB_THICKNESS));
            case SOUTH -> boxes.add(new AABB(0.0D, 0.0D, 1.0D - FACADE_AABB_THICKNESS, 1.0D, 1.0D, 1.0D));
            case WEST -> boxes.add(new AABB(0.0D, 0.0D, 0.0D, FACADE_AABB_THICKNESS, 1.0D, 1.0D));
            case EAST -> boxes.add(new AABB(1.0D - FACADE_AABB_THICKNESS, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D));
        }
    }

    @Override
    public List<AABB> getAabbs(boolean forCollision) {
        float th = this.cableItem.getThickness();
        float sp = (1.0F - th) / 2.0F;

        List<AABB> ret = new ArrayList<>();

        ret.add(new AABB(
                sp, sp, sp,
                sp + th, sp + th, sp + th
        ));

        for (Direction rawFacing : Direction.values()) {
            boolean hasConnection = (this.connectivity & (1 << rawFacing.ordinal())) != 0;
            if (!hasConnection) {
                continue;
            }

            Direction facing = remapConnectivityDirection(rawFacing);

            float xS = sp;
            float yS = sp;
            float zS = sp;
            float xE = sp + th;
            float yE = sp + th;
            float zE = sp + th;

            switch (facing) {
                case DOWN -> {
                    yS = 0.0F;
                    yE = sp;
                }
                case UP -> {
                    yS = sp + th;
                    yE = 1.0F;
                }
                case NORTH -> {
                    zS = 0.0F;
                    zE = sp;
                }
                case SOUTH -> {
                    zS = sp + th;
                    zE = 1.0F;
                }
                case WEST -> {
                    xS = 0.0F;
                    xE = sp;
                }
                case EAST -> {
                    xS = sp + th;
                    xE = 1.0F;
                }
            }

            ret.add(new AABB(xS, yS, zS, xE, yE, zE));
        }


        if (this.hasAnyFacade()) {
            for (Direction direction : Direction.values()) {
                if (this.hasFacade(direction)) {
                    this.addFacadeAabb(ret, direction);
                }
            }
        }

        return ret;
    }

    private Direction remapConnectivityDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case WEST -> Direction.UP;
            case EAST -> Direction.DOWN;
            case UP -> Direction.WEST;
            case DOWN -> Direction.EAST;
        };
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return null;
    }

    @Override
    public BlockTileEntity getBlock() {
        return null;
    }

    public void setConnectivity(final byte connectivity) {
        if (this.connectivity != connectivity) {
            this.connectivity = connectivity;
            new PacketUpdateFieldTile(this, "connectivity", this.connectivity);

            Direction[] directions = Direction.values();
            HashMap<Direction, Boolean> booleanMap = new HashMap<>();
            for (Direction facing : directions) {
                boolean hasConnection = (this.connectivity & (1 << facing.ordinal())) != 0;
                booleanMap.put(facing, hasConnection);
            }

            this.setBlockState(this.getBlockState()
                    .setValue(NORTH, booleanMap.get(Direction.SOUTH))
                    .setValue(SOUTH, booleanMap.get(Direction.NORTH))
                    .setValue(WEST, booleanMap.get(Direction.UP))
                    .setValue(EAST, booleanMap.get(Direction.DOWN))
                    .setValue(UP, booleanMap.get(Direction.WEST))
                    .setValue(DOWN, booleanMap.get(Direction.EAST)));

            this.getWorld().setBlock(this.worldPosition, getBlockState(), 3);
        }
    }

    @Override
    public boolean onSneakingActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (this.level == null || this.level.isClientSide) {
            return false;
        }

        final ItemStack stack = player.getItemInHand(hand);


        if (stack.isEmpty()) {
            if (this.clearAllFacades()) {
                return true;
            }
            return super.onSneakingActivated(player, hand, side, vec3);
        }


        if (stack.getItem() == IUItem.facadeItem.getItem()) {
            ItemStack facadeStack = this.extractFacadeFromFacadeItem(stack);
            if (!facadeStack.isEmpty()) {
                this.fillEmptyFacades(facadeStack);
                return true;
            }
            return false;
        }

        return super.onSneakingActivated(player, hand, side, vec3);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player player, final ContainerMenuBase<?> menu) {
        return new ScreenCable(getGuiContainer(player));
    }

    @Override
    public ContainerMenuCable getGuiContainer(final Player player) {
        return new ContainerMenuCable(player, this);
    }

    @Override
    public BlockState getBlockState() {
        if (this.blockState == null) {
            try {
                Direction[] directions = Direction.values();
                HashMap<Direction, Boolean> booleanMap = new HashMap<>();

                for (Direction facing : directions) {
                    boolean hasConnection = (this.connectivity & (1 << facing.ordinal())) != 0;
                    booleanMap.put(facing, hasConnection);
                }

                this.blockState = this.block.defaultBlockState()
                        .setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                        .setValue(this.block.facingProperty, this.getFacing())
                        .setValue(NORTH, booleanMap.get(Direction.SOUTH))
                        .setValue(SOUTH, booleanMap.get(Direction.NORTH))
                        .setValue(WEST, booleanMap.get(Direction.UP))
                        .setValue(EAST, booleanMap.get(Direction.DOWN))
                        .setValue(UP, booleanMap.get(Direction.WEST))
                        .setValue(DOWN, booleanMap.get(Direction.EAST));
            } catch (Exception e) {
                this.blockState = this.block.defaultBlockState();
            }
        }
        return this.blockState;
    }

    public void updateConnectivity() {
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        final ItemStack stack = player.getItemInHand(hand);

        if (!this.getWorld().isClientSide && stack.getItem() == IUItem.facadeItem.getItem()) {
            ItemStack facadeStack = this.extractFacadeFromFacadeItem(stack);
            if (!facadeStack.isEmpty()) {
                this.setFacade(side, facadeStack);
                return true;
            }
            return false;
        } else if (stack.getItem() == IUItem.connect_item.getItem()) {
            return super.onActivated(player, hand, side, vec3);
        }

        if (this instanceof ITransportConductor conductor) {
            boolean can = conductor.isInput() || conductor.isOutput();
            if (can) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                return false;
            }
        }

        return false;
    }


    public void rerenderCable(ItemStack stack) {
        if (stack != null && !stack.isEmpty()) {
            this.applyLegacyFacadePayload(stack);
        } else {
            this.clearFacadesInternal();
        }

        this.onFacadeStorageChanged();
    }

    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);

        if (name.equals("connectivity")) {
            try {
                this.connectivity = (byte) DecoderHandler.decode(is);

                Direction[] directions = Direction.values();
                HashMap<Direction, Boolean> booleanMap = new HashMap<>();
                for (Direction facing : directions) {
                    boolean hasConnection = (this.connectivity & (1 << facing.ordinal())) != 0;
                    booleanMap.put(facing, hasConnection);
                }

                this.setBlockState(this.block.defaultBlockState()
                        .setValue(this.block.typeProperty, this.block.typeProperty.getState(this.teBlock, this.active))
                        .setValue(this.block.facingProperty, this.getFacing())
                        .setValue(NORTH, booleanMap.get(Direction.SOUTH))
                        .setValue(SOUTH, booleanMap.get(Direction.NORTH))
                        .setValue(WEST, booleanMap.get(Direction.UP))
                        .setValue(EAST, booleanMap.get(Direction.DOWN))
                        .setValue(UP, booleanMap.get(Direction.WEST))
                        .setValue(DOWN, booleanMap.get(Direction.EAST)));

                this.getWorld().setBlock(this.worldPosition, super.getBlockState(), 3);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (name.equals("facadeStacks")) {
            try {
                List<ItemStack> syncStacks = (List<ItemStack>) DecoderHandler.decode(is);
                this.applyFacadePayload(syncStacks);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (name.equals("stackFacade")) {
            try {
                ItemStack legacyFacade = (ItemStack) DecoderHandler.decode(is);
                this.applyLegacyFacadePayload(legacyFacade);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, this.getFacadeStacksForSync());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            List<ItemStack> syncStacks = (List<ItemStack>) DecoderHandler.decode(customPacketBuffer);
            this.applyFacadePayload(syncStacks);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer buffer = super.writeContainerPacket();
        try {
            EncoderHandler.encode(buffer, this.blackList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            this.blackList = (List<Direction>) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasSpecialModel() {
        return true;
    }

    @Override
    public void updateTileServer(final Player player, final double value) {
        byte event1 = (byte) value;
        Direction facing1 = Direction.values()[event1];
        if (this.blackList.contains(facing1)) {
            this.blackList.remove(facing1);
        } else {
            this.blackList.add(facing1);
        }
        this.onNeighborChange(this.getWorld().getBlockState(pos.offset(facing1.getNormal())), pos.offset(facing1.getNormal()));

    }
}