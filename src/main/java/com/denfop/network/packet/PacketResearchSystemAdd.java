package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.research.main.EnumLeveling;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;

public class PacketResearchSystemAdd implements IPacket {

    public PacketResearchSystemAdd() {
    }

    public PacketResearchSystemAdd(EnumLeveling level, int add, EntityPlayer name) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, level.ordinal(), false);
            EncoderHandler.encode(buffer, add, false);

        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        IUCore.network.getServer().sendPacket(buffer, (EntityPlayerMP) name);
    }

    @Override
    public byte getId() {
        return 23;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
