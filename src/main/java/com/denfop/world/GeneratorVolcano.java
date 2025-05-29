package com.denfop.world;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockBasalts;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.FluidName;
import com.denfop.network.packet.PacketUpdateTile;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.TileEntityVolcanoChest;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

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
    private static IBlockState basalt_smooth = IUItem.basalts.getState(BlockBasalts.Type.basalt_smooth);
    private static IBlockState basalt_sulfur_ore = IUItem.basalts.getState(BlockBasalts.Type.basalt_sulfur_ore);
    private static IBlockState basalt_pylon = IUItem.basalts.getState(BlockBasalts.Type.basalt_pylon);
    private static IBlockState basalt_magma = IUItem.basalts.getState(BlockBasalts.Type.basalt_magma);
    private static IBlockState basalt_cobblestone = IUItem.basalts.getState(BlockBasalts.Type.basalt_cobblestone);
    private static IBlockState basalt_melted = IUItem.basalts.getState(BlockBasalts.Type.basalt_melted);
    private static IBlockState basalt = IUItem.basalts.getState(BlockBasalts.Type.basalt);
    private static IBlockState basalt_boron_ore = IUItem.basalts.getState(BlockBasalts.Type.basalt_boron_ore);
    private static IBlockState basalt_spongy = IUItem.basalts.getState(BlockBasalts.Type.basalt_spongy);
    private static IBlockState basalt_blocked = IUItem.basalts.getState(BlockBasalts.Type.basalt_blocked);
    private static IBlockState[][] basalts_ores = null;
    private final World world;
    private BlockPos position;
    private final Random rand;
    private final Thread thread;
    private final LinkedList<BlockPos> blockPosList1;
    private final LinkedList<BlockPos> blockPosList;
    private final ChunkPos chunkPos;
    private final Chunk chunk;
    private final int baseHeight;
    private final int baseRadius;
    private final double protrusionChance;
    private final double lavaFlowChance;
    private final double stalagmiteChance;
    boolean genChest = false;
    private Map<ChunkPos, Chunk> chunkPosChunkMap = new HashMap<>();
    private boolean end;
    private int y;
    private int maxbaseHeight;

    public GeneratorVolcano(World world, BlockPos position1) {
        this.world = world;
        BlockPos position2 = new BlockPos(position1.getX(), 30, position1.getZ());
        this.rand = world.rand;
        this.end = false;
        this.chunkPos = new ChunkPos(position2);
        this.chunk = world.getChunkFromChunkCoords(chunkPos.x, chunkPos.z);

        this.baseHeight = 60 + rand.nextInt(30);

        this.baseRadius = 25 + rand.nextInt(20);
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
        final int height = chunk.getHeight(position2);
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos(position2.getX(), world.getHeight(), position2.getZ());
        while (checkPos.getY() > 0) {
            checkPos.add(0, -1, 0);
            IBlockState state = world.getBlockState(checkPos);
            if (!(state.getMaterial() == Material.AIR ) && !state.getMaterial().isLiquid()) {
                break;
            }
        }
        this.position = checkPos.add(0, -maxbaseHeight / 2,0);
        if (position.getY() > 60)
            position = position.add(0,-(position.getY()-60),0);
        this.y = 0;
        this.end = false;
        this.thread = new Thread() {
            @Override
            public void run() {
                if (y == baseHeight) {
                    while (!genChest) {
                        int index = rand.nextInt(blockPosList1.size());
                        if (rand.nextDouble() >= 0.95) {
                            BlockPos pos = blockPosList1.get(index);
                            while (true) {
                                if (world.getBlockState(pos).getMaterial() != Material.AIR) {
                                    pos = pos.up();
                                }
                                generateChest(pos);
                                break;
                            }
                            genChest = true;
                        } else {
                            BlockPos pos = blockPosList1.remove(index);
                            if (blockPosList1.isEmpty()) {
                                while (true) {
                                    if (world.getBlockState(pos).getMaterial() != Material.AIR) {
                                        pos = pos.up();
                                    }
                                    generateChest(pos);
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
                                BlockPos pos = position.add(x, y, z);
                                setBlockState1(world, pos, Blocks.AIR.getDefaultState(), 3);
                            }
                        }
                    }
                    y++;
                    return;
                }
                for (int x = -radius; x <= radius; x++) {
                    for (int z = -radius; z <= radius; z++) {
                        if (x * x + z * z <= radius * radius) {
                            BlockPos pos = position.add(x, y, z);
                            if (y >= baseHeight - 1 || x * x + z * z > (radius - 2) * (radius - 2)) {
                                world.setBlockState(pos, getBlockState(maxbaseHeight, y, rand), 3);
                                if (y < baseHeight - 5 && y > maxbaseHeight * 0.1 && rand.nextDouble() < stalagmiteChance) {
                                    for (int i = 1; i <= rand.nextInt(3); i++) {
                                        BlockPos belowPos = pos.down(i);
                                        setBlockState1(world, belowPos, getBlockState(maxbaseHeight, y, rand), 3);
                                    }
                                } else if (y < maxbaseHeight * 0.6 && y > maxbaseHeight * 0.1 && rand.nextDouble() < 0.05) {
                                    world.setBlockState(pos, getBlockState(maxbaseHeight, y, rand), 3);
                                    for (int i = 1; i <= 3 + rand.nextInt(6); i++) {
                                        BlockPos belowPos = pos.down(i);
                                        setBlockState1(world, belowPos, getBlockStatePylon(maxbaseHeight, y, rand), 3);
                                    }
                                } else if (y < maxbaseHeight * 0.25 && y > maxbaseHeight * 0.025 && rand.nextDouble() < lavaFlowChance) {
                                    BlockPos belowPos = pos.down();
                                    setBlockState1(
                                            world,
                                            belowPos,
                                            FluidName.fluidpahoehoe_lava.getInstance().getBlock().getDefaultState(),
                                            3
                                    );
                                }
                                if (rand.nextInt(1000) > 20) {
                                    continue;
                                }
                                BlockPos belowPos = pos.up();
                                setBlockState1(
                                        world,
                                        belowPos,
                                        FluidName.fluidpahoehoe_lava.getInstance().getBlock().getDefaultState(),
                                        3
                                );

                            } else {
                                if (y == 0) {
                                    if (rand.nextDouble() < protrusionChance) {
                                        final int type = rand.nextInt(5);
                                        if (type == 0) {
                                            BlockPos protrusionPos = pos.up(0);
                                            setBlockState1(world, protrusionPos, getBlockDownState(y, rand), 3);
                                            blockPosList1.add(protrusionPos);
                                        } else if (type == 1) {
                                            int protrusionSize = rand.nextInt(5) + 2;
                                            for (int i = 0; i < protrusionSize; i++) {
                                                BlockPos protrusionPos = pos.up(i);
                                                blockPosList.add(protrusionPos);
                                                setBlockState1(
                                                        world,
                                                        protrusionPos,
                                                        getBlockStatePylon(maxbaseHeight, y, rand),
                                                        3
                                                );
                                            }
                                            blockPosList1.add(pos.up(protrusionSize - 1));
                                        } else if (type == 2) {
                                            for (int x1 = -1; x1 < 2; x1++) {
                                                for (int z1 = -1; z1 < 2; z1++) {
                                                    BlockPos protrusionPos = pos.add(x1, 0, z1);
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
                                                    BlockPos protrusionPos = pos.add(offset[0], yy, offset[2]);
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
                                                    BlockPos protrusionPos = pos.add(x1, 0, z1);
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
                                                BlockPos protrusionPos = pos.up(i);
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
                                                BlockPos protrusionPos = pos.up(i);
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
                                                    BlockPos protrusionPos = pos.add(x1, 0, z1);
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
                                                BlockPos protrusionPos = pos.up(i);
                                                blockPosList.add(protrusionPos);
                                                setBlockState1(world, protrusionPos, getBlockState(maxbaseHeight, y, rand), 3);
                                            }
                                            protrusionSize = rand.nextInt(protrusionSize);
                                            for (int i = 0; i < protrusionSize; i++) {
                                                BlockPos protrusionPos = pos.up(i);
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
                                            setBlockState1(world, pos, Blocks.FLOWING_LAVA.getDefaultState(), 3);
                                        }
                                    }
                                } else {
                                    if (blockPosList.isEmpty() || y > 10) {

                                        setBlockState1(world, pos, Blocks.AIR.getDefaultState(), 3);
                                    } else {
                                        boolean remove = blockPosList.remove(pos);
                                        if (!remove) {
                                            setBlockState1(world, pos, Blocks.AIR.getDefaultState(), 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                y += 1;
            }

        };
        this.thread.setPriority(1);
    }

    public IBlockState setBlockState(Chunk chunk, BlockPos pos, IBlockState state) {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        int l = k << 4 | i;

        if (j >= chunk.precipitationHeightMap[l] - 1) {
            chunk.precipitationHeightMap[l] = -999;
        }

        int i1 = chunk.heightMap[l];
        IBlockState iblockstate = chunk.getBlockState(pos);

        if (iblockstate == state) {
            return null;
        } else {
            Block block = state.getBlock();
            Block block1 = iblockstate.getBlock();
            int k1 = iblockstate.getLightOpacity(
                    this.world,
                    pos
            );
            ExtendedBlockStorage extendedblockstorage = chunk.storageArrays[j >> 4];
            boolean flag = false;

            if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE) {
                if (block == Blocks.AIR) {
                    return null;
                }

                extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.world.provider.hasSkyLight());
                chunk.storageArrays[j >> 4] = extendedblockstorage;
                flag = j >= i1;
            }

            extendedblockstorage.set(i, j & 15, k, state);

            {
                if (!this.world.isRemote) {
                    if (block1 != block) //Only fire block breaks when the block changes.
                    {
                        block1.breakBlock(this.world, pos, iblockstate);
                    }
                    TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.world, pos, iblockstate, state)) {
                        this.world.removeTileEntity(pos);
                    }
                } else if (block1.hasTileEntity(iblockstate)) {
                    TileEntity te = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.world, pos, iblockstate, state)) {
                        this.world.removeTileEntity(pos);
                    }
                }
            }

            if (extendedblockstorage.get(i, j & 15, k).getBlock() != block) {
                return null;
            } else {


                if (!this.world.isRemote && block1 != block && (!this.world.captureBlockSnapshots || block.hasTileEntity(state))) {
                    block.onBlockAdded(this.world, pos, state);
                }

                if (block.hasTileEntity(state)) {
                    TileEntity tileentity1 = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity1 == null) {
                        tileentity1 = block.createTileEntity(this.world, state);
                        this.world.setTileEntity(pos, tileentity1);
                    }

                    if (tileentity1 != null) {
                        tileentity1.updateContainingBlockInfo();
                    }
                }

                chunk.markDirty();
                return iblockstate;
            }
        }
    }

    public boolean setBlockState1(World world, BlockPos pos, IBlockState newState, int flags) {
        if (world.isOutsideBuildHeight(pos)) {
            return false;
        } else if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        } else {
            ChunkPos chunkPos = new ChunkPos(pos);
            Chunk chunk = chunkPosChunkMap.get(chunkPos);
            if (chunk == null) {
                chunk = world.getChunkFromBlockCoords(pos);
                chunkPosChunkMap.put(chunkPos, chunk);
            }


            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            net.minecraftforge.common.util.BlockSnapshot blockSnapshot = null;
            if (world.captureBlockSnapshots && !world.isRemote) {
                blockSnapshot = net.minecraftforge.common.util.BlockSnapshot.getBlockSnapshot(world, pos, flags);
                world.capturedBlockSnapshots.add(blockSnapshot);
            }
            IBlockState iblockstate = this.setBlockState(chunk, pos, newState);

            if (iblockstate == null) {
                if (blockSnapshot != null) {
                    world.capturedBlockSnapshots.remove(blockSnapshot);
                }
                return false;
            } else {


                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    world.markAndNotifyBlock(pos, chunk, iblockstate, newState, flags);
                }
                return true;
            }
        }
    }

    private void initBasaltsOres() {
        basalts_ores = new IBlockState[veinTypes.size()][];
        for (int i = 0; i < veinTypes.size(); i++) {
            VeinType type = veinTypes.get(i);
            int mineral = type.getHeavyOre() == null ? 0 : 1;
            basalts_ores[i] = new IBlockState[mineral + type.getOres().size()];
            if (type.getHeavyOre() != null) {
                if (type.getHeavyOre() instanceof BlockHeavyOre) {
                    basalts_ores[i][0] = IUItem.basaltheavyore.getStateMeta(type.getMeta());
                } else if (type.getHeavyOre() instanceof BlockMineral) {
                    basalts_ores[i][0] = IUItem.basaltheavyore1.getStateMeta(type.getMeta());
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


    public void generateChest(BlockPos pos) {

        IBlockState oldState = world.getBlockState(pos);
        IBlockState newState = IUItem.invalid.getDefaultState();
        IMultiTileBlock teBlock = IUItem.volcanoChest.item.getTeBlock(new ItemStack(IUItem.volcanoChest));
        Class<? extends TileEntityBlock> teClass = teBlock.getTeClass();
        TileEntityVolcanoChest te = (TileEntityVolcanoChest) TileEntityBlock.instantiate(teClass);
        if (world.setBlockState(pos, newState, 0)) {
            world.setTileEntity(pos, te);
            te.onPlaced(ItemStack.EMPTY, null, EnumFacing.NORTH);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), oldState, te.getBlockState(), 3);
            if (!world.isRemote) {
                new PacketUpdateTile(te);
            }

        }
        return;
    }

    public IBlockState getBlockDownState(int y, Random random) {

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

    public IBlockState getBlockState(int baseHeight, int y, Random random) {
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
                    return Blocks.STONE.getDefaultState();
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

    public IBlockState getBlockStatePylon(int baseHeight, int y, Random random) {
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

    public void generate() {
        thread.run();
    }

    public boolean isEnd() {
        return end;
    }

}
