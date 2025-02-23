package com.denfop.tiles.mechanism.generator.things.fluid;

import com.denfop.IUItem;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerWaterGenerator;
import com.denfop.gui.GuiWaterGenerator;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileWaterGenerator extends TileElectricMachine implements IUpgradableBlock {

    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final InvSlotFluid containerslot;
    public final FluidTank fluidTank;
    protected final Fluids fluids;
    private final float energycost;
    private double lastEnergy;

    public TileWaterGenerator() {
        super(10000, 1, 1);

        this.energycost = 40;
        this.outputSlot = new InvSlotOutput(this, 1);
        this.containerslot = new InvSlotFluidByList(
                this,
                InvSlot.TypeItemSlot.INPUT,
                1,
                InvSlotFluid.TypeFluidSlot.OUTPUT,
                FluidRegistry.WATER
        );

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", 20 * 1000, InvSlot.TypeItemSlot.OUTPUT,
                Fluids.fluidPredicate(FluidRegistry.WATER)
        );
        this.upgradeSlot = new InvSlotUpgrade(this, 4);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.25));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));

    }

    private static int applyModifier(int extra) {
        double ret = (double) Math.round(((double) 14 + (double) extra));
        return ret > 2.147483647E9D ? 2147483647 : (int) ret;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.watergenerator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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


        if (!(this.energy.getEnergy() <= 0.0D) && this.fluidTank.getFluidAmount() < this.fluidTank.getCapacity()) {
            if (!this.getActive()) {
                this.setActive(true);
            }

            if (this.energy.getEnergy() >= this.energycost) {
                this.attemptGeneration();
                if (this.world.provider.getWorldTime() % 40 == 0) {
                    initiate(0);
                }
            } else {
                if (this.world.provider.getWorldTime() % 40 == 0) {
                    initiate(2);
                }
            }

            this.lastEnergy = this.energy.getEnergy();

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

    public void attemptGeneration() {
        int k = (int) (this.energy.getEnergy() / this.energycost);
        int m;

        if (this.fluidTank.getFluidAmount() + 1 > this.fluidTank.getCapacity()) {
            return;
        }
        m = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
        this.fluidTank.fillInternal(new FluidStack(FluidRegistry.WATER, Math.min(m, k)), true);
        this.energy.useEnergy(this.energycost * Math.min(m, k));
    }

    public String getProgressAsString() {
        int p = Math.min((int) (this.energy.getEnergy() * 100.0D / this.energycost), 100);
        return "" + p + "%";
    }

    public ContainerWaterGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerWaterGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiWaterGenerator(new ContainerWaterGenerator(entityPlayer, this));
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


    @Override
    public SoundEvent getSound() {
        return null;
    }

}
