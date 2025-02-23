package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class PacketResearchSystem implements IPacket {

    public PacketResearchSystem() {

    }

    public PacketResearchSystem(BaseLevelSystem levelSystem) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, levelSystem, false);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 22;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
