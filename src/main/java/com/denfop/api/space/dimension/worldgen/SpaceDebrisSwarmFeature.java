package com.denfop.api.space.dimension.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SpaceDebrisSwarmFeature extends AbstractSpaceExoticFeature<SpaceDebrisSwarmFeature.Config> {
    public SpaceDebrisSwarmFeature() {
        super(Config.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        SpaceMaterialSet materials = this.materials(context, this.traits(context));
        Config config = context.config();
        BlockPos base = this.surfaceOrigin(context);
        long seed = context.random().nextLong();
        int radius = config.radius();

        for (int i = 0; i < config.count(); i++) {
            int x = base.getX() + SpaceShapeMath.hashInt(seed + i * 17L, radius * 2 + 1) - radius;
            int z = base.getZ() + SpaceShapeMath.hashInt(seed + i * 31L, radius * 2 + 1) - radius;
            int y = base.getY() + SpaceShapeMath.hashInt(seed + i * 73L, radius + 1) - radius / 3;
            BlockPos pos = new BlockPos(x, y, z);
            SpaceCarvingSupport.fillEllipsoid(context.level(), net.minecraft.world.phys.Vec3.atCenterOf(pos), 1.0D + SpaceShapeMath.unit(seed + i * 101L) * 2.1D, 0.8D, 1.0D + SpaceShapeMath.unit(seed + i * 151L) * 2.1D, seed ^ i, config.roughness(), i % 3 == 0 ? materials.accent() : materials.stone());
        }

        return true;
    }

    public record Config(int count, int radius, double roughness) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("count").forGetter(Config::count),
                Codec.INT.fieldOf("radius").forGetter(Config::radius),
                Codec.DOUBLE.fieldOf("roughness").forGetter(Config::roughness)
        ).apply(instance, Config::new));
    }
}
