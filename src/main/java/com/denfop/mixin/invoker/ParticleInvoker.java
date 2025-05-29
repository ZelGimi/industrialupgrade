package com.denfop.mixin.invoker;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Particle.class)
public interface ParticleInvoker {

    @Invoker("setAlpha")
    void invokeSetAlpha(float p_107272_);
}
