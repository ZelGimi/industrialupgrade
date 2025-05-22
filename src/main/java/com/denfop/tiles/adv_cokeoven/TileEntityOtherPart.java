package com.denfop.tiles.adv_cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvCokeOven;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityOtherPart extends TileEntityMultiBlockElement implements IPart {


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName());

    }

    public IMultiTileBlock getTeBlock() {
        return BlockAdvCokeOven.adv_coke_oven_part;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_cokeoven;
    }

}
