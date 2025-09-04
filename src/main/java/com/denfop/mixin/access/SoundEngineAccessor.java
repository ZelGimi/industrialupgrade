package com.denfop.mixin.access;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor
    List<SoundEventListener> getListeners();
}
