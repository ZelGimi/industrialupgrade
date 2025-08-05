package com.denfop.tiles.mechanism.multimechanism.photonic;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksPhotonicMachine;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;


public class TileEntityPhoCombRecycler extends TileMultiMachine {


    public TileEntityPhoCombRecycler(BlockPos pos, BlockState state) {
        super(EnumMultiMachine.Pho_RECYCLER.usagePerTick, EnumMultiMachine.Pho_RECYCLER.lenghtOperation, BlocksPhotonicMachine.photonic_comb_recycler, pos, state);
    }
    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        this.cold.storage = 0;
    }

    public void initiate(int soundEvent) {
        if (this.getWorld().getGameTime() % 40 == 0) {
            super.initiate(soundEvent);
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlocksPhotonicMachine.photonic_comb_recycler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.pho_machine.getBlock(getTeBlock().getId());
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Pho_RECYCLER;
    }


}
