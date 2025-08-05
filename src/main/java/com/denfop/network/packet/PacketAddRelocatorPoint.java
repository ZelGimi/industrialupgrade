package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.world.entity.player.Player;

public class PacketAddRelocatorPoint implements IPacket {
    private CustomPacketBuffer buffer;

    public PacketAddRelocatorPoint() {

    }

    public PacketAddRelocatorPoint(Player player, Point point) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(60, player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName().getString());
        point.writeToBuffer(buffer);
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this);
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
        return (byte) 146;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (!entityPlayer.getName().getString().equals(customPacketBuffer.readString())) {
            return;
        }
        RelocatorNetwork.instance.addPoint(entityPlayer, new Point(customPacketBuffer));
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
