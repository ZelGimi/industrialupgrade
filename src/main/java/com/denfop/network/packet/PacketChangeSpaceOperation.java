package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PacketChangeSpaceOperation implements IPacket{
    public PacketChangeSpaceOperation(){};
    public PacketChangeSpaceOperation( EntityPlayer player, IBody iBody){
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
    public byte getId() {
        return 44;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUniqueID().equals(uuid)){
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody){
                    String body = customPacketBuffer.readString();
                    IBody body1 = SpaceNet.instance.getBodyFromName(body);
                    final List<IFakeBody> fakeBodies = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(uuid);
                    for (IFakeBody fakeBody : fakeBodies){
                        if (fakeBody.matched(body1)){
                            fakeBody.resetAuto();
                            break;
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
