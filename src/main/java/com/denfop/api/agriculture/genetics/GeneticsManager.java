package com.denfop.api.agriculture.genetics;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import net.minecraft.world.biome.Biome;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.denfop.api.agriculture.genetics.Genome.geneticBiomes;

public class GeneticsManager {

    public static GeneticsManager instance;
    public static Map<EnumGenetic, List<GeneticTraits>> enumGeneticListMap = new HashMap<>();
    public static Map<GeneticTraits, GeneticTraits> geneticTraitsMap = new HashMap<>();

    public GeneticsManager() {

    }

    public static void init() {
        if (instance == null) {
            instance = new GeneticsManager();
        }
    }

    public GeneticsManager getInstance() {
        return instance;
    }

    public void loadGenomeToCrop(Genome genome, ICrop crop) {
        genome.loadCrop(crop);
    }

    public void deleteGenomeCrop(ICrop crop, final GeneticTraits geneticTraits) {
        switch (geneticTraits) {
            case SUN:
                crop.setSun(false);
                break;
            case BIOME:
            case BIOME_I:
            case BIOME_II:
            case BIOME_III:
            case BIOME_IV:
                final List<Biome> biomes = geneticBiomes.get(geneticTraits);
                biomes.forEach(crop::removeBiome);
                break;
            case AIR_I:
            case AIR_II:
            case AIR_III:
                crop.setAirRequirements(LevelPollution.LOW);
                break;
            case SOIL_I:
            case SOIL_II:
            case SOIL_III:
                crop.setSoilRequirements(LevelPollution.LOW);
                break;
            case WATER:
                crop.setWaterRequirement(crop.getDefaultWaterRequirement());
                break;
            case PEST_I:
            case PEST_II:
            case PEST_III:
                crop.setPestResistance(crop.getDefaultPestResistance());
                break;
            case WEED_I:
            case WEED_II:
            case WEED_III:
                crop.addChanceWeed(geneticTraits.getValue(Integer.class) * -1);
                break;
            case LIGHT_I:
            case LIGHT_II:
            case LIGHT_III:
            case LIGHT_IV:
                crop.setLight(crop.getDefaultLightLevel());
                break;
            case RADIATION_I:
            case RADIATION_II:
            case RADIATION_III:
                crop.setRadiationRequirements(EnumLevelRadiation.LOW);
                break;
            case SEED_I:
            case SEED_II:
            case SEED_III:
                crop.addSizeSeed(-1 * geneticTraits.getValue(Integer.class));
                break;
            case YIELD_I:
            case YIELD_II:
            case YIELD_III:
                crop.setYield(crop.getYield() - geneticTraits.getValue(Integer.class));
                break;
            case BEECOMBINE:
                crop.setBeeCombine(false);
                break;
            case NIGHT_GROW:
                crop.setNight(false);
                break;
            case SOIL_BLOCK:
                crop.setIgnoreSoil(false);
                break;
            case CHANCE_I:
            case CHANCE_II:
            case CHANCE_III:
                crop.setChance(crop.getChance() - geneticTraits.getValue(Integer.class));
                break;
            case GROW_SPEED_I:
            case GROW_SPEED_II:
            case GROW_SPEED_III:
                crop.setGrowthSpeed(crop.getGrowthSpeed() - geneticTraits.getValue(Double.class));
                break;
            case WEATHER_I:
            case WEATHER_II:
                crop.setWeatherResistance(crop.getDefaultWeatherResistance());
                break;
            case GENOME_ADAPTIVE_I:
            case GENOME_ADAPTIVE_II:
            case GENOME_ADAPTIVE_III:
                crop.setGenomeAdaptive(crop.getGenomeAdaptive() - geneticTraits.getValue(Integer.class));
                break;
            case GENOME_RESISTANCE_I:
            case GENOME_RESISTANCE_II:
            case GENOME_RESISTANCE_III:
                crop.setGenomeResistance(crop.getGenomeResistance() - geneticTraits.getValue(Integer.class));
                break;
        }
    }


    public void addGenomeCrop(ICrop crop, GeneticTraits geneticTraits) {
        switch (geneticTraits) {
            case SUN:
                crop.setSun(true);
                break;
            case BIOME:
            case BIOME_I:
            case BIOME_II:
            case BIOME_III:
            case BIOME_IV:
                final List<Biome> biomes = geneticBiomes.get(geneticTraits);
                biomes.forEach(crop::addBiome);
                break;
            case AIR_I:
            case AIR_II:
            case AIR_III:
                crop.setAirRequirements(geneticTraits.getValue(LevelPollution.class));
                break;
            case SOIL_I:
            case SOIL_II:
            case SOIL_III:
                crop.setSoilRequirements(geneticTraits.getValue(LevelPollution.class));
                break;
            case WATER:
                crop.setWaterRequirement(geneticTraits.getValue(Integer.class));
                break;
            case PEST_I:
            case PEST_II:
            case PEST_III:
                crop.setPestResistance(geneticTraits.getValue(Integer.class));
                break;
            case WEED_I:
            case WEED_II:
            case WEED_III:
                crop.addChanceWeed(geneticTraits.getValue(Integer.class));
                break;
            case LIGHT_I:
            case LIGHT_II:
            case LIGHT_III:
            case LIGHT_IV:
                crop.setLight(geneticTraits.getValue(Integer.class));
                break;
            case RADIATION_I:
            case RADIATION_II:
            case RADIATION_III:
                crop.setRadiationRequirements(geneticTraits.getValue(EnumLevelRadiation.class));
                break;
            case SEED_I:
            case SEED_II:
            case SEED_III:
                crop.addSizeSeed(geneticTraits.getValue(Integer.class));
                break;
            case YIELD_I:
            case YIELD_II:
            case YIELD_III:
                crop.setYield(crop.getYield() + geneticTraits.getValue(Integer.class));
                break;
            case BEECOMBINE:
                crop.setBeeCombine(true);
                break;
            case NIGHT_GROW:
                crop.setNight(true);
                break;
            case SOIL_BLOCK:
                crop.setIgnoreSoil(true);
                break;
            case CHANCE_I:
            case CHANCE_II:
            case CHANCE_III:
                crop.setChance(crop.getChance() + geneticTraits.getValue(Integer.class));
                break;
            case GROW_SPEED_I:
            case GROW_SPEED_II:
            case GROW_SPEED_III:
                crop.setGrowthSpeed(crop.getGrowthSpeed() + geneticTraits.getValue(Integer.class));
                break;
            case WEATHER_I:
            case WEATHER_II:
                crop.setWeatherResistance(geneticTraits.getValue(Integer.class));
                break;
            case GENOME_ADAPTIVE_I:
            case GENOME_ADAPTIVE_II:
            case GENOME_ADAPTIVE_III:
                crop.setGenomeAdaptive(crop.getGenomeAdaptive() + geneticTraits.getValue(Integer.class));
                break;
            case GENOME_RESISTANCE_I:
            case GENOME_RESISTANCE_II:
            case GENOME_RESISTANCE_III:
                crop.setGenomeResistance(crop.getGenomeResistance() + geneticTraits.getValue(Integer.class));
                break;
        }
    }

}

