package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.blocks.BlockBasalts;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.FluidName;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import static com.denfop.world.WorldBaseGen.veinTypes;

public class GeneratorVolcano {
    public static final int[][] protrusionOffsets = {
            {-1, 1, -1},
            {0, 2, -1},
            {1, 3, -1},
            {1, 4, 0},
            {1, 5, 1},
            {0, 6, 1},
            {-1, 7, 1},
            {-1, 8, 0},
            {0, 9, 0}
    };
    private static BlockState basalt_smooth = IUItem.basalts.getState(BlockBasalts.Type.basalt_smooth);
    private static BlockState basalt_sulfur_ore = IUItem.basalts.getState(BlockBasalts.Type.basalt_sulfur_ore);
    private static BlockState basalt_pylon = IUItem.basalts.getState(BlockBasalts.Type.basalt_pylon);
    private static BlockState basalt_magma = IUItem.basalts.getState(BlockBasalts.Type.basalt_magma);
    private static BlockState basalt_cobblestone = IUItem.basalts.getState(BlockBasalts.Type.basalt_cobblestone);
    private static BlockState basalt_melted = IUItem.basalts.getState(BlockBasalts.Type.basalt_melted);
    private static BlockState basalt = IUItem.basalts.getState(BlockBasalts.Type.basalt);
    private static BlockState basalt_boron_ore = IUItem.basalts.getState(BlockBasalts.Type.basalt_boron_ore);
    private static BlockState basalt_spongy = IUItem.basalts.getState(BlockBasalts.Type.basalt_spongy);
    private static BlockState basalt_blocked = IUItem.basalts.getState(BlockBasalts.Type.basalt_blocked);
    private static BlockState[][] basalts_ores = null;
    private final BlockPos position2;
    private final Thread thread;
    boolean genChest = false;
    private Level world;
    private BlockPos position;
    private Random rand;
    private LinkedList<BlockPos> blockPosList1;
    private LinkedList<BlockPos> blockPosList;
    private ChunkPos chunkPos;
    private ChunkAccess chunk;
    private int baseHeight;
    private int baseRadius;
    private double protrusionChance;
    private double lavaFlowChance;
    private double stalagmiteChance;
    private Map<ChunkPos, ChunkAccess> chunkPosChunkMap = new HashMap<>();
    private boolean end;
    private int y;
    private int maxbaseHeight;


