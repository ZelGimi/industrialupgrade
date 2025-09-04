package com.denfop.sound;

import com.denfop.mixin.access.SoundEngineAccessor;
import com.denfop.mixin.access.SoundManagerAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSound extends AbstractTickableSoundInstance {


    private final int subtitleFrequency;
    private Player player;
    private int consecutiveTicks;


    public PlayerSound(@NotNull Player player, @NotNull SoundEvent sound) {
        super(sound, SoundSource.PLAYERS, player.level().getRandom());
        this.player = player;
        this.subtitleFrequency = 60;
        this.delay = 0;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Nullable
    private Player getPlayer() {
        return player;
    }


    @Override
    public void tick() {
        Player player = getPlayer();
        if (player == null || !player.isAlive() || player.isRemoved()) {
            stop();
            volume = 0.0F;
            consecutiveTicks = 0;
            return;
        }
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();

        if (consecutiveTicks % subtitleFrequency == 0) {
            SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
            for (SoundEventListener soundEventListener : ((SoundEngineAccessor) ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine()).getListeners()) {
                WeighedSoundEvents soundEventAccessor = resolve(soundHandler);
                if (soundEventAccessor != null) {
                    soundEventListener.onPlaySound(this, soundEventAccessor, volume);
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
        Player player = getPlayer();
        if (player == null) {
            return super.canPlaySound();
        }
        return !player.isSilent();
    }


}