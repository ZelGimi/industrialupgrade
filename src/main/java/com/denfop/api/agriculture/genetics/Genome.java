package com.denfop.api.agriculture.genetics;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Genome implements IGenome {

    public static Map<GeneticTraits, List<Biome>> geneticBiomes = new HashMap<>();
    private ItemStack stack;
    Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>();

    public Genome(ItemStack stack) {
        final NBTTagCompound nbt1 = ModUtils.nbt(stack);
        if (!nbt1.hasKey("genome")){
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt1.setTag("genome",nbt);
        }
        NBTTagCompound  nbt = nbt1.getCompoundTag("genome");
        NBTTagList tagList = nbt.getTagList("genomeList", 10);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound genomeNbt = tagList.getCompoundTagAt(i);
            int meta = genomeNbt.getByte("meta");
            GeneticTraits geneticTraits = GeneticTraits.values()[meta];
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
        }
        this.stack=stack;
    }

    public Map<EnumGenetic, GeneticTraits> getGeneticTraitsMap() {
        return geneticTraitsMap;
    }

    private static GeneticTraits getTemperatureCategory(Biome biome) {
        if (biome.getDefaultTemperature() < 0.1F) {
            return GeneticTraits.BIOME_IV;
        } else if (biome.getDefaultTemperature() < 0.5F) {
            return GeneticTraits.BIOME_III;
        } else if (biome.getDefaultTemperature() < 1.0F) {
            return GeneticTraits.BIOME;
        } else if (biome.getDefaultTemperature() < 1.5F) {
            return GeneticTraits.BIOME_I;
        } else {
            return GeneticTraits.BIOME_II;
        }
    }
    public static void init() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            GeneticTraits geneticTraits = getTemperatureCategory(biome);
            List<Biome> biomes = geneticBiomes.get(geneticTraits);
            if (biomes != null) {
                biomes.add(biome);
            }else{
                biomes = new ArrayList<>();
                biomes.add(biome);
                geneticBiomes.put(geneticTraits,biomes);
            }
        }
    }

    public ItemStack getStack() {
        return stack;
    }

    public Genome(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {
        this.geneticTraitsMap = new HashMap<>(geneticTraitsMap);
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
            writeNBT(ModUtils.nbt(stack));
        }
    }
    public void addGenome(GeneticTraits geneticTraits) {
        if (!geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(ModUtils.nbt(stack));
        }
    }
    public void removeGenome(GeneticTraits geneticTraits, ItemStack stack) {
        if (geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.remove(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(ModUtils.nbt(stack));
        }
    }
    public GeneticTraits removeGenome(EnumGenetic genetic, ItemStack stack) {
        if (geneticTraitsMap.containsKey(genetic)) {
            final GeneticTraits value = geneticTraitsMap.remove(genetic);
            writeNBT(ModUtils.nbt(stack));
            return value;
        }
        return null;
    }
    public NBTTagCompound writeNBT(NBTTagCompound nbtTagCompound) {
        NBTTagList genomeNBT = new NBTTagList();
        for (GeneticTraits geneticTraits : geneticTraitsMap.values()) {
            NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
            nbtTagCompound1.setByte("meta", (byte) geneticTraits.ordinal());
            genomeNBT.appendTag(nbtTagCompound1);
        }
        NBTTagCompound nbt = nbtTagCompound.getCompoundTag("genome");
        nbt.setTag("genomeList", genomeNBT);
        nbtTagCompound.setTag("genome",nbt);
        return nbtTagCompound;
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
                    List<Biome> biomes = geneticBiomes.get(geneticTraits);
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
