package com.denfop.componets;

import com.denfop.api.energy.event.load.EnergyTileLoadEvent;
import com.denfop.api.energy.event.unload.EnergyTileUnLoadEvent;
import com.denfop.api.energy.interfaces.EnergySink;
import com.denfop.api.energy.interfaces.EnergySource;
import com.denfop.api.energy.interfaces.EnergyTile;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.energy.*;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryCharge;
import com.denfop.inventory.InventoryDischarge;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.IEnergyStorage;

import java.io.IOException;
import java.util.*;

public class Energy extends AbstractComponent {

    public final boolean fullEnergy;
    public final double defaultCapacity;
    public final BufferEnergy buffer;

    public boolean upgrade;

    public int defaultSinkTier;
    public int defaultSourceTier;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public List<Inventory> managedSlots = new ArrayList<>();
    public boolean multiSource;
    public int sourcePackets;
    public EnergyNetDelegate delegate;
    public boolean loaded;

    public boolean limit;
    public double limit_amount = 0;

    Map<BlockPos, IEnergyStorage> energyStorageMap = new HashMap<>();

    private ChunkPos chunkPos;


    public Energy(BlockEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public Energy(
            BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public Energy(
            BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);

        this.multiSource = false;
        this.sourcePackets = 1;
        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : sinkDirections;
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : sourceDirections;
        this.fullEnergy = fullEnergy;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
    }

    public Energy(
            BlockEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);

        this.multiSource = false;
        this.sourcePackets = 1;


        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : new HashSet<>(sinkDirections);
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : new HashSet<>(sourceDirections);
        this.fullEnergy = fullEnergy;
        ;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
    }

