package com.denfop.items.book;

import ic2.core.ContainerBase;
import ic2.core.item.tool.HandHeldInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class HandHeldBook extends HandHeldInventory {


    public final ItemStack itemStack1;

    public HandHeldBook(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public ContainerBase<HandHeldBook> getGuiContainer(EntityPlayer player) {
        return new ContainerBook(this);
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GUIBook(player, itemStack1, new ContainerBook(this));
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

}
