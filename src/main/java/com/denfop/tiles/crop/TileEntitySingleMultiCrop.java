package com.denfop.tiles.crop;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

public class TileEntitySingleMultiCrop extends TileEntityMultiCrop{

    public TileEntitySingleMultiCrop() {
        super(1);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.single_multi_crop;
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2D, 1.0D));

    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
