package com.denfop.blockentity.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlantEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import com.denfop.screen.ScreenChemicalSeparate;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityChemicalPlantSeparate extends BlockEntityMultiBlockElement implements ISeparate {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntityChemicalPlantSeparate(BlockPos pos, BlockState state) {
        super(BlockChemicalPlantEntity.chemical_plant_separate, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidhelium.getInstance().get()));
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockChemicalPlantEntity.chemical_plant_separate;
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
    public ContainerMenuDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerMenuDefaultMultiElement(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenChemicalSeparate((ContainerMenuDefaultMultiElement) var2);
    }

}
