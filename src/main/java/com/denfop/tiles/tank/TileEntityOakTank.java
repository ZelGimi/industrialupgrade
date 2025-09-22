package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerTank;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityOakTank extends TileEntityLiquedTank {

    public TileEntityOakTank() {
        super(4);
        this.containerslot1.setTypeItemSlot(Inventory.TypeItemSlot.NONE);
    }

    @Override
    public ContainerTank getGuiContainer(final EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean isAdmin) {
        return null;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.oak_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

}
