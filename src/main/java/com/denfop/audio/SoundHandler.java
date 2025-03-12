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
import java.util.Optional;

@SideOnly(Side.CLIENT)
public class SoundHandler {

    private static final SoundManager soundManager = Minecraft.getMinecraft().getSoundHandler().sndManager;


    public static void stopSound(BlockPos pos) {
        getSoundAtPosition(pos).ifPresent(soundManager::stopSound);
    }


    public static void stopAllSounds() {
        getAllSoundsFromMod().forEach(soundManager::stopSound);
    }


    public static void stopSound(EnumSound sound) {
        getSoundsMatching(sound.getNameSounds().toLowerCase()).forEach(soundManager::stopSound);
    }


    public static void playSound(EntityPlayer player, Object sound) {
        if (player == null || sound == null) return;

        String soundName = (sound instanceof EnumSound)
                ? ((EnumSound) sound).getNameSounds().toLowerCase()
                : sound.toString().toLowerCase();

        if (!isSoundPlaying(soundName)) {
            player.playSound(EnumSound.getSondFromString(soundName), 1.0F, 1.0F);
        }
    }


    private static boolean isSoundPlaying(String soundName) {
        return soundManager.invPlayingSounds.keySet().stream()
                .anyMatch(sound -> isSoundFromMod(sound) && sound.getSoundLocation().getResourcePath().contains(soundName));
    }

    private static List<ISound> getAllSoundsFromMod() {
        List<ISound> sounds = new ArrayList<>();
        soundManager.invPlayingSounds.keySet().stream()
                .filter(SoundHandler::isSoundFromMod)
                .forEach(sounds::add);
        return sounds;
    }


    private static List<ISound> getSoundsMatching(String soundName) {
        List<ISound> sounds = new ArrayList<>();
        soundManager.invPlayingSounds.keySet().stream()
                .filter(sound -> isSoundFromMod(sound) && sound.getSoundLocation().getResourcePath().contains(soundName))
                .forEach(sounds::add);
        return sounds;
    }


    private static Optional<ISound> getSoundAtPosition(BlockPos pos) {
        return soundManager.invPlayingSounds.keySet().stream()
                .filter(sound -> isSoundFromMod(sound) && isSoundAtPosition(sound, pos))
                .findFirst();
    }


    private static boolean isSoundFromMod(ISound sound) {
        return sound.getCategory() == SoundCategory.PLAYERS &&
                sound.getSoundLocation().getResourceDomain().equals(Constants.MOD_ID);
    }


    private static boolean isSoundAtPosition(ISound sound, BlockPos pos) {
        return new BlockPos(sound.getXPosF(), sound.getYPosF(), sound.getZPosF()).equals(pos);
    }
}
