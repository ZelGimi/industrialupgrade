package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.gassensor.GasSensorScannerManager;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketGasSensorScanRequest implements IPacket {

    public PacketGasSensorScanRequest() {
    }

    public PacketGasSensorScanRequest(int radiusChunks) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(getId());

        try {
            EncoderHandler.encode(buffer, radiusChunks);
        } catch (IOException ignored) {
        }

        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return -100;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        try {
            int radius = (Integer) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer instanceof ServerPlayer serverPlayer) {
                GasSensorScannerManager.requestScan(serverPlayer, radius);
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