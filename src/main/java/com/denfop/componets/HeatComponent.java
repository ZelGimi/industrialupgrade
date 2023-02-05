package com.denfop.componets;

import com.denfop.api.heat.IHeatAcceptor;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.heat.IHeatSink;
import com.denfop.api.heat.IHeatSource;
import com.denfop.api.heat.IHeatTile;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
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
import java.util.Random;
import java.util.Set;

public class HeatComponent extends TileEntityComponent {

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
    public HeatComponent.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean need;
    public boolean allow;
    Random rand = new Random();
    private double coef;

    public HeatComponent(TileEntityBlock parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public HeatComponent(
            TileEntityBlock parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public HeatComponent(
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
        this.need = true;
        this.allow = false;
        this.coef = 0;
    }

    public static HeatComponent asBasicSink(TileEntityBlock parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static HeatComponent asBasicSink(TileEntityBlock parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, Util.allFacings, Collections.emptySet(), tier);
    }

    public static HeatComponent asBasicSource(TileEntityBlock parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static HeatComponent asBasicSource(TileEntityBlock parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, Collections.emptySet(), Util.allFacings, tier);
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.capacity = nbt.getDouble("capacity");
        this.need = nbt.getBoolean("need");
        this.allow = nbt.getBoolean("allow");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setDouble("capacity", this.capacity);
        ret.setBoolean("need", this.need);
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
                MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this.delegate, this.parent.getWorld()));
            }

            this.loaded = true;
            switch (this.parent.getWorld().provider.getBiomeForCoords(this.parent.getPos()).getTempCategory()) {
                case COLD:
                    coef = -1;
                    break;
                case WARM:
                    coef = 1;
                    break;
                default:
                    coef = 0;
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
                this.delegate = new HeatComponent.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new HeatComponent.EnergyNetDelegateSink();
            }
            if (delegate == null) {
                return;
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

            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this.delegate, this.parent.getWorld()));
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
        buffer.writeBoolean(this.need);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(DataInput is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.need = is.readBoolean();
    }

    public boolean enableWorldTick() {
        return !this.parent.getWorld().isRemote;
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


    public double getFillRatio() {
        return this.storage / this.capacity;
    }


    public double addEnergy(double amount) {

        this.storage += amount;
        if (this.world != null) {
            if (rand.nextInt(2) == 1) {
                this.storage += 1 * this.coef;
            }
        }
        this.storage = Math.min(this.storage, this.capacity);
        this.storage = Math.max(this.storage, 0);


        return amount;
    }


    public boolean canUseEnergy(double amount) {
        return this.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.storage >= amount) {
            this.storage -= amount;
            if (rand.nextInt(2) == 1) {
                if (this.coef == -1) {
                    this.storage -= 1;
                }
            }
            if (storage < 0) {
                this.storage = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.storage) - amount);
        if (!simulate) {
            this.storage -= ret;
            if (rand.nextInt(2) == 1) {
                if (this.coef == -1) {
                    this.storage -= 1;
                }
            }
            if (storage < 0) {
                this.storage = 0;
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

            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this.delegate, this.world));
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

            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this.delegate, this.world));
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

    public IHeatTile getDelegate() {
        return this.delegate;
    }


    private abstract static class EnergyNetDelegate extends TileEntity implements IHeatTile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateSink extends HeatComponent.EnergyNetDelegate implements IHeatSink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return HeatComponent.this.sinkTier;
        }

        public boolean acceptsHeatFrom(IHeatEmitter emitter, EnumFacing dir) {
            return HeatComponent.this.sinkDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return HeatComponent.this.parent.getPos();
        }

        public double getDemandedHeat() {

            return HeatComponent.this.capacity;
        }

        public double injectHeat(EnumFacing directionFrom, double amount, double voltage) {
            this.setHeatStored(amount);
            return 0.0D;

        }

        @Override
        public boolean needTemperature() {
            return HeatComponent.this.need;
        }

        @Override
        public boolean setNeedTemperature(final boolean need) {
            return HeatComponent.this.need = need;
        }

        public void setHeatStored(double amount) {
            if (HeatComponent.this.storage < amount) {
                HeatComponent.this.storage = amount;
            }
        }

        @Override
        public TileEntity getTile() {
            return HeatComponent.this.parent;
        }

    }

    private class EnergyNetDelegateSource extends HeatComponent.EnergyNetDelegate implements IHeatSource {

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return HeatComponent.this.sourceTier;
        }

        public boolean emitsHeatTo(IHeatAcceptor receiver, EnumFacing dir) {
            return HeatComponent.this.sourceDirections.contains(dir);
        }

        public double getOfferedHeat() {

            return HeatComponent.this.storage;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return HeatComponent.this.parent.getPos();
        }

        public void drawHeat(double amount) {
        }

        @Override
        public boolean isAllowed() {
            return HeatComponent.this.allow;
        }

        @Override
        public boolean setAllowed(final boolean allowed) {
            return HeatComponent.this.allow = allowed;
        }

        @Override
        public TileEntity getTile() {
            return HeatComponent.this.parent;
        }

    }

}
