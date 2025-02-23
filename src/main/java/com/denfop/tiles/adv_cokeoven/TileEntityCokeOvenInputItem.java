package com.denfop.tiles.adv_cokeoven;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockAdvCokeOven;
import com.denfop.blocks.mechanism.BlockCokeOven;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TileEntityCokeOvenInputItem extends TileEntityMultiBlockElement implements IInputItem {


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        tooltip.add(Localization.translate("iu.blastfurnace.info5") + new ItemStack(IUItem.ForgeHammer).getDisplayName());

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

    public IMultiTileBlock getTeBlock() {
        return BlockAdvCokeOven.adv_coke_oven_input;
    }

    public BlockTileEntity getBlock() {
        return IUItem.adv_cokeoven;
    }

}
