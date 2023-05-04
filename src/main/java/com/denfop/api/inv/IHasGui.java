package com.denfop.api.inv;

import com.denfop.container.ContainerBase;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHasGui extends IInventory {

    ContainerBase<?> getGuiContainer(EntityPlayer var1);

    @SideOnly(Side.CLIENT)
    GuiScreen getGui(EntityPlayer var1, boolean var2);

    void onGuiClosed(EntityPlayer var1);

}
