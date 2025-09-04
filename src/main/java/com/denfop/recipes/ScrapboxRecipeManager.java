package com.denfop.recipes;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public final class ScrapboxRecipeManager {

    public static ScrapboxRecipeManager instance;
    public final List<Drop> drops = new LinkedList<>();


    public ScrapboxRecipeManager() {
        instance = this;
        this.addBuiltinDrops();
    }

    public boolean addRecipe(IInputItemStack input, Collection<ItemStack> output, CompoundTag metadata, boolean replace) {
        if (!input.matches(IUItem.scrapBox)) {
            throw new IllegalArgumentException("currently only scrap boxes are supported");
        } else if (metadata != null && metadata.contains("weight")) {
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

    public boolean addRecipe(IInputItemStack input, CompoundTag metadata, boolean replace, ItemStack... outputs) {
        return this.addRecipe(input, Arrays.asList(outputs), metadata, replace);
    }

    public ItemStack apply(ItemStack input) {
        if (!ModUtils.isEmpty(input) && input.is(
                IUItem.scrapBox.getItem()
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

    public ItemStack getRandomDrop() {
        if (this.drops.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            float chance = WorldBaseGen.random.nextFloat() * ScrapboxRecipeManager.Drop.topChance;
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

            return this.drops.get(low).item;
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
        this.addDrop(Blocks.OAK_SIGN, 1.0F);
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
        this.addDrop(Blocks.COPPER_ORE, 0.7F);
        this.addDrop(IUItem.classic_ore.getItemStack(BlockClassicOre.Type.tin), 0.7F);


        this.addDrop(Blocks.EXPOSED_COPPER, 0.5F);
        this.addDrop(Blocks.WEATHERED_COPPER, 0.5F);
        this.addDrop(Blocks.OXIDIZED_COPPER, 0.4F);
        this.addDrop(Blocks.WAXED_COPPER_BLOCK, 0.6F);
        this.addDrop(Blocks.WAXED_EXPOSED_COPPER, 0.5F);
        this.addDrop(Blocks.WAXED_WEATHERED_COPPER, 0.4F);
        this.addDrop(Blocks.WAXED_OXIDIZED_COPPER, 0.3F);


        this.addDrop(Blocks.AMETHYST_BLOCK, 0.8F);
        this.addDrop(Blocks.BUDDING_AMETHYST, 0.6F);
        this.addDrop(Blocks.AMETHYST_CLUSTER, 0.4F);
        this.addDrop(Blocks.LARGE_AMETHYST_BUD, 0.3F);
        this.addDrop(Blocks.MEDIUM_AMETHYST_BUD, 0.2F);
        this.addDrop(Blocks.SMALL_AMETHYST_BUD, 0.1F);
        this.addDrop(Items.AMETHYST_SHARD, 0.7F);


        this.addDrop(Blocks.CALCITE, 1.0F);
        this.addDrop(Blocks.TUFF, 0.8F);
        this.addDrop(Blocks.SMOOTH_BASALT, 0.6F);


        this.addDrop(Items.HONEY_BOTTLE, 0.9F);
        this.addDrop(Blocks.HONEY_BLOCK, 0.8F);
        this.addDrop(Items.HONEYCOMB, 0.7F);
        this.addDrop(Blocks.HONEYCOMB_BLOCK, 0.6F);
        this.addDrop(Blocks.BEEHIVE, 0.4F);
        this.addDrop(Blocks.BEE_NEST, 0.4F);


        this.addDrop(Blocks.SCULK, 0.6F);
        this.addDrop(Blocks.SCULK_VEIN, 0.5F);
        this.addDrop(Blocks.SCULK_CATALYST, 0.7F);
        this.addDrop(Blocks.SCULK_SHRIEKER, 0.8F);
        this.addDrop(Blocks.SCULK_SENSOR, 0.7F);


        this.addDrop(Blocks.MANGROVE_LOG, 1.5F);
        this.addDrop(Blocks.STRIPPED_MANGROVE_LOG, 1.5F);
        this.addDrop(Blocks.MANGROVE_WOOD, 1.4F);
        this.addDrop(Blocks.STRIPPED_MANGROVE_WOOD, 1.4F);
        this.addDrop(Blocks.MANGROVE_PLANKS, 1.0F);
        this.addDrop(Blocks.MANGROVE_LEAVES, 0.6F);
        this.addDrop(Blocks.MANGROVE_PROPAGULE, 0.7F);
        this.addDrop(Blocks.MANGROVE_ROOTS, 0.5F);
        this.addDrop(Blocks.MUDDY_MANGROVE_ROOTS, 0.5F);

        this.addDrop(Blocks.MUD, 1.3F);
        this.addDrop(Blocks.PACKED_MUD, 0.9F);
        this.addDrop(Blocks.MUD_BRICKS, 1.0F);
        this.addDrop(Blocks.MUD_BRICK_STAIRS, 0.7F);
        this.addDrop(Blocks.MUD_BRICK_SLAB, 0.5F);
        this.addDrop(Blocks.MUD_BRICK_WALL, 0.7F);

        this.addDrop(Blocks.MANGROVE_DOOR, 0.8F);
        this.addDrop(Blocks.MANGROVE_TRAPDOOR, 0.8F);
        this.addDrop(Blocks.MANGROVE_FENCE, 0.7F);
        this.addDrop(Blocks.MANGROVE_FENCE_GATE, 0.7F);
        this.addDrop(Blocks.MANGROVE_SLAB, 0.5F);
        this.addDrop(Blocks.MANGROVE_STAIRS, 0.7F);
        this.addDrop(Items.MANGROVE_BOAT, 1.0F);


        this.addDrop(Blocks.HANGING_ROOTS, 0.3F);
        this.addDrop(Blocks.ROOTED_DIRT, 0.6F);


        this.addDrop(Blocks.AZALEA, 1.0F);
        this.addDrop(Blocks.FLOWERING_AZALEA, 1.2F);
        this.addDrop(Blocks.MOSS_BLOCK, 0.8F);
        this.addDrop(Blocks.MOSS_CARPET, 0.4F);
        this.addDrop(Blocks.SPORE_BLOSSOM, 0.6F);
        this.addDrop(Blocks.SMALL_DRIPLEAF, 0.4F);
        this.addDrop(Blocks.BIG_DRIPLEAF, 0.6F);
        this.addDrop(Items.GLOW_BERRIES, 0.5F);


        this.addDrop(Blocks.SUNFLOWER, 1.2F);
        this.addDrop(Blocks.ROSE_BUSH, 1.2F);
        this.addDrop(Blocks.LILAC, 1.0F);
        this.addDrop(Blocks.ALLIUM, 0.8F);
        this.addDrop(Blocks.BLUE_ORCHID, 0.6F);
        this.addDrop(Blocks.AZURE_BLUET, 0.8F);
        this.addDrop(Blocks.ORANGE_TULIP, 0.6F);
        this.addDrop(Blocks.RED_TULIP, 0.6F);
        this.addDrop(Blocks.WHITE_TULIP, 0.6F);
        this.addDrop(Blocks.PINK_TULIP, 0.6F);
        this.addDrop(Blocks.OXEYE_DAISY, 0.8F);


        this.addDrop(Blocks.CANDLE, 0.7F);
        this.addDrop(Blocks.WHITE_CANDLE, 0.7F);
        this.addDrop(Blocks.ORANGE_CANDLE, 0.7F);
        this.addDrop(Blocks.MAGENTA_CANDLE, 0.7F);
        this.addDrop(Blocks.LIGHT_BLUE_CANDLE, 0.7F);
        this.addDrop(Blocks.YELLOW_CANDLE, 0.7F);
        this.addDrop(Blocks.LIME_CANDLE, 0.7F);
        this.addDrop(Blocks.PINK_CANDLE, 0.7F);
        this.addDrop(Blocks.GRAY_CANDLE, 0.7F);
        this.addDrop(Blocks.LIGHT_GRAY_CANDLE, 0.7F);
        this.addDrop(Blocks.CYAN_CANDLE, 0.7F);
        this.addDrop(Blocks.PURPLE_CANDLE, 0.7F);
        this.addDrop(Blocks.BLUE_CANDLE, 0.7F);
        this.addDrop(Blocks.BROWN_CANDLE, 0.7F);
        this.addDrop(Blocks.GREEN_CANDLE, 0.7F);
        this.addDrop(Blocks.RED_CANDLE, 0.7F);
        this.addDrop(Blocks.BLACK_CANDLE, 0.7F);


        this.addDrop(Items.GLOW_ITEM_FRAME, 0.3F);
        this.addDrop(Items.GLOW_INK_SAC, 0.3F);

        this.addDrop(Items.EXPERIENCE_BOTTLE, 0.6F);
        this.addDrop(Items.GOAT_HORN, 0.5F);
        this.addDrop(Items.TADPOLE_BUCKET, 0.3F);
        this.addDrop(Items.FROG_SPAWN_EGG, 0.2F);


        this.addDrop(Items.OAK_CHEST_BOAT, 1.0F);
        this.addDrop(Items.SPRUCE_CHEST_BOAT, 1.0F);
        this.addDrop(Items.BIRCH_CHEST_BOAT, 1.0F);
        this.addDrop(Items.JUNGLE_CHEST_BOAT, 1.0F);
        this.addDrop(Items.ACACIA_CHEST_BOAT, 1.0F);
        this.addDrop(Items.DARK_OAK_CHEST_BOAT, 1.0F);


        this.addDrop(Items.MUSIC_DISC_5, 0.3F);
        this.addDrop(Items.MUSIC_DISC_OTHERSIDE, 0.3F);
        this.addDrop(Items.MUSIC_DISC_PIGSTEP, 0.3F);


        this.addDrop(Blocks.REINFORCED_DEEPSLATE, 0.5F);
        this.addDrop(Blocks.BLACKSTONE, 1.2F);
        this.addDrop(Blocks.POLISHED_BLACKSTONE, 1.5F);
        this.addDrop(Blocks.POLISHED_BLACKSTONE_BRICKS, 1.5F);
        this.addDrop(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 1.2F);
        this.addDrop(Blocks.CHISELED_POLISHED_BLACKSTONE, 1.2F);

        this.addDrop(Items.COOKED_MUTTON, 1.0F);
        this.addDrop(Items.COOKED_RABBIT, 1.0F);
        this.addDrop(Items.COOKED_SALMON, 1.0F);
        this.addDrop(Items.COOKED_COD, 1.0F);
        this.addDrop(Items.POTATO, 1.2F);
        this.addDrop(Items.BAKED_POTATO, 1.2F);
        this.addDrop(Items.CARROT, 1.3F);
        this.addDrop(Items.GOLDEN_CARROT, 0.5F);
        this.addDrop(Items.PUMPKIN_PIE, 0.8F);
        this.addDrop(Items.MELON_SLICE, 1.0F);
        this.addDrop(Items.SWEET_BERRIES, 0.9F);
        this.addDrop(Items.COOKIE, 1.0F);
        this.addDrop(Items.HONEY_BOTTLE, 0.8F);
        this.addDrop(Items.MILK_BUCKET, 0.4F);
        this.addDrop(Items.RABBIT_STEW, 0.6F);


        this.addDrop(Items.POTION, 0.5F);
        this.addDrop(Items.SPLASH_POTION, 0.3F);
        this.addDrop(Items.LINGERING_POTION, 0.2F);
        this.addDrop(Items.TIPPED_ARROW, 0.3F);


        this.addDrop(Items.STONE_SWORD, 1.5F);
        this.addDrop(Items.STONE_SHOVEL, 1.5F);
        this.addDrop(Items.STONE_PICKAXE, 1.5F);
        this.addDrop(Items.STONE_AXE, 1.5F);
        this.addDrop(Items.STONE_HOE, 1.5F);

        this.addDrop(Items.IRON_SWORD, 0.8F);
        this.addDrop(Items.IRON_SHOVEL, 0.8F);
        this.addDrop(Items.IRON_PICKAXE, 0.8F);
        this.addDrop(Items.IRON_AXE, 0.8F);
        this.addDrop(Items.IRON_HOE, 0.8F);

        this.addDrop(Items.BOW, 0.6F);
        this.addDrop(Items.CROSSBOW, 0.4F);
        this.addDrop(Items.ARROW, 3.0F);
        this.addDrop(Items.SPECTRAL_ARROW, 0.1F);

        this.addDrop(Items.SHIELD, 0.3F);


        this.addDrop(Items.LEATHER_HELMET, 0.4F);
        this.addDrop(Items.LEATHER_CHESTPLATE, 0.3F);
        this.addDrop(Items.LEATHER_LEGGINGS, 0.3F);
        this.addDrop(Items.LEATHER_BOOTS, 0.4F);

        this.addDrop(Items.CHAINMAIL_HELMET, 0.1F);
        this.addDrop(Items.CHAINMAIL_CHESTPLATE, 0.1F);
        this.addDrop(Items.CHAINMAIL_LEGGINGS, 0.1F);
        this.addDrop(Items.CHAINMAIL_BOOTS, 0.1F);

        this.addDrop(Items.IRON_HELMET, 0.1F);
        this.addDrop(Items.IRON_CHESTPLATE, 0.08F);
        this.addDrop(Items.IRON_LEGGINGS, 0.08F);
        this.addDrop(Items.IRON_BOOTS, 0.1F);

        this.addDrop(Items.GOLDEN_HELMET, 0.05F);
        this.addDrop(Items.GOLDEN_CHESTPLATE, 0.04F);
        this.addDrop(Items.GOLDEN_LEGGINGS, 0.04F);
        this.addDrop(Items.GOLDEN_BOOTS, 0.05F);

        this.addDrop(Items.DIAMOND_HELMET, 0.02F);
        this.addDrop(Items.DIAMOND_CHESTPLATE, 0.015F);
        this.addDrop(Items.DIAMOND_LEGGINGS, 0.015F);
        this.addDrop(Items.DIAMOND_BOOTS, 0.02F);

        this.addDrop(Items.NETHERITE_HELMET, 0.01F);
        this.addDrop(Items.NETHERITE_CHESTPLATE, 0.008F);
        this.addDrop(Items.NETHERITE_LEGGINGS, 0.008F);
        this.addDrop(Items.NETHERITE_BOOTS, 0.01F);

        this.addDrop(Items.FLINT_AND_STEEL, 0.2F);
        this.addDrop(Items.FISHING_ROD, 0.3F);
        this.addDrop(Items.SHEARS, 0.3F);
        this.addDrop(Items.LEAD, 0.5F);
        this.addDrop(Items.NAME_TAG, 0.1F);
        this.addDrop(Items.BOOK, 0.5F);
        this.addDrop(Items.ENCHANTED_BOOK, 0.1F);
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
