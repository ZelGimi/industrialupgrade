package com.denfop.network.packet;


import net.minecraft.world.entity.player.Player;

public interface IPacket {

    byte getId();

    void readPacket(CustomPacketBuffer customPacketBuffer, final Player entityPlayer);

    EnumTypePacket getPacketType();


}
