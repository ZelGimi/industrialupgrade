package com.denfop.effects;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class EffectsRegister {
    public static RegistryObject<SimpleParticleType> STEAM_ASH;
    public static RegistryObject<SimpleParticleType> ANVIL;

    public static void register(DeferredRegister<ParticleType<?>> PARTICLES) {
        STEAM_ASH = PARTICLES.register("steam_ash", () -> new SimpleParticleType(true));
        ANVIL = PARTICLES.register("anvil", () -> new SimpleParticleType(true));
    }
}
