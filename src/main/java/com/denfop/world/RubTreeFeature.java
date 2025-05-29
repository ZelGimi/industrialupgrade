package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.utils.ModUtils;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

import java.util.*;

public class RubTreeFeature extends Feature<NoneFeatureConfiguration> {

    public RubTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    protected boolean canGrowInto(Block blockType) {
        BlockState material = blockType.defaultBlockState();
        return material.isAir() || material.is(BlockTags.LEAVES) || blockType == Blocks.GRASS_BLOCK || blockType == Blocks.DIRT || blockType.defaultBlockState().is(BlockTags.LOGS) || blockType.defaultBlockState().is(BlockTags.LEAVES) || blockType.defaultBlockState().is(BlockTags.SAPLINGS) || blockType == Blocks.VINE || blockType == IUItem.rubWood.getBlock().get() || blockType == IUItem.leaves.getBlock().get();
    }


    public boolean placeInstantly(ServerLevel pLevel, ChunkGenerator generator, BlockState pState, RandomSource pRandom, BlockPos pPos) {
        int i =pRandom.nextInt(5) + pRandom.nextInt(4) + pRandom.nextInt(2);
        WorldGenLevel worldIn =pLevel;
        BlockPos position = pPos;
        RandomSource rand =pRandom;
        if (i <= 3) {
            i = 4;
        }
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockState state = worldIn.getBlockState(position.offset(x, 0, z));
                if (state.getMapColor(pLevel,position.offset(x, 0, z)) == MapColor.WOOD || state.getBlock() == IUItem.rubWood.getBlock().get()) {
                    return false;
                }
            }
        }
        boolean flag = true;
        Block woodBlock = IUItem.rubWood.getBlock().get();
        BlockState leaves = IUItem.leaves.getDefaultState();

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;

                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < worldIn.getHeight()) {
                            if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.set(l, j, i1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = position.below();
                BlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(
                        state,
                        worldIn,
                        down,
                        Direction.UP,
                        IUItem.rubberSapling.getBlock().get()
                );

                if (isSoil && position.getY() < worldIn.getHeight() - i - 1) {
                    this.onPlantGrow(state, worldIn, down, position);
                    Holder<Biome> biome = worldIn.getBiome(position);


                    if (!biome.is(Tags.Biomes.IS_SWAMP) && !biome.is(BiomeTags.IS_JUNGLE)) {
                        for (int i2 = position.getY() - i / 2 + i; i2 <= position.getY() + i + 1 + rand.nextInt(3); ++i2) {
                            int k2 = i2 - (position.getY() + i + 1);
                            int l2 = 3;
                            if (i2 >= position.getY() + i) {
                                l2 = 1;
                            }
                            if (i2 == position.getY() + i) {
                                int[][] offsets = {
                                        {-1, 0}, {0, -1}, {1, 0}, {0, 1}
                                };

                                for (int[] offset : offsets) {
                                    BlockPos blockpos = new BlockPos(
                                            position.getX() + offset[0],
                                            i2,
                                            position.getZ() + offset[1]
                                    );
                                    BlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.isAir()) {
                                        worldIn.setBlock(blockpos, leaves, 3);
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i + 1) {
                                l2 = 3;
                                int[][] offsets = {
                                        {-2, 0}, {0, -2}, {2, 0}, {0, 2}
                                };

                                for (int[] offset : offsets) {
                                    BlockPos blockpos = new BlockPos(
                                            position.getX() + offset[0],
                                            i2,
                                            position.getZ() + offset[1]
                                    );
                                    BlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.isAir()) {
                                        worldIn.setBlock(blockpos, leaves, 3);
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i) {
                                l2 = 4;
                                if (i > 6) {
                                    for (int i3 = position.getX() - l2 / 2; i3 <= position.getX() + l2 / 2; ++i3) {
                                        int j1 = i3 - position.getX();

                                        for (int k1 = position.getZ() - l2 / 2; k1 <= position.getZ() + l2 / 2; ++k1) {
                                            int l1 = k1 - position.getZ();

                                            if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                                BlockPos blockpos = new BlockPos(i3, i2 - 1, k1);
                                                BlockState state2 = worldIn.getBlockState(blockpos);

                                                if (state2.isAir()) {
                                                    worldIn.setBlock(blockpos, leaves, 3);
                                                }
                                            }
                                        }
                                    }
                                    for (int i3 = position.getX() - 2; i3 <= position.getX() + 2; i3 += 4) {
                                        int j1 = i3 - position.getX();

                                        for (int k1 = position.getZ() - 2; k1 <= position.getZ() + 2; k1 += 4) {
                                            int l1 = k1 - position.getZ();

                                            if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                                BlockPos blockpos = new BlockPos(i3, i2 - 1, k1);
                                                BlockState state2 = worldIn.getBlockState(blockpos);
                                                if (state2.isAir()) {
                                                    this.setBlock(
                                                            worldIn,
                                                            blockpos,
                                                            Blocks.AIR.defaultBlockState()
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                                l2 = 4;
                            }

                            for (int i3 = position.getX() - l2 / 2; i3 <= position.getX() + l2 / 2; ++i3) {
                                int j1 = i3 - position.getX();

                                for (int k1 = position.getZ() - l2 / 2; k1 <= position.getZ() + l2 / 2; ++k1) {
                                    int l1 = k1 - position.getZ();

                                    if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                        BlockPos blockpos = new BlockPos(i3, i2, k1);
                                        BlockState state2 = worldIn.getBlockState(blockpos);

                                        if (state2.isAir()) {
                                            this.setBlock(worldIn, blockpos, leaves);
                                        }
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i) {
                                for (int i3 = position.getX() - 2; i3 <= position.getX() + 2; i3 += 4) {
                                    int j1 = i3 - position.getX();

                                    for (int k1 = position.getZ() - 2; k1 <= position.getZ() + 2; k1 += 4) {
                                        int l1 = k1 - position.getZ();

                                        if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                            BlockPos blockpos = new BlockPos(i3, i2, k1);
                                            BlockState state2 = worldIn.getBlockState(blockpos);
                                            if (state2.isAir()) {
                                                this.setBlock(worldIn, blockpos, Blocks.AIR.defaultBlockState());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        int treeholechance = 40;
                        for (int j2 = 0; j2 < i; ++j2) {
                            BlockPos upN = position.above(j2);
                            BlockState state2 = worldIn.getBlockState(upN);

                            if (state2.isAir() || state2.is(
                                    BlockTags.LEAVES
                            ) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                                if (rand.nextInt(100) <= treeholechance) {
                                    treeholechance -= 10;
                                    this.setBlock(worldIn, position.above(j2),
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                                    4)])
                                                    )
                                    );
                                } else {
                                    this.setBlock(
                                            worldIn,
                                            position.above(j2),
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.plain_y
                                                    )
                                    );
                                }
                            }
                        }
                    } else if (biome.is(Tags.Biomes.IS_SWAMP)) {
                        Map<BlockPos, BlockState> map = new HashMap<>();
                        boolean create = createSwampTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                                this.setBlock(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }
                        return create;
                    } else {
                        Map<BlockPos, BlockState> map = new HashMap<>();
                        boolean create = createRubTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                                this.setBlock(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }
                        return create;

                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159749_) {

        List<Holder<Biome>> biomes = new LinkedList<>();

        int rubberTrees;

        ChunkPos chunkPos = new ChunkPos(p_159749_.origin());
        for (rubberTrees = 0; rubberTrees < 5; ++rubberTrees) {
            int x = chunkPos.getMinBlockX() + 8 + (rubberTrees & 2) * 15;
            int i = chunkPos.getMinBlockZ() + 8 + ((rubberTrees & 2) >>> 1) * 15;
            BlockPos pos = new BlockPos(x, 0, i);
            biomes.add(p_159749_.level().getBiome(pos));
        }

        rubberTrees = 0;


        for (Holder<Biome> biome : biomes) {
            if (biome != null) {
                if (biome.is(Tags.Biomes.IS_SWAMP)) {
                    rubberTrees += WorldBaseGen.random.nextInt(10) + 2;
                }
                if (biome.is(BiomeTags.IS_JUNGLE)) {
                    rubberTrees += WorldBaseGen.random.nextInt(15) + 5;
                }
                if (biome.is(BiomeTags.IS_FOREST)) {
                    rubberTrees += WorldBaseGen.random.nextInt(5) + 1;
                }
            }
        }


        rubberTrees = Math.round((float) rubberTrees * 2);
        rubberTrees /= 2;
        if (!(WorldBaseGen.random.nextInt(100) < rubberTrees))
            return false;
        int i = p_159749_.random().nextInt(5) + p_159749_.random().nextInt(4) + p_159749_.random().nextInt(2);
        WorldGenLevel worldIn = p_159749_.level();
        BlockPos position = p_159749_.origin();
        RandomSource rand = p_159749_.random();
        if (i <= 3) {
            i = 4;
        }
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockState state = worldIn.getBlockState(position.offset(x, 0, z));
                if (state.getMapColor(worldIn,position.offset(x, 0, z)) == MapColor.WOOD || state.getBlock() == IUItem.rubWood.getBlock().get()) {
                    return false;
                }
            }
        }
        boolean flag = true;
        Block woodBlock = IUItem.rubWood.getBlock().get();
        BlockState leaves = IUItem.leaves.getDefaultState();

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;

                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < worldIn.getHeight()) {
                            if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.set(l, j, i1))) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = position.below();
                BlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(
                        state,
                        worldIn,
                        down,
                        Direction.UP,
                        IUItem.rubberSapling.getBlock().get()
                );

                if (isSoil && position.getY() < worldIn.getHeight() - i - 1) {
                    this.onPlantGrow(state, worldIn, down, position);
                    Holder<Biome> biome = worldIn.getBiome(position);


                    if (!biome.is(Tags.Biomes.IS_SWAMP) && !biome.is(BiomeTags.IS_JUNGLE)) {
                        for (int i2 = position.getY() - i / 2 + i; i2 <= position.getY() + i + 1 + rand.nextInt(3); ++i2) {
                            int k2 = i2 - (position.getY() + i + 1);
                            int l2 = 3;
                            if (i2 >= position.getY() + i) {
                                l2 = 1;
                            }
                            if (i2 == position.getY() + i) {
                                int[][] offsets = {
                                        {-1, 0}, {0, -1}, {1, 0}, {0, 1}
                                };

                                for (int[] offset : offsets) {
                                    BlockPos blockpos = new BlockPos(
                                            position.getX() + offset[0],
                                            i2,
                                            position.getZ() + offset[1]
                                    );
                                    BlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.isAir()) {
                                        worldIn.setBlock(blockpos, leaves, 3);
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i + 1) {
                                l2 = 3;
                                int[][] offsets = {
                                        {-2, 0}, {0, -2}, {2, 0}, {0, 2}
                                };

                                for (int[] offset : offsets) {
                                    BlockPos blockpos = new BlockPos(
                                            position.getX() + offset[0],
                                            i2,
                                            position.getZ() + offset[1]
                                    );
                                    BlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.isAir()) {
                                        worldIn.setBlock(blockpos, leaves, 3);
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i) {
                                l2 = 4;
                                if (i > 6) {
                                    for (int i3 = position.getX() - l2 / 2; i3 <= position.getX() + l2 / 2; ++i3) {
                                        int j1 = i3 - position.getX();

                                        for (int k1 = position.getZ() - l2 / 2; k1 <= position.getZ() + l2 / 2; ++k1) {
                                            int l1 = k1 - position.getZ();

                                            if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                                BlockPos blockpos = new BlockPos(i3, i2 - 1, k1);
                                                BlockState state2 = worldIn.getBlockState(blockpos);

                                                if (state2.isAir()) {
                                                    worldIn.setBlock(blockpos, leaves, 3);
                                                }
                                            }
                                        }
                                    }
                                    for (int i3 = position.getX() - 2; i3 <= position.getX() + 2; i3 += 4) {
                                        int j1 = i3 - position.getX();

                                        for (int k1 = position.getZ() - 2; k1 <= position.getZ() + 2; k1 += 4) {
                                            int l1 = k1 - position.getZ();

                                            if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                                BlockPos blockpos = new BlockPos(i3, i2 - 1, k1);
                                                BlockState state2 = worldIn.getBlockState(blockpos);
                                                if (state2.isAir()) {
                                                    this.setBlock(
                                                            worldIn,
                                                            blockpos,
                                                            Blocks.AIR.defaultBlockState()
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                                l2 = 4;
                            }

                            for (int i3 = position.getX() - l2 / 2; i3 <= position.getX() + l2 / 2; ++i3) {
                                int j1 = i3 - position.getX();

                                for (int k1 = position.getZ() - l2 / 2; k1 <= position.getZ() + l2 / 2; ++k1) {
                                    int l1 = k1 - position.getZ();

                                    if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                        BlockPos blockpos = new BlockPos(i3, i2, k1);
                                        BlockState state2 = worldIn.getBlockState(blockpos);

                                        if (state2.isAir()) {
                                            this.setBlock(worldIn, blockpos, leaves);
                                        }
                                    }
                                }
                            }
                            if (i2 == position.getY() - i / 2 + i) {
                                for (int i3 = position.getX() - 2; i3 <= position.getX() + 2; i3 += 4) {
                                    int j1 = i3 - position.getX();

                                    for (int k1 = position.getZ() - 2; k1 <= position.getZ() + 2; k1 += 4) {
                                        int l1 = k1 - position.getZ();

                                        if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
                                            BlockPos blockpos = new BlockPos(i3, i2, k1);
                                            BlockState state2 = worldIn.getBlockState(blockpos);
                                            if (state2.isAir()) {
                                                this.setBlock(worldIn, blockpos, Blocks.AIR.defaultBlockState());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        int treeholechance = 40;
                        for (int j2 = 0; j2 < i; ++j2) {
                            BlockPos upN = position.above(j2);
                            BlockState state2 = worldIn.getBlockState(upN);

                            if (state2.isAir() || state2.is(
                                    BlockTags.LEAVES
                            ) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                                if (rand.nextInt(100) <= treeholechance) {
                                    treeholechance -= 10;
                                    this.setBlock(worldIn, position.above(j2),
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                                    4)])
                                                    )
                                    );
                                } else {
                                    this.setBlock(
                                            worldIn,
                                            position.above(j2),
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.plain_y
                                                    )
                                    );
                                }
                            }
                        }
                    } else if (biome.is(Tags.Biomes.IS_SWAMP)) {
                        Map<BlockPos, BlockState> map = new HashMap<>();
                        boolean create = createSwampTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                                this.setBlock(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }
                        return create;
                    } else {
                        Map<BlockPos, BlockState> map = new HashMap<>();
                        boolean create = createRubTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                                this.setBlock(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }
                        return create;

                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private boolean createRubTree(Random rand, BlockPos position, WorldGenLevel worldIn, Map<BlockPos, BlockState> map) {
        int max = 6 + WorldBaseGen.random.nextInt(3);
        int treeholechance = 90;
        final BlockTropicalRubWood woodBlock = IUItem.tropicalRubWood.getBlock().get();
        BlockState leaves = IUItem.leaves.getDefaultState();
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                    continue;
                }
                BlockPos upN = position.below().offset(x, 0, z);
                BlockTropicalRubWood.RubberWoodState rubberWoodState;
                if (x == 1 || x == -1) {
                    rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_x;
                } else {
                    rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_z;
                }
                final BlockPos pos2 = upN;
                map.put(pos2, woodBlock
                        .defaultBlockState()
                        .setValue(
                                BlockTropicalRubWood.stateProperty,
                                rubberWoodState
                        ));

            }
        }
        for (int j2 = 0; j2 < max; ++j2) {
            BlockPos upN = position.above(j2);
            BlockState state2 = worldIn.getBlockState(upN);
            if (j2 == max - 1) {
                BlockPos upN1;
                for (int x = -2; x < 3; x++) {
                    for (int z = -2; z < 3; z++) {
                        if (x == 0 && z == 0) {
                            continue;
                        }
                        if (!(z == 0 || x == 0)) {
                            continue;
                        }
                        upN1 = upN.offset(x, 0, z);
                        BlockTropicalRubWood.RubberWoodState rubberWoodState;
                        if (x != 0) {
                            rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_x;
                        } else {
                            rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_z;
                        }
                        BlockPos pos2 = upN1;
                        map.put(pos2, woodBlock
                                .defaultBlockState()
                                .setValue(
                                        BlockTropicalRubWood.stateProperty,
                                        rubberWoodState
                                ));
                        upN1 = upN1.above();
                        if (x <= -1) {
                            upN1 = upN1.offset(-1, 0, 0);
                        } else if (z <= -1) {
                            upN1 = upN1.offset(0, 0, -1);
                        } else if (x >= 1) {
                            upN1 = upN1.offset(1, 0, 0);
                        } else if (z >= 1) {
                            upN1 = upN1.offset(0, 0, 1);
                        }
                        pos2 = upN1;
                        map.put(pos2, woodBlock
                                .defaultBlockState()
                                .setValue(
                                        BlockTropicalRubWood.stateProperty,
                                        rubberWoodState
                                ));


                        for (int xx = -1; xx < 2; xx++) {
                            for (int zz = -1; zz < 2; zz++) {
                                if (xx == 0 && zz == 0) {
                                    continue;
                                }
                                if (xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1) {
                                    continue;
                                }
                                BlockPos upN2 = upN1.offset(xx, 0, zz);
                                map.put(upN2, leaves);
                            }
                        }

                        upN1 = upN1.above();
                        pos2 = upN1;
                        map.put(pos2, leaves);
                        upN1 = upN1.below();
                        if (x == -2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.offset(0, yy, zz);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.offset(-1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.offset(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);
                                if (j == 0) {
                                    for (int zz = -1; zz < 2; zz++) {
                                        for (int yy = -1; yy < 2; yy++) {
                                            if (zz == 0 && yy == 0) {
                                                continue;
                                            }
                                            if ((zz == -1 && yy == -1 || zz == 1 && yy == 1 || zz == 1 && yy == -1 || zz == -1 && yy == 1)) {
                                                continue;
                                            }

                                            final BlockPos upN3 = upN1.offset(0, yy, zz);
                                            pos2 = upN3;
                                            map.put(pos2, leaves);
                                        }
                                    }
                                }
                            }

                        } else if (z == -2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.offset(xx, yy, 0);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.offset(0, 0, -1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.offset(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);
                            }
                        } else if (x == 2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.offset(0, yy, zz);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.offset(1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.offset(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);
                            }
                        } else if (z == 2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.offset(xx, yy, 0);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.offset(0, 0, 1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.offset(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);

                            }
                        }
                    }
                }
                upN1 = upN.above();
                for (int xx = -1; xx < 2; xx++) {
                    for (int zz = -1; zz < 2; zz++) {

                        if (!(xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1)) {
                            continue;
                        }
                        BlockPos upN2 = upN1.offset(xx, 0, zz);
                        map.put(upN2, leaves);
                    }
                }

                BlockPos pos2 = upN1;
                map.put(pos2, leaves);
                upN1 = upN.above().above();
                for (int xx = -1; xx < 2; xx++) {
                    for (int zz = -1; zz < 2; zz++) {

                        if (xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1) {
                            continue;
                        }
                        BlockPos upN2 = upN1.offset(xx, 0, zz);
                        map.put(upN2, leaves);
                    }
                }

            }

            if (state2.isAir() || state2.is(BlockTags.LEAVES) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                if (rand.nextInt(100) <= treeholechance && j2 < max - 1) {
                    treeholechance -= 10;
                    map.put(position.above(j2), woodBlock
                            .defaultBlockState()
                            .setValue(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));

                } else {
                    map.put(position.above(j2), woodBlock
                            .defaultBlockState()
                            .setValue(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.plain_y
                            ));

                }
            }
        }
        return true;
    }

    private boolean createSwampTree(Random rand, BlockPos position, WorldGenLevel worldIn, Map<BlockPos, BlockState> map) {
        int treeholechance = 60;
        BlockState leaves = IUItem.leaves.getDefaultState();
        BlockSwampRubWood woodBlock = IUItem.swampRubWood.getBlock().get();
        if (rand.nextInt(2) == 0) {
            final int max = 7 + rand.nextInt(4);
            Direction facing = null;
            for (int j2 = 0; j2 < max; ++j2) {
                BlockPos upN = position.above(j2);
                BlockState state2 = worldIn.getBlockState(upN);
                int type = WorldBaseGen.random.nextInt(3);
                if (WorldBaseGen.random.nextInt(100) <= 50) {
                    if (j2 > 1 && j2 != max - 1) {
                        if (type == 0) {
                            final Direction prevFacing = facing;
                            facing = Direction.values()[2 + WorldBaseGen.random.nextInt(4)];
                            while (facing == prevFacing) {
                                facing = Direction.values()[2 + WorldBaseGen.random.nextInt(4)];
                            }
                            BlockPos pos1 = upN.offset(facing.getNormal());
                            final BlockState state1 = worldIn.getBlockState(pos1);
                            if (state1.isAir() || state1.is(BlockTags.LEAVES) || state1.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                                BlockSwampRubWood.RubberWoodState rubberWoodState;
                                if (facing.getAxis() == Direction.Axis.X) {
                                    rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                                } else {
                                    rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                                }
                                map.put(pos1, woodBlock
                                        .defaultBlockState()
                                        .setValue(
                                                BlockSwampRubWood.stateProperty,
                                                rubberWoodState
                                        ));
                            } else {
                                return false;
                            }


                            for (int x = -1; x < 2; x++) {
                                for (int z = -1; z < 2; z++) {
                                    if (x == 0 && z == 0) {
                                        continue;
                                    }
                                    if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                                        continue;
                                    }

                                    final BlockPos pos2 = pos1.offset(x, 0, z);
                                    final BlockState state3 = worldIn.getBlockState(pos2);
                                    if (state3.isAir()) {
                                        map.put(pos2, leaves);
                                    } else {
                                        return false;
                                    }
                                }
                            }

                        } else if (type == 1) {
                            final Direction prevFacing = facing;
                            facing = Direction.values()[2 + WorldBaseGen.random.nextInt(4)];
                            while (facing == prevFacing) {
                                facing = Direction.values()[2 + WorldBaseGen.random.nextInt(4)];
                            }
                            BlockPos pos1 = upN;
                            int max1 = 2;
                            for (int ii = 0; ii < max1; ii++) {
                                pos1 = pos1.offset(facing.getNormal());
                                final BlockState state1 = worldIn.getBlockState(pos1);
                                if (state1.isAir() || state1.is(BlockTags.LEAVES) || state1.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                                    BlockSwampRubWood.RubberWoodState rubberWoodState;
                                    if (facing.getAxis() == Direction.Axis.X) {
                                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                                    } else {
                                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                                    }
                                    map.put(pos1, woodBlock
                                            .defaultBlockState()
                                            .setValue(
                                                    BlockSwampRubWood.stateProperty,
                                                    rubberWoodState
                                            ));
                                } else {
                                    return false;
                                }
                                for (int x = -1; x < 2; x++) {
                                    for (int z = -1; z < 2; z++) {
                                        if (x == 0 && z == 0) {
                                            continue;
                                        }
                                        if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                                            continue;
                                        }

                                        final BlockPos pos2 = pos1.offset(x, 0, z);
                                        final BlockState state3 = worldIn.getBlockState(pos2);
                                        if (state3.isAir()) {
                                            map.put(pos2, leaves);
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                            }

                            max1 = 1;
                            for (int ii = 0; ii < max1; ii++) {
                                pos1 = pos1.above();
                                if (rand.nextInt(100) <= treeholechance) {
                                    treeholechance -= 10;
                                    this.setBlock(worldIn, pos1,
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockSwampRubWood.stateProperty,
                                                            BlockSwampRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                                    4)])
                                                    )
                                    );
                                } else {
                                    this.setBlock(
                                            worldIn,
                                            pos1,
                                            woodBlock
                                                    .defaultBlockState()
                                                    .setValue(
                                                            BlockSwampRubWood.stateProperty,
                                                            BlockSwampRubWood.RubberWoodState.plain_y
                                                    )
                                    );
                                }
                                for (int x = -1; x < 2; x++) {
                                    for (int z = -1; z < 2; z++) {
                                        if (x == 0 && z == 0) {
                                            continue;
                                        }
                                        if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                                            continue;
                                        }

                                        final BlockPos pos2 = pos1.offset(x, 0, z);
                                        final BlockState state3 = worldIn.getBlockState(pos2);
                                        if (state3.isAir()) {
                                            map.put(pos2, leaves);
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                                final BlockPos pos2 = pos1.above();
                                final BlockState state3 = worldIn.getBlockState(pos2);
                                if (state3.isAir()) {
                                    map.put(pos2, leaves);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (state2.isAir() || state2.is(BlockTags.LEAVES) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                    if (rand.nextInt(100) <= treeholechance) {
                        treeholechance -= 10;
                        map.put(position.above(j2), woodBlock
                                .defaultBlockState()
                                .setValue(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    } else {
                        map.put(position.above(j2), woodBlock
                                .defaultBlockState()
                                .setValue(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.plain_y
                                ));
                    }
                    BlockPos pos = position.above(j2);
                    if (j2 >= 3) {
                        for (int x = -1; x < 2; x++) {
                            for (int z = -1; z < 2; z++) {
                                if (x == 0 && z == 0) {
                                    continue;
                                }
                                if (j2 >= max - 3) {
                                    if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                                        continue;
                                    }
                                }

                                final BlockPos pos1 = pos.offset(x, 0, z);
                                final BlockState state1 = worldIn.getBlockState(pos1);
                                if (state1.isAir()) {
                                    map.put(pos1, leaves);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                    if (j2 == max - 1) {
                        final BlockPos pos2 = pos.above();
                        final BlockState state3 = worldIn.getBlockState(pos2);
                        if (state3.isAir()) {
                            map.put(pos2, leaves);
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else {
            treeholechance = 60;
            woodBlock = IUItem.swampRubWood.getBlock().get();
            final int max = 5 + rand.nextInt(4);
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }
                    if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                        continue;
                    }
                    BlockPos upN = position.below().offset(x, 0, z);
                    BlockSwampRubWood.RubberWoodState rubberWoodState;
                    if (x == 1 || x == -1) {
                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                    } else {
                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                    }
                    final BlockPos pos2 = upN;
                    final BlockState state3 = worldIn.getBlockState(pos2);
                    if (state3.isAir()) {
                        return false;
                    }
                    map.put(pos2, woodBlock
                            .defaultBlockState()
                            .setValue(
                                    BlockSwampRubWood.stateProperty,
                                    rubberWoodState
                            ));
                }
            }
            for (int j2 = 0; j2 < max; ++j2) {
                BlockPos upN = position.above(j2);
                BlockState state2 = worldIn.getBlockState(upN);
                if (j2 < max - 1) {
                    if (state2.isAir() || state2.is(BlockTags.LEAVES) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                        if (rand.nextInt(100) <= treeholechance) {
                            treeholechance -= 10;
                            map.put(position.above(j2), woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                    4)])
                                    ));
                        } else {
                            map.put(position.above(j2), woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.plain_y
                                    ));
                        }
                    }
                } else if (j2 == max - 1) {
                    if (state2.isAir() || state2.is(BlockTags.LEAVES) || state2.getBlock() == IUItem.rubberSapling.getBlock().get()) {
                        if (rand.nextInt(100) <= treeholechance) {
                            treeholechance -= 10;
                            map.put(position.above(j2), woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.getWet(ModUtils.HORIZONTALS[rand.nextInt(
                                                    4)])
                                    ));
                        } else {
                            map.put(position.above(j2), woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.plain_y
                                    ));
                        }
                    }
                    BlockPos upN1;
                    upN1 = position.above(j2);
                    for (int y = 0; y < 5; y++) {
                        int radius = 0;
                        if (y < 3) {
                            radius = 3;
                        }
                        if (y == 3) {
                            radius = 1;
                        }
                        if (y == 4) {
                            radius = 0;
                        }
                        for (int x = -radius; x <= radius; x++) {
                            for (int z = -radius; z <= radius; z++) {
                                if (x * x + z * z <= radius * radius) {
                                    if (Math.abs(x) == Math.abs(z) && Math.abs(x) == 2) {
                                        continue;
                                    }
                                    if (y == 2 && ((Math.abs(z) == 0 && Math.abs(x) == 3) || (Math.abs(z) == 3 && Math.abs(
                                            x) == 0))) {
                                        continue;
                                    }
                                    BlockPos pos = upN1.offset(x, y, z);
                                    state2 = worldIn.getBlockState(pos);
                                    if (state2.isAir()) {
                                        map.put(pos, leaves);
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }
                    }

                    upN1 = position.above(j2).below().below();
                    for (int y = 0; y < 2; y++) {
                        for (int x = -1; x < 2; x++) {
                            for (int z = -1; z < 2; z++) {
                                if (x == 0 && z == 0) {
                                    continue;
                                }
                                if (y == 0) {
                                    if (Math.abs(x) == Math.abs(z)) {
                                        continue;
                                    }
                                } else {
                                    if (Math.abs(x) != Math.abs(z)) {
                                        continue;
                                    }
                                }
                                final BlockPos upN2 = upN1.offset(x, y, z);
                                final BlockState state3 = worldIn.getBlockState(upN2);
                                if (state3.isAir()) {
                                    map.put(upN2, leaves);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                    for (int x = -2; x < 3; x++) {
                        for (int z = -2; z < 3; z++) {
                            if (x == 0 && z == 0) {
                                continue;
                            }
                            if (!(z == 0 || x == 0)) {
                                continue;
                            }
                            upN1 = upN.offset(x, 0, z);
                            BlockSwampRubWood.RubberWoodState rubberWoodState;
                            if (x != 0) {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                            } else {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                            }
                            BlockPos pos2 = upN1;
                            BlockState state3 = worldIn.getBlockState(pos2);
                            if (state3.isAir()) {
                                return false;
                            }
                            map.put(pos2, woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));

                            upN1 = upN1.below();
                            state3 = worldIn.getBlockState(upN1);
                            if (state3.isAir()) {
                                map.put(upN1, leaves);
                            } else {
                                return false;
                            }
                            upN1 = upN1.above().above();
                            if (x == -1) {
                                upN1 = upN1.offset(-1, 0, 0);
                            } else if (z == -1) {
                                upN1 = upN1.offset(0, 0, -1);
                            } else if (x == 1) {
                                upN1 = upN1.offset(1, 0, 0);
                            } else if (z == 1) {
                                upN1 = upN1.offset(0, 0, 1);
                            } else {
                                continue;
                            }
                            rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_y;
                            pos2 = upN1;
                            state3 = worldIn.getBlockState(pos2);
                            if (!state3.isAir()) {
                                return false;
                            }
                            map.put(pos2, woodBlock
                                    .defaultBlockState()
                                    .setValue(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));

                        }
                    }
                }
            }
        }
        return true;
    }

    public void onPlantGrow(BlockState state, WorldGenLevel world, BlockPos pos, BlockPos source) {
        if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.FARMLAND) {
            world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
        }
    }

    public boolean isReplaceable(WorldGenLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isAir() || state.is(BlockTags.LEAVES) || state.is(BlockTags.LOGS) || canGrowInto(state.getBlock());
    }


}
