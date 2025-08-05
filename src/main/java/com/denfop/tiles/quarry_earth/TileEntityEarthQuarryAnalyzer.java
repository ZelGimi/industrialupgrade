package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEarthAnalyzer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityEarthQuarryAnalyzer extends TileEntityMultiBlockElement implements IAnalyzer, IUpdatableTileEvent {

    public int blockCol;
    public int blockOres;
    Map<ChunkPos, List<DataPos>> chunkPosListHashMap = new HashMap<>();
    int x = -1;
    int z = -1;
    private boolean analyzer;
    private boolean fullAnalyzer;
    private ChunkPos chunkPos;

    public TileEntityEarthQuarryAnalyzer(BlockPos pos, BlockState state) {
        super(BlockEarthQuarry.earth_analyzer, pos, state);
    }


    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_analyzer;
    }

    @Override
    public ContainerEarthAnalyzer getGuiContainer(final Player var1) {
        return new ContainerEarthAnalyzer(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiEarthAnalyzer((ContainerEarthAnalyzer) menu);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(analyzer);
        customPacketBuffer.writeBoolean(fullAnalyzer);
        customPacketBuffer.writeInt(blockCol);
        customPacketBuffer.writeInt(blockOres);
        return customPacketBuffer;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        analyzer = customPacketBuffer.readBoolean();
        fullAnalyzer = customPacketBuffer.readBoolean();
        blockCol = customPacketBuffer.readInt();
        blockOres = customPacketBuffer.readInt();
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        nbt.putBoolean("analyzer", this.analyzer);
        nbt.putBoolean("fullAnalyzer", this.fullAnalyzer);
        nbt.putInt("blockCol", this.blockCol);
        nbt.putInt("blockOres", this.blockOres);
        ListTag mapList = new ListTag();

        for (Map.Entry<ChunkPos, List<DataPos>> entry : chunkPosListHashMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            ChunkPos pos = entry.getKey();
            entryTag.putInt("x", pos.x);
            entryTag.putInt("z", pos.z);

            ListTag dataList = new ListTag();
            for (DataPos dp : entry.getValue()) {
                dataList.add(dp.save());
            }
            entryTag.put("data", dataList);

            mapList.add(entryTag);
        }

        nbt.put("ChunkPosListHashMap", mapList);

        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.analyzer = nbtTagCompound.getBoolean("analyzer");
        this.fullAnalyzer = nbtTagCompound.getBoolean("fullAnalyzer");
        this.blockCol = nbtTagCompound.getInt("blockCol");
        this.blockOres = nbtTagCompound.getInt("blockOres");
        chunkPosListHashMap = new HashMap<>();

        ListTag mapList = nbtTagCompound.getList("ChunkPosListHashMap", CompoundTag.TAG_COMPOUND);
        for (Tag tagElement : mapList) {
            CompoundTag entryTag = (CompoundTag) tagElement;
            int x = entryTag.getInt("x");
            int z = entryTag.getInt("z");
            ChunkPos pos = new ChunkPos(x, z);

            ListTag dataList = entryTag.getList("data", CompoundTag.TAG_COMPOUND);
            List<DataPos> dataPosList = new ArrayList<>();
            for (Tag dpTag : dataList) {
                dataPosList.add(DataPos.load((CompoundTag) dpTag));
            }

            chunkPosListHashMap.put(pos, dataPosList);
        }

    }
    @Override
    public void onLoaded() {
        super.onLoaded();
        this.chunkPos = this.level.getChunkAt(this.pos).getPos();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().getGameTime() % 10 == 0) {
            if (analyzer && !fullAnalyzer && this.getMain() != null) {
                if (z == 2) {
                    if (!this.chunkPosListHashMap.isEmpty()) {
                        fullAnalyzer = true;
                        TileEntityEarthQuarryController controller = (TileEntityEarthQuarryController) this.getMain();
                        controller.map = this.chunkPosListHashMap;
                        controller.chunkPosList = new ArrayList<>(this.chunkPosListHashMap.keySet());
                    }
                    analyzer = false;
                }
                TileEntityEarthQuarryController controller = (TileEntityEarthQuarryController) this.getMain();
                final LevelChunk chunk1 = this.level.getChunk(this.chunkPos.x + x, this.chunkPos.z + z);
                if (!TileEntityEarthQuarryController.chunkPos.contains(chunk1.getPos())) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int height = chunk1.getHeight(Heightmap.Types.WORLD_SURFACE,
                                    (chunk1.getPos().x * 16 + x) & 15,
                                    (chunk1.getPos().z * 16 + z) & 15
                            );
                            for (int y = 0; y < 10; y++) {
                                if (controller.getEnergy().getEnergy() > 20) {
                                    controller.getEnergy().useEnergy(20);
                                    BlockPos pos = new BlockPos(
                                            chunk1.getPos().x * 16 + x,
                                            height - y,
                                            chunk1.getPos().z * 16 + z
                                    );
                                    this.blockCol++;
                                    BlockState state = level.getBlockState(pos);
                                    if (state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRAVEL || state.getBlock() == Blocks.SAND) {
                                        List<DataPos> dataPos = this.chunkPosListHashMap.getOrDefault(
                                                chunk1.getPos(),
                                                new ArrayList<>()
                                        );

                                        if (dataPos.isEmpty()) {
                                            dataPos.add(new DataPos(pos, state));
                                            chunkPosListHashMap.put(chunk1.getPos(), dataPos);
                                        } else {
                                            dataPos.add(new DataPos(pos, state));
                                        }
                                        this.blockOres = 0;
                                        for (List<DataPos> dataPos1 : chunkPosListHashMap.values()) {
                                            this.blockOres += dataPos1.size();
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (x < 1) {
                        x++;
                    } else {
                        z++;
                        x = -1;
                    }
                } else {
                    if (x < 1) {
                        x++;
                    } else {
                        z++;
                        x = -1;
                    }
                }
            } else {
                analyzer = false;
            }
        }
    }

    @Override
    public boolean isAnalyzed() {
        return analyzer;
    }

    @Override
    public boolean fullAnalyzed() {
        return fullAnalyzer;
    }

    @Override
    public Map<ChunkPos, List<DataPos>> getChunkPoses() {
        return this.chunkPosListHashMap;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0 && this.getMain() != null) {
            if (!this.analyzer) {
                this.analyzer = true;
                x = -1;
                z = -1;
                this.chunkPosListHashMap.clear();
                this.blockCol = 0;
                blockOres = 0;
            }
        }
    }

}
