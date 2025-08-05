package com.denfop.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataEntities {
    List<EntityType<?>> entityList;
    Map<ResourceLocation, EntityType<?>> mapList = new HashMap<>();

    public DataEntities() {
        entityList = new ArrayList<>(BuiltInRegistries.ENTITY_TYPE.stream().toList());
        entityList.forEach(entityType -> mapList.put(BuiltInRegistries.ENTITY_TYPE.getKey(entityType), entityType));
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

}
