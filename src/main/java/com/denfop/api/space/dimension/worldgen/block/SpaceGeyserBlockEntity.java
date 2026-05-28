package com.denfop.api.space.dimension.worldgen.block;

import com.denfop.api.space.dimension.SpaceBodyProfiles;
import com.denfop.api.space.dimension.SpaceDimensionProfile;
import com.denfop.api.space.dimension.SpaceVentType;
import com.denfop.api.space.dimension.worldgen.SpaceMaterialSet;
import com.denfop.api.space.dimension.worldgen.SpacePlanetTraits;
import com.denfop.api.space.dimension.worldgen.SpaceWorldgenContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

import static com.denfop.api.space.dimension.worldgen.AbstractSpaceExoticFeature.resolveProfile;

public class SpaceGeyserBlockEntity extends BlockEntity {

    private static final String TAG_TIMER = "Timer";
    private static final String TAG_ACTIVE = "Active";
    private static final String TAG_SOURCE_STATE = "SourceState";
    private static final String TAG_SOURCE_INITIALIZED = "SourceInitialized";

    private static final double TWO_PI = Math.PI * 2.0D;

    private static final double LOD_NEAR_DIST_SQ = 14.0D * 14.0D;
    private static final double LOD_MID_DIST_SQ = 28.0D * 28.0D;
    private static final double LOD_FAR_DIST_SQ = 42.0D * 42.0D;
    private static final double LOD_MAX_DIST_SQ = 56.0D * 56.0D;

    private static final ParticleLod LOD_NEAR = new ParticleLod(
            0.55D,
            2,
            true,
            true,
            true,
            0.55D,
            0.70D
    );

    private static final ParticleLod LOD_MID = new ParticleLod(
            0.30D,
            4,
            true,
            false,
            false,
            0.32D,
            0.0D
    );

    private static final ParticleLod LOD_FAR = new ParticleLod(
            0.14D,
            7,
            false,
            false,
            false,
            0.14D,
            0.0D
    );

    private static final GeyserVisualProfile[] VISUAL_PROFILES = buildProfiles();
    SpaceDimensionProfile profile;
    private int timer;
    private boolean active;
    private BlockState sourceState = Blocks.STONE.defaultBlockState();
    private boolean sourceInitialized = false;
    private BlockState cachedPlanetSurfaceState = Blocks.STONE.defaultBlockState();
    private boolean cachedPlanetSurfaceResolved = false;
    private int clientBurstCooldown = 0;

    public SpaceGeyserBlockEntity(final BlockPos pos, final BlockState state) {
        super(SpaceWorldgenContent.SPACE_GEYSER_BE.get(), pos, state);
    }

    public static void serverTick(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final SpaceGeyserBlockEntity be
    ) {
        be.timer++;
        if (be.timer >= 100 + level.random.nextInt(120)) {
            be.timer = 0;
            be.active = !be.active;
            if (state.getValue(SpaceGeyserBlock.ACTIVE) != be.active) {
                level.setBlock(pos, state.setValue(SpaceGeyserBlock.ACTIVE, be.active), 3);
            }
        }

        if (!be.active || level.getGameTime() % 5L != 0L) {
            return;
        }

        for (Entity entity : level.getEntities(null, new net.minecraft.world.phys.AABB(pos).inflate(1.2D, 2.6D, 1.2D))) {
            entity.push(0.0D, 0.25D + level.random.nextDouble() * 0.15D, 0.0D);
            if (entity instanceof LivingEntity living) {
                switch (state.getValue(SpaceGeyserBlock.VENT_TYPE)) {
                    case LAVA -> living.hurt(level.damageSources().lava(), 2.0F);
                    case ACID -> living.hurt(level.damageSources().magic(), 1.0F);
                    default -> {
                    }
                }
            }
        }

        if (level.random.nextInt(5) == 0) {
            level.playSound(
                    null,
                    pos,
                    soundFor(state.getValue(SpaceGeyserBlock.VENT_TYPE)),
                    SoundSource.BLOCKS,
                    0.8F,
                    0.9F + level.random.nextFloat() * 0.2F
            );
        }
    }

