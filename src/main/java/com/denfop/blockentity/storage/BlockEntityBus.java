package com.denfop.blockentity.storage;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.IAcceptor;
import com.denfop.api.otherenergies.common.IEmitter;
import com.denfop.api.otherenergies.common.ITile;
import com.denfop.api.storage.*;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.ComponentStorageEnergy;
import com.denfop.componets.Redstone;
import com.denfop.containermenu.ContainerBus;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.SlotInfo;
import com.denfop.network.DecoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.screen.ScreenBus;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.denfop.blocks.BlockTileEntity.*;

public class BlockEntityBus extends BlockEntityInventory implements IUpdatableTileEvent {

    private final ComponentBaseEnergy energy;
    private final boolean isImport;
    private final boolean isFluid;
    private final Redstone redstone;
    public SlotInfo listSlot;
    public TypeRedstone redstoneMode = TypeRedstone.NONE;
    public TypeComponent componentMode = TypeComponent.USE;
    public TypeSlot slotMode = TypeSlot.QUEUE;
    public Map<Integer, Integer> indexesMap = new HashMap<>();
    public byte connectivity;
    BlockEntity blockEntityDown;

    public BlockEntityBus(MultiBlockEntity block, boolean isFluid, boolean isImport, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.isImport = isImport;
        this.isFluid = isFluid;
        this.listSlot = new SlotInfo(this, 9, isFluid);
        this.energy = this.addComponent(ComponentStorageEnergy.asBasicSink(EnergyType.STORAGE, this, 0));
        this.redstone = this.addComponent(new Redstone(this));
    }
    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }
    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer buf = super.writeContainerPacket();

        buf.writeInt(redstoneMode == null ? -1 : redstoneMode.ordinal());
        buf.writeInt(componentMode == null ? -1 : componentMode.ordinal());
        buf.writeInt(slotMode == null ? -1 : slotMode.ordinal());

        buf.writeInt(indexesMap.size());
        for (Map.Entry<Integer, Integer> e : indexesMap.entrySet()) {
            buf.writeInt(e.getKey());
            buf.writeInt(e.getValue());
        }

        return buf;
    }

    @Override
    public void readContainerPacket(CustomPacketBuffer buf) {
        super.readContainerPacket(buf);

        int r = buf.readInt();
        redstoneMode = (r >= 0 && r < TypeRedstone.values().length) ? TypeRedstone.values()[r] : TypeRedstone.NONE;

        int c = buf.readInt();
        componentMode = (c >= 0 && c < TypeComponent.values().length) ? TypeComponent.values()[c] : TypeComponent.USE;

        int s = buf.readInt();
        slotMode = (s >= 0 && s < TypeSlot.values().length) ? TypeSlot.values()[s] : TypeSlot.QUEUE;

        indexesMap.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int k = buf.readInt();
            int v = buf.readInt();
            indexesMap.put(k, v);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {
        nbt = super.writeToNBT(nbt);

        if (redstoneMode != null) {
            nbt.putInt("redstoneMode", redstoneMode.ordinal());
        }

        if (componentMode != null) {
            nbt.putInt("componentMode", componentMode.ordinal());
        }
        if (slotMode != null) {
            nbt.putInt("slotMode", slotMode.ordinal());
        }
        CompoundTag mapTag = new CompoundTag();

        for (Map.Entry<Integer, Integer> entry : indexesMap.entrySet()) {
            mapTag.putInt(String.valueOf(entry.getKey()), entry.getValue());
        }

        nbt.put("indexesMap", mapTag);

        return nbt;
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        super.readFromNBT(nbt);

        if (nbt.contains("redstoneMode")) {
            redstoneMode = TypeRedstone.values()[nbt.getInt("redstoneMode")];
        }
        if (nbt.contains("slotMode")) {
            slotMode = TypeSlot.values()[nbt.getInt("slotMode")];
        }
        if (nbt.contains("componentMode")) {
            componentMode = TypeComponent.values()[nbt.getInt("componentMode")];
        }

        indexesMap.clear();

        if (nbt.contains("indexesMap")) {
            CompoundTag mapTag = nbt.getCompound("indexesMap");

            for (String key : mapTag.getAllKeys()) {
                int k = Integer.parseInt(key);
                int v = mapTag.getInt(key);
                indexesMap.put(k, v);
            }
        }
    }

    public TypeSlot getSlotMode() {
        return slotMode;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.updateConnectivity();
        }
    }

    public void updateConnectivity() {
        byte newConnectivity = 0;
        Direction[] var4 = Direction.values();

        for (Direction dir : var4) {
            newConnectivity = (byte) (newConnectivity << 1);
            ITile tile = energy.delegate.energyConductorMap.get(dir);
            if ((energy.delegate instanceof IEmitter && tile instanceof IAcceptor && ((IAcceptor) tile).acceptsFrom(
                    (IEmitter) energy.delegate,
                    dir.getOpposite()
            )) || (energy.delegate instanceof IAcceptor && tile instanceof IEmitter && ((IEmitter) tile).emitsTo(
                    (IAcceptor) energy.delegate,
                    dir.getOpposite()
            ))) {
                newConnectivity = (byte) (newConnectivity + 1);
            }


        }
        setConnectivity(newConnectivity);

    }

    public void setConnectivity(final byte connectivity) {
        if (this.connectivity != connectivity) {
            this.connectivity = connectivity;
            new PacketUpdateFieldTile(this, "connectivity", this.connectivity);

            Direction[] directions = Direction.values();
            Map<Direction, Boolean> booleanMap = new HashMap<>();
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
    public BlockState getBlockState() {
        if (this.blockState == null) {
            try {
                Direction[] directions = Direction.values();
                Map<Direction, Boolean> booleanMap = new HashMap<>();

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

    public boolean isFluid() {
        return isFluid;
    }

    public boolean isImport() {
        return isImport;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        addStorageSystemConsumptionInformation(tooltip);
    }


    private void addStorageSystemConsumptionInformation(final List<String> tooltip) {
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.title"));
        tooltip.add("§7" + Localization.translate("iu.storage_system.tooltip.consumption") + ": §b" +
                formatStorageSystemPower(this.getRequiredPower()) + " §7" +
                Localization.translate("iu.storage_system.tooltip.energy_per_tick"));
        tooltip.add("§8" + Localization.translate("iu.storage_system.tooltip.description"));
    }

    private static String formatStorageSystemPower(final double value) {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    public double getRequiredPower() {
        return 1;
    }

    public SlotInfo getListSlot() {
        return listSlot;
    }

    public TypeRedstone getRedstone() {
        return redstoneMode;
    }

    public int getRedstoneSignal() {
        return redstone.getRedstoneInput();
    }

    public TypeComponent getComponent() {
        return componentMode;
    }

    public int getIndexFromSlot(int slot) {
        return indexesMap.getOrDefault(slot, -1);
    }

    public void updateField(String name, CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("connectivity")) {
            try {
                this.connectivity = (byte) DecoderHandler.decode(is);
                this.connectivity = (byte) DecoderHandler.decode(is);

                Direction[] directions = Direction.values();
                Map<Direction, Boolean> booleanMap = new HashMap<>();
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

    }

    @Override
    public ContainerBus getGuiContainer(final Player var1) {
        return new ContainerBus(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenBus((ContainerBus) var2);
    }

    public void setStorageNetwork(StorageNetwork network) {

    }

    @Override
    public void loadBeforeFirstUpdate() {
        super.loadBeforeFirstUpdate();
        blockEntityDown = this.getLevel().getBlockEntity(this.pos.offset(getFacing().getOpposite().getNormal()));
        if (energy.delegate != null)
            this.updateConnectivity();
    }

    @Override
    public void onNeighborChange(BlockState neighbor, BlockPos neighborPos) {
        super.onNeighborChange(neighbor, neighborPos);
        if (this.pos.offset(getFacing().getOpposite().getNormal()).equals(neighborPos)) {
            blockEntityDown = this.getLevel().getBlockEntity(neighborPos);

        }
        if (energy.delegate != null)
            this.updateConnectivity();
    }


    public TypeStack getTypeStack() {
        return isFluid ? TypeStack.FLUID : TypeStack.ITEM;
    }


    public List<ItemStack> getStacks() {
        return listSlot.getListBlack();
    }


    public List<FluidStack> getFluidStacks() {
        return listSlot.getFluidStackList();
    }

    public Direction getDirection() {
        return getFacing().getOpposite();
    }

    public BlockEntity getBlockEntityNeighbor() {
        return blockEntityDown;
    }


    public EnumTypeSlots getTypeSlots() {
        return !isImport ? EnumTypeSlots.WHITELIST : !listSlot.isEmpty() ? EnumTypeSlots.WHITELIST : EnumTypeSlots.BLACKLIST;
    }

    private Direction remapConnectivityDirection(Direction direction) {
        if (direction == this.getFacing())
            return switch (direction) {
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case WEST -> Direction.EAST;
                case EAST -> Direction.WEST;
                case UP -> Direction.DOWN;
                case DOWN -> Direction.UP;
            };
        else
            return switch (direction) {
                case NORTH -> Direction.SOUTH;
                case SOUTH -> Direction.NORTH;
                case WEST -> Direction.UP;
                case EAST -> Direction.DOWN;
                case UP -> Direction.WEST;
                case DOWN -> Direction.EAST;
            };
    }

    public List<AABB> getAabbs(boolean forCollision) {


        float th = 0.25f;
        float sp = (1.0F - th) / 2.0F;

        List<AABB> ret = new ArrayList<>();

        ret.add(new AABB(
                sp, sp, sp,
                sp + th, sp + th, sp + th
        ));

        for (Direction rawFacing : Direction.values()) {
            boolean hasConnection = (this.connectivity & (1 << rawFacing.ordinal())) != 0;
            if (rawFacing != this.getFacing())
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


        return ret;
    }

    @Override
    public void updateTileServer(Player var1, double var2) {
        if (this.isImport) {
            if (var2 == 0) {
                this.redstoneMode = TypeRedstone.values()[(this.redstoneMode.ordinal() + 1) % TypeRedstone.values().length];
            } else {
                this.componentMode = TypeComponent.values()[(this.componentMode.ordinal() + 1) % TypeComponent.values().length];
            }
        } else {
            if (var2 == 0) {
                this.redstoneMode = TypeRedstone.values()[(this.redstoneMode.ordinal() + 1) % TypeRedstone.values().length];
            } else {
                this.slotMode = TypeSlot.values()[(this.slotMode.ordinal() + 1) % TypeSlot.values().length];
            }
        }
    }
}
