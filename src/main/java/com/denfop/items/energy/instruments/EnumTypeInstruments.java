package com.denfop.items.energy.instruments;

import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.items.EnumInfoUpgradeModules;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public enum EnumTypeInstruments {
    SHOVEL(
            Sets
                    .newHashSet(
                            Blocks.GRASS_BLOCK.defaultBlockState(),
                            Blocks.DIRT.defaultBlockState(),
                            Blocks.SAND.defaultBlockState(),
                            Blocks.GRAVEL.defaultBlockState(),
                            Blocks.POWDER_SNOW.defaultBlockState(),
                            Blocks.SNOW.defaultBlockState(),
                            Blocks.CLAY.defaultBlockState(),
                            Blocks.FARMLAND.defaultBlockState(),
                            Blocks.SOUL_SAND.defaultBlockState(),
                            Blocks.MYCELIUM.defaultBlockState()
                    ),

            ImmutableSet.of("shovel"),
            Collections.singletonList(BlockTags.MINEABLE_WITH_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES, EnumOperations.TUNNEL)
    ),
    PICKAXE(Sets.newHashSet(
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
            Blocks.STONE_SLAB.defaultBlockState(),
            Blocks.STONE.defaultBlockState(),
            Blocks.SANDSTONE.defaultBlockState(),
            Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
            Blocks.IRON_ORE.defaultBlockState(),
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.COAL_ORE.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.DIAMOND_ORE.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.ICE.defaultBlockState(),
            Blocks.NETHERRACK.defaultBlockState(),
            Blocks.LAPIS_ORE.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_ORE.defaultBlockState(),
            Blocks.RAIL.defaultBlockState(),
            Blocks.DETECTOR_RAIL.defaultBlockState(),
            Blocks.POWERED_RAIL.defaultBlockState(),
            Blocks.ACTIVATOR_RAIL.defaultBlockState()
    ), ImmutableSet.of("pickaxe"),
            Collections.singletonList(BlockTags.MINEABLE_WITH_PICKAXE), Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES,
            EnumOperations.ORE, EnumOperations.TUNNEL
    )
    ),
    DRILL(Sets.newHashSet(
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
            Blocks.STONE_SLAB.defaultBlockState(),
            Blocks.STONE.defaultBlockState(),
            Blocks.SANDSTONE.defaultBlockState(),
            Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
            Blocks.IRON_ORE.defaultBlockState(),
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.COAL_ORE.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.DIAMOND_ORE.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.ICE.defaultBlockState(),
            Blocks.NETHERRACK.defaultBlockState(),
            Blocks.LAPIS_ORE.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_ORE.defaultBlockState(),
            Blocks.RAIL.defaultBlockState(),
            Blocks.DETECTOR_RAIL.defaultBlockState(),
            Blocks.POWERED_RAIL.defaultBlockState(),
            Blocks.ACTIVATOR_RAIL.defaultBlockState(),
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            Blocks.SAND.defaultBlockState(),
            Blocks.GRAVEL.defaultBlockState(),
            Blocks.POWDER_SNOW.defaultBlockState(),
            Blocks.SNOW.defaultBlockState(),
            Blocks.CLAY.defaultBlockState(),
            Blocks.FARMLAND.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
            Blocks.MYCELIUM.defaultBlockState()
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES, EnumOperations.MEGAHOLES,
                    EnumOperations.ORE, EnumOperations.TUNNEL
            )
    ),
    SIMPLE_DRILL("drill", Sets.newHashSet(
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
            Blocks.STONE_SLAB.defaultBlockState(),
            Blocks.STONE.defaultBlockState(),
            Blocks.SANDSTONE.defaultBlockState(),
            Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
            Blocks.IRON_ORE.defaultBlockState(),
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.COAL_ORE.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.DIAMOND_ORE.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.ICE.defaultBlockState(),
            Blocks.NETHERRACK.defaultBlockState(),
            Blocks.LAPIS_ORE.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_ORE.defaultBlockState(),
            Blocks.RAIL.defaultBlockState(),
            Blocks.DETECTOR_RAIL.defaultBlockState(),
            Blocks.POWERED_RAIL.defaultBlockState(),
            Blocks.ACTIVATOR_RAIL.defaultBlockState(),
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            Blocks.SAND.defaultBlockState(),
            Blocks.GRAVEL.defaultBlockState(),
            Blocks.POWDER_SNOW.defaultBlockState(),
            Blocks.SNOW.defaultBlockState(),
            Blocks.CLAY.defaultBlockState(),
            Blocks.FARMLAND.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
            Blocks.MYCELIUM.defaultBlockState()
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL),
            Collections.singletonList(EnumOperations.DEFAULT
            )
    ),
    DIAMOND_DRILL("drill", Sets.newHashSet(
            Blocks.COBBLESTONE.defaultBlockState(),
            Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
            Blocks.STONE_SLAB.defaultBlockState(),
            Blocks.STONE.defaultBlockState(),
            Blocks.SANDSTONE.defaultBlockState(),
            Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
            Blocks.IRON_ORE.defaultBlockState(),
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.COAL_ORE.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.DIAMOND_ORE.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.ICE.defaultBlockState(),
            Blocks.NETHERRACK.defaultBlockState(),
            Blocks.LAPIS_ORE.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_ORE.defaultBlockState(),
            Blocks.RAIL.defaultBlockState(),
            Blocks.DETECTOR_RAIL.defaultBlockState(),
            Blocks.POWERED_RAIL.defaultBlockState(),
            Blocks.ACTIVATOR_RAIL.defaultBlockState(),
            Blocks.GRASS_BLOCK.defaultBlockState(),
            Blocks.DIRT.defaultBlockState(),
            Blocks.SAND.defaultBlockState(),
            Blocks.GRAVEL.defaultBlockState(),
            Blocks.POWDER_SNOW.defaultBlockState(),
            Blocks.SNOW.defaultBlockState(),
            Blocks.CLAY.defaultBlockState(),
            Blocks.FARMLAND.defaultBlockState(),
            Blocks.SOUL_SAND.defaultBlockState(),
            Blocks.MYCELIUM.defaultBlockState()
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES
            )
    ),
    AXE(
            Sets.newHashSet(
                    Blocks.OAK_PLANKS.defaultBlockState(),
                    Blocks.BOOKSHELF.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.CHEST.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.OAK_LEAVES.defaultBlockState(),
                    Blocks.DARK_OAK_LEAVES.defaultBlockState()
            ),

            ImmutableSet.of("axe"),
            Collections.singletonList(BlockTags.MINEABLE_WITH_AXE),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES,
                    EnumOperations.TREE
            )
    ),
    CHAINSAW(
            Sets.newHashSet(
                    Blocks.OAK_PLANKS.defaultBlockState(),
                    Blocks.BOOKSHELF.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.CHEST.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.OAK_LEAVES.defaultBlockState(),
                    Blocks.DARK_OAK_LEAVES.defaultBlockState()
            ),

            ImmutableSet.of("axe", "shears"),
            Arrays.asList(BlockTags.MINEABLE_WITH_AXE, BlockTags.LEAVES),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.SHEARS
            )
    ),
    PERFECT_DRILL(
            Sets.newHashSet(
                    Blocks.COBBLESTONE.defaultBlockState(),
                    Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
                    Blocks.STONE_SLAB.defaultBlockState(),
                    Blocks.STONE.defaultBlockState(),
                    Blocks.SANDSTONE.defaultBlockState(),
                    Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
                    Blocks.IRON_ORE.defaultBlockState(),
                    Blocks.IRON_BLOCK.defaultBlockState(),
                    Blocks.COAL_ORE.defaultBlockState(),
                    Blocks.GOLD_BLOCK.defaultBlockState(),
                    Blocks.GOLD_ORE.defaultBlockState(),
                    Blocks.DIAMOND_ORE.defaultBlockState(),
                    Blocks.DIAMOND_BLOCK.defaultBlockState(),
                    Blocks.ICE.defaultBlockState(),
                    Blocks.NETHERRACK.defaultBlockState(),
                    Blocks.LAPIS_ORE.defaultBlockState(),
                    Blocks.LAPIS_BLOCK.defaultBlockState(),
                    Blocks.REDSTONE_ORE.defaultBlockState(),
                    Blocks.RAIL.defaultBlockState(),
                    Blocks.DETECTOR_RAIL.defaultBlockState(),
                    Blocks.POWERED_RAIL.defaultBlockState(),
                    Blocks.ACTIVATOR_RAIL.defaultBlockState(),
                    Blocks.GRASS_BLOCK.defaultBlockState(),
                    Blocks.DIRT.defaultBlockState(),
                    Blocks.SAND.defaultBlockState(),
                    Blocks.GRAVEL.defaultBlockState(),
                    Blocks.POWDER_SNOW.defaultBlockState(),
                    Blocks.SNOW.defaultBlockState(),
                    Blocks.OAK_PLANKS.defaultBlockState(),
                    Blocks.BOOKSHELF.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.CHEST.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.OAK_LEAVES.defaultBlockState(),
                    Blocks.DARK_OAK_LEAVES.defaultBlockState(),
                    Blocks.CLAY.defaultBlockState(),
                    Blocks.FARMLAND.defaultBlockState(),
                    Blocks.SOUL_SAND.defaultBlockState(),
                    Blocks.MYCELIUM.defaultBlockState()
            ),

            ImmutableSet.of("pickaxe", "shovel", "axe"),
            Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE),
            Arrays.asList(
                    EnumOperations.DEFAULT,
                    EnumOperations.BIGHOLES,
                    EnumOperations.MEGAHOLES,
                    EnumOperations.ULTRAHOLES,
                    EnumOperations.ORE,
                    EnumOperations.TUNNEL,
                    EnumOperations.TREE
            )
    ),
    VAJRA(
            Sets.newHashSet(
                    Blocks.COBBLESTONE.defaultBlockState(),
                    Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
                    Blocks.STONE_SLAB.defaultBlockState(),
                    Blocks.STONE.defaultBlockState(),
                    Blocks.SANDSTONE.defaultBlockState(),
                    Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
                    Blocks.IRON_ORE.defaultBlockState(),
                    Blocks.IRON_BLOCK.defaultBlockState(),
                    Blocks.COAL_ORE.defaultBlockState(),
                    Blocks.GOLD_BLOCK.defaultBlockState(),
                    Blocks.GOLD_ORE.defaultBlockState(),
                    Blocks.DIAMOND_ORE.defaultBlockState(),
                    Blocks.DIAMOND_BLOCK.defaultBlockState(),
                    Blocks.ICE.defaultBlockState(),
                    Blocks.NETHERRACK.defaultBlockState(),
                    Blocks.LAPIS_ORE.defaultBlockState(),
                    Blocks.LAPIS_BLOCK.defaultBlockState(),
                    Blocks.REDSTONE_ORE.defaultBlockState(),
                    Blocks.RAIL.defaultBlockState(),
                    Blocks.DETECTOR_RAIL.defaultBlockState(),
                    Blocks.POWERED_RAIL.defaultBlockState(),
                    Blocks.ACTIVATOR_RAIL.defaultBlockState(),
                    Blocks.GRASS_BLOCK.defaultBlockState(),
                    Blocks.DIRT.defaultBlockState(),
                    Blocks.SAND.defaultBlockState(),
                    Blocks.GRAVEL.defaultBlockState(),
                    Blocks.POWDER_SNOW.defaultBlockState(),
                    Blocks.SNOW.defaultBlockState(),
                    Blocks.OAK_PLANKS.defaultBlockState(),
                    Blocks.BOOKSHELF.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.CHEST.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.OAK_LEAVES.defaultBlockState(),
                    Blocks.DARK_OAK_LEAVES.defaultBlockState(),
                    Blocks.CLAY.defaultBlockState(),
                    Blocks.FARMLAND.defaultBlockState(),
                    Blocks.SOUL_SAND.defaultBlockState(),
                    Blocks.MYCELIUM.defaultBlockState()
            ),

            ImmutableSet.of("pickaxe", "shovel", "axe"),
            Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE),
            Arrays.asList(
                    EnumOperations.DEFAULT
            )
    ),
    ULT_VAJRA(
            Sets.newHashSet(
                    Blocks.COBBLESTONE.defaultBlockState(),
                    Blocks.SMOOTH_STONE_SLAB.defaultBlockState(),
                    Blocks.STONE_SLAB.defaultBlockState(),
                    Blocks.STONE.defaultBlockState(),
                    Blocks.SANDSTONE.defaultBlockState(),
                    Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
                    Blocks.IRON_ORE.defaultBlockState(),
                    Blocks.IRON_BLOCK.defaultBlockState(),
                    Blocks.COAL_ORE.defaultBlockState(),
                    Blocks.GOLD_BLOCK.defaultBlockState(),
                    Blocks.GOLD_ORE.defaultBlockState(),
                    Blocks.DIAMOND_ORE.defaultBlockState(),
                    Blocks.DIAMOND_BLOCK.defaultBlockState(),
                    Blocks.ICE.defaultBlockState(),
                    Blocks.NETHERRACK.defaultBlockState(),
                    Blocks.LAPIS_ORE.defaultBlockState(),
                    Blocks.LAPIS_BLOCK.defaultBlockState(),
                    Blocks.REDSTONE_ORE.defaultBlockState(),
                    Blocks.RAIL.defaultBlockState(),
                    Blocks.DETECTOR_RAIL.defaultBlockState(),
                    Blocks.POWERED_RAIL.defaultBlockState(),
                    Blocks.ACTIVATOR_RAIL.defaultBlockState(),
                    Blocks.GRASS_BLOCK.defaultBlockState(),
                    Blocks.DIRT.defaultBlockState(),
                    Blocks.SAND.defaultBlockState(),
                    Blocks.GRAVEL.defaultBlockState(),
                    Blocks.POWDER_SNOW.defaultBlockState(),
                    Blocks.SNOW.defaultBlockState(),
                    Blocks.OAK_PLANKS.defaultBlockState(),
                    Blocks.BOOKSHELF.defaultBlockState(),
                    Blocks.OAK_LOG.defaultBlockState(),
                    Blocks.DARK_OAK_LOG.defaultBlockState(),
                    Blocks.CHEST.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.PUMPKIN_STEM.defaultBlockState(),
                    Blocks.OAK_LEAVES.defaultBlockState(),
                    Blocks.DARK_OAK_LEAVES.defaultBlockState(),
                    Blocks.CLAY.defaultBlockState(),
                    Blocks.FARMLAND.defaultBlockState(),
                    Blocks.SOUL_SAND.defaultBlockState(),
                    Blocks.MYCELIUM.defaultBlockState()
            ),

            ImmutableSet.of("pickaxe", "shovel", "axe"),
            Arrays.asList(BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.MINEABLE_WITH_SHOVEL, BlockTags.MINEABLE_WITH_AXE),
            Arrays.asList(
                    EnumOperations.DEFAULT,
                    EnumOperations.BIGHOLES,
                    EnumOperations.MEGAHOLES,
                    EnumOperations.ULTRAHOLES,
                    EnumOperations.ORE,
                    EnumOperations.TUNNEL,
                    EnumOperations.TREE
            )
    );

    private final Set<BlockState> mineableBlocks;
    private final Set<String> toolType;
    private final List<TagKey<Block>> listItems;
    private final List<EnumOperations> listOperations;
    private final List<EnumInfoUpgradeModules> enumInfoUpgradeModules;
    private final String type_name;

    EnumTypeInstruments(
            Set<BlockState> mineableBlocks, Set<String> toolType, List<TagKey<Block>> listItems,
            List<EnumOperations> listOperations
    ) {
        this.type_name = null;
        this.mineableBlocks = mineableBlocks;
        this.toolType = toolType;
        this.listItems = listItems;
        this.listOperations = listOperations;
        this.enumInfoUpgradeModules = EnumUpgrades.INSTRUMENTS.list;
    }

    EnumTypeInstruments(
            String type_name,
            Set<BlockState> mineableBlocks, Set<String> toolType, List<TagKey<Block>> listItems,
            List<EnumOperations> listOperations
    ) {
        this.type_name = type_name;
        this.mineableBlocks = mineableBlocks;
        this.toolType = toolType;
        this.listItems = listItems;
        this.listOperations = listOperations;
        this.enumInfoUpgradeModules = EnumUpgrades.INSTRUMENTS.list;
    }

    public List<EnumInfoUpgradeModules> getEnumInfoUpgradeModules() {
        return enumInfoUpgradeModules;
    }

    public String getType_name() {
        return type_name;
    }

    public List<EnumOperations> getListOperations() {
        return listOperations;
    }

    public List<TagKey<Block>> getListItems() {
        return listItems;
    }

    public Set<BlockState> getMineableBlocks() {
        return mineableBlocks;
    }


    public Set<String> getToolType() {
        return toolType;
    }
}