    public static Energy asBasicSink(BlockEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static Energy asBasicSink(BlockEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static Energy asBasicSink(BlockEntityInventory parent, double capacity, boolean meta) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), 14, 14, false);
    }

    public static Energy asBasicSource(BlockEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static Energy asBasicSource(BlockEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public void onNeighborChange(BlockState srcBlock, BlockPos srcPos) {
        super.onNeighborChange(srcBlock, srcPos);
    }

    @Override
    public void updateEntityServer() {
        if (this.delegate != null)
            this.delegate.limit_amount = limit_amount;
        if (!managedSlots.isEmpty()) {
            for (Inventory slot : managedSlots) {
                if (slot instanceof InventoryDischarge) {
                    InventoryDischarge discharge = (InventoryDischarge) slot;
                    if (!discharge.isEmpty()) {
                        if (discharge.get(0).getItem() == Items.REDSTONE) {
                            double energy = discharge.dischargeWithRedstone(buffer.capacity, this.getFreeEnergy());
                            this.addEnergy(energy);
                        } else {
                            if (this.getFreeEnergy() > 0) {
                                double energy = discharge.discharge(this.getFreeEnergy(), false);
                                this.addEnergy(energy);
                            }
                        }
                    }
                } else if (slot instanceof InventoryCharge) {
                    InventoryCharge charge = (InventoryCharge) slot;
                    if (!charge.isEmpty()) {
                        double energy = charge.charge(this.buffer.storage);
                        this.useEnergy(energy);
                    }
                }
            }
        }
    }

    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            this.chunkPos = new ChunkPos(getParent().getBlockPos());
        }
        return chunkPos;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public Energy addManagedSlot(Inventory slot) {
        if (this.managedSlots == null) {
            this.managedSlots = new ArrayList<>(4);
        }

        this.managedSlots.add(slot);
        return this;
    }


    public void readFromNbt(CompoundTag nbt) {
        this.buffer.storage = nbt.getDouble("storage");
        this.limit_amount = nbt.getDouble("limit_amount");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.buffer.storage);
        ret.putDouble("limit_amount", this.limit_amount);

        return ret;
    }

    public void onLoaded() {


        assert this.delegate == null;

        if (!this.parent.getLevel().isClientSide) {
            if (!(this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty())) {
                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
            }
            this.loaded = true;
        }

    }

    public int getComparatorValue() {
        return Math.min((int) (this.buffer.storage * 15.0 / this.buffer.capacity), 15);
    }

    public void setLimit(final boolean limit) {
        this.limit = limit;
        if (delegate != null)
            this.delegate.limit = limit;
    }

    public void createDelegate() {
        if (this.delegate == null) {
            if (!this.multiSource) {
                if (this.sinkDirections.isEmpty()) {
                    this.delegate = new EnergyNetDelegateSource(this);
                } else if (this.sourceDirections.isEmpty()) {
                    this.delegate = new EnergyNetDelegateSink(this);
                } else {

                    this.delegate = new EnergyNetDelegateDual(this);
                }
            } else {
                this.delegate = new EnergyNetDelegateMultiDual(this);
            }


        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
            this.delegate = null;
        }
        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeDouble(this.limit_amount);
        buffer.writeInt(this.buffer.sourceTier);
        buffer.writeInt(this.buffer.sinkTier);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeDouble(this.limit_amount);
        buffer.writeInt(this.buffer.sourceTier);
        buffer.writeInt(this.buffer.sinkTier);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.buffer.capacity = is.readDouble();
        this.buffer.storage = is.readDouble();
        limit_amount = is.readDouble();
        this.buffer.sourceTier = is.readInt();
        this.buffer.sinkTier = is.readInt();

    }

    public double getDefaultCapacity() {
        return defaultCapacity;
    }

    public double getCapacity() {
        return this.buffer.capacity;
    }

    public void setCapacity(double capacity) {
        this.buffer.capacity = capacity;
        this.buffer.storage = Math.min(this.buffer.capacity, this.buffer.storage);
    }

    public void addCapacity(double capacity) {
        this.buffer.capacity += capacity;
    }

    public double getEnergy() {
        return this.buffer.storage;
    }

    public double getFreeEnergy() {
        return Math.max(0.0D, this.buffer.capacity - this.buffer.storage);
    }

    public double getFillRatio() {
        if (this.buffer.storage > this.buffer.capacity) {
            this.buffer.storage = this.buffer.capacity;
        }
        return this.buffer.storage / this.buffer.capacity;
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.buffer.capacity - this.buffer.storage, amount);
        this.buffer.storage += amount;
        this.buffer.storage = Math.min(this.buffer.storage, this.buffer.capacity);
        return amount;
    }

    public void forceAddEnergy(double amount) {
        this.buffer.storage += amount;
    }

    public boolean canUseEnergy(double amount) {
        return this.buffer.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.buffer.storage >= amount) {
            this.buffer.storage -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.buffer.storage) - amount);
        if (!simulate) {
            this.buffer.storage -= ret;
        }
        return ret;
    }

    public void setOverclockRates(InventoryUpgrade invSlotUpgrade) {
        if (this.getDelegate() instanceof EnergySink) {
            int tier = invSlotUpgrade.getTier(this.defaultSinkTier);
            this.setSinkTier(tier);
            for (Inventory slot : this.managedSlots) {
                if (slot instanceof InventoryDischarge) {
                    InventoryDischarge discharge = (InventoryDischarge) slot;
                    discharge.setTier(tier);
                }
            }
        }
        if (this.getDelegate() instanceof EnergySource) {
            int tier = invSlotUpgrade.getTier(this.defaultSourceTier);
            this.setSourceTier(tier);
            for (Inventory slot : this.managedSlots) {
                if (slot instanceof InventoryCharge) {
                    InventoryCharge discharge = (InventoryCharge) slot;
                    discharge.setTier(tier);
                }
            }
        }
        this.setCapacity(invSlotUpgrade.getEnergyStorage(
                this.defaultCapacity
        ));
    }

    public int getSinkTier() {
        return this.buffer.sinkTier;
    }

    public void setSinkTier(int tier) {
        this.buffer.sinkTier = tier;
        for (Inventory slot : this.managedSlots) {
            if (slot instanceof InventoryDischarge) {
                InventoryDischarge discharge = (InventoryDischarge) slot;
                discharge.setTier(tier);
            }
        }
    }

    public int getSourceTier() {
        return this.buffer.sourceTier;
    }

    public void setSourceTier(int tier) {
        for (Inventory slot : this.managedSlots) {
            if (slot instanceof InventoryCharge) {
                InventoryCharge discharge = (InventoryCharge) slot;
                discharge.setTier(tier);
            }
        }
        buffer.sourceTier = tier;
    }

    public void setEnabled(boolean enabled) {
        if (delegate != null)
            delegate.receivingDisabled = delegate.sendingSidabled = !enabled;
    }

    public void setReceivingEnabled(boolean enabled) {
        if (delegate != null)
            delegate.receivingDisabled = !enabled;
    }

    public void setSendingEnabled(boolean enabled) {
        if (delegate != null)
            delegate.sendingSidabled = !enabled;
    }

    public Energy setMultiSource(boolean multiSource) {
        this.multiSource = multiSource;
        if (!multiSource) {
            this.sourcePackets = 1;
        }

        return this;
    }

    public void setPacketOutput(int number) {
        if (this.multiSource) {
            this.sourcePackets = number;
        }

    }

    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
        }

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }
        if (this.delegate != null) {
            this.delegate.sourceDirections = sourceDirections;
            this.delegate.sinkDirections = sinkDirections;

            assert !this.parent.getLevel().isClientSide;
            delegate.energyConductorMap.clear();
            delegate.getValidReceivers().clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
        }


    }


    public void setDirections(List<Direction> sinkDirections, List<Direction> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
        }

        this.sinkDirections = new HashSet<>(sinkDirections);
        this.sourceDirections = new HashSet<>(sourceDirections);
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }
        if (this.delegate != null) {

            this.delegate.sourceDirections = new HashSet<>(sourceDirections);
            this.delegate.sinkDirections = new HashSet<>(sinkDirections);
            delegate.energyConductorMap.clear();
            delegate.getValidReceivers().clear();
            assert !this.parent.getLevel().isClientSide;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
        }


    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public EnergyTile getDelegate() {
        return this.delegate;
    }


}

