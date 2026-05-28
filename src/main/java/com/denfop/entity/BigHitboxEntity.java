package com.denfop.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class BigHitboxEntity extends Entity {
    AABB aabb;
    private AABB box;

    public BigHitboxEntity(EntityType<? extends BigHitboxEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;


    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    @Override
    public void tick() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.aabb == null ? super.getBoundingBoxForCulling() : this.getBox();
    }

    private AABB getBox() {
        if (box == null) {
            AABB one = getBoundingBoxForCulling();
            this.box = new AABB(one.minX + aabb.minX, one.minY + aabb.minY, one.minZ + aabb.minZ,
                    one.maxX + aabb.maxX, one.maxY + aabb.maxY, one.maxZ + aabb.maxZ);
        }
        return box;
    }


}
