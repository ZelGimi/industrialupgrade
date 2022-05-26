package com.denfop.items.book;

import ic2.core.ContainerBase;
import ic2.core.item.tool.HandHeldInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HandHeldBook extends HandHeldInventory {


    public final ItemStack itemStack1;
    private final ItemBook itemEnergyBags;

    public HandHeldBook(EntityPlayer player, ItemStack stack, ItemBook itemEnergyBags) {
        super(player, stack, 0);
        this.itemStack1 = stack;
        this.itemEnergyBags = itemEnergyBags;
    }

    public ContainerBase<HandHeldBook> getGuiContainer(EntityPlayer player) {
        return new ContainerBook(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GUIBook(player, itemStack1, new ContainerBook(player, this));
    }

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
