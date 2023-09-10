package com.denfop.network;

import com.denfop.network.packet.*;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateTileEntityPacket {


    UpdateTileEntityPacket() {
    }

    public static void send(WorldData worldData) throws IOException {
        for (TileEntityBlock te : worldData.listUpdateTile) {
            CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
            EncoderHandler.encode(commonBuffer, te.getPos(), false);
            List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                    te.getWorld(),
                    te.getPos(),
                    new ArrayList<>()
            );
            commonBuffer.writeBytes(te.writePacket());
            byte[] bytes = new byte[commonBuffer.writerIndex() - commonBuffer.readerIndex()];
            commonBuffer.readBytes(bytes);
            for (EntityPlayerMP player : playersInRange) {
                final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(0);
                playerBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                playerBuffer.writeBytes(bytes);
                new PacketUpdateTile(playerBuffer, player);
            }
        }
        if (worldData.getWorld().provider.getWorldTime() % 20 == 0) {
            for (TileEntityBlock te : worldData.mapUpdateOvertimeField.values()) {
                CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                EncoderHandler.encode(commonBuffer, te.getPos(), false);
                List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                        te.getWorld(),
                        te.getPos(),
                        new ArrayList<>()
                );
                commonBuffer.writeBytes(te.writeUpdatePacket());
                byte[] bytes = new byte[commonBuffer.writerIndex() - commonBuffer.readerIndex()];
                commonBuffer.readBytes(bytes);
                for (EntityPlayerMP player : playersInRange) {
                    final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(0);
                    playerBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                    playerBuffer.writeBytes(bytes);
                    new PacketUpdateOvertimeTile(playerBuffer, player);
                }
            }
        }
        for (Map.Entry<TileEntityBlock, Map<EntityPlayer, CustomPacketBuffer>> entry : worldData.mapUpdateContainer.entrySet()) {
            for (Map.Entry<EntityPlayer, CustomPacketBuffer> entry1 : entry.getValue().entrySet()) {
                CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                EncoderHandler.encode(commonBuffer, entry.getKey().getPos(), false);
                final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(0);
                playerBuffer.writeInt(entry1.getKey().getEntityWorld().provider.getDimension());
                playerBuffer.writeBytes(commonBuffer);
                playerBuffer.writeBytes(entry1.getValue());
                new PacketUpdateFieldContainerTile(playerBuffer, (EntityPlayerMP) entry1.getKey());
            }
        }
        for (Map.Entry<TileEntityBlock, List<CustomPacketBuffer>> entry : worldData.mapUpdateField.entrySet()) {

            final TileEntityBlock te = entry.getKey();

            List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                    te.getWorld(),
                    te.getPos(),
                    new ArrayList<>()
            );
            for (CustomPacketBuffer buffer : entry.getValue()) {
                CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                EncoderHandler.encode(commonBuffer, te.getPos(), false);
                commonBuffer.writeBytes(buffer);
                byte[] bytes = new byte[commonBuffer.writerIndex() - commonBuffer.readerIndex()];
                commonBuffer.readBytes(bytes);
                for (EntityPlayerMP player : playersInRange) {
                    final CustomPacketBuffer playerBuffer = new CustomPacketBuffer(0);
                    playerBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                    playerBuffer.writeBytes(bytes);
                    new PacketUpdateFieldTile(playerBuffer, player);
                }
            }
        }
        worldData.mapUpdateField.clear();
        worldData.mapUpdateContainer.clear();
        worldData.listUpdateTile.clear();
    }


}
