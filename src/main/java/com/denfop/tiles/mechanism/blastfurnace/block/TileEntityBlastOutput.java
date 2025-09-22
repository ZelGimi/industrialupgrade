package com.denfop.tiles.mechanism.blastfurnace.block;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import com.denfop.tiles.mechanism.blastfurnace.api.IBlastOutputItem;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TileEntityBlastOutput extends TileEntityMultiBlockElement implements IBlastOutputItem {


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
        return BlockBlastFurnace.blast_furnace_output;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blastfurnace;
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        if (this.getMain() != null) {
            return ((TileMultiBlockBase) this.getMain()).hasCapability(capability, facing);
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@NotNull final Capability<T> capability, final EnumFacing facing) {
        if (this.getMain() != null) {
            return ((TileMultiBlockBase) this.getMain()).getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

}
