package com.denfop.items.book;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemStackBook extends ItemStackInventory {


    public final ItemStack itemStack1;

    public ItemStackBook(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public ContainerBase<ItemStackBook> getGuiContainer(Player player) {
        return new ContainerBook(this);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GUIBook(player, itemStack1, (ContainerBook) isAdmin);
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
