package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.network.packet.*;
import com.denfop.tiles.base.TileEntityBlock;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public class NetworkManager {

    private static SimpleChannel channel;
    private static ResourceLocation handler;
    public static Map<Byte, IPacket> packetMap = new HashMap<>();

    public NetworkManager() {
        handler = new ResourceLocation("industrialupgrade", "network");

        if (channel == null) {
            channel = NetworkRegistry.newSimpleChannel(handler, () -> "1.0.0", (e) -> true, (e) -> true);
        }
        this.registerPacket(new PacketKeys());
        this.registerPacket(new PacketAbstractComponent());
        this.registerPacket(new PacketColorPickerAllLoggIn(null));
        this.registerPacket(new PacketRadiation());
        this.registerPacket(new PacketUpdateServerTile());
        this.registerPacket(new PacketUpdateTile());
        this.registerPacket(new PacketRadiationChunk());
        this.registerPacket(new PacketRadiationUpdateValue());
        this.registerPacket(new PacketUpdateFieldContainerTile());
        this.registerPacket(new PacketColorPicker());
        this.registerPacket(new PacketUpdateFieldTile());
        this.registerPacket(new PacketExplosion());
        this.registerPacket(new PacketChangeSolarPanel());
        this.registerPacket(new PacketSoundPlayer());
        this.registerPacket(new PacketUpdateOvertimeTile());
        this.registerPacket(new PacketItemStackUpdate());
        this.registerPacket(new PacketItemStackEvent());
        this.registerPacket(new PacketStopSound());
        this.registerPacket(new PacketUpdateInventory());
        this.registerPacket(new PacketUpdateRadiationValue());
        this.registerPacket(new PacketUpdateRadiation());
        this.registerPacket(new PacketStopSoundPlayer());
        this.registerPacket(new PacketAddRelocatorPoint());
        this.registerPacket(new PacketRemoveRelocatorPoint());
        this.registerPacket(new PacketRelocatorTeleportPlayer());
        this.registerPacket(new PacketUpdateBody());
        this.registerPacket(new PacketUpdateFakeBody());
        this.registerPacket(new PacketSendRoversToPlanet());
        this.registerPacket(new PacketReturnRoversToPlanet());
        this.registerPacket(new PacketChangeSpaceOperation());
        this.registerPacket(new PacketCreateColony());
        this.registerPacket(new PacketSendResourceToEarth());
        this.registerPacket(new PacketAddBuildingToColony());
        this.registerPacket(new PacketSuccessUpdateColony());
        this.registerPacket(new PacketCreateAutoSends());
        this.registerPacket(new PacketDeleteColony());
        this.registerPacket(new PacketUpdateCompleteQuest());
        this.registerPacket(new PacketUpdateInformationAboutQuestsPlayer());
        this.registerPacket(new PacketSynhronyzationRelocator());
        this.registerPacket(new PacketUpdateRelocator());
        this.registerPacket(new PacketFixerRecipe());
        this.registerPacket(new PacketDrainFluidPipette());

        channel.registerMessage(0, CustomPacketBuffer.class,
                (customPacketBuffer, buf) -> {
                    buf.writeBytes(customPacketBuffer);
                }, CustomPacketBuffer::new
                , this::onPacketData);
    }

    public static SimpleChannel getChannel() {
        return channel;
    }

    public static <T extends Collection<ServerPlayer>> T getPlayersInRange(Level world, BlockPos pos, T result) {
        if (!(world instanceof ServerLevel)) {
            return result;
        } else {
            List<ServerPlayer> list = ((ServerLevel) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
            result.addAll(list);
            return result;
        }
    }

    public Packet<?> makePacket(NetworkDirection direction, CustomPacketBuffer buffer) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(0);
        buf.writeBoolean(this.isClient());
        buf.writeBytes(buffer);
        return direction.buildPacket(Pair.of(buf, 0), handler).getThis();
    }

    public void sendPacket(PacketDistributor.PacketTarget packetDistributor, CustomPacketBuffer buffer) {
        if (!this.isClient()) {
            packetDistributor.send(makePacket(packetDistributor.getDirection(), buffer));
        } else{
            IUCore.network.getClient().sendPacket(packetDistributor,buffer);
        }

    }

    public void registerPacket(IPacket packet) {

        if (!packetMap.containsKey(packet.getId())) {
            packetMap.put(packet.getId(), packet);
        }

    }


    protected boolean isClient() {
        return false;
    }

    public void onPacketData(CustomPacketBuffer is, Supplier<NetworkEvent.Context> ctx) {
        boolean isClient = is.readBoolean();
        if (!isClient) {
            is.retain();
            byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
            is.readBytes(bytes);
            ctx.get().enqueueWork(() -> {
                CustomPacketBuffer is1 = new CustomPacketBuffer(bytes);
                if (is1.writerIndex() > is1.readerIndex()) {
                    byte type = is1.readByte();
                    IUCore.network.getClient().onPacketData(is1,type);

                }
            });
        } else {
            is.retain();
            byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
            is.readBytes(bytes);
            ctx.get().enqueueWork(() -> {
                CustomPacketBuffer is1 = new CustomPacketBuffer(bytes);
                if (is1.writerIndex() > is1.readerIndex()) {
                    try {
                        byte type = is1.readByte();
                        IPacket packet = packetMap.get(type);
                        if (packet != null && packet.getPacketType() == EnumTypePacket.CLIENT) {
                            packet.readPacket(is1, ctx.get().getSender());
                        }
                    } catch (Exception e) {
                        System.err.println("Ошибка обработки пакета: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }

    public void onPacketData(CustomPacketBuffer is, byte type) {
    }

    public final void sendPacket(CustomPacketBuffer buffer, ServerPlayer player) {
        this.sendPacket(PacketDistributor.PLAYER.with(() -> player), buffer);
    }

    public void sendPacket(CustomPacketBuffer buffer) {
        if (!this.isClient()) {
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            for (ServerPlayer player : players.getPlayers())
                this.sendPacket(PacketDistributor.PLAYER.with(() -> player), buffer);
        } else {
            IUCore.network.getClient().sendPacket(buffer);
        }

    }

    public void onTickEnd(WorldData worldData) {
        try {
            UpdateTileEntityPacket.send(worldData);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public void addTileContainerToUpdate(TileEntityBlock te, ServerPlayer player, CustomPacketBuffer packetBuffer) {
        if (te == null) {
            return;
        }
        WorldData worldData = WorldData.get(te.getLevel());
        if (te.isRemoved()) {
            return;
        }
        Map<Player, CustomPacketBuffer> map;
        if (worldData.mapUpdateContainer.containsKey(te)) {
            map = worldData.mapUpdateContainer.computeIfAbsent(te, k -> new HashMap<>());
        } else {
            map = new HashMap<>();
            worldData.mapUpdateContainer.put(te, map);
        }
        map.put(player, packetBuffer);
    }

    public void addTileToUpdate(TileEntityBlock te) {
        if (te.hasLevel()) {
            WorldData worldData = WorldData.get(te.getLevel());
            worldData.listUpdateTile.add(te);
        }
    }

    public void addTileToOvertimeUpdate(TileEntityBlock te) {
        WorldData worldData = WorldData.get(te.getLevel());
        if (!worldData.mapUpdateOvertimeField.containsKey(te.getBlockPos())) {
            worldData.mapUpdateOvertimeField.put(te.getBlockPos(), te);
        }
    }

    public void removeTileToOvertimeUpdate(TileEntityBlock te) {
        WorldData worldData = WorldData.get(te.getLevel());
        worldData.mapUpdateOvertimeField.remove(te.getBlockPos());
    }

    public void addTileFieldToUpdate(TileEntityBlock te, CustomPacketBuffer packet) {
        WorldData worldData = WorldData.get(te.getLevel());
        if (worldData.mapUpdateField.containsKey(te)) {
            worldData.mapUpdateField.get(te).add(packet);
        } else {
            worldData.mapUpdateField.put(te, new LinkedList<>(Collections.singletonList(packet)));
        }
    }


}
