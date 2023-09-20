package com.denfop.network;

import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.EnumTypePacket;
import com.denfop.network.packet.IPacket;
import com.denfop.network.packet.PacketAbstractComponent;
import com.denfop.network.packet.PacketCableSound;
import com.denfop.network.packet.PacketColorPicker;
import com.denfop.network.packet.PacketColorPickerAllLoggIn;
import com.denfop.network.packet.PacketExplosion;
import com.denfop.network.packet.PacketFixedClient;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.network.packet.PacketItemStackUpdate;
import com.denfop.network.packet.PacketKeys;
import com.denfop.network.packet.PacketLandEffect;
import com.denfop.network.packet.PacketRadiation;
import com.denfop.network.packet.PacketRadiationChunk;
import com.denfop.network.packet.PacketRemoveUpdateTile;
import com.denfop.network.packet.PacketResearchSystem;
import com.denfop.network.packet.PacketResearchSystemAdd;
import com.denfop.network.packet.PacketResearchSystemDelete;
import com.denfop.network.packet.PacketRunParticles;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.network.packet.PacketStopSound;
import com.denfop.network.packet.PacketUpdateFieldContainerTile;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.network.packet.PacketUpdateOvertimeTile;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static FMLEventChannel channel;
    public Map<Byte, IPacket> packetMap = new HashMap<>();

    public NetworkManager() {
        if (channel == null) {
            channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("IU");
        }

        channel.register(this);
        this.registerPacket(new PacketKeys());
        this.registerPacket(new PacketAbstractComponent());
        this.registerPacket(new PacketColorPickerAllLoggIn(null));
        this.registerPacket(new PacketRadiation());
        this.registerPacket(new PacketUpdateServerTile());
        this.registerPacket(new PacketUpdateTile());
        this.registerPacket(new PacketRadiationChunk());
        this.registerPacket(new PacketUpdateFieldContainerTile());
        this.registerPacket(new PacketColorPicker());
        this.registerPacket(new PacketUpdateFieldTile());
        this.registerPacket(new PacketLandEffect());
        this.registerPacket(new PacketRunParticles());
        this.registerPacket(new PacketExplosion());
        this.registerPacket(new PacketSoundPlayer());
        this.registerPacket(new PacketResearchSystem());
        this.registerPacket(new PacketResearchSystemAdd());
        this.registerPacket(new PacketResearchSystemDelete());
        this.registerPacket(new PacketUpdateOvertimeTile());
        this.registerPacket(new PacketItemStackUpdate());
        this.registerPacket(new PacketItemStackEvent());
        this.registerPacket(new PacketCableSound());
        this.registerPacket(new PacketStopSound());
        this.registerPacket(new PacketFixedClient());
        this.registerPacket(new PacketRemoveUpdateTile());
    }

    private static FMLProxyPacket makePacket(CustomPacketBuffer buffer) {
        return new FMLProxyPacket(new PacketBuffer(buffer.toByteBuf()), "IU");
    }

    public static <T extends Collection<EntityPlayerMP>> T getPlayersInRange(World world, BlockPos pos, T result) {
        if (world instanceof WorldServer) {
            PlayerChunkMap playerManager = ((WorldServer) world).getPlayerChunkMap();
            PlayerChunkMapEntry instance = playerManager.getEntry(pos.getX() >> 4, pos.getZ() >> 4);
            if (instance != null) {
                result.addAll(instance.players);
            }

        }
        return result;
    }


    public void addTileContainerToUpdate(TileEntityBlock te, EntityPlayer player, CustomPacketBuffer packetBuffer) {
        WorldData worldData = WorldData.get(te.getWorld());
        if (te.isInvalid() || te.getWorld().getTileEntity(te.getPos()) == null) {
            return;
        }
        Map<EntityPlayer, CustomPacketBuffer> map;
        if (worldData.mapUpdateContainer.containsKey(te)) {
            map = worldData.mapUpdateContainer.computeIfAbsent(te, k -> new HashMap<>());
        } else {
            map = new HashMap<>();
            worldData.mapUpdateContainer.put(te, map);
        }
        map.put(player, packetBuffer);
    }


    public void addTileToUpdate(TileEntityBlock te) {
        if (te.hasWorld()) {
            WorldData worldData = WorldData.get(te.getWorld());
            worldData.listUpdateTile.add(te);
        }
    }


    public void registerPacket(IPacket packet) {
        if (!packetMap.containsKey(packet.getId())) {
            packetMap.put(packet.getId(), packet);
        }

    }

    public void onTickEnd(WorldData worldData) {
        try {
            UpdateTileEntityPacket.send(worldData);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    protected boolean isClient() {
        return false;
    }

    @SubscribeEvent
    public void onPacket(ServerCustomPacketEvent event) {
        if (this.getClass() == NetworkManager.class) {
            try {
                this.onPacketData(
                        new CustomPacketBuffer(event.getPacket().payload()),
                        ((NetHandlerPlayServer) event.getHandler()).player
                );
            } catch (Throwable var3) {
                throw new RuntimeException(var3);
            }

            event.getPacket().payload().release();
        }

    }

    private void onPacketData(CustomPacketBuffer is, final EntityPlayer player) {
        if (is.writerIndex() > is.readerIndex()) {

            byte type = is.readByte();
            IPacket packet = this.packetMap.get(type);
            if (packet != null && packet.getPacketType() == EnumTypePacket.CLIENT) {
                packet.readPacket(is, player);
            }

        }

    }


    public final void sendPacket(CustomPacketBuffer buffer, EntityPlayerMP player) {
        assert !this.isClient();

        channel.sendTo(makePacket(buffer), player);
    }

    public final void sendPacket(CustomPacketBuffer buffer) {
        if (!this.isClient()) {
            channel.sendToAll(makePacket(buffer));
        } else {
            channel.sendToServer(makePacket(buffer));
        }

    }


    public void addTileFieldToUpdate(TileEntityBlock te, CustomPacketBuffer packet) {
        WorldData worldData = WorldData.get(te.getWorld());
        if (worldData.mapUpdateField.containsKey(te)) {
            worldData.mapUpdateField.get(te).add(packet);
        } else {
            worldData.mapUpdateField.put(te, new ArrayList<>(Collections.singletonList(packet)));
        }
    }

    public void addTileToOvertimeUpdate(TileEntityBlock te) {
        WorldData worldData = WorldData.get(te.getWorld());
        if (!worldData.mapUpdateOvertimeField.containsKey(te.getPos())) {
            worldData.mapUpdateOvertimeField.put(te.getPos(), te);
        }
    }
    public void removeOvertimeUpdate(TileEntityBlock te) {
        WorldData worldData = WorldData.get(te.getWorld());
        worldData.mapUpdateOvertimeField.remove(te.getPos());
    }
    public void removeTileToOvertimeUpdate(TileEntityBlock te) {
        WorldData worldData = WorldData.get(te.getWorld());
        worldData.mapUpdateOvertimeField.remove(te.getPos());
    }

}
