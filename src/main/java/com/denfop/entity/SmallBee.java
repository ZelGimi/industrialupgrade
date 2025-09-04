package com.denfop.entity;

import com.denfop.api.bee.Bee;
import com.denfop.blockentity.bee.BlockEntityApiary;
import com.denfop.blockentity.crop.TileEntityCrop;
import com.denfop.mixin.access.BeeAccessor;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class SmallBee extends net.minecraft.world.entity.animal.Bee {
    public Bee bee;
    List<TileEntityCrop> crops;
    boolean can = true;
    private BlockPos hivePos;
    private boolean hasCustomNectar = false;

    public SmallBee(EntityType<? extends net.minecraft.world.entity.animal.Bee> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, (double) 0.6F).add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    public void setCrops(List<TileEntityCrop> crops) {
        this.crops = crops;
    }

    public void setBee(Bee bee) {
        this.bee = bee;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(5, new CollectCustomNectarGoal(this));
        this.goalSelector.addGoal(6, new ReturnToHiveGoal(this));
    }

    public boolean hasCustomNectar() {
        return hasCustomNectar;
    }

    @Override
    public void tick() {
        super.tick();
        if (can) {
            if (bee == null) {
                this.hivePos = new BlockPos((int) this.position().x, (int) this.position().y, (int) this.position().z);
                if (this.getLevel().getBlockEntity(hivePos) instanceof BlockEntityApiary apiary) {
                    this.bee = apiary.getQueen();
                }
            }
            can = false;
        }
    }

    public void setHasCustomNectar(boolean value) {
        this.hasCustomNectar = value;
    }

    public BlockPos getCustomHive() {
        return hivePos;
    }

    public void setCustomHive(BlockPos pos) {
        this.hivePos = pos;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasCustomNectar", hasCustomNectar);
        if (hivePos != null) {
            tag.put("HivePos", NbtUtils.writeBlockPos(hivePos));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.hasCustomNectar = tag.getBoolean("HasCustomNectar");
        if (tag.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(tag.getCompound("HivePos"));
        }
    }

    public void pathfindRandomlyTowards(BlockPos targetPos) {
        Vec3 targetCenter = Vec3.atBottomCenterOf(targetPos);
        BlockPos currentPos = this.blockPosition();
        double distanceSq = this.position().distanceToSqr(targetCenter);

        if (distanceSq < 0.5 * 0.5) {
            return;
        }

        int verticalOffset = 0;
        int yDiff = (int) targetCenter.y - currentPos.getY();

        if (yDiff > 2) {
            verticalOffset = 4;
        } else if (yDiff < -2) {
            verticalOffset = -4;
        }

        int k = 6;
        int l = 8;
        int manhattan = currentPos.distManhattan(targetPos);

        if (manhattan < 15) {
            k = Math.max(manhattan / 2, 1);
            l = Math.max(manhattan / 2, 1);
        }

        Vec3 airPos = AirRandomPos.getPosTowards(this, k, l, verticalOffset, targetCenter, (float) Math.PI / 10F);


        if (manhattan < 4 || airPos == null) {
            this.navigation.setMaxVisitedNodesMultiplier(1.5F);
            this.navigation.moveTo(targetCenter.x, targetCenter.y, targetCenter.z, 1.0D);
        } else {
            this.navigation.setMaxVisitedNodesMultiplier(1.5F);
            this.navigation.moveTo(airPos.x, airPos.y, airPos.z, 1.0D);
        }
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
        if (bee.getCustomHive() == null)
            return true;
        return bee.hasCustomNectar() && bee.getCustomHive() != null;
    }

    @Override
    public void start() {
        BlockPos hive = bee.getCustomHive();
        bee.getNavigation().moveTo(hive.getX() + 0.5, hive.getY() + 1.0, hive.getZ() + 0.5, 0.5);

    }

    @Override
    public void tick() {
        if (bee.getCustomHive() == null || bee.tickCount >= 200) {
            bee.discard();
            return;
        }
        if (bee.getCustomHive() != null && bee.position().distanceTo(Vec3.atCenterOf(bee.getCustomHive())) < 1.5D) {
            bee.setHasCustomNectar(false);
            bee.discard();
        } else {
            if (bee.getCustomHive() != null)
                bee.pathfindRandomlyTowards(bee.getCustomHive().above());
        }
    }

    @Override
    public boolean canContinueToUse() {
        return bee.hasCustomNectar();
    }
}

class CollectCustomNectarGoal extends Goal {
    private final SmallBee bee;
    private BlockPos cropTarget;

    public CollectCustomNectarGoal(SmallBee bee) {
        this.bee = bee;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bee.crops == null) return true;
        if (bee.hasCustomNectar()) return false;
        if (bee.crops.isEmpty()) {
            bee.discard();
            return false;
        }
        cropTarget = bee.crops.get(bee.getLevel().random.nextInt(bee.crops.size())).pos;
        return true;
    }

    @Override
    public void start() {
        if (cropTarget != null)
            bee.pathfindRandomlyTowards(cropTarget);
    }

    @Override
    public void tick() {
        if (bee.crops == null) {
            bee.discard();
            return;
        }
        if (bee.getCustomHive() == null || bee.tickCount >= 200) {
            bee.discard();
            return;
        }
        if (cropTarget != null && bee.position().distanceTo(Vec3.atCenterOf(cropTarget)) < 1.5D) {
            if (!bee.getLevel().isClientSide()) {
                ((ServerLevel) bee.getLevel()).sendParticles(
                        new DustParticleOptions(new Vector3f(1.0f, 0.8f, 0.2f), 1.0f),
                        cropTarget.getX(), cropTarget.getY() + 0.3, cropTarget.getZ(),
                        10, 0.1, 0.1, 0.1, 0.01
                );
                bee.getLevel().levelEvent(1505, this.cropTarget, 0);
                ((BeeAccessor) this.bee).invokeSetFlag(8, true);
            }
            bee.setHasCustomNectar(true);
            cropTarget = null;
        } else {
            bee.pathfindRandomlyTowards(cropTarget.below());
        }
    }

    @Override
    public boolean canContinueToUse() {
        return cropTarget != null && !bee.hasCustomNectar();
    }
}
