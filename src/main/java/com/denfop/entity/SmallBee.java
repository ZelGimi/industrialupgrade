package com.denfop.entity;

import com.denfop.api.bee.Bee;
import com.denfop.api.bee.BeeNetwork;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.crop.TileEntityCrop;
import com.denfop.mixin.access.BeeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.*;

public class SmallBee extends net.minecraft.world.entity.animal.Bee {

    private static final String TAG_CUSTOM_HIVE_POS = "IUCustomHivePos";
    private static final String TAG_LEGACY_HIVE_POS = "HivePos";
    private static final String TAG_HAS_CUSTOM_NECTAR = "HasCustomNectar";
    private static final String TAG_CROP_TARGETS = "IUCropTargets";
    private static final String TAG_CURRENT_CROP_TARGET = "IUCurrentCropTarget";
    private static final String TAG_LIFE_TICKS = "IULifeTicks";
    private static final String TAG_STUCK_TICKS = "IUStuckTicks";
    private static final String TAG_REPATH_COOLDOWN = "IURepathCooldown";
    private static final String TAG_RECOVERY_TICKS = "IURecoveryTicks";
    private static final String TAG_NEEDS_RECOVERY = "IUNeedsRecovery";

    private static final int MAX_WORK_TICKS = 200;
    private static final int RECOVERY_GRACE_TICKS = 10;
    private static final int MAX_STUCK_TICKS = 40;
    private static final int PROGRESS_SAMPLE_INTERVAL = 10;
    private static final EntityDataAccessor<Integer> DATA_BEE_ID =
            SynchedEntityData.defineId(SmallBee.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_BEE_NAME =
            SynchedEntityData.defineId(SmallBee.class, EntityDataSerializers.STRING);
    private static final String TAG_BEE_ID = "IUBeeId";
    private static final String TAG_BEE_NAME = "IUBeeName";
    private final List<BlockPos> cropTargets = new ArrayList<>();
    public Bee bee;
    private BlockPos hivePos;
    private BlockPos currentCropTarget;

    private boolean hasCustomNectar = false;
    private boolean needsRecovery = false;
    private boolean bootstrapResolved = false;

    private int lifeTicks = 0;
    private int stuckTicks = 0;
    private int repathCooldown = 0;
    private int recoveryTicks = 0;
    private int progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;

    private Vec3 lastProgressPos = Vec3.ZERO;

    public SmallBee(EntityType<? extends net.minecraft.world.entity.animal.Bee> type, Level level) {
        super(type, level);
        this.setInvulnerable(true);
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.removeConflictingVanillaBeeGoals(this.goalSelector);

        this.goalSelector.addGoal(1, new ReturnToHiveGoal(this));
        this.goalSelector.addGoal(2, new CollectCustomNectarGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326248_) {
        super.defineSynchedData(p_326248_);
        p_326248_.define(DATA_BEE_ID, -1);
        p_326248_.define(DATA_BEE_NAME, "");
    }


    private void removeConflictingVanillaBeeGoals(GoalSelector selector) {
        selector.getAvailableGoals().removeIf(this::isConflictingVanillaBeeGoal);
    }

    private boolean isConflictingVanillaBeeGoal(WrappedGoal wrappedGoal) {
        Goal goal = wrappedGoal.getGoal();
        String className = goal.getClass().getName();

        return goal instanceof TemptGoal
                || className.startsWith("net.minecraft.world.entity.animal.Bee$");
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void setCrops(List<TileEntityCrop> crops) {
        this.cropTargets.clear();

        if (crops == null || crops.isEmpty()) {
            return;
        }

        Set<BlockPos> unique = new LinkedHashSet<>();
        for (TileEntityCrop crop : crops) {
            if (crop != null && crop.getCrop() != null && crop.getPos() != null) {
                unique.add(crop.getPos().immutable());
            }
        }

        this.cropTargets.addAll(unique);
    }

    public void setBee(Bee bee) {
        this.bee = bee;

        int beeId = bee != null ? bee.getId() : -1;
        String beeName = (bee != null && bee.getName() != null) ? bee.getName() : "";

        this.entityData.set(DATA_BEE_ID, beeId);
        this.entityData.set(DATA_BEE_NAME, beeName);
    }

    public int getBeeRenderId() {
        return this.entityData.get(DATA_BEE_ID);
    }

    public String getBeeRenderName() {
        return this.entityData.get(DATA_BEE_NAME);
    }

    public Bee getBeeOrResolve() {
        if (this.bee != null) {
            return this.bee;
        }

        int beeId = this.getBeeRenderId();
        if (beeId >= 0) {
            Bee resolved = BeeNetwork.instance.getBee(beeId);
            if (resolved != null) {
                this.bee = resolved;
            }
        }

        return this.bee;
    }

    public boolean hasCustomNectar() {
        return this.hasCustomNectar;
    }

    public void setHasCustomNectar(boolean value) {
        this.hasCustomNectar = value;

        if (!this.level().isClientSide()) {
            ((BeeAccessor) this).invokeSetFlag(8, value);
        }
    }

    public BlockPos getCustomHive() {
        return this.hivePos;
    }

    public void setCustomHive(BlockPos pos) {
        this.hivePos = pos == null ? null : pos.immutable();
    }

    public boolean isRecovering() {
        return this.needsRecovery;
    }

    public boolean hasCropTarget() {
        return this.currentCropTarget != null;
    }

    public boolean shouldTerminateWork() {
        return this.isRemoved() || this.lifeTicks >= MAX_WORK_TICKS;
    }

    public boolean hasValidCustomHive() {
        return this.isHivePosUsable(this.hivePos);
    }

    private boolean isHivePosUsable(BlockPos pos) {
        if (pos == null) {
            return false;
        }

        if (!this.level().hasChunkAt(pos)) {
            return true;
        }

        return this.level().getBlockEntity(pos) instanceof BlockEntityApiary;
    }

    private boolean isLoadedCropValid(BlockPos pos) {
        if (pos == null) {
            return false;
        }

        return this.level().getBlockEntity(pos) instanceof TileEntityCrop crop && crop.getCrop() != null;
    }

    private boolean isCropPosUsable(BlockPos pos) {
        if (pos == null) {
            return false;
        }

        if (!this.level().hasChunkAt(pos)) {
            return true;
        }

        return this.isLoadedCropValid(pos);
    }

    public boolean ensureCropTarget() {
        if (this.currentCropTarget != null && this.isCropPosUsable(this.currentCropTarget)) {
            return true;
        }

        if (this.currentCropTarget != null && this.level().hasChunkAt(this.currentCropTarget) && !this.isLoadedCropValid(this.currentCropTarget)) {
            this.cropTargets.removeIf(pos -> pos.equals(this.currentCropTarget));
        }

        this.currentCropTarget = null;
        this.removeInvalidLoadedCropTargets();

        if (this.cropTargets.isEmpty()) {
            return false;
        }

        List<BlockPos> candidates = new ArrayList<>();
        for (BlockPos pos : this.cropTargets) {
            if (this.isCropPosUsable(pos)) {
                candidates.add(pos);
            }
        }

        if (candidates.isEmpty()) {
            return false;
        }

        this.currentCropTarget = candidates.get(this.random.nextInt(candidates.size())).immutable();
        return true;
    }

    private void removeInvalidLoadedCropTargets() {
        this.cropTargets.removeIf(pos -> this.level().hasChunkAt(pos) && !this.isLoadedCropValid(pos));
    }

    public boolean isAtCurrentCrop() {
        return this.currentCropTarget != null
                && this.position().distanceTo(Vec3.atCenterOf(this.currentCropTarget)) < 1.5D;
    }

    public boolean isAtCustomHive() {
        return this.hivePos != null
                && this.position().distanceTo(Vec3.atCenterOf(this.hivePos)) < 1.5D;
    }

    public void finishAtCrop() {
        if (this.currentCropTarget == null) {
            return;
        }

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();
            serverLevel.sendParticles(
                    new DustParticleOptions(new Vector3f(1.0f, 0.8f, 0.2f), 1.0f),
                    this.currentCropTarget.getX() + 0.5D,
                    this.currentCropTarget.getY() + 0.3D,
                    this.currentCropTarget.getZ() + 0.5D,
                    10,
                    0.1D,
                    0.1D,
                    0.1D,
                    0.01D
            );
            this.level().levelEvent(1505, this.currentCropTarget, 0);
        }

        this.setHasCustomNectar(true);
        this.currentCropTarget = null;
        this.stopCustomNavigation();
        this.resetPathingStateForGoal();
    }

    public void finishAtHive() {
        this.setHasCustomNectar(false);
        this.stopCustomNavigation();
        this.discard();
    }

    public void safeDiscard() {
        this.stopCustomNavigation();
        this.discard();
    }

    public void resetPathingStateForGoal() {
        this.stuckTicks = 0;
        this.repathCooldown = 0;
        this.progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;
        this.lastProgressPos = this.position();
    }

    public void stopCustomNavigation() {
        this.getNavigation().stop();
    }

    public void moveTowardsCurrentCrop() {
        if (this.repathCooldown > 0 || this.currentCropTarget == null) {
            return;
        }

        if (!this.level().hasChunkAt(this.currentCropTarget)) {
            return;
        }

        Vec3 approach = this.getCropApproachPos();
        if (approach == null) {
            return;
        }

        boolean moved = this.pathfindRandomlyTowards(approach);
        this.repathCooldown = moved ? 6 : 12;

        if (!moved) {
            this.stuckTicks += 10;
            if (this.stuckTicks >= MAX_STUCK_TICKS) {
                this.recoverFromStuck();
            }
        }
    }

    public void moveTowardsHive() {
        if (this.repathCooldown > 0 || this.hivePos == null) {
            return;
        }

        if (!this.level().hasChunkAt(this.hivePos)) {
            return;
        }

        Vec3 approach = this.getHiveApproachPos();
        if (approach == null) {
            return;
        }

        boolean moved = this.pathfindRandomlyTowards(approach);
        this.repathCooldown = moved ? 6 : 12;

        if (!moved) {
            this.stuckTicks += 10;
            if (this.stuckTicks >= MAX_STUCK_TICKS) {
                this.recoverFromStuck();
            }
        }
    }

    public boolean pathfindRandomlyTowards(Vec3 targetCenter) {
        if (targetCenter == null) {
            return false;
        }

        BlockPos targetPos = BlockPos.containing(targetCenter);

        if (!this.level().hasChunkAt(targetPos)) {
            return false;
        }

        BlockPos currentPos = this.blockPosition();
        double distanceSq = this.position().distanceToSqr(targetCenter);

        if (distanceSq < 0.5D * 0.5D) {
            return true;
        }

        int verticalOffset = 0;
        int yDiff = targetPos.getY() - currentPos.getY();

        if (yDiff > 2) {
            verticalOffset = 4;
        } else if (yDiff < -2) {
            verticalOffset = -4;
        }

        int horizontal = 6;
        int vertical = 8;
        int manhattan = currentPos.distManhattan(targetPos);

        if (manhattan < 15) {
            horizontal = Math.max(manhattan / 2, 1);
            vertical = Math.max(manhattan / 2, 1);
        }

        Vec3 airPos = AirRandomPos.getPosTowards(
                this,
                horizontal,
                vertical,
                verticalOffset,
                targetCenter,
                (float) Math.PI / 10F
        );

        this.getNavigation().setMaxVisitedNodesMultiplier(1.0F);

        if (manhattan < 4 || airPos == null) {
            return this.getNavigation().moveTo(targetCenter.x, targetCenter.y, targetCenter.z, 1.0D);
        }

        return this.getNavigation().moveTo(airPos.x, airPos.y, airPos.z, 1.0D);
    }

    private void recoverFromStuck() {
        this.stopCustomNavigation();
        this.stuckTicks = 0;
        this.repathCooldown = 10;

        if (this.onGround() || this.horizontalCollision || this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.25D, 0.0D));
            this.hasImpulse = true;
        }

        Vec3 objective = this.hasCustomNectar ? this.getHiveApproachPos() : this.getCropApproachPos();
        if (objective == null) {
            return;
        }

        if (!this.pathfindRandomlyTowards(objective)) {
            Vec3 fallback = new Vec3(
                    objective.x + (this.random.nextDouble() - 0.5D) * 4.0D,
                    Math.max(this.getY() + 1.0D, objective.y + this.random.nextInt(3)),
                    objective.z + (this.random.nextDouble() - 0.5D) * 4.0D
            );
            this.getNavigation().moveTo(fallback.x, fallback.y, fallback.z, 1.0D);
        }
    }

