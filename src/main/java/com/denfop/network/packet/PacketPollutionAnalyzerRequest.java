package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshot;
import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshotBuilder;
import com.denfop.items.ItemPollutionDevice;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class PacketPollutionAnalyzerRequest implements IPacket {

    public PacketPollutionAnalyzerRequest() {
    }

    public PacketPollutionAnalyzerRequest(boolean sendNow) {
        if (sendNow) {
            CustomPacketBuffer buffer = new CustomPacketBuffer(8);
            buffer.writeByte(this.getId());
            buffer.flip();
            IUCore.network.getClient().sendPacket(buffer);
        }
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