    public GeneratorVolcano(FeaturePlaceContext<NoneFeatureConfiguration> p159749) {


        this.position2 = new BlockPos(p159749.origin().getX(), 30, p159749.origin().getZ());
        this.thread = new Thread(() -> {
            if (y == baseHeight) {
                while (!genChest) {
                    int index = rand.nextInt(blockPosList1.size());
                    if (rand.nextDouble() >= 0.95) {
                        BlockPos pos = blockPosList1.get(index);
                        while (true) {
                            if (!world.getBlockState(pos).isAir()) {
                                pos = pos.above();
                            }
                            // generateChest(pos);
                            break;
                        }
                        genChest = true;
                    } else {
                        BlockPos pos = blockPosList1.remove(index);
                        if (blockPosList1.isEmpty()) {
                            while (true) {
                                if (!world.getBlockState(pos).isAir()) {
                                    pos = pos.above();
                                }
                                //   generateChest(pos);
                                break;
                            }
                            genChest = true;
                        }
                    }
                }
                end = true;
                return;
            }
            int radius = baseRadius - y / 2;
            if (radius < 10) {
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z <= radius * radius) {
                            BlockPos pos = position.offset(x, y, z);
                            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }
                }
                y++;
                return;
            }
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + z * z <= radius * radius) {
                        BlockPos pos = position.offset(x, y, z);
                        if (y >= baseHeight - 1 || x * x + z * z > (radius - 2) * (radius - 2)) {
                            world.setBlock(pos, getBlockState(maxbaseHeight, y, rand), 3);
                            if (y < baseHeight - 5 && y > maxbaseHeight * 0.1 && rand.nextDouble() < stalagmiteChance) {
                                for (int i = 1; i <= rand.nextInt(3); i++) {
                                    BlockPos belowPos = pos.below(i);
                                    world.setBlock(belowPos, getBlockState(maxbaseHeight, y, rand), 3);
                                }
                            } else if (y < maxbaseHeight * 0.6 && y > maxbaseHeight * 0.1 && rand.nextDouble() < 0.05) {
                                world.setBlock(pos, getBlockState(maxbaseHeight, y, rand), 3);
                                for (int i = 1; i <= 3 + rand.nextInt(6); i++) {
                                    BlockPos belowPos = pos.below(i);
                                    world.setBlock(belowPos, getBlockStatePylon(maxbaseHeight, y, rand), 3);
                                }
                            } else if (y < maxbaseHeight * 0.25 && y > maxbaseHeight * 0.025 && rand.nextDouble() < lavaFlowChance) {
                                BlockPos belowPos = pos.below();
                                setBlockState1(
                                        world,
                                        belowPos,
                                        FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock(),
                                        3
                                );
                            }
                            if (rand.nextInt(1000) > 20) {
                                continue;
                            }
                            BlockPos belowPos = pos.above();
                            setBlockState1(
                                    world,
                                    belowPos,
                                    FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock(),
                                    3
                            );

                        } else {
                            if (y == 0) {
                                if (rand.nextDouble() < protrusionChance) {
                                    final int type = rand.nextInt(5);
                                    if (type == 0) {
                                        BlockPos protrusionPos = pos.above(0);
                                        setBlockState1(world, protrusionPos, getBlockDownState(y, rand), 3);
                                        blockPosList1.add(protrusionPos);
                                    } else if (type == 1) {
                                        int protrusionSize = rand.nextInt(5) + 2;
                                        for (int i = 0; i < protrusionSize; i++) {
                                            BlockPos protrusionPos = pos.above(i);
                                            blockPosList.add(protrusionPos);
                                            setBlockState1(
                                                    world,
                                                    protrusionPos,
                                                    getBlockStatePylon(maxbaseHeight, y, rand),
                                                    3
                                            );
                                        }
                                        blockPosList1.add(pos.above(protrusionSize - 1));
                                    } else if (type == 2) {
                                        for (int x1 = -1; x1 < 2; x1++) {
                                            for (int z1 = -1; z1 < 2; z1++) {
                                                BlockPos protrusionPos = pos.offset(x1, 0, z1);
                                                blockPosList.add(protrusionPos);
                                                setBlockState1(
                                                        world,
                                                        protrusionPos,
                                                        getBlockDownState(y, rand),
                                                        3
                                                );
                                                blockPosList1.add(protrusionPos);
                                            }
                                        }


                                        for (int[] offset : protrusionOffsets) {
                                            for (int yy = 0; yy < offset[1]; yy++) {
                                                BlockPos protrusionPos = pos.offset(offset[0], yy, offset[2]);
                                                blockPosList.add(protrusionPos);
                                                setBlockState1(
                                                        world,
                                                        protrusionPos,
                                                        getBlockState(maxbaseHeight, y, rand),
                                                        3
                                                );
                                            }

                                        }

                                    } else if (type == 3) {
                                        for (int x1 = -1; x1 < 2; x1++) {
                                            for (int z1 = -1; z1 < 2; z1++) {
                                                BlockPos protrusionPos = pos.offset(x1, 0, z1);
                                                blockPosList.add(protrusionPos);
                                                setBlockState1(
                                                        world,
                                                        protrusionPos,
                                                        getBlockDownState(y, rand),
                                                        3
                                                );
                                                blockPosList1.add(protrusionPos);
                                            }
                                        }
                                        int protrusionSize = rand.nextInt(5) + 2;
                                        for (int i = 0; i < protrusionSize; i++) {
                                            BlockPos protrusionPos = pos.above(i);
                                            blockPosList.add(protrusionPos);
                                            setBlockState1(
                                                    world,
                                                    protrusionPos,
                                                    getBlockStatePylon(maxbaseHeight, y, rand),
                                                    3
                                            );
                                        }
                                        protrusionSize = rand.nextInt(protrusionSize);
                                        for (int i = 0; i < protrusionSize; i++) {
                                            BlockPos protrusionPos = pos.above(i);
                                            for (int z1 = -1; z1 < 2; z1 += 2) {
                                                final BlockPos pos1 = protrusionPos.east(z1);
                                                blockPosList.add(pos1);
                                                setBlockState1(world, pos1, getBlockState(maxbaseHeight, y, rand), 32);
                                            }
                                            for (int x1 = -1; x1 < 2; x1 += 2) {
                                                final BlockPos pos1 = protrusionPos.north(x1);
                                                blockPosList.add(pos1);

                                                setBlockState1(world, pos1, getBlockState(maxbaseHeight, y, rand), 3);
                                            }
                                        }


                                    } else {
                                        for (int x1 = -1; x1 < 2; x1++) {
                                            for (int z1 = -1; z1 < 2; z1++) {
                                                BlockPos protrusionPos = pos.offset(x1, 0, z1);
                                                blockPosList.add(protrusionPos);
                                                blockPosList1.add(protrusionPos);
                                                setBlockState1(
                                                        world,
                                                        protrusionPos,
                                                        getBlockState(maxbaseHeight, y, rand),
                                                        3
                                                );
                                            }
                                        }
                                        int protrusionSize = rand.nextInt(5) + 2;
                                        for (int i = 0; i < protrusionSize; i++) {
                                            BlockPos protrusionPos = pos.above(i);
                                            blockPosList.add(protrusionPos);
                                            setBlockState1(world, protrusionPos, getBlockState(maxbaseHeight, y, rand), 3);
                                        }
                                        protrusionSize = rand.nextInt(protrusionSize);
                                        for (int i = 0; i < protrusionSize; i++) {
                                            BlockPos protrusionPos = pos.above(i);
                                            for (int z1 = -1; z1 < 2; z1 += 2) {
                                                for (int x1 = -1; x1 < 2; x1 += 2) {
                                                    BlockPos pos1 = protrusionPos.east(z1);
                                                    pos1 = pos1.north(x1);
                                                    blockPosList.add(pos1);
                                                    setBlockState1(world, pos1, getBlockState(maxbaseHeight, y, rand), 3);
                                                }
                                            }

                                        }

                                    }

                                } else {
                                    final boolean remove = blockPosList.remove(pos);
                                    if (!remove) {
                                        setBlockState1(world, pos, FluidName.fluidpahoehoe_lava.getInstance().get().getSource().defaultFluidState().createLegacyBlock(), 3);
                                    }
                                }
                            } else {
                                if (blockPosList.isEmpty() || y > 10) {

                                    setBlockState1(world, pos, Blocks.AIR.defaultBlockState(), 3);
                                } else {
                                    boolean remove = blockPosList.remove(pos);
                                    if (!remove) {
                                        setBlockState1(world, pos, Blocks.AIR.defaultBlockState(), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            y += 1;
        });
        this.thread.setPriority(1);
    }

    public void setWorld(Level world) {
        if (this.world == null) {
            this.world = world;
            this.rand = WorldBaseGen.random;
            this.end = false;
            this.chunkPos = new ChunkPos(position2);
            this.chunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.FULL, false);
            this.baseHeight = 80 + rand.nextInt(30);
            this.baseRadius = 35 + rand.nextInt(20);
            this.protrusionChance = 0.05;
            this.lavaFlowChance = 0.01;
            this.stalagmiteChance = 0.1;
            this.blockPosList = new LinkedList<>();
            this.blockPosList1 = new LinkedList<>();
            this.maxbaseHeight = 0;
            for (int y = 0; y < baseHeight; y++) {
                int radius = baseRadius - y / 2;
                if (radius < 10) {
                    maxbaseHeight = y;
                    break;
                }
            }
            if (basalts_ores == null) {
                initBasaltsOres();
            }
            final int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, position2.getX(), position2.getZ());
            BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(position2.getX(), height, position2.getZ());
            this.position = checkPos.above(maxbaseHeight / 2);
            if (position.getY() >= 40)
                position = position.below(position.getY() - 40);
            this.y = 0;
            this.end = false;
        }
    }


    private void initBasaltsOres() {
        basalts_ores = new BlockState[veinTypes.size()][];
        for (int i = 0; i < veinTypes.size(); i++) {
            VeinType type = veinTypes.get(i);
            int mineral = type.getHeavyOre() == null ? 0 : 1;
            basalts_ores[i] = new BlockState[mineral + type.getOres().size()];
            if (type.getHeavyOre() != null) {
                if (type.getHeavyOre() instanceof BlockHeavyOre) {
                    basalts_ores[i][0] = IUItem.basaltheavyore.getStateFromMeta(type.getMeta());
                } else if (type.getHeavyOre() instanceof BlockMineral) {
                    basalts_ores[i][0] = IUItem.basaltheavyore1.getStateFromMeta(type.getMeta());
                }
                for (int j = 0; j < type.getOres().size(); j++) {
                    ChanceOre chanceOre = type.getOres().get(j);
                    basalts_ores[i][1 + j] = chanceOre.getBlock();
                }
            } else {
                for (int j = 0; j < type.getOres().size(); j++) {
                    ChanceOre chanceOre = type.getOres().get(j);
                    basalts_ores[i][j] = chanceOre.getBlock();
                }
            }
        }

    }

    public BlockState getBlockDownState(int y, Random random) {

        final double chance = random.nextDouble();
        if (chance < 0.7) {
            final double chance1 = random.nextDouble();
            if (chance1 < 0.9) {
                return basalt_blocked;
            } else {
                int meta = random.nextInt(veinTypes.size());
                while (veinTypes.get(meta).getHeavyOre() == null) {
                    meta = random.nextInt(veinTypes.size());
                }
                return basalts_ores[meta][0];
            }
        } else if (chance < 0.9) {
            return basalt_cobblestone;
        } else {
            return basalt_magma;
        }
    }

    public BlockState getBlockState(int baseHeight, int y, Random random) {
        if (y < baseHeight * 0.15) {
            final double chance = random.nextDouble();
            if (chance < 0.65) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_melted;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    while (veinTypes.get(meta).getHeavyOre() == null) {
                        meta = random.nextInt(veinTypes.size());
                    }
                    return basalts_ores[meta][0];
                }
            } else if (chance < 0.9) {
                return basalt_cobblestone;
            } else {
                return basalt_magma;
            }
        } else if (y < baseHeight * 0.7) {
            final double chance = random.nextDouble();
            if (chance < 0.65) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    while (veinTypes.get(meta).getHeavyOre() == null) {
                        meta = random.nextInt(veinTypes.size());
                    }
                    return basalts_ores[meta][0];
                }
            } else if (chance < 0.8) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_spongy;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    final VeinType vein = veinTypes.get(meta);
                    int index = vein.getHeavyOre() == null ? 0 : 1;
                    final int typeOreMeta = random.nextInt(vein.getOres().size());
                    return basalts_ores[meta][index + typeOreMeta];
                }
            } else {
                return basalt_magma;
            }
        } else {
            final double chance = random.nextDouble();
            if (chance < 0.25) {
                return basalt;
            } else {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    if (chance1 < 0.45) {
                        return basalt_smooth;
                    }
                    if (chance1 <= 0.48) {
                        return basalt_sulfur_ore;
                    }
                    return Blocks.STONE.defaultBlockState();
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    final VeinType vein = veinTypes.get(meta);
                    int index = vein.getHeavyOre() == null ? 0 : 1;
                    final int typeOreMeta = random.nextInt(vein.getOres().size());
                    return basalts_ores[meta][index + typeOreMeta];
                }
            }
        }
    }

    public BlockState getBlockStatePylon(int baseHeight, int y, Random random) {
        if (y < baseHeight * 0.15) {
            final double chance = random.nextDouble();
            if (chance < 0.65) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_pylon;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    while (veinTypes.get(meta).getHeavyOre() == null) {
                        meta = random.nextInt(veinTypes.size());
                    }
                    return basalts_ores[meta][0];
                }
            } else if (chance < 0.9) {
                return basalt_magma;
            } else {
                return basalt_boron_ore;
            }
        } else if (y < baseHeight * 0.7) {
            final double chance = random.nextDouble();
            if (chance < 0.65) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_pylon;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    while (veinTypes.get(meta).getHeavyOre() == null) {
                        meta = random.nextInt(veinTypes.size());
                    }
                    return basalts_ores[meta][0];
                }
            } else if (chance < 0.8) {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_pylon;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    final VeinType vein = veinTypes.get(meta);
                    int index = vein.getHeavyOre() == null ? 0 : 1;
                    final int typeOreMeta = random.nextInt(vein.getOres().size());
                    return basalts_ores[meta][index + typeOreMeta];
                }
            } else {
                return basalt_magma;
            }
        } else {
            final double chance = random.nextDouble();
            if (chance < 0.25) {
                return basalt_sulfur_ore;
            } else {
                final double chance1 = random.nextDouble();
                if (chance1 < 0.9) {
                    return basalt_pylon;
                } else {
                    int meta = random.nextInt(veinTypes.size());
                    final VeinType vein = veinTypes.get(meta);
                    int index = vein.getHeavyOre() == null ? 0 : 1;
                    final int typeOreMeta = random.nextInt(vein.getOres().size());
                    return basalts_ores[meta][index + typeOreMeta];
                }
            }
        }
    }

    public void setBlockState1(Level level, BlockPos p_46605_, BlockState p_46606_, int p_46607_) {
        level.setBlock(p_46605_, p_46606_, p_46607_);
    }

    public void generate() {
        thread.run();
    }

    public boolean isEnd() {
        return end;
    }
}
