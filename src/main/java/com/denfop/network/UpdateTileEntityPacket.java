package com.denfop.network;

import com.denfop.Config;
import com.denfop.items.ItemStackInventory;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldContainerItemStack;
import com.denfop.network.packet.PacketUpdateFieldContainerTile;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateOvertimeTile;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UpdateTileEntityPacket {


    UpdateTileEntityPacket() {
    }

    public static void send(WorldData worldData) throws IOException {
        if (!Config.optimization_network) {
            if (!worldData.listUpdateTile.isEmpty()) {
                for (TileEntityBlock te : new ArrayList<>(worldData.listUpdateTile)) {
                    List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                            te.getWorld(),
                            te.getPos(),
                            new ArrayList<>()
                    );
                    for (EntityPlayerMP player : playersInRange) {
                        CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                        commonBuffer.writeByte(0);
                        commonBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                        EncoderHandler.encode(commonBuffer, te.getPos(), false);
                        commonBuffer.writeBytes(te.writePacket());
                        new PacketUpdateTile(commonBuffer, player);
                    }
                }
            }

            worldData.listUpdateTile.clear();
        } else {
            final Iterator<TileEntityBlock> iterator = worldData.listUpdateTile.iterator();
            int max = Config.optimization_network_col;
            int index = 0;
            try {
                while (iterator.hasNext() && index < max) {
                    index++;
                    TileEntityBlock te = iterator.next();
                    List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                            te.getWorld(),
                            te.getPos(),
                            new ArrayList<>()
                    );
                    for (EntityPlayerMP player : playersInRange) {
                        CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                        commonBuffer.writeByte(0);
                        commonBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                        EncoderHandler.encode(commonBuffer, te.getPos(), false);
                        commonBuffer.writeBytes(te.writePacket());
                        new PacketUpdateTile(commonBuffer, player);
                    }
                    iterator.remove();
                }
            } catch (Exception ignored) {
            }
            ;
        }

        if (worldData.getWorld().provider.getWorldTime() % 80 == 0) {
            for (TileEntityBlock te : worldData.mapUpdateOvertimeField.values()) {
                List<EntityPlayerMP> playersInRange = NetworkManager.getPlayersInRange(
                        te.getWorld(),
                        te.getPos(),
                        new ArrayList<>()
                );
                for (EntityPlayerMP player : playersInRange) {
                    CustomPacketBuffer commonBuffer = new CustomPacketBuffer();
                    commonBuffer.writeByte(25);
                    commonBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                    EncoderHandler.encode(commonBuffer, te.getPos(), false);
                    commonBuffer.writeBytes(te.writeUpdatePacket());
                    new PacketUpdateOvertimeTile(commonBuffer, player);
                }
            }
        }
        for (Map.Entry<TileEntityBlock, Map<EntityPlayer, CustomPacketBuffer>> entry : worldData.mapUpdateContainer.entrySet()) {
            for (Map.Entry<EntityPlayer, CustomPacketBuffer> entry1 : entry.getValue().entrySet()) {

                final CustomPacketBuffer playerBuffer = new CustomPacketBuffer();
                playerBuffer.writeByte(8);
                playerBuffer.writeInt(entry1.getKey().getEntityWorld().provider.getDimension());
                EncoderHandler.encode(playerBuffer, entry.getKey().getPos(), false);
                playerBuffer.writeBytes(entry1.getValue());
                new PacketUpdateFieldContainerTile(playerBuffer, (EntityPlayerMP) entry1.getKey());
            }
        }
        for (Map.Entry<ItemStackInventory, Map<EntityPlayer, CustomPacketBuffer>> entry : worldData.mapUpdateItemStackContainer.entrySet()) {
            for (Map.Entry<EntityPlayer, CustomPacketBuffer> entry1 : entry.getValue().entrySet()) {

                final CustomPacketBuffer playerBuffer = new CustomPacketBuffer();
                playerBuffer.writeByte(133);
                playerBuffer.writeInt(entry1.getKey().getEntityWorld().provider.getDimension());
                EncoderHandler.encode(playerBuffer, entry1.getKey().getName(), false);
                playerBuffer.writeBytes(entry1.getValue());
                new PacketUpdateFieldContainerItemStack(playerBuffer, (EntityPlayerMP) entry1.getKey());
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
                byte[] bytes = new byte[buffer.writerIndex() - buffer.readerIndex()];
                buffer.readBytes(bytes);
                for (EntityPlayerMP player : playersInRange) {
                    final CustomPacketBuffer playerBuffer = new CustomPacketBuffer();
                    playerBuffer.writeByte(12);
                    playerBuffer.writeInt(player.getEntityWorld().provider.getDimension());
                    EncoderHandler.encode(playerBuffer, te.getPos(), false);
                    playerBuffer.writeBytes(bytes);
                    new PacketUpdateFieldTile(playerBuffer, player);
                }
            }
        }
        worldData.mapUpdateField.clear();
        worldData.mapUpdateContainer.clear();
        worldData.mapUpdateItemStackContainer.clear();
    }


}