    public static void clientTick(
            final Level level,
            final BlockPos pos,
            final BlockState state,
            final SpaceGeyserBlockEntity be
    ) {
        if (!state.getValue(SpaceGeyserBlock.ACTIVE)) {
            return;
        }

        final double centerX = pos.getX() + 0.5D;
        final double centerZ = pos.getZ() + 0.5D;
        final ParticleLod lod = resolveLod(level, centerX, pos.getY() + 0.5D, centerZ);

        if (lod == null) {
            return;
        }

        if (be.clientBurstCooldown > 0) {
            be.clientBurstCooldown--;
        }

        final int phase = particlePhase(pos);
        final long tick = level.getGameTime();

        if ((tick + phase) % lod.emissionInterval != 0L) {
            return;
        }

        final SpaceVentType type = state.getValue(SpaceGeyserBlock.VENT_TYPE);
        final GeyserVisualProfile profile = VISUAL_PROFILES[type.ordinal()];

        final double centerY = pos.getY() + profile.baseSpawnY;
        final int cycle = (int) ((tick / lod.emissionInterval + phase) % 3L);

        switch (cycle) {
            case 0 -> spawnCoreColumn(level, centerX, centerY, centerZ, type, profile, lod);
            case 1 -> {
                if (lod.outerEnabled) {
                    spawnOuterColumn(level, centerX, centerY, centerZ, type, profile, lod);
                } else {
                    spawnCoreColumn(level, centerX, centerY, centerZ, type, profile, lod);
                }
            }
            default -> {
                if (lod.topEnabled) {
                    spawnTopMist(level, centerX, centerY, centerZ, type, profile, lod);
                } else {
                    spawnCoreColumn(level, centerX, centerY, centerZ, type, profile, lod);
                }
            }
        }

        if (lod.burstEnabled && be.clientBurstCooldown <= 0) {
            final float chance = (float) (profile.burstChance * lod.burstChanceScale);
            if (level.random.nextFloat() < chance) {
                spawnBurst(level, centerX, centerY, centerZ, type, profile, lod);
                be.clientBurstCooldown = profile.burstCooldownMin + level.random.nextInt(profile.burstCooldownRandom + 1);
            }
        }
    }

    private static void spawnCoreColumn(
            final Level level,
            final double centerX,
            final double centerY,
            final double centerZ,
            final SpaceVentType type,
            final GeyserVisualProfile profile,
            final ParticleLod lod
    ) {
        final int count = scaledCount(
                level.random,
                profile.coreCountMin,
                profile.coreCountMax,
                lod.countScale,
                1
        );

        for (int i = 0; i < count; i++) {
            final double angle = level.random.nextDouble() * TWO_PI;
            final double radius = level.random.nextDouble() * profile.coreRadius;

            final double x = centerX + Math.cos(angle) * radius;
            final double y = centerY + level.random.nextDouble() * profile.baseHeightSpread;
            final double z = centerZ + Math.sin(angle) * radius;

            final double vx = (level.random.nextDouble() - 0.5D) * profile.horizontalDrift * 0.65D;
            final double vy = profile.minUpSpeed + level.random.nextDouble() * (profile.maxUpSpeed - profile.minUpSpeed);
            final double vz = (level.random.nextDouble() - 0.5D) * profile.horizontalDrift * 0.65D;

            spawnMainParticles(level, type, x, y, z, vx, vy, vz, lod.secondaryChanceScale);
        }
    }

    private static void spawnOuterColumn(
            final Level level,
            final double centerX,
            final double centerY,
            final double centerZ,
            final SpaceVentType type,
            final GeyserVisualProfile profile,
            final ParticleLod lod
    ) {
        final int count = scaledCount(
                level.random,
                profile.outerCountMin,
                profile.outerCountMax,
                lod.countScale * 0.65D,
                1
        );

        for (int i = 0; i < count; i++) {
            final double angle = (TWO_PI / Math.max(1, count)) * i + level.random.nextDouble() * 0.25D;
            final double radius = profile.outerRadiusMin
                    + level.random.nextDouble() * (profile.outerRadiusMax - profile.outerRadiusMin);

            final double x = centerX + Math.cos(angle) * radius;
            final double y = centerY + level.random.nextDouble() * (profile.baseHeightSpread + 0.10D);
            final double z = centerZ + Math.sin(angle) * radius;

            final double inwardFactor = 0.010D + level.random.nextDouble() * 0.018D;
            final double vx = -Math.cos(angle) * inwardFactor
                    + (level.random.nextDouble() - 0.5D) * profile.outerHorizontalNoise * 0.55D;
            final double vy = profile.outerMinUpSpeed
                    + level.random.nextDouble() * (profile.outerMaxUpSpeed - profile.outerMinUpSpeed);
            final double vz = -Math.sin(angle) * inwardFactor
                    + (level.random.nextDouble() - 0.5D) * profile.outerHorizontalNoise * 0.55D;

            spawnMainParticles(level, type, x, y, z, vx, vy, vz, lod.secondaryChanceScale * 0.75D);
        }
    }

