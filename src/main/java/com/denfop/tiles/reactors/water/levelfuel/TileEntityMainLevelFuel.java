package com.denfop.tiles.reactors.water.levelfuel;

import com.denfop.container.ContainerLevelFuel;
import com.denfop.gui.GuiLevelFuel;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ILevelFuel;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityMainLevelFuel extends TileEntityMultiBlockElement implements ILevelFuel {

    public TileEntityMainLevelFuel() {

    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        if (this.getMain() != null) {
            return new GuiLevelFuel(getGuiContainer(var1));
        }
        return null;
    }

    @Override
    public ContainerLevelFuel getGuiContainer(final EntityPlayer var1) {
        if (this.getMain() != null) {
            return new ContainerLevelFuel((TileEntityMainController) this.getMain(), var1);
        }
        return null;
    }

}
