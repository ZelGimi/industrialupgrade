package com.denfop.api.bee.genetics;

import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.GenomeBee;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Genome implements GenomeBase {

    private Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new EnumMap<>(EnumGenetic.class);
    private ItemStack stack = ItemStack.EMPTY;

    public Genome(ItemStack stack) {
        this.stack = stack == null ? ItemStack.EMPTY : stack;

        if (!this.stack.isEmpty() && !this.stack.has(DataComponentsInit.GENOME_BEE)) {
            this.stack.set(DataComponentsInit.GENOME_BEE, new GenomeBee(new EnumMap<>(EnumGenetic.class)));
        }

        GenomeBee genomeBee = this.stack.isEmpty() ? null : this.stack.get(DataComponentsInit.GENOME_BEE);
        this.geneticTraitsMap = copyToEnumMap(genomeBee == null ? null : genomeBee.geneticTraitsMap());
    }

    public Genome(Map<EnumGenetic, GeneticTraits> geneticTraitsMap) {
        this.geneticTraitsMap = copyToEnumMap(geneticTraitsMap);
    }

    private static EnumMap<EnumGenetic, GeneticTraits> copyToEnumMap(Map<EnumGenetic, GeneticTraits> source) {
        EnumMap<EnumGenetic, GeneticTraits> result = new EnumMap<>(EnumGenetic.class);
        if (source != null && !source.isEmpty()) {
            result.putAll(source);
        }
        return result;
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
        if (!(o instanceof Genome genome)) {
            return false;
        }
        return Objects.equals(this.geneticTraitsMap, genome.geneticTraitsMap);
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
        if (geneticTraits != null && !geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(stack);
        }
    }

    public void addGenome(GeneticTraits geneticTraits) {
        if (geneticTraits != null && !geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
            writeNBT(this.stack);
        }
    }

    public void removeGenome(GeneticTraits geneticTraits, ItemStack stack) {
        if (geneticTraits != null && geneticTraitsMap.containsKey(geneticTraits.getGenetic())) {
            geneticTraitsMap.remove(geneticTraits.getGenetic());
            writeNBT(stack);
        }
    }

    public GeneticTraits removeGenome(EnumGenetic genetic, ItemStack stack) {
        if (genetic != null && geneticTraitsMap.containsKey(genetic)) {
            GeneticTraits value = geneticTraitsMap.remove(genetic);
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
        GeneticTraits traits = geneticTraitsMap.get(genome);
        return traits == null ? null : traits.getValue(tClass);
    }

    public GeneticTraits getGenome(final EnumGenetic genome) {
        return geneticTraitsMap.get(genome);
    }

    public Genome copy() {
        Genome genome = new Genome(this.geneticTraitsMap);
        genome.stack = this.stack == null ? ItemStack.EMPTY : this.stack.copy();
        return genome;
    }

    @Override
    public void writeNBT(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        stack.set(DataComponentsInit.GENOME_BEE, new GenomeBee(copyToEnumMap(this.geneticTraitsMap)));
    }
}