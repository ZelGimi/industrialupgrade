package com.quantumgenerators;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;

public class TileGQG extends TileQuantumGenerator {

    public TileGQG() {
        super(13, "grasp_gen", 12);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockQG.grasp_gen;
    }

    public BlockTileEntity getBlock() {
        return QGCore.qg;
    }

}
