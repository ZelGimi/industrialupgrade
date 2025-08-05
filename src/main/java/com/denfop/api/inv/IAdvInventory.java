package com.denfop.api.inv;

import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface IAdvInventory<P extends IInventory> extends Container, MenuProvider {

    P getParent();


    void addInventorySlot(InvSlot var1);

    int getBaseIndex(InvSlot var1);

    MenuType<?> getMenuType();

    int getContainerId();

    ContainerBase<?> getGuiContainer(Player var1);

    @OnlyIn(Dist.CLIENT)
    GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> var2);

}
