package com.denfop.audio;

import com.denfop.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class Sounds {

    public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
        for (EnumSound enumSound : EnumSound.values()) {
            enumSound.setSoundEvent(registerSound(enumSound.getSoundName(), registry));
        }
    }

    private static SoundEvent registerSound(String soundName, IForgeRegistry<SoundEvent> registry) {
        ResourceLocation soundID = new ResourceLocation(Constants.MOD_ID, soundName);
        SoundEvent result = (new SoundEvent(soundID)).setRegistryName(soundID);
        registry.register(result);
        return result;
    }

}
