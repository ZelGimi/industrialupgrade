package com.denfop.tiles.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarry;
import com.denfop.container.ContainerEarthChest;
import com.denfop.gui.GuiEarthChest;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChest extends TileEntityMultiBlockElement implements IEarthChest {

    private final Inventory slot;

    public TileEntityChest() {
        this.slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 9);
    }

    @Override
    public Inventory getSlot() {
        return slot;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockEarthQuarry.earth_chest;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerEarthChest getGuiContainer(final EntityPlayer var1) {
        return new ContainerEarthChest(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiEarthChest(getGuiContainer(var1));
    }

}
