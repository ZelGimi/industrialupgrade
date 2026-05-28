package com.denfop.render.rocketpad;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class RocketLaunchAnimation {

    private static final int PRE_LAUNCH_TICKS = 30;   // 1.5 сек
    private static final int LIFTOFF_TICKS = 20;      // повільний старт
    private static final int TOTAL_FLIGHT_TICKS = 180;
    private final ItemStack rocketStack;
    private final double baseX;
    private final double baseY;
    private final double baseZ;
    private final long seed;
    private final long launchStartTick;
    private boolean finished;

    public RocketLaunchAnimation(ItemStack rocketStack, double baseX, double baseY, double baseZ, long launchStartTick, long seed) {
        this.rocketStack = rocketStack;
        this.baseX = baseX;
        this.baseY = baseY;
        this.baseZ = baseZ;
        this.launchStartTick = launchStartTick;
        this.seed = seed;
    }

    public ItemStack getRocketStack() {
        return rocketStack;
    }

    public long getSeed() {
        return seed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void markFinished() {
        this.finished = true;
    }

    public float getAge(float partialTicks, long gameTime) {
        return (gameTime - launchStartTick) + partialTicks;
    }

    public Phase getPhase(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);

        if (age < PRE_LAUNCH_TICKS) {
            return Phase.PRE_LAUNCH;
        }
        if (age < PRE_LAUNCH_TICKS + LIFTOFF_TICKS) {
            return Phase.LIFTOFF;
        }
        if (age < TOTAL_FLIGHT_TICKS) {
            return Phase.ASCENT;
        }
        return Phase.FINISHED;
    }

    public double getRenderY(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);

        if (age <= PRE_LAUNCH_TICKS) {
            return baseY;
        }

        float flightAge = age - PRE_LAUNCH_TICKS;
        float t = Mth.clamp(flightAge / (TOTAL_FLIGHT_TICKS - PRE_LAUNCH_TICKS), 0.0F, 1.0F);

        // ease-in + прискорення вгору
        float eased = RocketAnimationMath.easeInQuad(t);

        // максимальна висота рендеру
        double maxLift = 260.0D - baseY;
        return baseY + eased * maxLift;
    }

    public double getRenderX(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);
        return baseX + RocketAnimationMath.getShakeOffset(seed, age, 0);
    }

    public double getRenderZ(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);
        return baseZ + RocketAnimationMath.getShakeOffset(seed, age, 1);
    }

    public float getPitch(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);
        return RocketAnimationMath.getPitch(age);
    }

    public float getRoll(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);
        return RocketAnimationMath.getRoll(seed, age);
    }

    public float getEngineStrength(float partialTicks, long gameTime) {
        float age = getAge(partialTicks, gameTime);

        if (age < PRE_LAUNCH_TICKS) {
            return 0.35F + 0.65F * (age / PRE_LAUNCH_TICKS);
        }

        float flightAge = age - PRE_LAUNCH_TICKS;
        float t = Mth.clamp(flightAge / (TOTAL_FLIGHT_TICKS - PRE_LAUNCH_TICKS), 0.0F, 1.0F);
        return 0.8F + 0.2F * t;
    }

    public boolean shouldRemove(float partialTicks, long gameTime) {
        return getPhase(partialTicks, gameTime) == Phase.FINISHED || getRenderY(partialTicks, gameTime) > 255.0D;
    }

    public enum Phase {
        PRE_LAUNCH,
        LIFTOFF,
        ASCENT,
        FINISHED
    }
}