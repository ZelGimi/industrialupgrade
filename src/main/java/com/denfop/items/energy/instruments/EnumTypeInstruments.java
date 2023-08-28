package com.denfop.items.energy.instruments;

import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.items.EnumInfoUpgradeModules;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public enum EnumTypeInstruments {
    SHOVEL(
            Sets
                    .newHashSet(
                            Blocks.GRASS.getDefaultState(),
                            Blocks.DIRT.getDefaultState(),
                            Blocks.SAND.getDefaultState(),
                            Blocks.GRAVEL.getDefaultState(),
                            Blocks.SNOW_LAYER.getDefaultState(),
                            Blocks.SNOW.getDefaultState(),
                            Blocks.CLAY.getDefaultState(),
                            Blocks.FARMLAND.getDefaultState(),
                            Blocks.SOUL_SAND.getDefaultState(),
                            Blocks.MYCELIUM.getDefaultState()
                    ),
            Sets.newHashSet(Material.GRASS, Material.GROUND,
                    Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY, Material.GLASS
            ),
            ImmutableSet.of("shovel"),
            Collections.singletonList(Items.DIAMOND_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES, EnumOperations.TUNNEL)
    ),
    PICKAXE(Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState()
    ), Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GLASS
    ), ImmutableSet.of("pickaxe"),
            Collections.singletonList(Items.DIAMOND_PICKAXE), Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES,
            EnumOperations.ORE, EnumOperations.TUNNEL
    )
    ),
    DRILL(Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.SAND.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
            Blocks.SNOW_LAYER.getDefaultState(),
            Blocks.SNOW.getDefaultState(),
            Blocks.CLAY.getDefaultState(),
            Blocks.FARMLAND.getDefaultState(),
            Blocks.SOUL_SAND.getDefaultState(),
            Blocks.MYCELIUM.getDefaultState()
    ), Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GRASS, Material.GROUND,
            Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES, EnumOperations.MEGAHOLES,
                    EnumOperations.ORE, EnumOperations.TUNNEL
            )
    ),
    SIMPLE_DRILL("drill", Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.SAND.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
            Blocks.SNOW_LAYER.getDefaultState(),
            Blocks.SNOW.getDefaultState(),
            Blocks.CLAY.getDefaultState(),
            Blocks.FARMLAND.getDefaultState(),
            Blocks.SOUL_SAND.getDefaultState(),
            Blocks.MYCELIUM.getDefaultState()
    ), Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GRASS, Material.GROUND,
            Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL),
            Collections.singletonList(EnumOperations.DEFAULT
            )
    ),
    DIAMOND_DRILL("drill", Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState(),
            Blocks.GRASS.getDefaultState(),
            Blocks.DIRT.getDefaultState(),
            Blocks.SAND.getDefaultState(),
            Blocks.GRAVEL.getDefaultState(),
            Blocks.SNOW_LAYER.getDefaultState(),
            Blocks.SNOW.getDefaultState(),
            Blocks.CLAY.getDefaultState(),
            Blocks.FARMLAND.getDefaultState(),
            Blocks.SOUL_SAND.getDefaultState(),
            Blocks.MYCELIUM.getDefaultState()
    ), Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GRASS, Material.GROUND,
            Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
    ), ImmutableSet.of("pickaxe", "shovel"), Arrays.asList(Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES
            )
    ),
    AXE(
            Sets.newHashSet(
                    Blocks.PLANKS.getDefaultState(),
                    Blocks.BOOKSHELF.getDefaultState(),
                    Blocks.LOG.getDefaultState(),
                    Blocks.LOG2.getDefaultState(),
                    Blocks.CHEST.getDefaultState(),
                    Blocks.PUMPKIN_STEM.getDefaultState(),
                    Blocks.LIT_PUMPKIN.getDefaultState(),
                    Blocks.LEAVES.getDefaultState(),
                    Blocks.LEAVES2.getDefaultState()
            ),
            Sets.newHashSet(Material.WOOD, Material.LEAVES,
                    Material.CORAL, Material.CACTUS, Material.PLANTS, Material.VINE
            ),
            ImmutableSet.of("axe"),
            Collections.singletonList(Items.DIAMOND_AXE),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.BIGHOLES,
                    EnumOperations.TREE
            )
    ),
    CHAINSAW(
            Sets.newHashSet(
                    Blocks.PLANKS.getDefaultState(),
                    Blocks.BOOKSHELF.getDefaultState(),
                    Blocks.LOG.getDefaultState(),
                    Blocks.LOG2.getDefaultState(),
                    Blocks.CHEST.getDefaultState(),
                    Blocks.PUMPKIN_STEM.getDefaultState(),
                    Blocks.LIT_PUMPKIN.getDefaultState(),
                    Blocks.LEAVES.getDefaultState(),
                    Blocks.LEAVES2.getDefaultState()
            ),
            Sets.newHashSet(Material.WOOD, Material.LEAVES,
                    Material.CORAL, Material.CACTUS, Material.PLANTS, Material.VINE
            ),
            ImmutableSet.of("axe", "shears"),
            Arrays.asList(Items.DIAMOND_AXE, Items.SHEARS),
            Arrays.asList(EnumOperations.DEFAULT, EnumOperations.SHEARS
            )
    ),
    PERFECT_DRILL(
            Sets.newHashSet(
                    Blocks.COBBLESTONE.getDefaultState(),
                    Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
                    Blocks.STONE_SLAB.getDefaultState(),
                    Blocks.STONE.getDefaultState(),
                    Blocks.SANDSTONE.getDefaultState(),
                    Blocks.MOSSY_COBBLESTONE.getDefaultState(),
                    Blocks.IRON_ORE.getDefaultState(),
                    Blocks.IRON_BLOCK.getDefaultState(),
                    Blocks.COAL_ORE.getDefaultState(),
                    Blocks.GOLD_BLOCK.getDefaultState(),
                    Blocks.GOLD_ORE.getDefaultState(),
                    Blocks.DIAMOND_ORE.getDefaultState(),
                    Blocks.DIAMOND_BLOCK.getDefaultState(),
                    Blocks.ICE.getDefaultState(),
                    Blocks.NETHERRACK.getDefaultState(),
                    Blocks.LAPIS_ORE.getDefaultState(),
                    Blocks.LAPIS_BLOCK.getDefaultState(),
                    Blocks.REDSTONE_ORE.getDefaultState(),
                    Blocks.LIT_REDSTONE_ORE.getDefaultState(),
                    Blocks.RAIL.getDefaultState(),
                    Blocks.DETECTOR_RAIL.getDefaultState(),
                    Blocks.GOLDEN_RAIL.getDefaultState(),
                    Blocks.ACTIVATOR_RAIL.getDefaultState(),
                    Blocks.GRASS.getDefaultState(),
                    Blocks.DIRT.getDefaultState(),
                    Blocks.SAND.getDefaultState(),
                    Blocks.GRAVEL.getDefaultState(),
                    Blocks.SNOW_LAYER.getDefaultState(),
                    Blocks.SNOW.getDefaultState(),
                    Blocks.PLANKS.getDefaultState(),
                    Blocks.BOOKSHELF.getDefaultState(),
                    Blocks.LOG.getDefaultState(),
                    Blocks.LOG2.getDefaultState(),
                    Blocks.CHEST.getDefaultState(),
                    Blocks.PUMPKIN_STEM.getDefaultState(),
                    Blocks.LIT_PUMPKIN.getDefaultState(),
                    Blocks.LEAVES.getDefaultState(),
                    Blocks.LEAVES2.getDefaultState(),
                    Blocks.CLAY.getDefaultState(),
                    Blocks.FARMLAND.getDefaultState(),
                    Blocks.SOUL_SAND.getDefaultState(),
                    Blocks.MYCELIUM.getDefaultState()
            ),
            Sets.newHashSet(Material.WOOD, Material.LEAVES,
                    Material.CORAL, Material.CACTUS, Material.PLANTS, Material.VINE, Material.IRON, Material.ANVIL,
                    Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE, Material.GRASS, Material.GROUND,
                    Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY
            ),
            ImmutableSet.of("pickaxe", "shovel", "axe"),
            Arrays.asList(Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE),
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

    private final Set<IBlockState> mineableBlocks;
    private final Set<Material> materials;
    private final Set<String> toolType;
    private final List<Item> listItems;
    private final List<EnumOperations> listOperations;
    private final List<EnumInfoUpgradeModules> enumInfoUpgradeModules;
    private final String type_name;

    EnumTypeInstruments(
            Set<IBlockState> mineableBlocks, Set<Material> materials, Set<String> toolType, List<Item> listItems,
            List<EnumOperations> listOperations
    ) {
        this.type_name = null;
        this.mineableBlocks = mineableBlocks;
        this.materials = materials;
        this.toolType = toolType;
        this.listItems = listItems;
        this.listOperations = listOperations;
        this.enumInfoUpgradeModules = EnumUpgrades.INSTRUMENTS.list;
    }

    EnumTypeInstruments(
            String type_name,
            Set<IBlockState> mineableBlocks, Set<Material> materials, Set<String> toolType, List<Item> listItems,
            List<EnumOperations> listOperations
    ) {
        this.type_name = type_name;
        this.mineableBlocks = mineableBlocks;
        this.materials = materials;
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

    public List<Item> getListItems() {
        return listItems;
    }

    public Set<IBlockState> getMineableBlocks() {
        return mineableBlocks;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public Set<String> getToolType() {
        return toolType;
    }
}
