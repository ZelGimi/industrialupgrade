package com.denfop.api.agriculture.genetics;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.GenomeCrop;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.*;

public class Genome implements IGenome {

    public static Map<GeneticTraits, List<ResourceKey<Biome>>> geneticBiomes = new HashMap<>();
    Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>();
    private ItemStack stack;

    public Genome(ItemStack stack) {
        if (!stack.has(DataComponentsInit.GENOME_CROP)) {
            stack.set(DataComponentsInit.GENOME_CROP, new GenomeCrop(new HashMap<>()));
        }
        GenomeCrop genomeBee = stack.get(DataComponentsInit.GENOME_CROP);

        geneticTraitsMap = genomeBee.geneticTraitsMap();
        this.stack = stack;
    }

    public Genome(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {
        this.geneticTraitsMap = new HashMap<>(geneticTraitsMap);
    }

    private static GeneticTraits getTemperatureCategory(Holder<Biome> biomeHolder) {
        if (biomeHolder.is(Tags.Biomes.IS_SNOWY) || biomeHolder.is(BiomeTags.IS_HILL) || biomeHolder.is(BiomeTags.IS_MOUNTAIN)) {
            return GeneticTraits.BIOME_IV;
        } else if (biomeHolder.is(BiomeTags.IS_OCEAN) || biomeHolder.is(BiomeTags.IS_DEEP_OCEAN) || biomeHolder.is(BiomeTags.IS_TAIGA) || biomeHolder.is(BiomeTags.IS_BADLANDS)) {
            return GeneticTraits.BIOME_III;
        } else if (biomeHolder.is(BiomeTags.IS_OCEAN) || biomeHolder.is(BiomeTags.IS_DEEP_OCEAN) || biomeHolder.is(BiomeTags.IS_FOREST) || biomeHolder.is(Tags.Biomes.IS_PLAINS) || biomeHolder.is(BiomeTags.IS_RIVER) || biomeHolder.is(BiomeTags.IS_BEACH)) {
            return GeneticTraits.BIOME;
        } else if (biomeHolder.is(BiomeTags.IS_JUNGLE) || biomeHolder.is(Tags.Biomes.IS_SWAMP)) {
            return GeneticTraits.BIOME_I;
        } else if (biomeHolder.is(BiomeTags.IS_SAVANNA) || biomeHolder.is(Tags.Biomes.IS_DESERT)) {
            return GeneticTraits.BIOME_II;
        } else {
            return null;
        }
    }

    public static void init(Registry<Biome> biomeRegistry) {

        for (Map.Entry<ResourceKey<Biome>, Biome> biome : biomeRegistry.entrySet()) {
            GeneticTraits geneticTraits = getTemperatureCategory(biomeRegistry.getHolderOrThrow(biome.getKey()));
            List<ResourceKey<Biome>> biomes = geneticBiomes.get(geneticTraits);
            if (biomes != null) {
                biomes.add(biome.getKey());
            } else {
                biomes = new ArrayList<>();
                biomes.add(biome.getKey());
                if (geneticTraits != null)
                    geneticBiomes.put(geneticTraits, biomes);
            }
        }
    }

    public Map<EnumGenetic, GeneticTraits> getGeneticTraitsMap() {
        return geneticTraitsMap;
    }

    public ItemStack getStack() {
        return stack;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Genome genome = (Genome) o;
        return geneticTraitsMap.values().size() == genome.geneticTraitsMap.size() && checkGenomes(genome);
    }

    private boolean checkGenomes(Genome genome) {
        List<GeneticTraits> geneticTraits = new ArrayList<>(genome.geneticTraitsMap.values());
        List<GeneticTraits> geneticTraits1 = new ArrayList<>(this.geneticTraitsMap.values());
        geneticTraits1.removeIf(geneticTraits::contains);
        return geneticTraits1.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(geneticTraitsMap);
    }

    public void addGenome(GeneticTraits geneticTraits, ItemStack stack) {
        if (!geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(stack);
        }
    }

    public void addGenome(GeneticTraits geneticTraits) {
        if (!geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(stack);
        }
    }

    public void removeGenome(GeneticTraits geneticTraits, ItemStack stack) {
        if (geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.remove(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(stack);
        }
    }

    public GeneticTraits removeGenome(EnumGenetic genetic, ItemStack stack) {
        if (geneticTraitsMap.containsKey(genetic)) {
            final GeneticTraits value = geneticTraitsMap.remove(genetic);
            writeNBT(stack);
            return value;
        }
        return null;
    }

    public void writeNBT(ItemStack stack) {
        Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>(this.geneticTraitsMap);
        stack.set(DataComponentsInit.GENOME_CROP, new GenomeCrop(geneticTraitsMap));
    }

    @Override
    public boolean hasGenome(final EnumGenetic genome) {
        return geneticTraitsMap.containsKey(genome);
    }

    @Override
    public <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass) {
        return geneticTraitsMap.get(genome).getValue(tClass);
    }

    public GeneticTraits getGenome(final EnumGenetic genome) {
        return geneticTraitsMap.get(genome);
    }

    public Genome copy() {
        Genome genome = new Genome(this.geneticTraitsMap);
        genome.stack = this.stack.copy();
        return genome;
    }

    public void loadCrop(ICrop crop) {
        for (GeneticTraits geneticTraits : this.geneticTraitsMap.values()) {
            switch (geneticTraits) {
                case SUN:
                    crop.setSun(true);
                    break;
                case BIOME:
                case BIOME_I:
                case BIOME_II:
                case BIOME_III:
                case BIOME_IV:
                    List<ResourceKey<Biome>> biomes = geneticBiomes.get(geneticTraits);
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
                    crop.setWaterRequirement(0);
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
                    crop.setYield(geneticTraits.getValue(Integer.class));
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
                    crop.setChance(geneticTraits.getValue(Integer.class));
                    break;
                case GROW_SPEED_I:
                case GROW_SPEED_II:
                case GROW_SPEED_III:
                    crop.setGrowthSpeed(geneticTraits.getValue(Double.class));
                    break;
                case WEATHER_I:
                case WEATHER_II:
                    crop.setWeatherResistance(geneticTraits.getValue(Integer.class));
                    break;
                case GENOME_ADAPTIVE_I:
                case GENOME_ADAPTIVE_II:
                case GENOME_ADAPTIVE_III:
                    crop.setGenomeAdaptive(geneticTraits.getValue(Integer.class));
                    break;
                case GENOME_RESISTANCE_I:
                case GENOME_RESISTANCE_II:
                case GENOME_RESISTANCE_III:
                    crop.setGenomeResistance(geneticTraits.getValue(Integer.class));
                    break;
            }
        }
    }

}
