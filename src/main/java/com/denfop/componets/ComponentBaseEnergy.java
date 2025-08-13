package com.denfop.componets;


import com.denfop.api.sytem.*;
import com.denfop.componets.system.EnergyNetDelegate;
import com.denfop.componets.system.EnergyNetDelegateDual;
import com.denfop.componets.system.EnergyNetDelegateSink;
import com.denfop.componets.system.EnergyNetDelegateSource;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.*;

public class ComponentBaseEnergy extends AbstractComponent {



    private final EnergyType type;
    private final double defaultCapacity;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public EnergyNetDelegate delegate;
    public boolean loaded;

    public final BufferEnergy buffer;

    public ComponentBaseEnergy(EnergyType type, TileEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier
    ) {
        super(parent);
        this.type = type;
        this.defaultCapacity = capacity;
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
            int sinkTier,
            int sourceTier
    ) {
        super(parent);
        this.type = type;
        this.defaultCapacity = capacity;
        this.sinkDirections = new HashSet<>(sinkDirections);
        this.sourceDirections = new HashSet<>(sourceDirections);
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
    }
    public void setReceivingEnabled(boolean enabled) {
        if (this.delegate != null) {
            this.delegate.receivingDisabled = !enabled;
        }

    }

    public void setSendingEnabled(boolean enabled) {
        if (this.delegate != null) {
            this.delegate.sendingSidabled = !enabled;
        }

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

    public void readFromNbt(CompoundTag nbt) {
        this.buffer.storage = nbt.getDouble("storage");
        this.buffer.capacity = nbt.getDouble("capacity");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.buffer.storage);
        ret.putDouble("capacity", this.buffer.capacity);
        return ret;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    public void onLoaded() {
        assert this.delegate == null;

        if (this.buffer.capacity < this.defaultCapacity) {
            this.buffer.capacity = this.defaultCapacity;
        }
        if (!this.parent.getLevel().isClientSide) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {

            } else {


                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.LOAD, this.type,
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
                this.delegate = new EnergyNetDelegateSource(this);
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new EnergyNetDelegateSink(this);
            } else {
                this.delegate = new EnergyNetDelegateDual(this);
            }

        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.UNLOAD, this.type,
                    this.delegate
            ));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeInt(this.buffer.sourceTier);
        buffer.writeInt(this.buffer.sinkTier);
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeInt(this.buffer.sourceTier);
        buffer.writeInt(this.buffer.sinkTier);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.buffer.capacity = is.readDouble();
        this.buffer.storage = is.readDouble();
        this.buffer.sourceTier = is.readInt();
        this.buffer.sinkTier = is.readInt();
    }

    public double getCapacity() {
        return this.buffer.capacity;
    }

    public void setCapacity(double capacity) {
        this.buffer.capacity = capacity;
        if (this.buffer.storage > this.buffer.capacity) {
            this.buffer.storage = capacity;
        }
    }

    public double getEnergy() {
        return this.buffer.storage;
    }

    public double getFillRatio() {
        return this.buffer.storage / this.buffer.capacity;
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.buffer.capacity - this.buffer.storage, amount);
        this.buffer.storage += amount;
        return amount;
    }

    public void blockBreak() {
        if (this.getType() == EnergyType.RADIATION) {
            new PacketUpdateRadiationValue(new ChunkPos(this.parent.getBlockPos()), (int) this.buffer.storage);
        } else if (this.getType() == EnergyType.EXPERIENCE && this.buffer.storage > 0) {
            if (this.parent.getLevel() instanceof ServerLevel serverLevel) {
                double f = 0.7;
                double dx = serverLevel.random.nextDouble() * 1 + (1.0 - f) * 0.5;
                double dy = serverLevel.random.nextDouble() * f + (1.0 - f) * 0.5;
                double dz = serverLevel.random.nextDouble() * f + (1.0 - f) * 0.5;

                int xpAmount = ExperienceOrb.getExperienceValue((int) buffer.storage);
                ExperienceOrb xpOrb = new ExperienceOrb(
                        serverLevel,
                        this.parent.getBlockPos().getX() + dx,
                        this.parent.getBlockPos().getY() + dy,
                        this.parent.getBlockPos().getZ() + dz,
                        xpAmount
                );

                serverLevel.addFreshEntity(xpOrb);
            }
        }
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

    public int getSinkTier() {
        return this.buffer.sinkTier;
    }

    public void setSinkTier(int tier) {
        this.buffer.sinkTier = tier;
    }

    public int getSourceTier() {
        return this.buffer.sourceTier;
    }

    public void setSourceTier(int tier) {
        this.buffer.sourceTier = tier;
    }




    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (this.delegate != null) {
            assert !this.parent.getLevel().isClientSide;
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.UNLOAD, this.type,
                    this.delegate
            ));
        }

        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }

        if (this.delegate != null) {

            this.delegate.sourceDirections = sourceDirections;
            this.delegate.sinkDirections = sinkDirections;
            delegate.energyConductorMap.clear();
            delegate.getValidReceivers(type).clear();
            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.LOAD, this.type,
                    this.delegate
            ));
        }


    }



    public ITile getDelegate() {
        return this.delegate;
    }



    public double getFreeEnergy() {
        return this.buffer.capacity - this.buffer.storage;
    }









}
