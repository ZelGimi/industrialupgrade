package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.IUItem;
import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.items.energy.ItemToolWrench;
import com.denfop.render.oilquarry.DataBlock;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMultiBlockElement extends TileEntityInventory implements IMultiElement {

    IMainMultiBlock mainMultiBlock;


    public TileEntityMultiBlockElement() {

    }
    public boolean doesSideBlockRendering(EnumFacing side) {
        return getMain() == null;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return !(getMain() != null && getMain().getMultiBlockStucture().hasUniqueModels);
    }

    @Override
    public boolean isNormalCube() {
        return  getMain() == null;
    }

    @Override
    public boolean clientNeedsExtraModelInfo() {
        return getMain() == null;
    }


    public boolean isSideSolid(EnumFacing side) {
        return getMain() == null;
    }

    public boolean shouldRenderInPass(int pass) {
        return getMain() != null;
    }
    @Override
    public IMainMultiBlock getMain() {
        return mainMultiBlock;
    }

    @Override
    public void setMainMultiElement(final IMainMultiBlock main) {
        mainMultiBlock = main;
    }


    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (player != null && hand != null && player
                .getHeldItem(hand)
                .getItem() instanceof ItemToolWrench || player != null && hand != null && player
                .getHeldItem(hand)
                .getItem() == IUItem.GraviTool) {
            return false;
        }
        if (this.getMain() != null && !this.hasOwnInventory()) {
            if (this.getMain() instanceof TileEntityInventory) {
                return ((TileEntityInventory) this.getMain()).onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                return true;
            }
        } else {
            if (this.getMain() == null && !this.hasOwnInventory())
                    return false;
            return  super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void onUnloaded() {
        if (this.getMain() != null) {
            if (this.getMain().isFull()) {
                getMain().setFull(false);
                getMain().setActivated(false);
            }
        }
        super.onUnloaded();
    }

}
