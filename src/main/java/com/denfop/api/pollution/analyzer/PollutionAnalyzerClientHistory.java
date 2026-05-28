package com.denfop.api.pollution.analyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PollutionAnalyzerClientHistory {

    public static final int HISTORY_TICKS = 20 * 60;

    private final Map<Long, Series> seriesMap = new HashMap<>();

    public void capture(PollutionAnalyzerSnapshot snapshot) {
        if (snapshot == null || !snapshot.isSupportedDimension()) {
            return;
        }

        Set<Long> visible = new HashSet<>();

        for (PollutionAnalyzerChunkSnapshot chunk : snapshot.getChunks()) {
            long key = chunk.key();
            visible.add(key);

            Series series = this.seriesMap.computeIfAbsent(key, k -> new Series());
            series.push(
                    chunk.getAirSeverity(),
                    chunk.getSoilSeverity(),
                    chunk.getAirCleaningPerSecond(),
                    chunk.getSoilCleaningPerSecond()
            );
        }

        this.seriesMap.keySet().removeIf(key -> !visible.contains(key));
    }

    public double[] getAirTotal(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.airTotal);
    }

    public double[] getSoilTotal(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.soilTotal);
    }

    public double[] getAirRate(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.airRate);
    }

    public double[] getSoilRate(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.soilRate);
    }

    public double[] getAirCleaning(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.airCleaning);
    }

    public double[] getSoilCleaning(long key) {
        Series s = this.seriesMap.get(key);
        return s == null ? new double[]{0.0D, 0.0D} : s.copyChronological(s.soilCleaning);
    }

    private static final class Series {
        private final double[] airTotal = new double[HISTORY_TICKS];
        private final double[] soilTotal = new double[HISTORY_TICKS];
        private final double[] airRate = new double[HISTORY_TICKS];
        private final double[] soilRate = new double[HISTORY_TICKS];
        private final double[] airCleaning = new double[HISTORY_TICKS];
        private final double[] soilCleaning = new double[HISTORY_TICKS];

        private int cursor = 0;
        private int size = 0;

        private double lastAir = 0.0D;
        private double lastSoil = 0.0D;
        private boolean initialized = false;

        private void push(double air, double soil, double airCleaningValue, double soilCleaningValue) {
            double airDeltaPerSecond = 0.0D;
            double soilDeltaPerSecond = 0.0D;

            if (initialized) {
                airDeltaPerSecond = (air - lastAir) * 20.0D;
                soilDeltaPerSecond = (soil - lastSoil) * 20.0D;
            }

            airTotal[cursor] = air;
            soilTotal[cursor] = soil;
            airRate[cursor] = airDeltaPerSecond;
            soilRate[cursor] = soilDeltaPerSecond;
            airCleaning[cursor] = airCleaningValue;
            soilCleaning[cursor] = soilCleaningValue;

            lastAir = air;
            lastSoil = soil;
            initialized = true;

            cursor = (cursor + 1) % HISTORY_TICKS;
            size = Math.min(size + 1, HISTORY_TICKS);
        }

        private double[] copyChronological(double[] src) {
            if (size <= 0) {
                return new double[]{0.0D, 0.0D};
            }

            double[] out = new double[size];
            int start = (cursor - size + HISTORY_TICKS) % HISTORY_TICKS;

            for (int i = 0; i < size; i++) {
                out[i] = src[(start + i) % HISTORY_TICKS];
            }

            return out;
        }
    }
}