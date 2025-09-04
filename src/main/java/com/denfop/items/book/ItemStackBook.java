package com.denfop.items.book;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
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

    public ContainerMenuBase<ItemStackBook> getGuiContainer(Player player) {
        return new ContainerMenuBook(this);
    }


    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenBook(player, itemStack1, (ContainerMenuBook) isAdmin);
    }


    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
