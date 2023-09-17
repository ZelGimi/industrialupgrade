package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerBlockLimiter;
import com.denfop.gui.GuiBlockLimiter;
import com.denfop.invslot.InvSlotLimiter;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class TileLimiter extends TileEntityInventory implements IUpdatableTileEvent, IAudioFixer {

    private final AdvEnergy energy;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio;
    public boolean sound = true;
    public InvSlotLimiter slot;
    public double max_value;

    public TileLimiter() {
        this.energy = this.addComponent(new AdvEnergy(
                this,
                Double.MAX_VALUE,
                Arrays
                        .asList(EnumFacing.VALUES)
                        .stream()
                        .filter(facing -> facing != this.getFacing())
                        .collect(Collectors.toList()), Collections.singletonList(this.getFacing()),
                14,
                0,
                false
        ));
        this.energy.setLimit(true);
        this.slot = new InvSlotLimiter(this);
        valuesAudio = EnumTypeAudio.values();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            max_value = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, max_value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.limiter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.pen.getSoundEvent();
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
    public void onLoaded() {
        super.onLoaded();
        if (this.slot.isEmpty()) {
            setTier(0);
        } else {
            setTier(this.slot.get().getItemDamage() - 205);
        }
        this.energy.setDirections(Arrays
                .asList(EnumFacing.VALUES)
                .stream()
                .filter(facing -> facing != this.getFacing())
                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);
    }

    public void setTier(int tier) {
        this.energy.setSourceTier(tier);
        if (tier != 0) {
            this.energy.setCapacity(EnergyNetGlobal.instance.getPowerFromTier(tier));
        } else {
            this.energy.setCapacity(0);
            this.energy.limit_amount = 0;
        }
        if (tier != 0) {
            this.max_value = EnergyNetGlobal.instance.getPowerFromTier(tier);
        } else {
            this.max_value = 0;
        }

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("max_value", max_value);
        return nbt;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.max_value = nbtTagCompound.getDouble("max_value");
    }

    public AdvEnergy getEnergy() {
        return energy;
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {

        this.energy.limit_amount += i;
        this.energy.limit_amount = (int) Math.min(this.max_value, this.energy.limit_amount);
        this.energy.limit_amount = (int) Math.max(0, this.energy.limit_amount);
        this.energy.setDirections(Arrays
                .asList(EnumFacing.VALUES)
                .stream()
                .filter(facing -> facing != this.getFacing())
                .collect(Collectors.toList()), Collections.singletonList(this.getFacing()));
        this.energy.setCapacity(this.energy.limit_amount);
        initiate(2);
        initiate(0);

    }

    @Override
    public ContainerBlockLimiter getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerBlockLimiter(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiBlockLimiter(getGuiContainer(entityPlayer));
    }


}
