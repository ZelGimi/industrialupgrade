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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
    public Map<ChunkPos, List<DataPos>> map = new HashMap<>();
    public List<ChunkPos> chunkPosList = new LinkedList<>();
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
        dataPos = new LinkedList<>();
        ListTag dataPosList = nbttagcompound.getList("DataPosList", CompoundTag.TAG_COMPOUND);
        for (Tag t : dataPosList) {
            CompoundTag dpTag = (CompoundTag) t;
            dataPos.add(DataPos.load(dpTag));
        }
        ListTag nodAddedDataPosList = nbttagcompound.getList("nodAddedDataPosList", CompoundTag.TAG_COMPOUND);
        for (Tag t : nodAddedDataPosList) {
            CompoundTag dpTag = (CompoundTag) t;
            notAddedPos.add(DataPos.load(dpTag));
        }
        chunkPosList = new ArrayList<>();
        ListTag chunkList = nbttagcompound.getList("ChunkPosList", CompoundTag.TAG_COMPOUND);
        for (Tag t : chunkList) {
            CompoundTag cpTag = (CompoundTag) t;
            int x = cpTag.getInt("x");
            int z = cpTag.getInt("z");
            chunkPosList.add(new ChunkPos(x, z));
        }

        map = new HashMap<>();
        ListTag mapList = nbttagcompound.getList("ChunkDataMap", CompoundTag.TAG_COMPOUND);
        for (Tag t : mapList) {
            CompoundTag entryTag = (CompoundTag) t;
            int x = entryTag.getInt("x");
            int z = entryTag.getInt("z");
            ChunkPos chunkPos = new ChunkPos(x, z);

            ListTag dpList = entryTag.getList("data", CompoundTag.TAG_COMPOUND);
            List<DataPos> list = new ArrayList<>();
            for (Tag dpTag : dpList) {
                list.add(DataPos.load((CompoundTag) dpTag));
            }
            map.put(chunkPos, list);
        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        nbttagcompound.putInt("indexChunk", indexChunk);
        nbttagcompound.putInt("max", max);
        nbttagcompound.putInt("block_Col", block_Col);
        ListTag dataPosList = new ListTag();
        for (DataPos dp : dataPos) {
            dataPosList.add(dp.save());
        }
        nbttagcompound.put("DataPosList", dataPosList);

        ListTag nodAddedDataPosList = new ListTag();
        for (DataPos dp : notAddedPos) {
            nodAddedDataPosList.add(dp.save());
        }
        nbttagcompound.put("nodAddedDataPosList", nodAddedDataPosList);


        ListTag chunkList = new ListTag();
        for (ChunkPos cp : chunkPosList) {
            CompoundTag cpTag = new CompoundTag();
            cpTag.putInt("x", cp.x);
            cpTag.putInt("z", cp.z);
            chunkList.add(cpTag);
        }
        nbttagcompound.put("ChunkPosList", chunkList);

        ListTag mapList = new ListTag();
        for (Map.Entry<ChunkPos, List<DataPos>> entry : map.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("x", entry.getKey().x);
            entryTag.putInt("z", entry.getKey().z);

            ListTag dpList = new ListTag();
            for (DataPos dp : entry.getValue()) {
                dpList.add(dp.save());
            }
            entryTag.put("data", dpList);

            mapList.add(entryTag);
        }
        nbttagcompound.put("ChunkDataMap", mapList);
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
        }
    }

    List<DataPos> notAddedPos = new ArrayList<>();
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
                            notAddedPos.add(dataPos1);
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
                    if (!notAddedPos.isEmpty()){
                        Iterator<DataPos> iter = notAddedPos.iterator();
                        while (iter.hasNext()) {
                            DataPos dataPos1 = iter.next();
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
                            iter.remove();
                        }
                        this.block_Col = this.dataPos.size();
                        this.work = false;
                        this.indexChunk = this.max;
                    }else {
                        this.work = false;
                        this.indexChunk = this.max;
                    }
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
