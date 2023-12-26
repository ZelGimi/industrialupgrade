package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.IULeaves;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

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
                                BlockPos blockpos = new BlockPos(position.getX() + offset[0], i2, position.getZ() + offset[1]);
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
                                BlockPos blockpos = new BlockPos(position.getX() + offset[0], i2, position.getZ() + offset[1]);
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
                                            this.setBlockAndNotifyAdequately(worldIn, blockpos, Blocks.AIR.getDefaultState());
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
                    int treeholechance = 35;
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
                                                        BlockRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(4)])
                                                )
                                );
                            } else {
                                this.setBlockAndNotifyAdequately(
                                        worldIn,
                                        position.up(j2),
                                        woodBlock
                                                .getDefaultState()
                                                .withProperty(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y)
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


}
