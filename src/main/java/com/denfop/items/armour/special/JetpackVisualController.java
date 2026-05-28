package com.denfop.items.armour.special;

import com.denfop.sound.EnumSound;
import com.denfop.sound.SoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JetpackVisualController {

    private static final Map<UUID, JetpackAnimationData> STATES = new HashMap<>();

    private static final Vec3 WORLD_UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 FALLBACK_RIGHT = new Vec3(1.0D, 0.0D, 0.0D);

    private JetpackVisualController() {
    }

    public static void tick(
            final Player player,
            final boolean thrustActive,
            final boolean jetpackEnabled,
            final boolean verticalMode,
            final boolean jumpPressed,
            final boolean descendPressed,
            final boolean forwardPressed,
            final float energyRatio
    ) {
        if (!(player.level() instanceof ClientLevel level)) {
            return;
        }

        final Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.isPaused()) {
            return;
        }

        final float partialTicks = minecraft.getFrameTimeNs();
        final JetpackAnimationData data = STATES.computeIfAbsent(player.getUUID(), id -> new JetpackAnimationData());

        final JetpackAnimationContext context = new JetpackAnimationContext(
                player,
                thrustActive,
                jetpackEnabled,
                verticalMode,
                jumpPressed,
                descendPressed,
                forwardPressed,
                energyRatio,
                partialTicks
        );

        updateState(data, context);
        updateSound(player, data);
        spawnParticles(level, context, data);

        data.lastSeenTick = player.tickCount;

        if (!context.jetpackEnabled && !context.thrustActive && data.inactiveTicks > 10 && !data.loopSoundPlaying) {
            STATES.remove(player.getUUID());
        }
    }

    private static void updateState(final JetpackAnimationData data, final JetpackAnimationContext context) {
        final boolean activeNow = context.thrustActive || (context.jetpackEnabled && context.isAirborne());

        if (activeNow && !data.lastActive) {
            data.startTicks = 4;
        }

        JetpackVisualState desiredState = detectState(context);

        if (data.startTicks > 0 && desiredState != JetpackVisualState.IDLE) {
            desiredState = JetpackVisualState.START;
            data.startTicks--;
        }

        if (desiredState != data.state) {
            data.state = desiredState;
            data.stateTicks = 0;
        } else {
            data.stateTicks++;
        }

        data.lastActive = activeNow;
        data.inactiveTicks = desiredState == JetpackVisualState.IDLE ? data.inactiveTicks + 1 : 0;

        final float energyFactor = 0.55F + context.energyRatio * 0.45F;
        final float flicker = 0.92F + 0.08F * Mth.sin((context.player.tickCount + context.partialTicks) * (8.0F + data.thrust * 3.0F));
        final float descentPulse = 0.55F + 0.45F * (0.5F + 0.5F * Mth.sin((context.player.tickCount + context.partialTicks) * 1.6F));

        float targetThrust;
        float targetFlameRate;
        float targetSmokeRate;
        float targetSparkRate;
        float targetGlowRate;
        float targetLength;
        float targetWidth;
        float targetGlow;
        float targetJitter;

        switch (desiredState) {
            case START -> {
                targetThrust = 1.00F;
                targetFlameRate = 7.20F;
                targetSmokeRate = 3.40F;
                targetSparkRate = 1.80F;
                targetGlowRate = 1.40F;
                targetLength = 1.08F;
                targetWidth = 0.18F;
                targetGlow = 0.68F;
                targetJitter = 0.028F;
            }
            case ASCENT -> {
                targetThrust = 0.86F;
                targetFlameRate = 5.00F;
                targetSmokeRate = 2.20F;
                targetSparkRate = 0.35F;
                targetGlowRate = 0.90F;
                targetLength = 0.88F;
                targetWidth = 0.16F;
                targetGlow = 0.52F;
                targetJitter = 0.017F;
            }
            case HOVER -> {
                targetThrust = 0.54F;
                targetFlameRate = 2.80F;
                targetSmokeRate = 1.20F;
                targetSparkRate = 0.08F;
                targetGlowRate = 0.45F;
                targetLength = 0.56F;
                targetWidth = 0.21F;
                targetGlow = 0.28F;
                targetJitter = 0.009F;
            }
            case DESCENT -> {
                targetThrust = 0.42F * descentPulse;
                targetFlameRate = 1.60F * descentPulse;
                targetSmokeRate = 1.00F * descentPulse;
                targetSparkRate = 0.12F * descentPulse;
                targetGlowRate = 0.30F * descentPulse;
                targetLength = 0.46F + 0.10F * descentPulse;
                targetWidth = 0.23F;
                targetGlow = 0.20F * descentPulse;
                targetJitter = 0.010F;
            }
            case BOOST -> {
                targetThrust = 1.16F;
                targetFlameRate = 8.60F;
                targetSmokeRate = 3.00F;
                targetSparkRate = 1.15F;
                targetGlowRate = 1.20F;
                targetLength = 1.24F;
                targetWidth = 0.13F;
                targetGlow = 0.74F;
                targetJitter = 0.026F;
            }
            case IDLE -> {
                targetThrust = 0.06F;
                targetFlameRate = 0.00F;
                targetSmokeRate = 0.00F;
                targetSparkRate = 0.00F;
                targetGlowRate = 0.16F;
                targetLength = 0.10F;
                targetWidth = 0.08F;
                targetGlow = 0.08F;
                targetJitter = 0.002F;
            }
            default -> throw new IllegalStateException("Unexpected state: " + desiredState);
        }

        targetThrust *= energyFactor * flicker;
        targetFlameRate *= energyFactor;
        targetSmokeRate *= (0.80F + 0.20F * energyFactor);
        targetSparkRate *= (0.60F + 0.40F * energyFactor);
        targetGlowRate *= (0.70F + 0.30F * energyFactor);
        targetLength *= energyFactor;
        targetGlow *= energyFactor;

        data.thrust = approach(data.thrust, targetThrust, 0.35F);
        data.flameRate = approach(data.flameRate, targetFlameRate, 0.35F);
        data.smokeRate = approach(data.smokeRate, targetSmokeRate, 0.28F);
        data.sparkRate = approach(data.sparkRate, targetSparkRate, 0.30F);
        data.glowRate = approach(data.glowRate, targetGlowRate, 0.22F);
        data.length = approach(data.length, targetLength, 0.32F);
        data.width = approach(data.width, targetWidth, 0.26F);
        data.glow = approach(data.glow, targetGlow, 0.24F);
        data.jitter = approach(data.jitter, targetJitter, 0.24F);
    }

    private static JetpackVisualState detectState(final JetpackAnimationContext context) {
        if (!context.jetpackEnabled && !context.thrustActive) {
            return JetpackVisualState.IDLE;
        }

        if (context.isBoosting()) {
            return JetpackVisualState.BOOST;
        }

        if (context.thrustActive) {
            if (context.verticalSpeed > 0.11D || (context.jumpPressed && context.isAirborne())) {
                return JetpackVisualState.ASCENT;
            }

            if (context.verticalSpeed < -0.08D || context.descendPressed) {
                return JetpackVisualState.DESCENT;
            }

            return JetpackVisualState.HOVER;
        }

        if (context.verticalSpeed > 0.08D) {
            return JetpackVisualState.ASCENT;
        }

        if (context.verticalSpeed < -0.08D) {
            return JetpackVisualState.DESCENT;
        }

        return JetpackVisualState.HOVER;
    }

    private static void updateSound(final Player player, final JetpackAnimationData data) {
        final boolean shouldPlayLoop = data.state != JetpackVisualState.IDLE;

        if (shouldPlayLoop && !data.loopSoundPlaying) {
            SoundHandler.playSound(player, "JetpackLoop");
            data.loopSoundPlaying = true;
        } else if (!shouldPlayLoop && data.loopSoundPlaying) {
            SoundHandler.stopSound(EnumSound.JetpackLoop);
            data.loopSoundPlaying = false;
        }
    }

    private static void spawnParticles(
            final ClientLevel level,
            final JetpackAnimationContext context,
            final JetpackAnimationData data
    ) {
        if (!context.jetpackEnabled && !context.thrustActive && context.energyRatio <= 0.0F) {
            return;
        }

        final Vec3[] nozzles = resolveNozzles(context.player, context.partialTicks);
        final Vec3 exhaustDirection = resolveExhaustDirection(context);
        Vec3 side = exhaustDirection.cross(WORLD_UP);

        if (side.lengthSqr() < 1.0E-5D) {
            side = FALLBACK_RIGHT;
        } else {
            side = side.normalize();
        }

        Vec3 up = side.cross(exhaustDirection);
        if (up.lengthSqr() < 1.0E-5D) {
            up = WORLD_UP;
        } else {
            up = up.normalize();
        }

        final Vec3 inheritedMotion = context.player.getDeltaMovement().scale(0.12D);

        data.flameAccumulator = Math.min(data.flameAccumulator + data.flameRate, 12.0F);
        data.smokeAccumulator = Math.min(data.smokeAccumulator + data.smokeRate, 8.0F);
        data.sparkAccumulator = Math.min(data.sparkAccumulator + data.sparkRate, 6.0F);
        data.glowAccumulator = Math.min(data.glowAccumulator + data.glowRate, 4.0F);

        int nozzleIndex = 0;
        while (data.flameAccumulator >= 1.0F) {
            spawnFlame(
                    level,
                    nozzles[nozzleIndex++ & 1],
                    exhaustDirection,
                    side,
                    up,
                    inheritedMotion,
                    data
            );
            data.flameAccumulator -= 1.0F;
        }

        nozzleIndex = 0;
        while (data.smokeAccumulator >= 1.0F) {
            spawnSmoke(
                    level,
                    nozzles[nozzleIndex++ & 1],
                    exhaustDirection,
                    side,
                    up,
                    inheritedMotion,
                    data
            );
            data.smokeAccumulator -= 1.0F;
        }

        nozzleIndex = 0;
        while (data.sparkAccumulator >= 1.0F) {
            spawnSpark(
                    level,
                    nozzles[nozzleIndex++ & 1],
                    exhaustDirection,
                    side,
                    up,
                    inheritedMotion,
                    data
            );
            data.sparkAccumulator -= 1.0F;
        }

        while (data.glowAccumulator >= 1.0F) {
            spawnGlow(level, context, data);
            data.glowAccumulator -= 1.0F;
        }
    }

    private static void spawnFlame(
            final ClientLevel level,
            final Vec3 nozzle,
            final Vec3 direction,
            final Vec3 side,
            final Vec3 up,
            final Vec3 inheritedMotion,
            final JetpackAnimationData data
    ) {
        final RandomSource random = level.random;

        final Vec3 spread = side.scale(randomSigned(random, data.width * 0.55F))
                .add(up.scale(randomSigned(random, data.width * 0.35F)));

        final Vec3 spawnPos = nozzle.add(spread);

        final double speed = 0.14D + data.length * 0.16D + random.nextDouble() * 0.05D;
        final Vec3 velocity = direction.scale(speed)
                .add(inheritedMotion)
                .add(side.scale(randomSigned(random, data.jitter)))
                .add(up.scale(randomSigned(random, data.jitter * 0.65F)));

        level.addParticle(
                ParticleTypes.FLAME,
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                velocity.x,
                velocity.y,
                velocity.z
        );
    }

    private static void spawnSmoke(
            final ClientLevel level,
            final Vec3 nozzle,
            final Vec3 direction,
            final Vec3 side,
            final Vec3 up,
            final Vec3 inheritedMotion,
            final JetpackAnimationData data
    ) {
        final RandomSource random = level.random;

        final Vec3 spread = side.scale(randomSigned(random, data.width * 0.70F))
                .add(up.scale(randomSigned(random, data.width * 0.45F)));

        final Vec3 spawnPos = nozzle.add(spread);

        final double speed = 0.07D + data.length * 0.09D + random.nextDouble() * 0.03D;
        final Vec3 velocity = direction.scale(speed)
                .add(inheritedMotion.scale(0.75D))
                .add(side.scale(randomSigned(random, 0.025D)))
                .add(up.scale(randomSigned(random, 0.018D)));

        final ParticleOptions smokeType = (data.state == JetpackVisualState.START || data.state == JetpackVisualState.BOOST)
                ? ParticleTypes.LARGE_SMOKE
                : ParticleTypes.SMOKE;

        level.addParticle(
                smokeType,
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                velocity.x,
                velocity.y,
                velocity.z
        );
    }

    private static void spawnSpark(
            final ClientLevel level,
            final Vec3 nozzle,
            final Vec3 direction,
            final Vec3 side,
            final Vec3 up,
            final Vec3 inheritedMotion,
            final JetpackAnimationData data
    ) {
        final RandomSource random = level.random;

        final Vec3 spawnPos = nozzle
                .add(side.scale(randomSigned(random, data.width * 0.35F)))
                .add(up.scale(randomSigned(random, data.width * 0.22F)));

        final Vec3 velocity = direction.scale(0.10D + data.length * 0.09D)
                .add(inheritedMotion.scale(0.60D))
                .add(side.scale(randomSigned(random, 0.06D)))
                .add(up.scale(randomSigned(random, 0.05D)));

        level.addParticle(
                ParticleTypes.ELECTRIC_SPARK,
                spawnPos.x,
                spawnPos.y,
                spawnPos.z,
                velocity.x,
                velocity.y,
                velocity.z
        );
    }

    private static void spawnGlow(
            final ClientLevel level,
            final JetpackAnimationContext context,
            final JetpackAnimationData data
    ) {
        final RandomSource random = level.random;
        final Vec3 playerPos = interpolatePosition(context.player, context.partialTicks);

        final double radius = 0.12D + data.glow * 0.20D;
        final double x = playerPos.x + randomSigned(random, radius);
        final double y = playerPos.y + 0.06D;
        final double z = playerPos.z + randomSigned(random, radius);

        final float lowEnergyPenalty = 1.0F - context.energyRatio;
        final float red = 1.0F;
        final float green = Mth.clamp(0.50F + data.thrust * 0.22F - lowEnergyPenalty * 0.12F, 0.15F, 1.0F);
        final float blue = Mth.clamp(0.05F + data.thrust * 0.08F - lowEnergyPenalty * 0.05F, 0.0F, 0.25F);

        final DustParticleOptions dust = new DustParticleOptions(
                new Vector3f(red, green, blue),
                0.60F + data.glow * 0.85F
        );

        level.addParticle(
                dust,
                x,
                y,
                z,
                0.0D,
                0.015D + data.glow * 0.015D,
                0.0D
        );
    }

    private static Vec3[] resolveNozzles(final Player player, final float partialTicks) {
        final Vec3 interpolatedPos = interpolatePosition(player, partialTicks);

        final float bodyYaw = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
        final float yawRad = bodyYaw * ((float) Math.PI / 180.0F);

        final Vec3 forward = new Vec3(-Mth.sin(yawRad), 0.0D, Mth.cos(yawRad));
        final Vec3 right = new Vec3(forward.z, 0.0D, -forward.x);

        final double backOffset = player.isCrouching() ? 0.18D : 0.24D;
        final double height = player.isCrouching() ? player.getBbHeight() * 0.58D : player.getBbHeight() * 0.72D;
        final double sideOffset = player.getBbWidth() * 0.38D;

        final Vec3 center = interpolatedPos
                .add(0.0D, height, 0.0D)
                .add(forward.scale(-backOffset));

        return new Vec3[]{
                center.add(right.scale(-sideOffset)),
                center.add(right.scale(sideOffset))
        };
    }

    private static Vec3 resolveExhaustDirection(final JetpackAnimationContext context) {
        final double motionX = context.player.getDeltaMovement().x;
        final double motionZ = context.player.getDeltaMovement().z;

        Vec3 horizontalMotion = new Vec3(motionX, 0.0D, motionZ);
        if (horizontalMotion.lengthSqr() > 1.0E-5D) {
            horizontalMotion = horizontalMotion.normalize();
        } else {
            horizontalMotion = Vec3.ZERO;
        }

        final float bodyYaw = Mth.rotLerp(context.partialTicks, context.player.yBodyRotO, context.player.yBodyRot);
        final float yawRad = bodyYaw * ((float) Math.PI / 180.0F);
        final Vec3 forward = new Vec3(-Mth.sin(yawRad), 0.0D, Mth.cos(yawRad));

        Vec3 direction = new Vec3(
                -horizontalMotion.x * (0.30D + Math.min(context.horizontalSpeed, 0.60D) * 0.50D),
                -1.0D,
                -horizontalMotion.z * (0.30D + Math.min(context.horizontalSpeed, 0.60D) * 0.50D)
        );

        if (context.forwardPressed) {
            direction = direction.add(forward.scale(-0.18D));
        }

        if (context.descendPressed) {
            direction = direction.add(0.0D, 0.15D, 0.0D);
        }

        if (direction.lengthSqr() < 1.0E-5D) {
            direction = new Vec3(0.0D, -1.0D, 0.0D);
        }

        return direction.normalize();
    }

    private static Vec3 interpolatePosition(final Player player, final float partialTicks) {
        return new Vec3(
                Mth.lerp(partialTicks, player.xo, player.getX()),
                Mth.lerp(partialTicks, player.yo, player.getY()),
                Mth.lerp(partialTicks, player.zo, player.getZ())
        );
    }

    private static float approach(final float current, final float target, final float factor) {
        return current + (target - current) * factor;
    }

    private static double randomSigned(final RandomSource random, final double maxAbs) {
        return (random.nextDouble() - 0.5D) * 2.0D * maxAbs;
    }
}