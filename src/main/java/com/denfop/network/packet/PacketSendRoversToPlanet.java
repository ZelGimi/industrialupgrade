package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;
import java.util.UUID;

public class PacketSendRoversToPlanet implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketSendRoversToPlanet() {

    }

    public PacketSendRoversToPlanet(IResearchTable base, Player player, IBody iBody) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());
        try {
            EncoderHandler.encode(customPacketBuffer, ((TileEntityBlock) base).getWorld());
            EncoderHandler.encode(customPacketBuffer, ((TileEntityBlock) base).getPos());
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            customPacketBuffer.writeBoolean(iBody != null);
            if (iBody != null) {
                customPacketBuffer.writeString(iBody.getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
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
        return 42;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level world = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid)) {
                boolean hasBody = customPacketBuffer.readBoolean();
                if (hasBody) {
                    String body = customPacketBuffer.readString();
                    IBody body1 = SpaceNet.instance.getBodyFromName(body);
                    BlockEntity tile = world.getBlockEntity(pos);
                    if (tile instanceof IResearchTable) {
                        IResearchTable tileEntityResearchTableSpace = (IResearchTable) tile;
                        if (tileEntityResearchTableSpace.getPlayer().equals(uuid)) {
                            IRocketLaunchPad rocketLaunchPad = SpaceNet.instance.getFakeSpaceSystem().getRocketPadMap().get(uuid);
                            if (rocketLaunchPad != null && rocketLaunchPad.getRover() != null) {
                                SpaceNet.instance.getResearchSystem().sendingOperation(new Rovers(
                                        rocketLaunchPad.getRover(),
                                        rocketLaunchPad.getRoverStack()
                                ), body1, tileEntityResearchTableSpace);
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
