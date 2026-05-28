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

public class PacketGasSensorScanProgress implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketGasSensorScanProgress() {
    }

    public PacketGasSensorScanProgress(ServerPlayer player, GasSensorScanState state) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(getId());

        try {
            EncoderHandler.encode(buffer, state.toTag());
        } catch (IOException ignored) {
        }
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, player);
    }

    @Override
    public byte getId() {
        return -101;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        try {
            CompoundTag tag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            GasSensorClientCache.applyProgress(GasSensorScanState.fromTag(tag));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}