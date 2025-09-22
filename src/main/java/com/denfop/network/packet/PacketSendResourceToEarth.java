package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;
import java.util.UUID;

public class PacketSendResourceToEarth implements IPacket {

    public PacketSendResourceToEarth() {
    }

    ;

    public PacketSendResourceToEarth(EntityPlayer player, IBody iBody) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, player.getUniqueID());
            customPacketBuffer.writeBoolean(iBody != null);
            if (iBody != null) {
                customPacketBuffer.writeString(iBody.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUniqueID().equals(uuid)) {
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody) {
                    String body = customPacketBuffer.readString();
                    IBody body1 = SpaceNet.instance.getBodyFromName(body);
                    SpaceNet.instance.getColonieNet().sendResourceToPlanet(entityPlayer.getUniqueID(), body1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getId() {
        return 46;
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }


}

