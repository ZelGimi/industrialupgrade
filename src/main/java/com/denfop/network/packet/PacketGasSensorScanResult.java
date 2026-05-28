package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.gassensor.GasSensorClientCache;
import com.denfop.api.gassensor.GasSensorScanState;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketGasSensorScanResult implements IPacket {

    public PacketGasSensorScanResult() {
    }

    public PacketGasSensorScanResult(ServerPlayer player, GasSensorScanState state) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(getId());

        try {
            EncoderHandler.encode(buffer, state.toTag());
        } catch (IOException ignored) {
        }

        IUCore.network.getServer().sendPacket(buffer, player);
    }

    @Override
    public byte getId() {
        return -102;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        try {
            CompoundTag tag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            GasSensorClientCache.applyResult(GasSensorScanState.fromTag(tag));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}