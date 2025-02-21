package com.denfop.effects;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SparkParticle extends Particle {
    public SparkParticle(World world, double x, double y, double z) {
        super(world, x, y, z);

        // Яркие цвета для раскаленных искр
        this.particleRed = 1.0f;
        this.particleGreen = 0.5f + rand.nextFloat() * 0.2f;
        this.particleBlue = 0.1f;
        this.particleAlpha = 0.9f;

        // Настройки движения искр
        this.motionX = (rand.nextDouble() - 0.5) * 0.1; // Легкое боковое движение
        this.motionY = 0.08 + rand.nextDouble() * 0.1;  // Поднимаются вверх
        this.motionZ = (rand.nextDouble() - 0.5) * 0.1;
        this.particleMaxAge = 20 + rand.nextInt(15); // Время жизни
        this.particleGravity = 0.02f; // Легкое влияние гравитации
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        // Постепенное уменьшение прозрачности и размера
        this.particleAlpha *= 0.95f;
        this.particleScale *= 0.98f;

        // Постепенное изменение цвета к темному по мере старения частицы
        if (this.particleAge > this.particleMaxAge / 2) {
            this.particleRed *= 0.9f;
            this.particleGreen *= 0.6f;
            this.particleBlue *= 0.5f;
        }
    }
}
