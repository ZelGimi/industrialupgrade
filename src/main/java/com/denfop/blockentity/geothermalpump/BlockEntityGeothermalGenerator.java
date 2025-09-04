package com.denfop.blockentity.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockGeothermalPumpEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGeothermalgenerator;
import com.denfop.screen.ScreenGeothermalGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityGeothermalGenerator extends BlockEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;
    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntityGeothermalGenerator(BlockPos pos, BlockState state) {
        super(BlockGeothermalPumpEntity.geothermal_generator, pos, state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidpetroleum.getInstance().get()));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public ContainerMenuGeothermalgenerator getGuiContainer(final Player var1) {
        return new ContainerMenuGeothermalgenerator(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGeothermalGenerator((ContainerMenuGeothermalgenerator) menu);
    }

    @Override
    public ComponentBaseEnergy getEnergy() {
        return energy;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGeothermalPumpEntity.geothermal_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
