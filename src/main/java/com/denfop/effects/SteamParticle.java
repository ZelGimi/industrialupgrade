package com.denfop.effects;

import com.denfop.api.windsystem.WindSystem;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SteamParticle extends Particle {

    private final float smokeParticleScale;

    public SteamParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn + 0.5, posYIn, posZIn + 0.5, 0, 0.1, 0);
        this.particleGravity = 0.1F;
        this.particleMaxAge = 20;
        this.particleScale *= 0.75F;
        this.smokeParticleScale = this.particleScale;
    }

    public int getFXLayer() {
        return 0;
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
        float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        this.particleScale = this.smokeParticleScale * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    @Override
    public void onUpdate() {


        Vec3d vec3d = WindSystem.windSystem.getWindSide().getDirectionVector();
        double windDirectionX = vec3d.x;
        double windDirectionZ = vec3d.z;
        double windSpeed = WindSystem.windSystem.getSpeed() / 32F;
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionX += windDirectionX * windSpeed;
        this.motionZ += windDirectionZ * windSpeed;
        super.onUpdate();
    }

}
