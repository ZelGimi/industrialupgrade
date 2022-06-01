package com.denfop.componets;

import com.denfop.api.energy.IAdvEnergySink;
import com.denfop.api.energy.IAdvEnergySource;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IChargingSlot;
import ic2.api.energy.tile.IDischargingSlot;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class AdvEnergy extends TileEntityComponent {

    public final boolean fullEnergy;
    private final boolean meta;
    public double tick;
    public boolean upgrade;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;
    public Set<EnumFacing> sinkDirections;
    public Set<EnumFacing> sourceDirections;
    public List<InvSlot> managedSlots;
    public boolean multiSource;
    public int sourcePackets;
    public AdvEnergy.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    protected double pastEnergy;
    protected double perenergy;

    public AdvEnergy(TileEntityBlock parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public AdvEnergy(TileEntityBlock parent, double capacity, boolean meta) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1, 1, false, meta);
    }

    public AdvEnergy(
            TileEntityBlock parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public AdvEnergy(
            TileEntityBlock parent,
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
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.meta = false;
    }

    public AdvEnergy(
            TileEntityBlock parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy,
            boolean meta
    ) {
        super(parent);

        this.multiSource = false;
        this.sourcePackets = 1;
        this.capacity = capacity;
        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.meta = meta;
    }


    public static AdvEnergy asBasicSink(TileEntityBlock parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static AdvEnergy asBasicSink(TileEntityBlock parent, double capacity, int tier) {
        return new AdvEnergy(parent, capacity, Util.allFacings, Collections.emptySet(), tier);
    }

    public static AdvEnergy asBasicSink(TileEntityBlock parent, double capacity, boolean meta) {
        return new AdvEnergy(parent, capacity, Util.allFacings, Collections.emptySet(), 14, 14, false, meta);
    }

    public static AdvEnergy asBasicSource(TileEntityBlock parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static AdvEnergy asBasicSource(TileEntityBlock parent, double capacity, int tier) {
        return new AdvEnergy(parent, capacity, Collections.emptySet(), Util.allFacings, tier);
    }

    public AdvEnergy addManagedSlot(InvSlot slot) {
        if (!(slot instanceof IChargingSlot) && !(slot instanceof IDischargingSlot)) {
            throw new IllegalArgumentException("No charge/discharge slot.");
        } else {
            if (this.managedSlots == null) {
                this.managedSlots = new ArrayList<>(4);
            }

            this.managedSlots.add(slot);
            return this;
        }
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        return ret;
    }

    public void onLoaded() {
        assert this.delegate == null;

        if (!this.parent.getWorld().isRemote) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {

            } else {


                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.delegate));
            }

            this.loaded = true;
        }

    }

    private void createDelegate() {
        if (this.delegate != null) {
            throw new IllegalStateException();
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();
            if (!this.meta) {
                if (this.sinkDirections.isEmpty()) {
                    this.delegate = new AdvEnergy.EnergyNetDelegateSource();
                } else if (this.sourceDirections.isEmpty()) {
                    this.delegate = new AdvEnergy.EnergyNetDelegateSink();
                } else {
                    this.delegate = new AdvEnergy.EnergyNetDelegateDual();
                }
            } else {
                if (this.sinkDirections.isEmpty()) {
                    this.delegate = new AdvEnergy.EnergyMetaNetDelegateSource();
                } else if (this.sourceDirections.isEmpty()) {
                    this.delegate = new AdvEnergy.EnergyMetaNetDelegateSink();
                }
            }

            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.delegate));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {

    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
    }

    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote && this.managedSlots != null;
    }

    public void onWorldTick() {

        for (final InvSlot slot : this.managedSlots) {
            if (slot instanceof IChargingSlot) {
                if (this.storage > 0.0D) {
                    this.storage -= ((IChargingSlot) slot).charge(this.storage);
                }
            } else if (slot instanceof IDischargingSlot) {
                double space = this.capacity - this.storage;
                if (space > 0.0D) {
                    this.storage += ((IDischargingSlot) slot).discharge(space, false);
                }
            }
        }

    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getEnergy() {
        return this.storage;
    }

    public double getFreeEnergy() {
        return Math.max(0.0D, this.capacity - this.storage);
    }

    public double getFillRatio() {
        return this.storage / this.capacity;
    }

    public int getComparatorValue() {
        return Math.min((int) (this.storage * 15.0D / this.capacity), 15);
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.capacity - this.storage, amount);
        this.storage += amount;
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

    public int getSinkTier() {
        return this.sinkTier;
    }

    public void setSinkTier(int tier) {
        this.sinkTier = tier;
    }

    public int getSourceTier() {
        return this.sourceTier;
    }

    public void setSourceTier(int tier) {
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

    public boolean isMultiSource() {
        return this.multiSource;
    }

    public AdvEnergy setMultiSource(boolean multiSource) {
        this.multiSource = multiSource;
        if (!multiSource) {
            this.sourcePackets = 1;
        }

        return this;
    }

    public int getPacketOutput() {
        return this.sourcePackets;
    }

    public void setPacketOutput(int number) {
        if (this.multiSource) {
            this.sourcePackets = number;
        }

    }

    public void setDirections(Set<EnumFacing> sinkDirections, Set<EnumFacing> sourceDirections) {

        if (this.delegate != null) {

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this.delegate));
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

            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.delegate));
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
        if (this.fullEnergy) {
            return this.storage >= EnergyNet.instance.getPowerFromTier(this.sourceTier) ? this.storage : 0.0D;
        } else {
            return this.storage;
        }
    }

    private int getPacketCount() {
        return this.fullEnergy ? Math.min(
                this.sourcePackets,
                (int) Math.floor(this.storage / EnergyNet.instance.getPowerFromTier(this.sourceTier))
        ) : this.sourcePackets;
    }

    private abstract static class EnergyNetDelegate extends TileEntity implements IEnergyTile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateDual extends AdvEnergy.EnergyNetDelegate implements IAdvEnergySink, IAdvEnergySource {


        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {
            return AdvEnergy.this.sinkDirections.contains(dir);
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
            return AdvEnergy.this.sourceDirections.contains(dir);
        }

        public double getDemandedEnergy() {
            return !AdvEnergy.this.receivingDisabled && !AdvEnergy.this.sinkDirections.isEmpty() && AdvEnergy.this.storage < AdvEnergy.this.capacity
                    ? AdvEnergy.this.capacity - AdvEnergy.this.storage
                    : 0.0D;
        }

        public double getOfferedEnergy() {
            return !AdvEnergy.this.sendingSidabled && !AdvEnergy.this.sourceDirections.isEmpty()
                    ? AdvEnergy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return AdvEnergy.this.sinkTier;
        }

        public int getSourceTier() {
            return AdvEnergy.this.sourceTier;
        }

        public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
            AdvEnergy.this.storage = AdvEnergy.this.storage + amount;
            return 0.0D;
        }

        public void drawEnergy(double amount) {
            assert amount <= AdvEnergy.this.storage;

            AdvEnergy.this.storage = AdvEnergy.this.storage - amount;
        }


        @Override
        public double getPerEnergy() {
            return AdvEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return AdvEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            AdvEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            AdvEnergy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return !AdvEnergy.this.sendingSidabled;
        }


        @Override
        public void addTick(final double tick) {
            AdvEnergy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return AdvEnergy.this.tick;
        }

        @Override
        public boolean isSink() {
            return AdvEnergy.this.sendingSidabled;
        }

    }

    private class EnergyNetDelegateSink extends AdvEnergy.EnergyNetDelegate implements IAdvEnergySink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return AdvEnergy.this.sinkTier;
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing dir) {
            return AdvEnergy.this.sinkDirections.contains(dir);
        }

        public double getDemandedEnergy() {
            assert !AdvEnergy.this.sinkDirections.isEmpty();

            return !AdvEnergy.this.receivingDisabled && AdvEnergy.this.storage < AdvEnergy.this.capacity
                    ? AdvEnergy.this.capacity - AdvEnergy.this.storage
                    : 0.0D;
        }

        public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
            AdvEnergy.this.storage = AdvEnergy.this.storage + amount;
            return 0.0D;
        }

        @Override
        public double getPerEnergy() {
            return AdvEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return AdvEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            AdvEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            AdvEnergy.this.perenergy += setEnergy;
        }

        @Override
        public void addTick(final double tick) {
            AdvEnergy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return AdvEnergy.this.tick;
        }

        @Override
        public boolean isSink() {
            return true;
        }

    }

    private class EnergyNetDelegateSource extends AdvEnergy.EnergyNetDelegate implements IAdvEnergySource {


        private EnergyNetDelegateSource() {
            super();

        }


        public int getSourceTier() {
            return AdvEnergy.this.sourceTier;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
            return AdvEnergy.this.sourceDirections.contains(dir);
        }

        public double getOfferedEnergy() {
            assert !AdvEnergy.this.sourceDirections.isEmpty();

            return !AdvEnergy.this.sendingSidabled ? AdvEnergy.this.getSourceEnergy() : 0.0D;
        }


        public void drawEnergy(double amount) {
            assert amount <= AdvEnergy.this.storage;

            AdvEnergy.this.storage = AdvEnergy.this.storage - amount;
        }

        @Override
        public double getPerEnergy() {
            return AdvEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return AdvEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            AdvEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            AdvEnergy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }

    }

    private class EnergyMetaNetDelegateSource extends EnergyNetDelegateSource implements IMetaDelegate {

        List<IEnergyTile> list;

        public EnergyMetaNetDelegateSource() {
            list = new ArrayList<>();
            list.add(this);
        }

        @Override
        public List<IEnergyTile> getSubTiles() {
            return list;
        }

    }

    private class EnergyMetaNetDelegateSink extends EnergyNetDelegateSink implements IMetaDelegate {

        List<IEnergyTile> list;

        public EnergyMetaNetDelegateSink() {
            list = new ArrayList<>();
            list.add(this);
        }

        @Override
        public List<IEnergyTile> getSubTiles() {
            return list;
        }

    }

}
