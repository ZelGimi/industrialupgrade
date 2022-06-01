package com.denfop.utils;


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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class CapturedMobUtils {

    @Nonnull
    private static final ResourceLocation PIG = new ResourceLocation("pig");
    @Nullable
    private final NBTTagCompound entityNbt;
    @Nonnull
    private final ResourceLocation entityId;
    @Nullable
    private final String customName;
    private int color;

    private CapturedMobUtils(@Nonnull EntityLivingBase entity) {
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
        if (entity instanceof EntitySheep) {
            this.color = ((EntitySheep) entity).getFleeceColor().getMetadata();
        }
        if (name != null && name.length() > 0) {
            this.customName = name;
        } else {
            this.customName = null;
        }

    }

    private CapturedMobUtils(@Nonnull NBTTagCompound nbt) {
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

    private CapturedMobUtils(@Nonnull ResourceLocation entityId) {
        this.entityNbt = null;
        this.entityId = entityId;
        this.customName = null;
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable Entity entity) {
        return entity instanceof EntityLivingBase && entity.isEntityAlive() && !entity.world.isRemote && !(entity instanceof EntityPlayer) && !isBlacklisted(
                entity) ? new CapturedMobUtils((EntityLivingBase) entity) : null;
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable ResourceLocation entityId) {
        return entityId != null && EntityList.isRegistered(entityId) && isRegisteredMob(entityId)
                ? new CapturedMobUtils(entityId)
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
    public static CapturedMobUtils create(@Nonnull ItemStack stack) {
        if (containsSoul(stack)) {
            assert stack.getTagCompound() != null;
            return new CapturedMobUtils(stack.getTagCompound());
        } else {
            return null;
        }
    }

    @Nullable
    public static CapturedMobUtils create(@Nullable NBTTagCompound nbt) {
        return containsSoul(nbt) ? new CapturedMobUtils(nbt) : null;
    }

    public static boolean isBlacklisted(@Nonnull Entity entity) {
        ResourceLocation entityId = EntityList.getKey(entity);
        return entityId == null;
    }


    @Nonnull
    public ItemStack toStack(@Nonnull Item item, int meta, int amount) {
        ItemStack stack = new ItemStack(item, amount, meta);
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
                    if (entity instanceof EntitySheep) {
                        ((EntitySheep) entity).setFleeceColor(EnumDyeColor.byMetadata(color));
                    }

                    return entity;
                }
            } else {
                entity = EntityList.createEntityFromNBT(entityNbt_nullchecked, world);
                if (entity instanceof EntitySheep) {
                    ((EntitySheep) entity).setFleeceColor(EnumDyeColor.byMetadata(color));
                }
                return entity;
            }
        }
    }

}
