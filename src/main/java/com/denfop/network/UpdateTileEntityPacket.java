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
        if (worldData == null || worldData.getWorld() == null) {
            return;
        }

        try {
            if (!worldData.listUpdateTile.isEmpty()) {
                for (BlockEntityBase te : new ArrayList<>(worldData.listUpdateTile)) {
                    if (!canSend(te)) {
                        continue;
                    }
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
        } finally {
            worldData.listUpdateTile.clear();
        }

        if (worldData.getWorld().getGameTime() % 80 == 0) {
            worldData.mapUpdateOvertimeField.entrySet().removeIf(entry -> !canSend(entry.getValue()));
            for (BlockEntityBase te : new ArrayList<>(worldData.mapUpdateOvertimeField.values())) {
                if (!canSend(te)) {
                    continue;
                }
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

        try {
            for (Map.Entry<BlockEntityBase, Map<Player, CustomPacketBuffer>> entry : new ArrayList<>(worldData.mapUpdateContainer.entrySet())) {
                BlockEntityBase te = entry.getKey();
                if (!canSend(te)) {
                    continue;
                }
                for (Map.Entry<Player, CustomPacketBuffer> entry1 : entry.getValue().entrySet()) {
                    if (!(entry1.getKey() instanceof ServerPlayer serverPlayer)) {
                        continue;
                    }
                    final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(serverPlayer.registryAccess());
                    playerBuffer.writeByte(8);
                    EncoderHandler.encode(playerBuffer, te.getBlockPos(), false);
                    playerBuffer.writeBytes(entry1.getValue());
                    new PacketUpdateFieldContainerTile(playerBuffer, serverPlayer);
                }
            }

            for (Map.Entry<BlockEntityBase, List<CustomPacketBuffer>> entry : new ArrayList<>(worldData.mapUpdateField.entrySet())) {
                final BlockEntityBase te = entry.getKey();
                if (!canSend(te)) {
                    continue;
                }

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
        } finally {
            worldData.mapUpdateField.clear();
            worldData.mapUpdateContainer.clear();
        }
        //  worldData.mapUpdateItemStackContainer.clear();
    }

    private static boolean canSend(BlockEntityBase te) {
        return te != null && !te.isRemoved() && te.hasLevel() && te.getLevel() != null;
    }
}
