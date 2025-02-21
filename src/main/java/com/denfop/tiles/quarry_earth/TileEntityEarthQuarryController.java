package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerEarthController;
import com.denfop.gui.GuiEarthController;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TileEntityEarthQuarryController extends TileMultiBlockBase implements IEarthQuarry, IUpdatableTileEvent {

    public static List<ChunkPos> chunkPos = new LinkedList<>();
    public static Random random = new Random();
    public final Energy energy;
    public List<IEarthChest> earthChestList = new ArrayList<>();
    public IAnalyzer analyzer;
    public boolean work;
    public int indexChunk = 0;
    public Map<ChunkPos, List<DataPos>> map;
    public List<ChunkPos> chunkPosList;
    public int max = 0;
    public int block_Col;
    private List<DataPos> dataPos = new LinkedList<>();
    private IRigDrill quarry;

    public TileEntityEarthQuarryController() {
        super(InitMultiBlockSystem.EarthQuarryMultiBlock);
        this.energy = this.addComponent(Energy.asBasicSink(this, 100000, 14));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
        indexChunk = nbttagcompound.getInteger("indexChunk");
        max = nbttagcompound.getInteger("max");
        block_Col = nbttagcompound.getInteger("block_Col");
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        nbttagcompound.setBoolean("work", work);
        nbttagcompound.setInteger("indexChunk", indexChunk);
        nbttagcompound.setInteger("max", max);
        nbttagcompound.setInteger("block_Col", block_Col);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_controller;
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        customPacketBuffer.writeInt(this.max);
        customPacketBuffer.writeInt(this.indexChunk);
        customPacketBuffer.writeInt(this.block_Col);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        max = customPacketBuffer.readInt();
        indexChunk = customPacketBuffer.readInt();
        block_Col = customPacketBuffer.readInt();
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0 && !this.analyzer.getChunkPoses().isEmpty()) {
            this.work = !this.work;
            indexChunk = 0;
            dataPos.clear();
            this.block_Col = 0;
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.work && this.full) {
            if (this.getWorld().provider.getWorldTime() % 10 == 0) {
                if (this.analyzer.fullAnalyzed() && !chunkPosList.isEmpty()) {
                    List<DataPos> dataPos = this.map.getOrDefault(chunkPosList.get(indexChunk), Collections.emptyList());
                    max = chunkPosList.size();
                    for (DataPos dataPos1 : dataPos) {
                        if (energy.getEnergy() < 50) {
                            continue;
                        }
                        this.energy.useEnergy(50);
                        if (dataPos1.state.getBlock() == Blocks.DIRT) {
                            if (random.nextInt(100) >= 90) {
                                world.setBlockState(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(1));
                                this.dataPos.add(dataPos1);
                            }
                        } else if (dataPos1.state.getBlock() == Blocks.GRAVEL) {
                            if (random.nextInt(100) >= 94) {
                                world.setBlockState(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(2));
                                this.dataPos.add(dataPos1);
                            }
                        } else if (dataPos1.state.getBlock() == Blocks.SAND) {

                            if (random.nextInt(100) >= 80) {
                                world.setBlockState(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(0));
                                this.dataPos.add(dataPos1);
                            }

                        }
                    }
                    this.block_Col = this.dataPos.size();
                    chunkPos.add(chunkPosList.get(indexChunk));
                    indexChunk++;
                    if (indexChunk == chunkPosList.size()) {
                        quarry.startOperation(this.dataPos);
                        chunkPosList.clear();
                        this.work = false;

                    }
                } else {
                    this.work = false;
                    this.indexChunk = this.max;
                }
            }

        }
    }

    @Override
    public ContainerEarthController getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerEarthController(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiEarthController(getGuiContainer(var1));
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.earthChestList.clear();
        }


    }

    @Override
    public void updateAfterAssembly() {

        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IAnalyzer.class
                );
        this.analyzer = (IAnalyzer) this.getWorld().getTileEntity(pos1.get(0));
        List<BlockPos> pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IEarthChest.class
                );
        for (BlockPos pos3 : pos2) {
            this.earthChestList.add((IEarthChest) this.getWorld().getTileEntity(pos3));
        }
        this.map = this.analyzer.getChunkPoses();
        this.chunkPosList = new ArrayList<>(this.map.keySet());
        List<BlockPos> pos3 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRigDrill.class
                );
        this.quarry = (IRigDrill) this.getWorld().getTileEntity(pos3.get(0));
    }

    @Override
    public void usingBeforeGUI() {

    }

    public Energy getEnergy() {
        return energy;
    }

}
