package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketDeleteColony   implements IPacket{
    public PacketDeleteColony(){};
    public PacketDeleteColony(EntityPlayer player, IBody iBody){
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, player.getUniqueID());
            customPacketBuffer.writeString(iBody.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }
    @Override
    public byte getId() {
        return 51;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUniqueID().equals(uuid)){
                String body = customPacketBuffer.readString();
                IBody body1 = SpaceNet.instance.getBodyFromName(body);
                SpaceNet.instance.getColonieNet().removeColony(body1,entityPlayer.getUniqueID());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}
