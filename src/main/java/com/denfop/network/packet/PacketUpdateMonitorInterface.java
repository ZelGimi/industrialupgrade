package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.containermenu.ContainerMonitor;
import com.denfop.containermenu.ContainerPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.UUID;

public class PacketUpdateMonitorInterface implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateMonitorInterface() {

    }


    public PacketUpdateMonitorInterface(Player player, boolean type) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            customPacketBuffer.writeBoolean(type);
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
        return 123;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getGameProfile().getId().equals(uuid))
                if (entityPlayer instanceof ServerPlayer player && player.containerMenu instanceof ContainerMonitor monitor) {
                    monitor.isActiveAllInventory = customPacketBuffer.readBoolean();
                }
            if (entityPlayer instanceof ServerPlayer player && player.containerMenu instanceof ContainerPatternMonitor monitor) {
                monitor.isActiveAllInventory = customPacketBuffer.readBoolean();
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

