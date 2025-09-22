package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.Fluids;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFluidTrash extends TileEntityInventory {

    private final Fluids.InternalFluidTank tank;

    public TileEntityFluidTrash() {
        final Fluids fluids = this.addComponent(new Fluids(this));
        tank = fluids.addTankInsert("tank", Integer.MAX_VALUE);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.tank.getFluidAmount() > 0) {
            this.tank.drain(this.tank.getFluidAmount(), true);
        }
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.fluid_trash;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

}
