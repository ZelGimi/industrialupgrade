package com.denfop.api.bee.genetics;

import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Genome implements IGenome {

    Map<EnumGenetic, GeneticTraits> geneticTraitsMap = new HashMap<>();
    private ItemStack stack;

    public Genome(ItemStack stack) {
        final NBTTagCompound nbt1 = ModUtils.nbt(stack);
        if (!nbt1.hasKey("genome")) {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt1.setTag("genome", nbt);
        }

        NBTTagCompound nbt = nbt1.getCompoundTag("genome");
        NBTTagList tagList = nbt.getTagList("genomeList", 10);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound genomeNbt = tagList.getCompoundTagAt(i);
            int meta = genomeNbt.getByte("meta");
            GeneticTraits geneticTraits = GeneticTraits.values()[meta];
            geneticTraitsMap.put(geneticTraits.getGenetic(), geneticTraits);
        }
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
        nbtTagCompound.setTag("genome", nbt);
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
