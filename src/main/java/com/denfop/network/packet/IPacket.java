package com.denfop.network.packet;


import com.denfop.network.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public interface IPacket extends CustomPacketPayload {


    @Override
    default Type<IPacket> type() {
        return NetworkManager.packetTypeMap.get(getId());
    }

    byte getId();

    void readPacket(CustomPacketBuffer customPacketBuffer, final Player entityPlayer);

    CustomPacketBuffer getPacketBuffer();

    void setPacketBuffer(CustomPacketBuffer customPacketBuffer);

    EnumTypePacket getPacketType();


}
