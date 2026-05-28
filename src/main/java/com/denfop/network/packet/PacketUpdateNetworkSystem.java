package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.api.storage.ElectricStorage;
import com.denfop.blockentity.storage.BlockEntityController;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.WorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.List;

public class PacketUpdateNetworkSystem implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateNetworkSystem() {

    }


    public PacketUpdateNetworkSystem(Level level, List<BlockPos> poses, BlockPos pos, boolean remove) {
        if (level == null || poses == null || pos == null || level.isClientSide() || WorldData.isStoppingOrUnloading(level) || level.getServer() == null || !level.getServer().isRunning()) {
            return;
        }
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(level.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, level);
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, poses);
            customPacketBuffer.writeBoolean(remove);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer);
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
        return 126;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            List<BlockPos> poses = (List<BlockPos>) DecoderHandler.decode(customPacketBuffer);
            boolean remove = customPacketBuffer.readBoolean();
            if (!remove) {
                if (level.getBlockEntity(pos) instanceof BlockEntityController controller) {
                    for (BlockPos blockPos : poses) {
                        if (level.getBlockEntity(blockPos) instanceof ElectricStorage electricStorage) {
                            electricStorage.setStorageNetwork(controller.networkSystem);
                        }
                    }
                }
            } else {
                for (BlockPos blockPos : poses) {
                    if (level.getBlockEntity(blockPos) instanceof ElectricStorage electricStorage) {
                        electricStorage.setStorageNetwork(null);
                    }
                }
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

