package com.denfop.api.agriculture.genetics;

import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.api.agriculture.genetics.EnumGenetic.AIR;
import static com.denfop.api.agriculture.genetics.EnumGenetic.BEE;
import static com.denfop.api.agriculture.genetics.EnumGenetic.CHANCE;
import static com.denfop.api.agriculture.genetics.EnumGenetic.GENOME_ADAPTIVE;
import static com.denfop.api.agriculture.genetics.EnumGenetic.GENOME_RESISTANCE;
import static com.denfop.api.agriculture.genetics.EnumGenetic.GROW_SPEED;
import static com.denfop.api.agriculture.genetics.EnumGenetic.LIGHT;
import static com.denfop.api.agriculture.genetics.EnumGenetic.PEST;
import static com.denfop.api.agriculture.genetics.EnumGenetic.RADIATION;
import static com.denfop.api.agriculture.genetics.EnumGenetic.SEED;
import static com.denfop.api.agriculture.genetics.EnumGenetic.SOIL;
import static com.denfop.api.agriculture.genetics.EnumGenetic.WEATHER;
import static com.denfop.api.agriculture.genetics.EnumGenetic.WEED;
import static com.denfop.api.agriculture.genetics.EnumGenetic.YIELD;
import static com.denfop.api.agriculture.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.agriculture.genetics.GeneticsManager.geneticTraitsMap;

public enum GeneticTraits {

    SOIL_I(SOIL, LevelPollution.MEDIUM, null),
    SOIL_II(SOIL, LevelPollution.HIGH, SOIL_I),
    SOIL_III(SOIL, LevelPollution.VERY_HIGH, SOIL_II),
    CHANCE_I(CHANCE, 3, null),
    CHANCE_II(CHANCE, 5, CHANCE_I),
    CHANCE_III(CHANCE, 8, CHANCE_II),
    BEECOMBINE(BEE, true, null),
    YIELD_I(YIELD, 2, null),
    YIELD_II(YIELD, 3, YIELD_I),
    YIELD_III(YIELD, 5, YIELD_II),
    WEATHER_I(WEATHER, 1, null),
    WEATHER_II(WEATHER, 2, WEATHER_I),
    WATER(EnumGenetic.WATER, true, null),
    LIGHT_I(LIGHT, 9, null),
    LIGHT_II(LIGHT, 6, LIGHT_I),
    LIGHT_III(LIGHT, 3, LIGHT_II),
    LIGHT_IV(LIGHT, 0, LIGHT_III),
    BIOME_I(EnumGenetic.BIOME, 1, null),
    BIOME_II(EnumGenetic.BIOME, 2, null),
    BIOME(EnumGenetic.BIOME, 3, null),
    BIOME_III(EnumGenetic.BIOME, 4, null),
    BIOME_IV(EnumGenetic.BIOME, 5, null),
    GROW_SPEED_I(GROW_SPEED, 1.25D, null),
    GROW_SPEED_II(GROW_SPEED, 1.5D, GROW_SPEED_I),
    GROW_SPEED_III(GROW_SPEED, 2.0D, GROW_SPEED_II),
    PEST_I(PEST, 5, null),
    PEST_II(PEST, 10, PEST_I),
    PEST_III(PEST, 15, PEST_II),
    SEED_I(SEED, 2, null),
    SEED_II(SEED, 3, SEED_I),
    SEED_III(SEED, 4, SEED_II),
    WEED_I(WEED, 1, null),
    WEED_II(WEED, 2, WEED_I),
    WEED_III(WEED, 3, WEED_II),
    SOIL_BLOCK(EnumGenetic.SOIL_BLOCK, true, null),
    SUN(EnumGenetic.SUN, true, null),
    NIGHT_GROW(EnumGenetic.NIGHT_GROW, true, null),
    AIR_I(AIR, LevelPollution.MEDIUM, null),
    AIR_II(AIR, LevelPollution.HIGH, AIR_I),
    AIR_III(AIR, LevelPollution.VERY_HIGH, AIR_II),
    RADIATION_I(RADIATION, EnumLevelRadiation.MEDIUM, null),
    RADIATION_II(RADIATION, EnumLevelRadiation.HIGH, RADIATION_I),
    RADIATION_III(RADIATION, EnumLevelRadiation.VERY_HIGH, RADIATION_II),
    GENOME_RESISTANCE_I(GENOME_RESISTANCE, 25, null),
    GENOME_RESISTANCE_II(GENOME_RESISTANCE, 50, GENOME_RESISTANCE_I),
    GENOME_RESISTANCE_III(GENOME_RESISTANCE, 100, GENOME_RESISTANCE_II),
    GENOME_ADAPTIVE_I(GENOME_ADAPTIVE, 20, null),
    GENOME_ADAPTIVE_II(GENOME_ADAPTIVE, 40, GENOME_ADAPTIVE_I),
    GENOME_ADAPTIVE_III(GENOME_ADAPTIVE, 60, GENOME_ADAPTIVE_II);

    private final EnumGenetic genetic;
    private final Object value;
    private final GeneticTraits prev;

    <T> GeneticTraits(EnumGenetic genetic, T value, GeneticTraits prev) {
        this.genetic = genetic;
        this.value = value;
        this.prev = prev;
        if (prev != null) {
            geneticTraitsMap.put(prev, this);
        }
        List<GeneticTraits> geneticTraits = enumGeneticListMap.get(genetic);
        if (geneticTraits == null) {
            geneticTraits = new ArrayList<>();
            geneticTraits.add(this);
            enumGeneticListMap.put(genetic, geneticTraits);
        } else {
            geneticTraits.add(this);
        }
    }

    public GeneticTraits getPrev() {
        return prev;
    }

    public <T> T getValue(Class<T> type) {
        return type.cast(value);
    }

    public EnumGenetic getGenetic() {
        return genetic;
    }
}
