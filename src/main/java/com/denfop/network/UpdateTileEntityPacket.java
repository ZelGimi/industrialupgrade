package com.denfop.network;

import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateTileEntityPacket {

    public static void send(WorldData worldData) throws IOException {
        if (!worldData.listUpdateTile.isEmpty()) {
            for (BlockEntityBase te : new ArrayList<>(worldData.listUpdateTile)) {
                List<ServerPlayer> playersInRange = NetworkManager.getPlayersInRange(
                        te.getLevel(),
                        te.getBlockPos(),
                        new ArrayList<>()
                );
                for (ServerPlayer player : playersInRange) {
                    CustomPacketBuffer commonBuffer = new CustomPacketBuffer(player.registryAccess());
                    commonBuffer.writeByte(0);
                    EncoderHandler.encode(commonBuffer, te.getBlockPos(), false);
                    commonBuffer.writeBytes(te.writePacket());
                    new PacketUpdateTile(commonBuffer, player);
                }
            }
        }

        worldData.listUpdateTile.clear();
        if (worldData.getWorld().getGameTime() % 80 == 0) {
            for (BlockEntityBase te : worldData.mapUpdateOvertimeField.values()) {
                List<ServerPlayer> playersInRange = NetworkManager.getPlayersInRange(
                        te.getLevel(),
                        te.getBlockPos(),
                        new ArrayList<>()
                );
                for (ServerPlayer player : playersInRange) {
                    CustomPacketBuffer commonBuffer = new CustomPacketBuffer(player.registryAccess());
                    commonBuffer.writeByte(25);
                    EncoderHandler.encode(commonBuffer, te.getBlockPos(), false);
                    commonBuffer.writeBytes(te.writeUpdatePacket());
                    new PacketUpdateOvertimeTile(commonBuffer, player);
                }
            }
        }
        for (Map.Entry<BlockEntityBase, Map<Player, CustomPacketBuffer>> entry : worldData.mapUpdateContainer.entrySet()) {
            for (Map.Entry<Player, CustomPacketBuffer> entry1 : entry.getValue().entrySet()) {

                final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(entry1.getKey().registryAccess());
                playerBuffer.writeByte(8);
                EncoderHandler.encode(playerBuffer, entry.getKey().getBlockPos(), false);
                playerBuffer.writeBytes(entry1.getValue());
                new PacketUpdateFieldContainerTile(playerBuffer, (ServerPlayer) entry1.getKey());
            }
        }

        for (Map.Entry<BlockEntityBase, List<CustomPacketBuffer>> entry : worldData.mapUpdateField.entrySet()) {

            final BlockEntityBase te = entry.getKey();

            List<ServerPlayer> playersInRange = NetworkManager.getPlayersInRange(
                    te.getLevel(),
                    te.getBlockPos(),
                    new ArrayList<>()
            );
            for (CustomPacketBuffer buffer : entry.getValue()) {
                byte[] bytes = new byte[buffer.writerIndex() - buffer.readerIndex()];
                buffer.readBytes(bytes);
                for (ServerPlayer player : playersInRange) {
                    final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(player.registryAccess());
                    playerBuffer.writeByte(12);
                    EncoderHandler.encode(playerBuffer, te.getBlockPos(), false);
                    playerBuffer.writeBytes(bytes);
                    new PacketUpdateFieldTile(playerBuffer, player);
                }
            }
        }
        worldData.mapUpdateField.clear();
        worldData.mapUpdateContainer.clear();
        //  worldData.mapUpdateItemStackContainer.clear();
    }
}
