package com.denfop.datagen.blocktags;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BlockTagsProvider extends net.neoforged.neoforge.common.data.BlockTagsProvider {

    public static final List<IBlockTag> list = new ArrayList<>();

    private final String key;

    public BlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper
    ) {
        this(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    public BlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            String key,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, key, existingFileHelper);
        this.key = key;
    }

    private static TagKey<Block> getToolFromString(String tool) {
        String normalized = tool.toLowerCase(Locale.ROOT);

        return switch (normalized) {
            case "pickaxe", "mineable/pickaxe" -> BlockTags.MINEABLE_WITH_PICKAXE;
            case "axe", "mineable/axe" -> BlockTags.MINEABLE_WITH_AXE;
            case "shovel", "mineable/shovel" -> BlockTags.MINEABLE_WITH_SHOVEL;
            case "hoe", "mineable/hoe" -> BlockTags.MINEABLE_WITH_HOE;
            default -> blockTag(normalized);
        };
    }

    private static TagKey<Block> getLevelFromInteger(int level) {
        return switch (level) {
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
            default -> BlockTags.MINEABLE_WITH_PICKAXE;
        };
    }

    private static TagKey<Block> blockTag(String id) {
        return TagKey.create(Registries.BLOCK, normalizeLocation(id));
    }

    private static ResourceLocation normalizeLocation(String id) {
        String normalized = id.trim().toLowerCase(Locale.ROOT);

        if (normalized.indexOf(':') >= 0) {
            return ResourceLocation.parse(normalized);
        }

        return ResourceLocation.tryBuild(Constants.MOD_ID, normalized);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addRegisteredBlockTags();


        addBlockTagsFromItemTags();

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

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
            if (blockId == null || !blockId.getNamespace().equals(key)) {
                continue;
            }

            var pair = tag.getHarvestLevel();

            if (pair != null && pair.getA() != null) {
                TagKey<Block> toolTag = getToolFromString(pair.getA());
                TagKey<Block> levelTag = getLevelFromInteger(pair.getB());

                this.tag(toolTag).add(block);
                this.tag(levelTag).add(block);
            }
        }
    }

    private void addBlockTagsFromItemTags() {
        for (IItemTag itemTag : ItemTagProvider.list) {
            if (itemTag == null) {
                continue;
            }

            Item item = itemTag.getItem();
            if (!(item instanceof BlockItem blockItem)) {
                continue;
            }

            Block block = blockItem.getBlock();
            if (block == null) {
                continue;
            }

            ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
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

                TagKey<Block> blockTag = blockTag(tagId);
                this.tag(blockTag).add(block);
            }
        }
    }

    private void addRubberTreeTags() {
        this.tag(BlockTags.LOGS).add(IUItem.swampRubWood.getBlock().get());
        this.tag(BlockTags.LOGS).add(IUItem.rubWood.getBlock().get());
        this.tag(BlockTags.LOGS).add(IUItem.tropicalRubWood.getBlock().get());

        TagKey<Block> logRubber = blockTag("forge:logs/rubber");

        this.tag(logRubber).add(IUItem.swampRubWood.getBlock().get());
        this.tag(logRubber).add(IUItem.rubWood.getBlock().get());
        this.tag(logRubber).add(IUItem.tropicalRubWood.getBlock().get());

        this.tag(BlockTags.LEAVES).add(IUItem.leaves.getBlock().get());
        this.tag(BlockTags.SAPLINGS).add(IUItem.rubberSapling.getBlock().get());
    }
}