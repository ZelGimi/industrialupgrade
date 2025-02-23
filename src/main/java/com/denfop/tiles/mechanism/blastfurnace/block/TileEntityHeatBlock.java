package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastHeat;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityHeatBlock extends TileEntityMultiBlockElement implements IBlastHeat {


    public TileEntityHeatBlock() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.blastfurnace.info1"));
        tooltip.add(Localization.translate("iu.blastfurnace.info3") + Localization.translate(new ItemStack(
                IUItem.blastfurnace,
                1,
                0
        ).getUnlocalizedName()));
        tooltip.add(Localization.translate("iu.blastfurnace.info4"));
        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName());
        tooltip.add(Localization.translate("iu.blastfurnace.info6"));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBlastFurnace.blast_furnace_heat;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace;
    }

}
