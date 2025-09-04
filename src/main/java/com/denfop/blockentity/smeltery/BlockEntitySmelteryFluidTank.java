package com.denfop.blockentity.smeltery;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmelteryEntity;
import com.denfop.componets.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class BlockEntitySmelteryFluidTank extends BlockEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public BlockEntitySmelteryFluidTank(BlockPos pos, BlockState state) {
        super(BlockSmelteryEntity.smeltery_tank, pos, state);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTankExtract("fluids", 10000);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.fluidTank.setCanAccept(this.getMain() != null);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }


    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSmelteryEntity.smeltery_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

    @Override
    public FluidTank getTank() {
        return fluidTank;
    }

}
