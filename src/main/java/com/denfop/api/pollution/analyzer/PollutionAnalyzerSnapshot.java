package com.denfop.api.pollution.analyzer;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollutionAnalyzerSnapshot {

    private final boolean supportedDimension;
    private final String dimensionName;
    private final long worldGameTime;
    private final int centerChunkX;
    private final int centerChunkZ;
    private final String windSide;
    private final String windType;

    private final PollutionAnalyzerThresholds airThresholds;
    private final PollutionAnalyzerThresholds soilThresholds;

    private final PollutionAnalyzerCleanerOption airCleaner;
    private final PollutionAnalyzerCleanerOption soilCleaner;

    private final List<PollutionAnalyzerChunkSnapshot> chunks;
    private final List<PollutionAnalyzerSourceSnapshot> sources;
    private final List<PollutionAnalyzerRecommendation> recommendations;

    private transient Map<Long, PollutionAnalyzerChunkSnapshot> byKey;

    public PollutionAnalyzerSnapshot(
            boolean supportedDimension,
            String dimensionName,
            long worldGameTime,
            int centerChunkX,
            int centerChunkZ,
            String windSide,
            String windType,
            PollutionAnalyzerThresholds airThresholds,
            PollutionAnalyzerThresholds soilThresholds,
            PollutionAnalyzerCleanerOption airCleaner,
            PollutionAnalyzerCleanerOption soilCleaner,
            List<PollutionAnalyzerChunkSnapshot> chunks,
            List<PollutionAnalyzerSourceSnapshot> sources,
            List<PollutionAnalyzerRecommendation> recommendations
    ) {
        this.supportedDimension = supportedDimension;
        this.dimensionName = dimensionName;
        this.worldGameTime = worldGameTime;
        this.centerChunkX = centerChunkX;
        this.centerChunkZ = centerChunkZ;
        this.windSide = windSide;
        this.windType = windType;
        this.airThresholds = airThresholds;
        this.soilThresholds = soilThresholds;
        this.airCleaner = airCleaner;
        this.soilCleaner = soilCleaner;
        this.chunks = new ArrayList<>(chunks);
        this.sources = new ArrayList<>(sources);
        this.recommendations = new ArrayList<>(recommendations);
    }

    public static PollutionAnalyzerSnapshot fromTag(CompoundTag tag, RegistryAccess access) {
        List<PollutionAnalyzerChunkSnapshot> chunks = new ArrayList<>();
        ListTag chunksTag = tag.getList("chunks", 10);
        for (int i = 0; i < chunksTag.size(); i++) {
            chunks.add(PollutionAnalyzerChunkSnapshot.fromTag(chunksTag.getCompound(i)));
        }

        List<PollutionAnalyzerSourceSnapshot> sources = new ArrayList<>();
        ListTag sourcesTag = tag.getList("sources", 10);
        for (int i = 0; i < sourcesTag.size(); i++) {
            sources.add(PollutionAnalyzerSourceSnapshot.fromTag(sourcesTag.getCompound(i), access));
        }

        List<PollutionAnalyzerRecommendation> recommendations = new ArrayList<>();
        ListTag recommendationsTag = tag.getList("recommendations", 10);
        for (int i = 0; i < recommendationsTag.size(); i++) {
            recommendations.add(PollutionAnalyzerRecommendation.fromTag(recommendationsTag.getCompound(i)));
        }

        return new PollutionAnalyzerSnapshot(
                tag.getBoolean("supportedDimension"),
                tag.getString("dimensionName"),
                tag.getLong("worldGameTime"),
                tag.getInt("centerChunkX"),
                tag.getInt("centerChunkZ"),
                tag.getString("windSide"),
                tag.getString("windType"),
                PollutionAnalyzerThresholds.fromTag(tag.getCompound("airThresholds")),
                PollutionAnalyzerThresholds.fromTag(tag.getCompound("soilThresholds")),
                PollutionAnalyzerCleanerOption.fromTag(tag.getCompound("airCleaner"), access),
                PollutionAnalyzerCleanerOption.fromTag(tag.getCompound("soilCleaner"), access),
                chunks,
                sources,
                recommendations
        );
    }

    public boolean isSupportedDimension() {
        return supportedDimension;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public long getWorldGameTime() {
        return worldGameTime;
    }

    public int getCenterChunkX() {
        return centerChunkX;
    }

    public int getCenterChunkZ() {
        return centerChunkZ;
    }

    public String getWindSide() {
        return windSide;
    }

    public String getWindType() {
        return windType;
    }

    public PollutionAnalyzerThresholds getAirThresholds() {
        return airThresholds;
    }

    public PollutionAnalyzerThresholds getSoilThresholds() {
        return soilThresholds;
    }

    public PollutionAnalyzerCleanerOption getAirCleaner() {
        return airCleaner;
    }

    public PollutionAnalyzerCleanerOption getSoilCleaner() {
        return soilCleaner;
    }

    public List<PollutionAnalyzerChunkSnapshot> getChunks() {
        return chunks;
    }

    public List<PollutionAnalyzerSourceSnapshot> getSources() {
        return sources;
    }

    public List<PollutionAnalyzerRecommendation> getRecommendations() {
        return recommendations;
    }

    public long centerKey() {
        return ChunkPos.asLong(centerChunkX, centerChunkZ);
    }

    public PollutionAnalyzerChunkSnapshot getChunk(long key) {
        if (byKey == null) {
            byKey = new HashMap<>();
            for (PollutionAnalyzerChunkSnapshot chunk : chunks) {
                byKey.put(chunk.key(), chunk);
            }
        }
        return byKey.get(key);
    }

    public CompoundTag toTag(RegistryAccess registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("supportedDimension", supportedDimension);
        tag.putString("dimensionName", dimensionName);
        tag.putLong("worldGameTime", worldGameTime);
        tag.putInt("centerChunkX", centerChunkX);
        tag.putInt("centerChunkZ", centerChunkZ);
        tag.putString("windSide", windSide);
        tag.putString("windType", windType);

        tag.put("airThresholds", airThresholds.toTag());
        tag.put("soilThresholds", soilThresholds.toTag());
        tag.put("airCleaner", airCleaner.toTag(registryAccess));
        tag.put("soilCleaner", soilCleaner.toTag(registryAccess));

        ListTag chunksTag = new ListTag();
        for (PollutionAnalyzerChunkSnapshot chunk : chunks) {
            chunksTag.add(chunk.toTag());
        }
        tag.put("chunks", chunksTag);

        ListTag sourcesTag = new ListTag();
        for (PollutionAnalyzerSourceSnapshot source : sources) {
            sourcesTag.add(source.toTag(registryAccess));
        }
        tag.put("sources", sourcesTag);

        ListTag recommendationsTag = new ListTag();
        for (PollutionAnalyzerRecommendation recommendation : recommendations) {
            recommendationsTag.add(recommendation.toTag());
        }
        tag.put("recommendations", recommendationsTag);

        return tag;
    }
}