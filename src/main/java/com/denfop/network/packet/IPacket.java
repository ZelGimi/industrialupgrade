package com.denfop.network.packet;

import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {

    byte getId();

    void readPacket(CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer);

    EnumTypePacket getPacketType();


}
