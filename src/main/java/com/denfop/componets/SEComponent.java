package com.denfop.componets;

import com.denfop.api.se.ISEAcceptor;
import com.denfop.api.se.ISEEmitter;
import com.denfop.api.se.ISESink;
import com.denfop.api.se.ISESource;
import com.denfop.api.se.ISETile;
import com.denfop.api.se.event.SETileLoadEvent;
import com.denfop.api.se.event.SETileUnloadEvent;
import ic2.api.energy.EnergyNet;
import ic2.core.IC2;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.TileEntityComponent;
import ic2.core.block.invslot.InvSlot;
import ic2.core.network.GrowingBuffer;
import ic2.core.util.LogCategory;
import ic2.core.util.Util;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SEComponent extends TileEntityComponent {

    public static final boolean debugLoad = System.getProperty("ic2.comp.energy.debugload") != null;
    public final World world;
    public final boolean fullEnergy;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;
    public Set<EnumFacing> sinkDirections;
    public Set<EnumFacing> sourceDirections;
    public List<InvSlot> managedSlots;
    public boolean multiSource;
    public int sourcePackets;
    public SEComponent.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;

    public SEComponent(TileEntityBlock parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public SEComponent(
            TileEntityBlock parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public SEComponent(
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
        this.world = parent.getWorld();
    }

    public static SEComponent asBasicSink(TileEntityBlock parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static SEComponent asBasicSink(TileEntityBlock parent, double capacity, int tier) {
        return new SEComponent(parent, capacity, Util.allFacings, Collections.emptySet(), tier);
    }

    public static SEComponent asBasicSource(TileEntityBlock parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static SEComponent asBasicSource(TileEntityBlock parent, double capacity, int tier) {
        return new SEComponent(parent, capacity, Collections.emptySet(), Util.allFacings, tier);
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
                if (debugLoad) {
                    IC2.log.debug(
                            LogCategory.Component,
                            "Skipping Energy onLoaded for %s at %s.",
                            this.parent, Util.formatPosition(this.parent)
                    );
                }
            } else {
                if (debugLoad) {
                    IC2.log.debug(
                            LogCategory.Component,
                            "Energy onLoaded for %s at %s.",
                            this.parent, Util.formatPosition(this.parent)
                    );
                }

                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new SETileLoadEvent(this.delegate, this.parent.getWorld()));
            }

            this.loaded = true;
        }

    }

    private void createDelegate() {
        if (this.delegate != null) {
            throw new IllegalStateException();
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new SEComponent.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new SEComponent.EnergyNetDelegateSink();
            } else {
                this.delegate = new SEComponent.EnergyNetDelegateDual();
            }

            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            if (debugLoad) {
                IC2.log.debug(LogCategory.Component, "Energy onUnloaded for %s at %s.",
                        this.parent,
                        Util.formatPosition(this.parent)
                );
            }

            MinecraftForge.EVENT_BUS.post(new SETileUnloadEvent(this.delegate, this.parent.getWorld()));
            this.delegate = null;
        } else if (debugLoad) {
            IC2.log.debug(LogCategory.Component, "Skipping Energy onUnloaded for %s at %s.",
                    this.parent,
                    Util.formatPosition(this.parent)
            );
        }

        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buffer = new GrowingBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
    }

    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote && this.managedSlots != null;
    }

    public void onWorldTick() {


    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
        this.storage = Math.min(this.storage, this.capacity);
    }

    public double getEnergy() {
        return this.storage;
    }


    public double getFillRatio() {
        return this.storage / this.capacity;
    }


    public double addEnergy(double amount) {
        amount = Math.min(this.capacity - this.storage, amount);
        this.storage += amount;
        return amount;
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


    public void setDirections(Set<EnumFacing> sinkDirections, Set<EnumFacing> sourceDirections) {

        if (this.delegate != null) {
            if (debugLoad) {
                IC2.log.debug(
                        LogCategory.Component,
                        "Energy setDirections unload for %s at %s.",
                        this.parent, Util.formatPosition(this.parent)
                );
            }

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new SETileUnloadEvent(this.delegate, this.world));
        }

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }

        if (this.delegate != null) {
            if (debugLoad) {
                IC2.log.debug(
                        LogCategory.Component,
                        "Energy setDirections load for %s at %s, sink: %s, source: %s.",
                        this.parent, Util.formatPosition(this.parent), sinkDirections, sourceDirections
                );
            }

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new SETileLoadEvent(this.delegate, this.world));
        } else if (debugLoad) {
            IC2.log.debug(
                    LogCategory.Component,
                    "Skipping Energy setDirections load for %s at %s, sink: %s, source: %s, loaded: %b.",
                    this.parent, Util.formatPosition(this.parent), sinkDirections, sourceDirections, this.loaded
            );
        }


    }

    public Set<EnumFacing> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<EnumFacing> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public ISETile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        if (this.fullEnergy) {
            return this.storage >= EnergyNet.instance.getPowerFromTier(this.sourceTier) ? this.storage : 0.0D;
        } else {
            return this.storage;
        }
    }

    private abstract static class EnergyNetDelegate extends TileEntity implements ISETile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateDual extends SEComponent.EnergyNetDelegate implements ISESink, ISESource {

        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsSEFrom(ISEEmitter emitter, EnumFacing dir) {
            return SEComponent.this.sinkDirections.contains(dir);
        }

        public boolean emitsSETo(ISEAcceptor receiver, EnumFacing dir) {
            return SEComponent.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return SEComponent.this.parent.getPos();
        }

        public double getOfferedSE() {
            return !SEComponent.this.sendingSidabled && !SEComponent.this.sourceDirections.isEmpty()
                    ? SEComponent.this.getSourceEnergy()
                    : 0.0D;
        }

        @Override
        public TileEntity getTile() {
            return SEComponent.this.parent;
        }

        public int getSinkTier() {
            return SEComponent.this.sinkTier;
        }

        public int getSourceTier() {
            return SEComponent.this.sourceTier;
        }

        @Override
        public double getDemandedSE() {
            return !SEComponent.this.receivingDisabled && !SEComponent.this.sinkDirections.isEmpty() && SEComponent.this.storage < SEComponent.this.capacity
                    ? SEComponent.this.capacity - SEComponent.this.storage
                    : 0.0D;

        }

        @Override
        public double injectSE(final EnumFacing var1, final double var2, final double var4) {
            SEComponent.this.storage = SEComponent.this.storage + var2;
            return 0.0D;
        }


        public void drawSE(double amount) {
            assert amount <= SEComponent.this.storage;

            SEComponent.this.storage = SEComponent.this.storage - amount;
        }


    }

    private class EnergyNetDelegateSink extends SEComponent.EnergyNetDelegate implements ISESink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return SEComponent.this.sinkTier;
        }

        public boolean acceptsSEFrom(ISEEmitter emitter, EnumFacing dir) {
            return SEComponent.this.sinkDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return SEComponent.this.parent.getPos();
        }

        public double getDemandedSE() {
            assert !SEComponent.this.sinkDirections.isEmpty();

            return !SEComponent.this.receivingDisabled && SEComponent.this.storage < SEComponent.this.capacity
                    ? SEComponent.this.capacity - SEComponent.this.storage
                    : 0.0D;
        }

        public double injectSE(EnumFacing directionFrom, double amount, double voltage) {
            SEComponent.this.storage = SEComponent.this.storage + amount;
            return 0.0D;
        }

        @Override
        public TileEntity getTile() {
            return SEComponent.this.parent;
        }

    }

    private class EnergyNetDelegateSource extends SEComponent.EnergyNetDelegate implements ISESource {

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return SEComponent.this.sourceTier;
        }

        public boolean emitsSETo(ISEAcceptor receiver, EnumFacing dir) {
            return SEComponent.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return SEComponent.this.parent.getPos();
        }

        public double getOfferedSE() {
            assert !SEComponent.this.sourceDirections.isEmpty();

            return !SEComponent.this.sendingSidabled ? SEComponent.this.getSourceEnergy() : 0.0D;
        }

        public void drawSE(double amount) {
            assert amount <= SEComponent.this.storage;

            SEComponent.this.storage = SEComponent.this.storage - amount;
        }

        @Override
        public TileEntity getTile() {
            return SEComponent.this.parent;
        }

    }

}
