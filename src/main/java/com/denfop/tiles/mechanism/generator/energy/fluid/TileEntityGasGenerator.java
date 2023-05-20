package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUCore;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.AudioSource;
import com.denfop.blocks.FluidName;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerGasGenerator;
import com.denfop.gui.GuiGasGenerator;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotConsumableLiquid;
import com.denfop.invslot.InvSlotConsumableLiquidByList;
import com.denfop.tiles.base.TileEntityLiquidTankInventory;
import ic2.api.item.ElectricItem;
import ic2.api.network.INetworkClientTileEntityEventListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

public class TileEntityGasGenerator extends TileEntityLiquidTankInventory implements IHasGui,
        IAudioFixer, INetworkClientTileEntityEventListener {

    public final InvSlotCharge chargeSlot = new InvSlotCharge(this, 1);
    public final InvSlotConsumableLiquid fluidSlot;
    public final InvSlotOutput outputSlot;
    public final AdvEnergy energy;
    public final int production = Math.round(200.0F);
    public boolean addedToEnergyNet = false;
    public AudioSource audioSource;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public TileEntityGasGenerator() {
        super(24);
        this.fluidSlot = new InvSlotConsumableLiquidByList(this, "fluidSlot", 1, FluidName.fluidgas.getInstance(),
                InvSlotConsumableLiquid.OpType.Drain
        );
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.energy = this.addComponent(AdvEnergy.asBasicSource(this, 50000000, 1));

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

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

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

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
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
            IUCore.network.get(true).initiateTileEntityEvent(this, soundEvent, true);
        }
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

        this.setActive(this.gainEnergy());
        if (this.energy.getEnergy() >= 1.0D && !this.chargeSlot.get().isEmpty()) {
            double used = ElectricItem.manager.charge(this.chargeSlot.get(), this.energy.getEnergy(), 1, false, false);
            this.energy.useEnergy(used);
        }


        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
    }


    public void onGuiClosed(EntityPlayer entityPlayer) {
    }


    public String getStartSoundFile() {
        return "Machines/gas.ogg";
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
            this.energy.addEnergy(this.production);
            this.getFluidTank().drain(2, true);
            if (getWorld().provider.getWorldTime() % 200 == 0) {
                initiate(0);
            }
            return true;
        } else {

            initiate(2);
            return false;
        }
    }

    public boolean canFill(Fluid fluid) {
        return fluid == FluidName.fluidhyd.getInstance();
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


    public ContainerGasGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerGasGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiGasGenerator(new ContainerGasGenerator(entityPlayer, this));
    }


}
