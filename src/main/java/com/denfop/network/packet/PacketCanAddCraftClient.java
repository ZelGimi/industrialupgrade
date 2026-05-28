package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.screen.ScreenFluidMonitor;
import com.denfop.screen.ScreenMonitor;
import com.denfop.screen.ScreenPatternMonitor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketCanAddCraftClient implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketCanAddCraftClient() {

    }


    public PacketCanAddCraftClient(Player player, Boolean can) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, can);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer, (ServerPlayer) player);
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
        return 119;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            ScreenMonitor.canAddAutoCraft = (Boolean) DecoderHandler.decode(customPacketBuffer);
            ScreenPatternMonitor.canAddAutoCraft = ScreenMonitor.canAddAutoCraft;
            ScreenFluidMonitor.canAddAutoCraft = ScreenMonitor.canAddAutoCraft;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}

