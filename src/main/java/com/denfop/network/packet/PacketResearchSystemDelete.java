package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketResearchSystemDelete implements IPacket {

    public PacketResearchSystemDelete() {
    }

    public PacketResearchSystemDelete(EntityPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(this.getId());
        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) player);
    }

    @Override
    public byte getId() {
        return 24;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
