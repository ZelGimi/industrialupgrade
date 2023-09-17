package com.denfop.tiles.mechanism.generator.energy.fluid;

import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerHydrogenGenerator;
import com.denfop.gui.GuiHydrogenGenerator;
import com.denfop.invslot.InvSlotCharge;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;

public class TileHydrogenGenerator extends TileEntityLiquidTankInventory implements
        IAudioFixer, IUpdatableTileEvent {

    public final InvSlotCharge chargeSlot = new InvSlotCharge(this, 1);
    public final InvSlotFluid fluidSlot;
    public final InvSlotOutput outputSlot;
    public final double coef;
    public final AdvEnergy energy;
    public final int production = Math.round(20.0F * 1);
    public boolean addedToEnergyNet = false;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();
    private boolean sound = true;

    public TileHydrogenGenerator() {
        super(12);
        this.coef = 1;
        this.fluidSlot = new InvSlotFluidByList(this, 1, FluidName.fluidhyd.getInstance());
        this.outputSlot = new InvSlotOutput(this, "output", 1);
        this.energy = this.addComponent(AdvEnergy.asBasicSource(this, (double) 25000 * coef, 1));
        ((Fluids.InternalFluidTank) this.getFluidTank()).setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidhyd.getInstance()));
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
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

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.gen_hyd;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
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
        return EnumSound.hydrogen_generator.getSoundEvent();
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

        if (this.energy.getEnergy() >= 1.0D && !this.chargeSlot.get().isEmpty()) {
            double used = ElectricItem.manager.charge(this.chargeSlot.get(), this.energy.getEnergy(), 1, false, false);
            this.energy.useEnergy(used);
        }


        if (this.getActive() != newActive) {
            this.setActive(newActive);
        }
        if (getWorld().provider.getWorldTime() % 60 == 0) {
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
            if (getWorld().provider.getWorldTime() % 60 == 0) {
                initiate(0);
            }
            return true;
        } else {

            initiate(2);
            return false;
        }
    }

    public boolean isConverting() {
        return this.getTankAmount() > 0 && this.energy.getEnergy() + (double) this.production <= this.energy.getCapacity();
    }


    public ContainerHydrogenGenerator getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerHydrogenGenerator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiHydrogenGenerator(new ContainerHydrogenGenerator(entityPlayer, this));
    }


    @Override
    public void onNetworkEvent(final int var1) {

    }

}
