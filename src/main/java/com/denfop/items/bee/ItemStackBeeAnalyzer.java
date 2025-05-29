package com.denfop.items.bee;


import com.denfop.api.bee.IBee;
import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBeeAnalyzer;
import com.denfop.gui.GuiBeeAnalyzer;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemStackBeeAnalyzer extends ItemStackInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;
    public Genome genome;
    public IBee crop;
    public int weatherGenome = 0;
    public double pestGenome = 1;
    public double birthRateGenome = 1;
    public double radiusGenome = 1;
    public double populationGenome = 1;
    public double foodGenome = 1;
    public double jellyGenome = 1;
    public double productGenome = 1;
    public double hardeningGenome = 1;
    public double swarmGenome = 1;
    public double mortalityGenome = 1;
    public boolean sunGenome = false;
    public boolean nightGenome = false;
    public int genomeResistance = 0;
    public int genomeAdaptive = 0;
    public LevelPollution airPollution = LevelPollution.LOW;
    public LevelPollution soilPollution = LevelPollution.LOW;
    public EnumLevelRadiation radiationPollution = EnumLevelRadiation.LOW;


    public ItemStackBeeAnalyzer(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return itemstack.getItem() instanceof ItemJarBees;
    }

    public void set() {
        reset();
        if (genome == null) {
            return;
        }

        if (genome.hasGenome(EnumGenetic.WEATHER)) {
            this.weatherGenome = genome.getLevelGenome(EnumGenetic.WEATHER, Integer.class);
        }
        if (genome.hasGenome(EnumGenetic.PEST)) {
            this.pestGenome = genome.getLevelGenome(EnumGenetic.PEST, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.BIRTH)) {
            this.birthRateGenome = genome.getLevelGenome(EnumGenetic.BIRTH, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.RADIUS)) {
            this.radiusGenome = genome.getLevelGenome(EnumGenetic.RADIUS, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.POPULATION)) {
            this.populationGenome = genome.getLevelGenome(EnumGenetic.POPULATION, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.FOOD)) {
            this.foodGenome = genome.getLevelGenome(EnumGenetic.FOOD, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.JELLY)) {
            this.jellyGenome = genome.getLevelGenome(EnumGenetic.JELLY, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.PRODUCT)) {
            this.productGenome = genome.getLevelGenome(EnumGenetic.PRODUCT, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.HARDENING)) {
            this.hardeningGenome = genome.getLevelGenome(EnumGenetic.HARDENING, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.SWARM)) {
            this.swarmGenome = genome.getLevelGenome(EnumGenetic.SWARM, Double.class);
        }
        if (genome.hasGenome(EnumGenetic.MORTALITY_RATE)) {
            this.mortalityGenome = genome.getLevelGenome(EnumGenetic.MORTALITY_RATE, Double.class);
        }


        if (genome.hasGenome(EnumGenetic.SUN)) {
            this.sunGenome = genome.getLevelGenome(EnumGenetic.SUN, Boolean.class);
        }
        if (genome.hasGenome(EnumGenetic.NIGHT)) {
            this.nightGenome = genome.getLevelGenome(EnumGenetic.NIGHT, Boolean.class);
        }


        if (genome.hasGenome(EnumGenetic.AIR)) {
            this.airPollution = genome.getLevelGenome(EnumGenetic.AIR, LevelPollution.class);
        }
        if (genome.hasGenome(EnumGenetic.SOIL)) {
            this.soilPollution = genome.getLevelGenome(EnumGenetic.SOIL, LevelPollution.class);
        }
        if (genome.hasGenome(EnumGenetic.RADIATION)) {
            this.radiationPollution = genome.getLevelGenome(EnumGenetic.RADIATION, EnumLevelRadiation.class);
        }

        if (genome.hasGenome(EnumGenetic.GENOME_RESISTANCE)) {
            this.genomeResistance = genome.getLevelGenome(EnumGenetic.GENOME_RESISTANCE, Integer.class);
        }
        if (genome.hasGenome(EnumGenetic.GENOME_ADAPTIVE)) {
            this.genomeAdaptive = genome.getLevelGenome(EnumGenetic.GENOME_ADAPTIVE, Integer.class);
        }
    }


    public void reset() {
        this.weatherGenome = 0;
        this.pestGenome = 1;
        this.swarmGenome = 1;
        this.mortalityGenome = 1;
        this.birthRateGenome = 1;
        this.radiusGenome = 1;
        this.populationGenome = 1;
        this.foodGenome = 1;
        this.jellyGenome = 1;
        this.productGenome = 1;
        this.hardeningGenome = 1;
        this.sunGenome = false;
        this.nightGenome = false;
        this.genomeResistance = 0;
        this.genomeAdaptive = 0;
        this.airPollution = LevelPollution.LOW;
        this.soilPollution = LevelPollution.LOW;
        this.radiationPollution = EnumLevelRadiation.LOW;
    }

    public void save() {
        super.save();
    }

    public void saveAndThrow(ItemStack stack) {
        ListTag contentList = new ListTag();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                CompoundTag nbt = new CompoundTag();
                nbt.putByte("Slot", (byte) i);
                this.inventory[i].save(nbt);
                contentList.add(nbt);
            }
        }

        ModUtils.nbt(stack).put("Items", contentList);
        this.clear();
    }

    public ContainerBeeAnalyzer getGuiContainer(Player player) {
        return new ContainerBeeAnalyzer(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GuiBeeAnalyzer((ContainerBeeAnalyzer) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
        return 0;
    }


    public ItemStack get(int index) {
        return this.inventory[index];
    }

    protected void restore(ItemStack[] backup) {
        if (backup.length != this.inventory.length) {
            throw new IllegalArgumentException("invalid array size");
        } else {
            System.arraycopy(backup, 0, this.inventory, 0, this.inventory.length);

        }
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }




}
