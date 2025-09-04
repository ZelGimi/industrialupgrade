package com.denfop.componets;

import com.denfop.IUItem;
import com.denfop.api.otherenergies.heat.IHeatTile;
import com.denfop.api.otherenergies.heat.event.HeatTileLoadEvent;
import com.denfop.api.otherenergies.heat.event.HeatTileUnloadEvent;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.heat.EnergyNetDelegate;
import com.denfop.componets.heat.EnergyNetDelegateSink;
import com.denfop.componets.heat.EnergyNetDelegateSource;
import com.denfop.network.packet.CustomPacketBuffer;
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
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HeatComponent extends AbstractComponent {
    public final BufferEnergy buffer;
    public final Level world;

    private final double defaultCapacity;
    public Set<Direction> sinkDirections;
    public Set<Direction> sourceDirections;
    public EnergyNetDelegate delegate;
    public boolean loaded;
    public boolean auto;
    Random rand = new Random();
    private double coef;

    public HeatComponent(BlockEntityInventory parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public HeatComponent(
            BlockEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public HeatComponent(
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
        this.world = parent.getLevel();
        this.defaultCapacity = capacity;
        this.buffer = new BufferEnergy(0, capacity, sinkTier, sourceTier);
        this.coef = 0;
    }

    public static HeatComponent asBasicSink(BlockEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }

    public static HeatComponent asBasicSink(BlockEntityInventory parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static HeatComponent asBasicSource(BlockEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static HeatComponent asBasicSource(BlockEntityInventory parent, double capacity, int tier) {
        return new HeatComponent(parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
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
                MinecraftForge.EVENT_BUS.post(new HeatTileLoadEvent(this.delegate, this.parent.getLevel()));
            }

            this.loaded = true;
            Level world = parent.getLevel();


            BlockPos pos = parent.getBlockPos();

            Biome biome = world.getBiome(pos).value();
            if (biome.coldEnoughToSnow(pos)) {
                coef = -1;
            } else if (biome.coldEnoughToSnow(pos)) {
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
        if (this.coef != 0 && rand.nextInt(2) == 1) {
            this.buffer.storage += 7 * this.coef;
        }
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
            if (rand.nextInt(2) == 1) {
                if (this.coef == -1) {
                    this.buffer.storage -= 1;
                }
            }
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
        if (this.auto) {
            if (this.getEnergy() + 1 <= this.getCapacity()) {
                this.addEnergy(2);
            }
        }
    }

    public double useEnergy(double amount, boolean simulate) {
        double ret = Math.abs(Math.max(0.0D, amount - this.buffer.storage) - amount);
        if (!simulate) {
            this.buffer.storage -= ret;
            if (rand.nextInt(2) == 1) {
                if (this.coef == -1) {
                    this.buffer.storage -= 1;
                }
            }
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

            this.delegate.sinkDirections = sinkDirections;
            this.delegate.sourceDirections = sourceDirections;
            assert !this.parent.getLevel().isClientSide;
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


}
