package com.denfop.items.relocator;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RelocatorNetwork {

    public static RelocatorNetwork instance;
    Map<Integer, Map<UUID, List<Point>>> worldDataPoints = new HashMap<>();

    public static void init() {
        if (instance == null) {
            instance = new RelocatorNetwork();
        }
    }

    public Map<Integer, Map<UUID, List<Point>>> getWorldDataPoints() {
        return worldDataPoints;
    }

    public void addPoint(EntityPlayer player, Point point) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.getEntityWorld().provider.getDimension(),
                k -> new HashMap<>()
        );
        final List<Point> list = map.computeIfAbsent(player.getPersistentID(), k -> new LinkedList<>());
        list.add(point);
    }

    public void removePoint(EntityPlayer player, Point point) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.getEntityWorld().provider.getDimension(),
                k -> new HashMap<>()
        );
        final List<Point> list = map.computeIfAbsent(player.getPersistentID(), k -> new LinkedList<>());
        list.removeIf(point1 -> point1.getName().equals(point.getName()));
    }

    public void teleportPlayer(EntityPlayer player, Point point) {
        if (!player.getEntityWorld().isRemote) {
            double x = point.getPos().getX() + 0.5;
            double y = point.getPos().getY();
            double z = point.getPos().getZ() + 0.5;


            player.setPositionAndUpdate(x, y, z);


            player.fallDistance = 0.0F;
        }
    }

    public List<Point> getPoints(EntityPlayer player) {
        final Map<UUID, List<Point>> map = worldDataPoints.computeIfAbsent(
                player.getEntityWorld().provider.getDimension(),
                k -> new HashMap<>()
        );
        return map.computeIfAbsent(player.getPersistentID(), k -> new LinkedList<>());
    }

    public void onUnload() {
        worldDataPoints.clear();
    }

}
