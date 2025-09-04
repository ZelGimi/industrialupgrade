package com.denfop.blockentity.gaswell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasWellTank;
import com.denfop.screen.ScreenGasWellTank;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityGasWellTank extends BlockEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntityGasWellTank(BlockPos pos, BlockState state) {
        super(BlockGasWellEntity.gas_well_tank, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
    }


    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuGasWellTank getGuiContainer(final Player var1) {
        return new ContainerMenuGasWellTank(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenGasWellTank((ContainerMenuGasWellTank) menu);
    }

    @Override
    public Fluids.InternalFluidTank getTank() {
        return this.fluidTank;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasWellEntity.gas_well_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

}
