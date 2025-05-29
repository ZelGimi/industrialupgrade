package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWirelessMatterCollector;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWirelessMatterCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityWirelessMatterCollector extends TileEntityInventory {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;
    private final Energy energy;
    Map<ChunkPos, List<IMatter>> chunkPosListMap = new HashMap<>();

    public TileEntityWirelessMatterCollector(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.matter_collector,pos,state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("tank", 16384000, Fluids.fluidPredicate(FluidName.fluiduu_matter.getInstance().get()));
        this.energy = this.addComponent(Energy.asBasicSink(this, 10000, 14));
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.15));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.matter_collector;
    }

    @Override
    public ContainerWirelessMatterCollector getGuiContainer(final Player var1) {
        return new ContainerWirelessMatterCollector(this, var1);
    }

    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWirelessMatterCollector((ContainerWirelessMatterCollector) menu);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.level.getGameTime() % 200 == 0) {
            ChunkPos chunkPos = new ChunkPos(this.pos);
            Map<ChunkPos, List<IMatter>> map = TileMultiMatter.worldMatterMap.get(this.level.dimension());
            this.chunkPosListMap.clear();
            if (map != null) {
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        ChunkPos chunkPos1 = new ChunkPos(chunkPos.x + x, chunkPos.z + z);
                        final List<IMatter> list = map.get(chunkPos1);
                        if (list != null) {
                            chunkPosListMap.put(chunkPos1, list);
                        }
                    }
                }
            }
        }
        boolean active = false;
        if (this.energy.getEnergy() > 30) {
            for (Map.Entry<ChunkPos, List<IMatter>> chunkPosListEntry : chunkPosListMap.entrySet()) {
                if (this.energy.getEnergy() < 30) {
                    break;
                }
                for (IMatter matter : chunkPosListEntry.getValue()) {
                    if (this.energy.getEnergy() < 30) {
                        break;
                    }
                    int amount = matter.getMatterTank().getFluidAmount();
                    amount = Math.min(amount, this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount());
                    if (amount > 0) {
                        this.fluidTank.fill(new FluidStack(FluidName.fluiduu_matter.getInstance().get(), amount), IFluidHandler.FluidAction.EXECUTE);
                        matter.getMatterTank().drain(amount, IFluidHandler.FluidAction.EXECUTE);
                        this.energy.useEnergy(30);
                        active = true;
                    }
                }
            }
        }
        this.setActive(active);
    }

    public int getFilled(int i) {
        return (int) (i * this.getFluidTank().getFluidAmount() / (this.getFluidTank().getCapacity() * 1D));
    }

    public int getIntegerPercentage() {
        return (int) (100 * this.getFluidTank().getFluidAmount() / (this.getFluidTank().getCapacity() * 1D));
    }

    public Energy getEnergy() {
        return energy;
    }

}
