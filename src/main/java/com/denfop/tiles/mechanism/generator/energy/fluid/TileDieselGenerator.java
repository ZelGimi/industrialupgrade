package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerDieselGenerator;
import com.denfop.gui.GuiDieselGenerator;
import com.denfop.invslot.*;
import com.denfop.invslot.InventoryFluid;
import com.denfop.invslot.InventoryCharge;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.TileEntityLiquidTankInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;

public class TileDieselGenerator extends TileEntityLiquidTankInventory implements IAudioFixer,
        IUpdatableTileEvent {

    public final InventoryCharge chargeSlot = new InventoryCharge(this, 1);
    public final InventoryFluid fluidSlot;
    public final InventoryOutput outputSlot;
    public final String name = null;
    public final Energy energy;
    public final int production = 200;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public double coef;
    public boolean addedToEnergyNet = false;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public TileDieselGenerator() {
        super(12);
        this.coef = 1;
        this.fluidSlot = new InventoryFluidByList(this, Inventory.TypeItemSlot.INPUT, 1, InventoryFluid.TypeFluidSlot.INPUT,
                FluidName.fluiddizel.getInstance(),
                FluidName.fluida_diesel.getInstance(), FluidName.fluidaa_diesel.getInstance(),
                FluidName.fluidaaa_diesel.getInstance(), FluidName.fluidaaaa_diesel.getInstance()
        );
        this.outputSlot = new InventoryOutput(this, 1);
        this.energy = this.addComponent(Energy.asBasicSource(
                this,
                (double) 10000000,
                4
        ).addManagedSlot(chargeSlot));
        ((Fluids.InternalFluidTank) this.getFluidTank()).setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluiddizel.getInstance(),
                FluidName.fluida_diesel.getInstance(), FluidName.fluidaa_diesel.getInstance(),
                FluidName.fluidaaa_diesel.getInstance(), FluidName.fluidaaaa_diesel.getInstance()
        ));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.45));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.75));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sound = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.gen_disel;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public boolean getEnable() {
        return this.sound;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (!getEnable()) {
            return;
        }
        if (getSound() == null) {
            return;
        }
        if (soundEvent == 0) {
            this.getWorld().playSound(null, this.pos, getSound(), SoundCategory.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.pos);
            this.getWorld().playSound(null, this.pos, EnumSound.InterruptOne.getSoundEvent(), SoundCategory.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.pos);
        }
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.diesel_generator.getSoundEvent();
    }

    public void onUnloaded() {
        super.onUnloaded();

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
        if (this.fluidTank.getFluid() != null) {
            Fluid fluid = this.fluidTank.getFluid().getFluid();
            if (fluid == FluidName.fluiddizel.getInstance()) {
                coef = 1;
            } else if (fluid == FluidName.fluida_diesel.getInstance()) {
                coef = 2;

            } else if (fluid == FluidName.fluidaa_diesel.getInstance()) {
                coef = 4;

            } else if (fluid == FluidName.fluidaaa_diesel.getInstance()) {
                coef = 8;

            } else if (fluid == FluidName.fluidaaaa_diesel.getInstance()) {
                coef = 16;

            }
        }
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

    public boolean isConverting() {
        return this.getTankAmount() > 1 && this.energy.getEnergy() + (double) this.production * coef <= this.energy.getCapacity();
    }


    public ContainerDieselGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerDieselGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiDieselGenerator(new ContainerDieselGenerator(entityPlayer, this));
    }


    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

}