    private Vec3 makeFallbackAirMove(BlockPos objective) {
        double x = objective.getX() + 0.5D + (this.random.nextDouble() - 0.5D) * 4.0D;
        double y = Math.max(this.getY() + 1.0D, objective.getY() + 1.0D + this.random.nextInt(3));
        double z = objective.getZ() + 0.5D + (this.random.nextDouble() - 0.5D) * 4.0D;
        return new Vec3(x, y, z);
    }

    private BlockPos getCurrentObjectiveApproachPos() {
        if (this.hasCustomNectar) {
            return this.hivePos == null ? null : this.hivePos.above();
        }

        return this.currentCropTarget == null ? null : this.currentCropTarget.below();
    }

    private void validateRuntimeState() {
        if (this.shouldTerminateWork()) {
            this.safeDiscard();
            return;
        }

        if (this.hivePos == null) {
            if (!this.needsRecovery) {
                this.safeDiscard();
            }
            return;
        }

        if (!this.isHivePosUsable(this.hivePos)) {
            if (!this.needsRecovery) {
                this.safeDiscard();
            }
            return;
        }

        if (this.hasCustomNectar) {
            this.currentCropTarget = null;
            return;
        }

        if (!this.ensureCropTarget() && !this.needsRecovery) {
            this.safeDiscard();
        }
    }

