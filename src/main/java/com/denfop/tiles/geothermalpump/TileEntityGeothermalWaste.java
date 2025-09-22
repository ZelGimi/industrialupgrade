package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.container.ContainerGeothermalWaste;
import com.denfop.gui.GuiGeothermalWaste;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityGeothermalWaste extends TileEntityMultiBlockElement implements IWaste {

    private final Inventory slot;

    public TileEntityGeothermalWaste() {
        this.slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 4);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public Inventory getSlot() {
        return slot;
    }

    public ContainerGeothermalWaste getGuiContainer(final EntityPlayer var1) {
        return new ContainerGeothermalWaste(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiGeothermalWaste(this.getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_waste;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump;
    }

}
