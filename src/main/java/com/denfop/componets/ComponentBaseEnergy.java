package com.denfop.componets;


import com.denfop.api.sytem.*;
import com.denfop.invslot.InvSlot;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateRadiationValue;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class ComponentBaseEnergy extends AbstractComponent {


    public final boolean fullEnergy;
    private final EnergyType type;
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
    public ComponentBaseEnergy.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public double tick;
    protected double pastEnergy;
    protected double perenergy;
    Map<Direction, ITile> energyConductorMap = new HashMap<>();
    List<InfoTile<ITile>> validReceivers = new LinkedList<>();
    private double perenergy1;
    private double pastEnergy1;
    private double tick1;
    private long id;

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
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentBaseEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
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
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
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

    public void readFromNbt(CompoundTag nbt) {
        this.storage = nbt.getDouble("storage");
        this.capacity = nbt.getDouble("capacity");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.storage);
        ret.putDouble("capacity", this.capacity);
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
        if (!this.parent.getLevel().isClientSide) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {

            } else {


                this.createDelegate();
                this.energyConductorMap.clear();
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
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateSource();
            } else if (this.sourceDirections.isEmpty()) {
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateSink();
            } else {
                this.delegate = new ComponentBaseEnergy.EnergyNetDelegateDual();
            }

            this.delegate.setLevel(this.parent.getLevel());
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
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        return buffer;
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
        if (this.storage > this.capacity) {
            this.storage = capacity;
        }
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

    public void blockBreak() {
        if (this.getType() == EnergyType.RADIATION) {
            new PacketUpdateRadiationValue(new ChunkPos(this.parent.getBlockPos()), (int) this.storage);
        } else if (this.getType() == EnergyType.EXPERIENCE && this.storage > 0) {
            if (this.parent.getLevel() instanceof ServerLevel serverLevel) {
                double f = 0.7;
                double dx = serverLevel.random.nextDouble() * 1 + (1.0 - f) * 0.5;
                double dy = serverLevel.random.nextDouble() * f + (1.0 - f) * 0.5;
                double dz = serverLevel.random.nextDouble() * f + (1.0 - f) * 0.5;

                int xpAmount = ExperienceOrb.getExperienceValue((int) storage);
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

    public Map<Direction, ITile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<ITile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<ITile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<ITile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(EnergyType type, ITile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));

        }
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

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
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

        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;
            this.energyConductorMap.clear();

            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.UNLOAD, this.type,
                    this.delegate
            ));
        }

        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.energyConductorMap.clear();
            this.createDelegate();
        }

        if (this.delegate != null) {

            assert !this.parent.getLevel().isClientSide;
            this.energyConductorMap.clear();

            MinecraftForge.EVENT_BUS.post(new EnergyEvent(this.parent.getLevel(), EnumTypeEvent.LOAD, this.type,
                    this.delegate
            ));
        }


    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
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

    private abstract class EnergyNetDelegate extends BlockEntity implements ITile {

        private EnergyNetDelegate() {
            super(ComponentBaseEnergy.this.parent.getType(), ComponentBaseEnergy.this.parent.getBlockPos(), ComponentBaseEnergy.this.parent.getBlockState());
        }

    }

    private class EnergyNetDelegateDual extends ComponentBaseEnergy.EnergyNetDelegate implements IDual {
        List<ISource> systemTicks = new LinkedList<>();
        int hashCodeSource;
        boolean hasHashCode = false;
        private int hashCode;

        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsFrom(IEmitter emitter, Direction dir) {
            return ComponentBaseEnergy.this.sinkDirections.contains(dir);
        }

        public boolean emitsTo(IAcceptor receiver, Direction dir) {
            return ComponentBaseEnergy.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getPos() {
            return ComponentBaseEnergy.this.parent.getBlockPos();
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
            ComponentBaseEnergy.this.addEnergy(var2);
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

            ComponentBaseEnergy.this.useEnergy(amount);
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
        public List<ISource> getEnergyTickList() {
            return systemTicks;
        }

        public long getIdNetwork() {
            return ComponentBaseEnergy.this.getIdNetwork();
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
            ComponentBaseEnergy.this.setId(id);
        }

        @Override
        public void AddTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.AddTile(type, tile, dir);
        }

        @Override
        public void RemoveTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.RemoveTile(type, tile, dir);
        }

        @Override
        public Map<Direction, ITile> getTiles(EnergyType energyType) {
            return ComponentBaseEnergy.this.energyConductorMap;
        }

        @Override
        public List<InfoTile<ITile>> getValidReceivers(final EnergyType energyType) {
            return validReceivers;
        }

        @Override
        public BlockEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

    }

    private class EnergyNetDelegateSink extends ComponentBaseEnergy.EnergyNetDelegate implements ISink {

        int hashCodeSource;
        boolean hasHashCode = false;
        List<ISource> systemTicks = new LinkedList<>();
        private int hashCode;

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return ComponentBaseEnergy.this.sinkTier;
        }

        public boolean acceptsFrom(IEmitter emitter, Direction dir) {
            return ComponentBaseEnergy.this.sinkDirections.contains(dir);
        }

        public long getIdNetwork() {
            return ComponentBaseEnergy.this.getIdNetwork();
        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }

        @Override
        public List<ISource> getEnergyTickList() {
            return systemTicks;
        }

        public void setId(final long id) {
            ComponentBaseEnergy.this.setId(id);
        }

        @Override
        public void AddTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.AddTile(type, tile, dir);
        }

        @Override
        public void RemoveTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.RemoveTile(type, tile, dir);
        }

        @Override
        public Map<Direction, ITile> getTiles(EnergyType energyType) {
            return ComponentBaseEnergy.this.energyConductorMap;
        }

        @Override
        public List<InfoTile<ITile>> getValidReceivers(final EnergyType energyType) {
            return validReceivers;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return ComponentBaseEnergy.this.parent.getBlockPos();
        }

        public double getDemanded() {
            assert !ComponentBaseEnergy.this.sinkDirections.isEmpty();

            return !ComponentBaseEnergy.this.receivingDisabled && ComponentBaseEnergy.this.storage < ComponentBaseEnergy.this.capacity
                    ? ComponentBaseEnergy.this.capacity - ComponentBaseEnergy.this.storage
                    : 0.0D;
        }

        @Override
        public BlockEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

        public void receivedEnergy(double amount) {
            ComponentBaseEnergy.this.addEnergy(amount);
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

        int hashCodeSource;
        boolean hasHashCode = false;
        private int hashCode;

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return ComponentBaseEnergy.this.sourceTier;
        }

        public boolean emitsTo(IAcceptor receiver, Direction dir) {
            return ComponentBaseEnergy.this.sourceDirections.contains(dir);
        }

        public long getIdNetwork() {
            return ComponentBaseEnergy.this.getIdNetwork();
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
            ComponentBaseEnergy.this.setId(id);
        }

        @Override
        public void AddTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.AddTile(type, tile, dir);
        }

        @Override
        public void RemoveTile(EnergyType type, final ITile tile, final Direction dir) {
            ComponentBaseEnergy.this.RemoveTile(type, tile, dir);
        }

        @Override
        public Map<Direction, ITile> getTiles(EnergyType energyType) {
            return ComponentBaseEnergy.this.energyConductorMap;
        }

        @Override
        public List<InfoTile<ITile>> getValidReceivers(final EnergyType energyType) {
            return validReceivers;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return ComponentBaseEnergy.this.parent.getBlockPos();
        }

        public double canProvideEnergy() {
            assert !ComponentBaseEnergy.this.sourceDirections.isEmpty();

            return !ComponentBaseEnergy.this.sendingSidabled ? ComponentBaseEnergy.this.getSourceEnergy() : 0.0D;
        }

        @Override
        public BlockEntity getTile() {
            return ComponentBaseEnergy.this.parent;
        }

        public void extractEnergy(double amount) {
            assert amount <= ComponentBaseEnergy.this.storage;
            ComponentBaseEnergy.this.useEnergy(amount);
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
