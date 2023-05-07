package com.powerutils;

import com.denfop.api.inv.IHasGui;
import com.denfop.api.sytem.EnergyBase;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.tile.IEnergyStorage;
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
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityQEConverter extends TileEntityInventory implements IHasGui,
        INetworkClientTileEntityEventListener,
        IEnergyStorage, IUpgradableBlock {


    public final AdvEnergy energy;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public final int defaultEnergyStorage;
    public double capacity;
    public double capacity2;
    public double maxStorage2;
    public ComponentBaseEnergy energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy1 = 0;
    public double differenceenergy1 = 0;
    public int tier = 5;
    public List<EntityPlayer> list = new ArrayList<>();

    public TileEntityQEConverter() {

        this.rf = true;
        this.energy = this.addComponent((new AdvEnergy(this, 40000, Util.allFacings,
                Util.allFacings,
                5,
                5, false
        )));
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.energy2 = this.addComponent(new ComponentBaseEnergy(EnergyType.QUANTUM, this, 40000D / 16, Util.allFacings,
                Util.allFacings,
                5,
                5, false
        ));
        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultEnergyStorage = 40000;
        this.defaultEnergyRFStorage = 40000 / 16;
        this.capacity = this.energy.getCapacity();
        this.capacity2 = this.energy2.getCapacity();
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

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage
        );
        this.energy2.setSinkTier(tier);
        this.energy2.setSourceTier(tier);
        this.energy2.setCapacity((this.upgradeSlot.extraEnergyStorage +
                this.defaultEnergyStorage) / 16D
        );
        this.tier = tier;
        this.capacity = this.energy.capacity;
        this.capacity2 = this.energy2.capacity;
    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }


    protected void updateEntityServer() {
        super.updateEntityServer();
        this.differenceenergy = 0;
        this.differenceenergy1 = 0;
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.energy2.setReceivingEnabled(this.shouldEmitEnergy());
        this.energy2.setSendingEnabled(!this.shouldEmitEnergy());
        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2.getEnergy() < energy2.getCapacity()) {
                double add = Math.min(energy2.getFreeEnergy(), energy.getEnergy() / 16);
                add = Math.max(add,0);
                energy2.addEnergy(add);
                energy.useEnergy(add * 16);
            }
        } else {

            if (energy2.getEnergy() > 0 && energy.getEnergy() < energy.getCapacity()) {
                double add = Math.min(energy.getFreeEnergy(), energy2.getEnergy() * 10);
                energy.addEnergy(add);
                energy2.useEnergy(add / 10);
            }

        }
        if (this.energy2.getEnergy() > 0) {
            if (!this.list.isEmpty()) {
                if (this.rf) {
                    NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
                    com.denfop.api.sytem.NodeStats stats1 = EnergyBase.QE.getNodeStats(this.energy2.getDelegate(), this.world);

                    if (stats != null) {
                        this.differenceenergy1 = stats.getEnergyIn();
                    }
                    if (stats1 != null) {
                        this.differenceenergy = stats1.getOut();
                    }

                } else {
                    this.perenergy1 = this.energy.getEnergy();
                    NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
                    com.denfop.api.sytem.NodeStats stats1 = EnergyBase.QE.getNodeStats(this.energy2.getDelegate(), this.world);

                    if (stats != null) {
                        this.differenceenergy = stats.getEnergyOut();
                    }
                    if (stats1 != null) {
                        this.differenceenergy1 = stats1.getIn();
                    }

                }
            }
        }


        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            setOverclockRates();
        }

    }

    protected boolean shouldEmitEnergy() {

        return !this.rf;

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.tier = nbttagcompound.getInteger("tier");

        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.rf = nbttagcompound.getBoolean("rf");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

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

    public double getOutputEnergyUnitsPerTick() {
        return EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier());
    }

    @Override
    public int getStored() {
        return (int) this.energy.getEnergy();
    }

    public void setStored(int energy1) {

    }

    public int addEnergy(int amount) {
        this.energy.addEnergy(amount);
        return amount;
    }


    public boolean isTeleporterCompatible(EnumFacing side) {
        return true;
    }

    public ContainerQEConverter getGuiContainer(EntityPlayer player) {
        list.add(player);
        return new ContainerQEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQEConverter(getGuiContainer(entityPlayer));
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
        return (int) Math.min(this.energy2.getEnergy() * i / this.energy2.getCapacity(), i);
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
