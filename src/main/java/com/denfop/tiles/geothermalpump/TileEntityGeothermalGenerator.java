package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGeothermalgenerator;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGeothermalGenerator;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGeothermalGenerator extends TileEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityGeothermalGenerator(BlockPos pos, BlockState state) {
        super(BlockGeothermalPump.geothermal_generator,pos,state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidneft.getInstance().get()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public ContainerGeothermalgenerator getGuiContainer(final Player var1) {
        return new ContainerGeothermalgenerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGeothermalGenerator((ContainerGeothermalgenerator) menu);
    }

    @Override
    public ComponentBaseEnergy getEnergy() {
        return energy;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
