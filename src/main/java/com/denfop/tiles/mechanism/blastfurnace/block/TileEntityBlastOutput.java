package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastOutputItem;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import ic2.core.init.Localization;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityBlastOutput extends TileEntityMultiBlockElement implements IBlastOutputItem {


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        super.addInformation(stack, tooltip, advanced);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + Localization.translate(Ic2Items.ForgeHammer.getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }


}
