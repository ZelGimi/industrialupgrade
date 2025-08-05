package com.denfop.audio;


import com.denfop.IUCore;
import com.denfop.mixin.access.DeferredRegisterAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Sounds {

    public static void registerSounds(DeferredRegister<SoundEvent> registry) {
        for (EnumSound enumSound : EnumSound.values()) {
            enumSound.setSoundEvent(registerSound(enumSound.getSoundName().toLowerCase(), registry));
        }
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String soundName, DeferredRegister<SoundEvent> registry) {
        ResourceLocation soundID = ResourceLocation.tryBuild(IUCore.MODID, soundName);
        DeferredHolder<SoundEvent, SoundEvent> ret = DeferredHolder.create(registry.getRegistryKey(), soundID);
        var entries = ((DeferredRegisterAccessor) registry).getEntries();
        Supplier<SoundEvent> supplier = () -> SoundEvent.createVariableRangeEvent(soundID);
        if (entries.putIfAbsent(ret, supplier) != null) {
            throw new IllegalArgumentException("Duplicate registration " + ret);
        }
        return ret;
    }

}
