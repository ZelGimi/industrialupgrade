package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.phys.Vec3;

public class SpaceDistortedSpireFeature extends AbstractSpaceExoticFeature<SpaceDistortedSpireFeature.Config> {
    public SpaceDistortedSpireFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        SpaceMaterialSet materials = this.materials(context, this.traits(context));
        Config config = context.config();
        Vec3 cursor = Vec3.atCenterOf(this.surfaceOrigin(context));
        long seed = context.random().nextLong();

        for (int y = 0; y < config.height(); y += 2) {
            double progress = y / (double) config.height();
            double angle = progress * Math.PI * 2.0D * config.twist();
            Vec3 offset = new Vec3(Math.cos(angle) * config.radius() * progress, 0.0D, Math.sin(angle) * config.radius() * progress);
            Vec3 slice = cursor.add(offset).add(0.0D, y, 0.0D);
            double localRadius = config.radius() * (1.0D - progress * 0.55D);
            SpaceCarvingSupport.fillEllipsoid(context.level(), slice, localRadius, 1.2D, localRadius, seed + y, config.roughness(), progress > 0.6D ? materials.accent() : materials.stone());
        }

        return true;
    }

    public record Config(int height, double radius, double twist, double roughness) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("height").forGetter(Config::height),
                Codec.DOUBLE.fieldOf("radius").forGetter(Config::radius),
                Codec.DOUBLE.fieldOf("twist").forGetter(Config::twist),
                Codec.DOUBLE.fieldOf("roughness").forGetter(Config::roughness)
        ).apply(instance, Config::new));
    }
}
