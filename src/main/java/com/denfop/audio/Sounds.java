package com.denfop.audio;


import com.denfop.IUCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class Sounds {

    public static void registerSounds(DeferredRegister<SoundEvent> registry) {
        for (EnumSound enumSound : EnumSound.values()) {
            enumSound.setSoundEvent(registerSound(enumSound.getSoundName().toLowerCase(), registry));
        }
    }

    private static RegistryObject<SoundEvent> registerSound(String soundName, DeferredRegister<SoundEvent> registry) {
        ResourceLocation soundID = new ResourceLocation(IUCore.MODID, soundName);
        RegistryObject<SoundEvent> ret = RegistryObject.create(soundID, registry.getRegistryKey(), IUCore.MODID);
        var entries = ((DeferredRegisterAccessor) registry).getEntries();
        Supplier<SoundEvent> supplier = () -> SoundEvent.createVariableRangeEvent(soundID);
        if (entries.putIfAbsent(ret, supplier) != null) {
            throw new IllegalArgumentException("Duplicate registration " + ret);
        }
        return ret;
    }

}