    private void tickRecoveryState() {
        this.stopCustomNavigation();

        if (this.recoveryTicks > 0) {
            this.recoveryTicks--;
            return;
        }

        this.needsRecovery = false;
        this.resetPathingStateForGoal();
        this.validateRuntimeState();
    }

    private void tickStuckDetection() {
        if (this.needsRecovery) {
            return;
        }

        BlockPos objective = this.getCurrentObjectiveApproachPos();
        if (objective == null) {
            this.stuckTicks = 0;
            this.lastProgressPos = this.position();
            this.progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;
            return;
        }

        if (this.hasCustomNectar ? this.isAtCustomHive() : this.isAtCurrentCrop()) {
            this.stuckTicks = 0;
            this.lastProgressPos = this.position();
            this.progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;
            return;
        }

        if (this.horizontalCollision || this.verticalCollision) {
            this.stuckTicks += 5;
        }

        if (this.progressSampleCooldown > 0) {
            this.progressSampleCooldown--;
            return;
        }

        this.progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;

        double movedDistance = this.position().distanceTo(this.lastProgressPos);
        this.lastProgressPos = this.position();

        if (!this.getNavigation().isDone() && movedDistance < 0.15D) {
            this.stuckTicks += PROGRESS_SAMPLE_INTERVAL;
        } else {
            this.stuckTicks = Math.max(0, this.stuckTicks - PROGRESS_SAMPLE_INTERVAL);
        }

        if (this.stuckTicks >= MAX_STUCK_TICKS) {
            this.recoverFromStuck();
        }
    }

