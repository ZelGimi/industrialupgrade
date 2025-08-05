package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.cool.*;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.invslot.InvSlot;
import com.denfop.items.modules.ItemCoolingUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.base.TileMultiMachine;
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

public class CoolComponent extends AbstractComponent {

    public final Level world;
    public final boolean fullEnergy;
    public double capacity;
    public double storage;
    public int sinkTier;
    public int sourceTier;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
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
    Map<Direction, ICoolTile> energyCoolConductorMap = new HashMap<>();
    List<InfoTile<ICoolTile>> validColdReceivers = new LinkedList<>();
    private double coef;

    public CoolComponent(TileEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public CoolComponent(
            TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public CoolComponent(
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

    public void readFromNbt(CompoundTag nbt) {
        this.storage = nbt.getDouble("storage");
        this.upgrade = nbt.getBoolean("upgrade");
        this.meta = nbt.getInt("meta");
        this.allow = nbt.getBoolean("allow");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage", this.storage);
        ret.putBoolean("upgrade", this.upgrade);
        ret.putInt("meta", this.meta);
        ret.putBoolean("allow", this.allow);

        return ret;
    }
    public TypePurifierJob getPurifierJob() {
        return TypePurifierJob.ItemStack;
    }

    public boolean canUsePurifier(Player player) {
        return this.upgrade;
    }

    public ItemStack getItemStackUpgrade() {
        this.upgrade = false;
        this.meta = 0;
        return new ItemStack(IUItem.coolupgrade.getStack(meta));
    }

    public void onLoaded() {
        assert this.delegate == null;
        if (this.storage > this.capacity) {
            this.storage = this.capacity;
        }
        if (!this.parent.getLevel().isClientSide) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {
            } else {

                this.createDelegate();
                this.energyCoolConductorMap.clear();
                this.validColdReceivers.clear();
                MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.parent.getLevel()));
            }

            this.loaded = true;

            Level world = parent.getLevel();


            BlockPos pos = parent.getBlockPos();

            Biome biome = world.getBiome(pos).value();
            if (biome.coldEnoughToSnow(pos)) {
                coef = 0.5;
            } else if (biome.shouldSnowGolemBurn(pos)) {
                coef = 1.5;
            } else {
                coef = 1;
            }
        }

    }

    public void createDelegate() {
        if (this.delegate != null) {
        } else {
            assert !this.sinkDirections.isEmpty() || !this.sourceDirections.isEmpty();

            if (this.sinkDirections.isEmpty()) {
                this.delegate = new CoolComponent.EnergyNetDelegateSource();
            } else {
                this.delegate = new CoolComponent.EnergyNetDelegateSink();
            }

            this.delegate.setLevel(this.parent.getLevel());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {


            MinecraftForge.EVENT_BUS.post(new CoolTileUnloadEvent(this.delegate, this.parent.getLevel()));
            this.delegate = null;
        }

        this.loaded = false;
    }

    @Override
    public boolean onBlockActivated(Player player, InteractionHand hand) {
        if (!player.getLevel().isClientSide() && player != null) {
            if (this.getParent() instanceof TileMultiMachine multiMachine && player.getItemInHand(hand).getItem() instanceof ItemCoolingUpgrade<?>) {

                ItemCoolingUpgrade<ItemCoolingUpgrade.Types> coolingUpgrade = (ItemCoolingUpgrade) player.getItemInHand(hand).getItem();

                if (multiMachine.multi_process.getSizeWorkingSlot() <= coolingUpgrade.getTypeUpgrade(player.getItemInHand(hand)).getLevel()
                        && !this.upgrade) {
                    this.upgrade = true;
                    this.meta = coolingUpgrade.getElement().getId();
                    player.getItemInHand(hand).shrink(1);
                    return true;
                }
            }


        }
        return super.onBlockActivated(player, hand);
    }

    public void onContainerUpdate(ServerPlayer player) {
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

    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;

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

            assert !this.parent.getLevel().isClientSide;

            this.energyCoolConductorMap.clear();
            this.validColdReceivers.clear();
            MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.world));
        }


    }

    public Set<Direction> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }

    public Set<Direction> getSinkDirs() {
        return Collections.unmodifiableSet(this.sinkDirections);
    }

    public ICoolTile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        return this.storage;
    }


    private abstract class EnergyNetDelegate extends BlockEntity implements ICoolTile {

        private EnergyNetDelegate() {
            super(CoolComponent.this.parent.getType(), CoolComponent.this.parent.getBlockPos(), CoolComponent.this.parent.getBlockState());
        }

    }


    private class EnergyNetDelegateSink extends CoolComponent.EnergyNetDelegate implements ICoolSink {


        int hashCodeSource;
        List<ICoolSource> list = new LinkedList<>();
        private long id;

        private EnergyNetDelegateSink() {
            super();
        }

        public int getSinkTier() {
            return CoolComponent.this.sinkTier;
        }

        public boolean acceptsCoolFrom(ICoolEmitter emitter, Direction dir) {
            return CoolComponent.this.sinkDirections.contains(dir);
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
        public void AddCoolTile(final ICoolTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
        }

        @Override
        public void RemoveCoolTile(final ICoolTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyCoolConductorMap.remove(dir);
                final Iterator<InfoTile<ICoolTile>> iter = validColdReceivers.iterator();
                while (iter.hasNext()) {
                    InfoTile<ICoolTile> tileInfoTile = iter.next();
                    if (tileInfoTile.tileEntity == tile) {
                        iter.remove();
                        break;
                    }
                }
            }
        }

        @Override
        public Map<Direction, ICoolTile> getCoolTiles() {
            return energyCoolConductorMap;
        }

        @Override
        public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
            return validColdReceivers;
        }

        @Override
        public @NotNull BlockPos getPos() {
            return CoolComponent.this.parent.getBlockPos();
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
        public List<ICoolSource> getEnergyTickList() {
            return list;
        }

        @Override
        public BlockEntity getTile() {
            return CoolComponent.this.parent;
        }

    }

    private class EnergyNetDelegateSource extends CoolComponent.EnergyNetDelegate implements ICoolSource {

        int hashCodeSource;
        private long id;

        private EnergyNetDelegateSource() {
            super();
        }

        public int getSourceTier() {
            return CoolComponent.this.sourceTier;
        }

        public boolean emitsCoolTo(ICoolAcceptor receiver, Direction dir) {
            return CoolComponent.this.sourceDirections.contains(dir);
        }

        @Override
        public @NotNull BlockPos getPos() {
            return CoolComponent.this.parent.getBlockPos();
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
        public void AddCoolTile(final ICoolTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyCoolConductorMap.put(dir, tile);
                validColdReceivers.add(new InfoTile<>(tile, dir.getOpposite()));
            }
        }

        @Override
        public void RemoveCoolTile(final ICoolTile tile, final Direction dir) {
            if (!this.getLevel().isClientSide) {
                energyCoolConductorMap.remove(dir);
                final Iterator<InfoTile<ICoolTile>> iter = validColdReceivers.iterator();
                while (iter.hasNext()) {
                    InfoTile<ICoolTile> tileInfoTile = iter.next();
                    if (tileInfoTile.tileEntity == tile) {
                        iter.remove();
                        break;
                    }
                }
            }
        }

        @Override
        public Map<Direction, ICoolTile> getCoolTiles() {
            return energyCoolConductorMap;
        }

        @Override
        public List<InfoTile<ICoolTile>> getCoolValidReceivers() {
            return validColdReceivers;
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
        public BlockEntity getTile() {
            return CoolComponent.this.parent;
        }

    }

}
