package com.denfop.tiles.base;

import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.audio.IAudioFixer;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.componets.Energy;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketStopSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TileStandartMachine extends TileEntityInventory implements IAudioFixer,
        IUpdatableTileEvent {


    public InvSlotOutput outputSlot = null;

    public Energy energy;
    public EnumTypeAudio typeAudio = EnumTypeAudio.OFF;
    public EnumTypeAudio[] valuesAudio = EnumTypeAudio.values();

    public TileStandartMachine(int count, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
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
            this.getWorld().playSound(null, this.getBlockPos(), getSound(), SoundSource.BLOCKS, 1F, 1);
        } else if (soundEvent == 1) {
            new PacketStopSound(getWorld(), this.getBlockPos());
            this.getWorld().playSound(null, this.getBlockPos(), EnumSound.InterruptOne.getSoundEvent(), SoundSource.BLOCKS, 1F, 1);
        } else {
            new PacketStopSound(getWorld(), this.getBlockPos());
        }
    }


    public SoundEvent getSound() {
        return null;
    }


    public float getChargeLevel() {
        return (float) Math.min(1, this.energy.getEnergy() / this.energy.getCapacity());
    }


}
