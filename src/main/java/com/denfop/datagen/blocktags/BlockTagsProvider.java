package com.denfop.datagen.blocktags;

import com.denfop.Constants;
import com.denfop.IUItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import oshi.util.tuples.Pair;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

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
        for (IBlockTag tag : list) {
            Block block = tag.getBlock();
            if (!ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(key))
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
