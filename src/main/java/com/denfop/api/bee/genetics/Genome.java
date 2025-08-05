package com.denfop.api.bee.genetics;

import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.GenomeBee;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Genome implements IGenome {

    Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>();
    private ItemStack stack;

    public Genome(ItemStack stack) {
        if (!stack.has(DataComponentsInit.GENOME_BEE)) {
            stack.set(DataComponentsInit.GENOME_BEE, new GenomeBee(new HashMap<>()));
        }
        GenomeBee genomeBee = stack.get(DataComponentsInit.GENOME_BEE);

        geneticTraitsMap = genomeBee.geneticTraitsMap();
        this.stack = stack;
    }

    public Genome(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {
        this.geneticTraitsMap = new HashMap<>(geneticTraitsMap);
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

    @Override
    public void writeNBT(ItemStack stack) {
        Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>(this.geneticTraitsMap);
        stack.set(DataComponentsInit.GENOME_BEE, new GenomeBee(geneticTraitsMap));
    }


}
