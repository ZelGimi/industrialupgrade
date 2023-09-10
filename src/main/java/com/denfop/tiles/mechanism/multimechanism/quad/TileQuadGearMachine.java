package com.denfop.tiles.mechanism.multimechanism.quad;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TileQuadGearMachine extends TileMultiMachine {

    public TileQuadGearMachine() {
        super(EnumMultiMachine.QUAD_Gearing.usagePerTick, EnumMultiMachine.QUAD_Gearing.lenghtOperation, 1);

    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.quadgearing;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.QUAD_Gearing;
    }

}
