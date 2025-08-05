package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import net.minecraft.world.entity.player.Player;

public class PacketRemoveRelocatorPoint implements IPacket {
    private CustomPacketBuffer buffer;

    public PacketRemoveRelocatorPoint() {

    }

    public PacketRemoveRelocatorPoint(Player player, Point point) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(60, player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName().getString());
        point.writeToBuffer(buffer);
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
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
        return (byte) 147;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (!entityPlayer.getName().getString().equals(customPacketBuffer.readString())) {
            return;
        }
        RelocatorNetwork.instance.removePoint(entityPlayer, new Point(customPacketBuffer));
        entityPlayer.closeContainer();
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
