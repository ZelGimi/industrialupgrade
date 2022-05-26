package com.powerutils;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.componets.AdvEnergy;
import com.denfop.tiles.panels.entity.TileEntitySolarPanel;
import com.powerutils.handler.TeslaHelper;
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
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.util.Util;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityTEConverter extends TileEntityInventory implements IHasGui, IWrenchable,
        INetworkClientTileEntityEventListener,
        IEnergyStorage, ITeslaConsumer, ITeslaProducer, IUpgradableBlock {



    public final AdvEnergy energy;
    public double capacity;
    public double maxStorage2;
    public final InvSlotUpgrade upgradeSlot;
    public final int defaultEnergyRFStorage;
    public double energy2;
    public boolean rf;
    public double differenceenergy = 0;
    public double perenergy = 0;
    public double perenergy1= 0;
    public double differenceenergy1= 0;
    public final int defaultEnergyStorage;
    public int tier = 5;


    public TileEntityTEConverter(){
        this.energy2 = 0.0D;
        this.maxStorage2 = 400000;
        this.rf = true;
        this.energy = this.addComponent((new AdvEnergy(this, 40000,Util.allFacings,
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
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side) {
        if (TeslaHelper.isTeslaCapability(capability)) {
            final Capability<ITeslaProducer> mjConnector = TeslaHelper.TESLA_PRODUCER;
            final Capability<ITeslaConsumer> teslaConsumer = TeslaHelper.TESLA_CONSUMER;
            if (capability == mjConnector && canExtract()) {
                return mjConnector.cast(this);
            }

            if (capability == teslaConsumer && canReceive()) {
                return teslaConsumer.cast(this);
            }

            return super.getCapability(capability, side);
        }
        return super.getCapability(capability, side);
    }
    public boolean canExtract() {
        return extractEnergy(1, true) != 0;
    }

    public boolean canReceive() {
        return receiveEnergy(1, true) != 0;
    }
    public void setOverclockRates() {
        this.upgradeSlot.onChanged();
        int tier = this.upgradeSlot.getTier(5);
        this.energy.setSinkTier(tier);
        this.energy.setSourceTier(tier);
        this.energy.setCapacity(this.upgradeSlot.extraEnergyStorage+
                this.defaultEnergyStorage
        );
        this.maxStorage2 = this.defaultEnergyRFStorage+this.upgradeSlot.extraEnergyStorage*Config.coefficientrf;
        this.tier=tier;
        this.capacity = this.energy.capacity;
    }
    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            this.setOverclockRates();
        }

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        if(this.rf)
            return 0;
        int i = (int) Math.min(this.maxStorage2 - this.energy2,
                Math.min(EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier() * Config.coefficientrf),
                        paramInt));
        if (!paramBoolean) {
            this.energy2 += i;
            this.differenceenergy1=i;
        }
        return i;
    }

    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return extractEnergy((int) Math.min(EnergyNet.instance.getPowerFromTier(this.energy.getSourceTier() * Config.coefficientrf), maxExtract), simulate);
    }
    protected void updateEntityServer() {
        super.updateEntityServer();
        this.energy.setReceivingEnabled(!this.shouldEmitEnergy());
        this.energy.setSendingEnabled(this.shouldEmitEnergy());
        this.differenceenergy = 0;
        this.differenceenergy1=0;

        if (this.rf) {
            if (energy.getEnergy() > 0 && energy2 < maxStorage2) {
                double add = Math.min(maxStorage2 -energy2, energy.getEnergy() * Config.coefficientrf);
                energy2 += add;
                energy.useEnergy(add/ Config.coefficientrf);
            }
        } else {

            if (energy2 > 0 && energy.getEnergy() < energy.getCapacity()) {
                double k = energy.addEnergy(energy2 / Config.coefficientrf) * Config.coefficientrf;
                energy2 -= k;
            }

        }

        if(this.rf) {
            NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
            if(stats != null)
                this.differenceenergy1=stats.getEnergyIn();
            this.differenceenergy = this.energy2 - this.perenergy;

        }else{
            this.perenergy1 = this.energy.getEnergy();
            NodeStats stats = EnergyNet.instance.getNodeStats(this.energy.getDelegate());
            if(stats != null)
                this.differenceenergy=stats.getEnergyOut();
        }

        if(this.rf)
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos pos = new BlockPos(
                        this.pos.getX() + facing.getFrontOffsetX(),
                        this.pos.getY() + facing.getFrontOffsetY(),
                        this.pos.getZ() + facing.getFrontOffsetZ()
                );

                if (this.getWorld().getTileEntity(pos) == null) {
                    continue;
                }
                TileEntity tile = this.getWorld().getTileEntity(pos);

                if (!(tile instanceof TileEntitySolarPanel)) {

                    if (tile instanceof IEnergyReceiver) {
                        extractEnergy(facing, ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(),
                                extractEnergy(facing, (int) this.energy2, true), false
                        ), false);
                    }
                }
            }

        int transfer = (int) Math.min(this.energy2, 10000);
        this.energy2 -= transfer;
        this.energy2 += this.transmitEnergy(transfer);

        final boolean needsInvUpdate = this.upgradeSlot.tickNoMark();
        if (needsInvUpdate) {
            super.markDirty();
        }
        this.energy2 = Math.min(this.energy2,this.maxStorage2);

    }

    private int transmitEnergy(int energy) {
        EnumFacing[] var2 = EnumFacing.values();

        for (EnumFacing e : var2) {
            BlockPos neighbor = this.getPos().offset(e);
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
        if(!this.rf)
            return 0;
        int i = (int) Math.min(this.energy2, paramInt);
        if (!paramBoolean) {
            this.energy2 -= i;
            this.differenceenergy=i;
        }
        return i;
    }



    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxStorage2;
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
        this.tier= nbttagcompound.getInteger("tier");

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
        nbttagcompound.setInteger("tier",this.tier);
        nbttagcompound.setBoolean("rf",this.rf);
        return nbttagcompound;
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

    @Override
    public int getCapacity() {
        return (int) this.energy.getCapacity();
    }


    public boolean isTeleporterCompatible(EnumFacing side) {
        return true;
    }

    public ContainerBase<TileEntityTEConverter> getGuiContainer(EntityPlayer player) {
        return new ContainerTEConverter(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiTEConverter(new ContainerTEConverter(entityPlayer, this));
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
        this.maxStorage2 = this.defaultEnergyRFStorage+this.upgradeSlot.extraEnergyStorage*Config.coefficientrf;
        return (int) Math.min(this.energy2 * i / this.maxStorage2,i);
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
    public long givePower(final long l, final boolean b) {
        return receiveEnergy((int) l,b);
    }

    @Override
    public long takePower(final long l, final boolean b) {
        return extractEnergy((int) l,b);
    }

}
