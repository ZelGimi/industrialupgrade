package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileBQG extends TileQuantumGenerator {

    public TileBQG() {
        super(11, "bsp_gen", 10);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.bsp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
