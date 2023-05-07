package com.powerutils;

import com.denfop.Config;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.inv.IHasGui;
import com.denfop.componets.AdvEnergy;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityFEConverter extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener,
        IEnergyStorage, IUpgradableBlock {


    public final AdvEnergy energy;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public final int defaultEnergyStorage;
    public double capacity;
    public double maxStorage2;
    public double energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy = 0;
    public double differenceenergy1 = 0;
    public int tier = 5;
    public List<EntityPlayer> list = new ArrayList<>();
    private long tick;

    public TileEntityFEConverter() {
        this.energy2 = 0.0D;
        this.maxStorage2 = 400000;
        this.rf = true;
        this.energy = this.addComponent((new AdvEnergy(this, 40000, Util.allFacings,
                Util.allFacings,
                5,
                5, false
        )));
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultEnergyStorage = 40000;
        this.defaultEnergyRFStorage = 400000;
        this.capacity = this.energy.capacity;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.hasComp(AdvEnergy.class)) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }

    }

    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing side) {
        return cap == CapabilityEnergy.ENERGY || super.hasCapability(cap, side);
    }

    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return cap == CapabilityEnergy.ENERGY ? CapabilityEnergy.ENERGY.cast(this) : super.getCapability(cap, side);
    }

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage
        );
        this.maxStorage2 = this.defaultEnergyRFStorage + this.upgradeSlot.extraEnergyStorage * Config.coefficientrf;
        this.tier = tier;
        this.capacity = this.energy.capacity;
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {

        if (this.rf) {
            return 0;
        }
        int i = (int) Math.min(
                this.maxStorage2 - this.energy2,
                paramInt
        );
        if (!paramBoolean) {
            this.energy2 += i;
            this.differenceenergy1 = i;
            if (this.tick != this.getWorld().getWorldTime()) {
                this.tick = this.getWorld().getWorldTime();
                this.perenergy = i;
            } else {
                this.perenergy += i;
            }
        }
        return i;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.differenceenergy = 0;
        this.differenceenergy1 = 0;
        if (this.tick != this.getWorld().getWorldTime()) {
            this.perenergy = 0;
        }
        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2 < maxStorage2) {
                double add = Math.min(maxStorage2 - energy2, energy.getEnergy() * Config.coefficientrf);
                add = Math.max(add,0);
                energy2 += add;
                energy.useEnergy(add / Config.coefficientrf);
            }
        } else {

            if (energy2 > 0 && energy.getEnergy() < energy.getCapacity()) {
                double k = energy.addEnergy(energy2 / Config.coefficientrf) * Config.coefficientrf;
                energy2 -= k;
            }

        }
        if (!this.list.isEmpty()) {
            NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
            if (this.rf) {
                if (stats != null) {
                    this.differenceenergy1 = stats.getEnergyIn();
                }


            } else {

                if (stats != null) {
                    this.differenceenergy = stats.getEnergyOut();
                }
            }
        }


        int transfer = (int) Math.min(this.energy2, EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier()));
        this.energy2 -= transfer;
        this.energy2 += this.transmitEnergy(transfer);

        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            setOverclockRates();
        }
        this.energy2 = Math.min(this.energy2, this.maxStorage2);

    }

    private int transmitEnergy(int energy) {
        EnumFacing[] var2 = EnumFacing.values();

        for (EnumFacing e : var2) {
            BlockPos neighbor = this.getBlockPos().offset(e);
            if (this.world.isBlockLoaded(neighbor)) {
                TileEntity te = this.world.getTileEntity(neighbor);
                if (te != null) {
                    net.minecraftforge.energy.IEnergyStorage storage = null;
                    if (te.hasCapability(CapabilityEnergy.ENERGY, e.getOpposite())) {
                        storage = te.getCapability(
                                CapabilityEnergy.ENERGY,
                                e.getOpposite()
                        );
                    } else if (te.hasCapability(CapabilityEnergy.ENERGY, null)) {
                        storage = te.getCapability(
                                CapabilityEnergy.ENERGY,
                                null
                        );
                    }

                    if (storage != null) {
                        energy -= storage.receiveEnergy(energy, false);
                        if (energy <= 0) {
                            return 0;
                        }
                    }
                }
            }
        }

        return energy;
    }

    protected boolean shouldEmitEnergy() {

        return !this.rf;

    }

    public int extractEnergy(int paramInt, boolean paramBoolean) {
        if (!this.rf) {
            return 0;
        }
        int i = (int) Math.min(Math.min(this.energy2, EnergyNetGlobal.instance.getPowerFromTier(this.energy.getSourceTier())),
                paramInt);
        if (!paramBoolean) {
            this.energy2 -= i;
            this.differenceenergy += i;
        }
        return i;
    }

    @Override
    public int getEnergyStored() {
        return (int) this.energy2;
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) this.maxStorage2;
    }

    @Override
    public boolean canExtract() {
        return this.rf;
    }

    @Override
    public boolean canReceive() {
        return !this.rf;
    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.tier = nbttagcompound.getInteger("tier");

        this.energy2 = Util.limit(nbttagcompound.getDouble("energy2"), 0.0D,
                this.maxStorage2
        );
        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (energy2 > 0) {
            nbttagcompound.setDouble("energy2", this.energy2);
        }
        nbttagcompound.setInteger("tier", this.tier);
        nbttagcompound.setBoolean("rf", this.rf);
        return nbttagcompound;
    }

    public int getCapacity() {
        return (int) this.energy.getCapacity();
    }


    public int getOutput() {
        return (int) EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier());
    }


    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }


    public ContainerFEConverter getGuiContainer(EntityPlayer player) {
        list.add(player);
        return new ContainerFEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiFEConverter(getGuiContainer(entityPlayer));
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public void onNetworkEvent(EntityPlayer player, int event) {
        this.rf = !this.rf;

    }

    public int gaugeICEnergyScaled(int i) {
        return (int) Math.min(this.energy.getEnergy() * i / this.energy.getCapacity(), i);
    }

    public int gaugeTEEnergyScaled(int i) {
        this.maxStorage2 = this.defaultEnergyRFStorage + this.upgradeSlot.extraEnergyStorage * Config.coefficientrf;
        return (int) Math.min(this.energy2 * i / this.maxStorage2, i);
    }

    @Override
    public double getEnergy() {
        return this.energy.getEnergy();
    }

    @Override
    public boolean useEnergy(final double v) {
        return this.energy.useEnergy(v);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage
        );
    }


}
