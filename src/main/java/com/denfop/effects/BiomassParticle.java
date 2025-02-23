package com.denfop.effects;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BiomassParticle extends Particle {
    public BiomassParticle(World world, double x, double y, double z) {
        super(world, x, y, z);


        this.particleRed = 0.3f;
        this.particleGreen = 0.8f;
        this.particleBlue = 0.3f;
        this.particleAlpha = 0.75f;


        this.motionX = (rand.nextDouble() - 0.5) * 0.02;
        this.motionY = 0.01;
        this.motionZ = (rand.nextDouble() - 0.5) * 0.02;
        this.particleMaxAge = 40 + rand.nextInt(20);
        this.particleGravity = 0.0f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();


        if (this.particleAge > this.particleMaxAge / 2) {
            this.particleAlpha -= 0.02f;
        }
    }
}
