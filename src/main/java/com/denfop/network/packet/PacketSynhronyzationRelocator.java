package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketSynhronyzationRelocator implements IPacket {
    public PacketSynhronyzationRelocator() {

    }

    public PacketSynhronyzationRelocator(Player player, List<Point> list) {
        if (player instanceof ServerPlayer serverPlayer) {
            CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
            customPacketBuffer.writeByte(this.getId());
            customPacketBuffer.writeInt(list.size());
            list.forEach(point -> point.writeToBuffer(customPacketBuffer));
            IUCore.network.getServer().sendPacket(customPacketBuffer, serverPlayer);
        }
    }

    @Override
    public byte getId() {
        return 54;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        List<Point> points = new ArrayList<>();
        int size = customPacketBuffer.readInt();
        for (int i = 0; i < size; i++) {
            Point point = new Point(customPacketBuffer);
            points.add(point);
        }
        RelocatorNetwork.instance.addPoints(entityPlayer, points);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
