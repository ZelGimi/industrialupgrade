package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.UUID;

public class PacketAddBuildingToColony implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketAddBuildingToColony() {
    }

    public PacketAddBuildingToColony(IColony colony, Player player) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        if (colony == null)
            return;
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, colony.getFakePlayer());
            customPacketBuffer.writeString(colony.getBody().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 47;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        UUID uuid;
        try {
            uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (entityPlayer.getUUID().equals(uuid)) {
            String body = customPacketBuffer.readString();
            IBody body1 = SpaceNet.instance.getBodyFromName(body);
            SpaceNet.instance.getColonieNet().addItemToColony(body1, entityPlayer);

        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
