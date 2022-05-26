package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.audio.AudioSource;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerDieselGenerator;
import com.denfop.gui.GUIDieselGenerator;
import ic2.api.energy.EnergyNet;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotCharge;
import ic2.core.block.invslot.InvSlotConsumableLiquid;
import ic2.core.block.invslot.InvSlotConsumableLiquidByList;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class TileEntityDieselGenerator extends TileEntityLiquidTankInventory implements IHasGui, INetworkTileEntityEventListener {

    public final InvSlotCharge chargeSlot = new InvSlotCharge(this, 1);
    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput outputSlot;
    public final double coef;
    public final String name = null;
    public final Energy energy;
    public final int production = Math.round(20.0F * ConfigUtil.getFloat(
            MainConfig.get(),
            "balance/energy/generator/geothermal"
    ));
    public boolean addedToEnergyNet = false;
    public AudioSource audioSource;

    public TileEntityDieselGenerator() {
        super(12);
        this.coef = 2;
        this.fluidSlot = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidName.fluiddizel.getInstance());
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.energy = this.addComponent(Energy.asBasicSource(
                this,
                (double) 32000 * coef,
                EnergyNet.instance.getTierFromPower(this.production)
        ));

    }



    public String getInventoryName() {
        return Localization.translate(name);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


    }

    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        return nbttagcompound;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        boolean needsInvUpdate = false;
        if (this.needsFluid()) {
            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.fluidSlot.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                needsInvUpdate = this.fluidSlot.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }

        boolean newActive = this.gainEnergy();

        if (this.energy.getEnergy() >= 1.0D && this.chargeSlot.get() != null) {
            double used = ElectricItem.manager.charge(this.chargeSlot.get(), this.energy.getEnergy(), 1, false, false);
            this.energy.useEnergy(used);
            if (used > 0.0D) {
                needsInvUpdate = true;
            }
        }

        if (needsInvUpdate) {
            this.markDirty();
        }

        if (this.getActive() != newActive) {
            this.setActive(newActive);
        }
        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
    }

    private void initiate(int soundEvent) {
        IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
    }

    public String getStartSoundFile() {
        return "Machines/diesel_generator.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;


        }
    }

    public float getWrenchDropRate() {
        return 0.9F;
    }

    public boolean gainEnergy() {
        if (this.isConverting()) {
            this.energy.addEnergy(this.production * coef);
            this.getFluidTank().drain(2, true);
            initiate(0);
            return true;
        } else {
            initiate(2);
            return false;
        }
    }

    public boolean canFill(Fluid fluid) {
        return fluid == FluidName.fluiddizel.getInstance();
    }

    public boolean canDrain(Fluid fluid) {
        return false;
    }

    public boolean isConverting() {
        return this.getTankAmount() > 0 && this.energy.getEnergy() + (double) this.production <= this.energy.getCapacity();
    }

    public int gaugeStorageScaled(int i) {
        return (int) (this.energy.getEnergy() * (double) i / this.energy.getCapacity());
    }


    public ContainerBase<TileEntityDieselGenerator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerDieselGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIDieselGenerator(new ContainerDieselGenerator(entityPlayer, this));
    }


}
