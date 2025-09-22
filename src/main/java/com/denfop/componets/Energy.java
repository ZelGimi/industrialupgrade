package com.denfop.componets;

import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.IDual;
import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergyEmitter;
import com.denfop.api.energy.IEnergySink;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.IMultiDual;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.*;
import com.denfop.invslot.InventoryDischarge;
import com.denfop.invslot.Inventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Energy extends AbstractComponent {

    public final boolean fullEnergy;
    public final double defaultCapacity;
    public double tick;
    public boolean upgrade;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;

    public int defaultSinkTier;
    public int defaultSourceTier;
    public Set<EnumFacing> sinkDirections;
    public Set<EnumFacing> sourceDirections;
    public List<Inventory> managedSlots = new ArrayList<>();
    public boolean multiSource;
    public int sourcePackets;
    public Energy.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean limit;
    public double limit_amount = 0;
    protected double pastEnergy;
    protected double perenergy;
    protected double pastEnergy1;
    protected double perenergy1;
    Map<BlockPos, IEnergyStorage> energyStorageMap = new HashMap<>();
    Map<EnumFacing, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    private ChunkPos chunkPos;
    private long id;

    public Energy(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public Energy(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public Energy(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);

        this.multiSource = false;
        this.sourcePackets = 1;
        this.capacity = capacity;

        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : sinkDirections;
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
    }

    public Energy(
            TileEntityInventory parent,
            double capacity,
            List<EnumFacing> sinkDirections,
            List<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);

        this.multiSource = false;
        this.sourcePackets = 1;
        this.capacity = capacity;

        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : new HashSet<>(sinkDirections);
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : new HashSet<>(sourceDirections);
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity, boolean meta) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), 14, 14, false);
    }

    public static Energy asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static Energy asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public void onNeighborChange(final Block srcBlock, final BlockPos srcPos) {

    }

    @Override
    public void updateEntityServer() {

        if (!managedSlots.isEmpty()) {
            for (Inventory slot : managedSlots) {
                if (slot instanceof InventoryDischarge) {
                    InventoryDischarge discharge = (InventoryDischarge) slot;
                    if (!discharge.isEmpty()) {
                        if (discharge.get().getItem() == Items.REDSTONE) {
                            double energy = discharge.dischargeWithRedstone(this.capacity, this.getFreeEnergy());
                            this.addEnergy(energy);
                        } else {
                            double energy = discharge.discharge(this.getFreeEnergy(), false);
                            this.addEnergy(energy);
                        }
                    }
                } else if (slot instanceof InventoryCharge) {
                    InventoryCharge charge = (InventoryCharge) slot;
                    if (!charge.isEmpty()) {
                        double energy = charge.charge(this.storage);
                        this.useEnergy(energy);
                    }
                }
            }
        }
    }

    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            this.chunkPos = new ChunkPos(getParent().getPos().getX() >> 4, getParent().getPos().getZ() >> 4);
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

    public Map<EnumFacing, IEnergyTile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(IEnergyTile tile, final EnumFacing facing1) {
        if (!this.parent.getWorld().isRemote) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.limit_amount = nbt.getDouble("limit_amount");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setDouble("limit_amount", this.limit_amount);

        return ret;
    }

    public void onLoaded() {


        assert this.delegate == null;

        if (!this.parent.getWorld().isRemote) {
            if (!(this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty())) {
                this.createDelegate();
                this.energyConductorMap.clear();
                validReceivers.clear();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
            }
            this.loaded = true;
        }

    }

    public int getComparatorValue() {
        return Math.min((int) (this.storage * 15.0 / this.capacity), 15);
    }

    public void setLimit(final boolean limit) {
        this.limit = limit;
    }

    public void createDelegate() {
        if (this.delegate == null) {
            if (!this.multiSource) {
                if (this.sinkDirections.isEmpty()) {
                    this.delegate = new Energy.EnergyNetDelegateSource();
                } else if (this.sourceDirections.isEmpty()) {
                    this.delegate = new Energy.EnergyNetDelegateSink();
                } else {

                    this.delegate = new Energy.EnergyNetDelegateDual();
                }
            } else {
                this.delegate = new Energy.EnergyNetDelegateMultiDual();
            }


            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
            this.delegate = null;
        }
        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.limit_amount);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.limit_amount);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        limit_amount = is.readDouble();

    }

    public double getDefaultCapacity() {
        return defaultCapacity;
    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
        this.storage = Math.min(this.capacity, this.storage);
    }

    public void addCapacity(double capacity) {
        this.capacity += capacity;
    }

    public double getEnergy() {
        return this.storage;
    }

    public double getFreeEnergy() {
        return Math.max(0.0D, this.capacity - this.storage);
    }

    public double getFillRatio() {
        if (this.storage > this.capacity) {
            this.storage = this.capacity;
        }
        return this.storage / this.capacity;
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.capacity - this.storage, amount);
        this.storage += amount;
        this.storage = Math.min(this.storage, this.capacity);
        return amount;
    }

    public void forceAddEnergy(double amount) {
        this.storage += amount;
    }

    public boolean canUseEnergy(double amount) {
        return this.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.storage >= amount) {
            this.storage -= amount;
            return true;
        } else {
            return false;
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.storage) - amount);
        if (!simulate) {
            this.storage -= ret;
        }
        return ret;
    }

    public void setOverclockRates(InventoryUpgrade invSlotUpgrade) {
        if (this.getDelegate() instanceof IEnergySink) {
            int tier = invSlotUpgrade.getTier(this.defaultSinkTier);
            this.setSinkTier(tier);
            for (Inventory slot : this.managedSlots) {
                if (slot instanceof InventoryDischarge) {
                    InventoryDischarge discharge = (InventoryDischarge) slot;
                    discharge.setTier(tier);
                }
            }
        }
        if (this.getDelegate() instanceof IEnergySource) {
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
        return this.sinkTier;
    }

    public void setSinkTier(int tier) {
        this.sinkTier = tier;
        for (Inventory slot : this.managedSlots) {
            if (slot instanceof InventoryDischarge) {
                InventoryDischarge discharge = (InventoryDischarge) slot;
                discharge.setTier(tier);
            }
        }
    }

    public int getSourceTier() {
        return this.sourceTier;
    }

    public void setSourceTier(int tier) {
        for (Inventory slot : this.managedSlots) {
            if (slot instanceof InventoryCharge) {
                InventoryCharge discharge = (InventoryCharge) slot;
                discharge.setTier(tier);
            }
        }
        this.sourceTier = tier;
    }

    public void setEnabled(boolean enabled) {
        this.receivingDisabled = this.sendingSidabled = !enabled;
    }

    public void setReceivingEnabled(boolean enabled) {
        this.receivingDisabled = !enabled;
    }

    public void setSendingEnabled(boolean enabled) {
        this.sendingSidabled = !enabled;
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

    public void setDirections(Set<EnumFacing> sinkDirections, Set<EnumFacing> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
        }

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }
        if (this.delegate != null) {


            assert !this.parent.getWorld().isRemote;
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
        }


    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setDirections(List<EnumFacing> sinkDirections, List<EnumFacing> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
        }

        this.sinkDirections = new HashSet<>(sinkDirections);
        this.sourceDirections = new HashSet<>(sourceDirections);
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.energyConductorMap.clear();
            validReceivers.clear();
            this.createDelegate();
        }
        if (this.delegate != null) {


            this.energyConductorMap.clear();
            validReceivers.clear();
            assert !this.parent.getWorld().isRemote;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
        }


    }

    public Set<EnumFacing> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<EnumFacing> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public IEnergyTile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        if (!limit) {
            return Math.min(this.storage, EnergyNetGlobal.instance.getPowerFromTier(this.sourceTier));
        } else {

            return Math.min(this.storage, this.limit_amount);
        }
    }


    private abstract static class EnergyNetDelegate extends TileEntity implements IEnergyTile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateDual extends Energy.EnergyNetDelegate implements IDual {


        int hashCodeSource;
        boolean hasHashCode = false;
        List<Integer> energyTicks = new LinkedList<>();
        private int hashCode;

        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }

        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }



        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return Energy.this.parent.getPos();
        }

        @Override
        public void AddTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<EnumFacing, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        @Override
        public TileEntity getTileEntity() {
            return Energy.this.parent;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled && Energy.this.storage < Energy.this.capacity
                    ? Energy.this.capacity - Energy.this.storage
                    : 0.0D;
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled
                    ? Energy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage = Energy.this.storage + amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }


        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return !Energy.this.sendingSidabled;
        }


        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return !Energy.this.receivingDisabled;
        }


        @Override
        public double getPerEnergy1() {
            return Energy.this.perenergy1;
        }

        @Override
        public double getPastEnergy1() {
            return Energy.this.pastEnergy1;
        }

        @Override
        public void setPastEnergy1(final double pastEnergy) {
            Energy.this.pastEnergy1 = pastEnergy;
        }

        @Override
        public void addPerEnergy1(final double setEnergy) {
            Energy.this.perenergy1 += setEnergy;
        }


    }

    private class EnergyNetDelegateMultiDual extends Energy.EnergyNetDelegate implements IMultiDual {


        int hashCodeSource;
        boolean hasHashCode = false;
        List<Integer> energyTicks = new ArrayList<>();
        private int hashCode;

        private EnergyNetDelegateMultiDual() {
            super();
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }

        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }


        @Override
        public void AddTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<EnumFacing, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return Energy.this.parent.getPos();
        }

        @Override
        public TileEntity getTileEntity() {
            return Energy.this.parent;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled && Energy.this.storage < Energy.this.capacity
                    ? Energy.this.capacity - Energy.this.storage
                    : 0.0D;
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled
                    ? Energy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage = Energy.this.storage + amount;
        }

        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return !Energy.this.sendingSidabled;
        }


        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return !Energy.this.receivingDisabled;
        }


        @Override
        public double getPerEnergy1() {
            return Energy.this.perenergy1;
        }

        @Override
        public double getPastEnergy1() {
            return Energy.this.pastEnergy1;
        }

        @Override
        public void setPastEnergy1(final double pastEnergy) {
            Energy.this.pastEnergy1 = pastEnergy;
        }

        @Override
        public void addPerEnergy1(final double setEnergy) {
            Energy.this.perenergy1 += setEnergy;
        }


    }

    private class EnergyNetDelegateSink extends Energy.EnergyNetDelegate implements IEnergySink {

        boolean hasHashCode = false;
        int hashCodeSource;
        List<Integer> energyTicks = new LinkedList<>();
        private int hashCode;


        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }

        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }




        @Override
        public void AddTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<EnumFacing, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled ? Energy.this.capacity - Energy.this.storage : 0;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage += amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return true;
        }

        @Override
        public TileEntity getTileEntity() {
            return Energy.this.parent;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return Energy.this.parent.getPos();
        }

    }

    private class EnergyNetDelegateSource extends Energy.EnergyNetDelegate implements IEnergySource {


        int hashCodeSource;
        boolean hasHashCode = false;
        private int hashCode;

        private EnergyNetDelegateSource() {
            super();

        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }



        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        @Override
        public void AddTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final EnumFacing dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<EnumFacing, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return Energy.this.parent.getPos();
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
            for (EnumFacing facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }


        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled ? Energy.this.getSourceEnergy() : 0.0D;
        }


        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }

        @Override
        public TileEntity getTileEntity() {
            return this;
        }

    }


}
