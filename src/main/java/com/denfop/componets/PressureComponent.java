package com.denfop.componets;

import com.denfop.api.pressure.IPressureAcceptor;
import com.denfop.api.pressure.IPressureEmitter;
import com.denfop.api.pressure.IPressureSink;
import com.denfop.api.pressure.IPressureSource;
import com.denfop.api.pressure.IPressureTile;
import com.denfop.api.pressure.event.PressureTileLoadEvent;
import com.denfop.api.pressure.event.PressureTileUnloadEvent;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PressureComponent extends AbstractComponent {

    public final World world;
    public final boolean fullEnergy;
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
    public PressureComponent.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean need;
    public boolean auto;
    public boolean allow;
    Random rand = new Random();

    public PressureComponent(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public PressureComponent(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public PressureComponent(
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
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.fullEnergy = fullEnergy;
        this.world = parent.getWorld();
        this.defaultCapacity = capacity;
        this.need = true;
        this.allow = false;
    }

    public static PressureComponent asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static PressureComponent asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new PressureComponent(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static PressureComponent asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static PressureComponent asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new PressureComponent(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
        this.capacity = nbt.getDouble("capacity");
        this.need = nbt.getBoolean("need");
        this.allow = nbt.getBoolean("allow");
        this.auto = nbt.getBoolean("auto");


    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        packet.writeDouble(this.capacity);
        packet.writeDouble(this.storage);
        packet.writeBoolean(this.need);
        packet.writeBoolean(this.allow);
        packet.writeBoolean(this.auto);
        return packet;
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);
        ret.setDouble("capacity", this.capacity);
        ret.setBoolean("need", this.need);
        ret.setBoolean("allow", this.allow);
        ret.setBoolean("auto", this.auto);
        return ret;
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
                MinecraftForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, this.parent.getWorld()));
            }

            this.loaded = true;

        }

    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    public boolean canUsePurifier(EntityPlayer player) {
        return false;
    }


    public void createDelegate() {
        if (this.delegate != null) {
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new PressureComponent.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new PressureComponent.EnergyNetDelegateSink();
            }
            if (delegate == null) {
                return;
            }
            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();

        return ret;
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, EnumHand hand) {
        super.onBlockActivated(player, hand);
        return false;
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, this.parent.getWorld()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeBoolean(this.need);
        buffer.writeBoolean(this.allow);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.need = is.readBoolean();
        this.allow = is.readBoolean();
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
            if (storage < 0) {
                this.storage = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.parent.getWorld().provider.getWorldTime() % 120 == 0) {
            this.useEnergy(1);
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.storage) - amount);
        if (!simulate) {
            this.storage -= ret;
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


            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, this.world));
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

            MinecraftForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, this.world));
        }


    }

    public Set<EnumFacing> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<EnumFacing> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public IPressureTile getDelegate() {
        return this.delegate;
    }


    private abstract static class EnergyNetDelegate extends TileEntity implements IPressureTile {

        private EnergyNetDelegate() {
        }

    }

    private class EnergyNetDelegateSink extends PressureComponent.EnergyNetDelegate implements IPressureSink {

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return PressureComponent.this.sinkTier;
        }

        public boolean acceptsPressureFrom(IPressureEmitter emitter, EnumFacing dir) {
            return PressureComponent.this.sinkDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return PressureComponent.this.parent.getPos();
        }

        public double getDemandedPressure() {

            return PressureComponent.this.capacity;
        }

        public void receivedPressure(double amount) {
            this.setPressureStored(amount);

        }

        @Override
        public boolean needTemperature() {
            return PressureComponent.this.need;
        }

        public void setPressureStored(double amount) {
            if (PressureComponent.this.storage < amount) {
                PressureComponent.this.storage = amount;
            }
        }

        @Override
        public TileEntity getTile() {
            return PressureComponent.this.parent;
        }

    }

    private class EnergyNetDelegateSource extends PressureComponent.EnergyNetDelegate implements IPressureSource {

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return PressureComponent.this.sourceTier;
        }

        public boolean emitsPressureTo(IPressureAcceptor receiver, EnumFacing dir) {
            return PressureComponent.this.sourceDirections.contains(dir);
        }

        public double getOfferedPressure() {

            return PressureComponent.this.storage;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return PressureComponent.this.parent.getPos();
        }

        public void drawPressure(double amount) {
        }

        @Override
        public boolean isAllowed() {
            return PressureComponent.this.allow;
        }

        @Override
        public boolean setAllowed(final boolean allowed) {
            return PressureComponent.this.allow = allowed;
        }

        @Override
        public TileEntity getTile() {
            return PressureComponent.this.parent;
        }

    }

}
