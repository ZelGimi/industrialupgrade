package com.denfop.render.windgenerator;

import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.mechanism.wind.BlockEntityWindGenerator;
import com.denfop.blockentity.windturbine.BlockEntityWindTurbineController;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class RotorDamageProfile {

    private static final int BLADE_COUNT = 4;
    private static final int CACHE_LIMIT = 2048;
    private static final int DAMAGE_STATES = 12;
    private static final long GOLDEN = 0x9E3779B97F4A7C15L;

    private static final Map<Long, RotorDamageProfile> CACHE = new HashMap<>();
    private final int currentDamage;
    private final int maxDamage;
    private final int diameter;
    private final int damageLevel;
    private final float damageFraction;
    private final float wearTint;
    private final float crackTint;
    private final float vibration;
    private final BladeRenderState[] blades;
    private RotorDamageProfile(
            int currentDamage,
            int maxDamage,
            int diameter,
            int damageLevel,
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
        this.damageFraction = damageFraction;
        this.wearTint = wearTint;
        this.crackTint = crackTint;
        this.vibration = vibration;
        this.blades = blades;
    }

    public static RotorDamageProfile resolve(BlockEntityWindGenerator tile) {
        return resolveInternal(
                tile.getBlockPos().asLong(),
                tile.getRotorDiameter(),
                tile.getRotor(),
                tile.slot.get(0)
        );
    }

    public static RotorDamageProfile resolve(BlockEntityWindTurbineController tile) {
        return resolveInternal(
                tile.getBlockPos().asLong(),
                tile.getRotorDiameter(),
                tile.getRotor(),
                tile.slot.get(0)
        );
    }

    private static RotorDamageProfile resolveInternal(long posSeed, int diameter, WindRotor rotor, ItemStack stack) {
        int currentDamage = 0;
        int maxDamage = 0;

        if (rotor != null && !stack.isEmpty()) {
            maxDamage = Math.max(0, rotor.getMaxCustomDamage(stack));
            currentDamage = Mth.clamp(rotor.getCustomDamage(stack), 0, maxDamage);
        }

        long cacheKey = posSeed ^ (((long) diameter) << 48);
        RotorDamageProfile cached = CACHE.get(cacheKey);
        if (cached != null && cached.matches(currentDamage, maxDamage, diameter)) {
            return cached;
        }

        RotorDamageProfile built = build(cacheKey, currentDamage, maxDamage, diameter);

        if (CACHE.size() > CACHE_LIMIT) {
            CACHE.clear();
        }

        CACHE.put(cacheKey, built);
        return built;
    }

    private static RotorDamageProfile build(long seed, int currentDamage, int maxDamage, int diameter) {
        float fraction = maxDamage <= 0 ? 0.0F : Mth.clamp(currentDamage / (float) maxDamage, 0.0F, 1.0F);

        int state = toDamageState(fraction);
        float stateSpan = DAMAGE_STATES <= 1 ? 1.0F : 1.0F / DAMAGE_STATES;
        float stateStart = state * stateSpan;
        float localProgress = state == DAMAGE_STATES - 1
                ? 1.0F
                : Mth.clamp((fraction - stateStart) / stateSpan, 0.0F, 1.0F);

        float wearTint = Mth.clamp(1.0F - fraction * 0.42F, 0.42F, 1.0F);
        float crackTint = Mth.clamp(wearTint - 0.36F, 0.08F, 0.72F);

        float vibration = switch (state) {
            case 0, 1, 2 -> 0.0F;
            case 3, 4 -> 0.25F + fraction * 0.45F;
            case 5, 6, 7 -> 0.85F + fraction * 0.95F;
            case 8, 9 -> 1.75F + fraction * 1.35F;
            default -> 3.2F + fraction * 2.25F;
        };

        BladeRenderState[] blades = new BladeRenderState[BLADE_COUNT];
        for (int i = 0; i < BLADE_COUNT; i++) {
            BladeRenderState stateObj = new BladeRenderState();
            stateObj.phase = range(seed, 300 + i, 0.0F, (float) (Math.PI * 2.0));
            blades[i] = stateObj;
        }

        int[] order = bladeOrder(seed);
        int a = order[0];
        int b = order[1];
        int c = order[2];
        int d = order[3];

        switch (state) {
            case 0 -> {
                // Ідеальний стан.
            }

            case 1 -> {
                applyHairline(blades[a], seed, 10, mix(0.15F, 0.45F, localProgress));
            }

            case 2 -> {
                applyHairline(blades[a], seed, 20, mix(0.50F, 0.90F, localProgress));
                applyMicroWear(blades[b], seed, 21, mix(0.12F, 0.35F, localProgress));
            }

            case 3 -> {
                applyChipped(blades[a], seed, 30, mix(0.18F, 0.45F, localProgress));
                applyHairline(blades[b], seed, 31, mix(0.35F, 0.70F, localProgress));
            }

            case 4 -> {
                applyChipped(blades[a], seed, 40, mix(0.50F, 0.85F, localProgress));
                applyHairline(blades[b], seed, 41, mix(0.55F, 0.95F, localProgress));
                applyMicroWear(blades[c], seed, 42, mix(0.10F, 0.28F, localProgress));
            }

            case 5 -> {
                applyBroken(blades[a], seed, 50, mix(0.20F, 0.45F, localProgress));
                applyChipped(blades[b], seed, 51, mix(0.35F, 0.60F, localProgress));
                applyHairline(blades[c], seed, 52, mix(0.28F, 0.55F, localProgress));
            }

            case 6 -> {
                applyBroken(blades[a], seed, 60, mix(0.55F, 0.85F, localProgress));
                applyChipped(blades[b], seed, 61, mix(0.62F, 0.92F, localProgress));
                applyHairline(blades[c], seed, 62, mix(0.55F, 0.90F, localProgress));
                applyMicroWear(blades[d], seed, 63, mix(0.12F, 0.30F, localProgress));
            }

            case 7 -> {
                applyStump(blades[a], seed, 70, mix(0.18F, 0.45F, localProgress));
                applyBroken(blades[b], seed, 71, mix(0.45F, 0.75F, localProgress));
                applyChipped(blades[c], seed, 72, mix(0.55F, 0.88F, localProgress));
                applyHairline(blades[d], seed, 73, mix(0.30F, 0.60F, localProgress));
            }

            case 8 -> {
                applyStump(blades[a], seed, 80, mix(0.55F, 0.90F, localProgress));
                applyBroken(blades[b], seed, 81, mix(0.78F, 1.00F, localProgress));
                applyBroken(blades[c], seed, 82, mix(0.22F, 0.48F, localProgress));
                applyChipped(blades[d], seed, 83, mix(0.45F, 0.72F, localProgress));
            }

            case 9 -> {
                applyMissing(blades[a]);
                applyStump(blades[b], seed, 91, mix(0.28F, 0.62F, localProgress));
                applyBroken(blades[c], seed, 92, mix(0.58F, 0.88F, localProgress));
                applyBroken(blades[d], seed, 93, mix(0.35F, 0.65F, localProgress));
            }

            case 10 -> {
                applyMissing(blades[a]);
                applyStump(blades[b], seed, 101, mix(0.68F, 1.00F, localProgress));
                applyStump(blades[c], seed, 102, mix(0.22F, 0.55F, localProgress));
                applyBroken(blades[d], seed, 103, mix(0.72F, 1.00F, localProgress));
            }

            case 11 -> {
                boolean fullCollapse = fraction >= 0.999F;

                applyMissing(blades[a]);
                applyMissing(blades[b]);

                if (fullCollapse) {
                    applyMissing(blades[c]);
                    applyStump(blades[d], seed, 113, 1.0F);
                } else {
                    applyStump(blades[c], seed, 111, mix(0.70F, 1.00F, localProgress));
                    applyStump(blades[d], seed, 112, mix(0.45F, 0.92F, localProgress));
                }
            }

            default -> {
            }
        }

        return new RotorDamageProfile(
                currentDamage,
                maxDamage,
                diameter,
                state,
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

    private static void applyMicroWear(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.FULL;
        blade.crackA = severity > 0.18F;
        blade.crackB = severity > 0.82F;
        blade.tiltX = signedRange(seed, salt, 0.10F, 0.55F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.15F, 0.85F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 0.35F, 1.40F) * severity;
        blade.shade = 1.0F - 0.05F * severity;
    }

    private static void applyHairline(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.FULL;
        blade.crackA = true;
        blade.crackB = severity > 0.70F;
        blade.tiltX = signedRange(seed, salt, 0.15F, 0.85F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.25F, 1.35F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 0.75F, 2.80F) * severity;
        blade.shade = 0.99F - 0.08F * severity;
    }

    private static void applyChipped(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.CHIPPED;
        blade.crackA = true;
        blade.crackB = severity > 0.42F;
        blade.tiltX = signedRange(seed, salt, 0.35F, 1.40F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 0.60F, 2.50F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 1.80F, 7.50F) * severity;
        blade.shade = 0.94F - 0.18F * severity;
    }

    private static void applyBroken(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.BROKEN;
        blade.crackA = true;
        blade.crackB = severity > 0.22F;
        blade.tiltX = signedRange(seed, salt, 0.85F, 3.60F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 1.10F, 4.60F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 3.80F, 14.50F) * severity;
        blade.shade = 0.84F - 0.22F * severity;
    }

    private static void applyStump(BladeRenderState blade, long seed, int salt, float severity) {
        blade.geometry = BladeGeometry.STUMP;
        blade.crackA = true;
        blade.crackB = true;
        blade.tiltX = signedRange(seed, salt, 1.20F, 5.50F) * severity;
        blade.tiltY = signedRange(seed, salt + 1, 1.80F, 6.50F) * severity;
        blade.tiltZ = signedRange(seed, salt + 2, 6.50F, 22.50F) * severity;
        blade.shade = 0.72F - 0.24F * severity;
    }

    private static void applyMissing(BladeRenderState blade) {
        blade.geometry = BladeGeometry.MISSING;
        blade.crackA = false;
        blade.crackB = false;
        blade.tiltX = 0.0F;
        blade.tiltY = 0.0F;
        blade.tiltZ = 0.0F;
        blade.shade = 0.40F;
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
        float base = Mth.sin(animationTime * 0.22F + blade.phase) * vibration;
        float extra = Mth.cos(animationTime * 0.37F + blade.phase * 0.7F) * vibration * 0.35F;
        return base + extra;
    }

    public enum BladeGeometry {
        FULL,
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

        public float getShade() {
            return shade;
        }
    }
}