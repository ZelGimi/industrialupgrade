package com.denfop.datagen.blocktags;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import oshi.util.tuples.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BlockTagsProvider extends net.minecraftforge.common.data.BlockTagsProvider {
    public static List<IBlockTag> list = new LinkedList<>();
    private final String key;

    public BlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        this(packOutput, lookupProvider, IUCore.MODID, existingFileHelper);

    }

    public BlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid, ExistingFileHelper existingFileHelper) {
        super(packOutput, lookupProvider, modid, existingFileHelper);
        this.key = modid;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (IBlockTag tag : list) {
            Block block = tag.getBlock();
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(key))
                continue;
            Pair<String, Integer> pair = tag.getHarvestLevel();
            if (pair.getA() != null) {
                TagKey<Block> blockTagKey = getToolFromString(pair.getA());
                TagKey<Block> level = getLevelFromInteger(pair.getB());
                this.tag(blockTagKey).add(block);
                this.tag(level).add(block);
            }
        }
        if (key.equals(Constants.MOD_ID)) {
            this.tag(BlockTags.LOGS).add(IUItem.swampRubWood.getBlock().get());
            this.tag(BlockTags.LOGS).add(IUItem.rubWood.getBlock().get());
            this.tag(BlockTags.LOGS).add(IUItem.tropicalRubWood.getBlock().get());
            this.tag(BlockTags.LEAVES).add(IUItem.leaves.getBlock().get());
            this.tag(BlockTags.SAPLINGS).add(IUItem.rubberSapling.getBlock().get());
        }
    }

    private TagKey<Block> getLevelFromInteger(Integer b) {
        return switch (b) {
            default -> Tags.Blocks.NEEDS_WOOD_TOOL;
            case 1 -> BlockTags.NEEDS_STONE_TOOL;
            case 2 -> BlockTags.NEEDS_IRON_TOOL;
            case 3 -> BlockTags.NEEDS_DIAMOND_TOOL;
        };
    }

    private TagKey<Block> getToolFromString(String a) {
        return switch (a) {
            case "pickaxe" -> BlockTags.MINEABLE_WITH_PICKAXE;
            case "axe" -> BlockTags.MINEABLE_WITH_AXE;
            case "shovel" -> BlockTags.MINEABLE_WITH_SHOVEL;
            case "wrench" -> BlockTags.create(new ResourceLocation("mineable/wrench"));
            default -> BlockTags.MINEABLE_WITH_HOE;
        };
    }
}
