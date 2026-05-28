package com.denfop.render.rocketpad;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public final class RocketPadEffects {

    private static final DustParticleOptions ENGINE_GLOW =
            new DustParticleOptions(new Vector3f(1.0F, 0.25F, 0.05F), 1.2F);

    private RocketPadEffects() {
    }

    public static void tickEffects(Level level, RocketLaunchAnimation animation, RandomSource random) {
        long gameTime = level.getGameTime();
        float age = animation.getAge(0.0F, gameTime);
        float strength = animation.getEngineStrength(0.0F, gameTime);

        double x = animation.getRenderX(0.0F, gameTime) + 0.5D;
        double y = animation.getRenderY(0.0F, gameTime);
        double z = animation.getRenderZ(0.0F, gameTime) + 0.5D;

        int smokeCount = Mth.ceil(4 + strength * 8.0F);
        int flameCount = Mth.ceil(2 + strength * 5.0F);

        for (int i = 0; i < smokeCount; i++) {
            double ox = (random.nextDouble() - 0.5D) * 0.6D;
            double oz = (random.nextDouble() - 0.5D) * 0.6D;
            double mx = (random.nextDouble() - 0.5D) * 0.02D;
            double mz = (random.nextDouble() - 0.5D) * 0.02D;

            level.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    x + ox,
                    y - 0.2D,
                    z + oz,
                    mx,
                    -0.08D - strength * 0.05D,
                    mz
            );
        }

        for (int i = 0; i < flameCount; i++) {
            double ox = (random.nextDouble() - 0.5D) * 0.25D;
            double oz = (random.nextDouble() - 0.5D) * 0.25D;

            level.addParticle(
                    ParticleTypes.FLAME,
                    x + ox,
                    y - 0.15D,
                    z + oz,
                    0.0D,
                    -0.03D - strength * 0.03D,
                    0.0D
            );
        }

        if (((int) age) % 4 == 0) {
            double ox = (random.nextDouble() - 0.5D) * 0.4D;
            double oz = (random.nextDouble() - 0.5D) * 0.4D;

            level.addParticle(
                    ENGINE_GLOW,
                    x + ox,
                    y - 0.1D,
                    z + oz,
                    0.02D,
                    0.0D,
                    0.0D
            );
        }

        if (((int) age) % 20 == 0 && age > 10) {
            level.addParticle(
                    ParticleTypes.EXPLOSION,
                    x,
                    y - 0.2D,
                    z,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }
}