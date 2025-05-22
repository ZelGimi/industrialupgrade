package com.denfop.effects;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class CustomPollutionParticle extends Particle {

    private final float flameScale;

    public CustomPollutionParticle(World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x + 0.5, y, z + 0.5, motionX, motionY, motionZ);

        this.flameScale = this.particleScale;
        this.particleMaxAge = 16;
        this.setParticleTextureIndex(48);
    }

    public void renderParticle(
            BufferBuilder buffer,
            Entity entityIn,
            float partialTicks,
            float rotationX,
            float rotationZ,
            float rotationYZ,
            float rotationXY,
            float rotationXZ
    ) {
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
        this.particleScale = this.flameScale * (1.0F - f * f * 0.5F);
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    public void onUpdate() {

        super.onUpdate();
    }

}
