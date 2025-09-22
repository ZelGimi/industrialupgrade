package com.denfop.api.inv;

import com.denfop.container.ContainerBase;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAdvInventory<P extends TileEntityInventory> extends IInventory {



    void addInventorySlot(Inventory var1);


    ContainerBase<?> getGuiContainer(EntityPlayer var1);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(EntityPlayer var1, boolean var2);

}
