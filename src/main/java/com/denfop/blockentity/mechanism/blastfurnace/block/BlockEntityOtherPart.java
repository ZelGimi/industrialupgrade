package com.denfop.blockentity.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.blastfurnace.api.IOtherBlastPart;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnaceEntity;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class BlockEntityOtherPart extends BlockEntityMultiBlockElement implements IOtherBlastPart {


    public BlockEntityOtherPart(BlockPos pos, BlockState state) {
        super(BlockBlastFurnaceEntity.blast_furnace_part, pos, state);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace.getItem(0)
        ).getDescriptionId()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer.getItem()).getDisplayName().getString());
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBlastFurnaceEntity.blast_furnace_part;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace.getBlock(getTeBlock().getId());
    }

}
