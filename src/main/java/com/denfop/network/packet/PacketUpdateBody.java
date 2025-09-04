package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.BlockEntityResearchTableSpace;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateBody implements IPacket {

    public PacketUpdateBody() {

    }

    public PacketUpdateBody(BlockEntityResearchTableSpace tile, IBody body) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, ((BlockEntityBase) tile).getWorld());
            EncoderHandler.encode(customPacketBuffer, ((BlockEntityBase) tile).getPos());
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
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level world = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BlockEntityResearchTableSpace) {
                BlockEntityResearchTableSpace tileEntityResearchTableSpace = (BlockEntityResearchTableSpace) tile;
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
