package com.denfop.tiles.adv_cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvCokeOven;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TileEntityCokeOvenInputItem extends TileEntityMultiBlockElement implements IInputItem {
    public TileEntityCokeOvenInputItem(BlockPos pos, BlockState state) {
        super(BlockAdvCokeOven.adv_coke_oven_input, pos, state);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

    }


    public IMultiTileBlock getTeBlock() {
        return BlockAdvCokeOven.adv_coke_oven_input;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_cokeoven.getBlock(getTeBlock());
    }

}
