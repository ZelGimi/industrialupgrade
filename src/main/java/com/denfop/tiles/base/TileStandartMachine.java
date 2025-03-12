package com.denfop.tiles.base;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.audio.EnumSound;
import com.denfop.componets.Energy;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketStopSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class TileStandartMachine extends TileEntityInventory implements IAudioFixer,
        IUpdatableTileEvent {


    public InvSlotOutput outputSlot = null;

    public Energy energy;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    public TileStandartMachine(int count) {
        if (count != 0) {
            this.outputSlot = new InvSlotOutput(this, count);
        }


    }

    public EnumTypeAudio getTypeAudio() {
        return typeAudio;
    }

    public void setType(EnumTypeAudio type) {
        typeAudio = type;
    }

    public void initiate(int soundEvent) {
        if (this.getTypeAudio() == valuesAudio[soundEvent % valuesAudio.length]) {
            return;
        }

        setType(valuesAudio[soundEvent % valuesAudio.length]);
        if (getSound() == null) {
            return;
        }
        if (!this.getEnable()) {
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


    public SoundEvent getSound() {
        return null;
    }


    public void onGuiClosed(EntityPlayer player) {
    }


    public float getChargeLevel() {
        return (float) Math.min(1, this.energy.getEnergy() / this.energy.getCapacity());
    }


}
