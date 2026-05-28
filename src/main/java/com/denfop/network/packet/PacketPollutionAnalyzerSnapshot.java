package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshot;
import com.denfop.client.pollution.PollutionAnalyzerClientCache;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;

public class PacketPollutionAnalyzerSnapshot implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketPollutionAnalyzerSnapshot() {
    }

    public PacketPollutionAnalyzerSnapshot(PollutionAnalyzerSnapshot snapshot, ServerPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16384, player.registryAccess());
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, snapshot.toTag(player.registryAccess()), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, player);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return this.buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        this.buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return (byte) 201;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readPacket(CustomPacketBuffer is, Player entityPlayer) {
        try {
            CompoundTag tag = DecoderHandler.decode(is, CompoundTag.class);
            PollutionAnalyzerClientCache.setSnapshot(PollutionAnalyzerSnapshot.fromTag(tag, entityPlayer.registryAccess()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}