package com.denfop.items.book;

import com.denfop.container.ContainerBase;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemStackBook extends ItemStackInventory {


    public final ItemStack itemStack1;

    public ItemStackBook(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public ContainerBase<ItemStackBook> getGuiContainer(EntityPlayer player) {
        return new ContainerBook(this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GUIBook(player, itemStack1, new ContainerBook(this));
    }

    @Override
    public TileEntityInventory getParent() {
        return null;
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
