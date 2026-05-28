package com.denfop.datagen.blocktags;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class BlockTagsProvider extends net.minecraft.data.tags.BlockTagsProvider {

    public static List<IBlockTag> list = new LinkedList<>();

    private final String key;

    public BlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
        this(gen, Constants.MOD_ID, existingFileHelper);
    }

    public BlockTagsProvider(DataGenerator gen, String modid, @Nullable ExistingFileHelper existingFileHelper) {
        super(gen, modid, existingFileHelper);
        this.key = modid;
    }

    @Override
    protected void addTags() {
        try {
            addRegisteredBlockTags();
            addBlockTagsFromItemTags();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (key.equals(Constants.MOD_ID)) {
            addRubberTreeTags();
        }
    }

    private void addRegisteredBlockTags() {
        for (IBlockTag tag : list) {
            if (tag == null) {
                continue;
            }

            Block block = tag.getBlock();
            if (block == null) {
                continue;
            }

            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (blockId == null || !blockId.getNamespace().equals(key)) {
                continue;
            }

            Pair<String, Integer> pair = tag.getHarvestLevel();
            if (pair == null || pair.getA() == null) {
                continue;
            }

            TagKey<Block> blockTagKey = getToolFromString(pair.getA());
            TagKey<Block> level = getLevelFromInteger(pair.getB());

            this.tag(blockTagKey).add(block);
            this.tag(level).add(block);
        }
    }

    private void addBlockTagsFromItemTags() {
        for (IItemTag itemTag : ItemTagProvider.list) {
            if (itemTag == null) {
                continue;
            }

            Item item = itemTag.getItem();
            if (item == null || item == Items.AIR) {
                continue;
            }

            if (!(item instanceof BlockItem blockItem)) {
                continue;
            }

            Block block = blockItem.getBlock();
            if (block == null) {
                continue;
            }

            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (blockId == null || !blockId.getNamespace().equals(key)) {
                continue;
            }

            String[] tags = itemTag.getTags();
            if (tags == null) {
                continue;
            }

            for (String tagId : tags) {
                if (tagId == null || tagId.isBlank()) {
                    continue;
                }

                TagKey<Block> blockTagKey = createFromString(tagId);
                if (blockTagKey == null) {
                    continue;
                }

                this.tag(blockTagKey).add(block);
            }
        }
    }

    private void addRubberTreeTags() {
        this.tag(BlockTags.LOGS).add(IUItem.swampRubWood.getBlock().get());
        this.tag(BlockTags.LOGS).add(IUItem.rubWood.getBlock().get());
        this.tag(BlockTags.LOGS).add(IUItem.tropicalRubWood.getBlock().get());

        TagKey<Block> modLogRubber = create("logs/rubber");
        this.tag(modLogRubber).add(IUItem.swampRubWood.getBlock().get());
        this.tag(modLogRubber).add(IUItem.rubWood.getBlock().get());
        this.tag(modLogRubber).add(IUItem.tropicalRubWood.getBlock().get());


        TagKey<Block> forgeLogRubber = createFromString("forge:logs/rubber");
        if (forgeLogRubber != null) {
            this.tag(forgeLogRubber).add(IUItem.swampRubWood.getBlock().get());
            this.tag(forgeLogRubber).add(IUItem.rubWood.getBlock().get());
            this.tag(forgeLogRubber).add(IUItem.tropicalRubWood.getBlock().get());
        }

        this.tag(BlockTags.LEAVES).add(IUItem.leaves.getBlock().get());
        this.tag(BlockTags.SAPLINGS).add(IUItem.rubberSapling.getBlock().get());
    }

    private TagKey<Block> create(String pName) {
        ResourceLocation id = ResourceLocation.tryBuild(Constants.MOD_ID, pName);
        if (id == null) {
            throw new IllegalArgumentException("Invalid block tag path: " + pName);
        }

        return TagKey.create(Registry.BLOCK_REGISTRY, id);
    }

    @Nullable
    private TagKey<Block> createFromString(String rawId) {
        String normalized = rawId.trim().toLowerCase(Locale.ROOT);

        ResourceLocation id;

        if (normalized.contains(":")) {
            id = ResourceLocation.tryParse(normalized);
        } else {
            id = ResourceLocation.tryBuild(Constants.MOD_ID, normalized);
        }

        if (id == null) {
            return null;
        }

        return TagKey.create(Registry.BLOCK_REGISTRY, id);
    }

    private TagKey<Block> getLevelFromInteger(Integer b) {
        if (b == null) {
            return Tags.Blocks.NEEDS_WOOD_TOOL;
        }

        return switch (b) {
            default -> Tags.Blocks.NEEDS_WOOD_TOOL;
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
        };
    }

    private TagKey<Block> getToolFromString(String a) {
        if (a == null) {
            return BlockTags.MINEABLE_WITH_HOE;
        }

        return switch (a.toLowerCase(Locale.ROOT)) {
            case "pickaxe" -> BlockTags.MINEABLE_WITH_PICKAXE;
            case "axe" -> BlockTags.MINEABLE_WITH_AXE;
            case "shovel" -> BlockTags.MINEABLE_WITH_SHOVEL;
            case "wrench" -> BlockTags.create(new ResourceLocation("mineable/wrench"));
            default -> BlockTags.MINEABLE_WITH_HOE;
        };
    }
}