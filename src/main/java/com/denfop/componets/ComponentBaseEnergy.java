package com.denfop.componets;

import com.denfop.api.sytem.EnergyEvent;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.sytem.EnumTypeEvent;
import com.denfop.api.sytem.IAcceptor;
import com.denfop.api.sytem.IDual;
import com.denfop.api.sytem.IEmitter;
import com.denfop.api.sytem.ISink;
import com.denfop.api.sytem.ISource;
import com.denfop.api.sytem.ITile;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComponentBaseEnergy extends AbstractComponent {


    public final boolean fullEnergy;
    private final EnergyType type;
    private final double defaultCapacity;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;
    public Set<EnumFacing> sinkDirections;
    public Set<EnumFacing> sourceDirections;
    public List<InvSlot> managedSlots;
    public boolean multiSource;
    public int sourcePackets;
    public ComponentBaseEnergy.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    protected double pastEnergy;
    protected double perenergy;
    private double perenergy1;
    private double pastEnergy1;
    private double tick1;

    public ComponentBaseEnergy(EnergyType type, TileEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);
        this.type = type;
        this.multiSource = false;
        this.sourcePackets = 1;
        this.capacity = capacity;
        this.defaultCapacity = capacity;
        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            List<EnumFacing> sinkDirections,
            List<EnumFacing> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);
        this.type = type;
        this.multiSource = false;
        this.sourcePackets = 1;
        this.capacity = capacity;
        this.defaultCapacity = capacity;
        this.sinkTier = sinkTier;
        this.sourceTier = sourceTier;
        this.sinkDirections = new HashSet<>(sinkDirections);
        this.sourceDirections = new HashSet<>(sourceDirections);
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
    }

    public static ComponentBaseEnergy asBasicSink(EnergyType type, TileEntityInventory parent, double capacity) {
        return asBasicSink(type, parent, capacity, 1);
    }

    public static ComponentBaseEnergy asBasicSink(EnergyType type, TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBaseEnergy(type, parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static ComponentBaseEnergy asBasicSource(EnergyType type, TileEntityInventory parent, double capacity) {
        return asBasicSource(type, parent, capacity, 1);
    }

    public static ComponentBaseEnergy asBasicSource(EnergyType type, TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBaseEnergy(type, parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    public EnergyType getType() {
        return type;
    }

    @Override
    public String toString() {
        return super.toString() + this.type.name().toLowerCase();
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.capacity = nbt.getDouble("capacity");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setDouble("capacity", this.capacity);
        return ret;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    public void onLoaded() {
        assert this.delegate == null;

        if (this.capacity < this.defaultCapacity) {
            this.capacity = this.defaultCapacity;
        }
        if (!this.parent.getWorld().isRemote) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {

            } else {


                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getWorld(), EnumTypeEvent.LOAD, this.type,
                        this.delegate
                ));
            }

            this.loaded = true;
        }

    }

    private void createDelegate() {
        if (this.delegate != null) {
        } else {

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateSink();
            } else {
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateDual();
            }

            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getWorld(), EnumTypeEvent.UNLOAD, this.type,
                    this.delegate
            ));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
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

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (this.delegate != null) {


            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getWorld(), EnumTypeEvent.UNLOAD, this.type,
                    this.delegate
            ));
        }

        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }

        if (this.delegate != null) {

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getWorld(), EnumTypeEvent.LOAD, this.type,
                    this.delegate
            ));
        }


    }

    public Set<EnumFacing> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<EnumFacing> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public ITile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {

        return this.storage;
    }

    public double getFreeEnergy() {
        return this.capacity - this.storage;
    }

    private abstract static class EnergyNetDelegate extends TileEntity implements ITile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateDual extends ComponentBaseEnergy.EnergyNetDelegate implements IDual {

        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsFrom(IEmitter emitter, EnumFacing dir) {
            return ComponentBaseEnergy.this.sinkDirections.contains(dir);
        }

        public boolean emitsTo(IAcceptor receiver, EnumFacing dir) {
            return ComponentBaseEnergy.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return ComponentBaseEnergy.this.parent.getPos();
        }

        public double canProvideEnergy() {
            return !ComponentBaseEnergy.this.sendingSidabled && !ComponentBaseEnergy.this.sourceDirections.isEmpty()
                    ? ComponentBaseEnergy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return ComponentBaseEnergy.this.sinkTier;
        }

        public int getSourceTier() {
            return ComponentBaseEnergy.this.sourceTier;
        }

        @Override
        public double getDemanded() {
            return !ComponentBaseEnergy.this.receivingDisabled && !ComponentBaseEnergy.this.sinkDirections.isEmpty() && ComponentBaseEnergy.this.storage < ComponentBaseEnergy.this.capacity
                    ? ComponentBaseEnergy.this.capacity - ComponentBaseEnergy.this.storage
                    : 0.0D;

        }

        @Override
        public void receivedEnergy(final double var2) {
            ComponentBaseEnergy.this.storage = ComponentBaseEnergy.this.storage + var2;
        }

        @Override
        public double getPerEnergy1() {
            return ComponentBaseEnergy.this.perenergy1;
        }

        @Override
        public double getPastEnergy1() {
            return ComponentBaseEnergy.this.pastEnergy1;
        }

        @Override
        public void setPastEnergy1(final double pastEnergy) {
            ComponentBaseEnergy.this.pastEnergy1 = pastEnergy;
        }

        @Override
        public void addPerEnergy1(final double setEnergy) {
            ComponentBaseEnergy.this.perenergy1 += setEnergy;
        }


        @Override
        public void addTick1(final double tick) {
            ComponentBaseEnergy.this.tick1 = tick;
        }

        @Override
        public double getTick1() {
            return ComponentBaseEnergy.this.tick1;
        }

        public void extractEnergy(double amount) {
            assert amount <= ComponentBaseEnergy.this.storage;

            ComponentBaseEnergy.this.storage = ComponentBaseEnergy.this.storage - amount;
        }


        @Override
        public double getPerEnergy() {
            return ComponentBaseEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return ComponentBaseEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            ComponentBaseEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            ComponentBaseEnergy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }

        @Override
        public void addTick(final double tick) {
            ComponentBaseEnergy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return ComponentBaseEnergy.this.tick;
        }

        @Override
        public boolean isSink() {
            return true;
        }

        @Override
        public TileEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

    }

    private class EnergyNetDelegateSink extends ComponentBaseEnergy.EnergyNetDelegate implements ISink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return ComponentBaseEnergy.this.sinkTier;
        }

        public boolean acceptsFrom(IEmitter emitter, EnumFacing dir) {
            return ComponentBaseEnergy.this.sinkDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return ComponentBaseEnergy.this.parent.getPos();
        }

        public double getDemanded() {
            assert !ComponentBaseEnergy.this.sinkDirections.isEmpty();

            return !ComponentBaseEnergy.this.receivingDisabled && ComponentBaseEnergy.this.storage < ComponentBaseEnergy.this.capacity
                    ? ComponentBaseEnergy.this.capacity - ComponentBaseEnergy.this.storage
                    : 0.0D;
        }

        @Override
        public TileEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

        public void receivedEnergy(double amount) {
            ComponentBaseEnergy.this.storage = ComponentBaseEnergy.this.storage + amount;
        }

        @Override
        public double getPerEnergy() {
            return ComponentBaseEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return ComponentBaseEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            ComponentBaseEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            ComponentBaseEnergy.this.perenergy += setEnergy;
        }

        @Override
        public void addTick(final double tick) {
            ComponentBaseEnergy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return ComponentBaseEnergy.this.tick;
        }

        @Override
        public boolean isSink() {
            return true;
        }

    }

    private class EnergyNetDelegateSource extends ComponentBaseEnergy.EnergyNetDelegate implements ISource {

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return ComponentBaseEnergy.this.sourceTier;
        }

        public boolean emitsTo(IAcceptor receiver, EnumFacing dir) {
            return ComponentBaseEnergy.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return ComponentBaseEnergy.this.parent.getPos();
        }

        public double canProvideEnergy() {
            assert !ComponentBaseEnergy.this.sourceDirections.isEmpty();

            return !ComponentBaseEnergy.this.sendingSidabled ? ComponentBaseEnergy.this.getSourceEnergy() : 0.0D;
        }

        @Override
        public TileEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

        public void extractEnergy(double amount) {
            assert amount <= ComponentBaseEnergy.this.storage;

            ComponentBaseEnergy.this.storage = ComponentBaseEnergy.this.storage - amount;
        }

        @Override
        public double getPerEnergy() {
            return ComponentBaseEnergy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return ComponentBaseEnergy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            ComponentBaseEnergy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            ComponentBaseEnergy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }


    }

}
