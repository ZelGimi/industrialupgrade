package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.blockentity.storage.BlockEntityFluidMonitor;
import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.UUID;

public class PacketCanAddCraft implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketCanAddCraft() {

    }


    public PacketCanAddCraft(Player player, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, pos);
        } catch (IOException ignored) {
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
        return 118;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid))
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    boolean canAdd = controller.network.hasAvailableProcessors();
                    new PacketCanAddCraftClient(entityPlayer, canAdd);
                }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                boolean canAdd = controller.network.hasAvailableProcessors();
                new PacketCanAddCraftClient(entityPlayer, canAdd);
            }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityFluidMonitor controller && controller.network != null) {
                boolean canAdd = controller.network.hasAvailableProcessors();
                new PacketCanAddCraftClient(entityPlayer, canAdd);
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

