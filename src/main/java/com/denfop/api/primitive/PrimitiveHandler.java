package com.denfop.api.primitive;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PrimitiveHandler {

    static Map<EnumPrimitive, Map<UUID, Double>> mapPrimitives = new ConcurrentHashMap<>();

    public static Map<UUID, Double> getPlayersData(EnumPrimitive enumPrimitive) {
        return mapPrimitives.computeIfAbsent(enumPrimitive, k -> new HashMap<>());
    }

    public static void addExperience(EnumPrimitive primitive, double experience, UUID player) {
        Double exp = mapPrimitives.computeIfAbsent(primitive, k -> new HashMap<>()).putIfAbsent(player, 0.0);
        if (exp == null)
            exp = 0.0;
        exp += experience;
        exp = Math.min(exp, 100);
        mapPrimitives.get(primitive).replace(player, exp);
    }

    public static Map<EnumPrimitive, Map<UUID, Double>> getMapPrimitives() {
        return mapPrimitives;
    }

}
