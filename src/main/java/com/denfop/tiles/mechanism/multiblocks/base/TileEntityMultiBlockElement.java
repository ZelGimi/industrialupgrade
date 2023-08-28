package com.denfop.tiles.mechanism.multiblocks.base;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.api.multiblock.IMultiElement;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

public class TileEntityMultiBlockElement extends TileEntityInventory implements IMultiElement {

    IMainMultiBlock mainMultiBlock;

    public TileEntityMultiBlockElement() {

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
        if (this.getMain() != null && !this.hasOwnInventory()) {
            if (this.getMain() instanceof TileEntityInventory) {
                return ((TileEntityInventory) this.getMain()).onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }

    @Override
    public void onUnloaded() {
        if (this.getMain() != null && !this.getWorld().isRemote) {
            if (this.getMain().isFull()) {
                getMain().setFull(false);
                getMain().setActivated(false);
            }
        }
        super.onUnloaded();
    }

}
