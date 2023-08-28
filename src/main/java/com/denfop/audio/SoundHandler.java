package com.denfop.audio;

import com.denfop.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SoundHandler {

    @SideOnly(Side.CLIENT)
    public static void stopSound(BlockPos pos) {
        pos = pos.add(0.5, 0.5, 0.5);
        final SoundManager man = Minecraft.getMinecraft().getSoundHandler().sndManager;
        ISound sound = null;
        if (man.loaded) {
            for (Map.Entry<ISound, String> map : man.invPlayingSounds.entrySet()) {
                BlockPos pos1 = new BlockPos(map.getKey().getXPosF() - 0.5, map.getKey().getYPosF() - 0.5,
                        map.getKey().getZPosF() - 0.5
                );
                if (pos1.equals(pos)) {
                    sound = map.getKey();
                    break;
                }
            }
        }
        if (sound != null) {
            man.stopSound(sound);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void stopSound() {
        final SoundManager man = Minecraft.getMinecraft().getSoundHandler().sndManager;
        ISound sound = null;
        if (man.loaded) {
            for (Map.Entry<ISound, String> map : man.invPlayingSounds.entrySet()) {

                if (map.getKey().getCategory() == SoundCategory.PLAYERS && map
                        .getKey()
                        .getSoundLocation()
                        .getResourceDomain()
                        .equals(
                                Constants.MOD_ID)) {
                    sound = map.getKey();
                    break;
                }
            }
        }
        if (sound != null) {
            man.stopSound(sound);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void stopSound(EnumSound sound1) {
        final SoundManager man = Minecraft.getMinecraft().getSoundHandler().sndManager;
        List<ISound> sounds = new ArrayList<>();
        if (man.loaded) {
            for (Map.Entry<ISound, String> map : man.invPlayingSounds.entrySet()) {

                if (map.getKey().getCategory() == SoundCategory.PLAYERS && map
                        .getKey()
                        .getSoundLocation()
                        .getResourceDomain()
                        .equals(
                                Constants.MOD_ID) && map.getKey().getSoundLocation().getResourcePath().contains(sound1
                        .getNameSounds()
                        .toLowerCase())) {
                    sounds.add(map.getKey());
                    break;
                }
            }
        }
        sounds.forEach(man::stopSound);
    }

    @SideOnly(Side.CLIENT)
    public static void playSound(EntityPlayer player, EnumSound sound1) {
        final SoundManager man = Minecraft.getMinecraft().getSoundHandler().sndManager;
        boolean can = true;
        if (man.loaded) {
            for (Map.Entry<ISound, String> map : man.invPlayingSounds.entrySet()) {

                if (map.getKey().getCategory() == SoundCategory.PLAYERS && map
                        .getKey()
                        .getSoundLocation()
                        .getResourceDomain()
                        .equals(
                                Constants.MOD_ID) && map.getKey().getSoundLocation().getResourcePath().contains(sound1
                        .getNameSounds()
                        .toLowerCase())) {
                    can = false;
                    break;
                }
            }
        }
        if (can) {
            player.playSound(sound1.getSoundEvent(), 1, 1);

        }
    }

    @SideOnly(Side.CLIENT)
    public static void playSound(EntityPlayer player, String sound1) {
        final SoundManager man = Minecraft.getMinecraft().getSoundHandler().sndManager;
        boolean can = true;
        if (man.loaded) {
            for (Map.Entry<ISound, String> map : man.invPlayingSounds.entrySet()) {

                if (map.getKey().getCategory() == SoundCategory.PLAYERS && map
                        .getKey()
                        .getSoundLocation()
                        .getResourceDomain()
                        .equals(
                                Constants.MOD_ID) && map
                        .getKey()
                        .getSoundLocation()
                        .getResourcePath()
                        .contains(sound1.toLowerCase())) {
                    can = false;
                    break;
                }
            }
        }
        if (can) {
            player.playSound(EnumSound.getSondFromString(sound1), 1, 1);

        }
    }

}
