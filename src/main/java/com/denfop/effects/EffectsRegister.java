package com.denfop.effects;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectsRegister {


    public static DeferredHolder<ParticleType<?>, SimpleParticleType> STEAM_ASH;
    public static DeferredHolder<ParticleType<?>, SimpleParticleType> ANVIL;

    public static void register(DeferredRegister<ParticleType<?>> PARTICLES) {
        STEAM_ASH = PARTICLES.register("steam_ash", () -> new SimpleParticleType(true));
        ANVIL = PARTICLES.register("anvil", () -> new SimpleParticleType(true));
    }
}
