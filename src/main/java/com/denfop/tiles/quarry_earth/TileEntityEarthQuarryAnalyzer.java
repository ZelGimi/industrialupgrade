package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.gui.GuiEarthAnalyzer;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public TileEntityEarthQuarryAnalyzer() {

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_analyzer;
    }

    @Override
    public ContainerEarthAnalyzer getGuiContainer(final EntityPlayer var1) {
        return new ContainerEarthAnalyzer(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiEarthAnalyzer(getGuiContainer(var1));
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
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        nbt.setBoolean("analyzer", this.analyzer);
        nbt.setBoolean("fullAnalyzer", this.fullAnalyzer);
        nbt.setInteger("blockCol", this.blockCol);
        nbt.setInteger("blockOres", this.blockOres);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.analyzer = nbtTagCompound.getBoolean("analyzer");
        this.fullAnalyzer = nbtTagCompound.getBoolean("fullAnalyzer");
        this.blockCol = nbtTagCompound.getInteger("blockCol");
        this.blockOres = nbtTagCompound.getInteger("blockOres");
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.chunkPos = this.world.getChunkFromBlockCoords(this.pos).getPos();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getWorld().provider.getWorldTime() % 10 == 0) {
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
                final Chunk chunk1 = this.world.getChunkFromChunkCoords(this.chunkPos.x + x, this.chunkPos.z + z);
                if (!TileEntityEarthQuarryController.chunkPos.contains(chunk1.getPos())) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            int height = chunk1.getHeightValue(
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
                                    IBlockState state = world.getBlockState(pos);
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
    public void updateTileServer(final EntityPlayer var1, final double var2) {
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