    private static void spawnTopMist(
            final Level level,
            final double centerX,
            final double centerY,
            final double centerZ,
            final SpaceVentType type,
            final GeyserVisualProfile profile,
            final ParticleLod lod
    ) {
        final int count = scaledCount(
                level.random,
                profile.topMistCountMin,
                profile.topMistCountMax,
                lod.countScale * 0.22D,
                1
        );

        for (int i = 0; i < count; i++) {
            final double angle = level.random.nextDouble() * TWO_PI;
            final double radius = level.random.nextDouble() * profile.topMistRadius;

            final double x = centerX + Math.cos(angle) * radius;
            final double y = centerY + profile.topMistBaseHeight
                    + level.random.nextDouble() * profile.topMistHeightSpread;
            final double z = centerZ + Math.sin(angle) * radius;

            final double vx = (level.random.nextDouble() - 0.5D) * profile.topMistHorizontalSpeed * 0.65D;
            final double vy = profile.topMistUpSpeedMin
                    + level.random.nextDouble() * (profile.topMistUpSpeedMax - profile.topMistUpSpeedMin);
            final double vz = (level.random.nextDouble() - 0.5D) * profile.topMistHorizontalSpeed * 0.65D;

            spawnTopParticles(level, type, x, y, z, vx, vy, vz, lod.secondaryChanceScale);
        }
    }

    private static void spawnBurst(
            final Level level,
            final double centerX,
            final double centerY,
            final double centerZ,
            final SpaceVentType type,
            final GeyserVisualProfile profile,
            final ParticleLod lod
    ) {
        final int count = scaledCount(
                level.random,
                profile.burstCountMin,
                profile.burstCountMax,
                lod.countScale * 0.55D,
                2
        );

        for (int i = 0; i < count; i++) {
            final double angle = level.random.nextDouble() * TWO_PI;
            final double radius = level.random.nextDouble() * profile.burstRadius;

            final double x = centerX + Math.cos(angle) * radius;
            final double y = centerY + 0.04D + level.random.nextDouble() * 0.08D;
            final double z = centerZ + Math.sin(angle) * radius;

            final double vx = (level.random.nextDouble() - 0.5D) * profile.burstHorizontalSpeed;
            final double vy = profile.burstMinUpSpeed
                    + level.random.nextDouble() * (profile.burstMaxUpSpeed - profile.burstMinUpSpeed);
            final double vz = (level.random.nextDouble() - 0.5D) * profile.burstHorizontalSpeed;

            spawnBurstParticles(level, type, x, y, z, vx, vy, vz, lod.secondaryChanceScale);
        }
    }

