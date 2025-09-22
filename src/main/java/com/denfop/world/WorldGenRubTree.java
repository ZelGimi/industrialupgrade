package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.blocks.IULeaves;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldGenRubTree extends WorldGenAbstractTree {

    public WorldGenRubTree(boolean notify) {
        super(notify);
    }

    protected boolean canGrowInto(Block blockType) {
        Material material = blockType.getDefaultState().getMaterial();
        return material == Material.AIR || material == Material.LEAVES || blockType == Blocks.GRASS || blockType == Blocks.DIRT || blockType == Blocks.LOG || blockType == Blocks.LOG2 || blockType == Blocks.SAPLING || blockType == Blocks.VINE || blockType == IUItem.rubberSapling || blockType == IUItem.rubWood || blockType == IUItem.leaves;
    }

    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        int i = rand.nextInt(5) + rand.nextInt(4) + rand.nextInt(2);
        if (i <= 3) {
            i = 4;
        }
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                IBlockState state = worldIn.getBlockState(position.add(x, 0, z));
                if (state.getMaterial() == Material.WOOD || state.getBlock() == IUItem.rubWood) {
                    return false;
                }
            }
        }
        boolean flag = true;
        Block woodBlock = IUItem.rubWood;
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);

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
                            if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) {
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
                BlockPos down = position.down();
                IBlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(
                        state,
                        worldIn,
                        down,
                        net.minecraft.util.EnumFacing.UP,
                        (net.minecraft.block.BlockSapling) Blocks.SAPLING
                );

                if (isSoil && position.getY() < worldIn.getHeight() - i - 1) {
                    state.getBlock().onPlantGrow(state, worldIn, down, position);
                    Biome biome = worldIn.getBiome(position);


                    if (!(biome instanceof BiomeSwamp) && !(biome instanceof BiomeJungle)) {
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
                                    IBlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.getBlock().isAir(state2, worldIn, blockpos)) {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, leaves);
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
                                    IBlockState state2 = worldIn.getBlockState(blockpos);

                                    if (state2.getBlock().isAir(state2, worldIn, blockpos)) {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos, leaves);
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
                                                IBlockState state2 = worldIn.getBlockState(blockpos);

                                                if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(
                                                        state2,
                                                        worldIn,
                                                        blockpos
                                                )) {
                                                    this.setBlockAndNotifyAdequately(worldIn, blockpos, leaves);
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
                                                IBlockState state2 = worldIn.getBlockState(blockpos);
                                                if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(
                                                        state2,
                                                        worldIn,
                                                        blockpos
                                                )) {
                                                    this.setBlockAndNotifyAdequately(
                                                            worldIn,
                                                            blockpos,
                                                            Blocks.AIR.getDefaultState()
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
                                        IBlockState state2 = worldIn.getBlockState(blockpos);

                                        if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(
                                                state2,
                                                worldIn,
                                                blockpos
                                        )) {
                                            this.setBlockAndNotifyAdequately(worldIn, blockpos, leaves);
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
                                            IBlockState state2 = worldIn.getBlockState(blockpos);
                                            if (state2.getBlock().isAir(state2, worldIn, blockpos) || state2.getBlock().isAir(
                                                    state2,
                                                    worldIn,
                                                    blockpos
                                            )) {
                                                this.setBlockAndNotifyAdequately(worldIn, blockpos, Blocks.AIR.getDefaultState());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        int treeholechance = 40;
                        for (int j2 = 0; j2 < i; ++j2) {
                            BlockPos upN = position.up(j2);
                            IBlockState state2 = worldIn.getBlockState(upN);

                            if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(
                                    state2,
                                    worldIn,
                                    upN
                            ) || state2.getBlock() == IUItem.rubberSapling) {
                                if (rand.nextInt(100) <= treeholechance) {
                                    treeholechance -= 10;
                                    this.setBlockAndNotifyAdequately(worldIn, position.up(j2),
                                            woodBlock
                                                    .getDefaultState()
                                                    .withProperty(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                                    4)])
                                                    )
                                    );
                                } else {
                                    this.setBlockAndNotifyAdequately(
                                            worldIn,
                                            position.up(j2),
                                            woodBlock
                                                    .getDefaultState()
                                                    .withProperty(
                                                            BlockRubWood.stateProperty,
                                                            BlockRubWood.RubberWoodState.plain_y
                                                    )
                                    );
                                }
                            }
                        }
                    } else if (biome instanceof BiomeSwamp) {
                        Map<BlockPos, IBlockState> map = new HashMap<>();
                        boolean create = createSwampTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, IBlockState> entry : map.entrySet()) {
                                this.setBlockAndNotifyAdequately(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }
                    } else {
                        Map<BlockPos, IBlockState> map = new HashMap<>();
                        boolean create = createRubTree(WorldBaseGen.random, position, worldIn, map);
                        if (create) {
                            for (Map.Entry<BlockPos, IBlockState> entry : map.entrySet()) {
                                this.setBlockAndNotifyAdequately(
                                        worldIn,
                                        entry.getKey(),
                                        entry.getValue()
                                );
                            }
                        }

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

    private boolean createRubTree(Random rand, BlockPos position, World worldIn, Map<BlockPos, IBlockState> map) {
        int max = 6 + WorldBaseGen.random.nextInt(3);
        int treeholechance = 90;
        final BlockTropicalRubWood woodBlock = IUItem.tropicalRubWood;
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                    continue;
                }
                BlockPos upN = position.down().add(x, 0, z);
                BlockTropicalRubWood.RubberWoodState rubberWoodState;
                if (x == 1 || x == -1) {
                    rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_x;
                } else {
                    rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_z;
                }
                final BlockPos pos2 = upN;
                map.put(pos2, woodBlock
                        .getDefaultState()
                        .withProperty(
                                BlockTropicalRubWood.stateProperty,
                                rubberWoodState
                        ));

            }
        }
        for (int j2 = 0; j2 < max; ++j2) {
            BlockPos upN = position.up(j2);
            IBlockState state2 = worldIn.getBlockState(upN);
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
                        upN1 = upN.add(x, 0, z);
                        BlockTropicalRubWood.RubberWoodState rubberWoodState;
                        if (x != 0) {
                            rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_x;
                        } else {
                            rubberWoodState = BlockTropicalRubWood.RubberWoodState.plain_z;
                        }
                        BlockPos pos2 = upN1;
                        map.put(pos2, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockTropicalRubWood.stateProperty,
                                        rubberWoodState
                                ));
                        upN1 = upN1.up();
                        if (x <= -1) {
                            upN1 = upN1.add(-1, 0, 0);
                        } else if (z <= -1) {
                            upN1 = upN1.add(0, 0, -1);
                        } else if (x >= 1) {
                            upN1 = upN1.add(1, 0, 0);
                        } else if (z >= 1) {
                            upN1 = upN1.add(0, 0, 1);
                        }
                        pos2 = upN1;
                        map.put(pos2, woodBlock
                                .getDefaultState()
                                .withProperty(
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
                                BlockPos upN2 = upN1.add(xx, 0, zz);
                                map.put(upN2, leaves);
                            }
                        }

                        upN1 = upN1.up();
                        pos2 = upN1;
                        map.put(pos2, leaves);
                        upN1 = upN1.down();
                        if (x == -2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(0, yy, zz);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.add(-1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
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

                                            final BlockPos upN3 = upN1.add(0, yy, zz);
                                            pos2 = upN3;
                                            map.put(pos2, leaves);
                                        }
                                    }
                                }
                            }

                        } else if (z == -2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(xx, yy, 0);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.add(0, 0, -1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);
                            }
                        } else if (x == 2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(0, yy, zz);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.add(1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);
                            }
                        } else if (z == 2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(xx, yy, 0);
                                    pos2 = upN2;
                                    map.put(pos2, leaves);
                                }
                            }
                            upN1 = upN1.add(0, 0, 1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                pos2 = upN2;
                                map.put(pos2, leaves);

                            }
                        }
                    }
                }
                upN1 = upN.up();
                for (int xx = -1; xx < 2; xx++) {
                    for (int zz = -1; zz < 2; zz++) {

                        if (!(xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1)) {
                            continue;
                        }
                        BlockPos upN2 = upN1.add(xx, 0, zz);
                        map.put(upN2, leaves);
                    }
                }

                BlockPos pos2 = upN1;
                map.put(pos2, leaves);
                upN1 = upN.up().up();
                for (int xx = -1; xx < 2; xx++) {
                    for (int zz = -1; zz < 2; zz++) {

                        if (xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1) {
                            continue;
                        }
                        BlockPos upN2 = upN1.add(xx, 0, zz);
                        map.put(upN2, leaves);
                    }
                }

            }

            if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(
                    state2,
                    worldIn,
                    upN
            ) || state2.getBlock() == IUItem.rubberSapling) {
                if (rand.nextInt(100) <= treeholechance && j2 < max - 1) {
                    treeholechance -= 10;
                    map.put(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));

                } else {
                    map.put(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.plain_y
                            ));

                }
            }
        }
        return true;
    }

    private boolean createSwampTree(Random rand, BlockPos position, World worldIn, Map<BlockPos, IBlockState> map) {
        int treeholechance = 60;
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        BlockSwampRubWood woodBlock = IUItem.swampRubWood;
        if (rand.nextInt(2) == 0) {
            final int max = 7 + rand.nextInt(4);
            EnumFacing facing = null;
            for (int j2 = 0; j2 < max; ++j2) {
                BlockPos upN = position.up(j2);
                IBlockState state2 = worldIn.getBlockState(upN);
                int type = WorldBaseGen.random.nextInt(3);
                if (WorldBaseGen.random.nextInt(100) <= 50) {
                    if (j2 > 1 && j2 != max - 1) {
                        if (type == 0) {
                            final EnumFacing prevFacing = facing;
                            facing = EnumFacing.values()[2 + WorldBaseGen.random.nextInt(4)];
                            while (facing == prevFacing) {
                                facing = EnumFacing.values()[2 + WorldBaseGen.random.nextInt(4)];
                            }
                            BlockPos pos1 = upN.offset(facing);
                            final IBlockState state1 = worldIn.getBlockState(pos1);
                            if (state1.getBlock().isAir(state1, worldIn, pos1) || state1.getBlock().isLeaves(
                                    state1,
                                    worldIn,
                                    pos1
                            ) || state1.getBlock() == IUItem.rubberSapling) {
                                BlockSwampRubWood.RubberWoodState rubberWoodState;
                                if (facing.getAxis() == EnumFacing.Axis.X) {
                                    rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                                } else {
                                    rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                                }
                                map.put(pos1, woodBlock
                                        .getDefaultState()
                                        .withProperty(
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

                                    final BlockPos pos2 = pos1.add(x, 0, z);
                                    final IBlockState state3 = worldIn.getBlockState(pos2);
                                    if (state3.getMaterial() == Material.AIR) {
                                        map.put(pos2, leaves);
                                    } else {
                                        return false;
                                    }
                                }
                            }

                        } else if (type == 1) {
                            final EnumFacing prevFacing = facing;
                            facing = EnumFacing.values()[2 + WorldBaseGen.random.nextInt(4)];
                            while (facing == prevFacing) {
                                facing = EnumFacing.values()[2 + WorldBaseGen.random.nextInt(4)];
                            }
                            BlockPos pos1 = upN;
                            int max1 = 2;
                            for (int ii = 0; ii < max1; ii++) {
                                pos1 = pos1.offset(facing);
                                final IBlockState state1 = worldIn.getBlockState(pos1);
                                if (state1.getBlock().isAir(state1, worldIn, pos1) || state1.getBlock().isLeaves(
                                        state1,
                                        worldIn,
                                        pos1
                                ) || state1.getBlock() == IUItem.rubberSapling) {
                                    BlockSwampRubWood.RubberWoodState rubberWoodState;
                                    if (facing.getAxis() == EnumFacing.Axis.X) {
                                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                                    } else {
                                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                                    }
                                    map.put(pos1, woodBlock
                                            .getDefaultState()
                                            .withProperty(
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

                                        final BlockPos pos2 = pos1.add(x, 0, z);
                                        final IBlockState state3 = worldIn.getBlockState(pos2);
                                        if (state3.getMaterial() == Material.AIR) {
                                            map.put(pos2, leaves);
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                            }

                            max1 = 1;
                            for (int ii = 0; ii < max1; ii++) {
                                pos1 = pos1.up();
                                if (rand.nextInt(100) <= treeholechance) {
                                    treeholechance -= 10;
                                    this.setBlockAndNotifyAdequately(worldIn, pos1,
                                            woodBlock
                                                    .getDefaultState()
                                                    .withProperty(
                                                            BlockSwampRubWood.stateProperty,
                                                            BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                                    4)])
                                                    )
                                    );
                                } else {
                                    this.setBlockAndNotifyAdequately(
                                            worldIn,
                                            pos1,
                                            woodBlock
                                                    .getDefaultState()
                                                    .withProperty(
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

                                        final BlockPos pos2 = pos1.add(x, 0, z);
                                        final IBlockState state3 = worldIn.getBlockState(pos2);
                                        if (state3.getMaterial() == Material.AIR) {
                                            map.put(pos2, leaves);
                                        } else {
                                            return false;
                                        }
                                    }
                                }
                                final BlockPos pos2 = pos1.up();
                                final IBlockState state3 = worldIn.getBlockState(pos2);
                                if (state3.getMaterial() == Material.AIR) {
                                    map.put(pos2, leaves);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(
                        state2,
                        worldIn,
                        upN
                ) || state2.getBlock() == IUItem.rubberSapling) {
                    if (rand.nextInt(100) <= treeholechance) {
                        treeholechance -= 10;
                        map.put(position.up(j2), woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    } else {
                        map.put(position.up(j2), woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.plain_y
                                ));
                    }
                    BlockPos pos = position.up(j2);
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

                                final BlockPos pos1 = pos.add(x, 0, z);
                                final IBlockState state1 = worldIn.getBlockState(pos1);
                                if (state1.getMaterial() == Material.AIR) {
                                    map.put(pos1, leaves);
                                } else {
                                    return false;
                                }
                            }
                        }
                    }
                    if (j2 == max - 1) {
                        final BlockPos pos2 = pos.up();
                        final IBlockState state3 = worldIn.getBlockState(pos2);
                        if (state3.getMaterial() == Material.AIR) {
                            map.put(pos2, leaves);
                        } else {
                            return false;
                        }
                    }
                }
            }
        } else {
            treeholechance = 60;
            woodBlock = IUItem.swampRubWood;
            final int max = 5 + rand.nextInt(4);
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (x == 0 && z == 0) {
                        continue;
                    }
                    if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                        continue;
                    }
                    BlockPos upN = position.down().add(x, 0, z);
                    BlockSwampRubWood.RubberWoodState rubberWoodState;
                    if (x == 1 || x == -1) {
                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                    } else {
                        rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                    }
                    final BlockPos pos2 = upN;
                    final IBlockState state3 = worldIn.getBlockState(pos2);
                    if (state3.getMaterial() != Material.AIR) {
                        return false;
                    }
                    map.put(pos2, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    rubberWoodState
                            ));
                }
            }
            for (int j2 = 0; j2 < max; ++j2) {
                BlockPos upN = position.up(j2);
                IBlockState state2 = worldIn.getBlockState(upN);
                if (j2 < max - 1) {
                    if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(
                            state2,
                            worldIn,
                            upN
                    ) || state2.getBlock() == IUItem.rubberSapling) {
                        if (rand.nextInt(100) <= treeholechance) {
                            treeholechance -= 10;
                            map.put(position.up(j2), woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                    4)])
                                    ));
                        } else {
                            map.put(position.up(j2), woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.plain_y
                                    ));
                        }
                    }
                } else if (j2 == max - 1) {
                    if (state2.getBlock().isAir(state2, worldIn, upN) || state2.getBlock().isLeaves(
                            state2,
                            worldIn,
                            upN
                    ) || state2.getBlock() == IUItem.rubberSapling) {
                        if (rand.nextInt(100) <= treeholechance) {
                            treeholechance -= 10;
                            map.put(position.up(j2), woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                    4)])
                                    ));
                        } else {
                            map.put(position.up(j2), woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            BlockSwampRubWood.RubberWoodState.plain_y
                                    ));
                        }
                    }
                    BlockPos upN1;
                    upN1 = position.up(j2);
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
                                    BlockPos pos = upN1.add(x, y, z);
                                    state2 = worldIn.getBlockState(pos);
                                    if (state2.getMaterial() == Material.AIR) {
                                        map.put(pos, leaves);
                                    } else {
                                        return false;
                                    }
                                }
                            }
                        }
                    }

                    upN1 = position.up(j2).down().down();
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
                                final BlockPos upN2 = upN1.add(x, y, z);
                                final IBlockState state3 = worldIn.getBlockState(upN2);
                                if (state3.getMaterial() == Material.AIR) {
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
                            upN1 = upN.add(x, 0, z);
                            BlockSwampRubWood.RubberWoodState rubberWoodState;
                            if (x != 0) {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                            } else {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                            }
                            BlockPos pos2 = upN1;
                            IBlockState state3 = worldIn.getBlockState(pos2);
                            if (state3.getMaterial() != Material.AIR) {
                                return false;
                            }
                            map.put(pos2, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));

                            upN1 = upN1.down();
                            state3 = worldIn.getBlockState(upN1);
                            if (state3.getMaterial() == Material.AIR) {
                                map.put(upN1, leaves);
                            } else {
                                return false;
                            }
                            upN1 = upN1.up().up();
                            if (x == -1) {
                                upN1 = upN1.add(-1, 0, 0);
                            } else if (z == -1) {
                                upN1 = upN1.add(0, 0, -1);
                            } else if (x == 1) {
                                upN1 = upN1.add(1, 0, 0);
                            } else if (z == 1) {
                                upN1 = upN1.add(0, 0, 1);
                            } else {
                                continue;
                            }
                            rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_y;
                            pos2 = upN1;
                            state3 = worldIn.getBlockState(pos2);
                            if (state3.getMaterial() != Material.AIR) {
                                return false;
                            }
                            map.put(pos2, woodBlock
                                    .getDefaultState()
                                    .withProperty(
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


}