    private void bootstrapFromWorld() {
        if (this.hivePos == null) {
            this.hivePos = this.findNearbyApiary();
        }

        if (this.bee == null && this.hivePos != null && this.level().getBlockEntity(this.hivePos) instanceof BlockEntityApiary apiary) {
            this.bee = apiary.getQueen();
        }

        if (!this.hasCustomNectar && this.currentCropTarget == null) {
            this.ensureCropTarget();
        }

        if (this.lastProgressPos == Vec3.ZERO) {
            this.lastProgressPos = this.position();
        }
    }

    private BlockPos findNearbyApiary() {
        BlockPos origin = BlockPos.containing(this.position());

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos checkPos = origin.offset(dx, dy, dz);
                    if (this.level().getBlockEntity(checkPos) instanceof BlockEntityApiary) {
                        return checkPos.immutable();
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        if (!this.bootstrapResolved) {
            this.bootstrapResolved = true;
            this.bootstrapFromWorld();
        }

        this.lifeTicks++;
        if (this.onGround() || this.horizontalCollision) {
            this.setDeltaMovement(
                    this.getDeltaMovement().x,
                    Math.max(this.getDeltaMovement().y, 0.12D),
                    this.getDeltaMovement().z
            );
            this.hasImpulse = true;
        }
        if (this.repathCooldown > 0) {
            this.repathCooldown--;
        }

        if (this.needsRecovery) {
            this.tickRecoveryState();
        } else {
            this.validateRuntimeState();
        }

        if (!this.isRemoved()) {
            this.tickStuckDetection();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putBoolean(TAG_HAS_CUSTOM_NECTAR, this.hasCustomNectar);

        if (this.hivePos != null) {
            tag.put(TAG_CUSTOM_HIVE_POS, NbtUtils.writeBlockPos(this.hivePos));
        }

        if (this.currentCropTarget != null) {
            tag.put(TAG_CURRENT_CROP_TARGET, NbtUtils.writeBlockPos(this.currentCropTarget));
        }

        ListTag cropList = new ListTag();
        for (BlockPos pos : this.cropTargets) {
            cropList.add(NbtUtils.writeBlockPos(pos));
        }
        tag.put(TAG_CROP_TARGETS, cropList);

        tag.putInt(TAG_LIFE_TICKS, this.lifeTicks);
        tag.putInt(TAG_STUCK_TICKS, this.stuckTicks);
        tag.putInt(TAG_REPATH_COOLDOWN, this.repathCooldown);
        tag.putInt(TAG_RECOVERY_TICKS, this.recoveryTicks);
        tag.putBoolean(TAG_NEEDS_RECOVERY, this.needsRecovery);
        int beeId = this.entityData.get(DATA_BEE_ID);
        String beeName = this.entityData.get(DATA_BEE_NAME);

        if (beeId >= 0) {
            tag.putInt(TAG_BEE_ID, beeId);
        }
        if (!beeName.isEmpty()) {
            tag.putString(TAG_BEE_NAME, beeName);
        }
    }

    public void launchFromHive() {
        if (this.hivePos != null) {
            Vec3 start = Vec3.atCenterOf(this.hivePos).add(0.0D, 1.05D, 0.0D);
            this.moveTo(start.x, start.y, start.z, this.getYRot(), this.getXRot());
        }

        this.stopCustomNavigation();
        this.resetPathingStateForGoal();

        this.setDeltaMovement(
                (this.random.nextDouble() - 0.5D) * 0.05D,
                0.18D,
                (this.random.nextDouble() - 0.5D) * 0.05D
        );
        this.hasImpulse = true;
    }

    private Vec3 getCropApproachPos() {
        if (this.currentCropTarget == null) {
            return null;
        }
        return Vec3.atCenterOf(this.currentCropTarget).add(0.0D, 0.6D, 0.0D);
    }

    private Vec3 getHiveApproachPos() {
        if (this.hivePos == null) {
            return null;
        }
        return Vec3.atCenterOf(this.hivePos).add(0.0D, 1.05D, 0.0D);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.cropTargets.clear();

        this.hasCustomNectar = tag.getBoolean(TAG_HAS_CUSTOM_NECTAR);

        if (tag.contains(TAG_CUSTOM_HIVE_POS, Tag.TAG_COMPOUND)) {
            this.hivePos = NbtUtils.readBlockPos(tag.getCompound(TAG_CUSTOM_HIVE_POS), "position").get();
        } else if (tag.contains(TAG_LEGACY_HIVE_POS, Tag.TAG_COMPOUND)) {
            this.hivePos = NbtUtils.readBlockPos(tag.getCompound(TAG_LEGACY_HIVE_POS), "position").get();
        } else {
            this.hivePos = null;
        }

        if (tag.contains(TAG_CURRENT_CROP_TARGET, Tag.TAG_COMPOUND)) {
            this.currentCropTarget = NbtUtils.readBlockPos(tag.getCompound(TAG_CURRENT_CROP_TARGET), "position").get();
        } else {
            this.currentCropTarget = null;
        }

        if (tag.contains(TAG_CROP_TARGETS, Tag.TAG_LIST)) {
            ListTag cropList = tag.getList(TAG_CROP_TARGETS, Tag.TAG_COMPOUND);
            for (int i = 0; i < cropList.size(); i++) {
                this.cropTargets.add(NbtUtils.readBlockPos(cropList.getCompound(i), "position").get());
            }
        }

        this.lifeTicks = tag.getInt(TAG_LIFE_TICKS);
        this.stuckTicks = tag.getInt(TAG_STUCK_TICKS);
        this.repathCooldown = tag.getInt(TAG_REPATH_COOLDOWN);
        this.recoveryTicks = tag.contains(TAG_RECOVERY_TICKS, Tag.TAG_INT) ? tag.getInt(TAG_RECOVERY_TICKS) : RECOVERY_GRACE_TICKS;
        this.needsRecovery = tag.contains(TAG_NEEDS_RECOVERY, Tag.TAG_BYTE)
                ? tag.getBoolean(TAG_NEEDS_RECOVERY)
                : true;

        this.bootstrapResolved = false;
        this.progressSampleCooldown = PROGRESS_SAMPLE_INTERVAL;
        this.lastProgressPos = this.position();

        if (this.recoveryTicks <= 0) {
            this.recoveryTicks = RECOVERY_GRACE_TICKS;
        }
        int beeId = tag.contains(TAG_BEE_ID) ? tag.getInt(TAG_BEE_ID) : -1;
        String beeName = tag.contains(TAG_BEE_NAME) ? tag.getString(TAG_BEE_NAME) : "";
        this.entityData.set(DATA_BEE_ID, beeId);
        this.entityData.set(DATA_BEE_NAME, beeName);

        if (!this.level().isClientSide() && beeId >= 0) {
            Bee resolved = BeeNetwork.instance.getBee(beeId);
            this.bee = resolved;
        } else {
            this.bee = null;
        }
        this.setHasCustomNectar(this.hasCustomNectar);
    }
}

class ReturnToHiveGoal extends Goal {

    private final SmallBee bee;

    public ReturnToHiveGoal(SmallBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !this.bee.isRecovering()
                && this.bee.hasCustomNectar()
                && this.bee.hasValidCustomHive();
    }

    @Override
    public void start() {
        this.bee.resetPathingStateForGoal();
        this.bee.moveTowardsHive();
    }

    @Override
    public void tick() {
        if (this.bee.shouldTerminateWork()) {
            this.bee.safeDiscard();
            return;
        }

        if (!this.bee.hasValidCustomHive()) {
            this.bee.safeDiscard();
            return;
        }

        if (this.bee.isAtCustomHive()) {
            this.bee.finishAtHive();
            return;
        }

        this.bee.moveTowardsHive();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.bee.isRecovering()
                && this.bee.hasCustomNectar()
                && this.bee.hasValidCustomHive()
                && !this.bee.isRemoved();
    }

    @Override
    public void stop() {
        this.bee.stopCustomNavigation();
    }
}

class CollectCustomNectarGoal extends Goal {

    private final SmallBee bee;

    public CollectCustomNectarGoal(SmallBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !this.bee.isRecovering()
                && !this.bee.hasCustomNectar()
                && this.bee.hasValidCustomHive()
                && this.bee.ensureCropTarget();
    }

    @Override
    public void start() {
        this.bee.resetPathingStateForGoal();
        this.bee.moveTowardsCurrentCrop();
    }

    @Override
    public void tick() {
        if (this.bee.shouldTerminateWork()) {
            this.bee.safeDiscard();
            return;
        }

        if (!this.bee.hasValidCustomHive()) {
            this.bee.safeDiscard();
            return;
        }

        if (!this.bee.ensureCropTarget()) {
            this.bee.safeDiscard();
            return;
        }

        if (this.bee.isAtCurrentCrop()) {
            this.bee.finishAtCrop();
            return;
        }

        this.bee.moveTowardsCurrentCrop();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.bee.isRecovering()
                && !this.bee.hasCustomNectar()
                && this.bee.hasCropTarget()
                && !this.bee.isRemoved();
    }

    @Override
    public void stop() {
        this.bee.stopCustomNavigation();
    }
}