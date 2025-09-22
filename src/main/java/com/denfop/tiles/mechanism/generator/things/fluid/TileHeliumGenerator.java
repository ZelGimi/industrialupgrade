package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerHeliumGenerator;
import com.denfop.gui.GuiHeliumGenerator;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryFluid;
import com.denfop.invslot.InventoryFluidByList;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileHeliumGenerator extends TileElectricMachine implements IUpgradableBlock {

    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final InventoryFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private double lastEnergy;

    public TileHeliumGenerator() {
        super(50000, 1, 1);

        this.energycost = 1000;
        this.outputSlot = new InventoryOutput(this, 1);
        this.containerslot = new InventoryFluidByList(
                this,
                Inventory.TypeItemSlot.INPUT,
                1,

                InventoryFluid.TypeFluidSlot.OUTPUT,
                FluidName.fluidHelium.getInstance()
        );
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 20 * 1000, Inventory.TypeItemSlot.OUTPUT,
                Fluids.fluidPredicate(FluidName.fluidHelium.getInstance())
        );
        this.upgradeSlot = new InventoryUpgrade(this, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.helium_generator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.hydrogen_generator.getSoundEvent();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.lastEnergy = nbt.getDouble("lastEnergy");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("lastEnergy", this.lastEnergy);
        return nbt;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            this.setUpgradestat();
        }

    }


    public void updateEntityServer() {
        super.updateEntityServer();

        boolean needsInvUpdate = false;
        if (!(this.energy.getEnergy() <= 0.0D)) {

            if (!this.getActive()) {
                this.setActive(true);
            }

            if (this.energy.getEnergy() >= this.energycost) {
                needsInvUpdate = this.attemptGeneration();
            }

            this.lastEnergy = this.energy.getEnergy();
            if (this.upgradeSlot.tickNoMark() && needsInvUpdate) {
                setUpgradestat();
            }
        } else {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
        if (!this.containerslot.isEmpty() && this.fluidTank.getFluidAmount() > 0) {
            this.containerslot.processFromTank(this.fluidTank, this.outputSlot);
        }
        if (this.upgradeSlot.tickNoMark()) {
            setUpgradestat();
        }
    }

    public boolean attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return false;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fillInternal(new FluidStack(FluidName.fluidHelium.getInstance(), Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
        return true;
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerHeliumGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerHeliumGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiHeliumGenerator(new ContainerHeliumGenerator(entityPlayer, this));
    }


    public void setUpgradestat() {
        this.energy.setSinkTier(applyModifier(this.upgradeSlot.extraTier));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(

                UpgradableProperty.Transformer,
                UpgradableProperty.FluidExtract
        );
    }


}
