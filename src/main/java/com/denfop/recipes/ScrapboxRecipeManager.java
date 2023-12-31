package com.denfop.recipes;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScrapboxRecipeManager {

    public static ScrapboxRecipeManager instance;
    public final List<Drop> drops = new ArrayList<>();


    public ScrapboxRecipeManager() {
        instance = this;
        this.addBuiltinDrops();
    }

    public boolean addRecipe(IInputItemStack input, Collection<ItemStack> output, NBTTagCompound metadata, boolean replace) {
        if (!input.matches(IUItem.scrapBox)) {
            throw new IllegalArgumentException("currently only scrap boxes are supported");
        } else if (metadata != null && metadata.hasKey("weight")) {
            if (output.size() != 1) {
                throw new IllegalArgumentException("currently only a single drop stack is supported");
            } else {
                float weight = metadata.getFloat("weight");
                if (!(weight <= 0.0F) && !Float.isInfinite(weight) && !Float.isNaN(weight)) {
                    this.addDrop(output.iterator().next(), weight);
                    return true;
                } else {
                    throw new IllegalArgumentException("invalid weight");
                }
            }
        } else {
            throw new IllegalArgumentException("no weight metadata");
        }
    }

    public boolean addRecipe(IInputItemStack input, NBTTagCompound metadata, boolean replace, ItemStack... outputs) {
        return this.addRecipe(input, Arrays.asList(outputs), metadata, replace);
    }

    public ItemStack apply(ItemStack input) {
        if (!ModUtils.isEmpty(input) && input.isItemEqual(
                IUItem.scrapBox
        )) {
            if (this.drops.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                float chance = IUCore.random.nextFloat() * ScrapboxRecipeManager.Drop.topChance;
                int low = 0;
                int high = this.drops.size() - 1;

                while (low < high) {
                    int mid = (high + low) / 2;
                    if (chance < this.drops.get(mid).upperChanceBound) {
                        high = mid;
                    } else {
                        low = mid + 1;
                    }
                }

                return this.drops.get(low).item.copy();
            }
        } else {
            return ItemStack.EMPTY;
        }
    }


    public boolean isIterable() {
        return false;
    }

    public void addDrop(ItemStack drop, float rawChance) {
        this.drops.add(new Drop(drop, rawChance));
    }

    public ItemStack getDrop(ItemStack input) {
        return this.apply(input);
    }

    public Map<ItemStack, Float> getDrops() {
        Map<ItemStack, Float> ret = new HashMap<>(this.drops.size());

        for (final Drop drop : this.drops) {
            ret.put(drop.item, drop.originalChance / Drop.topChance);
        }

        return ret;
    }

    private void addBuiltinDrops() {
        this.addDrop(Items.WOODEN_HOE, 5.01F);
        this.addDrop(Blocks.DIRT, 5.0F);
        this.addDrop(Items.STICK, 4.0F);
        this.addDrop(Blocks.GRASS, 3.0F);
        this.addDrop(Blocks.GRAVEL, 3.0F);
        this.addDrop(Blocks.NETHERRACK, 2.0F);
        this.addDrop(Items.ROTTEN_FLESH, 2.0F);
        this.addDrop(Items.APPLE, 1.5F);
        this.addDrop(Items.BREAD, 1.5F);
        this.addDrop(Items.WOODEN_SWORD, 1.0F);
        this.addDrop(Items.WOODEN_SHOVEL, 1.0F);
        this.addDrop(Items.WOODEN_PICKAXE, 1.0F);
        this.addDrop(Blocks.SOUL_SAND, 1.0F);
        this.addDrop(Items.SIGN, 1.0F);
        this.addDrop(Items.LEATHER, 1.0F);
        this.addDrop(Items.FEATHER, 1.0F);
        this.addDrop(Items.BONE, 1.0F);
        this.addDrop(Items.COOKED_PORKCHOP, 0.9F);
        this.addDrop(Items.COOKED_BEEF, 0.9F);
        this.addDrop(Blocks.PUMPKIN, 0.9F);
        this.addDrop(Items.COOKED_CHICKEN, 0.9F);
        this.addDrop(Items.MINECART, 0.01F);
        this.addDrop(Items.REDSTONE, 0.9F);
        this.addDrop(IUItem.rubber, 0.8F);
        this.addDrop(Items.GLOWSTONE_DUST, 0.8F);
        this.addDrop(IUItem.coalDust, 0.8F);
        this.addDrop(IUItem.copperDust, 0.8F);
        this.addDrop(IUItem.tinDust, 0.8F);
        this.addDrop(IUItem.ironDust, 0.7F);
        this.addDrop(IUItem.goldDust, 0.7F);
        this.addDrop(Items.SLIME_BALL, 0.6F);
        this.addDrop(Blocks.IRON_ORE, 0.5F);
        this.addDrop(Items.GOLDEN_HELMET, 0.01F);
        this.addDrop(Blocks.GOLD_ORE, 0.5F);
        this.addDrop(Items.CAKE, 0.5F);
        this.addDrop(Items.DIAMOND, 0.1F);
        this.addDrop(Items.EMERALD, 0.05F);
        this.addDrop(Items.ENDER_PEARL, 0.08F);
        this.addDrop(Items.BLAZE_ROD, 0.04F);
        this.addDrop(Items.EGG, 0.8F);
        this.addDrop(IUItem.classic_ore.getItemStack(BlockClassicOre.Type.copper), 0.7F);
        this.addDrop(IUItem.classic_ore.getItemStack(BlockClassicOre.Type.tin), 0.7F);
    }

    private void addDrop(Block block, float rawChance) {
        this.addDrop(new ItemStack(block), rawChance);
    }

    private void addDrop(Item item, float rawChance) {
        this.addDrop(new ItemStack(item), rawChance);
    }

    private static class Drop {

        static float topChance;
        final ItemStack item;
        final float originalChance;
        final float upperChanceBound;

        Drop(ItemStack item, float chance) {
            this.item = item;
            this.originalChance = chance;
            this.upperChanceBound = topChance += chance;
        }

    }

}
