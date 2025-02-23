package com.denfop.effects;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SmokeParticle extends Particle {
    public SmokeParticle(World world, double x, double y, double z) {
        super(world, x, y, z);

        // Темный дым для реалистичного эффекта
        this.particleRed = 0.2f;
        this.particleGreen = 0.2f;
        this.particleBlue = 0.2f;
        this.particleAlpha = 0.8f;

        // Медленное, стабильное движение
        this.motionX = (rand.nextDouble() - 0.5) * 0.02;
        this.motionY = 0.05;
        this.motionZ = (rand.nextDouble() - 0.5) * 0.02;
        this.particleMaxAge = 60 + rand.nextInt(20); // Более долгое время жизни
        this.particleGravity = 0.0f; // Дым не подвержен гравитации
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        // Плавное уменьшение прозрачности к концу жизни
        if (this.particleAge > this.particleMaxAge / 2) {
            this.particleAlpha -= 0.01f;
        }
    }
}
