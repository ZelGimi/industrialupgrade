package com.denfop.api.bee.genetics;

import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;

import java.util.ArrayList;
import java.util.List;

import static com.denfop.api.bee.genetics.EnumGenetic.*;
import static com.denfop.api.bee.genetics.GeneticsManager.enumGeneticListMap;
import static com.denfop.api.bee.genetics.GeneticsManager.geneticTraitsMap;


public enum GeneticTraits {

    SOIL_I(SOIL, LevelPollution.MEDIUM,null),
    SOIL_II(SOIL, LevelPollution.HIGH,SOIL_I),
    SOIL_III(SOIL, LevelPollution.VERY_HIGH,SOIL_II),
    FOOD_I(FOOD, 1.1,null),
    FOOD_II(FOOD, 1.25,FOOD_I),
    FOOD_III(FOOD, 1.5,FOOD_II),
    BIOME(COEF_BIOME, true,null),
    JELLY_I(JELLY, 1.1,null),
    JELLY_II(JELLY, 1.25,JELLY_I),
    JELLY_III(JELLY, 1.5,JELLY_II),
    WEATHER_I(WEATHER, 1,null),
    WEATHER_II(WEATHER, 2,WEATHER_I),
    PRODUCT_I(PRODUCT, 1.075,null),
    PRODUCT_II(PRODUCT, 1.15,PRODUCT_I),
    PRODUCT_III(PRODUCT, 1.225,PRODUCT_II),
    BIRTH_I(BIRTH, 1.2,null),
    BIRTH_II(BIRTH, 1.4,BIRTH_I),
    BIRTH_III(BIRTH, 1.6,BIRTH_II),

    HARDENING_I(HARDENING, 0.8,null),
    HARDENING_II(HARDENING, 0.6,HARDENING_I),
    HARDENING_III(HARDENING, 0.4,HARDENING_II),
    POPULATION_I(POPULATION, 1.1,null),
    POPULATION_II(POPULATION, 1.2,POPULATION_I),
    POPULATION_III(POPULATION, 1.4,POPULATION_II),
    SWARM_I(SWARM, 1.15,null),
    SWARM_II(SWARM, 1.3,SWARM_I),
    SWARM_III(SWARM, 1.45,SWARM_II),
    MORTALITY_I(MORTALITY_RATE, 0.9,null),
    MORTALITY_II(MORTALITY_RATE, 0.8,MORTALITY_I),
    MORTALITY_III(MORTALITY_RATE, 0.7,MORTALITY_II),
    PEST_I(PEST, 1.1,null),
    PEST_II(PEST, 1.2,PEST_I),
    PEST_III(PEST, 1.4,PEST_II),
    RADIUS_I(RADIUS, 1.5,null),
    RADIUS_II(RADIUS, 2.0,RADIUS_I),
    RADIUS_III(RADIUS, 2.5,RADIUS_II),
    SUN(EnumGenetic.SUN, true,null),
    NIGHT(EnumGenetic.NIGHT, true,null),
    AIR_I(AIR, LevelPollution.MEDIUM,null),
    AIR_II(AIR, LevelPollution.HIGH,AIR_I),
    AIR_III(AIR, LevelPollution.VERY_HIGH,AIR_II),
    RADIATION_I(RADIATION, EnumLevelRadiation.MEDIUM,null),
    RADIATION_II(RADIATION, EnumLevelRadiation.HIGH,RADIATION_I),
    RADIATION_III(RADIATION, EnumLevelRadiation.VERY_HIGH,RADIATION_II),
    GENOME_RESISTANCE_I(GENOME_RESISTANCE, 25,null),
    GENOME_RESISTANCE_II(GENOME_RESISTANCE, 50,GENOME_RESISTANCE_I),
    GENOME_RESISTANCE_III(GENOME_RESISTANCE, 100,GENOME_RESISTANCE_II),
    GENOME_ADAPTIVE_I(GENOME_ADAPTIVE, 20,null),
    GENOME_ADAPTIVE_II(GENOME_ADAPTIVE, 40,GENOME_ADAPTIVE_I),
    GENOME_ADAPTIVE_III(GENOME_ADAPTIVE, 60,GENOME_ADAPTIVE_II);

    private final EnumGenetic genetic;
    private final Object value;
    private final GeneticTraits prev;

    <T> GeneticTraits(EnumGenetic genetic, T value, GeneticTraits prev) {
        this.genetic = genetic;
        this.value = value;
        this.prev = prev;
        if (prev != null){
            geneticTraitsMap.put(prev,this);
        }
        List<GeneticTraits> geneticTraits = enumGeneticListMap.get(genetic);
        if (geneticTraits == null){
            geneticTraits = new ArrayList<>();
            geneticTraits.add(this);
            enumGeneticListMap.put(genetic,geneticTraits);
        }else{
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

    public static void init(){
        for (GeneticTraits genetic : values()){
            if (genetic.prev != null){
                geneticTraitsMap.put(genetic.prev,genetic);
            }
            List<GeneticTraits> geneticTraits = enumGeneticListMap.get(genetic.genetic);
            if (geneticTraits == null){
                geneticTraits = new ArrayList<>();
                geneticTraits.add(genetic);
                enumGeneticListMap.put(genetic.genetic,geneticTraits);
            }else{
                geneticTraits.add(genetic);
            }
        }
    }
}
