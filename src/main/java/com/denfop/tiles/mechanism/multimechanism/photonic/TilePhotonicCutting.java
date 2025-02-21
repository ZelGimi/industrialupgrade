package com.denfop.tiles.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TilePhotonicCutting extends TileMultiMachine {

    public TilePhotonicCutting() {
        super(
                EnumMultiMachine.PHO_Cutting
        );

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.storage = 0;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_cutting;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.PHO_Cutting;
    }


}
