package com.denfop.items.relocator;


import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RelocatorNetwork {
    public static RelocatorNetwork instance;
    Map<ResourceKey<Level>, Map<UUID, List<Point>>> worldDataPoints = new ConcurrentHashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new RelocatorNetwork();
        }
    }

    public Map<ResourceKey<Level>, Map<UUID, List<Point>>> getWorldDataPoints() {
        return worldDataPoints;
    }

    public void addPoint(Player player, Point point) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.level().dimension(),
                k -> new ConcurrentHashMap<>()
        );
        final List<Point> list = map.computeIfAbsent(player.getUUID(), k -> new LinkedList<>());
        list.add(point);
    }

    public void removePoint(Player player, Point point) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.level().dimension(),
                k -> new HashMap<>()
        );
        final List<Point> list = map.computeIfAbsent(player.getUUID(), k -> new LinkedList<>());
        list.removeIf(point1 -> point1.getName().equals(point.getName()));
    }

    public void teleportPlayer(Player player, Point point) {
        if (!player.level().isClientSide()) {
            double x = point.getPos().getX() + 0.5;
            double y = point.getPos().getY();
            double z = point.getPos().getZ() + 0.5;


            player.teleportTo(x, y, z);


            player.fallDistance = 0.0F;
        }
    }

    public List<Point> getPoints(Player player) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.level().dimension(),
                k -> new HashMap<>()
        );
        return map.computeIfAbsent(player.getUUID(), k -> new LinkedList<>());
    }

    public void onUnload() {
        worldDataPoints.clear();
    }
}
