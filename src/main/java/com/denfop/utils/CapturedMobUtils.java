package com.denfop.utils;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.SpawnData;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CapturedMobUtils {

    @Nonnull
    private static final ResourceLocation PIG = new ResourceLocation("pig");

    public static DataEntities data = new DataEntities();
    @Nullable
    private final CompoundTag entityNbt;
    @Nonnull
    private final ResourceLocation entityId;
    @Nullable
    private final String customName;
    private final String resource;
    private final double coefficient;
    private int color;

    private CapturedMobUtils(@Nonnull LivingEntity entity) {
        this.entityId = EntityType.getKey(entity.getType());
        this.entityNbt = entity.serializeNBT();
        String name = null;
        if (entity instanceof LivingEntity) {
            LivingEntity entLiv = entity;
            if (entLiv.hasCustomName()) {
                name = entLiv.getScoreboardName();
            }
        }
        this.color = -1;
        StringBuilder builder = new StringBuilder(this.entityId.toString());

        if (entity instanceof Sheep) {
            this.color = ((Sheep) entity).getColor().getId();
            builder.append("_").append(((Sheep) entity).getColor().getName());

        }
        if (name != null && name.length() > 0) {
            this.customName = name;
        } else {
            this.customName = null;
        }
        this.resource = builder.toString();
        this.coefficient = entity.getMaxHealth() / 40;

    }

    private CapturedMobUtils(@Nonnull CompoundTag nbt) {
        if (nbt.contains("entity")) {
            this.entityNbt = nbt.getCompound("entity").copy();
        } else if (nbt.contains("EntityTag")) {
            this.entityNbt = nbt.getCompound("EntityTag").copy();
        } else {
            this.entityNbt = null;
        }

        String id;

        id = nbt.getString("entityId");
        coefficient = nbt.getDouble("coefficient");

        this.entityId = !id.isEmpty() ? new ResourceLocation(id) : PIG;
        StringBuilder builder = new StringBuilder(this.entityId.toString());
        this.customName = nbt.getString("customName");
        color = nbt.getInt("color");
        if (color >= 0) {
            builder.append("_").append(DyeColor.byId(color));
        }
        this.resource = builder.toString();
    }

    private CapturedMobUtils(@Nonnull ResourceLocation entityId) {
        this.entityNbt = null;
        this.entityId = entityId;
        this.customName = null;
        this.resource = this.entityId.toString();
        coefficient = 1;
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable Entity entity) {
        return entity instanceof LivingEntity && entity.isAlive() && !entity.level().isClientSide() && !(entity instanceof Player) && !isBlacklisted(
                entity) ? new CapturedMobUtils((LivingEntity) entity) : null;
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable ResourceLocation entityId) {
        return entityId != null && data.contains(entityId) && isRegisteredMob(entityId)
                ? new CapturedMobUtils(entityId)
                : null;
    }

    public static boolean isRegisteredMob(ResourceLocation entityName) {
        if (entityName == null) {
            return false;
        } else {
            Class<? extends Entity> clazz = data.getTypeFromResourceLocation(entityName).getBaseClass();
            return LivingEntity.class.isAssignableFrom(clazz);
        }
    }

    public static boolean containsSoul(@Nullable CompoundTag nbt) {
        return nbt != null && (nbt.contains("entity") || nbt.contains("entityId") || nbt.contains("EntityTag"));
    }

    public static boolean containsSoul(@Nonnull ItemStack stack) {
        return isValid(stack) && stack.hasTag() && containsSoul(stack.getTag());
    }

    public static boolean isValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty();
    }

    @Nullable
    public static CapturedMobUtils create(@Nonnull ItemStack stack) {
        if (containsSoul(stack)) {
            assert stack.getTag() != null;
            return new CapturedMobUtils(stack.getTag());
        } else {
            return null;
        }
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable CompoundTag nbt) {
        return containsSoul(nbt) ? new CapturedMobUtils(nbt) : null;
    }

    public static boolean isBlacklisted(@Nonnull Entity entity) {
        ResourceLocation entityId = EntityType.getKey(entity.getType());
        if (entity.getType().is(Tags.EntityTypes.BOSSES))
            return true;
        return entityId == null;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public String getResource() {
        return resource;
    }

    @Nonnull
    public ItemStack toStack(@Nonnull Item item, int amount) {
        ItemStack stack = new ItemStack(item, amount);
        stack.setTag(this.toNbt(null));


        return stack;
    }

    @Nonnull
    public CompoundTag toNbt(@Nullable CompoundTag nbt) {
        CompoundTag data = nbt != null ? nbt : new CompoundTag();
        data.putString("entityId", this.entityId.toString());
        if (this.entityNbt != null) {
            data.put("entity", this.entityNbt.copy());
        }

        if (this.customName != null) {
            data.putString("customName", this.customName);
        }
        data.putInt("color", this.color);
        data.putDouble("coefficient", this.coefficient);
        return data;
    }

    @Nullable
    public Entity getEntity(@Nullable Level world, boolean clone) {
        return this.getEntity(world, null, null, clone);
    }

    @Nullable
    public Entity getEntity(
            @Nullable Level world,
            @Nullable BlockPos pos,
            @Nullable DifficultyInstance difficulty,
            boolean clone
    ) {
        if (world == null) {
            return null;
        } else {
            CompoundTag entityNbt_nullchecked = this.entityNbt;
            Entity entity;
            if (entityNbt_nullchecked == null || !clone) {
                entity = data.createEntityByIDFromName(this.entityId, world);
                if (entity == null) {
                    return null;
                } else {
                    if (pos != null) {
                        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                    }

                    if (entity instanceof Mob) {
                        if (pos != null && difficulty == null) {
                            difficulty = world.getCurrentDifficultyAt(pos);
                        }
                        SpawnData spawndata =new SpawnData();

                        if (difficulty != null && (pos == null || !ForgeEventFactory.checkSpawnPositionSpawner(
                                (Mob) entity,
                                (ServerLevelAccessor) world,MobSpawnType.NATURAL,
                                spawndata,
                                null
                        ))) {
                            ((Mob) entity).finalizeSpawn((ServerLevelAccessor) world, difficulty, MobSpawnType.NATURAL, null, null);
                        }
                    }
                    if (entity instanceof Sheep) {
                        ((Sheep) entity).setColor(DyeColor.byId(color));
                    }

                    return entity;
                }
            } else {
                entity = data.createEntityFromNBT(entityNbt_nullchecked, world);
                if (entity instanceof Sheep) {
                    ((Sheep) entity).setColor(DyeColor.byId(color));
                }
                return entity;
            }
        }
    }

}
