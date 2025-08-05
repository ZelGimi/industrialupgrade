package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketChangeSpaceOperation implements IPacket {

    private CustomPacketBuffer buffer;

    ;

    public PacketChangeSpaceOperation() {
    }

    public PacketChangeSpaceOperation(Player player, IBody iBody) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            customPacketBuffer.writeBoolean(iBody != null);
            if (iBody != null) {
                customPacketBuffer.writeString(iBody.getName());
            }
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
        return 44;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid)) {
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody) {
                    String body = customPacketBuffer.readString();
                    IBody body1 = SpaceNet.instance.getBodyFromName(body);
                    final List<IFakeBody> fakeBodies = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(uuid);
                    if (fakeBodies != null) {
                        for (IFakeBody fakeBody : fakeBodies) {
                            if (fakeBody.matched(body1)) {
                                fakeBody.resetAuto();
                                break;
                            }
                        }
                    }
                }
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
