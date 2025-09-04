package com.denfop.sound;

import com.denfop.api.sound.AudioFixer;
import com.denfop.api.sound.EnumTypeAudio;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.mixin.access.SoundEngineAccessor;
import com.denfop.mixin.access.SoundManagerAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;

public class BlockEntitySound<T extends BlockEntityBase & AudioFixer> extends AbstractTickableSoundInstance {


    private final EnumTypeAudio prev;
    private final int subtitleFrequency;
    private T blockEntity;
    private int consecutiveTicks;


    public BlockEntitySound(@NotNull T audioFixer, @NotNull SoundEvent sound) {
        super(sound, SoundSource.BLOCKS, audioFixer.getLevel().getRandom());
        this.blockEntity = blockEntity;
        this.subtitleFrequency = 60;
        this.delay = 0;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.prev = audioFixer.getTypeAudio();
        this.x = blockEntity.getPos().getX();
        this.y = blockEntity.getPos().getY();
        this.z = blockEntity.getPos().getZ();
    }


    @Override
    public void tick() {
        if (blockEntity.isRemoved() || blockEntity.getTypeAudio() != prev) {
            stop();
            volume = 0.0F;
            consecutiveTicks = 0;
            return;
        }
        this.x = blockEntity.getPos().getX();
        this.y = blockEntity.getPos().getY();
        this.z = blockEntity.getPos().getZ();

        if (consecutiveTicks % subtitleFrequency == 0) {
            SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
            for (SoundEventListener soundEventListener : ((SoundEngineAccessor) ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine()).getListeners()) {
                WeighedSoundEvents soundEventAccessor = resolve(soundHandler);
                if (soundEventAccessor != null) {
                    soundEventListener.onPlaySound(this, soundEventAccessor);
                }
            }
            consecutiveTicks = 1;
        } else {
            consecutiveTicks++;
        }
    }


    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {

        return blockEntity.getTypeAudio() != EnumTypeAudio.OFF;
    }


}