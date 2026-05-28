package com.denfop.network;

import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.network.packet.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
    public static final StreamCodec<RegistryFriendlyByteBuf, IPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public IPacket decode(RegistryFriendlyByteBuf buf) {
            byte packetId = buf.readByte();

            IPacket template = packetMap.get(packetId);
            if (template == null) {
                throw new IllegalStateException("Unknown packet id: " + packetId);
            }

            IPacket packet;
            try {
                packet = template.getClass().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException |
                     NoSuchMethodException | InvocationTargetException e) {
                throw new RuntimeException("Failed to instantiate packet for id " + packetId, e);
            }

            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);

            packet.setPacketBuffer(new CustomPacketBuffer(bytes, buf.registryAccess()));
            return packet;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, IPacket packet) {
            CustomPacketBuffer src = packet.getPacketBuffer();
            if (src == null) {
                throw new IllegalStateException("Packet buffer is null for " + packet.getClass().getName());
            }

            int readerIndex = src.readerIndex();
            int readableBytes = src.readableBytes();

            buf.writeBytes(src, readerIndex, readableBytes);
        }
    };
    public static Map<Byte, CustomPacketPayload.Type<IPacket>> packetTypeMap = new HashMap<>();
    public static boolean reg = false;

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

        this.registerPacket(new PacketUpdateSkipQuest());
        this.registerPacket(new PacketUpdateBookMarks());
        this.registerPacket(new PacketUpdateVeinData());
        this.registerPacket(new PacketUpdateRecipe());
        this.registerPacket(new PacketFixerRecipe());

        this.registerPacket(new PacketUpdatePollution());
        this.registerPacket(new PacketPollution());
        this.registerPacket(new PacketPollutionAnalyzerRequest());
        this.registerPacket(new PacketPollutionAnalyzerSnapshot());


        this.registerPacket(new PacketGasSensorScanRequest());
        this.registerPacket(new PacketGasSensorScanProgress());
        this.registerPacket(new PacketGasSensorScanResult());

        this.registerPacket(new PacketCreateNetwork());
        this.registerPacket(new PacketUpdateStorageCell());
        this.registerPacket(new PackerUpdateClientRemoveStack());
        this.registerPacket(new PacketRemoveStack());
        this.registerPacket(new PackerUpdateClientAddStack());
        this.registerPacket(new PacketAddStack());
        this.registerPacket(new PacketRemoveShiftStack());
        this.registerPacket(new PacketCanAddCraft());
        this.registerPacket(new PacketCanAddCraftClient());
        this.registerPacket(new PacketAddAutoCraft());
        this.registerPacket(new PacketUpdateMonitor());
        this.registerPacket(new PacketUpdateMonitorInterface());
        this.registerPacket(new PacketUpdateStorageForCraft());
        this.registerPacket(new PacketSetFluid());
        this.registerPacket(new PacketChangeSameStack());
        this.registerPacket(new PacketUpdatePreCraft());
        this.registerPacket(new PacketUpdateNetworkSystem());


        this.registerPacket(new PacketActivateAbility());
        this.registerPacket(new PacketSyncAbilityCooldowns());

        this.registerPacket(new PacketOpenPlanetaryTranslocatorScreen());
        this.registerPacket(new PacketPlanetaryTeleportRequest());
        this.registerPacket(new PacketPlanetaryReturnRequest());
        this.registerPacket(new PacketSpaceTeleportStateSync());
        this.registerPacket(new PacketSpaceTeleportFx());

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
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
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

                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {

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
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null || !server.isRunning()) {
                return;
            }
            PlayerList players = server.getPlayerList();
            if (players == null) {
                return;
            }
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

    private boolean canQueueTile(BlockEntityBase te) {
        return te != null && !te.isRemoved() && te.hasLevel() && te.getLevel() != null && !WorldData.isStoppingOrUnloading(te.getLevel());
    }

    private WorldData getTileWorldData(BlockEntityBase te, boolean load) {
        if (!canQueueTile(te)) {
            return null;
        }
        return WorldData.get(te.getLevel(), load);
    }

    public void addTileContainerToUpdate(BlockEntityBase te, ServerPlayer player, CustomPacketBuffer packetBuffer) {
        if (player == null || packetBuffer == null) {
            return;
        }
        WorldData worldData = getTileWorldData(te, true);
        if (worldData == null) {
            return;
        }
        Map<Player, CustomPacketBuffer> map = worldData.mapUpdateContainer.computeIfAbsent(te, k -> new HashMap<>());
        map.put(player, packetBuffer);
    }

    public void addTileToUpdate(BlockEntityBase te) {
        WorldData worldData = getTileWorldData(te, true);
        if (worldData != null) {
            worldData.listUpdateTile.add(te);
        }
    }

    public void addTileToOvertimeUpdate(BlockEntityBase te) {
        WorldData worldData = getTileWorldData(te, true);
        if (worldData != null && !worldData.mapUpdateOvertimeField.containsKey(te.getBlockPos())) {
            worldData.mapUpdateOvertimeField.put(te.getBlockPos(), te);
        }
    }

    public void removeTileToOvertimeUpdate(BlockEntityBase te) {
        WorldData worldData = getTileWorldData(te, false);
        if (worldData != null) {
            worldData.mapUpdateOvertimeField.remove(te.getBlockPos());
        }
    }

    public void addTileFieldToUpdate(BlockEntityBase te, CustomPacketBuffer packet) {
        if (packet == null) {
            return;
        }
        WorldData worldData = getTileWorldData(te, true);
        if (worldData == null) {
            return;
        }
        if (worldData.mapUpdateField.containsKey(te)) {
            worldData.mapUpdateField.get(te).add(packet);
        } else {
            worldData.mapUpdateField.put(te, new LinkedList<>(Collections.singletonList(packet)));
        }
    }


}
