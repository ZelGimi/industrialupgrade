package com.denfop.items.armour;

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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public final class JetpackVisualClient {

    private static final Map<UUID, Data> DATA = new HashMap<>();
    private static final Vec3 WORLD_UP = new Vec3(0.0D, 1.0D, 0.0D);
    private static final Vec3 FALLBACK_RIGHT = new Vec3(1.0D, 0.0D, 0.0D);

    private JetpackVisualClient() {
    }

    public static void tick(
            final Player player,
            final boolean thrustActive,
            final boolean jetpackEnabled,
            final boolean hoverMode,
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

        final float partialTicks = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
        final UUID id = player.getUUID();
        final Data data = DATA.computeIfAbsent(id, uuid -> new Data());

        final double motionX = player.getDeltaMovement().x;
        final double motionY = player.getDeltaMovement().y;
        final double motionZ = player.getDeltaMovement().z;
        final double horizontalSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);

        final boolean activeNow = thrustActive || (jetpackEnabled && !player.onGround());

        if (activeNow && !data.lastActive) {
            data.startTicks = 5;
        }

        State desiredState = detectState(
                player,
                thrustActive,
                jetpackEnabled,
                hoverMode,
                jumpPressed,
                descendPressed,
                forwardPressed,
                motionY,
                horizontalSpeed
        );

        if (data.startTicks > 0 && desiredState != State.IDLE) {
            desiredState = State.START;
            data.startTicks--;
        }

        if (desiredState != data.state) {
            data.state = desiredState;
            data.stateTicks = 0;
        } else {
            data.stateTicks++;
        }

        data.lastActive = activeNow;

        float targetThrust;
        float targetLength;
        float targetWidth;
        float targetGlow;
        float targetJitter;
        float targetFlameRate;
        float targetSmokeRate;
        float targetSparkRate;
        float targetGlowRate;

        final float energyFactor = 0.55F + energyRatio * 0.45F;
        final float flicker = 0.94F + 0.06F * Mth.sin((player.tickCount + partialTicks) * (7.5F + data.thrust * 3.0F));
        final float descentPulse = 0.55F + 0.45F * (0.5F + 0.5F * Mth.sin((player.tickCount + partialTicks) * 1.7F));

        switch (data.state) {
            case START -> {
                targetThrust = 1.00F;
                targetLength = 1.08F;
                targetWidth = 0.18F;
                targetGlow = 0.62F;
                targetJitter = 0.028F;
                targetFlameRate = 7.20F;
                targetSmokeRate = 3.40F;
                targetSparkRate = 1.70F;
                targetGlowRate = 1.30F;
            }
            case ASCENT -> {
                targetThrust = 0.84F;
                targetLength = 0.88F;
                targetWidth = 0.16F;
                targetGlow = 0.48F;
                targetJitter = 0.017F;
                targetFlameRate = 5.00F;
                targetSmokeRate = 2.20F;
                targetSparkRate = 0.30F;
                targetGlowRate = 0.90F;
            }
            case HOVER -> {
                targetThrust = 0.52F;
                targetLength = 0.56F;
                targetWidth = 0.21F;
                targetGlow = 0.26F;
                targetJitter = 0.009F;
                targetFlameRate = 2.80F;
                targetSmokeRate = 1.20F;
                targetSparkRate = 0.08F;
                targetGlowRate = 0.45F;
            }
            case DESCENT -> {
                targetThrust = 0.42F * descentPulse;
                targetLength = 0.44F + 0.12F * descentPulse;
                targetWidth = 0.23F;
                targetGlow = 0.18F * descentPulse;
                targetJitter = 0.010F;
                targetFlameRate = 1.55F * descentPulse;
                targetSmokeRate = 1.00F * descentPulse;
                targetSparkRate = 0.10F * descentPulse;
                targetGlowRate = 0.28F * descentPulse;
            }
            case BOOST -> {
                targetThrust = 1.16F;
                targetLength = 1.24F;
                targetWidth = 0.13F;
                targetGlow = 0.74F;
                targetJitter = 0.026F;
                targetFlameRate = 8.60F;
                targetSmokeRate = 3.00F;
                targetSparkRate = 1.20F;
                targetGlowRate = 1.20F;
            }
            case IDLE -> {
                targetThrust = 0.05F;
                targetLength = 0.10F;
                targetWidth = 0.08F;
                targetGlow = 0.06F;
                targetJitter = 0.002F;
                targetFlameRate = 0.0F;
                targetSmokeRate = 0.0F;
                targetSparkRate = 0.0F;
                targetGlowRate = 0.14F;
            }
            default -> throw new IllegalStateException("Unexpected state " + data.state);
        }

        targetThrust *= energyFactor * flicker;
        targetLength *= energyFactor;
        targetGlow *= energyFactor;
        targetFlameRate *= energyFactor;
        targetSmokeRate *= 0.80F + 0.20F * energyFactor;
        targetSparkRate *= 0.60F + 0.40F * energyFactor;
        targetGlowRate *= 0.70F + 0.30F * energyFactor;

        data.thrust = lerp(data.thrust, targetThrust, 0.35F);
        data.length = lerp(data.length, targetLength, 0.32F);
        data.width = lerp(data.width, targetWidth, 0.26F);
        data.glow = lerp(data.glow, targetGlow, 0.24F);
        data.jitter = lerp(data.jitter, targetJitter, 0.24F);

        data.flameRate = lerp(data.flameRate, targetFlameRate, 0.35F);
        data.smokeRate = lerp(data.smokeRate, targetSmokeRate, 0.28F);
        data.sparkRate = lerp(data.sparkRate, targetSparkRate, 0.30F);
        data.glowRate = lerp(data.glowRate, targetGlowRate, 0.22F);

        updateSound(player, data);
        spawnParticles(level, player, partialTicks, forwardPressed, descendPressed, energyRatio, data);

        if (!jetpackEnabled && !thrustActive && data.state == State.IDLE && !data.loopSoundPlaying && player.tickCount - data.lastSeenTick > 20) {
            DATA.remove(id);
            return;
        }

        data.lastSeenTick = player.tickCount;
    }

    private static State detectState(
            final Player player,
            final boolean thrustActive,
            final boolean jetpackEnabled,
            final boolean hoverMode,
            final boolean jumpPressed,
            final boolean descendPressed,
            final boolean forwardPressed,
            final double verticalSpeed,
            final double horizontalSpeed
    ) {
        if (!jetpackEnabled && !thrustActive) {
            return State.IDLE;
        }

        if (thrustActive && forwardPressed && horizontalSpeed > 0.14D) {
            return State.BOOST;
        }

        if (thrustActive) {
            if (verticalSpeed > 0.10D || (jumpPressed && !player.onGround())) {
                return State.ASCENT;
            }
            if (verticalSpeed < -0.08D || descendPressed) {
                return State.DESCENT;
            }
            if (hoverMode || Math.abs(verticalSpeed) < 0.08D) {
                return State.HOVER;
            }
            return State.ASCENT;
        }

        if (verticalSpeed > 0.08D) {
            return State.ASCENT;
        }
        if (verticalSpeed < -0.08D) {
            return State.DESCENT;
        }
        return State.HOVER;
    }

    private static void updateSound(final Player player, final Data data) {
        final boolean shouldPlayLoop = data.state != State.IDLE;

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
            final Player player,
            final float partialTicks,
            final boolean forwardPressed,
            final boolean descendPressed,
            final float energyRatio,
            final Data data
    ) {
        final Vec3[] nozzles = resolveNozzles(player, partialTicks);
        final Vec3 exhaustDirection = resolveExhaustDirection(player, partialTicks, forwardPressed, descendPressed);

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

        final Vec3 inheritedMotion = player.getDeltaMovement().scale(0.12D);

        data.flameAccumulator = Math.min(data.flameAccumulator + data.flameRate, 12.0F);
        data.smokeAccumulator = Math.min(data.smokeAccumulator + data.smokeRate, 8.0F);
        data.sparkAccumulator = Math.min(data.sparkAccumulator + data.sparkRate, 6.0F);
        data.glowAccumulator = Math.min(data.glowAccumulator + data.glowRate, 4.0F);

        int nozzleIndex = 0;
        while (data.flameAccumulator >= 1.0F) {
            spawnFlame(level, nozzles[nozzleIndex++ & 1], exhaustDirection, side, up, inheritedMotion, data);
            data.flameAccumulator -= 1.0F;
        }

        nozzleIndex = 0;
        while (data.smokeAccumulator >= 1.0F) {
            spawnSmoke(level, nozzles[nozzleIndex++ & 1], exhaustDirection, side, up, inheritedMotion, data);
            data.smokeAccumulator -= 1.0F;
        }

        nozzleIndex = 0;
        while (data.sparkAccumulator >= 1.0F) {
            spawnSpark(level, nozzles[nozzleIndex++ & 1], exhaustDirection, side, up, inheritedMotion, data);
            data.sparkAccumulator -= 1.0F;
        }

        while (data.glowAccumulator >= 1.0F) {
            spawnGlow(level, player, partialTicks, energyRatio, data);
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
            final Data data
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
            final Data data
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

        final ParticleOptions smokeType = (data.state == State.START || data.state == State.BOOST)
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
            final Data data
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
            final Player player,
            final float partialTicks,
            final float energyRatio,
            final Data data
    ) {
        final RandomSource random = level.random;
        final Vec3 pos = interpolatePosition(player, partialTicks);

        final double radius = 0.12D + data.glow * 0.20D;
        final double x = pos.x + randomSigned(random, radius);
        final double y = pos.y + 0.06D;
        final double z = pos.z + randomSigned(random, radius);

        final float lowEnergyPenalty = 1.0F - energyRatio;
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

    private static Vec3 resolveExhaustDirection(
            final Player player,
            final float partialTicks,
            final boolean forwardPressed,
            final boolean descendPressed
    ) {
        final double motionX = player.getDeltaMovement().x;
        final double motionZ = player.getDeltaMovement().z;
        final double horizontalSpeed = Math.sqrt(motionX * motionX + motionZ * motionZ);

        Vec3 horizontalMotion = new Vec3(motionX, 0.0D, motionZ);
        if (horizontalMotion.lengthSqr() > 1.0E-5D) {
            horizontalMotion = horizontalMotion.normalize();
        } else {
            horizontalMotion = Vec3.ZERO;
        }

        final float bodyYaw = Mth.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
        final float yawRad = bodyYaw * ((float) Math.PI / 180.0F);
        final Vec3 forward = new Vec3(-Mth.sin(yawRad), 0.0D, Mth.cos(yawRad));

        Vec3 direction = new Vec3(
                -horizontalMotion.x * (0.30D + Math.min(horizontalSpeed, 0.60D) * 0.50D),
                -1.0D,
                -horizontalMotion.z * (0.30D + Math.min(horizontalSpeed, 0.60D) * 0.50D)
        );

        if (forwardPressed) {
            direction = direction.add(forward.scale(-0.18D));
        }

        if (descendPressed) {
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

    private static float lerp(final float current, final float target, final float factor) {
        return current + (target - current) * factor;
    }

    private static double randomSigned(final RandomSource random, final double maxAbs) {
        return (random.nextDouble() - 0.5D) * 2.0D * maxAbs;
    }

    private enum State {
        IDLE,
        START,
        ASCENT,
        HOVER,
        DESCENT,
        BOOST
    }

    private static final class Data {
        private State state = State.IDLE;
        private boolean lastActive;
        private boolean loopSoundPlaying;

        private int startTicks;
        private int stateTicks;
        private int lastSeenTick;

        private float thrust;
        private float length;
        private float width;
        private float glow;
        private float jitter;

        private float flameRate;
        private float smokeRate;
        private float sparkRate;
        private float glowRate;

        private float flameAccumulator;
        private float smokeAccumulator;
        private float sparkAccumulator;
        private float glowAccumulator;
    }
}