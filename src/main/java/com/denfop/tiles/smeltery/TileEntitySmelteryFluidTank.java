package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.Fluids;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntitySmelteryFluidTank extends TileEntityMultiBlockElement implements ITank {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntitySmelteryFluidTank(BlockPos pos, BlockState state) {
        super(BlockSmeltery.smeltery_tank,pos,state);
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
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_tank;
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
