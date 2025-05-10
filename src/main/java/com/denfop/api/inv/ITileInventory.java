package com.denfop.api.inv;

import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.world.entity.player.Player;

public interface ITileInventory extends IInventory {

    TileEntityInventory getParent();

    GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> menu);
}
