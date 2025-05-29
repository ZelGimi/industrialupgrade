package com.denfop.api.bee.genetics;

import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Genome implements IGenome {

    private ItemStack stack;
    Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>();

    public Genome(ItemStack stack) {
        final CompoundTag nbt1 = ModUtils.nbt(stack);
        if (!nbt1.contains("genome")){
            final CompoundTag nbt = new CompoundTag();
            nbt1.put("genome",nbt);
        }

        CompoundTag  nbt = nbt1.getCompound("genome");
        ListTag tagList = nbt.getList("genomeList", 10);
        for (int i = 0; i < tagList.size(); ++i) {
            CompoundTag genomeNbt = tagList.getCompound(i);
            int meta = genomeNbt.getByte("meta");
            GeneticTraits geneticTraits = GeneticTraits.values()[meta];
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
        }
        this.stack=stack;
    }

    public Map<EnumGenetic, GeneticTraits> getGeneticTraitsMap() {
        return geneticTraitsMap;
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
    public CompoundTag writeNBT(CompoundTag nbtTagCompound) {
        ListTag genomeNBT = new ListTag();
        for (GeneticTraits geneticTraits : geneticTraitsMap.values()) {
            CompoundTag nbtTagCompound1 = new CompoundTag();
            nbtTagCompound1.putByte("meta", (byte) geneticTraits.ordinal());
            genomeNBT.add(nbtTagCompound1);
        }
        CompoundTag nbt = nbtTagCompound.getCompound("genome");
        nbt.put("genomeList", genomeNBT);
        nbtTagCompound.put("genome",nbt);
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



}
