package com.denfop.api.pollution.analyzer;

import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.component.LevelPollution;
import com.denfop.api.pollution.utils.Vec2f;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.EnumWindSide;
import com.denfop.api.windsystem.WindSystem;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public final class PollutionAnalyzerForecastEngine {

    private static final int HORIZON_SECONDS = 600;
    private static final int RUNS = 18;
    private static final int DECAY_INTERVAL_SECONDS = 300;
    private static final int RATE_WINDOW_SECONDS = 60;

    private PollutionAnalyzerForecastEngine() {
    }

    public static Result simulate(
            Set<Long> targetKeys,
            Map<ChunkPos, ChunkLevel> initialAir,
            Map<ChunkPos, ChunkLevel> initialSoil,
            Map<ChunkPos, Double> airSourcesByChunk,
            Map<ChunkPos, Double> soilSourcesByChunk,
            Map<ChunkPos, Double> airCleaningByChunk,
            Map<ChunkPos, Double> soilCleaningByChunk,
            long worldTimeSeed
    ) {
        EnumWindSide windSide = WindSystem.windSystem != null ? WindSystem.windSystem.getWindSide() : EnumWindSide.N;
        EnumTypeWind windType = WindSystem.windSystem != null ? WindSystem.windSystem.getEnumTypeWind() : EnumTypeWind.values()[0];
        Vec2f windVector = vectorForSide(windSide);

        Map<Long, double[]> airSeries = new HashMap<>();
        Map<Long, double[]> soilSeries = new HashMap<>();
        Map<Long, Double> airNow = new HashMap<>();
        Map<Long, Double> soilNow = new HashMap<>();

        for (long key : targetKeys) {
            ChunkPos pos = new ChunkPos(key);
            airSeries.put(key, new double[HORIZON_SECONDS]);
            soilSeries.put(key, new double[HORIZON_SECONDS]);
            airNow.put(key, PollutionAnalyzerMath.severity(initialAir.get(pos)));
            soilNow.put(key, PollutionAnalyzerMath.severity(initialSoil.get(pos)));
        }

        for (int run = 0; run < RUNS; run++) {
            Map<ChunkPos, ChunkLevel> air = cloneMap(initialAir);
            Map<ChunkPos, ChunkLevel> soil = cloneMap(initialSoil);
            RandomSource random = RandomSource.create(worldTimeSeed + (run * 7919L));

            for (int second = 1; second <= HORIZON_SECONDS; second++) {
                moveAirMasses(air, random, windVector, windType);

                if (second % DECAY_INTERVAL_SECONDS == 0) {
                    decayAll(air);
                    decayAll(soil);
                }

                applySources(air, airSourcesByChunk);
                applySources(soil, soilSourcesByChunk);

                applyCleaning(air, airCleaningByChunk);
                applyCleaning(soil, soilCleaningByChunk);

                for (long key : targetKeys) {
                    ChunkPos pos = new ChunkPos(key);
                    airSeries.get(key)[second - 1] += PollutionAnalyzerMath.severity(air.get(pos));
                    soilSeries.get(key)[second - 1] += PollutionAnalyzerMath.severity(soil.get(pos));
                }
            }
        }

        Map<Long, ChunkForecast> airForecasts = new HashMap<>();
        Map<Long, ChunkForecast> soilForecasts = new HashMap<>();

        for (long key : targetKeys) {
            double[] averagedAir = averageSeries(airSeries.get(key));
            double[] averagedSoil = averageSeries(soilSeries.get(key));

            airForecasts.put(key, buildForecast(airNow.get(key), averagedAir));
            soilForecasts.put(key, buildForecast(soilNow.get(key), averagedSoil));
        }

        return new Result(airForecasts, soilForecasts, windSide.name(), windType.name());
    }

    private static double[] averageSeries(double[] values) {
        double[] ret = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i] / RUNS;
        }
        return ret;
    }

    private static ChunkForecast buildForecast(double now, double[] series) {
        int window = Math.min(RATE_WINDOW_SECONDS, series.length);
        double rate = window <= 0 ? 0.0D : (series[window - 1] - now) / window;

        int rise = -1;
        int fall = -1;
        for (int i = 0; i < series.length; i++) {
            double value = series[i];
            if (rise < 0 && value > now + 1.0D) {
                rise = i + 1;
            }
            if (fall < 0 && value < now - 1.0D) {
                fall = i + 1;
            }
            if (rise >= 0 && fall >= 0) {
                break;
            }
        }

        int fiveMinutesIndex = Math.min(299, series.length - 1);
        return new ChunkForecast(now, rate, rise, fall, series[fiveMinutesIndex], series);
    }

    private static void moveAirMasses(
            Map<ChunkPos, ChunkLevel> air,
            RandomSource random,
            Vec2f windVector,
            EnumTypeWind windType
    ) {
        List<ChunkLevel> moved = new ArrayList<>(air.size());
        for (ChunkLevel level : air.values()) {
            ChunkLevel copy = cloneLevel(level);
            if (canMove(random, windType) && distance(copy.getDefaultPos(), copy.getPos()) <= 10.0D) {
                copy.addChunkPos((int) windVector.x, (int) windVector.y);
            }
            moved.add(copy);
        }

        air.clear();
        for (ChunkLevel movedLevel : moved) {
            ChunkPos pos = movedLevel.getPos();
            ChunkLevel existing = air.get(pos);
            if (existing == null) {
                air.put(pos, movedLevel);
            } else {
                double currentSeverity = PollutionAnalyzerMath.severity(existing);
                double incomingSeverity = PollutionAnalyzerMath.severity(movedLevel);
                if (incomingSeverity > currentSeverity) {
                    air.put(pos, movedLevel);
                }
            }
        }
    }

    private static void decayAll(Map<ChunkPos, ChunkLevel> map) {
        for (ChunkLevel chunkLevel : map.values()) {
            if (chunkLevel == null) {
                continue;
            }
            chunkLevel.setPollution(chunkLevel.getPollution() / 2.0D);
            if (chunkLevel.getPollution() < 2.0D && chunkLevel.getLevelPollution() != LevelPollution.VERY_LOW) {
                chunkLevel.setPollution(10.0D);
                chunkLevel.setLevelPollution(LevelPollution.values()[Math.max(
                        0,
                        chunkLevel.getLevelPollution().ordinal() - 1
                )]);
            }
        }
    }

    private static void applySources(Map<ChunkPos, ChunkLevel> map, Map<ChunkPos, Double> sources) {
        for (Map.Entry<ChunkPos, Double> entry : sources.entrySet()) {
            if (entry.getValue() == null || entry.getValue() <= 0.0D) {
                continue;
            }

            ChunkLevel chunkLevel = map.get(entry.getKey());
            if (chunkLevel == null) {
                chunkLevel = new ChunkLevel(entry.getKey(), LevelPollution.VERY_LOW, 0.0D);
                map.put(entry.getKey(), chunkLevel);
            }
            chunkLevel.addPollution(entry.getValue());
        }
    }

    private static void applyCleaning(Map<ChunkPos, ChunkLevel> map, Map<ChunkPos, Double> cleaning) {
        for (Map.Entry<ChunkPos, Double> entry : cleaning.entrySet()) {
            double value = entry.getValue() == null ? 0.0D : entry.getValue();
            if (value <= 0.0D) {
                continue;
            }

            ChunkLevel chunkLevel = map.get(entry.getKey());
            if (chunkLevel == null) {
                continue;
            }

            chunkLevel.removePollution(value);
        }
    }

    private static boolean canMove(RandomSource random, EnumTypeWind windType) {
        int chance = Math.max(1, 100 - windType.ordinal() * 5);
        return random.nextInt(chance) == 0;
    }

    private static double distance(ChunkPos a, ChunkPos b) {
        int dx = a.x - b.x;
        int dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    private static Vec2f vectorForSide(EnumWindSide side) {
        return switch (side) {
            case E -> new Vec2f(1, 0);
            case W -> new Vec2f(-1, 0);
            case N -> new Vec2f(0, 1);
            case S -> new Vec2f(0, -1);
            case NE -> new Vec2f(1, 1);
            case NW -> new Vec2f(-1, 1);
            case SE -> new Vec2f(1, -1);
            case SW -> new Vec2f(-1, -1);
        };
    }

    private static Map<ChunkPos, ChunkLevel> cloneMap(Map<ChunkPos, ChunkLevel> source) {
        Map<ChunkPos, ChunkLevel> copy = new HashMap<>();
        for (Map.Entry<ChunkPos, ChunkLevel> entry : source.entrySet()) {
            copy.put(entry.getKey(), cloneLevel(entry.getValue()));
        }
        return copy;
    }

    private static ChunkLevel cloneLevel(ChunkLevel source) {
        ChunkLevel ret = new ChunkLevel(
                new ChunkPos(source.getPos().x, source.getPos().z),
                source.getLevelPollution(),
                source.getPollution()
        );
        ret.setDefaultPos(new ChunkPos(source.getDefaultPos().x, source.getDefaultPos().z));
        return ret;
    }

    public static final class Result {
        private final Map<Long, ChunkForecast> airForecasts;
        private final Map<Long, ChunkForecast> soilForecasts;
        private final String windSide;
        private final String windType;

        public Result(
                Map<Long, ChunkForecast> airForecasts,
                Map<Long, ChunkForecast> soilForecasts,
                String windSide,
                String windType
        ) {
            this.airForecasts = airForecasts;
            this.soilForecasts = soilForecasts;
            this.windSide = windSide;
            this.windType = windType;
        }

        public ChunkForecast air(long key) {
            return airForecasts.getOrDefault(key, ChunkForecast.empty());
        }

        public ChunkForecast soil(long key) {
            return soilForecasts.getOrDefault(key, ChunkForecast.empty());
        }

        public String getWindSide() {
            return windSide;
        }

        public String getWindType() {
            return windType;
        }
    }

    public static final class ChunkForecast {
        private final double now;
        private final double ratePerSecond;
        private final int timeToRiseSeconds;
        private final int timeToFallSeconds;
        private final double severityAtFiveMinutes;
        private final double[] seriesSeconds;

        public ChunkForecast(
                double now,
                double ratePerSecond,
                int timeToRiseSeconds,
                int timeToFallSeconds,
                double severityAtFiveMinutes,
                double[] seriesSeconds
        ) {
            this.now = now;
            this.ratePerSecond = ratePerSecond;
            this.timeToRiseSeconds = timeToRiseSeconds;
            this.timeToFallSeconds = timeToFallSeconds;
            this.severityAtFiveMinutes = severityAtFiveMinutes;
            this.seriesSeconds = seriesSeconds.clone();
        }

        public static ChunkForecast empty() {
            return new ChunkForecast(0.0D, 0.0D, -1, -1, 0.0D, new double[]{0.0D});
        }

        public double getNow() {
            return now;
        }

        public double getRatePerSecond() {
            return ratePerSecond;
        }

        public int getTimeToRiseSeconds() {
            return timeToRiseSeconds;
        }

        public int getTimeToFallSeconds() {
            return timeToFallSeconds;
        }

        public double getSeverityAtFiveMinutes() {
            return severityAtFiveMinutes;
        }

        public double[] getSeriesSeconds() {
            return seriesSeconds.clone();
        }
    }
}