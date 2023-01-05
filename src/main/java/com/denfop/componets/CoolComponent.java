package com.denfop.componets;

import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
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
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CoolComponent extends TileEntityComponent {

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
    public CoolComponent.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean upgrade = false;
    public int meta = 0;
    public boolean allow = false;
    private double coef;

    public CoolComponent(TileEntityBlock parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public CoolComponent(
            TileEntityBlock parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public CoolComponent(
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
        this.coef = 1;
    }

    public static CoolComponent asBasicSink(TileEntityBlock parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static CoolComponent asBasicSink(TileEntityBlock parent, double capacity, int tier) {
        return new CoolComponent(parent, capacity, Util.allFacings, Collections.emptySet(), tier);
    }

    public static CoolComponent asBasicSource(TileEntityBlock parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static CoolComponent asBasicSource(TileEntityBlock parent, double capacity, int tier) {
        return new CoolComponent(parent, capacity, Collections.emptySet(), Util.allFacings, tier);
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.upgrade = nbt.getBoolean("upgrade");
        this.meta = nbt.getInteger("meta");
        this.allow = nbt.getBoolean("allow");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setBoolean("upgrade", this.upgrade);
        ret.setInteger("meta", this.meta);
        ret.setBoolean("allow", this.allow);

        return ret;
    }

    public void onLoaded() {
        assert this.delegate == null;

        if (!this.parent.getWorld().isRemote) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {
                if (debugLoad) {
                    IC2.log.debug(LogCategory.Component, "Skipping Energy onLoaded for %s at %s.",
                            this.parent,
                            Util.formatPosition(this.parent)
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
                MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.parent.getWorld()));
            }

            this.loaded = true;
            switch (this.parent.getWorld().provider.getBiomeForCoords(this.parent.getPos()).getTempCategory()) {
                case COLD:
                    coef = 0.5;
                    break;
                case WARM:
                    coef = 1.5;
                    break;
                default:
                    coef = 1;
                    break;
            }
        }

    }

    private void createDelegate() {
        if (this.delegate != null) {
            throw new IllegalStateException();
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new CoolComponent.EnergyNetDelegateSource();
            } else {
                this.delegate = new CoolComponent.EnergyNetDelegateSink();
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

            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this.delegate, this.parent.getWorld()));
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
        buffer.writeInt(this.meta);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.meta = is.readInt();
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
        if (this.storage > this.capacity) {
            this.storage = this.capacity;
        }
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

        this.storage += amount * this.coef;
        this.storage = Math.min(this.storage, this.capacity);
        this.storage = Math.max(this.storage, 0);
        if (this.upgrade) {
            this.storage = 0;
        }

        return amount;
    }

    public void forceAddEnergy(double amount) {
        this.storage += amount;
    }

    public boolean canUseEnergy(double amount) {
        return this.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.storage >= amount / this.coef) {
            this.storage -= amount / this.coef;
            if (CoolComponent.this.storage <= 0.005) {
                CoolComponent.this.storage = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        if (this.storage <= 0) {
            this.storage = 0;
            return amount;
        }
        double ret = Math.abs(Math.max(0.0D, amount - this.storage) - amount) / this.coef;
        if (!simulate) {
            this.storage -= ret / this.coef;
            if (CoolComponent.this.storage <= 0.005) {
                CoolComponent.this.storage = 0;
            }
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
            if (debugLoad) {
                IC2.log.debug(
                        LogCategory.Component,
                        "Energy setDirections unload for %s at %s.",
                        this.parent, Util.formatPosition(this.parent)
                );
            }

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this.delegate, this.world));
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

            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.world));
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

    public ICoolTile getDelegate() {
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

    private abstract static class EnergyNetDelegate extends TileEntity implements ICoolTile {

        private EnergyNetDelegate() {
        }

    }


    private class EnergyNetDelegateSink extends CoolComponent.EnergyNetDelegate implements ICoolSink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return CoolComponent.this.sinkTier;
        }

        public boolean acceptsCoolFrom(ICoolEmitter emitter, EnumFacing dir) {
            return CoolComponent.this.sinkDirections.contains(dir);
        }

        public double getDemandedCool() {
            if(CoolComponent.this.storage != 0)
            return 64;
            else
                return 0;
        }

        public double injectCool(EnumFacing directionFrom, double amount, double voltage) {
            if (amount > 0) {
                CoolComponent.this.useEnergy(0.05 * amount / 4, false);

            }

            return 0.0D;
        }

        @Override
        public boolean needCooling() {
            return CoolComponent.this.storage > 0;
        }

    }

    private class EnergyNetDelegateSource extends CoolComponent.EnergyNetDelegate implements ICoolSource {

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return CoolComponent.this.sourceTier;
        }

        public boolean emitsCoolTo(ICoolAcceptor receiver, EnumFacing dir) {
            return CoolComponent.this.sourceDirections.contains(dir);
        }

        public double getOfferedCool() {
            assert !CoolComponent.this.sourceDirections.isEmpty();

            return !CoolComponent.this.sendingSidabled ? CoolComponent.this.getSourceEnergy() : 0.0D;
        }

        public void drawCool(double amount) {
        }

        @Override
        public boolean isAllowed() {
            return CoolComponent.this.allow;
        }

        @Override
        public void setAllowed(final boolean allowed) {
            CoolComponent.this.allow = allowed;
        }


    }

}
