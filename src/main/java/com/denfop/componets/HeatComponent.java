package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.heat.*;
import com.denfop.api.heat.event.HeatTileLoadEvent;
import com.denfop.api.heat.event.HeatTileUnloadEvent;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class HeatComponent extends AbstractComponent {

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
    public HeatComponent.EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean receivingDisabled;
    public boolean sendingSidabled;
    public boolean need;
    public boolean auto;
    public boolean allow;
    Random rand = new Random();
    Map<Direction, IHeatTile> energyHeatConductorMap = new HashMap<>();
    List<InfoTile<IHeatTile>> validHeatReceivers = new LinkedList<>();
    private double coef;

    public HeatComponent(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public HeatComponent(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public HeatComponent(
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
        this.coef = 0;
    }

    public static HeatComponent asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static HeatComponent asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static HeatComponent asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static HeatComponent asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
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

                this.validHeatReceivers.clear();
                this.energyHeatConductorMap.clear();
                MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this.delegate, this.parent.getLevel()));
            }

            this.loaded = true;
            Level world = parent.getLevel();


            BlockPos pos = parent.getBlockPos();

            Biome biome = world.getBiome(pos).value();
            if (biome.coldEnoughToSnow(pos)) {
                coef = -1;
            } else if (biome.shouldSnowGolemBurn(pos)) {
                coef = 1;
            } else {
                coef = 0;
            }
        }

    }

    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    public boolean canUsePurifier(Player player) {
        return this.auto;
    }

    public ItemStack getItemStackUpgrade() {
        this.auto = false;
        return new ItemStack(IUItem.autoheater.getItem());
    }

    public void createDelegate() {
        if (this.delegate != null) {
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
            this.delegate.setLevel(this.parent.getLevel());
        }
    }

    @Override
    public List<ItemStack> getDrops() {
        final List<ItemStack> ret = super.getDrops();
        if (this.auto) {
            ret.add(new ItemStack(IUItem.autoheater.getItem()));
        }
        return ret;
    }

    @Override
    public boolean onBlockActivated(Player player, InteractionHand hand) {
        super.onBlockActivated(player, hand);
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem().equals(IUItem.autoheater.getItem()) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
            return true;
        }
        return false;
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new HeatTileUnloadEvent(this.delegate, this.parent.getLevel()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    public void onContainerUpdate(ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
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
        if (this.coef != 0 && rand.nextInt(2) == 1) {
            this.storage += 7 * this.coef;
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

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.auto) {
            if (this.getEnergy() + 1 <= this.getCapacity()) {
                this.addEnergy(2);
            }
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


    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;

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


            assert !this.parent.getLevel().isClientSide;
            this.validHeatReceivers.clear();
            this.energyHeatConductorMap.clear();
            MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this.delegate, this.world));
        }


    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public IHeatTile getDelegate() {
        return this.delegate;
    }


    private abstract class EnergyNetDelegate extends BlockEntity implements IHeatTile {

        private EnergyNetDelegate() {
            super(HeatComponent.this.parent.getType(), HeatComponent.this.parent.getBlockPos(), HeatComponent.this.parent.getBlockState());
        }

    }

    private class EnergyNetDelegateSink extends HeatComponent.EnergyNetDelegate implements IHeatSink {

        List<IHeatSource> list = new LinkedList<>();
        int hashCodeSource;
        private long id;

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return HeatComponent.this.sinkTier;
        }

        public boolean acceptsHeatFrom(IHeatEmitter emitter, Direction dir) {
            return HeatComponent.this.sinkDirections.contains(dir);
        }

        @Override
        public List<IHeatSource> getEnergyTickList() {
            return list;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return HeatComponent.this.parent.getBlockPos();
        }

        public double getDemandedHeat() {

            return HeatComponent.this.capacity;
        }

        public long getIdNetwork() {
            return this.id;
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
            this.id = id;
        }

        @Override
        public void AddHeatTile(final IHeatTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyHeatConductorMap.put(dir, tile);
                validHeatReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
        }

        @Override
        public void RemoveHeatTile(final IHeatTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyHeatConductorMap.remove(dir);
                final Iterator<InfoTile<IHeatTile>> iter = validHeatReceivers.iterator();
                while (iter.hasNext()) {
                    InfoTile<IHeatTile> tileInfoTile = iter.next();
                    if (tileInfoTile.tileEntity == tile) {
                        iter.remove();
                        break;
                    }
                }
            }
        }

        @Override
        public Map<Direction, IHeatTile> getHeatTiles() {
            return energyHeatConductorMap;
        }

        @Override
        public List<InfoTile<IHeatTile>> getHeatValidReceivers() {
            return validHeatReceivers;
        }

        public void receivedHeat(double amount) {
            this.setHeatStored(amount);

        }

        @Override
        public boolean needTemperature() {
            return HeatComponent.this.need;
        }

        public void setHeatStored(double amount) {
            if (HeatComponent.this.storage < amount) {
                HeatComponent.this.storage = amount;
            }
        }

        @Override
        public BlockEntity getTile() {
            return HeatComponent.this.parent;
        }

    }

    private class EnergyNetDelegateSource extends HeatComponent.EnergyNetDelegate implements IHeatSource {

        int hashCodeSource;
        private long id;

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return HeatComponent.this.sourceTier;
        }

        public boolean emitsHeatTo(IHeatAcceptor receiver, Direction dir) {
            return HeatComponent.this.sourceDirections.contains(dir);
        }

        public double getOfferedHeat() {

            return HeatComponent.this.storage;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return HeatComponent.this.parent.getBlockPos();
        }

        public void drawHeat(double amount) {
        }

        public long getIdNetwork() {
            return this.id;
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
            this.id = id;
        }

        @Override
        public void AddHeatTile(final IHeatTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyHeatConductorMap.put(dir, tile);
                validHeatReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
        }

        @Override
        public void RemoveHeatTile(final IHeatTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyHeatConductorMap.remove(dir);
                final Iterator<InfoTile<IHeatTile>> iter = validHeatReceivers.iterator();
                while (iter.hasNext()) {
                    InfoTile<IHeatTile> tileInfoTile = iter.next();
                    if (tileInfoTile.tileEntity == tile) {
                        iter.remove();
                        break;
                    }
                }
            }
        }

        @Override
        public Map<Direction, IHeatTile> getHeatTiles() {
            return energyHeatConductorMap;
        }

        @Override
        public List<InfoTile<IHeatTile>> getHeatValidReceivers() {
            return validHeatReceivers;
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
        public BlockEntity getTile() {
            return HeatComponent.this.parent;
        }

    }

}
