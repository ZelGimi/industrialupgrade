package com.denfop.componets;

import com.denfop.api.energy.*;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class Energy extends AbstractComponent {

    public final boolean fullEnergy;
    public final double defaultCapacity;
    public double tick;
    public boolean upgrade;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;

    public int defaultSinkTier;
    public int defaultSourceTier;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public List<InvSlot> managedSlots = new ArrayList<>();
    public boolean multiSource;
    public int sourcePackets;
    public Energy.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean limit;
    public double limit_amount = 0;
    protected double pastEnergy;
    protected double perenergy;
    protected double pastEnergy1;
    protected double perenergy1;
    Map<BlockPos, IEnergyStorage> energyStorageMap = new HashMap<>();
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    private ChunkPos chunkPos;
    private long id;

    public Energy(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public Energy(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public Energy(
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
        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : sinkDirections;
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
    }

    public Energy(
            TileEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
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
        this.sinkDirections = sinkDirections == null ? Collections.emptySet() : new HashSet<>(sinkDirections);
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : new HashSet<>(sourceDirections);
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.defaultSinkTier = sinkTier;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static Energy asBasicSink(TileEntityInventory parent, double capacity, boolean meta) {
        return new Energy(parent, capacity, ModUtils.allFacings, Collections.emptySet(), 14, 14, false);
    }

    public static Energy asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static Energy asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new Energy(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    @Override
    public void onNeighborChange(BlockState srcBlock, BlockPos srcPos) {
        super.onNeighborChange(srcBlock, srcPos);
    }

    @Override
    public void updateEntityServer() {

        if (!managedSlots.isEmpty()) {
            for (InvSlot slot : managedSlots) {
                if (slot instanceof InvSlotDischarge) {
                    InvSlotDischarge discharge = (InvSlotDischarge) slot;
                    if (!discharge.isEmpty()) {
                        if (discharge.get(0).getItem() == Items.REDSTONE) {
                            double energy = discharge.dischargeWithRedstone(this.capacity, this.getFreeEnergy());
                            this.addEnergy(energy);
                        } else {
                            if (this.getFreeEnergy() > 0) {
                                 double energy = discharge.discharge(this.getFreeEnergy(), false);
                                this.addEnergy(energy);
                            }
                        }
                    }
                } else if (slot instanceof InvSlotCharge) {
                    InvSlotCharge charge = (InvSlotCharge) slot;
                    if (!charge.isEmpty()) {
                        double energy = charge.charge(this.storage);
                        this.useEnergy(energy);
                    }
                }
            }
        }
    }

    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            this.chunkPos = new ChunkPos(getParent().getBlockPos());
        }
        return chunkPos;
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public Energy addManagedSlot(InvSlot slot) {
        if (this.managedSlots == null) {
            this.managedSlots = new ArrayList<>(4);
        }

        this.managedSlots.add(slot);
        return this;
    }

    public Map<Direction, IEnergyTile> getConductors() {
        return energyConductorMap;
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            this.energyConductorMap.remove(facing1);
            final Iterator<InfoTile<IEnergyTile>> iter = validReceivers.iterator();
            while (iter.hasNext()) {
                InfoTile<IEnergyTile> tileInfoTile = iter.next();
                if (tileInfoTile.tileEntity == tile) {
                    iter.remove();
                    break;
                }
            }
        }
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!this.parent.getLevel().isClientSide) {
            if (!this.energyConductorMap.containsKey(facing1)) {
                this.energyConductorMap.put(facing1, tile);
                validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
            }
        }
    }

    public void readFromNbt(CompoundTag nbt) {
        this.storage = nbt.getDouble("storage");
        this.limit_amount = nbt.getDouble("limit_amount");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.storage);
        ret.putDouble("limit_amount", this.limit_amount);

        return ret;
    }

    public void onLoaded() {


        assert this.delegate == null;

        if (!this.parent.getLevel().isClientSide) {
            if (!(this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty())) {
                this.createDelegate();
                this.energyConductorMap.clear();
                validReceivers.clear();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
            }
            this.loaded = true;
        }

    }

    public int getComparatorValue() {
        return Math.min((int) (this.storage * 15.0 / this.capacity), 15);
    }

    public void setLimit(final boolean limit) {
        this.limit = limit;
    }

    public void createDelegate() {
        if (this.delegate == null) {
            if (!this.multiSource) {
                if (this.sinkDirections.isEmpty()) {
                    this.delegate = new Energy.EnergyNetDelegateSource();
                } else if (this.sourceDirections.isEmpty()) {
                    this.delegate = new Energy.EnergyNetDelegateSink();
                } else {

                    this.delegate = new Energy.EnergyNetDelegateDual();
                }
            } else {
                this.delegate = new Energy.EnergyNetDelegateMultiDual();
            }


            this.delegate.setLevel(this.parent.getLevel());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
            this.delegate = null;
        }
        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.limit_amount);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.limit_amount);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        limit_amount = is.readDouble();

    }

    public double getDefaultCapacity() {
        return defaultCapacity;
    }

    public double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
        this.storage = Math.min(this.capacity, this.storage);
    }

    public void addCapacity(double capacity) {
        this.capacity += capacity;
    }

    public double getEnergy() {
        return this.storage;
    }

    public double getFreeEnergy() {
        return Math.max(0.0D, this.capacity - this.storage);
    }

    public double getFillRatio() {
        if (this.storage > this.capacity) {
            this.storage = this.capacity;
        }
        return this.storage / this.capacity;
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.capacity - this.storage, amount);
        this.storage += amount;
        this.storage = Math.min(this.storage, this.capacity);
        return amount;
    }

    public void forceAddEnergy(double amount) {
        this.storage += amount;
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

    public void setOverclockRates(InvSlotUpgrade invSlotUpgrade) {
        if (this.getDelegate() instanceof IEnergySink) {
            int tier = invSlotUpgrade.getTier(this.defaultSinkTier);
            this.setSinkTier(tier);
            for (InvSlot slot : this.managedSlots) {
                if (slot instanceof InvSlotDischarge) {
                    InvSlotDischarge discharge = (InvSlotDischarge) slot;
                    discharge.setTier(tier);
                }
            }
        }
        if (this.getDelegate() instanceof IEnergySource) {
            int tier = invSlotUpgrade.getTier(this.defaultSourceTier);
            this.setSourceTier(tier);
            for (InvSlot slot : this.managedSlots) {
                if (slot instanceof InvSlotCharge) {
                    InvSlotCharge discharge = (InvSlotCharge) slot;
                    discharge.setTier(tier);
                }
            }
        }
        this.setCapacity(invSlotUpgrade.getEnergyStorage(
                this.defaultCapacity
        ));
    }

    public int getSinkTier() {
        return this.sinkTier;
    }

    public void setSinkTier(int tier) {
        this.sinkTier = tier;
        for (InvSlot slot : this.managedSlots) {
            if (slot instanceof InvSlotDischarge) {
                InvSlotDischarge discharge = (InvSlotDischarge) slot;
                discharge.setTier(tier);
            }
        }
    }

    public int getSourceTier() {
        return this.sourceTier;
    }

    public void setSourceTier(int tier) {
        for (InvSlot slot : this.managedSlots) {
            if (slot instanceof InvSlotCharge) {
                InvSlotCharge discharge = (InvSlotCharge) slot;
                discharge.setTier(tier);
            }
        }
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

    public Energy setMultiSource(boolean multiSource) {
        this.multiSource = multiSource;
        if (!multiSource) {
            this.sourcePackets = 1;
        }

        return this;
    }

    public void setPacketOutput(int number) {
        if (this.multiSource) {
            this.sourcePackets = number;
        }

    }

    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
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
            this.energyConductorMap.clear();
            validReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
        }


    }

    public long getIdNetwork() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setDirections(List<Direction> sinkDirections, List<Direction> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getLevel().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getLevel(), this.delegate));
        }

        this.sinkDirections = new HashSet<>(sinkDirections);
        this.sourceDirections = new HashSet<>(sourceDirections);
        if (sinkDirections.isEmpty() && sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.energyConductorMap.clear();
            this.createDelegate();
        }
        if (this.delegate != null) {


            this.energyConductorMap.clear();
            assert !this.parent.getLevel().isClientSide;
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getLevel(), this.delegate));
        }


    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public IEnergyTile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        if (!limit) {
            return Math.min(this.storage, EnergyNetGlobal.instance.getPowerFromTier(this.sourceTier));
        } else {

            return Math.min(this.storage, this.limit_amount);
        }
    }


    private abstract class EnergyNetDelegate extends BlockEntity implements IEnergyTile {

        private EnergyNetDelegate() {
            super(Energy.this.parent.getType(), Energy.this.parent.getBlockPos(), Energy.this.parent.getBlockState());

        }

    }

    private class EnergyNetDelegateDual extends Energy.EnergyNetDelegate implements IDual {


        int hashCodeSource;
        boolean hasHashCode = false;
        List<Integer> energyTicks = new LinkedList<>();
        private int hashCode;

        private EnergyNetDelegateDual() {
            super();
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction dir) {
            for (Direction facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
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
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
            for (Direction facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return Energy.this.parent.getBlockPos();
        }

        @Override
        public void AddTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<Direction, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        @Override
        public BlockEntity getTileEntity() {
            return Energy.this.parent;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled && Energy.this.storage < Energy.this.capacity
                    ? Energy.this.capacity - Energy.this.storage
                    : 0.0D;
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled
                    ? Energy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage = Energy.this.storage + amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }


        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return !Energy.this.sendingSidabled;
        }


        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return !Energy.this.receivingDisabled;
        }


        @Override
        public double getPerEnergy1() {
            return Energy.this.perenergy1;
        }

        @Override
        public double getPastEnergy1() {
            return Energy.this.pastEnergy1;
        }

        @Override
        public void setPastEnergy1(final double pastEnergy) {
            Energy.this.pastEnergy1 = pastEnergy;
        }

        @Override
        public void addPerEnergy1(final double setEnergy) {
            Energy.this.perenergy1 += setEnergy;
        }


    }

    private class EnergyNetDelegateMultiDual extends Energy.EnergyNetDelegate implements IMultiDual {


        int hashCodeSource;
        boolean hasHashCode = false;
        List<Integer> energyTicks = new ArrayList<>();
        private int hashCode;

        private EnergyNetDelegateMultiDual() {
            super();
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction dir) {
            for (Direction facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }

        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        @Override
        public void AddTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<Direction, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
            for (Direction facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return Energy.this.parent.getBlockPos();
        }

        @Override
        public BlockEntity getTileEntity() {
            return Energy.this.parent;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled && Energy.this.storage < Energy.this.capacity
                    ? Energy.this.capacity - Energy.this.storage
                    : 0.0D;
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled
                    ? Energy.this.getSourceEnergy()
                    : 0.0D;
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage = Energy.this.storage + amount;
        }

        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return !Energy.this.sendingSidabled;
        }


        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return !Energy.this.receivingDisabled;
        }


        @Override
        public double getPerEnergy1() {
            return Energy.this.perenergy1;
        }

        @Override
        public double getPastEnergy1() {
            return Energy.this.pastEnergy1;
        }

        @Override
        public void setPastEnergy1(final double pastEnergy) {
            Energy.this.pastEnergy1 = pastEnergy;
        }

        @Override
        public void addPerEnergy1(final double setEnergy) {
            Energy.this.perenergy1 += setEnergy;
        }


    }

    private class EnergyNetDelegateSink extends Energy.EnergyNetDelegate implements IEnergySink {

        boolean hasHashCode = false;
        int hashCodeSource;
        List<Integer> energyTicks = new LinkedList<>();
        private int hashCode;


        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return Energy.this.sinkTier;
        }

        public boolean acceptsEnergyFrom(IEnergyEmitter emitter, Direction dir) {
            for (Direction facing1 : Energy.this.sinkDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }

        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
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
        public void AddTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<Direction, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        public double getDemandedEnergy() {
            return !Energy.this.receivingDisabled ? Energy.this.capacity - Energy.this.storage : 0;
        }

        public void receiveEnergy(double amount) {
            Energy.this.storage += amount;
        }

        @Override
        public List<Integer> getEnergyTickList() {
            return energyTicks;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public void addTick(final double tick) {
            Energy.this.tick = tick;
        }

        @Override
        public double getTick() {
            return Energy.this.tick;
        }

        @Override
        public boolean isSink() {
            return true;
        }

        @Override
        public BlockEntity getTileEntity() {
            return Energy.this.parent;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return Energy.this.parent.getBlockPos();
        }

    }

    private class EnergyNetDelegateSource extends Energy.EnergyNetDelegate implements IEnergySource {


        int hashCodeSource;
        boolean hasHashCode = false;
        private int hashCode;

        private EnergyNetDelegateSource() {
            super();

        }

        @Override
        public int getHashCodeSource() {
            return hashCodeSource;
        }

        @Override
        public void setHashCodeSource(final int hashCode) {
            hashCodeSource = hashCode;
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        @Override
        public void AddTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.AddTile(tile, dir);
        }

        @Override
        public void RemoveTile(final IEnergyTile tile, final Direction dir) {
            Energy.this.RemoveTile(tile, dir);
        }

        @Override
        public Map<Direction, IEnergyTile> getTiles() {
            return Energy.this.energyConductorMap;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return Energy.this.parent.getBlockPos();
        }

        public int getSourceTier() {
            return Energy.this.sourceTier;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
            for (Direction facing1 : Energy.this.sourceDirections) {
                if (facing1.ordinal() == dir.ordinal()) {
                    return true;
                }
            }
            return false;
        }

        public long getIdNetwork() {
            return Energy.this.getIdNetwork();
        }


        public void setId(final long id) {
            Energy.this.setId(id);
        }

        public double canExtractEnergy() {

            return !Energy.this.sendingSidabled ? Energy.this.getSourceEnergy() : 0.0D;
        }


        public void extractEnergy(double amount) {
            assert amount <= Energy.this.storage;

            Energy.this.storage = Energy.this.storage - amount;
        }

        @Override
        public double getPerEnergy() {
            return Energy.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return Energy.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            Energy.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            Energy.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }

        @Override
        public BlockEntity getTileEntity() {
            return this;
        }

    }


}
