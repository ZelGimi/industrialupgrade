package com.denfop.api.pollution.analyzer;

import com.denfop.api.pollution.component.LevelPollution;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollutionAnalyzerChunkSnapshot {

    private final int chunkX;
    private final int chunkZ;

    private final int terrainNW;
    private final int terrainNE;
    private final int terrainSW;
    private final int terrainSE;
    private final int terrainCenter;

    private final double airSeverity;
    private final double airBandPollution;
    private final int airLevelOrdinal;
    private final double airRatePerSecond;
    private final int airTimeToRiseSeconds;
    private final int airTimeToFallSeconds;
    private final double airForecastFiveMinutes;
    private final double airCleaningPerSecond;

    private final double soilSeverity;
    private final double soilBandPollution;
    private final int soilLevelOrdinal;
    private final double soilRatePerSecond;
    private final int soilTimeToRiseSeconds;
    private final int soilTimeToFallSeconds;
    private final double soilForecastFiveMinutes;
    private final double soilCleaningPerSecond;

    private final float riskScore;
    private final List<PollutionAnalyzerTerrainColumn> terrainColumns;

    private final double[] airRateTickSeries;
    private final double[] soilRateTickSeries;
    private final double[] airTotalTickSeries;
    private final double[] soilTotalTickSeries;

    public PollutionAnalyzerChunkSnapshot(
            int chunkX,
            int chunkZ,
            int terrainNW,
            int terrainNE,
            int terrainSW,
            int terrainSE,
            int terrainCenter,
            double airSeverity,
            double airBandPollution,
            int airLevelOrdinal,
            double airRatePerSecond,
            int airTimeToRiseSeconds,
            int airTimeToFallSeconds,
            double airForecastFiveMinutes,
            double airCleaningPerSecond,
            double soilSeverity,
            double soilBandPollution,
            int soilLevelOrdinal,
            double soilRatePerSecond,
            int soilTimeToRiseSeconds,
            int soilTimeToFallSeconds,
            double soilForecastFiveMinutes,
            double soilCleaningPerSecond,
            float riskScore,
            List<PollutionAnalyzerTerrainColumn> terrainColumns,
            double[] airRateTickSeries,
            double[] soilRateTickSeries,
            double[] airTotalTickSeries,
            double[] soilTotalTickSeries
    ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.terrainNW = terrainNW;
        this.terrainNE = terrainNE;
        this.terrainSW = terrainSW;
        this.terrainSE = terrainSE;
        this.terrainCenter = terrainCenter;
        this.airSeverity = airSeverity;
        this.airBandPollution = airBandPollution;
        this.airLevelOrdinal = airLevelOrdinal;
        this.airRatePerSecond = airRatePerSecond;
        this.airTimeToRiseSeconds = airTimeToRiseSeconds;
        this.airTimeToFallSeconds = airTimeToFallSeconds;
        this.airForecastFiveMinutes = airForecastFiveMinutes;
        this.airCleaningPerSecond = airCleaningPerSecond;
        this.soilSeverity = soilSeverity;
        this.soilBandPollution = soilBandPollution;
        this.soilLevelOrdinal = soilLevelOrdinal;
        this.soilRatePerSecond = soilRatePerSecond;
        this.soilTimeToRiseSeconds = soilTimeToRiseSeconds;
        this.soilTimeToFallSeconds = soilTimeToFallSeconds;
        this.soilForecastFiveMinutes = soilForecastFiveMinutes;
        this.soilCleaningPerSecond = soilCleaningPerSecond;
        this.riskScore = riskScore;
        this.terrainColumns = new ArrayList<>(terrainColumns);
        this.airRateTickSeries = airRateTickSeries.clone();
        this.soilRateTickSeries = soilRateTickSeries.clone();
        this.airTotalTickSeries = airTotalTickSeries.clone();
        this.soilTotalTickSeries = soilTotalTickSeries.clone();
    }

    public static PollutionAnalyzerChunkSnapshot fromTag(CompoundTag tag) {
        List<PollutionAnalyzerTerrainColumn> terrainColumns = new ArrayList<>();
        ListTag columnsTag = tag.getList("terrainColumns", 10);
        for (int i = 0; i < columnsTag.size(); i++) {
            terrainColumns.add(PollutionAnalyzerTerrainColumn.fromTag(columnsTag.getCompound(i)));
        }

        return new PollutionAnalyzerChunkSnapshot(
                tag.getInt("chunkX"),
                tag.getInt("chunkZ"),
                tag.getInt("terrainNW"),
                tag.getInt("terrainNE"),
                tag.getInt("terrainSW"),
                tag.getInt("terrainSE"),
                tag.getInt("terrainCenter"),
                tag.getDouble("airSeverity"),
                tag.getDouble("airBandPollution"),
                tag.getInt("airLevelOrdinal"),
                tag.getDouble("airRatePerSecond"),
                tag.getInt("airTimeToRiseSeconds"),
                tag.getInt("airTimeToFallSeconds"),
                tag.getDouble("airForecastFiveMinutes"),
                tag.getDouble("airCleaningPerSecond"),
                tag.getDouble("soilSeverity"),
                tag.getDouble("soilBandPollution"),
                tag.getInt("soilLevelOrdinal"),
                tag.getDouble("soilRatePerSecond"),
                tag.getInt("soilTimeToRiseSeconds"),
                tag.getInt("soilTimeToFallSeconds"),
                tag.getDouble("soilForecastFiveMinutes"),
                tag.getDouble("soilCleaningPerSecond"),
                tag.getFloat("riskScore"),
                terrainColumns,
                readDoubleArray(tag.getList("airRateTickSeries", 10)),
                readDoubleArray(tag.getList("soilRateTickSeries", 10)),
                readDoubleArray(tag.getList("airTotalTickSeries", 10)),
                readDoubleArray(tag.getList("soilTotalTickSeries", 10))
        );
    }

    private static ListTag writeDoubleArray(double[] values) {
        ListTag list = new ListTag();
        for (double value : values) {
            CompoundTag tag = new CompoundTag();
            tag.putDouble("v", value);
            list.add(tag);
        }
        return list;
    }

    private static double[] readDoubleArray(ListTag list) {
        double[] result = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.getCompound(i).getDouble("v");
        }
        return result;
    }

    public long key() {
        return ChunkPos.asLong(chunkX, chunkZ);
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getTerrainNW() {
        return terrainNW;
    }

    public int getTerrainNE() {
        return terrainNE;
    }

    public int getTerrainSW() {
        return terrainSW;
    }

    public int getTerrainSE() {
        return terrainSE;
    }

    public int getTerrainCenter() {
        return terrainCenter;
    }

    public double getAirSeverity() {
        return airSeverity;
    }

    public double getAirBandPollution() {
        return airBandPollution;
    }

    public int getAirLevelOrdinal() {
        return airLevelOrdinal;
    }

    public LevelPollution getAirLevel() {
        return LevelPollution.values()[airLevelOrdinal];
    }

    public double getAirRatePerSecond() {
        return airRatePerSecond;
    }

    public int getAirTimeToRiseSeconds() {
        return airTimeToRiseSeconds;
    }

    public int getAirTimeToFallSeconds() {
        return airTimeToFallSeconds;
    }

    public double getAirForecastFiveMinutes() {
        return airForecastFiveMinutes;
    }

    public double getAirCleaningPerSecond() {
        return airCleaningPerSecond;
    }

    public double getSoilSeverity() {
        return soilSeverity;
    }

    public double getSoilBandPollution() {
        return soilBandPollution;
    }

    public int getSoilLevelOrdinal() {
        return soilLevelOrdinal;
    }

    public LevelPollution getSoilLevel() {
        return LevelPollution.values()[soilLevelOrdinal];
    }

    public double getSoilRatePerSecond() {
        return soilRatePerSecond;
    }

    public int getSoilTimeToRiseSeconds() {
        return soilTimeToRiseSeconds;
    }

    public int getSoilTimeToFallSeconds() {
        return soilTimeToFallSeconds;
    }

    public double getSoilForecastFiveMinutes() {
        return soilForecastFiveMinutes;
    }

    public double getSoilCleaningPerSecond() {
        return soilCleaningPerSecond;
    }

    public float getRiskScore() {
        return riskScore;
    }

    public List<PollutionAnalyzerTerrainColumn> getTerrainColumns() {
        return Collections.unmodifiableList(terrainColumns);
    }

    public double[] getAirRateTickSeries() {
        return airRateTickSeries.clone();
    }

    public double[] getSoilRateTickSeries() {
        return soilRateTickSeries.clone();
    }

    public double[] getAirTotalTickSeries() {
        return airTotalTickSeries.clone();
    }

    public double[] getSoilTotalTickSeries() {
        return soilTotalTickSeries.clone();
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("chunkX", chunkX);
        tag.putInt("chunkZ", chunkZ);

        tag.putInt("terrainNW", terrainNW);
        tag.putInt("terrainNE", terrainNE);
        tag.putInt("terrainSW", terrainSW);
        tag.putInt("terrainSE", terrainSE);
        tag.putInt("terrainCenter", terrainCenter);

        tag.putDouble("airSeverity", airSeverity);
        tag.putDouble("airBandPollution", airBandPollution);
        tag.putInt("airLevelOrdinal", airLevelOrdinal);
        tag.putDouble("airRatePerSecond", airRatePerSecond);
        tag.putInt("airTimeToRiseSeconds", airTimeToRiseSeconds);
        tag.putInt("airTimeToFallSeconds", airTimeToFallSeconds);
        tag.putDouble("airForecastFiveMinutes", airForecastFiveMinutes);
        tag.putDouble("airCleaningPerSecond", airCleaningPerSecond);

        tag.putDouble("soilSeverity", soilSeverity);
        tag.putDouble("soilBandPollution", soilBandPollution);
        tag.putInt("soilLevelOrdinal", soilLevelOrdinal);
        tag.putDouble("soilRatePerSecond", soilRatePerSecond);
        tag.putInt("soilTimeToRiseSeconds", soilTimeToRiseSeconds);
        tag.putInt("soilTimeToFallSeconds", soilTimeToFallSeconds);
        tag.putDouble("soilForecastFiveMinutes", soilForecastFiveMinutes);
        tag.putDouble("soilCleaningPerSecond", soilCleaningPerSecond);

        tag.putFloat("riskScore", riskScore);

        ListTag columns = new ListTag();
        for (PollutionAnalyzerTerrainColumn column : terrainColumns) {
            columns.add(column.toTag());
        }
        tag.put("terrainColumns", columns);

        tag.put("airRateTickSeries", writeDoubleArray(airRateTickSeries));
        tag.put("soilRateTickSeries", writeDoubleArray(soilRateTickSeries));
        tag.put("airTotalTickSeries", writeDoubleArray(airTotalTickSeries));
        tag.put("soilTotalTickSeries", writeDoubleArray(soilTotalTickSeries));

        return tag;
    }
}