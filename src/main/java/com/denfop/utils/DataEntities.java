package com.denfop.utils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.denfop.IUCore.LOGGER;

public class DataEntities {
    List<EntityType<?>> entityList;
    Map<ResourceLocation, EntityType<?>> mapList = new HashMap<>();

    public DataEntities() {
        entityList = new ArrayList<>(ForgeRegistries.ENTITY_TYPES.getValues());
        entityList.forEach(entityType -> mapList.put(ForgeRegistries.ENTITY_TYPES.getKey(entityType), entityType));
    }

    public boolean contains(ResourceLocation location) {
        return mapList.containsKey(location);
    }

    public EntityType<?> getTypeFromResourceLocation(ResourceLocation location) {
        return this.mapList.get(location);
    }

    public Entity createEntityByIDFromName(ResourceLocation entityId, Level world) {
        EntityType<?> entityType = mapList.get(entityId);
        return entityType == null ? null : entityType.create(world);
    }

    public Entity createEntityFromNBT(CompoundTag nbt, Level world) {
        ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("id"));
        Entity entity = createEntityByIDFromName(resourcelocation, world);
        if (entity == null) {
            LOGGER.warn("Skipping Entity with id {}", resourcelocation);
        } else {
            try {
                entity.load(nbt);
            } catch (Exception e) {
                LOGGER.error("An Entity {}({}) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        nbt.getString("id"), entity.getName(), e);
                entity = null;
            }
        }

        return entity;
    }
}
