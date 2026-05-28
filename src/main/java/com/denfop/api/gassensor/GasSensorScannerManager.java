package com.denfop.api.gassensor;


import com.denfop.network.packet.PacketGasSensorScanProgress;
import com.denfop.network.packet.PacketGasSensorScanResult;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.*;

public final class GasSensorScannerManager {

    private static final int MAX_PER_TICK = 2;
    private static final Map<UUID, ScanSession> ACTIVE = new HashMap<>();

    private GasSensorScannerManager() {
    }

    public static void requestScan(ServerPlayer player, int radiusChunks) {
        int clampedRadius = Math.max(1, Math.min(32, radiusChunks));
        ChunkPos center = new ChunkPos(player.blockPosition());

        if (player.level().dimension() != Level.OVERWORLD) {
            new PacketGasSensorScanResult(player, GasSensorScanState.unsupported(clampedRadius, center.x, center.z));
            ACTIVE.remove(player.getUUID());
            return;
        }

        ACTIVE.put(player.getUUID(), new ScanSession(player, clampedRadius));
    }

    public static void tick(MinecraftServer server) {
        if (ACTIVE.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<UUID, ScanSession>> iterator = ACTIVE.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, ScanSession> entry = iterator.next();
            if (entry.getValue().tick(server)) {
                iterator.remove();
            }
        }
    }

    public static void clear(UUID playerId) {
        ACTIVE.remove(playerId);
    }

    private static final class ScanSession {

        private final UUID playerId;
        private final int radiusChunks;
        private final ChunkPos requestCenter;
        private final List<Map.Entry<ChunkPos, GenData>> candidates;
        private final List<GasSensorVeinEntry> found = new ArrayList<>();

        private int index;
        private int currentChunkX;
        private int currentChunkZ;
        private long lastProgressGameTime = Long.MIN_VALUE;

        private ScanSession(ServerPlayer player, int radiusChunks) {
            this.playerId = player.getUUID();
            this.radiusChunks = radiusChunks;
            this.requestCenter = new ChunkPos(player.blockPosition());
            this.candidates = buildCandidates(player, radiusChunks);
            this.index = 0;
            this.currentChunkX = requestCenter.x;
            this.currentChunkZ = requestCenter.z;

            new PacketGasSensorScanProgress(player, buildState(true));
        }

        private List<Map.Entry<ChunkPos, GenData>> buildCandidates(ServerPlayer player, int radiusChunks) {
            List<Map.Entry<ChunkPos, GenData>> list = new ArrayList<>();
            ChunkPos center = new ChunkPos(player.blockPosition());

            for (Map.Entry<ChunkPos, GenData> entry : WorldGenGas.gasMap.entrySet()) {
                ChunkPos pos = entry.getKey();
                if (Math.abs(pos.x - center.x) <= radiusChunks && Math.abs(pos.z - center.z) <= radiusChunks) {
                    list.add(entry);
                }
            }

            list.sort(Comparator.comparingDouble(entry -> {
                ChunkPos pos = entry.getKey();
                int dx = pos.x - center.x;
                int dz = pos.z - center.z;
                return (dx * dx) + (dz * dz);
            }));

            return list;
        }

        private boolean tick(MinecraftServer server) {
            ServerPlayer player = server.getPlayerList().getPlayer(playerId);
            if (player == null) {
                return true;
            }

            if (!(player.level() instanceof ServerLevel serverLevel) || player.level().dimension() != Level.OVERWORLD) {
                new PacketGasSensorScanResult(player, GasSensorScanState.unsupported(radiusChunks, requestCenter.x, requestCenter.z));
                return true;
            }

            int processedThisTick = 0;
            while (processedThisTick < MAX_PER_TICK && index < candidates.size()) {
                Map.Entry<ChunkPos, GenData> entry = candidates.get(index++);
                currentChunkX = entry.getKey().x;
                currentChunkZ = entry.getKey().z;

                GasSensorVeinEntry vein = GasSensorScanUtil.scanCandidate(serverLevel, player, entry.getKey(), entry.getValue());
                if (vein != null) {
                    found.add(vein);
                }

                processedThisTick++;
            }

            if (player.level().getGameTime() - lastProgressGameTime >= 2L) {
                lastProgressGameTime = player.level().getGameTime();
                new PacketGasSensorScanProgress(player, buildState(index < candidates.size()));
            }

            if (index >= candidates.size()) {
                found.sort(Comparator.comparingDouble(GasSensorVeinEntry::getDistanceBlocks));
                new PacketGasSensorScanResult(player, buildState(false));
                return true;
            }

            return false;
        }

        private GasSensorScanState buildState(boolean scanning) {
            return new GasSensorScanState(
                    true,
                    scanning,
                    radiusChunks,
                    requestCenter.x,
                    requestCenter.z,
                    candidates.size(),
                    index,
                    currentChunkX,
                    currentChunkZ,
                    found
            );
        }
    }
}