package com.denfop.api.pollution.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public final class PollutionVisualController {

    private static final PollutionVisualUpdateCache UPDATE_CACHE = new PollutionVisualUpdateCache();
    private static PollutionVisualData current = PollutionVisualData.NONE;
    private static PollutionVisualData target = PollutionVisualData.NONE;
    private static int clientTicks;

    private PollutionVisualController() {
    }

    public static void tickClient(Minecraft minecraft) {
        clientTicks++;

        if (minecraft == null || minecraft.isPaused() || minecraft.player == null || minecraft.level == null) {
            current = current.lerpTo(PollutionVisualData.NONE, 0.25F);
            target = PollutionVisualData.NONE;
            UPDATE_CACHE.reset();
            PollutionClientRenderRefresh.reset();
            return;
        }

        LocalPlayer player = minecraft.player;
        long gameTime = minecraft.level.getGameTime();

        if (UPDATE_CACHE.shouldRecompute(minecraft, player, gameTime)) {
            target = PollutionVisualCalculator.calculate(player);
            UPDATE_CACHE.updateSnapshot(minecraft, player, gameTime, target);
        }

        float alpha = computeLerpAlpha(current, target);
        current = current.lerpTo(target, alpha);

        if (!target.isActive() && current.getOverallInfluence() < 0.01F) {
            current = PollutionVisualData.NONE;
        }

        PollutionClientRenderRefresh.flushIfNeeded(minecraft);
        spawnAmbientParticles(minecraft, current);
    }

    public static PollutionVisualData getCurrent() {
        return current;
    }

    public static float getPulse(float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        long time = minecraft.level != null ? minecraft.level.getGameTime() : clientTicks;
        float speed = 0.11F + current.getRadiationInfluence() * 0.08F + current.getAirInfluence() * 0.03F;
        return 0.5F + 0.5F * Mth.sin((time + partialTick) * speed);
    }

    private static float computeLerpAlpha(PollutionVisualData currentState, PollutionVisualData targetState) {
        float maxInfluence = Math.max(currentState.getOverallInfluence(), targetState.getOverallInfluence());

        if (maxInfluence > 0.65F) {
            return 0.18F;
        }
        if (maxInfluence > 0.30F) {
            return 0.15F;
        }
        if (currentState.isActive() || targetState.isActive()) {
            return 0.12F;
        }
        return 0.25F;
    }

    private static void spawnAmbientParticles(Minecraft minecraft, PollutionVisualData state) {
        if (!state.isActive() || minecraft.player == null || minecraft.level == null) {
            return;
        }

        if ((minecraft.player.tickCount & 1) != 0) {
            return;
        }

        float chance = 0.010F + state.getOverallInfluence() * 0.060F;
        if (minecraft.level.random.nextFloat() > chance) {
            return;
        }

        Vec3 eye = minecraft.player.getEyePosition();
        double dx = (minecraft.level.random.nextDouble() - 0.5D) * 6.0D;
        double dy = (minecraft.level.random.nextDouble() - 0.25D) * 2.5D;
        double dz = (minecraft.level.random.nextDouble() - 0.5D) * 6.0D;

        int rgb = switch (state.getDominantType()) {
            case RADIATION -> 0x76B642;
            case SOIL -> 0x746C4C;
            case AIR, MIXED -> 0x847754;
            default -> 0x847754;
        };

        Vector3f vector = new Vector3f(
                ((rgb >> 16) & 255) / 255.0F,
                ((rgb >> 8) & 255) / 255.0F,
                (rgb & 255) / 255.0F
        );

        float size = 0.35F + state.getOverallInfluence() * 0.55F;

        minecraft.level.addParticle(
                new DustParticleOptions(vector, size),
                eye.x + dx,
                eye.y + dy,
                eye.z + dz,
                dx * 0.005D,
                0.002D + minecraft.level.random.nextDouble() * 0.006D,
                dz * 0.005D
        );

        if (state.getAirInfluence() > 0.35F && minecraft.level.random.nextFloat() < 0.18F) {
            minecraft.level.addParticle(
                    ParticleTypes.ASH,
                    eye.x + dx * 0.5D,
                    eye.y + 0.2D + minecraft.level.random.nextDouble(),
                    eye.z + dz * 0.5D,
                    0.0D,
                    0.004D,
                    0.0D
            );
        }
    }
}