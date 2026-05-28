package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshot;
import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshotBuilder;
import com.denfop.items.ItemPollutionDevice;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class PacketPollutionAnalyzerRequest implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketPollutionAnalyzerRequest() {
    }

    public PacketPollutionAnalyzerRequest(boolean sendNow, RegistryAccess registryAccess) {
        if (sendNow) {
            CustomPacketBuffer buffer = new CustomPacketBuffer(8, registryAccess);
            buffer.writeByte(this.getId());
            buffer.flip();
            this.buffer = buffer;
            IUCore.network.getClient().sendPacket(this, buffer);
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
    public byte getId() {
        return (byte) 200;
    }

    @Override
    public void readPacket(CustomPacketBuffer customPacketBuffer, Player entityPlayer) {
        if (!(entityPlayer instanceof ServerPlayer serverPlayer)) {
            return;
        }

        boolean hasAnalyzer =
                serverPlayer.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemPollutionDevice
                        || serverPlayer.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ItemPollutionDevice;

        if (!hasAnalyzer) {
            return;
        }

        PollutionAnalyzerSnapshot snapshot = PollutionAnalyzerSnapshotBuilder.build(serverPlayer);
        new PacketPollutionAnalyzerSnapshot(snapshot, serverPlayer);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}