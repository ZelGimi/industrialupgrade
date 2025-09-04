package com.denfop.componets;

import com.denfop.api.otherenergies.pressure.IPressureTile;
import com.denfop.api.otherenergies.pressure.event.PressureTileLoadEvent;
import com.denfop.api.otherenergies.pressure.event.PressureTileUnloadEvent;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.pressure.EnergyNetDelegate;
import com.denfop.componets.pressure.EnergyNetDelegateSink;
import com.denfop.componets.pressure.EnergyNetDelegateSource;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public class PressureComponent extends AbstractComponent {
    public final BufferEnergy buffer;
    private final double defaultCapacity;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean auto;
    Random rand = new Random();
    private long id;

    public PressureComponent(BlockEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public PressureComponent(
            BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public PressureComponent(
            BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
        this.defaultCapacity = capacity;
    }

    public static PressureComponent asBasicSink(BlockEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static PressureComponent asBasicSink(BlockEntityInventory parent, double capacity, int tier) {
        return new PressureComponent(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static PressureComponent asBasicSource(BlockEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static PressureComponent asBasicSource(BlockEntityInventory parent, double capacity, int tier) {
        return new PressureComponent(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public void readFromNbt(CompoundTag nbt) {
        this.buffer.storage = nbt.getDouble("storage");
        this.buffer.capacity = nbt.getDouble("capacity");
        this.buffer.need = nbt.getBoolean("need");
        this.buffer.allow = nbt.getBoolean("allow");
        this.auto = nbt.getBoolean("auto");


    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        packet.writeDouble(this.buffer.capacity);
        packet.writeDouble(this.buffer.storage);
        packet.writeBoolean(this.buffer.need);
        packet.writeBoolean(this.buffer.allow);
        packet.writeBoolean(this.auto);
        return packet;
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.buffer.storage);
        ret.putDouble("capacity", this.buffer.capacity);
        ret.putBoolean("need", this.buffer.need);
        ret.putBoolean("allow", this.buffer.allow);
        ret.putBoolean("auto", this.auto);
        return ret;
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
                MinecraftForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, this.parent.getLevel()));
            }

            this.loaded = true;

        }

    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    public boolean canUsePurifier(Player player) {
        return false;
    }


    public void createDelegate() {
        if (this.delegate != null) {
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new EnergyNetDelegateSource(this);
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new EnergyNetDelegateSink(this);
            }
            if (delegate == null) {
                return;
            }
        }
    }


    @Override
    public boolean onBlockActivated(Player player, InteractionHand hand) {
        super.onBlockActivated(player, hand);
        return false;
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, this.parent.getLevel()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeBoolean(this.buffer.need);
        buffer.writeBoolean(this.buffer.allow);
        buffer.writeBoolean(this.auto);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.buffer.capacity = is.readDouble();
        this.buffer.storage = is.readDouble();
        this.buffer.need = is.readBoolean();
        this.buffer.allow = is.readBoolean();
        this.auto = is.readBoolean();
    }


    public double getCapacity() {
        return this.buffer.capacity;
    }

    public void setCapacity(double capacity) {
        this.buffer.capacity = capacity;
        if (this.buffer.storage > this.buffer.capacity) {
            this.buffer.storage = this.buffer.capacity;
        }
    }

    public double getEnergy() {
        return this.buffer.storage;
    }


    public double getFillRatio() {
        return this.buffer.storage / this.buffer.capacity;
    }


    public double addEnergy(double amount) {

        this.buffer.storage += amount;
        this.buffer.storage = Math.min(this.buffer.storage, this.buffer.capacity);
        this.buffer.storage = Math.max(this.buffer.storage, 0);


        return amount;
    }


    public boolean canUseEnergy(double amount) {
        return this.buffer.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.buffer.storage >= amount) {
            this.buffer.storage -= amount;
            if (buffer.storage < 0) {
                this.buffer.storage = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.parent.getLevel().getGameTime() % 120 == 0) {
            this.useEnergy(1);
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.buffer.storage) - amount);
        if (!simulate) {
            this.buffer.storage -= ret;
            if (buffer.storage < 0) {
                this.buffer.storage = 0;
            }
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


    public void setReceivingEnabled(boolean enabled) {
        this.delegate.receivingDisabled = !enabled;
    }

    public void setSendingEnabled(boolean enabled) {
        this.delegate.sendingSidabled = !enabled;
    }


    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, parent.getLevel()));
        }

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }

        if (this.delegate != null) {

            delegate.sinkDirections = sinkDirections;
            delegate.sourceDirections = sourceDirections;
            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, parent.getLevel()));
        }


    }

    public IPressureTile getDelegate() {
        return this.delegate;
    }


}
