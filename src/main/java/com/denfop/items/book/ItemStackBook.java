package com.denfop.items.book;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import com.denfop.tiles.base.TileEntityInventory;
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

    public ContainerBase<ItemStackBook> getGuiContainer(Player player) {
        return new ContainerBook(this);
    }


    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player,  ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GUIBook(player, itemStack1,(ContainerBook)isAdmin);
    }


    @Override
    public void addInventorySlot(final InvSlot var1) {

    }

    @Override
    public int getBaseIndex(final InvSlot var1) {
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
