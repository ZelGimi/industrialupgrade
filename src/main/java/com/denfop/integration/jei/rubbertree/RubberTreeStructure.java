package com.denfop.integration.jei.rubbertree;

import com.denfop.IUItem;
import com.denfop.blocks.BlockRubWood;
import com.denfop.blocks.BlockSwampRubWood;
import com.denfop.blocks.BlockTropicalRubWood;
import com.denfop.blocks.IULeaves;
import com.denfop.world.WorldBaseGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RubberTreeStructure {

    public static List<Map<BlockPos, IBlockState>> rubberTreesList = new ArrayList<>();

    public static void init() {
        initFirstRubber();
        initTwoRubber();
        initThreeRubber();
        initFourRubber();
    }

    private static void initFourRubber() {
        final BlockPos position = BlockPos.ORIGIN;
        final Random rand = new Random();
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        Map<BlockPos, IBlockState> stateMap = new HashMap<>();
        int max = 6;
        int treeholechance = 90;
        final BlockTropicalRubWood woodBlock = IUItem.tropicalRubWood;
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
                if (stateMap.containsKey(upN)) {
                    stateMap.replace(upN, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    rubberWoodState
                            ));
                } else {
                    stateMap.put(upN, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    rubberWoodState
                            ));
                }

            }
        }
        for (int j2 = 0; j2 < max; ++j2) {
            BlockPos upN = position.up(j2);

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
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockTropicalRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        } else {
                            stateMap.put(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockTropicalRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        }

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
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockTropicalRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        } else {
                            stateMap.put(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockTropicalRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        }

                        for (int xx = -1; xx < 2; xx++) {
                            for (int zz = -1; zz < 2; zz++) {
                                if (xx == 0 && zz == 0) {
                                    continue;
                                }
                                if (xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1) {
                                    continue;
                                }
                                BlockPos upN2 = upN1.add(xx, 0, zz);
                                if (stateMap.containsKey(upN2)) {
                                    stateMap.replace(upN2, leaves);
                                } else {
                                    stateMap.put(upN2, leaves);
                                }
                            }
                        }

                        upN1 = upN1.up();
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, leaves);
                        } else {
                            stateMap.put(upN1, leaves);
                        }
                        upN1 = upN1.down();
                        if (x == -2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(0, yy, zz);
                                    if (stateMap.containsKey(upN2)) {
                                        stateMap.replace(upN2, leaves);
                                    } else {
                                        stateMap.put(upN2, leaves);
                                    }
                                }
                            }
                            upN1 = upN1.add(-1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                if (stateMap.containsKey(upN2)) {
                                    stateMap.replace(upN2, leaves);
                                } else {
                                    stateMap.put(upN2, leaves);
                                }
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
                                            if (stateMap.containsKey(upN3)) {
                                                stateMap.replace(upN3, leaves);
                                            } else {
                                                stateMap.put(upN3, leaves);
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (z == -2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(xx, yy, 0);
                                    if (stateMap.containsKey(upN2)) {
                                        stateMap.replace(upN2, leaves);
                                    } else {
                                        stateMap.put(upN2, leaves);
                                    }
                                }
                            }
                            upN1 = upN1.add(0, 0, -1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                if (stateMap.containsKey(upN2)) {
                                    stateMap.replace(upN2, leaves);
                                } else {
                                    stateMap.put(upN2, leaves);
                                }
                            }
                        } else if (x == 2) {
                            for (int zz = -1; zz < 2; zz++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(0, yy, zz);
                                    if (stateMap.containsKey(upN2)) {
                                        stateMap.replace(upN2, leaves);
                                    } else {
                                        stateMap.put(upN2, leaves);
                                    }
                                }
                            }
                            upN1 = upN1.add(1, 0, 0);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                if (stateMap.containsKey(upN2)) {
                                    stateMap.replace(upN2, leaves);
                                } else {
                                    stateMap.put(upN2, leaves);
                                }
                            }
                        } else if (z == 2) {
                            for (int xx = -1; xx < 2; xx++) {
                                for (int yy = -1; yy < 2; yy++) {
                                    final BlockPos upN2 = upN1.add(xx, yy, 0);
                                    if (stateMap.containsKey(upN2)) {
                                        stateMap.replace(upN2, leaves);
                                    } else {
                                        stateMap.put(upN2, leaves);
                                    }
                                }
                            }
                            upN1 = upN1.add(0, 0, 1);
                            for (int j = 1; j > -4; j--) {
                                final BlockPos upN2 = upN1.add(0, j, 0);
                                if (stateMap.containsKey(upN2)) {
                                    stateMap.replace(upN2, leaves);
                                } else {
                                    stateMap.put(upN2, leaves);
                                }

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
                        if (stateMap.containsKey(upN2)) {
                            stateMap.replace(upN2, leaves);
                        } else {
                            stateMap.put(upN2, leaves);
                        }
                    }
                }

                if (stateMap.containsKey(upN1)) {
                    stateMap.replace(upN1, leaves);
                } else {
                    stateMap.put(upN1, leaves);
                }
                upN1 = upN.up().up();
                for (int xx = -1; xx < 2; xx++) {
                    for (int zz = -1; zz < 2; zz++) {

                        if (xx == -1 && zz == -1 || xx == 1 && zz == 1 || xx == 1 && zz == -1 || xx == -1 && zz == 1) {
                            continue;
                        }
                        BlockPos upN2 = upN1.add(xx, 0, zz);
                        if (stateMap.containsKey(upN2)) {
                            stateMap.replace(upN2, leaves);
                        } else {
                            stateMap.put(upN2, leaves);
                        }
                    }
                }

            }


            if (rand.nextInt(100) <= treeholechance && j2 < max - 1) {
                treeholechance -= 10;
                if (stateMap.containsKey(position.up(j2))) {
                    stateMap.replace(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));
                } else {
                    stateMap.put(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));
                }

            } else {
                if (stateMap.containsKey(position.up(j2))) {
                    stateMap.replace(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.plain_y
                            ));
                } else {
                    stateMap.put(position.up(j2), woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockTropicalRubWood.stateProperty,
                                    BlockTropicalRubWood.RubberWoodState.plain_y
                            ));
                }

            }

        }
        rubberTreesList.add(stateMap);
    }

    private static void initThreeRubber() {
        final BlockPos position = BlockPos.ORIGIN;
        final Random rand = new Random();
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        Map<BlockPos, IBlockState> stateMap = new HashMap<>();
        int treeholechance = 60;
        final BlockSwampRubWood woodBlock = IUItem.swampRubWood;
        final int max = 5;
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
                if (stateMap.containsKey(upN)) {
                    stateMap.replace(upN, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    rubberWoodState
                            ));
                } else {
                    stateMap.put(upN, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    rubberWoodState
                            ));
                }
            }
        }
        for (int j2 = 0; j2 < max; ++j2) {
            BlockPos upN = position.up(j2);

            if (j2 < max - 1) {

                if (rand.nextInt(100) <= treeholechance) {
                    treeholechance -= 10;
                    if (stateMap.containsKey(upN)) {
                        stateMap.replace(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    } else {
                        stateMap.put(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    }
                } else {
                    if (stateMap.containsKey(upN)) {
                        stateMap.replace(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.plain_y
                                ));
                    } else {
                        stateMap.put(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.plain_y
                                ));
                    }

                }

            } else if (j2 == max - 1) {

                if (rand.nextInt(100) <= treeholechance) {
                    treeholechance -= 10;
                    if (stateMap.containsKey(upN)) {
                        stateMap.replace(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    } else {
                        stateMap.put(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                4)])
                                ));
                    }
                } else {
                    if (stateMap.containsKey(upN)) {
                        stateMap.replace(upN, woodBlock
                                .getDefaultState()
                                .withProperty(
                                        BlockSwampRubWood.stateProperty,
                                        BlockSwampRubWood.RubberWoodState.plain_y
                                ));
                    } else {
                        stateMap.put(upN, woodBlock
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
                                if (y == 2 && ((Math.abs(z) == 0 && Math.abs(x) == 3) || (Math.abs(z) == 3 && Math.abs(x) == 0))) {
                                    continue;
                                }
                                BlockPos pos = upN1.add(x, y, z);
                                if (stateMap.containsKey(pos)) {
                                    stateMap.replace(pos, leaves);
                                } else {
                                    stateMap.put(pos, leaves);
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
                            if (stateMap.containsKey(upN2)) {
                                stateMap.replace(upN2, leaves);
                            } else {
                                stateMap.put(upN2, leaves);
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
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        } else {
                            stateMap.put(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        }
                        upN1 = upN1.down();
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, leaves);
                        } else {
                            stateMap.put(upN1, leaves);
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
                        if (stateMap.containsKey(upN1)) {
                            stateMap.replace(upN1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        } else {
                            stateMap.put(upN1, woodBlock
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

        rubberTreesList.add(stateMap);
    }

    private static void initTwoRubber() {
        final BlockPos position = BlockPos.ORIGIN;
        final Random rand = new Random();
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        Map<BlockPos, IBlockState> stateMap = new HashMap<>();
        int treeholechance = 60;
        final BlockSwampRubWood woodBlock = IUItem.swampRubWood;
        final int max = 7;
        EnumFacing facing = null;
        for (int j2 = 0; j2 < max; ++j2) {
            BlockPos upN = position.up(j2);
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

                        BlockSwampRubWood.RubberWoodState rubberWoodState;
                        if (facing.getAxis() == EnumFacing.Axis.X) {
                            rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                        } else {
                            rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                        }

                        if (stateMap.containsKey(pos1)) {
                            stateMap.replace(pos1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));
                        } else {
                            stateMap.put(pos1, woodBlock
                                    .getDefaultState()
                                    .withProperty(
                                            BlockSwampRubWood.stateProperty,
                                            rubberWoodState
                                    ));
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
                                if (stateMap.containsKey(pos2)) {
                                    stateMap.replace(pos2, leaves);
                                } else {
                                    stateMap.put(pos2, leaves);
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

                            BlockSwampRubWood.RubberWoodState rubberWoodState;
                            if (facing.getAxis() == EnumFacing.Axis.X) {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_x;
                            } else {
                                rubberWoodState = BlockSwampRubWood.RubberWoodState.plain_z;
                            }
                            if (stateMap.containsKey(pos1)) {
                                stateMap.replace(pos1, woodBlock
                                        .getDefaultState()
                                        .withProperty(
                                                BlockSwampRubWood.stateProperty,
                                                rubberWoodState
                                        ));
                            } else {
                                stateMap.put(pos1, woodBlock
                                        .getDefaultState()
                                        .withProperty(
                                                BlockSwampRubWood.stateProperty,
                                                rubberWoodState
                                        ));
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
                                    if (stateMap.containsKey(pos2)) {
                                        stateMap.replace(pos2, leaves);
                                    } else {
                                        stateMap.put(pos2, leaves);
                                    }
                                }
                            }
                        }

                        max1 = 1;
                        for (int ii = 0; ii < max1; ii++) {
                            pos1 = pos1.up();
                            if (rand.nextInt(100) <= treeholechance) {
                                treeholechance -= 10;
                                if (stateMap.containsKey(pos1)) {
                                    stateMap.replace(pos1, woodBlock
                                            .getDefaultState()
                                            .withProperty(
                                                    BlockSwampRubWood.stateProperty,
                                                    BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                            4)])
                                            ));
                                } else {
                                    stateMap.put(pos1, woodBlock
                                            .getDefaultState()
                                            .withProperty(
                                                    BlockSwampRubWood.stateProperty,
                                                    BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                                            4)])
                                            ));
                                }
                            } else {
                                if (stateMap.containsKey(pos1)) {
                                    stateMap.replace(pos1, woodBlock
                                            .getDefaultState()
                                            .withProperty(
                                                    BlockSwampRubWood.stateProperty,
                                                    BlockSwampRubWood.RubberWoodState.plain_y
                                            ));
                                } else {
                                    stateMap.put(pos1, woodBlock
                                            .getDefaultState()
                                            .withProperty(
                                                    BlockSwampRubWood.stateProperty,
                                                    BlockSwampRubWood.RubberWoodState.plain_y
                                            ));
                                }

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
                                    if (stateMap.containsKey(pos2)) {
                                        stateMap.replace(pos2, leaves);
                                    } else {
                                        stateMap.put(pos2, leaves);
                                    }
                                }
                            }
                            final BlockPos pos2 = pos1.up();
                            if (stateMap.containsKey(pos2)) {
                                stateMap.replace(pos2, leaves);
                            } else {
                                stateMap.put(pos2, leaves);
                            }
                        }
                    }
                }
            }
            if (rand.nextInt(100) <= treeholechance) {
                treeholechance -= 10;

                final BlockPos pos1 = position.up(j2);
                if (stateMap.containsKey(pos1)) {
                    stateMap.replace(pos1, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));
                } else {
                    stateMap.put(pos1, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    BlockSwampRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                            4)])
                            ));
                }
            } else {
                final BlockPos pos1 = position.up(j2);
                if (stateMap.containsKey(pos1)) {
                    stateMap.replace(pos1, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    BlockSwampRubWood.RubberWoodState.plain_y
                            ));
                } else {
                    stateMap.put(pos1, woodBlock
                            .getDefaultState()
                            .withProperty(
                                    BlockSwampRubWood.stateProperty,
                                    BlockSwampRubWood.RubberWoodState.plain_y
                            ));
                }
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

                        final BlockPos pos2 = pos.add(x, 0, z);
                        if (stateMap.containsKey(pos2)) {
                            stateMap.replace(pos2, leaves);
                        } else {
                            stateMap.put(pos2, leaves);
                        }
                    }
                }
            }
            if (j2 == max - 1) {
                final BlockPos pos2 = pos.up();
                if (stateMap.containsKey(pos2)) {
                    stateMap.replace(pos2, leaves);
                } else {
                    stateMap.put(pos2, leaves);
                }
            }

        }
        rubberTreesList.add(stateMap);
    }

    private static void initFirstRubber() {
        final BlockPos position = BlockPos.ORIGIN;
        final Random rand = new Random();
        IBlockState leaves = IUItem.leaves.getDefaultState().withProperty(IULeaves.typeProperty, IULeaves.LeavesType.rubber);
        Map<BlockPos, IBlockState> stateMap = new HashMap<>();
        int i = 5;
        if (i <= 3) {
            i = 4;
        }
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
                    if (stateMap.containsKey(blockpos)) {
                        stateMap.replace(blockpos, leaves);
                    } else {
                        stateMap.put(blockpos, leaves);
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
                    if (stateMap.containsKey(blockpos)) {
                        stateMap.replace(blockpos, leaves);
                    } else {
                        stateMap.put(blockpos, leaves);
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
                                if (stateMap.containsKey(blockpos)) {
                                    stateMap.replace(blockpos, leaves);
                                } else {
                                    stateMap.put(blockpos, leaves);
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
                                if (stateMap.containsKey(blockpos)) {
                                    stateMap.replace(blockpos, leaves);
                                } else {
                                    stateMap.put(blockpos, leaves);
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
                        if (stateMap.containsKey(blockpos)) {
                            stateMap.replace(blockpos, leaves);
                        } else {
                            stateMap.put(blockpos, leaves);
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
                            if (stateMap.containsKey(blockpos)) {
                                stateMap.replace(blockpos, leaves);
                            } else {
                                stateMap.put(blockpos, leaves);
                            }
                        }
                    }
                }
            }
        }
        Block woodBlock = IUItem.rubWood;
        int treeholechance = 40;
        for (int j2 = 0; j2 < i; ++j2) {

            if (rand.nextInt(100) <= treeholechance) {
                treeholechance -= 10;
                final IBlockState state = woodBlock
                        .getDefaultState()
                        .withProperty(
                                BlockRubWood.stateProperty,
                                BlockRubWood.RubberWoodState.getWet(EnumFacing.HORIZONTALS[rand.nextInt(
                                        4)])
                        );
                final BlockPos blockpos = position.up(j2);
                if (stateMap.containsKey(blockpos)) {
                    stateMap.replace(blockpos, state);
                } else {
                    stateMap.put(blockpos, state);
                }
            } else {
                final BlockPos blockpos = position.up(j2);
                final IBlockState state = woodBlock
                        .getDefaultState()
                        .withProperty(
                                BlockRubWood.stateProperty,
                                BlockRubWood.RubberWoodState.plain_y
                        );
                if (stateMap.containsKey(blockpos)) {
                    stateMap.replace(blockpos, state);
                } else {
                    stateMap.put(blockpos, state);
                }

            }
        }
        rubberTreesList.add(stateMap);
    }

}
