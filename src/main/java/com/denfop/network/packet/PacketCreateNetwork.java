package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blockentity.storage.BlockEntityController;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.List;

public class PacketCreateNetwork implements IPacket {

    public PacketCreateNetwork() {

    }

    public PacketCreateNetwork(BlockPos pos, Level level, List<BlockPos> blockPoses) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
            EncoderHandler.encode(customPacketBuffer, blockPoses);
        } catch (IOException ignored) {
        }
        IUCore.network.getServer().sendPacket(customPacketBuffer);
    }

    @Override
    public byte getId() {
        return 111;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            List<BlockPos> blockPoses = (List<BlockPos>) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getLevel().dimension() == level.dimension())
                if (entityPlayer.getLevel().getBlockEntity(pos) instanceof BlockEntityController controller) {
                    controller.networkSystem.reBuild(entityPlayer.getLevel(), blockPoses);
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
