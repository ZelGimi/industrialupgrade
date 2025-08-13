package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.cool.*;
import com.denfop.api.cool.event.CoolTileLoadEvent;
import com.denfop.api.cool.event.CoolTileUnloadEvent;
import com.denfop.api.sytem.InfoTile;
import com.denfop.componets.cold.EnergyNetDelegate;
import com.denfop.componets.cold.EnergyNetDelegateSink;
import com.denfop.componets.cold.EnergyNetDelegateSource;
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

    public final BufferEnergy buffer;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;



    public EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean upgrade = false;
    public int meta = 0;
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
        this.sinkDirections = sinkDirections;
        this.sourceDirections = sourceDirections;
        this.world = parent.getLevel();
        this.buffer = new BufferEnergy(0,capacity,sinkTier,sourceTier);
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
        this.buffer.storage = nbt.getDouble("storage");
        this.upgrade = nbt.getBoolean("upgrade");
        this.meta = nbt.getInt("meta");
        this.buffer.allow = nbt.getBoolean("allow");
    }

    public CompoundTag writeToNbt() {
        CompoundTag ret = new CompoundTag();
        ret.putDouble("storage",  this.buffer.storage);
        ret.putBoolean("upgrade", this.upgrade);
        ret.putInt("meta", this.meta);
        ret.putBoolean("allow", this.buffer.allow);

        return ret;
    }

    public void onLoaded() {
        assert this.delegate == null;
        if (this.buffer.storage > this.buffer.capacity) {
            this.buffer.storage = this.buffer.capacity;
        }
        if (!this.parent.getLevel().isClientSide) {
            if (this.sinkDirections.isEmpty() && this.sourceDirections.isEmpty()) {
            } else {

                this.createDelegate();
                MinecraftForge.EVENT_BUS.post(new CoolTileLoadEvent(this.delegate, this.parent.getLevel()));
            }

            this.loaded = true;

            Level world = parent.getLevel();


            BlockPos pos = parent.getBlockPos();

            Biome biome = world.getBiome(pos).value();
            if (biome.coldEnoughToSnow(pos)) {
                coef = 0.5;
            } else if (biome.coldEnoughToSnow(pos)) {
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
                this.delegate = new EnergyNetDelegateSource(this);
            } else {
                this.delegate = new EnergyNetDelegateSink(this);
            }

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
        buffer.writeDouble(this.buffer.capacity);
        buffer.writeDouble(this.buffer.storage);
        buffer.writeInt(this.meta);
        buffer.writeBoolean(this.upgrade);
        buffer.writeBoolean(this.buffer.allow);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
    }

    public void onNetworkUpdate(CustomPacketBuffer is) throws IOException {
        this.buffer.capacity = is.readDouble();
        this.buffer.storage = is.readDouble();
        this.meta = is.readInt();
        this.upgrade = is.readBoolean();
        this.buffer.allow = is.readBoolean();
    }

    public CustomPacketBuffer updateComponent() {
        final CustomPacketBuffer packet = super.updateComponent();
        packet.writeDouble(this.buffer.capacity);
        packet.writeDouble(this.buffer.storage);
        packet.writeInt(this.meta);
        packet.writeBoolean(this.upgrade);
        packet.writeBoolean(this.buffer.allow);
        return packet;
    }

    @Override
    public boolean isServer() {
        return false;
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

        this.buffer.storage += amount * this.coef;
        this.buffer.storage = Math.min(this.buffer.storage, this.buffer.capacity);
        this.buffer.storage = Math.max(this.buffer.storage, 0);
        if (this.upgrade) {
            this.buffer.storage = 0;
        }

        return amount;
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


    public boolean canUseEnergy(double amount) {
        return this.buffer.storage >= amount;
    }

    public boolean useEnergy(double amount) {
        if (this.buffer.storage >= amount / this.coef) {
            this.buffer.storage -= amount / this.coef;
            if (CoolComponent.this.buffer.storage <= 0.005) {
                CoolComponent.this.buffer.storage = 0;
            }
            return true;
        } else {
            return false;
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        if (this.buffer.storage <= 0) {
            this.buffer.storage = 0;
            return amount;
        }
        double ret = Math.abs(Math.max(0.0D, amount - this.buffer.storage) - amount) / this.coef;
        if (!simulate) {
            this.buffer.storage -= ret / this.coef;
            if (this.buffer.storage <= 0.005) {
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



    public void setDirections(Set<Direction> sinkDirections, Set<Direction> sourceDirections) {

        if (this.delegate != null) {


            assert !this.parent.getLevel().isClientSide;
            delegate.sinkDirections = sinkDirections;
            delegate.sourceDirections = sourceDirections;
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
            delegate.sinkDirections = sinkDirections;
            delegate.sourceDirections = sourceDirections;
            assert !this.parent.getLevel().isClientSide;
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
        return this.buffer.storage;
    }









}
