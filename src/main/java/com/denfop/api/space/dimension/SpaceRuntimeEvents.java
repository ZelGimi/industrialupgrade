package com.denfop.api.space.dimension;

import com.denfop.IUCore;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Locale;

@EventBusSubscriber(modid = IUCore.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class SpaceRuntimeEvents {

    private static final String INERTIA_X = "iu_space_inertia_x";
    private static final String INERTIA_Y = "iu_space_inertia_y";
    private static final String INERTIA_Z = "iu_space_inertia_z";

    private SpaceRuntimeEvents() {
    }

    @SubscribeEvent
    public static void onLevelTick(final LevelTickEvent.Post event) {
        if (event.getLevel().isClientSide() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(level.dimension().location().getPath());
        if (profile == null) {
            return;
        }

        final long source = level.getServer().overworld().getDayTime();
        final long target = profile.fixedTime() != null
                ? profile.fixedTime()
                : Math.floorMod(Math.round(source * (24000.0D / Math.max(1L, profile.dayLength()))), 24000L);

        if (level.getDayTime() != target) {
            level.setDayTime(target);
        }
    }

    @SubscribeEvent
    public static void onLivingTick(final LivingBreatheEvent event) {
        final LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) {
            return;
        }

        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(entity.level().dimension().location().getPath());
        if (profile == null) {
            clearInertia(entity);
            return;
        }

        if (profile.isAsteroidField()) {
            applyAsteroidFieldPhysics(entity, profile);
            return;
        }

        applyGenericGravity(entity, profile);
        applyOxygenCheck(entity, profile);
        applyThermalCheck(entity, profile);
        applyPressureCheck(entity, profile);
    }

    private static void applyOxygenCheck(final LivingEntity entity, final SpaceDimensionProfile profile) {
        if (shouldIgnoreEnvironment(entity)) {
            return;
        }

        final boolean oxygenSafe = profile.body().oxygen()
                && profile.hasAtmosphere()
                && profile.atmosphereDensity() >= 0.15F;

        if (oxygenSafe || hasArmorUpgrade(entity, EnumInfoUpgradeModules.OXYGEN, 1)) {

            return;
        }


        if (entity.tickCount % 20 == 0) {
            entity.hurt(entity.damageSources().dryOut(), 2.0F);
        }
    }

    private static void applyThermalCheck(final LivingEntity entity, final SpaceDimensionProfile profile) {
        if (shouldIgnoreEnvironment(entity)) {
            return;
        }

        final int thermalLevel = getArmorUpgradeLevel(entity, EnumInfoUpgradeModules.THERMAL);


        final int maxSafeTemperature = 45 + (thermalLevel * 120);
        final int minSafeTemperature = -35 - (thermalLevel * 55);

        final int bodyTemperature = profile.body().temperature();

        if (bodyTemperature > maxSafeTemperature) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, true, true));

            if (entity.tickCount % 30 == 0) {
                entity.hurt(entity.damageSources().inFire(), 2.0F);
            }
            return;
        }

        if (bodyTemperature < minSafeTemperature) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, true, true));
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, true, true));

            entity.setTicksFrozen(Math.min(140, entity.getTicksFrozen() + 4));

            if (entity.tickCount % 30 == 0) {
                entity.hurt(entity.damageSources().freeze(), 2.0F);
            }
            return;
        }

        if (entity.getTicksFrozen() > 0) {
            entity.setTicksFrozen(Math.max(0, entity.getTicksFrozen() - 8));
        }
    }

    private static void applyPressureCheck(final LivingEntity entity, final SpaceDimensionProfile profile) {
        if (shouldIgnoreEnvironment(entity)) {
            return;
        }

        final boolean dangerousPressure = profile.body().pressure();

        if (!dangerousPressure) {
            return;
        }

        if (hasArmorUpgrade(entity, EnumInfoUpgradeModules.PRESSURE, 1)) {
            return;
        }


        entity.hurt(entity.damageSources().generic(), 1000.0F);
    }

    private static boolean shouldIgnoreEnvironment(final LivingEntity entity) {
        if (!entity.isAlive() || entity.isRemoved()) {
            return true;
        }

        if (entity instanceof Player player) {
            return player.isCreative() || player.isSpectator();
        }

        return false;
    }

    private static boolean hasArmorUpgrade(
            final LivingEntity entity,
            final EnumInfoUpgradeModules module,
            final int requiredLevel
    ) {
        return getArmorUpgradeLevel(entity, module) >= requiredLevel;
    }

    private static int getArmorUpgradeLevel(
            final LivingEntity entity,
            final EnumInfoUpgradeModules module
    ) {
        int total = 0;

        total += getArmorPieceUpgradeLevel(entity.getItemBySlot(EquipmentSlot.HEAD), module);
        if (total >= module.max) {
            return module.max;
        }

        total += getArmorPieceUpgradeLevel(entity.getItemBySlot(EquipmentSlot.CHEST), module);
        if (total >= module.max) {
            return module.max;
        }

        total += getArmorPieceUpgradeLevel(entity.getItemBySlot(EquipmentSlot.LEGS), module);
        if (total >= module.max) {
            return module.max;
        }

        total += getArmorPieceUpgradeLevel(entity.getItemBySlot(EquipmentSlot.FEET), module);
        return Math.min(total, module.max);
    }

    private static int getArmorPieceUpgradeLevel(
            final ItemStack stack,
            final EnumInfoUpgradeModules module
    ) {
        if (stack.isEmpty()) {
            return 0;
        }

        if (!(stack.getItem() instanceof UpgradeItem)) {
            return 0;
        }

        if (!UpgradeSystem.system.hasInMap(stack)) {
            return 0;
        }

        if (!UpgradeSystem.system.hasModules(module, stack)) {
            return 0;
        }

        final var installed = UpgradeSystem.system.getModules(module, stack);
        return installed == null ? 0 : Math.max(0, installed.number);
    }

    private static void applyGenericGravity(final LivingEntity entity, final SpaceDimensionProfile profile) {
        if (entity.isNoGravity() || entity.onGround() || entity.isInWater() || entity.isInLava()) {
            return;
        }
        if (entity instanceof Player player && (player.getAbilities().flying || player.isSpectator())) {
            return;
        }

        final double gravity = profile.gravity();
        final Vec3 motion = entity.getDeltaMovement();

        if (gravity < 1.0D) {
            if (motion.y < 0.0D) {
                entity.setDeltaMovement(motion.x, motion.y + (1.0D - gravity) * 0.045D, motion.z);
            }
        } else if (gravity > 1.0D) {
            entity.setDeltaMovement(motion.x, motion.y - (gravity - 1.0D) * 0.025D, motion.z);
        }
    }

    private static void applyAsteroidFieldPhysics(final LivingEntity entity, final SpaceDimensionProfile profile) {
        entity.fallDistance = 0.0F;

        if (entity.isNoGravity() || entity.isInWater() || entity.isInLava()) {
            storeInertia(entity, entity.getDeltaMovement());
            return;
        }

        if (entity instanceof Player player && (player.getAbilities().flying || player.isSpectator())) {
            clearInertia(entity);
            return;
        }

        Vec3 motion = entity.getDeltaMovement();
        final JetpackControlState controlState = JetpackControlState.resolve(entity);

        if (!entity.onGround()) {
            final double compensation = 0.08D - (0.08D * Mth.clamp(profile.gravity(), 0.0F, 1.0F));
            motion = motion.add(0.0D, compensation, 0.0D);
        }

        if (entity instanceof Player player) {
            motion = applyJetpackAssistance(player, motion, controlState);
        }

        motion = dampMotionAgainstAsteroids(entity, motion);
        motion = applyInertia(entity, motion, controlState);
        motion = clampMotion(motion, controlState.active ? 1.85D : 1.20D);

        if (motion.lengthSqr() < 1.0E-5D) {
            motion = Vec3.ZERO;
        }

        entity.setDeltaMovement(motion);
        storeInertia(entity, motion);
    }

    private static Vec3 applyJetpackAssistance(final Player player, final Vec3 baseMotion, final JetpackControlState controlState) {
        Vec3 motion = baseMotion;

        final boolean forward = IUCore.keyboard.isForwardKeyDown(player);
        final boolean jump = IUCore.keyboard.isJumpKeyDown(player);
        final boolean descend = player.isShiftKeyDown();

        if (!controlState.available) {
            return motion;
        }

        final double thrust = controlState.active ? 0.072D : 0.038D;
        final Vec3 look = player.getLookAngle().normalize();

        if (forward) {
            motion = motion.add(look.scale(thrust));
        }

        if (jump) {
            motion = motion.add(0.0D, thrust * 0.85D, 0.0D);
        }

        if (descend) {
            motion = controlState.hoverMode && !forward && !jump
                    ? motion.scale(0.82D)
                    : motion.add(0.0D, -thrust * 0.65D, 0.0D);
        }

        if (controlState.hoverMode && !forward && !jump && !descend) {
            motion = motion.scale(0.90D);
        }

        player.fallDistance = 0.0F;
        return motion;
    }

    private static Vec3 dampMotionAgainstAsteroids(final LivingEntity entity, final Vec3 motion) {
        if (motion.lengthSqr() < 1.0E-6D) {
            return motion;
        }

        final AABB box = entity.getBoundingBox();
        double x = motion.x;
        double y = motion.y;
        double z = motion.z;

        if (Math.abs(x) > 1.0E-4D && !entity.level().noCollision(entity, box.move(x, 0.0D, 0.0D))) {
            x = 0.0D;
        }
        if (Math.abs(y) > 1.0E-4D && !entity.level().noCollision(entity, box.move(0.0D, y, 0.0D))) {
            y = y > 0.0D ? 0.0D : Math.max(0.0D, y * 0.10D);
        }
        if (Math.abs(z) > 1.0E-4D && !entity.level().noCollision(entity, box.move(0.0D, 0.0D, z))) {
            z = 0.0D;
        }

        return new Vec3(x, y, z);
    }

    private static Vec3 applyInertia(final LivingEntity entity, final Vec3 motion, final JetpackControlState controlState) {
        if (entity.onGround() && !controlState.active) {
            return motion.scale(0.84D);
        }

        final Vec3 previous = readInertia(entity);
        if (controlState.active) {
            return motion;
        }

        final Vec3 preserved = previous.scale(controlState.hoverMode ? 0.965D : 0.9975D);

        final double x = preserveAxis(motion.x, preserved.x);
        final double y = preserveAxis(motion.y, preserved.y);
        final double z = preserveAxis(motion.z, preserved.z);

        Vec3 result = new Vec3(x, y, z);
        if (controlState.hoverMode) {
            result = result.scale(0.94D);
        }

        return result;
    }

    private static double preserveAxis(final double current, final double preserved) {
        if (Math.abs(current) < 0.003D) {
            return Math.abs(preserved) < 0.003D ? 0.0D : preserved;
        }
        if (Math.signum(current) != 0.0D && Math.signum(preserved) != 0.0D && Math.signum(current) != Math.signum(preserved)) {
            return current;
        }
        if (Math.abs(current) + 0.006D < Math.abs(preserved)) {
            return Mth.lerp(0.85D, current, preserved);
        }
        return current;
    }

    private static Vec3 clampMotion(final Vec3 motion, final double maxSpeed) {
        final double speed = motion.length();
        if (speed <= maxSpeed || speed < 1.0E-6D) {
            return motion;
        }
        return motion.scale(maxSpeed / speed);
    }

    @SubscribeEvent
    public static void onLivingJump(final LivingEvent.LivingJumpEvent event) {
        final LivingEntity entity = event.getEntity();
        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(entity.level().dimension().location().getPath());
        if (profile == null) {
            return;
        }
        if (entity instanceof Player player && player.getAbilities().flying) {
            return;
        }

        final Vec3 motion = entity.getDeltaMovement();
        if (profile.isAsteroidField()) {
            entity.setDeltaMovement(motion.x, motion.y * 1.85D, motion.z);
            entity.fallDistance = 0.0F;
            return;
        }

        if (profile.gravity() < 1.0D) {
            entity.setDeltaMovement(motion.x, motion.y * (1.0D + (1.0D - profile.gravity()) * 0.80D), motion.z);
        } else if (profile.gravity() > 1.0D) {
            entity.setDeltaMovement(motion.x, motion.y * Math.max(0.65D, 1.0D - (profile.gravity() - 1.0D) * 0.35D), motion.z);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(final LivingFallEvent event) {
        final SpaceDimensionProfile profile = SpaceBodyProfiles.byDimensionPath(event.getEntity().level().dimension().location().getPath());
        if (profile == null) {
            return;
        }

        if (profile.isAsteroidField()) {
            event.setCanceled(true);
            event.setDamageMultiplier(0.0F);
            return;
        }

        event.setDamageMultiplier(event.getDamageMultiplier() * profile.gravity());
    }

    private static void storeInertia(final LivingEntity entity, final Vec3 motion) {
        final CompoundTag tag = entity.getPersistentData();
        tag.putDouble(INERTIA_X, motion.x);
        tag.putDouble(INERTIA_Y, motion.y);
        tag.putDouble(INERTIA_Z, motion.z);
    }

    private static Vec3 readInertia(final LivingEntity entity) {
        final CompoundTag tag = entity.getPersistentData();
        return new Vec3(tag.getDouble(INERTIA_X), tag.getDouble(INERTIA_Y), tag.getDouble(INERTIA_Z));
    }

    private static void clearInertia(final LivingEntity entity) {
        final CompoundTag tag = entity.getPersistentData();
        tag.remove(INERTIA_X);
        tag.remove(INERTIA_Y);
        tag.remove(INERTIA_Z);
    }

    private record JetpackControlState(boolean available, boolean active, boolean hoverMode) {

        private static JetpackControlState resolve(final LivingEntity entity) {
            if (!(entity instanceof Player player)) {
                return new JetpackControlState(false, false, false);
            }

            final ItemStack chest = player.getInventory().armor.get(2);
            if (chest.isEmpty()) {
                return new JetpackControlState(false, false, false);
            }

            final CompoundTag nbt = ModUtils.nbt(chest);
            final String simpleName = chest.getItem().getClass().getSimpleName().toLowerCase(Locale.ROOT);
            final boolean looksLikeJetpack = simpleName.contains("jetpack")
                    || (nbt != null && (chest.getOrDefault(DataComponentsInit.JETPACK, false) || nbt.contains("hoverMode") || nbt.contains("vertical") || nbt.contains("canFly")));

            if (!looksLikeJetpack) {
                return new JetpackControlState(false, false, false);
            }

            final boolean active = nbt != null && chest.getOrDefault(DataComponentsInit.JETPACK, false);
            final boolean hover = nbt != null && (nbt.getBoolean("hoverMode") || nbt.getBoolean("vertical") || nbt.getBoolean("canFly"));
            return new JetpackControlState(true, active, hover);
        }
    }
}
