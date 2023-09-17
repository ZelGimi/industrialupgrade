package com.denfop.componets;

import com.denfop.api.cool.ICoolAcceptor;
import com.denfop.api.cool.ICoolEmitter;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.cool.ICoolSource;
import com.denfop.api.cool.ICoolTile;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CoolComponent extends AbstractComponent {

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

    public CoolComponent(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public CoolComponent(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sinkDirections,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public CoolComponent(
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
        this.coef = 1;
    }

    public static CoolComponent asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static CoolComponent asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new CoolComponent(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static CoolComponent asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static CoolComponent asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new CoolComponent(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
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
        if (this.storage > this.capacity) {
            this.storage = this.capacity;
        }
        if (!this.parent.getWorld().isRemote) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {
            } else {

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


            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this.delegate, this.parent.getWorld()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeInt(this.meta);
        buffer.writeBoolean(this.upgrade);
        buffer.writeBoolean(this.allow);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.meta = is.readInt();
        this.upgrade = is.readBoolean();
        this.allow = is.readBoolean();
    }
    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        packet.writeDouble(this.capacity);
        packet.writeDouble(this.storage);
        packet.writeInt(this.meta);
        packet.writeBoolean(this.upgrade);
        packet.writeBoolean(this.allow);
        return packet;
    }

    @Override
    public boolean isServer() {
        return false;
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

        this.storage += amount * this.coef;
        this.storage = Math.min(this.storage, this.capacity);
        this.storage = Math.max(this.storage, 0);
        if (this.upgrade) {
            this.storage = 0;
        }

        return amount;
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

    public void setDirections(Set<EnumFacing> sinkDirections, Set<EnumFacing> sourceDirections) {

        if (this.delegate != null) {


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

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.world));
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
        return this.storage;
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

        @Override
        public @NotNull BlockPos getBlockPos() {
            return CoolComponent.this.parent.getPos();
        }

        public double getDemandedCool() {
            if (CoolComponent.this.storage != 0) {
                return 64;
            } else {
                return 0;
            }
        }

        public void receivedCold(double amount) {
            if (amount > 0) {
                CoolComponent.this.useEnergy(0.05 * amount / 4, false);

            }

        }

        @Override
        public boolean needCooling() {
            return CoolComponent.this.storage > 0;
        }

        @Override
        public TileEntity getTile() {
            return CoolComponent.this.parent;
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

        @Override
        public @NotNull BlockPos getBlockPos() {
            return CoolComponent.this.parent.getPos();
        }

        public double getOfferedCool() {
            assert !CoolComponent.this.sourceDirections.isEmpty();

            return !CoolComponent.this.sendingSidabled ? CoolComponent.this.getSourceEnergy() : 0.0D;
        }

        @Override
        public boolean isAllowed() {
            return CoolComponent.this.allow;
        }

        @Override
        public void setAllowed(final boolean allowed) {
            CoolComponent.this.allow = allowed;
        }

        @Override
        public TileEntity getTile() {
            return CoolComponent.this.parent;
        }

    }

}
