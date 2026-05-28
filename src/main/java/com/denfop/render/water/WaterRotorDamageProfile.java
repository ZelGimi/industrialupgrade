package com.denfop.render.water;


import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.hydroturbine.BlockEntityHydroTurbineController;
import com.denfop.blockentity.mechanism.water.BlockEntityBaseWaterGenerator;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class WaterRotorDamageProfile {

    private static final int BLADE_COUNT = 4;
    private static final int CACHE_LIMIT = 2048;


    private static final int DAMAGE_STATES = 15;

    private static final long GOLDEN = 0x9E3779B97F4A7C15L;
    private static final Map<Long, WaterRotorDamageProfile> CACHE = new HashMap<>();
    private final int currentDamage;
    private final int maxDamage;
    private final int diameter;
    private final int damageLevel;
    private final int damageState;
    private final float damageFraction;
    private final float wearTint;
    private final float crackTint;
    private final float vibration;
    private final BladeRenderState[] blades;
    private WaterRotorDamageProfile(
            int currentDamage,
            int maxDamage,
            int diameter,
            int damageLevel,
            int damageState,
            float damageFraction,
            float wearTint,
            float crackTint,
            float vibration,
            BladeRenderState[] blades
    ) {
        this.currentDamage = currentDamage;
        this.maxDamage = maxDamage;
        this.diameter = diameter;
        this.damageLevel = damageLevel;
        this.damageState = damageState;
        this.damageFraction = damageFraction;
        this.wearTint = wearTint;
        this.crackTint = crackTint;
        this.vibration = vibration;
        this.blades = blades;
    }

    public static WaterRotorDamageProfile resolve(BlockEntityBaseWaterGenerator tile) {
        return resolveInternal(
                tile.getBlockPos().asLong(),
                tile.getRotorDiameter(),
                tile.getRotor(),
                tile.slot.get(0)
        );
    }

    public static WaterRotorDamageProfile resolve(BlockEntityHydroTurbineController tile) {
        return resolveInternal(
                tile.getBlockPos().asLong(),
                tile.getRotorDiameter(),
                tile.getRotor(),
                tile.slot.get(0)
        );
    }

    private static WaterRotorDamageProfile resolveInternal(long posSeed, int diameter, WindRotor rotor, ItemStack stack) {
        int currentDamage = 0;
        int maxDamage = 0;

        if (rotor != null && !stack.isEmpty()) {
            maxDamage = Math.max(0, rotor.getMaxCustomDamage(stack));
            currentDamage = Mth.clamp(rotor.getCustomDamage(stack), 0, maxDamage);
        }

        long cacheKey = posSeed ^ (((long) diameter) << 48);
        WaterRotorDamageProfile cached = CACHE.get(cacheKey);
        if (cached != null && cached.matches(currentDamage, maxDamage, diameter)) {
            return cached;
        }

        WaterRotorDamageProfile built = build(cacheKey, currentDamage, maxDamage, diameter);

        if (CACHE.size() > CACHE_LIMIT) {
            CACHE.clear();
        }

        CACHE.put(cacheKey, built);
        return built;
    }

    private static WaterRotorDamageProfile build(long seed, int currentDamage, int maxDamage, int diameter) {
        float fraction = maxDamage <= 0
                ? 0.0F
                : Mth.clamp(currentDamage / (float) maxDamage, 0.0F, 1.0F);

        int damageState = toDamageState(fraction);
        int damageLevel = toDamageLevel(fraction);

        float stateSpan = DAMAGE_STATES <= 1 ? 1.0F : 1.0F / DAMAGE_STATES;
        float stateStart = damageState * stateSpan;
        float localProgress = damageState == DAMAGE_STATES - 1
                ? 1.0F
                : Mth.clamp((fraction - stateStart) / stateSpan, 0.0F, 1.0F);

        float wearTint = Mth.clamp(1.0F - fraction * 0.38F, 0.45F, 1.0F);
        float crackTint = Mth.clamp(0.92F - fraction * 0.68F, 0.10F, 0.92F);

        float vibration = switch (damageState) {
            case 0, 1, 2 -> 0.0F;
            case 3, 4, 5 -> 0.10F + fraction * 0.35F;
            case 6, 7, 8 -> 0.35F + fraction * 0.70F;
            case 9, 10, 11 -> 0.85F + fraction * 0.95F;
            default -> 1.60F + fraction * 1.45F;
        };

        BladeRenderState[] blades = new BladeRenderState[BLADE_COUNT];
        for (int i = 0; i < BLADE_COUNT; i++) {
            BladeRenderState state = new BladeRenderState();
            state.phase = range(seed, 400 + i, 0.0F, (float) (Math.PI * 2.0));
            blades[i] = state;
        }

        int[] order = bladeOrder(seed);
        int a = order[0];
        int b = order[1];
        int c = order[2];
        int d = order[3];

        switch (damageState) {
            case 0 -> {
                // Идеальный ротор.
            }

            case 1 -> {
                applyMicroWear(blades[a], seed, 10, mix(0.18F, 0.45F, localProgress));
            }

            case 2 -> {
                applyMicroWear(blades[a], seed, 20, mix(0.45F, 0.80F, localProgress));
                applyMicroWear(blades[b], seed, 21, mix(0.12F, 0.28F, localProgress));
            }

            case 3 -> {
                applyEroded(blades[a], seed, 30, mix(0.18F, 0.42F, localProgress));
                applyMicroWear(blades[b], seed, 31, mix(0.35F, 0.65F, localProgress));
            }

            case 4 -> {
                applyEroded(blades[a], seed, 40, mix(0.45F, 0.78F, localProgress));
                applyMicroWear(blades[b], seed, 41, mix(0.42F, 0.80F, localProgress));
                applyMicroWear(blades[c], seed, 42, mix(0.10F, 0.22F, localProgress));
            }

            case 5 -> {
                applyChipped(blades[a], seed, 50, mix(0.20F, 0.42F, localProgress));
                applyEroded(blades[b], seed, 51, mix(0.40F, 0.72F, localProgress));
            }

            case 6 -> {
                applyChipped(blades[a], seed, 60, mix(0.45F, 0.72F, localProgress));
                applyEroded(blades[b], seed, 61, mix(0.52F, 0.85F, localProgress));
                applyMicroWear(blades[c], seed, 62, mix(0.22F, 0.48F, localProgress));
            }

            case 7 -> {
                applyChipped(blades[a], seed, 70, mix(0.70F, 0.95F, localProgress));
                applyChipped(blades[b], seed, 71, mix(0.25F, 0.52F, localProgress));
                applyEroded(blades[c], seed, 72, mix(0.55F, 0.88F, localProgress));
            }

            case 8 -> {
                applyBroken(blades[a], seed, 80, mix(0.20F, 0.48F, localProgress));
                applyChipped(blades[b], seed, 81, mix(0.55F, 0.85F, localProgress));
                applyEroded(blades[c], seed, 82, mix(0.68F, 0.95F, localProgress));
            }

            case 9 -> {
                applyBroken(blades[a], seed, 90, mix(0.55F, 0.88F, localProgress));
                applyChipped(blades[b], seed, 91, mix(0.65F, 0.95F, localProgress));
                applyChipped(blades[c], seed, 92, mix(0.20F, 0.45F, localProgress));
            }

            case 10 -> {
                applyStump(blades[a], seed, 100, mix(0.18F, 0.45F, localProgress));
                applyBroken(blades[b], seed, 101, mix(0.50F, 0.82F, localProgress));
                applyChipped(blades[c], seed, 102, mix(0.72F, 1.00F, localProgress));
            }

            case 11 -> {
                applyMissing(blades[a]);
                applyStump(blades[b], seed, 110, mix(0.35F, 0.68F, localProgress));
                applyBroken(blades[c], seed, 111, mix(0.68F, 0.98F, localProgress));
                applyChipped(blades[d], seed, 112, mix(0.50F, 0.75F, localProgress));
            }

            case 12 -> {
                applyMissing(blades[a]);
                applyStump(blades[b], seed, 120, mix(0.70F, 1.00F, localProgress));
                applyBroken(blades[c], seed, 121, mix(0.72F, 1.00F, localProgress));
                applyBroken(blades[d], seed, 122, mix(0.28F, 0.52F, localProgress));
            }

            case 13 -> {
                applyMissing(blades[a]);
                applyMissing(blades[b]);
                applyStump(blades[c], seed, 130, mix(0.30F, 0.72F, localProgress));
                applyBroken(blades[d], seed, 131, mix(0.72F, 1.00F, localProgress));
            }

            case 14 -> {
                boolean fullCollapse = fraction >= 0.999F;

                applyMissing(blades[a]);
                applyMissing(blades[b]);

                if (fullCollapse) {
                    applyMissing(blades[c]);
                    applyStump(blades[d], seed, 143, 1.0F);
                } else {
                    applyStump(blades[c], seed, 141, mix(0.72F, 1.00F, localProgress));
                    applyBroken(blades[d], seed, 142, mix(0.78F, 1.00F, localProgress));
                }
            }

            default -> {
            }
        }

        return new WaterRotorDamageProfile(
                currentDamage,
                maxDamage,
                diameter,
                damageLevel,
                damageState,
                fraction,
                wearTint,
                crackTint,
                vibration,
                blades
        );
    }

    private static int toDamageState(float fraction) {
        if (fraction <= 0.0F) {
            return 0;
        }
        int state = (int) Math.floor(fraction * DAMAGE_STATES);
        return Mth.clamp(state, 0, DAMAGE_STATES - 1);
    }

    private static int toDamageLevel(float fraction) {
        if (fraction <= 0.0F) {
            return 0;
        }
        int level = (int) Math.floor(fraction * 5.0F);
        return Mth.clamp(level, 0, 4);
    }

    private static void applyMicroWear(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.FULL;
        blade.crackA = severity > 0.35F;
        blade.crackB = severity > 0.88F;
        blade.tiltX = signedRange(seed, salt, 0.08F, 0.45F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.10F, 0.65F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 0.25F, 1.30F) * severity;
        blade.offsetY = signedRange(seed, salt + 3, 0.00F, 0.020F) * severity;
        blade.offsetZ = signedRange(seed, salt + 4, 0.00F, 0.025F) * severity;
        blade.shade = 1.0F - 0.04F * severity;
    }

    private static void applyEroded(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.ERODED;
        blade.crackA = true;
        blade.crackB = severity > 0.65F;
        blade.tiltX = signedRange(seed, salt, 0.15F, 0.80F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.20F, 1.10F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 0.65F, 3.50F) * severity;
        blade.offsetY = signedRange(seed, salt + 3, 0.00F, 0.040F) * severity;
        blade.offsetZ = signedRange(seed, salt + 4, 0.01F, 0.050F) * severity;
        blade.shade = 0.96F - 0.12F * severity;
    }

    private static void applyChipped(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.CHIPPED;
        blade.crackA = true;
        blade.crackB = severity > 0.42F;
        blade.tiltX = signedRange(seed, salt, 0.30F, 1.35F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.40F, 1.80F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 1.20F, 6.50F) * severity;
        blade.offsetY = signedRange(seed, salt + 3, 0.01F, 0.060F) * severity;
        blade.offsetZ = signedRange(seed, salt + 4, 0.02F, 0.080F) * severity;
        blade.shade = 0.90F - 0.18F * severity;
    }

    private static void applyBroken(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.BROKEN;
        blade.crackA = true;
        blade.crackB = severity > 0.25F;
        blade.tiltX = signedRange(seed, salt, 0.75F, 2.60F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.90F, 3.20F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 3.20F, 12.50F) * severity;
        blade.offsetY = signedRange(seed, salt + 3, 0.01F, 0.080F) * severity;
        blade.offsetZ = signedRange(seed, salt + 4, 0.03F, 0.120F) * severity;
        blade.shade = 0.82F - 0.22F * severity;
    }

    private static void applyStump(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.STUMP;
        blade.crackA = true;
        blade.crackB = true;
        blade.tiltX = signedRange(seed, salt, 1.00F, 4.20F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 1.40F, 5.50F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 5.80F, 18.00F) * severity;
        blade.offsetY = signedRange(seed, salt + 3, 0.02F, 0.100F) * severity;
        blade.offsetZ = signedRange(seed, salt + 4, 0.03F, 0.140F) * severity;
        blade.shade = 0.70F - 0.24F * severity;
    }

    private static void applyMissing(BladeRenderState blade) {
        blade.geometry = BladeGeometry.MISSING;
        blade.crackA = false;
        blade.crackB = false;
        blade.tiltX = 0.0F;
        blade.tiltY = 0.0F;
        blade.tiltZ = 0.0F;
        blade.offsetY = 0.0F;
        blade.offsetZ = 0.0F;
        blade.shade = 0.35F;
    }

    private static float mix(float from, float to, float progress) {
        return Mth.lerp(Mth.clamp(progress, 0.0F, 1.0F), from, to);
    }

    private static int[] bladeOrder(long seed) {
        int[] order = new int[]{0, 1, 2, 3};

        for (int i = order.length - 1; i > 0; i--) {
            int j = boundedIndex(seed, 100 + i, i + 1);
            int tmp = order[i];
            order[i] = order[j];
            order[j] = tmp;
        }

        return order;
    }

    private static int boundedIndex(long seed, int salt, int bound) {
        long mixed = mix64(seed + GOLDEN * (salt + 1L));
        return (int) Long.remainderUnsigned(mixed, bound);
    }

    private static float range(long seed, int salt, float min, float max) {
        long mixed = mix64(seed + GOLDEN * (salt + 1L));
        float t = ((mixed >>> 40) & 0xFFFFFFL) / (float) 0xFFFFFFL;
        return Mth.lerp(t, min, max);
    }

    private static float signedRange(long seed, int salt, float minAbs, float maxAbs) {
        float value = range(seed, salt, minAbs, maxAbs);
        long signBits = mix64(seed ^ (GOLDEN * (salt + 17L)));
        return (signBits & 1L) == 0L ? value : -value;
    }

    private static long mix64(long z) {
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }

    private boolean matches(int currentDamage, int maxDamage, int diameter) {
        return this.currentDamage == currentDamage
                && this.maxDamage == maxDamage
                && this.diameter == diameter;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public int getDamageState() {
        return damageState;
    }

    public float getDamageFraction() {
        return damageFraction;
    }

    public float getWearTint() {
        return wearTint;
    }

    public float getCrackTint() {
        return crackTint;
    }

    public BladeRenderState getBlade(int index) {
        return blades[index];
    }

    public float getBladeWobble(int bladeIndex, float animationTime, boolean spinning) {
        if (!spinning || vibration <= 0.0F) {
            return 0.0F;
        }

        BladeRenderState blade = blades[bladeIndex];
        float base = Mth.sin(animationTime * 0.18F + blade.phase) * vibration;
        float extra = Mth.cos(animationTime * 0.29F + blade.phase * 0.75F) * vibration * 0.30F;
        return base + extra;
    }

    public enum BladeGeometry {
        FULL,
        ERODED,
        CHIPPED,
        BROKEN,
        STUMP,
        MISSING
    }

    public static final class BladeRenderState {
        private BladeGeometry geometry = BladeGeometry.FULL;
        private boolean crackA;
        private boolean crackB;
        private float tiltX;
        private float tiltY;
        private float tiltZ;
        private float offsetY;
        private float offsetZ;
        private float shade = 1.0F;
        private float phase;

        public BladeGeometry getGeometry() {
            return geometry;
        }

        public boolean hasCrackA() {
            return crackA;
        }

        public boolean hasCrackB() {
            return crackB;
        }

        public float getTiltX() {
            return tiltX;
        }

        public float getTiltY() {
            return tiltY;
        }

        public float getTiltZ() {
            return tiltZ;
        }

        public float getOffsetY() {
            return offsetY;
        }

        public float getOffsetZ() {
            return offsetZ;
        }

        public float getShade() {
            return shade;
        }
    }
}