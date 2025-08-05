package com.denfop.utils;


import com.denfop.datacomponent.DataComponentsInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class CapturedMobUtils {
    public static final Codec<CapturedMobUtils> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("entityId").forGetter(CapturedMobUtils::getEntityId),
                    Codec.STRING.fieldOf("customName").forGetter(CapturedMobUtils::getCustomName),
                    Codec.DOUBLE.fieldOf("coefficient").forGetter(CapturedMobUtils::getCoefficient),
                    Codec.INT.fieldOf("color").forGetter(CapturedMobUtils::getColor),
                    Codec.STRING.fieldOf("resource").forGetter(CapturedMobUtils::getResource)
            ).apply(instance, CapturedMobUtils::new
            )
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CapturedMobUtils> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> {
                buf.writeResourceLocation(value.getEntityId());
                buf.writeBoolean(value.getCustomName() != null);
                if (value.getCustomName() != null) {
                    buf.writeUtf(value.getCustomName());
                }
                buf.writeDouble(value.getCoefficient());
                buf.writeInt(value.getColor());
                buf.writeUtf(value.getResource());
            },
            buf -> {
                ResourceLocation id = buf.readResourceLocation();
                String name = buf.readBoolean() ? buf.readUtf() : null;
                double coeff = buf.readDouble();
                int color = buf.readInt();
                String resource = buf.readUtf();
                return new CapturedMobUtils(id, name, coeff, color, resource);
            }
    );
    public static CapturedMobUtils EMPTY = new CapturedMobUtils(ResourceLocation.tryParse("pig"), "", 0, 0, "");
    public static DataEntities data = new DataEntities();
    private final ResourceLocation entityId;
    @Nullable
    private final String customName;
    private final double coefficient;
    private final String resource;
    private final int color;

    public CapturedMobUtils(ResourceLocation entityId, @Nullable String customName, double coefficient, int color, String resource) {
        this.entityId = entityId;
        this.customName = customName;
        this.coefficient = coefficient;
        this.color = color;

        this.resource = resource;
    }

    private CapturedMobUtils(@Nonnull LivingEntity entity) {
        this.entityId = EntityType.getKey(entity.getType());
        String name = null;
        if (entity instanceof LivingEntity) {
            if (entity.hasCustomName()) {
                name = entity.getScoreboardName();
            }
        }

        if (entity instanceof Sheep) {
            this.color = ((Sheep) entity).getColor().getId();
        } else {
            this.color = -1;
        }
        if (name != null && !name.isEmpty()) {
            this.customName = name;
        } else {
            this.customName = "";
        }
        this.coefficient = entity.getMaxHealth() / 40;
        StringBuilder builder = new StringBuilder(this.entityId.toString());
        if (color != -1) {
            builder.append("_").append(((Sheep) entity).getColor().getName());
        }
        this.resource = builder.toString();
    }

    public static CapturedMobUtils create(@Nullable Entity entity) {
        return entity instanceof LivingEntity && entity.isAlive() && !entity.level().isClientSide() && !(entity instanceof Player) && !isBlacklisted(
                entity) ? new CapturedMobUtils((LivingEntity) entity) : CapturedMobUtils.EMPTY;
    }

    public static boolean containsSoul(@Nonnull ItemStack stack) {
        return isValid(stack) && stack.has(DataComponentsInit.MOB);
    }

    public static boolean isValid(@Nonnull ItemStack stack) {
        return !stack.isEmpty();
    }

    @Nullable
    public static CapturedMobUtils create(@Nonnull ItemStack stack, RegistryAccess registryAccess) {
        if (containsSoul(stack)) {
            return stack.getOrDefault(DataComponentsInit.MOB, EMPTY);
        } else {
            return stack.getOrDefault(DataComponentsInit.MOB, EMPTY);
        }
    }

    public static boolean isBlacklisted(@Nonnull Entity entity) {
        ResourceLocation entityId = EntityType.getKey(entity.getType());
        if (entity.getType().is(Tags.EntityTypes.BOSSES))
            return true;
        return entityId == null;
    }

    public ResourceLocation getEntityId() {
        return entityId;
    }

    public @Nullable String getCustomName() {
        return customName;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CapturedMobUtils that = (CapturedMobUtils) o;
        return Double.compare(coefficient, that.coefficient) == 0 && color == that.color && Objects.equals(entityId, that.entityId) && Objects.equals(customName, that.customName) && Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, customName, coefficient, resource, color);
    }

    public double getCoefficient() {
        return coefficient;
    }


    @Nonnull
    public CompoundTag toNbt(@Nullable CompoundTag nbt) {
        CompoundTag data = nbt != null ? nbt : new CompoundTag();
        data.putString("entityId", this.entityId.toString());
        if (this.customName != null) {
            data.putString("customName", this.customName);
        }
        data.putInt("color", this.color);
        data.putDouble("coefficient", this.coefficient);
        return data;
    }

    @Nullable
    public Entity getEntity(@Nullable Level world, boolean clone) {
        return this.getEntity(world, null, clone);
    }

    @Nullable
    public Entity getEntity(
            @Nullable Level world,
            @Nullable BlockPos pos,
            boolean clone
    ) {
        if (world == null) {
            return null;
        } else {
            Entity entity;

            entity = data.createEntityByIDFromName(this.entityId, world);
            if (entity == null) {
                return null;
            } else {
                if (pos != null) {
                    entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                }
                if (entity instanceof Sheep) {
                    ((Sheep) entity).setColor(DyeColor.byId(color));
                }

                return entity;
            }

        }
    }

    public String getResource() {
        return resource;
    }
}