    private static void spawnMainParticles(
            final Level level,
            final SpaceVentType type,
            final double x,
            final double y,
            final double z,
            final double vx,
            final double vy,
            final double vz,
            final double secondaryChanceScale
    ) {
        switch (type) {
            case LAVA -> {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, vx * 0.42D, vy, vz * 0.42D);
                if (level.random.nextFloat() < 0.18F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.LAVA, x, y, z, vx * 0.18D, vy * 0.32D, vz * 0.18D);
                }
                if (level.random.nextFloat() < 0.08F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.FLAME, x, y, z, vx * 0.14D, vy * 0.18D, vz * 0.14D);
                }
            }
            case ACID -> {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, vx * 0.40D, vy * 0.88D, vz * 0.40D);
                if (level.random.nextFloat() < 0.28F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SPIT, x, y, z, vx * 0.50D, vy, vz * 0.50D);
                }
            }
            case CRYO -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.35D, vy * 0.68D, vz * 0.35D);
                if (level.random.nextFloat() < 0.38F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, vx * 0.45D, vy, vz * 0.45D);
                }
            }
            case GAS -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.42D, vy, vz * 0.42D);
                if (level.random.nextFloat() < 0.24F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.POOF, x, y, z, vx * 0.28D, vy * 0.60D, vz * 0.28D);
                }
            }
            case STEAM -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.40D, vy, vz * 0.40D);
                if (level.random.nextFloat() < 0.18F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, vx * 0.10D, vy * 0.24D, vz * 0.10D);
                }
            }
        }
    }

    private static void spawnTopParticles(
            final Level level,
            final SpaceVentType type,
            final double x,
            final double y,
            final double z,
            final double vx,
            final double vy,
            final double vz,
            final double secondaryChanceScale
    ) {
        switch (type) {
            case LAVA -> {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, vx, vy, vz);
                if (level.random.nextFloat() < 0.05F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.FLAME, x, y, z, vx * 0.15D, vy * 0.12D, vz * 0.15D);
                }
            }
            case ACID -> {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, vx * 0.34D, vy * 0.70D, vz * 0.34D);
                if (level.random.nextFloat() < 0.18F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SPIT, x, y, z, vx * 0.44D, vy, vz * 0.44D);
                }
            }
            case CRYO -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx, vy * 0.72D, vz);
                if (level.random.nextFloat() < 0.28F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, vx * 0.38D, vy, vz * 0.38D);
                }
            }
            case GAS -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx, vy, vz);
                if (level.random.nextFloat() < 0.20F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.POOF, x, y, z, vx * 0.36D, vy * 0.54D, vz * 0.36D);
                }
            }
            case STEAM -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx, vy, vz);
                if (level.random.nextFloat() < 0.10F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, vx * 0.08D, vy * 0.16D, vz * 0.08D);
                }
            }
        }
    }

    private static void spawnBurstParticles(
            final Level level,
            final SpaceVentType type,
            final double x,
            final double y,
            final double z,
            final double vx,
            final double vy,
            final double vz,
            final double secondaryChanceScale
    ) {
        switch (type) {
            case LAVA -> {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, vx * 0.40D, vy * 0.72D, vz * 0.40D);
                if (level.random.nextFloat() < 0.22F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.LAVA, x, y, z, vx * 0.20D, vy * 0.26D, vz * 0.20D);
                }
            }
            case ACID -> {
                if (level.random.nextFloat() < 0.35F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SPIT, x, y, z, vx * 0.58D, vy, vz * 0.58D);
                }
            }
            case CRYO -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.30D, vy * 0.58D, vz * 0.30D);
                if (level.random.nextFloat() < 0.30F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.SNOWFLAKE, x, y, z, vx * 0.46D, vy, vz * 0.46D);
                }
            }
            case GAS -> {
                level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.38D, vy, vz * 0.38D);
                if (level.random.nextFloat() < 0.22F * secondaryChanceScale) {
                    level.addParticle(ParticleTypes.POOF, x, y, z, vx * 0.46D, vy * 0.70D, vz * 0.46D);
                }
            }
            case STEAM -> level.addParticle(ParticleTypes.CLOUD, x, y, z, vx * 0.44D, vy, vz * 0.44D);
        }
    }

    private static SoundEvent soundFor(final SpaceVentType type) {
        return switch (type) {
            case LAVA -> SoundEvents.LAVA_POP;
            case ACID -> SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE;
            case CRYO -> SoundEvents.POWDER_SNOW_BREAK;
            case GAS, STEAM -> SoundEvents.FIRE_EXTINGUISH;
        };
    }

    private static boolean isValidVisualSource(final BlockState state) {
        return state != null
                && !state.isAir()
                && !state.is(SpaceWorldgenContent.SPACE_GEYSER.get());
    }

    @Nullable
    private static ParticleLod resolveLod(
            final Level level,
            final double x,
            final double y,
            final double z
    ) {
        final Player player = level.getNearestPlayer(x, y, z, 56.0D, false);
        if (player == null) {
            return null;
        }

        final double distSq = player.distanceToSqr(x, y, z);

        if (distSq <= LOD_NEAR_DIST_SQ) {
            return LOD_NEAR;
        }
        if (distSq <= LOD_MID_DIST_SQ) {
            return LOD_MID;
        }
        if (distSq <= LOD_FAR_DIST_SQ) {
            return LOD_FAR;
        }
        if (distSq <= LOD_MAX_DIST_SQ) {
            return LOD_FAR;
        }

        return null;
    }

    private static int scaledCount(
            final net.minecraft.util.RandomSource random,
            final int min,
            final int max,
            final double scale,
            final int floor
    ) {
        final int raw = min + (max > min ? random.nextInt(max - min + 1) : 0);
        final int scaled = (int) Math.floor(raw * scale);
        return Math.max(floor, scaled);
    }

    private static int particlePhase(final BlockPos pos) {
        int hash = pos.getX() * 73428767 ^ pos.getY() * 9122713 ^ pos.getZ() * 43828937;
        hash ^= (hash >>> 16);
        hash *= 0x7feb352d;
        hash ^= (hash >>> 15);
        return (hash & Integer.MAX_VALUE) % 53;
    }

    private static GeyserVisualProfile[] buildProfiles() {
        final GeyserVisualProfile[] profiles = new GeyserVisualProfile[SpaceVentType.values().length];

        profiles[SpaceVentType.LAVA.ordinal()] = new GeyserVisualProfile(
                0.82D,
                0.18D,
                0.17D,
                0.34D,
                0.48D,
                10,
                14,
                0.24D,
                0.30D,
                0.62D,
                0.12D,
                0.24D,
                8,
                12,
                1.25D,
                0.90D,
                0.34D,
                0.018D,
                0.07D,
                8,
                12,
                0.26D,
                0.34D,
                0.18D,
                0.18D,
                6,
                10,
                0.18D,
                0.34D,
                0.48D,
                18,
                26
        );

        profiles[SpaceVentType.ACID.ordinal()] = new GeyserVisualProfile(
                0.84D,
                0.16D,
                0.14D,
                0.30D,
                0.40D,
                8,
                12,
                0.20D,
                0.26D,
                0.54D,
                0.12D,
                0.22D,
                8,
                12,
                1.05D,
                0.74D,
                0.28D,
                0.016D,
                0.05D,
                8,
                12,
                0.22D,
                0.30D,
                0.12D,
                0.16D,
                5,
                8,
                0.16D,
                0.28D,
                0.38D,
                20,
                28
        );

        profiles[SpaceVentType.CRYO.ordinal()] = new GeyserVisualProfile(
                0.80D,
                0.18D,
                0.16D,
                0.26D,
                0.36D,
                9,
                14,
                0.24D,
                0.30D,
                0.66D,
                0.14D,
                0.24D,
                8,
                14,
                1.32D,
                0.98D,
                0.36D,
                0.020D,
                0.08D,
                9,
                14,
                0.24D,
                0.34D,
                0.16D,
                0.18D,
                6,
                10,
                0.18D,
                0.30D,
                0.42D,
                16,
                24
        );

        profiles[SpaceVentType.GAS.ordinal()] = new GeyserVisualProfile(
                0.82D,
                0.18D,
                0.18D,
                0.28D,
                0.38D,
                10,
                16,
                0.28D,
                0.34D,
                0.74D,
                0.16D,
                0.28D,
                10,
                16,
                1.52D,
                1.08D,
                0.42D,
                0.024D,
                0.10D,
                10,
                16,
                0.26D,
                0.36D,
                0.14D,
                0.20D,
                6,
                10,
                0.20D,
                0.34D,
                0.48D,
                16,
                22
        );

        profiles[SpaceVentType.STEAM.ordinal()] = new GeyserVisualProfile(
                0.82D,
                0.18D,
                0.16D,
                0.30D,
                0.40D,
                10,
                16,
                0.28D,
                0.34D,
                0.70D,
                0.14D,
                0.26D,
                10,
                16,
                1.62D,
                1.14D,
                0.40D,
                0.024D,
                0.09D,
                10,
                16,
                0.26D,
                0.36D,
                0.12D,
                0.18D,
                6,
                10,
                0.18D,
                0.32D,
                0.46D,
                15,
                22
        );

        return profiles;
    }

    protected SpacePlanetTraits traits(final Level context) {
        return SpacePlanetTraits.from(context);
    }

    protected SpaceDimensionProfile profile(final SpacePlanetTraits traits) {
        return resolveProfile(traits.dimensionId());
    }

    public SpaceMaterialSet materials(final Level context) {
        final SpacePlanetTraits traits = traits(context);
        return SpaceMaterialSet.resolve(
                null,
                BlockPos.ZERO,
                traits,
                profile(traits)
        );
    }

    public void captureSourceState(final boolean syncToClient) {
        if (this.level == null) {
            return;
        }

        final BlockState resolved = this.resolveSourceState();
        final boolean changed = !this.sourceInitialized || !resolved.equals(this.sourceState);

        this.sourceState = resolved;
        this.sourceInitialized = true;
        this.setChanged();

        if (syncToClient && changed) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public BlockState getSourceState() {
        return this.sourceInitialized ? this.sourceState : resolveFallbackSurfaceState();
    }

    private BlockState resolveSourceState() {
        if (this.level == null || this.profile == null) {
            return this.sourceInitialized ? this.sourceState : Blocks.STONE.defaultBlockState();
        }

        final BlockState below = this.profile.topBlock();
        if (isValidVisualSource(below)) {
            return below;
        }

        if (this.sourceInitialized && isValidVisualSource(this.sourceState)) {
            return this.sourceState;
        }

        return resolveFallbackSurfaceState();
    }

    private BlockState resolveFallbackSurfaceState() {
        if (this.cachedPlanetSurfaceResolved) {
            return this.cachedPlanetSurfaceState;
        }

        this.cachedPlanetSurfaceResolved = true;

        if (this.level == null || profile == null) {
            this.cachedPlanetSurfaceState = Blocks.STONE.defaultBlockState();
            return this.cachedPlanetSurfaceState;
        }

        try {

            this.cachedPlanetSurfaceState = profile.topBlock();
        } catch (final Throwable ignored) {
            this.cachedPlanetSurfaceState = Blocks.STONE.defaultBlockState();
        }

        return this.cachedPlanetSurfaceState;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.level != null && !this.level.isClientSide && !this.sourceInitialized) {
            this.profile = SpaceBodyProfiles.byDimensionPath(this.level.dimension().location().getPath());
            this.captureSourceState(true);
        }

    }

    @Override
    protected void saveAdditional(final CompoundTag tag, HolderLookup.Provider p_323635_) {
        super.saveAdditional(tag, p_323635_);

        tag.putInt(TAG_TIMER, this.timer);
        tag.putBoolean(TAG_ACTIVE, this.active);
        tag.putBoolean(TAG_SOURCE_INITIALIZED, this.sourceInitialized);
        tag.put(TAG_SOURCE_STATE, NbtUtils.writeBlockState(this.getSourceState()));
    }

    @Override
    public void loadAdditional(final CompoundTag tag, HolderLookup.Provider p_323635_) {
        super.loadAdditional(tag, p_323635_);

        this.timer = tag.getInt(TAG_TIMER);
        this.active = tag.getBoolean(TAG_ACTIVE);

        if (tag.contains(TAG_SOURCE_STATE, Tag.TAG_COMPOUND)) {
            this.sourceState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound(TAG_SOURCE_STATE));
            this.sourceInitialized = tag.contains(TAG_SOURCE_INITIALIZED, Tag.TAG_BYTE)
                    ? tag.getBoolean(TAG_SOURCE_INITIALIZED)
                    : true;
        } else {
            this.sourceState = Blocks.STONE.defaultBlockState();
            this.sourceInitialized = false;
        }

        this.cachedPlanetSurfaceResolved = false;
        this.cachedPlanetSurfaceState = Blocks.STONE.defaultBlockState();
        this.clientBurstCooldown = 0;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider p_323910_) {
        return this.saveWithoutMetadata(p_323910_);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.loadAdditional(tag, lookupProvider);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private record ParticleLod(
            double countScale,
            int emissionInterval,
            boolean outerEnabled,
            boolean topEnabled,
            boolean burstEnabled,
            double secondaryChanceScale,
            double burstChanceScale
    ) {
    }

    private record GeyserVisualProfile(
            double baseSpawnY,
            double baseHeightSpread,
            double coreRadius,
            double minUpSpeed,
            double maxUpSpeed,
            int coreCountMin,
            int coreCountMax,
            double outerRadiusMin,
            double outerRadiusMax,
            double topMistBaseHeight,
            double topMistHeightSpread,
            double topMistRadius,
            int topMistCountMin,
            int topMistCountMax,
            double outerMinUpSpeed,
            double outerMaxUpSpeed,
            double horizontalDrift,
            double outerHorizontalNoise,
            double topMistHorizontalSpeed,
            int outerCountMin,
            int outerCountMax,
            double topMistUpSpeedMin,
            double topMistUpSpeedMax,
            double burstChance,
            double burstRadius,
            int burstCountMin,
            int burstCountMax,
            double burstHorizontalSpeed,
            double burstMinUpSpeed,
            double burstMaxUpSpeed,
            int burstCooldownMin,
            int burstCooldownRandom
    ) {
    }
}