package com.denfop.items.book;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

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


    @Override
    public void addInventorySlot(final Inventory var1) {

    }

    @Override
    public int getBaseIndex(final Inventory var1) {
        return 0;
    }


    @Nonnull
    public String getName() {
        return "book";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
