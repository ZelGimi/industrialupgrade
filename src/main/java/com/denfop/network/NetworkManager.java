package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.network.packet.*;
import com.denfop.tiles.base.TileEntityBlock;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class NetworkManager {


    public static Map<Byte, IPacket> packetMap = new HashMap<>();
    public static Map<Byte, CustomPacketPayload.Type<IPacket>> packetTypeMap = new HashMap<>();
    public static boolean reg = false;
    public static StreamCodec<RegistryFriendlyByteBuf, IPacket> STREAM_CODEC = new StreamCodec<>() {
        public IPacket decode(RegistryFriendlyByteBuf p_320167_) {
            CustomPacketBuffer packetBuffer = new CustomPacketBuffer(p_320167_);
            IPacket packet;
            try {
                packet = packetMap.get(packetBuffer.readByte()).getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            byte[] bytes = new byte[p_320167_.writerIndex() - p_320167_.readerIndex()];
            p_320167_.readBytes(bytes);
            packetBuffer = new CustomPacketBuffer(bytes, p_320167_.registryAccess());
            packet.setPacketBuffer(packetBuffer);
            return packet;
        }

        public void encode(RegistryFriendlyByteBuf p_320240_, IPacket p_341316_) {
            CustomPacketBuffer customPacketBuffer = p_341316_.getPacketBuffer();
            customPacketBuffer.flip();
            p_320240_.writeBytes(customPacketBuffer);
            customPacketBuffer.flip();
            p_341316_.setPacketBuffer(customPacketBuffer);
        }
    };

    public NetworkManager() {
        IUCore.context.addListener(this::register);

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

    @SubscribeEvent
    public void register(final RegisterPayloadHandlersEvent event) {

        PayloadRegistrar handler = event.registrar("industrialupgrade");
        this.registerPacket(new PacketKeys());
        this.registerPacket(new PacketAbstractComponent());
        this.registerPacket(new PacketColorPickerAllLoggIn());
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
        this.registerPacket(new PacketDrainFluidPipette());

        if (!reg) {
            reg = true;
            for (IPacket packet : packetMap.values()) {


                if (packet.getPacketType() == EnumTypePacket.CLIENT) {
                    CustomPacketPayload.Type<IPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath(IUCore.MODID, "packet_" + packet.getId()));
                    packetTypeMap.put(packet.getId(), TYPE);
                    handler.playToServer(TYPE, STREAM_CODEC, (payload, context) -> {
                        IUCore.network.getServer().onPacketData(payload.getPacketBuffer(), context);
                    });

                } else {
                    CustomPacketPayload.Type<IPacket> TYPE = new CustomPacketPayload.Type(ResourceLocation.fromNamespaceAndPath(IUCore.MODID, "packet_" + packet.getId()));
                    packetTypeMap.put(packet.getId(), TYPE);
                    handler.playToClient(TYPE, STREAM_CODEC, (payload, context) -> {
                        IUCore.network.getServer().onPacketData(payload.getPacketBuffer(), context);
                    });
                }

            }
        }
    }

    public IPacket makePacket(IPacket packet, CustomPacketBuffer buffer) {
        CustomPacketBuffer buf = new CustomPacketBuffer(Unpooled.buffer(), packet.getPacketBuffer().registryAccess());
        buf.writeByte(packet.getId());
        buf.writeBoolean(this.isClient());
        buf.writeBytes(buffer);
        try {
            packet = packet.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        packet.setPacketBuffer(buf);
        return packet;
    }

    public IPacket makePacket(IPacket packet) {
        CustomPacketBuffer buf = new CustomPacketBuffer(Unpooled.buffer(), packet.getPacketBuffer().registryAccess());
        buf.writeByte(packet.getId());
        buf.writeBoolean(this.isClient());
        buf.writeBytes(packet.getPacketBuffer());
        try {
            packet = packet.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        packet.setPacketBuffer(buf);
        return packet;
    }

    public void sendPacket(IPacket packet, CustomPacketBuffer buffer) {
        if (!this.isClient()) {
            PacketDistributor.sendToAllPlayers(makePacket(packet, buffer));
        } else {
            IUCore.network.getClient().sendPacket(packet, null, buffer);
        }
    }

    public void sendPacket(IPacket packet, Player player, CustomPacketBuffer buffer) {
        if (!this.isClient()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, makePacket(packet, buffer));
        } else {
            IUCore.network.getClient().sendPacket(packet, player, buffer);
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

    public void onPacketData(CustomPacketBuffer is, IPayloadContext ctx) {
        try {
            boolean isClient = is.readBoolean();
            if (!isClient) {
                byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
                is.readBytes(bytes);
                ctx.enqueueWork(() -> {
                    CustomPacketBuffer is1 = new CustomPacketBuffer(bytes, is.registryAccess());
                    if (is1.writerIndex() > is1.readerIndex()) {
                        byte type = is1.readByte();
                        IUCore.network.getClient().onPacketData(is1, type);

                    }
                });
            } else {
                byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
                is.readBytes(bytes);
                ctx.enqueueWork(() -> {
                    CustomPacketBuffer is1 = new CustomPacketBuffer(bytes, is.registryAccess());
                    if (is1.writerIndex() > is1.readerIndex()) {
                        try {
                            byte type = is1.readByte();
                            IPacket packet = packetMap.get(type);
                            if (packet != null && packet.getPacketType() == EnumTypePacket.CLIENT) {
                                packet.readPacket(is1, ctx.player());
                            }
                        } catch (Exception e) {
                            System.err.println("Ошибка обработки пакета: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки пакета: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onPacketData(CustomPacketBuffer is, byte type) {
    }

    public final void sendPacket(IPacket buffer, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, makePacket(buffer));
    }

    public final void sendPacket(IPacket buffer, CustomPacketBuffer is, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, makePacket(buffer, is));
    }

    public void sendPacket(IPacket buffer) {
        if (!this.isClient()) {
            PlayerList players = ServerLifecycleHooks.getCurrentServer().getPlayerList();
            for (ServerPlayer player : players.getPlayers())
                PacketDistributor.sendToPlayer(player, makePacket(buffer));
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
