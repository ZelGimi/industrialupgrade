package com.denfop.sound;


import com.denfop.IUCore;
import com.denfop.mixin.access.SoundEngineAccess;
import com.denfop.mixin.access.SoundManagerAccess;
import com.mojang.blaze3d.audio.Channel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SoundHandler {

    @OnlyIn(Dist.CLIENT)
    public static void stopSound(BlockPos pos) {
        SoundEngine soundEngine = ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine();
        if (((SoundEngineAccess) soundEngine).getLoaded()) {
            sounds:
            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> map : ((SoundEngineAccess) soundEngine).getInstanceToChannel().entrySet()) {
                BlockPos pos1 = new BlockPos((int) map.getKey().getX(), (int) map.getKey().getY(),
                        (int) map.getKey().getZ()
                );
                if (pos1.getX() < 0) {
                    pos1 = pos1.offset(-1, 0, 0);
                }
                if (pos1.getZ() < 0) {
                    pos1 = pos1.offset(0, 0, -1);
                }
                if (pos1.equals(pos)) {
                    map.getValue().execute(Channel::stop);
                    break;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void stopSound() {
        SoundEngine soundEngine = ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine();
        if (((SoundEngineAccess) soundEngine).getLoaded()) {
            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> map : ((SoundEngineAccess) soundEngine).getInstanceToChannel().entrySet()) {

                if (map.getKey().getSource() == SoundSource.PLAYERS && map
                        .getKey()
                        .getLocation()
                        .getNamespace()
                        .equals(
                                IUCore.MODID)) {
                    map.getValue().execute(Channel::stop);
                    break;
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void stopSound(EnumSound sound1) {
        SoundEngine soundEngine = ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine();
        List<SoundInstance> sounds = new ArrayList<>();
        if (((SoundEngineAccess) soundEngine).getLoaded()) {
            for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> map : ((SoundEngineAccess) soundEngine).getInstanceToChannel().entrySet()) {

                if (map.getKey().getSource() == SoundSource.PLAYERS && map
                        .getKey()
                        .getLocation()
                        .getNamespace()
                        .equals(
                                IUCore.MODID) && map.getKey().getLocation().getPath().contains(sound1
                        .getNameSounds()
                        .toLowerCase())) {
                    sounds.add(map.getKey());
                    break;
                }
            }
        }
        sounds.forEach(soundEngine::stop);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound(Player player, EnumSound sound1) {
        SoundEngine soundEngine = ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine();
        boolean can = true;

        for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> map : ((SoundEngineAccess) soundEngine).getInstanceToChannel().entrySet()) {

            if (map.getKey().getSource() == SoundSource.PLAYERS && map
                    .getKey()
                    .getLocation()
                    .getNamespace()
                    .equals(
                            IUCore.MODID) && map.getKey().getLocation().getPath().contains(sound1
                    .getNameSounds()
                    .toLowerCase())) {
                can = false;
                break;
            }
        }

        if (can) {
            Minecraft.getInstance().getSoundManager().play(new PlayerSound(player, sound1.getSoundEvent()));


        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound(Player player, String sound1) {
        SoundEngine soundEngine = ((SoundManagerAccess) Minecraft.getInstance().getSoundManager()).getSoundEngine();
        boolean can = true;

        for (Map.Entry<SoundInstance, ChannelAccess.ChannelHandle> map : ((SoundEngineAccess) soundEngine).getInstanceToChannel().entrySet()) {

            if (map.getKey().getSource() == SoundSource.PLAYERS && map
                    .getKey()
                    .getLocation()
                    .getNamespace()
                    .equals(
                            IUCore.MODID) && map
                    .getKey()
                    .getLocation()
                    .getPath()
                    .contains(sound1.toLowerCase())) {
                can = false;
                break;
            }
        }

        if (can) {
            Minecraft.getInstance().getSoundManager().play(new PlayerSound(player, EnumSound.getSondFromString(sound1)));

        }
    }


}
