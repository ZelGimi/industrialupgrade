package com.denfop.blockentity.mechanism.steamboiler;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSteamBoilerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySteamCasingBoiler extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntitySteamCasingBoiler(BlockPos pos, BlockState state) {
        super(BlockSteamBoilerEntity.steam_boiler_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSteamBoilerEntity.steam_boiler_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.steam_boiler.getBlock(getTeBlock());
    }

}
