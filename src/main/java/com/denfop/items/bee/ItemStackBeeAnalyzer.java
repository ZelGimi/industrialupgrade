package com.denfop.items.bee;



import com.denfop.api.bee.IBee;
import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.api.pollution.LevelPollution;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerBeeAnalyzer;
import com.denfop.container.ContainerLeadBox;
import com.denfop.gui.GuiAgriculturalAnalyzer;
import com.denfop.gui.GuiBeeAnalyzer;
import com.denfop.gui.GuiLeadBox;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import com.denfop.items.energy.ItemMagnet;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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


    public ItemStackBeeAnalyzer(EntityPlayer player, ItemStack stack, int inventorySize) {
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
        NBTTagList contentList = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (!ModUtils.isEmpty(this.inventory[i])) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbt);
                contentList.appendTag(nbt);
            }
        }

        ModUtils.nbt(stack).setTag("Items", contentList);
        this.clear();
    }

    public ContainerBeeAnalyzer getGuiContainer(EntityPlayer player) {
        return new ContainerBeeAnalyzer(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiBeeAnalyzer getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBeeAnalyzer(getGuiContainer(player), itemStack1);
    }

    @Override
    public TileEntityInventory getParent() {
        return null;
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


    public int add(Collection<ItemStack> stacks) {
        return this.add(stacks, false);
    }

    public boolean add(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), false);
        }
    }

    public boolean canAdd(Collection<ItemStack> stacks) {
        return this.add(stacks, true) == 0;
    }

    public boolean canAdd(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null ItemStack");
        } else {
            return this.add(Collections.singletonList(stack), true);
        }
    }

    protected ItemStack[] backup() {
        ItemStack[] ret = new ItemStack[this.inventory.length];

        for (int i = 0; i < this.inventory.length; ++i) {
            ItemStack content = this.inventory[i];
            ret[i] = ModUtils.isEmpty(content) ? ModUtils.emptyStack : content.copy();
        }

        return ret;
    }

    public void put(ItemStack content) {
        this.put(0, content);
    }

    public void put(int index, ItemStack content) {
        if (ModUtils.isEmpty(content)) {
            content = ModUtils.emptyStack;
        }

        this.inventory[index] = content;
        this.save();
    }

    public int getStackSizeLimit() {
        return 64;
    }

    private boolean add(List<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {

            for (ItemStack stack : stacks) {
                for (int i = 0; i < this.inventory.length; i++) {
                    if (this.get(i) == null || this.get(i).isEmpty()) {
                        if (!simulate) {
                            this.put(i, stack.copy());

                        }
                        return true;
                    } else {
                        if (this.get(i).isItemEqual(stack)) {
                            if (this.get(i).getCount() + stack.getCount() <= stack.getMaxStackSize()) {
                                if (stack.getTagCompound() == null && this.get(i).getTagCompound() == null) {
                                    if (!simulate) {
                                        this.get(i).grow(stack.getCount());
                                    }
                                    return true;
                                } else {
                                    if (stack.getTagCompound() != null &&
                                            stack.getTagCompound().equals(this.get(i).getTagCompound())) {
                                        if (!simulate) {
                                            this.get(i).grow(stack.getCount());

                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    private int add(Collection<ItemStack> stacks, boolean simulate) {
        if (stacks != null && !stacks.isEmpty()) {
            ItemStack[] backup = simulate ? this.backup() : null;
            int totalAmount = 0;
            Iterator var5 = stacks.iterator();

            while (true) {
                ItemStack stack;
                int amount;
                do {
                    if (!var5.hasNext()) {
                        if (simulate) {
                            this.restore(backup);
                        }

                        return totalAmount;
                    }

                    stack = (ItemStack) var5.next();
                    amount = ModUtils.getSize(stack);
                } while (amount <= 0);

                label74:
                for (int pass = 0; pass < 2; ++pass) {
                    for (int i = 0; i < this.inventorySize; ++i) {
                        ItemStack existingStack = this.get(i);
                        int space = this.getStackSizeLimit();
                        if (!ModUtils.isEmpty(existingStack)) {
                            space = Math.min(space, existingStack.getMaxStackSize()) - ModUtils.getSize(existingStack);
                        }

                        if (space > 0) {
                            if (pass == 0 && !ModUtils.isEmpty(existingStack) && ModUtils.checkItemEqualityStrict(
                                    stack,
                                    existingStack
                            )) {
                                if (space >= amount) {
                                    existingStack.grow(amount);
                                    this.put(i, existingStack);
                                    amount = 0;
                                    break label74;
                                }
                                existingStack.grow(space);
                                this.put(i, existingStack);
                                amount -= space;
                            } else if (pass == 1 && ModUtils.isEmpty(existingStack)) {
                                if (space >= amount) {
                                    this.put(i, ModUtils.setSize(stack, amount));
                                    amount = 0;
                                    break label74;
                                }

                                this.put(i, ModUtils.setSize(stack, space));
                                amount -= space;
                            }
                        }
                    }
                }

                totalAmount += amount;
            }
        } else {
            return 0;
        }
    }

}
