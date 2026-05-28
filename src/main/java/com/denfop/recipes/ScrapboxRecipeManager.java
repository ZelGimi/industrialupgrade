package com.denfop.recipes;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.recipe.IInputItemStack;
import com.denfop.utils.ModUtils;
import com.denfop.world.WorldBaseGen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public final class ScrapboxRecipeManager {

    private static final Set<String> EXCLUDED_VANILLA_PATHS = new HashSet<>(Arrays.asList(
            "air",
            "barrier",
            "light",
            "structure_block",
            "jigsaw",
            "structure_void",
            "debug_stick",
            "knowledge_book",
            "command_block",
            "chain_command_block",
            "repeating_command_block",
            "command_block_minecart",
            "spawner",
            "bedrock",
            "end_portal_frame",
            "reinforced_deepslate",
            "budding_amethyst",
            "frogspawn",
            "suspicious_sand",
            "suspicious_gravel",
            "petrified_oak_slab",
            "player_head",
            "moving_piston",
            "bundle"
    ));
    private static final Set<String> NBT_SENSITIVE_PATHS = new HashSet<>(Arrays.asList(
            "written_book",
            "enchanted_book",
            "potion",
            "splash_potion",
            "lingering_potion",
            "tipped_arrow",
            "filled_map",
            "firework_star",
            "firework_rocket",
            "goat_horn",
            "suspicious_stew"
    ));
    public static ScrapboxRecipeManager instance;
    private final Map<DropTier, List<Drop>> dropsByTier = new EnumMap<>(DropTier.class);
    private final Map<DropTier, Float> topChanceByTier = new EnumMap<>(DropTier.class);

    public ScrapboxRecipeManager() {
        instance = this;

        for (DropTier tier : DropTier.values()) {
            this.dropsByTier.put(tier, new ArrayList<>());
            this.topChanceByTier.put(tier, 0.0F);
        }

        this.addBuiltinDrops();
    }

    public boolean addRecipe(IInputItemStack input, Collection<ItemStack> output, CompoundTag metadata, boolean replace) {
        if (!input.matches(IUItem.scrapBox)) {
            throw new IllegalArgumentException("currently only scrap boxes are supported");
        }

        if (metadata == null || !metadata.contains("weight")) {
            throw new IllegalArgumentException("no weight metadata");
        }

        if (output.size() != 1) {
            throw new IllegalArgumentException("currently only a single drop stack is supported");
        }

        float weight = metadata.getFloat("weight");
        if (weight <= 0.0F || Float.isNaN(weight) || Float.isInfinite(weight)) {
            throw new IllegalArgumentException("invalid weight");
        }

        this.addDrop(output.iterator().next(), weight);
        return true;
    }

    public boolean addRecipe(IInputItemStack input, CompoundTag metadata, boolean replace, ItemStack... outputs) {
        return this.addRecipe(input, Arrays.asList(outputs), metadata, replace);
    }

    public ItemStack apply(ItemStack input) {
        if (ModUtils.isEmpty(input) || !input.is(IUItem.scrapBox.getItem())) {
            return ItemStack.EMPTY;
        }

        DropTier tier = this.rollTier(IUCore.random.nextFloat());
        if (tier == null) {
            return ItemStack.EMPTY;
        }

        return this.pickDropFromTier(tier, IUCore.random.nextFloat());
    }

    public ItemStack getRandomDrop() {
        DropTier tier = this.rollTier(WorldBaseGen.random.nextFloat());
        if (tier == null) {
            return ItemStack.EMPTY;
        }

        return this.pickDropFromTier(tier, WorldBaseGen.random.nextFloat());
    }

    public boolean isIterable() {
        return false;
    }

    public void addDrop(ItemStack drop, float rawChance) {
        if (ModUtils.isEmpty(drop)) {
            return;
        }
        if (rawChance <= 0.0F || Float.isNaN(rawChance) || Float.isInfinite(rawChance)) {
            return;
        }

        ItemStack copy = drop.copy();
        DropTier tier = this.resolveTier(rawChance);

        float currentTop = this.topChanceByTier.get(tier);
        float newTop = currentTop + rawChance;

        this.topChanceByTier.put(tier, newTop);
        this.dropsByTier.get(tier).add(new Drop(copy, rawChance, newTop, tier));
    }

    public ItemStack getDrop(ItemStack input) {
        return this.apply(input);
    }

    public Map<ItemStack, Float> getDrops() {
        Map<ItemStack, Float> ret = new HashMap<>();

        float availableTierPercent = 0.0F;
        for (DropTier tier : DropTier.values()) {
            List<Drop> list = this.dropsByTier.get(tier);
            if (list != null && !list.isEmpty()) {
                availableTierPercent += tier.percent;
            }
        }

        if (availableTierPercent <= 0.0F) {
            return ret;
        }

        for (DropTier tier : DropTier.values()) {
            List<Drop> list = this.dropsByTier.get(tier);
            if (list == null || list.isEmpty()) {
                continue;
            }

            float tierChance = tier.percent / availableTierPercent;
            float tierTop = this.topChanceByTier.get(tier);

            if (tierTop <= 0.0F) {
                continue;
            }

            for (Drop drop : list) {
                float exactChance = tierChance * (drop.originalChance / tierTop);
                ret.put(drop.item.copy(), exactChance);
            }
        }

        return ret;
    }

    public void clear() {
        for (DropTier tier : DropTier.values()) {
            this.dropsByTier.get(tier).clear();
            this.topChanceByTier.put(tier, 0.0F);
        }
    }

    private void addBuiltinDrops() {
        for (DropTier tier : DropTier.values()) {
            this.dropsByTier.get(tier).clear();
            this.topChanceByTier.put(tier, 0.0F);
        }

        this.addAllBalancedVanillaDrops();
        this.addBalancedModDrops();
    }

    private DropTier rollTier(float roll01) {
        float totalPercent = 0.0F;

        for (DropTier tier : DropTier.values()) {
            List<Drop> list = this.dropsByTier.get(tier);
            if (list != null && !list.isEmpty()) {
                totalPercent += tier.percent;
            }
        }

        if (totalPercent <= 0.0F) {
            return null;
        }

        float roll = roll01 * totalPercent;
        float current = 0.0F;

        for (DropTier tier : DropTier.values()) {
            List<Drop> list = this.dropsByTier.get(tier);
            if (list == null || list.isEmpty()) {
                continue;
            }

            current += tier.percent;
            if (roll < current) {
                return tier;
            }
        }

        return DropTier.COMMON;
    }

    private ItemStack pickDropFromTier(DropTier tier, float roll01) {
        List<Drop> list = this.dropsByTier.get(tier);
        if (list == null || list.isEmpty()) {
            return ItemStack.EMPTY;
        }

        float tierTop = this.topChanceByTier.get(tier);
        if (tierTop <= 0.0F) {
            return ItemStack.EMPTY;
        }

        float chance = roll01 * tierTop;
        int low = 0;
        int high = list.size() - 1;

        while (low < high) {
            int mid = (high + low) >>> 1;
            if (chance < list.get(mid).upperChanceBound) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        return list.get(low).item.copy();
    }

    private DropTier resolveTier(float rawChance) {
        if (rawChance >= 0.55F) return DropTier.COMMON;
        if (rawChance >= 0.20F) return DropTier.UNCOMMON;
        if (rawChance >= 0.07F) return DropTier.RARE;
        if (rawChance >= 0.02F) return DropTier.EPIC;
        if (rawChance >= 0.008F) return DropTier.LEGENDARY;
        return DropTier.MYTHIC;
    }

    private void addAllBalancedVanillaDrops() {
        List<Item> candidates = new ArrayList<>();
        for (Item item : BuiltInRegistries.ITEM) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            if (id == null) {
                continue;
            }
            if (!"minecraft".equals(id.getNamespace())) {
                continue;
            }
            if (this.shouldIncludeVanillaItem(item, id)) {
                candidates.add(item);
            }
        }

        candidates.sort(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item).toString()));

        for (Item item : candidates) {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
            float weight = this.computeVanillaWeight(id.getPath());
            if (weight > 0.0F) {
                this.addDrop(new ItemStack(item), weight);
            }
        }
    }

    private void addBalancedModDrops() {
        this.addDrop(IUItem.rubber, 0.45F);
        this.addDrop(IUItem.coalDust, 0.35F);
        this.addDrop(IUItem.copperDust, 0.28F);
        this.addDrop(IUItem.tinDust, 0.28F);
        this.addDrop(IUItem.ironDust, 0.24F);
        this.addDrop(IUItem.goldDust, 0.18F);

        this.addDrop(IUItem.classic_ore.getItemStack(BlockClassicOre.Type.tin), 0.14F);
    }

    private boolean shouldIncludeVanillaItem(Item item, ResourceLocation id) {
        String path = id.getPath();

        if (item == null) {
            return false;
        }

        if ("air".equals(path)) {
            return false;
        }

        if (EXCLUDED_VANILLA_PATHS.contains(path)) {
            return false;
        }

        if (path.endsWith("_spawn_egg")) {
            return false;
        }

        if (path.contains("command_block")) {
            return false;
        }

        if (path.contains("portal")) {
            return false;
        }
        if (item == Blocks.TRIAL_SPAWNER.asItem()) {
            return false;
        }
        if (item == Blocks.VAULT.asItem()) {
            return false;
        }
        if (item == Blocks.SPAWNER.asItem()) {
            return false;
        }
        if (NBT_SENSITIVE_PATHS.contains(path)) {
            return false;
        }

        return true;
    }

    private float computeVanillaWeight(String path) {

        if ("elytra".equals(path)) return 0.006F;
        if ("nether_star".equals(path)) return 0.008F;
        if ("dragon_egg".equals(path)) return 0.006F;
        if ("enchanted_golden_apple".equals(path)) return 0.010F;

        if ("netherite_upgrade_smithing_template".equals(path)) return 0.010F;
        if (path.endsWith("_armor_trim_smithing_template")) return 0.014F;

        if ("sniffer_egg".equals(path)) return 0.020F;
        if ("totem_of_undying".equals(path)) return 0.020F;
        if ("dragon_breath".equals(path)) return 0.030F;
        if ("echo_shard".equals(path)) return 0.024F;
        if ("recovery_compass".equals(path)) return 0.024F;
        if ("heart_of_the_sea".equals(path)) return 0.020F;
        if ("conduit".equals(path)) return 0.016F;
        if ("beacon".equals(path)) return 0.012F;
        if ("disc_fragment_5".equals(path)) return 0.018F;
        if ("nautilus_shell".equals(path)) return 0.030F;

        if ("creeper_head".equals(path)) return 0.020F;
        if ("zombie_head".equals(path)) return 0.020F;
        if ("dragon_head".equals(path)) return 0.018F;
        if ("piglin_head".equals(path)) return 0.018F;
        if ("skeleton_skull".equals(path)) return 0.020F;
        if ("wither_skeleton_skull".equals(path)) return 0.014F;

        if (path.endsWith("_pottery_sherd")) return 0.028F;

        if (path.startsWith("music_disc_")) {
            if ("music_disc_relic".equals(path)) return 0.024F;
            if ("music_disc_pigstep".equals(path)) return 0.024F;
            if ("music_disc_otherside".equals(path)) return 0.024F;
            if ("music_disc_5".equals(path)) return 0.024F;
            return 0.035F;
        }


        if (this.isArmorOrTool(path)) {
            if (path.startsWith("wooden_")) return 0.55F;
            if (path.startsWith("stone_")) return 0.42F;
            if (path.startsWith("chainmail_")) return 0.090F;
            if (path.startsWith("iron_")) return 0.180F;
            if (path.startsWith("golden_")) return 0.140F;
            if (path.startsWith("diamond_")) return 0.045F;
            if (path.startsWith("netherite_")) return 0.016F;
            if ("bow".equals(path)) return 0.220F;
            if ("crossbow".equals(path)) return 0.140F;
            if ("shield".equals(path)) return 0.120F;
            if ("trident".equals(path)) return 0.020F;
            if ("fishing_rod".equals(path)) return 0.160F;
            if ("flint_and_steel".equals(path)) return 0.140F;
            if ("shears".equals(path)) return 0.140F;
            if ("brush".equals(path)) return 0.120F;
            return 0.160F;
        }


        switch (path) {
            case "coal":
            case "charcoal":
                return 0.45F;
            case "raw_copper":
                return 0.28F;
            case "raw_iron":
                return 0.22F;
            case "raw_gold":
                return 0.16F;
            case "copper_ingot":
                return 0.22F;
            case "iron_ingot":
                return 0.18F;
            case "gold_ingot":
                return 0.14F;
            case "netherite_scrap":
                return 0.030F;
            case "netherite_ingot":
                return 0.014F;
            case "redstone":
                return 0.30F;
            case "lapis_lazuli":
                return 0.26F;
            case "quartz":
                return 0.22F;
            case "amethyst_shard":
                return 0.20F;
            case "emerald":
                return 0.050F;
            case "diamond":
                return 0.040F;
            case "ancient_debris":
                return 0.016F;
        }

        if (path.endsWith("_ore")) {
            if (path.contains("diamond")) return 0.030F;
            if (path.contains("emerald")) return 0.040F;
            if (path.contains("gold")) return 0.100F;
            if (path.contains("iron")) return 0.130F;
            if (path.contains("copper")) return 0.150F;
            if (path.contains("lapis")) return 0.120F;
            if (path.contains("redstone")) return 0.120F;
            if (path.contains("coal")) return 0.180F;
            if (path.contains("nether_quartz")) return 0.120F;
            return 0.120F;
        }

        if (path.endsWith("_block")) {
            if (path.contains("netherite")) return 0.008F;
            if (path.contains("diamond")) return 0.018F;
            if (path.contains("emerald")) return 0.024F;
            if (path.contains("gold")) return 0.050F;
            if (path.contains("iron")) return 0.070F;
            if (path.contains("copper")) return 0.100F;
            if (path.contains("redstone")) return 0.100F;
            if (path.contains("lapis")) return 0.090F;
            if (path.contains("coal")) return 0.120F;
            if (path.contains("amethyst")) return 0.100F;
        }


        if ("hopper".equals(path)) return 0.080F;
        if ("observer".equals(path)) return 0.090F;
        if ("comparator".equals(path)) return 0.090F;
        if ("repeater".equals(path)) return 0.100F;
        if ("piston".equals(path)) return 0.120F;
        if ("sticky_piston".equals(path)) return 0.080F;
        if ("dispenser".equals(path)) return 0.120F;
        if ("dropper".equals(path)) return 0.130F;
        if ("daylight_detector".equals(path)) return 0.110F;
        if ("target".equals(path)) return 0.120F;
        if ("lectern".equals(path)) return 0.130F;
        if ("smithing_table".equals(path)) return 0.080F;
        if ("cartography_table".equals(path)) return 0.120F;
        if ("fletching_table".equals(path)) return 0.120F;
        if ("loom".equals(path)) return 0.120F;
        if ("stonecutter".equals(path)) return 0.110F;
        if ("grindstone".equals(path)) return 0.110F;
        if ("anvil".equals(path)) return 0.060F;
        if ("chipped_anvil".equals(path)) return 0.050F;
        if ("damaged_anvil".equals(path)) return 0.045F;
        if ("enchanting_table".equals(path)) return 0.035F;
        if ("brewing_stand".equals(path)) return 0.060F;
        if ("cauldron".equals(path)) return 0.100F;
        if ("barrel".equals(path)) return 0.180F;
        if ("chest".equals(path)) return 0.220F;
        if ("ender_chest".equals(path)) return 0.040F;
        if ("shulker_box".equals(path) || path.endsWith("_shulker_box")) return 0.030F;
        if ("item_frame".equals(path)) return 0.160F;
        if ("glow_item_frame".equals(path)) return 0.070F;
        if ("armor_stand".equals(path)) return 0.120F;
        if ("lead".equals(path)) return 0.120F;
        if ("name_tag".equals(path)) return 0.060F;
        if ("saddle".equals(path)) return 0.050F;
        if ("spyglass".equals(path)) return 0.100F;
        if ("clock".equals(path)) return 0.080F;
        if ("compass".equals(path)) return 0.090F;
        if ("map".equals(path)) return 0.110F;
        if ("paper".equals(path)) return 0.280F;
        if ("book".equals(path)) return 0.180F;
        if ("bookshelf".equals(path)) return 0.120F;
        if ("chiseled_bookshelf".equals(path)) return 0.080F;
        if ("bucket".equals(path)) return 0.180F;
        if ("water_bucket".equals(path)) return 0.100F;
        if ("lava_bucket".equals(path)) return 0.080F;
        if ("milk_bucket".equals(path)) return 0.100F;
        if (path.endsWith("_bucket")) return 0.060F;
        if ("minecart".equals(path)) return 0.100F;
        if (path.endsWith("_minecart")) return 0.080F;
        if (path.endsWith("_boat") || path.endsWith("_raft")) return 0.120F;
        if (path.endsWith("_chest_boat") || path.endsWith("_chest_raft")) return 0.080F;
        if ("rail".equals(path)) return 0.220F;
        if ("powered_rail".equals(path)) return 0.080F;
        if ("detector_rail".equals(path)) return 0.100F;
        if ("activator_rail".equals(path)) return 0.100F;
        if ("sculk_sensor".equals(path)) return 0.080F;
        if ("calibrated_sculk_sensor".equals(path)) return 0.040F;
        if ("sculk_catalyst".equals(path)) return 0.060F;
        if ("sculk_shrieker".equals(path)) return 0.050F;


        if ("stick".equals(path)) return 0.95F;
        if ("string".equals(path)) return 0.70F;
        if ("bone".equals(path)) return 0.70F;
        if ("arrow".equals(path)) return 0.65F;
        if ("spectral_arrow".equals(path)) return 0.050F;
        if ("rotten_flesh".equals(path)) return 0.75F;
        if ("spider_eye".equals(path)) return 0.45F;
        if ("gunpowder".equals(path)) return 0.50F;
        if ("slime_ball".equals(path)) return 0.22F;
        if ("magma_cream".equals(path)) return 0.12F;
        if ("blaze_powder".equals(path)) return 0.10F;
        if ("blaze_rod".equals(path)) return 0.060F;
        if ("ender_pearl".equals(path)) return 0.070F;
        if ("ghast_tear".equals(path)) return 0.040F;
        if ("phantom_membrane".equals(path)) return 0.050F;
        if ("rabbit_foot".equals(path)) return 0.030F;
        if ("rabbit_hide".equals(path)) return 0.180F;
        if ("feather".equals(path)) return 0.60F;
        if ("leather".equals(path)) return 0.42F;
        if ("prismarine_shard".equals(path)) return 0.140F;
        if ("prismarine_crystals".equals(path)) return 0.100F;
        if ("ink_sac".equals(path)) return 0.26F;
        if ("glow_ink_sac".equals(path)) return 0.090F;
        if ("shulker_shell".equals(path)) return 0.040F;
        if ("turtle_scute".equals(path)) return 0.030F;
        if ("goat_horn".equals(path)) return 0.050F;
        if ("experience_bottle".equals(path)) return 0.060F;
        if ("ominous_banner".equals(path)) return 0.030F;

        if ("wheat".equals(path)) return 0.80F;
        if ("wheat_seeds".equals(path)) return 0.70F;
        if ("beetroot".equals(path)) return 0.70F;
        if ("beetroot_seeds".equals(path)) return 0.70F;
        if ("potato".equals(path)) return 0.80F;
        if ("baked_potato".equals(path)) return 0.55F;
        if ("poisonous_potato".equals(path)) return 0.25F;
        if ("carrot".equals(path)) return 0.80F;
        if ("golden_carrot".equals(path)) return 0.090F;
        if ("apple".equals(path)) return 0.55F;
        if ("golden_apple".equals(path)) return 0.050F;
        if ("melon_slice".equals(path)) return 0.70F;
        if ("melon_seeds".equals(path)) return 0.60F;
        if ("pumpkin_pie".equals(path)) return 0.30F;
        if ("pumpkin_seeds".equals(path)) return 0.60F;
        if ("egg".equals(path)) return 0.45F;
        if ("honey_bottle".equals(path)) return 0.30F;
        if ("honeycomb".equals(path)) return 0.30F;
        if ("sweet_berries".equals(path)) return 0.55F;
        if ("glow_berries".equals(path)) return 0.35F;
        if ("chorus_fruit".equals(path)) return 0.16F;
        if ("popped_chorus_fruit".equals(path)) return 0.12F;
        if ("kelp".equals(path)) return 0.55F;
        if ("dried_kelp".equals(path)) return 0.40F;
        if ("cocoa_beans".equals(path)) return 0.40F;
        if ("sugar_cane".equals(path)) return 0.65F;
        if ("bamboo".equals(path)) return 0.70F;
        if ("nether_wart".equals(path)) return 0.28F;
        if ("torchflower_seeds".equals(path)) return 0.060F;
        if ("pitcher_pod".equals(path)) return 0.060F;

        if (this.isRawMeat(path)) return 0.55F;
        if (this.isCookedFood(path)) return 0.40F;


        if (this.isWoodFamily(path)) {
            if (path.endsWith("_sapling") || path.endsWith("_propagule")) return 0.34F;
            if (path.endsWith("_leaves")) return 0.26F;
            if (path.endsWith("_log") || path.endsWith("_wood") || path.endsWith("_stem") || path.endsWith("_hyphae"))
                return 0.60F;
            if (path.endsWith("_planks")) return 0.50F;
            if (path.endsWith("_door") || path.endsWith("_trapdoor")) return 0.24F;
            if (path.endsWith("_fence") || path.endsWith("_fence_gate")) return 0.22F;
            if (path.endsWith("_slab")) return 0.22F;
            if (path.endsWith("_stairs")) return 0.24F;
            if (path.endsWith("_sign") || path.endsWith("_hanging_sign")) return 0.20F;
            if (path.endsWith("_button") || path.endsWith("_pressure_plate")) return 0.18F;
            if (path.endsWith("_boat") || path.endsWith("_raft")) return 0.12F;
            if (path.endsWith("_chest_boat") || path.endsWith("_chest_raft")) return 0.08F;
            return 0.32F;
        }

        if (this.isPlantOrFlower(path)) {
            if (path.contains("torchflower")) return 0.050F;
            if (path.contains("pitcher")) return 0.060F;
            if (path.contains("wither_rose")) return 0.040F;
            if (path.contains("spore_blossom")) return 0.060F;
            if (path.contains("big_dripleaf") || path.contains("small_dripleaf")) return 0.18F;
            if (path.contains("pink_petals")) return 0.24F;
            if (path.contains("sunflower") || path.contains("rose_bush") || path.contains("lilac") || path.contains("peony"))
                return 0.26F;
            return 0.28F;
        }


        if (this.isStoneFamily(path)) {
            if (path.contains("deepslate")) return 0.42F;
            if (path.contains("blackstone")) return 0.34F;
            if (path.contains("prismarine")) return 0.22F;
            if (path.contains("purpur")) return 0.20F;
            if (path.contains("mud_brick")) return 0.26F;
            if (path.contains("sandstone")) return 0.34F;
            if (path.contains("red_sandstone")) return 0.30F;
            if (path.contains("stone_brick")) return 0.34F;
            if (path.contains("brick")) return 0.28F;
            if (path.contains("quartz")) return 0.16F;
            if (path.contains("end_stone")) return 0.20F;
            if (path.contains("calcite")) return 0.30F;
            if (path.contains("tuff")) return 0.32F;
            if (path.contains("smooth_basalt")) return 0.28F;
            if (path.contains("basalt")) return 0.26F;
            if (path.contains("obsidian")) return 0.10F;
            if (path.contains("crying_obsidian")) return 0.060F;
            return 0.36F;
        }

        if (path.endsWith("_wool")) return 0.24F;
        if (path.endsWith("_carpet")) return 0.18F;
        if (path.endsWith("_bed")) return 0.18F;
        if (path.endsWith("_banner")) return 0.16F;
        if (path.endsWith("_candle")) return 0.18F;
        if (path.endsWith("_glass")) return 0.16F;
        if (path.endsWith("_glass_pane")) return 0.14F;
        if (path.endsWith("_terracotta")) return 0.18F;
        if (path.endsWith("_concrete")) return 0.18F;
        if (path.endsWith("_concrete_powder")) return 0.20F;
        if (path.endsWith("_glazed_terracotta")) return 0.16F;
        if (path.endsWith("_stained_glass")) return 0.16F;
        if (path.endsWith("_stained_glass_pane")) return 0.14F;


        if ("netherrack".equals(path)) return 0.60F;
        if ("soul_sand".equals(path)) return 0.34F;
        if ("soul_soil".equals(path)) return 0.34F;
        if ("glowstone".equals(path)) return 0.18F;
        if ("glowstone_dust".equals(path)) return 0.22F;
        if ("magma_block".equals(path)) return 0.22F;
        if ("warped_fungus".equals(path)) return 0.22F;
        if ("crimson_fungus".equals(path)) return 0.22F;
        if ("warped_roots".equals(path)) return 0.20F;
        if ("crimson_roots".equals(path)) return 0.20F;
        if ("nether_sprouts".equals(path)) return 0.18F;
        if ("twisting_vines".equals(path)) return 0.18F;
        if ("weeping_vines".equals(path)) return 0.18F;
        if ("chorus_flower".equals(path)) return 0.12F;
        if ("end_rod".equals(path)) return 0.12F;


        return 0.20F;
    }

    private boolean isArmorOrTool(String path) {
        return path.endsWith("_sword")
                || path.endsWith("_pickaxe")
                || path.endsWith("_axe")
                || path.endsWith("_shovel")
                || path.endsWith("_hoe")
                || path.endsWith("_helmet")
                || path.endsWith("_chestplate")
                || path.endsWith("_leggings")
                || path.endsWith("_boots")
                || "bow".equals(path)
                || "crossbow".equals(path)
                || "shield".equals(path)
                || "trident".equals(path)
                || "fishing_rod".equals(path)
                || "flint_and_steel".equals(path)
                || "shears".equals(path)
                || "brush".equals(path);
    }

    private boolean isWoodFamily(String path) {
        return path.endsWith("_log")
                || path.endsWith("_wood")
                || path.endsWith("_stem")
                || path.endsWith("_hyphae")
                || path.endsWith("_planks")
                || path.endsWith("_slab")
                || path.endsWith("_stairs")
                || path.endsWith("_door")
                || path.endsWith("_trapdoor")
                || path.endsWith("_fence")
                || path.endsWith("_fence_gate")
                || path.endsWith("_sign")
                || path.endsWith("_hanging_sign")
                || path.endsWith("_button")
                || path.endsWith("_pressure_plate")
                || path.endsWith("_boat")
                || path.endsWith("_raft")
                || path.endsWith("_chest_boat")
                || path.endsWith("_chest_raft")
                || path.endsWith("_sapling")
                || path.endsWith("_propagule")
                || path.endsWith("_leaves")
                || path.endsWith("_roots")
                || path.contains("mangrove")
                || path.contains("bamboo")
                || path.contains("cherry")
                || path.contains("oak")
                || path.contains("spruce")
                || path.contains("birch")
                || path.contains("jungle")
                || path.contains("acacia")
                || path.contains("dark_oak")
                || path.contains("crimson")
                || path.contains("warped");
    }

    private boolean isStoneFamily(String path) {
        return path.contains("stone")
                || path.contains("cobble")
                || path.contains("deepslate")
                || path.contains("blackstone")
                || path.contains("andesite")
                || path.contains("granite")
                || path.contains("diorite")
                || path.contains("tuff")
                || path.contains("calcite")
                || path.contains("basalt")
                || path.contains("obsidian")
                || path.contains("sandstone")
                || path.contains("prismarine")
                || path.contains("quartz")
                || path.contains("purpur")
                || path.contains("brick")
                || path.contains("mud_brick")
                || path.contains("end_stone")
                || path.contains("terracotta")
                || path.contains("concrete");
    }

    private boolean isPlantOrFlower(String path) {
        return path.endsWith("_sapling")
                || path.endsWith("_flower")
                || path.endsWith("_bush")
                || path.endsWith("_roots")
                || path.endsWith("_mushroom")
                || path.endsWith("_fungus")
                || path.endsWith("_vine")
                || path.endsWith("_vines")
                || path.endsWith("_petals")
                || path.contains("flower")
                || path.contains("azalea")
                || path.contains("tulip")
                || path.contains("orchid")
                || path.contains("allium")
                || path.contains("daisy")
                || path.contains("dandelion")
                || path.contains("poppy")
                || path.contains("sunflower")
                || path.contains("rose")
                || path.contains("lilac")
                || path.contains("peony")
                || path.contains("moss")
                || path.contains("dripleaf")
                || path.contains("spore_blossom")
                || path.contains("torchflower")
                || path.contains("pitcher")
                || path.contains("seagrass")
                || path.contains("kelp");
    }

    private boolean isRawMeat(String path) {
        return "beef".equals(path)
                || "porkchop".equals(path)
                || "chicken".equals(path)
                || "mutton".equals(path)
                || "rabbit".equals(path)
                || "cod".equals(path)
                || "salmon".equals(path)
                || "tropical_fish".equals(path);
    }

    private boolean isCookedFood(String path) {
        return "cooked_beef".equals(path)
                || "cooked_porkchop".equals(path)
                || "cooked_chicken".equals(path)
                || "cooked_mutton".equals(path)
                || "cooked_rabbit".equals(path)
                || "cooked_cod".equals(path)
                || "cooked_salmon".equals(path)
                || "bread".equals(path)
                || "cookie".equals(path)
                || "cake".equals(path)
                || "rabbit_stew".equals(path)
                || "mushroom_stew".equals(path)
                || "beetroot_soup".equals(path);
    }

    private void addDrop(ItemLike like, float rawChance) {
        this.addDrop(new ItemStack(like), rawChance);
    }

    private enum DropTier {
        COMMON(52.0F),
        UNCOMMON(27.0F),
        RARE(12.0F),
        EPIC(6.0F),
        LEGENDARY(2.5F),
        MYTHIC(0.5F);

        private final float percent;

        DropTier(float percent) {
            this.percent = percent;
        }
    }

    private static final class Drop {
        private final ItemStack item;
        private final float originalChance;
        private final float upperChanceBound;
        private final DropTier tier;

        private Drop(ItemStack item, float originalChance, float upperChanceBound, DropTier tier) {
            this.item = item;
            this.originalChance = originalChance;
            this.upperChanceBound = upperChanceBound;
            this.tier = tier;
        }
    }
}