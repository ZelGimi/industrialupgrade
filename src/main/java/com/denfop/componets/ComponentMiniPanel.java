package com.denfop.componets;

import com.denfop.api.energy.IEnergyAcceptor;
import com.denfop.api.energy.IEnergySource;
import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ComponentMiniPanel extends AbstractComponent {

    public final boolean fullEnergy;
    private final double defaultCapacity;
    public double tick;
    public double capacity;
    public double storage;
    public int sourceTier;
    public int defaultSourceTier;
    public Set<EnumFacing> sourceDirections;
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
    private double prodution;

    public ComponentMiniPanel(TileEntityMiniPanels parent, double capacity) {
        this(parent, capacity, Collections.emptySet(), 1);
    }

    public ComponentMiniPanel(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sourceDirections,
            int tier
    ) {
        this(parent, capacity, sourceDirections, tier, false);
    }

    public ComponentMiniPanel(
            TileEntityInventory parent,
            double capacity,
            Set<EnumFacing> sourceDirections,
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
    public void onNeighborChange(final Block srcBlock, final BlockPos srcPos) {
        TileEntity tile = this.getParent().getWorld().getTileEntity(srcPos);
        boolean hasElement = this.energyStorageMap.containsKey(srcPos);
        if (srcBlock.getDefaultState().getMaterial() == Material.AIR && hasElement) {
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
        if (tile.hasCapability(CapabilityEnergy.ENERGY, this.getParent().getFacing().getOpposite())) {
            IEnergyStorage energy_storage = tile.getCapability(
                    CapabilityEnergy.ENERGY,
                    this.getParent().getFacing().getOpposite()
            );
            this.energyStorageMap.put(srcPos, energy_storage);
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


    public void readFromNbt(NBTTagCompound nbt) {
        this.storage = nbt.getDouble("storage");
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setDouble("storage", this.storage);

        return ret;
    }

    public void onLoaded() {
        if (this.capacity < this.defaultCapacity) {
            this.capacity = this.defaultCapacity;
        }
        if (!this.parent.getWorld().isRemote) {
            for (EnumFacing facing : EnumFacing.VALUES) {
                final BlockPos srcPos = this.parent.getPos().offset(facing);
                TileEntity tile = this.getParent().getWorld().getTileEntity(srcPos);
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
                if (tile.hasCapability(CapabilityEnergy.ENERGY, this.getParent().getFacing().getOpposite())) {
                    IEnergyStorage energy_storage = tile.getCapability(
                            CapabilityEnergy.ENERGY,
                            this.getParent().getFacing().getOpposite()
                    );
                    this.energyStorageMap.put(srcPos, energy_storage);
                }
            }
        }
        assert this.delegate == null;

        if (!this.parent.getWorld().isRemote) {
            if (!(this.sourceDirections.isEmpty())) {
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


            this.delegate.setWorld(this.parent.getWorld());
            this.delegate.setPos(this.parent.getPos());
        }
    }

    public void onUnloaded() {
        if (this.delegate != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
            this.delegate = null;
        }
        this.loaded = false;
    }

    public void onContainerUpdate(EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16);
        buffer.writeDouble(this.capacity);
        buffer.writeDouble(this.storage);
        buffer.writeDouble(this.bonusCapacity);
        buffer.writeDouble(this.bonusProdution);
        buffer.writeDouble(this.prodution);
        buffer.flip();
        this.setNetworkUpdate(player, buffer);
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


    public void setDirections(Set<EnumFacing> sourceDirections) {
        if (this.delegate != null) {

            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(this.parent.getWorld(), this.delegate));
        }

        this.sourceDirections = sourceDirections;
        if (sourceDirections.isEmpty()) {
            this.delegate = null;
        } else if (this.delegate == null && this.loaded) {
            this.createDelegate();
        }
        if (this.delegate != null) {


            assert !this.parent.getWorld().isRemote;

            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this.parent.getWorld(), this.delegate));
        }


    }

    public Set<EnumFacing> getSourceDirs() {
        return Collections.unmodifiableSet(this.sourceDirections);
    }


    public IEnergyTile getDelegate() {
        return this.delegate;
    }

    private double getSourceEnergy() {
        return Math.min(this.storage, this.getProdution());
    }


    private abstract static class EnergyNetDelegate extends TileEntity implements IEnergyTile {

        private EnergyNetDelegate() {
        }

    }


    private class EnergyNetDelegateSource extends ComponentMiniPanel.EnergyNetDelegate implements IEnergySource {


        private EnergyNetDelegateSource() {
            super();

        }

        @Override
        public @NotNull BlockPos getBlockPos() {
            return ComponentMiniPanel.this.parent.getPos();
        }

        public int getSourceTier() {
            return ComponentMiniPanel.this.sourceTier;
        }

        public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing dir) {
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
        public TileEntity getTileEntity() {
            return this;
        }

    }


}
