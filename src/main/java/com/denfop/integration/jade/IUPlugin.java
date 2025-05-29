package com.denfop.integration.jade;

import com.denfop.blocks.BlockDeposits;
import com.denfop.blocks.BlockDeposits1;
import com.denfop.blocks.BlockDeposits2;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.tiles.base.TileEntityBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class IUPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BlockComponentProvider.INSTANCE, TileEntityBlock.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BlockComponentProvider.INSTANCE, BlockTileEntity.class);
        registration.registerBlockComponent(DepositsComponentProvider.INSTANCE, BlockDeposits.class);
        registration.registerBlockComponent(DepositsComponentProvider.INSTANCE, BlockDeposits1.class);
        registration.registerBlockComponent(DepositsComponentProvider.INSTANCE, BlockDeposits2.class);
    }

}
