package com.denfop.componets;

import com.denfop.api.pressure.*;
import com.denfop.api.pressure.event.PressureTileLoadEvent;
import com.denfop.api.pressure.event.PressureTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class PressureComponent extends AbstractComponent {

    public final Level world;
    public final boolean fullEnergy;
    private final double defaultCapacity;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
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
    Map<Direction, IPressureTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IPressureTile>> validReceivers = new LinkedList<>();
    private long id;

    public PressureComponent(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public PressureComponent(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public PressureComponent(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
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
        this.world = parent.getLevel();
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

    public void readFromNbt(CompoundTag nbt) {
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

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.storage);
        ret.putDouble("capacity", this.capacity);
        ret.putBoolean("need", this.need);
        ret.putBoolean("allow", this.allow);
        ret.putBoolean("auto", this.auto);
        return ret;
    }

    public void onLoaded() {
        assert this.delegate == null;
        if (this.capacity < this.defaultCapacity) {
            this.capacity = this.defaultCapacity;
        }
        if (!this.parent.getLevel().isClientSide) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {

            } else {


                this.createDelegate();
                NeoForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, this.parent.getLevel()));
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
                this.delegate = new PressureComponent.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new PressureComponent.EnergyNetDelegateSink();
            }
            if (delegate == null) {
                return;
            }
            this.delegate.setLevel(this.parent.getLevel());
        }
    }

    @Override
    public boolean onBlockActivated(Player player, InteractionHand hand) {
        super.onBlockActivated(player, hand);
        return false;
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            NeoForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, this.parent.getLevel()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16, player.registryAccess());
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeBoolean(this.need);
        buffer.writeBoolean(this.allow);
        buffer.writeBoolean(this.auto);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.need = is.readBoolean();
        this.allow = is.readBoolean();
        this.auto = is.readBoolean();
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
        if (this.parent.getLevel().getGameTime() % 120 == 0) {
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

    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;

            NeoForge.EVENT_BUS.post(new PressureTileUnloadEvent(this.delegate, this.world));
        }

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;

            NeoForge.EVENT_BUS.post(new PressureTileLoadEvent(this.delegate, this.world));
        }


    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public Map<Direction, IPressureTile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(IPressureTile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IPressureTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IPressureTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<IPressureTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(IPressureTile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));

        }
    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public IPressureTile getDelegate() {
        return this.delegate;
    }


    private abstract class EnergyNetDelegate extends BlockEntity implements IPressureTile {

        private int hashCodeSource;

        private EnergyNetDelegate() {
            super(PressureComponent.this.parent.getType(), PressureComponent.this.parent.getBlockPos(), PressureComponent.this.parent.getBlockState());

        }

        public long getIdNetwork() {
            return PressureComponent.this.getIdNetwork();
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
            PressureComponent.this.setId(id);
        }

        @Override
        public void AddTile(final IPressureTile tile, final Direction dir) {
            PressureComponent.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IPressureTile tile, final Direction dir) {
            PressureComponent.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<Direction, IPressureTile> getTiles() {
            return PressureComponent.this.energyConductorMap;
        }

        @Override
        public List<InfoTile<IPressureTile>> getValidReceivers() {
            return validReceivers;
        }
    }

    private class EnergyNetDelegateSink extends PressureComponent.EnergyNetDelegate implements IPressureSink {

        List<IPressureSource> systemTicks = new LinkedList<>();

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return PressureComponent.this.sinkTier;
        }

        public boolean acceptsPressureFrom(IPressureEmitter emitter, Direction dir) {
            return PressureComponent.this.sinkDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getPos() {
            return PressureComponent.this.parent.getBlockPos();
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

        @Override
        public List<IPressureSource> getEnergyTickList() {
            return systemTicks;
        }

        public void setPressureStored(double amount) {
            if (PressureComponent.this.storage < amount) {
                PressureComponent.this.storage = amount;
            }
        }

        @Override
        public BlockEntity getTile() {
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

        public boolean emitsPressureTo(IPressureAcceptor receiver, Direction dir) {
            return PressureComponent.this.sourceDirections.contains(dir);
        }

        public double getOfferedPressure() {

            return PressureComponent.this.storage;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return PressureComponent.this.parent.getBlockPos();
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
        public BlockEntity getTile() {
            return PressureComponent.this.parent;
        }

    }

}
