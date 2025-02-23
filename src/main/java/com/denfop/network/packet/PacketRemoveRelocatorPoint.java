package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketRemoveRelocatorPoint implements IPacket{
    public PacketRemoveRelocatorPoint(){

    }
    public PacketRemoveRelocatorPoint(EntityPlayer player, Point point){
        CustomPacketBuffer buffer = new CustomPacketBuffer(60);
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName());
        point.writeToBuffer(buffer);
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return (byte) 147;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        if (!entityPlayer.getName().equals(customPacketBuffer.readString())) {
            return;
        }
        RelocatorNetwork.instance.removePoint(entityPlayer,new Point(customPacketBuffer));
        entityPlayer.closeScreen();
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
