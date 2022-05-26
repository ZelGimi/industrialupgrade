package com.powerutils;

import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.api.qe.IQEEmitter;
import com.denfop.api.qe.IQESink;
import com.denfop.api.qe.NodeQEStats;
import com.denfop.api.qe.QENet;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.QEComponent;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityQEConverter extends TileEntityInventory implements IHasGui, IWrenchable,
        INetworkClientTileEntityEventListener,
        IEnergyStorage, IUpgradableBlock, IQESink {



    public final AdvEnergy energy;
    public double capacity;
    public double capacity2;
    public double maxStorage2;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public QEComponent energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy = 0;
    public double perenergy1= 0;
    public double differenceenergy1= 0;
    public final int defaultEnergyStorage;
    public int tier = 5;
    public TileEntityQEConverter(){

        this.rf = true;
        this.energy = this.addComponent((new AdvEnergy(this, 40000,Util.allFacings,
                Util.allFacings,
                5,
                5, false
        )));
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.energy2 = this.addComponent(new QEComponent(this, 40000,Util.allFacings,
                Util.allFacings,
                5,
                5, false
        )) ;
        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);
        this.defaultEnergyStorage = 40000;
        this.defaultEnergyRFStorage = 40000/16;
        this.capacity = this.energy.capacity;
        this.capacity2 = this.energy2.capacity;
    }
    protected void onLoaded() {
        super.onLoaded();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }


    }
    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage+
                this.defaultEnergyStorage
        );
        this.energy2.setSinkTier(tier);
        this.energy2.setSourceTier(tier);
        this.energy2.setCapacity((this.upgradeSlot.extraEnergyStorage+
                this.defaultEnergyStorage)/16D
        );
        this.tier=tier;
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
        this.differenceenergy1=0;
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.energy2.setReceivingEnabled(this.shouldEmitEnergy());
        this.energy2.setSendingEnabled(!this.shouldEmitEnergy());
        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2.getEnergy() < energy2.getCapacity()) {
                double add = Math.min(energy2.getFreeEnergy(), energy.getEnergy() / 16);
                energy2.addEnergy(add);
                energy.useEnergy(add* 16);
            }
        } else {

            if (energy2.getEnergy() > 0 && energy.getEnergy() < energy.getCapacity()) {
                double k = energy.addEnergy(energy2.getEnergy() * 16) / 16;
                energy2.useEnergy(k);
            }

        }

        if(this.rf) {
            NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
            NodeQEStats stats1 = QENet.instance.getNodeStats(this.energy2.getDelegate(),this.world);

            if(stats != null)
                this.differenceenergy1=stats.getEnergyIn();
            if(stats1 != null)
            this.differenceenergy = stats1.getQE();

        }else{
            this.perenergy1 = this.energy.getEnergy();
            NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
            NodeQEStats stats1 = QENet.instance.getNodeStats(this.energy2.getDelegate(),this.world);

            if(stats != null)
                this.differenceenergy=stats.getEnergyOut();
            if(stats1 != null)
                this.differenceenergy = stats1.getQEIn();
        }


        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            super.markDirty();
        }

    }
    protected boolean shouldEmitEnergy() {

        return !this.rf;

    }



     @Override
    public EnumFacing getFacing(World world, BlockPos blockPos) {
        return this.getFacing();
    }

    @Override
    public boolean setFacing(World world, BlockPos blockPos, EnumFacing enumFacing, EntityPlayer entityPlayer) {
        if (!this.canSetFacingWrench(enumFacing, entityPlayer)) {
            return false;
        } else {
            this.setFacing(enumFacing);
            return true;
        }
    }

    @Override
    public boolean wrenchCanRemove(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public List<ItemStack> getWrenchDrops(
            final World world,
            final BlockPos blockPos,
            final IBlockState iBlockState,
            final TileEntity tileEntity,
            final EntityPlayer entityPlayer,
            final int i
    ) {
        List<ItemStack> list = new ArrayList<>();
        return list;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.energy.setDirections(Util.allFacings, Util.allFacings);
        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.tier= nbttagcompound.getInteger("tier");

        this.energy2.setDirections(Util.allFacings, Util.allFacings);
        this.rf = nbttagcompound.getBoolean("rf");

    }
    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("tier",this.tier);
        nbttagcompound.setBoolean("rf",this.rf);
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

    public ContainerBase<TileEntityQEConverter> getGuiContainer(EntityPlayer player) {
        return new ContainerQEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQEConverter(new ContainerQEConverter(entityPlayer, this));
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public void onNetworkEvent(EntityPlayer player, int event) {
        this.rf = !this.rf;

    }

    public int gaugeICEnergyScaled(int i) {
        return (int) Math.min(this.energy.getEnergy() * i / this.energy.getCapacity(),i);
    }
    public int gaugeTEEnergyScaled(int i) {
        return (int) Math.min(this.energy2.getEnergy() * i / this.energy2.getCapacity(),i);
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


    @Override
    public boolean acceptsQEFrom(final IQEEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public double getDemandedQE() {
        return 0;
    }

    @Override
    public double injectQE(final EnumFacing var1, final double var2, final double var4) {
        return 0;
    }

}
