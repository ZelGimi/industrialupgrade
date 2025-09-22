package com.denfop.tiles.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;

public class TilePhotonicExtractor extends TileMultiMachine {

    public TilePhotonicExtractor() {
        super(
                EnumMultiMachine.Pho_EXTRACTOR
        );

    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.storage = 0;
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_extractor;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Pho_EXTRACTOR;
    }


}
