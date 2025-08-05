package com.denfop.audio;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;

public class PlayerSound extends PositionedSound implements ITickableSound {

    private final EntityPlayer player;
    private boolean donePlaying = false;

    public PlayerSound(EntityPlayer player, ResourceLocation sound) {
        super(sound, SoundCategory.PLAYERS);
        this.player = player;
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.repeat = true;
        this.repeatDelay = 0;
        this.xPosF = (float) player.posX;
        this.yPosF = (float) player.posY;
        this.zPosF = (float) player.posZ;
    }

    @Override
    public void update() {
        if (player == null || player.isDead) {
            this.donePlaying = true;
            return;
        }

        this.xPosF = (float) player.posX;
        this.yPosF = (float) player.posY;
        this.zPosF = (float) player.posZ;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }
}
