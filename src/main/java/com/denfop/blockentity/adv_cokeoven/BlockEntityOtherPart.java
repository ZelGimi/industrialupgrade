package com.denfop.blockentity.adv_cokeoven;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvCokeOvenEntity;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockEntityOtherPart extends BlockEntityMultiBlockElement implements IPart {


    public BlockEntityOtherPart(BlockPos pos, BlockState state) {
        super(BlockAdvCokeOvenEntity.adv_coke_oven_part, pos, state);
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());

    }

    public MultiBlockEntity getTeBlock() {
        return BlockAdvCokeOvenEntity.adv_coke_oven_part;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_cokeoven.getBlock(getTeBlock());
    }

}
