package com.denfop.api.inv;

import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;

public interface IStackInventory extends IInventory {

    ItemStackInventory getParent();

    GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> menu);
}
