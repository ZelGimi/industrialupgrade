package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.mechanism.TileEntityResearchTableSpace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketUpdateBody implements IPacket {

    public PacketUpdateBody() {

    }

    public PacketUpdateBody(TileEntityResearchTableSpace tile, IBody body) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, tile.getWorld());
            EncoderHandler.encode(customPacketBuffer, tile.getPos());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        customPacketBuffer.writeBoolean(body != null);
        if (body != null) {
            customPacketBuffer.writeString(body.getName());
        }
        tile.body = body;
        IUCore.network.getClient().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 40;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        try {
            World world = (World) DecoderHandler.decode(customPacketBuffer);
            BlockPos blockPos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            TileEntity tile = world.getTileEntity(blockPos);
            if (tile instanceof TileEntityResearchTableSpace) {
                TileEntityResearchTableSpace tileEntityResearchTableSpace = (TileEntityResearchTableSpace) tile;
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody) {
                    tileEntityResearchTableSpace.body = SpaceNet.instance.getBodyFromName(customPacketBuffer.readString());
                    if (tileEntityResearchTableSpace.fakeBody != null && !tileEntityResearchTableSpace.fakeBody.matched(
                            tileEntityResearchTableSpace.body)) {
                        tileEntityResearchTableSpace.fakeBody = null;
                    }

                } else {
                    tileEntityResearchTableSpace.fakeBody = null;
                    tileEntityResearchTableSpace.body = null;
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
