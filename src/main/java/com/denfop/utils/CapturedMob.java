//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.utils;


import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;

public final class CapturedMob {

    @Nonnull
    private static final String NBT_HEAL_F = "HealF";
    @Nonnull
    private static final String NBT_FLUID_NAME = "FluidName";
    @Nonnull
    private static final String NBT_COLOR = "Color";
    @Nonnull
    private static final String NBT_ATTRIBUTES = "Attributes";
    @Nonnull
    private static final String PRIVATE_FINAL_FIELD_CHANGED_ITS_VALUE = "private final field changed its value";
    @Nonnull
    private static final ResourceLocation PIG = new ResourceLocation("pig");
    @Nonnull
    private static final ResourceLocation DRAGON = new ResourceLocation("ender_dragon");
    @Nonnull
    private static final String ENTITY_KEY = "entity";
    @Nonnull
    private static final String ENTITY_ID_KEY = "entityId";
    @Nonnull
    private static final String ENTITY_TAG_KEY = "EntityTag";
    @Nonnull
    private static final String CUSTOM_NAME_KEY = "customName";
    private static boolean bossesBlacklisted = true;
    @Nullable
    private final NBTTagCompound entityNbt;
    @Nonnull
    private final ResourceLocation entityId;
    @Nullable
    private final String customName;
    private  int color;
    private CapturedMob(@Nonnull EntityLivingBase entity) {
        ResourceLocation id = EntityList.getKey(entity);
        this.entityId = id == null ? PIG : id;
        this.entityNbt = entity.serializeNBT();
        String name = null;
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving) entity;
            if (entLiv.hasCustomName()) {
                name = entLiv.getCustomNameTag();
            }
        }
        this.color = -1;
        if(entity instanceof EntitySheep){
            this.color=   ((EntitySheep)entity).getFleeceColor().getMetadata();
        }
        if (name != null && name.length() > 0) {
            this.customName = name;
        } else {
            this.customName = null;
        }

    }

    private CapturedMob(@Nonnull NBTTagCompound nbt) {
        if (nbt.hasKey("entity")) {
            this.entityNbt = nbt.getCompoundTag("entity").copy();
        } else if (nbt.hasKey("EntityTag")) {
            this.entityNbt = nbt.getCompoundTag("EntityTag").copy();
        } else {
            this.entityNbt = null;
        }

        String id = null;
        if (nbt.hasKey("entityId")) {
            id = nbt.getString("entityId");
        }

        this.entityId = id != null && !id.isEmpty() ? new ResourceLocation(id) : PIG;
        if (nbt.hasKey("customName")) {
            this.customName = nbt.getString("customName");
        } else {
            this.customName = null;
        }
        color = nbt.getInteger("color");

    }

    private CapturedMob(@Nonnull ResourceLocation entityId) {
        this.entityNbt = null;
        this.entityId = entityId;
        this.customName = null;
    }

    @Nullable
    public static CapturedMob create(@Nullable Entity entity) {
        return entity instanceof EntityLivingBase && entity.isEntityAlive() && !entity.world.isRemote && !(entity instanceof EntityPlayer) && !isBlacklisted(
                entity) ? new CapturedMob((EntityLivingBase) entity) : null;
    }

    @Nullable
    public static CapturedMob create(@Nullable ResourceLocation entityId) {
        return entityId != null && EntityList.isRegistered(entityId) && isRegisteredMob(entityId)
                ? new CapturedMob(entityId)
                : null;
    }

    public static boolean isRegisteredMob(ResourceLocation entityName) {
        if (entityName == null) {
            return false;
        } else {
            Class<? extends Entity> clazz = EntityList.getClass(entityName);
            return clazz != null && EntityLiving.class.isAssignableFrom(clazz);
        }
    }

    @Nonnull
    public ItemStack toStack(@Nonnull Item item, int meta, int amount) {
        ItemStack stack = new ItemStack(item, amount, meta);
        stack.setTagCompound(this.toNbt(null));


        return stack;
    }

    @Nonnull
    public ItemStack toStack(@Nonnull Block block, int meta, int amount) {
        ItemStack stack = new ItemStack(block, amount, meta);
        stack.setTagCompound(this.toNbt(null));
        return stack;
    }


    @Nonnull
    public NBTTagCompound toNbt(@Nullable NBTTagCompound nbt) {
        NBTTagCompound data = nbt != null ? nbt : new NBTTagCompound();
        data.setString("entityId", this.entityId.toString());
        if (this.entityNbt != null) {
            data.setTag("entity", this.entityNbt.copy());
        }

        if (this.customName != null) {
            data.setString("customName", this.customName);
        }
        data.setInteger("color", this.color);
        return data;
    }

    public static boolean containsSoul(@Nullable NBTTagCompound nbt) {
        return nbt != null && (nbt.hasKey("entity") || nbt.hasKey("entityId") || nbt.hasKey("EntityTag"));
    }

    public static boolean containsSoul(@Nonnull ItemStack stack) {
        return isValid(stack) && stack.hasTagCompound() && containsSoul(stack.getTagCompound());
    }

    public static boolean isValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty();
    }

    @Nullable
    public static CapturedMob create(@Nonnull ItemStack stack) {
        return containsSoul(stack) ? new CapturedMob(stack.getTagCompound()) : null;
    }

    @Nullable
    public static CapturedMob create(@Nullable NBTTagCompound nbt) {
        return nbt != null && containsSoul(nbt) ? new CapturedMob(nbt) : null;
    }

    public static boolean isBlacklisted(@Nonnull Entity entity) {
        ResourceLocation entityId = EntityList.getKey(entity);
        return entityId == null || isBlacklistedBoss(entityId, entity);
    }

    private static boolean isBlacklistedBoss(ResourceLocation entityId, Entity entity) {
        return bossesBlacklisted && !entity.isNonBoss() && !"minecraft".equals(entityId.getResourceDomain());
    }

    public boolean spawn(@Nullable World world, @Nullable BlockPos pos, @Nullable EnumFacing side, boolean clone) {
        return this.doSpawn(world, pos, side, clone) != null;
    }

    @Nullable
    public Entity doSpawn(@Nullable World world, @Nullable BlockPos pos, @Nullable EnumFacing side, boolean clone) {
        if (world != null && pos != null) {
            EnumFacing theSide = side != null ? side : EnumFacing.UP;
            Entity entity = this.getEntity(world, pos, null, clone);
            if (entity == null) {
                return null;
            } else {
                double spawnX = (double) (pos.getX() + theSide.getFrontOffsetX()) + 0.5D;
                double spawnY = pos.getY() + theSide.getFrontOffsetY();
                double spawnZ = (double) (pos.getZ() + theSide.getFrontOffsetZ()) + 0.5D;
                entity.setPositionAndRotation(
                        spawnX,
                        spawnY,
                        spawnZ,
                        MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F),
                        0.0F
                );
                if (entity instanceof EntityLiving) {
                    ((EntityLiving) entity).rotationYawHead = ((EntityLiving) entity).renderYawOffset = entity.rotationYaw;
                }

                AxisAlignedBB bb = entity.getEntityBoundingBox();
                switch (theSide) {
                    case UP:
                        spawnY = (double) (pos.getY() + 1) + 0.01D;
                        break;
                    case DOWN:
                        spawnY = (double) pos.getY() - (bb.maxY - bb.minY) - 0.01D;
                        break;
                    case EAST:
                        spawnX = (double) (pos.getX() + 1) + (bb.maxX - bb.minX) / 2.0D + 0.01D;
                        break;
                    case WEST:
                        spawnX = (double) pos.getX() - (bb.maxX - bb.minX) / 2.0D - 0.01D;
                        break;
                    case NORTH:
                        spawnZ = (double) pos.getZ() - (bb.maxZ - bb.minZ) / 2.0D - 0.01D;
                        break;
                    case SOUTH:
                        spawnZ = (double) (pos.getZ() + 1) + (bb.maxZ - bb.minZ) / 2.0D + 0.01D;
                }

                AxisAlignedBB blockBB;
                if (theSide != EnumFacing.DOWN) {
                    for (Iterator var14 = world.getCollisionBoxes(
                            null,
                            new AxisAlignedBB((new BlockPos(spawnX, spawnY, spawnZ)).down())
                    ).iterator(); var14.hasNext(); spawnY = Math.max(blockBB.maxY + 0.01D, spawnY)) {
                        blockBB = (AxisAlignedBB) var14.next();
                    }
                }

                entity.setLocationAndAngles(spawnX, spawnY, spawnZ, entity.rotationYaw, entity.rotationPitch);
                if (world.checkNoEntityCollision(entity.getEntityBoundingBox()) && world.getCollisionBoxes(
                        entity,
                        entity.getEntityBoundingBox()
                ).isEmpty()) {
                    if (this.customName != null && entity instanceof EntityLiving) {
                        entity.setCustomNameTag(this.customName);
                    }

                    if (!world.spawnEntity(entity)) {
                        entity.setUniqueId(MathHelper.getRandomUUID(world.rand));
                        if (!world.spawnEntity(entity)) {
                            return null;
                        }
                    }

                    if (entity instanceof EntityLiving) {
                        ((EntityLiving) entity).playLivingSound();
                    }

                    return entity;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    @Nullable
    public Class<? extends Entity> getEntityClass() {
        return EntityList.getClass(this.entityId);
    }

    @Nullable
    public Entity getEntity(@Nullable World world, boolean clone) {
        return this.getEntity(world, null, null, clone);
    }

    @Nullable
    public Entity getEntity(
            @Nullable World world,
            @Nullable BlockPos pos,
            @Nullable DifficultyInstance difficulty,
            boolean clone
    ) {
        if (world == null) {
            return null;
        } else {
            NBTTagCompound entityNbt_nullchecked = this.entityNbt;
            Entity entity;
            if (entityNbt_nullchecked == null || !clone) {
                entity = EntityList.createEntityByIDFromName(this.entityId, world);
                if (entity == null) {
                    return null;
                } else {
                    if (pos != null) {
                        entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    }

                    if (entity instanceof EntityLiving) {
                        if (pos != null && difficulty == null) {
                            difficulty = world.getDifficultyForLocation(pos);
                        }

                        if (difficulty != null && (pos == null || !ForgeEventFactory.doSpecialSpawn(
                                (EntityLiving) entity,
                                world,
                                (float) pos.getX(),
                                (float) pos.getY(),
                                (float) pos.getZ(),
                                null
                        ))) {
                            ((EntityLiving) entity).onInitialSpawn(difficulty, null);
                        }
                    }
                    if(entity instanceof EntitySheep){
                        ((EntitySheep)entity).setFleeceColor(EnumDyeColor.byMetadata(color));
                    }

                    return entity;
                }
            } else {
                entity = EntityList.createEntityFromNBT(entityNbt_nullchecked, world);
                if (!clone && entity != null) {
                    entity.setUniqueId(MathHelper.getRandomUUID(world.rand));
                }
                if(entity instanceof EntitySheep){
                    ((EntitySheep)entity).setFleeceColor(EnumDyeColor.byMetadata(color));
                }
                return entity;
            }
        }
    }


    public String getTranslationName() {
        return EntityList.getTranslationName(this.entityId);
    }


    @Nonnull
    public ResourceLocation getEntityName() {
        return this.entityId;
    }

    public boolean isSameType(Entity entity) {
        return entity != null && this.getEntityName().equals(EntityList.getKey(entity));
    }

    public boolean isSameType(CapturedMob other) {
        return other != null && this.getEntityName().equals(other.getEntityName());
    }


    public static void setBossesBlacklisted(boolean b) {
        bossesBlacklisted = b;
    }

}
