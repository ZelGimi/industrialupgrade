package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

public class PacketUpdateRelocator implements IPacket {
    private CustomPacketBuffer buffer;

    public PacketUpdateRelocator() {

    }

    public PacketUpdateRelocator(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
            Map<ResourceKey<Level>, Map<UUID, List<Point>>> map = RelocatorNetwork.instance.getWorldDataPoints();
            Map<ResourceKey<Level>, List<Point>> playerData = new HashMap<>();

            for (Map.Entry<ResourceKey<Level>, Map<UUID, List<Point>>> entry : RelocatorNetwork.instance.getWorldDataPoints().entrySet()) {
                List<Point> points = entry.getValue().get(serverPlayer.getUUID());
                if (points != null) {
                    playerData.put(entry.getKey(), points);
                }
            }
            customPacketBuffer.writeByte(this.getId());
            customPacketBuffer.writeInt(playerData.size());
            for (Map.Entry<ResourceKey<Level>, List<Point>> entry : playerData.entrySet()) {
                customPacketBuffer.writeResourceKey(entry.getKey());

                List<Point> points = entry.getValue();
                customPacketBuffer.writeInt(points.size());
                for (Point point : points) {
                    point.writeToBuffer(customPacketBuffer);
                }
            }
            this.buffer = customPacketBuffer;
            IUCore.network.getServer().sendPacket(this, customPacketBuffer, serverPlayer);
        }
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 55;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        int worldsCount = customPacketBuffer.readInt();

        for (int i = 0; i < worldsCount; i++) {
            ResourceKey<Level> levelKey = customPacketBuffer.readResourceKey(Registries.DIMENSION);

            int pointsCount = customPacketBuffer.readInt();
            List<Point> points = new ArrayList<>();

            for (int j = 0; j < pointsCount; j++) {
                points.add(new Point(customPacketBuffer));
            }
            RelocatorNetwork.instance.addPoints(entityPlayer, levelKey, points);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
