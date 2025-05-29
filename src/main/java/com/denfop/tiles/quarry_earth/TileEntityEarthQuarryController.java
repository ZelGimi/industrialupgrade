package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerEarthController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiEarthController;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

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

    public TileEntityEarthQuarryController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.EarthQuarryMultiBlock,BlockEarthQuarry.earth_controller,pos,state);
        this.energy = this.addComponent(Energy.asBasicSink(this, 100000, 14));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");
        indexChunk = nbttagcompound.getInt("indexChunk");
        max = nbttagcompound.getInt("max");
        block_Col = nbttagcompound.getInt("block_Col");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        nbttagcompound.putInt("indexChunk", indexChunk);
        nbttagcompound.putInt("max", max);
        nbttagcompound.putInt("block_Col", block_Col);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
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
    public void updateTileServer(final Player var1, final double var2) {
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
            if (this.getWorld().getGameTime() % 10 == 0) {
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
                                level.setBlock(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(1),3);
                                this.dataPos.add(dataPos1);
                            }
                        } else if (dataPos1.state.getBlock() == Blocks.GRAVEL) {
                            if (random.nextInt(100) >= 94) {
                                level.setBlock(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(2),3);
                                this.dataPos.add(dataPos1);
                            }
                        } else if (dataPos1.state.getBlock() == Blocks.SAND) {

                            if (random.nextInt(100) >= 80) {
                                level.setBlock(dataPos1.getPos(), IUItem.ore2.getStateFromMeta(0),3);
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
    public ContainerEarthController getGuiContainer(final Player entityPlayer) {
        return new ContainerEarthController(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiEarthController((ContainerEarthController) menu);
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
        this.analyzer = (IAnalyzer) this.getWorld().getBlockEntity(pos1.get(0));
        List<BlockPos> pos2 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IEarthChest.class
                );
        for (BlockPos pos3 : pos2) {
            this.earthChestList.add((IEarthChest) this.getWorld().getBlockEntity(pos3));
        }
        this.map = this.analyzer.getChunkPoses();
        this.chunkPosList = new ArrayList<>(this.map.keySet());
        List<BlockPos> pos3 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IRigDrill.class
                );
        this.quarry = (IRigDrill) this.getWorld().getBlockEntity(pos3.get(0));
    }

    @Override
    public void usingBeforeGUI() {

    }

    public Energy getEnergy() {
        return energy;
    }

}
