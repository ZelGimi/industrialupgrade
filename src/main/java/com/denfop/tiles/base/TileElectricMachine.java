package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockResource;
import com.denfop.componets.Energy;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotDischarge;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileElectricMachine extends TileEntityInventory implements IAudioFixer,
        IUpdatableTileEvent {


    public int tier;
    public double guiChargeLevel = 0;


    public InvSlotOutput outputSlot = null;

    public Energy energy = null;
    public InvSlotDischarge dischargeSlot;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio;
    public boolean sound = true;

    public TileElectricMachine(double MaxEnergy, int tier, int count) {

        this.tier = tier;

        if (MaxEnergy != 0) {
            energy = this.addComponent(Energy.asBasicSink(this, MaxEnergy, tier));
            dischargeSlot = new InvSlotDischarge(this,14);
            energy.addManagedSlot(dischargeSlot);
        }

        if (count != 0) {
            this.outputSlot = new InvSlotOutput(this, count);
        }
        if (MaxEnergy != 0) {
            this.guiChargeLevel = this.energy.getFillRatio();
        }
        valuesAudio = EnumTypeAudio.values();

    }

    public ItemStack adjustDrop(ItemStack drop, boolean wrench) {
        if (!wrench) {
            switch (this.teBlock.getDefaultDrop()) {
                case Self:
                default:
                    final Energy component = this.getComp(Energy.class);
                    if (component != null) {
                        if (component.getEnergy() != 0) {
                            final NBTTagCompound nbt = ModUtils.nbt(drop);
                            nbt.setDouble("energy", component.getEnergy());
                        }
                    }
                    return drop;
                case None:
                    return null;
                case Generator:
                    return new ItemStack(IUItem.basemachine2, 1, 78);
                case Machine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.machine);
                case AdvMachine:
                    return IUItem.blockResource.getItemStack(BlockResource.Type.advanced_machine);
            }
        }
        final Energy component = this.getComp(Energy.class);
        if (component != null) {
            if (component.getEnergy() != 0) {
                final NBTTagCompound nbt = ModUtils.nbt(drop);
                nbt.setDouble("energy", component.getEnergy());
            }
        }
        return drop;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        new PacketUpdateFieldTile(this, "sound", this.sound);

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

    public void updateTileServer(EntityPlayer player, double event) {


        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getType() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                new PacketStopSound(getWorld(), this.pos);

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

    @Override
    public void onPlaced(final ItemStack stack, final EntityLivingBase placer, final EnumFacing facing) {
        super.onPlaced(stack, placer, facing);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        final double energy1 = nbt.getDouble("energy");
        if (energy1 != 0) {
            this.energy.addEnergy(energy1);
        }
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            final double energy1 = nbt.getDouble("energy");
            if (energy1 != 0) {
                tooltip.add(Localization.translate("iu.item.tooltip.Store") + " " + ModUtils.getString(energy1) + "/" + ModUtils.getString(
                        energy.getCapacity())
                        + " EF ");
            }

        }
        super.addInformation(stack,tooltip);
    }

    public EnumTypeAudio getType() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    @Override
    public SoundEvent getSound() {
        return null;
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
        if (getSound() == null) {
            return;
        }
        if (!getEnable()) {
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.sound = nbttagcompound.getBoolean("sound");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setBoolean("sound", this.sound);
        return nbttagcompound;
    }


    public void onUnloaded() {
        super.onUnloaded();


    }

    public void markDirty() {
        super.markDirty();


    }

    public void updateEntityServer() {
        super.updateEntityServer();


    }


    public float getChargeLevel() {
        return (float) Math.min(1, this.energy.getEnergy() / this.energy.getCapacity());
    }

    @Override
    public void onNetworkEvent(final int var1) {

    }

}
