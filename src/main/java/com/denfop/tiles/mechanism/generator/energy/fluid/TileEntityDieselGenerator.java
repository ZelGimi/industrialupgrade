package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUCore;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.AudioSource;
import com.denfop.blocks.FluidName;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerDieselGenerator;
import com.denfop.gui.GuiDieselGenerator;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.tiles.base.TileEntityLiquidTankInventory;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkClientTileEntityEventListener;
import ic2.core.IC2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

public class TileEntityDieselGenerator extends TileEntityLiquidTankInventory implements IHasGui, IAudioFixer,
        INetworkClientTileEntityEventListener {

    public final InvSlotCharge chargeSlot = new InvSlotCharge(this, 1);
    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput outputSlot;
    public final double coef;
    public final String name = null;
    public final AdvEnergy energy;
    public final int production = 120;
    public boolean addedToEnergyNet = false;
    public AudioSource audioSource;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public TileEntityDieselGenerator() {
        super(12);
        this.coef = 1;
        this.fluidSlot = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidName.fluiddizel.getInstance());
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.energy = this.addComponent(AdvEnergy.asBasicSource(
                this,
                (double) 50000 * coef,
                EnergyNet.instance.getTierFromPower(this.production)
        ).addManagedSlot(chargeSlot));

    }

    public void changeSound() {
        sound = !sound;
        IUCore.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IUCore.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getType() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }
        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (sound) {
            IC2.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
        }
    }


    protected void onUnloaded() {
        super.onUnloaded();
        if (IC2.platform.isRendering() && this.audioSource != null) {
            IUCore.audioManager.removeSources(this);
            this.audioSource = null;
        }

    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.needsFluid()) {
            MutableObject<ItemStack> output = new MutableObject<>();
            if (this.fluidSlot.transferToTank(
                    this.fluidTank,
                    output,
                    true
            ) && (output.getValue() == null || this.outputSlot.canAdd(output.getValue()))) {
                this.fluidSlot.transferToTank(this.fluidTank, output, false);
                if (output.getValue() != null) {
                    this.outputSlot.add(output.getValue());
                }
            }
        }

        boolean newActive = this.gainEnergy();


        if (this.getActive() != newActive) {
            this.setActive(newActive);
        }
        if (this.getActive() && getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
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


    public ContainerDieselGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerDieselGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiDieselGenerator(new ContainerDieselGenerator(entityPlayer, this));
    }


    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {
        sound = !sound;
        IUCore.network.get(true).updateTileEntityField(this, "sound");

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                IUCore.network.get(true).initiateTileEntityEvent(this, 2, true);

            }
        }
    }

}
