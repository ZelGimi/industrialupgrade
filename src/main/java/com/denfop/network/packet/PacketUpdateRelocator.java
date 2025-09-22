package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class PacketUpdateRelocator implements IPacket{
    public PacketUpdateRelocator(){

    }

    public PacketUpdateRelocator(EntityPlayer player) {
        if (player instanceof EntityPlayerMP){
            EntityPlayerMP    serverPlayer = (EntityPlayerMP) player;
            CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
            Map<Integer, Map<UUID, List<Point>>> map = RelocatorNetwork.instance.getWorldDataPoints();
            Map<Integer, List<Point>> playerData = new HashMap<>();

            for (Map.Entry<Integer, Map<UUID, List<Point>>> entry : RelocatorNetwork.instance.getWorldDataPoints().entrySet()) {
                List<Point> points = entry.getValue().get(serverPlayer.getUniqueID());
                if (points != null) {
                    playerData.put(entry.getKey(), points);
                }
            }
            customPacketBuffer.writeByte(this.getId());
            customPacketBuffer.writeInt(playerData.size());
            for (Map.Entry<Integer, List<Point>> entry : playerData.entrySet()) {
                customPacketBuffer.writeInt(entry.getKey());

                List<Point> points = entry.getValue();
                customPacketBuffer.writeInt(points.size());
                for (Point point : points) {
                    point.writeToBuffer(customPacketBuffer);
                }
            }
            IUCore.network.getServer().sendPacket(customPacketBuffer,serverPlayer);
        }
    }

    @Override
    public byte getId() {
        return 55;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, EntityPlayer entityPlayer) {
        int worldsCount = customPacketBuffer.readInt();

        for (int i = 0; i < worldsCount; i++) {
           int levelKey = customPacketBuffer.readInt();

            int pointsCount = customPacketBuffer.readInt();
            List<Point> points = new ArrayList<>();

            for (int j = 0; j < pointsCount; j++) {
                points.add(new Point(customPacketBuffer));
            }
            RelocatorNetwork.instance.addPoints(entityPlayer,levelKey, points);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
