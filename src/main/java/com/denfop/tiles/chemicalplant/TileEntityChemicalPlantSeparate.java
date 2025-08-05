package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.gui.GuiChemicalSeparate;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityChemicalPlantSeparate extends TileEntityMultiBlockElement implements ISeparate {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityChemicalPlantSeparate(BlockPos pos, BlockState state) {
        super(BlockChemicalPlant.chemical_plant_separate, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidHelium.getInstance().get()));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_separate;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

    @Override
    public Fluids.InternalFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerDefaultMultiElement(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(final Player var1, final ContainerBase<?> var2) {
        return new GuiChemicalSeparate((ContainerDefaultMultiElement) var2);
    }

}
