package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.UUID;

public class PacketDeleteColony implements IPacket {

    public PacketDeleteColony() {
    }

    ;

    public PacketDeleteColony(Player player, IBody iBody) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
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
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid)) {
                String body = customPacketBuffer.readString();
                IBody body1 = SpaceNet.instance.getBodyFromName(body);
                SpaceNet.instance.getColonieNet().removeColony(body1, entityPlayer.getUUID());
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
