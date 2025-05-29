package com.denfop.componets;

import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class ComponentMiniPanel extends AbstractComponent {

    public final boolean fullEnergy;
    private final double defaultCapacity;
    public double tick;
    public double capacity;
    public double storage;
    public int sourceTier;
    public int defaultSourceTier;
    public Set<Direction> sourceDirections;
    public boolean multiSource;
    public ComponentMiniPanel.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    protected double pastEnergy;
    protected double perenergy;
    protected double bonusCapacity;
    protected double bonusProdution;
    Map<BlockPos, IEnergyStorage> energyStorageMap = new HashMap<>();
    List<InfoTile<IEnergyTile>> validReceivers = new LinkedList<>();
    Map<Direction, IEnergyTile> energyConductorMap = new HashMap<>();
    private double prodution;
    private ChunkPos chunkPos;

    public ComponentMiniPanel(TileEntityMiniPanels parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), 1);
    }


    public ComponentMiniPanel(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sourceDirections, tier, false);
    }

    public ComponentMiniPanel(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sourceDirections,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(parent);

        this.multiSource = false;
        this.capacity = capacity;
        this.sourceTier = sourceTier;
        this.sourceDirections = sourceDirections == null ? Collections.emptySet() : sourceDirections;
        this.fullEnergy = fullEnergy;
        this.pastEnergy = 0;
        this.perenergy = 0;
        this.tick = 0;
        this.defaultSourceTier = sourceTier;
        this.defaultCapacity = capacity;
        this.prodution = 0;
    }

    public static ComponentMiniPanel asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static ComponentMiniPanel asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentMiniPanel(parent, capacity, ModUtils.allFacings, tier);
    }

    public double getBonusCapacity() {
        return bonusCapacity;
    }

    public void setBonusCapacity(final double bonusCapacity) {
        this.bonusCapacity = bonusCapacity;
    }

    public double getBonusProdution() {
        return bonusProdution;
    }

    public void setBonusProdution(final double bonusProdution) {
        this.bonusProdution = bonusProdution;
    }

    @Override
    public void onNeighborChange(final BlockState srcBlock, final BlockPos srcPos) {
        BlockEntity tile = this.getParent().getWorld().getBlockEntity(srcPos);
        boolean hasElement = this.energyStorageMap.containsKey(srcPos);
        if (srcBlock.getBlock() == Blocks.AIR && hasElement) {
            this.energyStorageMap.remove(srcPos);
        } else if (hasElement) {
            this.energyStorageMap.remove(srcPos);
        }
        if (tile instanceof TileEntityInventory) {
            return;
        }
        if (tile == null) {
            return;
        }

    }

    @Override
    public void updateEntityServer() {
        if (!this.energyStorageMap.isEmpty() && this.getDelegate() != null && !this.sourceDirections.isEmpty()) {
            for (Map.Entry<BlockPos, IEnergyStorage> iEnergyStorageEntry : this.energyStorageMap.entrySet()) {
                this.useEnergy(4 * iEnergyStorageEntry.getValue().receiveEnergy(
                        (int) Math.min(Math.min(
                                this.getEnergy() / 4,
                                Integer.MAX_VALUE - 1
                        ), ((IEnergySource) this.getDelegate()).canExtractEnergy() / 4),
                        false
                ));
                if (this.getEnergy() <= 0) {
                    break;
                }
            }
        }
    }

    @Override
    public boolean isServer() {
        return true;
    }

    public void readFromNbt(CompoundTag nbt) {
        this.storage = nbt.getDouble("storage");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.storage);

        return ret;
    }

    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return validReceivers;
    }

    public void onLoaded() {
        if (this.capacity < this.defaultCapacity) {
            this.capacity = this.defaultCapacity;
        }
        if (!this.parent.getWorld().isClientSide) {
            for (Direction facing : Direction.values()) {
                final BlockPos srcPos = this.parent.getPos().offset(facing.getNormal());
                BlockEntity tile = this.getParent().getWorld().getBlockEntity(srcPos);
                boolean hasElement = this.energyStorageMap.containsKey(srcPos);
                if (hasElement) {
                    continue;
                }
                if (tile instanceof TileEntityInventory) {
                    continue;
                }
                if (tile == null) {
                    continue;
                }
            }
        }
        assert this.delegate == null;

        if (!this.parent.getWorld().isClientSide) {
            if (!(this.sourceDirections.isEmpty())) {
                this.energyConductorMap.clear();
                validReceivers.clear();
                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
            }
            this.loaded = true;
        }

    }

    public int getComparatorValue() {
        return Math.min((int) (this.storage * 15.0 / this.capacity), 15);
    }

    private void createDelegate() {
        if (this.delegate != null) {
        } else {


            this.delegate = new ComponentMiniPanel.EnergyNetDelegateSource();


            this.delegate.setLevel(this.parent.getWorld());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
            this.delegate = null;
        }
        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.bonusCapacity);
        buffer.writeDouble(this.bonusProdution);
        buffer.writeDouble(this.prodution);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer buffer = super.updateComponent();
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.bonusCapacity);
        buffer.writeDouble(this.bonusProdution);
        buffer.writeDouble(this.prodution);
        return buffer;
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {

        this.capacity = is.readDouble();
        this.storage = is.readDouble();
        this.bonusCapacity = is.readDouble();
        this.bonusProdution = is.readDouble();
        this.prodution = is.readDouble();
    }

    public double getCapacity() {
        return this.capacity * (1 + this.bonusCapacity);
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
        this.storage = Math.min(this.capacity, this.storage);
    }

    public double getProdution() {
        return prodution * (1 + this.bonusProdution);
    }

    public void setProdution(double prodution) {
        this.prodution = prodution;
    }

    public double getStorage() {
        return storage;
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
        return this.storage / this.getCapacity();
    }

    public double addEnergy(double amount) {
        amount = Math.min(this.getCapacity() - this.storage, amount);
        this.storage += amount;
        this.storage = Math.min(this.storage, this.getCapacity());
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

    public void setDirections(Set<Direction> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getWorld().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
        }

        this.sourceDirections = sourceDirections;
        if (sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }
        if (this.delegate != null) {


            assert !this.parent.getWorld().isClientSide;

            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
        }


    }

    public ChunkPos getChunkPos() {
        if (this.chunkPos == null) {
            this.chunkPos = new ChunkPos(getParent().getPos().getX() >> 4, getParent().getPos().getZ() >> 4);
        }
        return chunkPos;
    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public IEnergyTile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        return Math.min(this.storage, this.getProdution());
    }

    public void RemoveTile(IEnergyTile tile, final Direction facing1) {
        if (!parent.getWorld().isClientSide) {
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

    public void AddTile(IEnergyTile tile, final Direction facing1) {
        if (!parent.getWorld().isClientSide) {
            this.energyConductorMap.put(facing1, tile);
            validReceivers.add(new InfoTile<>(tile, facing1.getOpposite()));
        }
    }

    public Map<Direction, IEnergyTile> getTiles() {
        return energyConductorMap;
    }

    private abstract class EnergyNetDelegate extends BlockEntity implements IEnergyTile {

        private EnergyNetDelegate() {
            super(ComponentMiniPanel.this.parent.getType(), ComponentMiniPanel.this.parent.getBlockPos(), ComponentMiniPanel.this.parent.getBlockState());
        }

    }

    private class EnergyNetDelegateSource extends ComponentMiniPanel.EnergyNetDelegate implements IEnergySource {


        int hashCodeSource;
        private long id;

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

        public long getIdNetwork() {
            return this.id;
        }

        public void setId(final long id) {
            this.id = id;
        }

        public List<InfoTile<IEnergyTile>> getValidReceivers() {
            return validReceivers;
        }

        public void RemoveTile(IEnergyTile tile, final Direction facing1) {
            if (!parent.getWorld().isClientSide) {
                ComponentMiniPanel.this.RemoveTile(tile, facing1);

            }
        }


        public void AddTile(IEnergyTile tile, final Direction facing1) {
            if (!parent.getWorld().isClientSide) {
                ComponentMiniPanel.this.AddTile(tile, facing1);
            }
        }

        public Map<Direction, IEnergyTile> getTiles() {
            return ComponentMiniPanel.this.energyConductorMap;
        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return ComponentMiniPanel.this.parent.getPos();
        }

        public int getSourceTier() {
            return ComponentMiniPanel.this.sourceTier;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, Direction dir) {
            return ComponentMiniPanel.this.sourceDirections.contains(dir);
        }

        public double canExtractEnergy() {

            return !ComponentMiniPanel.this.sendingSidabled ? ComponentMiniPanel.this.getSourceEnergy() : 0.0D;
        }


        public void extractEnergy(double amount) {
            assert amount <= ComponentMiniPanel.this.storage;

            ComponentMiniPanel.this.storage = ComponentMiniPanel.this.storage - amount;
        }

        @Override
        public double getPerEnergy() {
            return ComponentMiniPanel.this.perenergy;
        }

        @Override
        public double getPastEnergy() {
            return ComponentMiniPanel.this.pastEnergy;
        }

        @Override
        public void setPastEnergy(final double pastEnergy) {
            ComponentMiniPanel.this.pastEnergy = pastEnergy;
        }

        @Override
        public void addPerEnergy(final double setEnergy) {
            ComponentMiniPanel.this.perenergy += setEnergy;
        }

        @Override
        public boolean isSource() {
            return true;
        }

        @Override
        public BlockEntity getTileEntity() {
            return this;
        }


        @Override
        public BlockPos getPos() {
            return ComponentMiniPanel.this.parent.getPos();
        }

    }


}
