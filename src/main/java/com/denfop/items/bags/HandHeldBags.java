package com.denfop.items.bags;

import com.denfop.container.ContainerBags;
import com.denfop.gui.GuiBags;
import ic2.api.item.ElectricItem;
import ic2.core.ContainerBase;
import ic2.core.item.tool.HandHeldInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class HandHeldBags extends HandHeldInventory {

    public final int inventorySize;
    public final ItemStack itemStack1;

    public HandHeldBags(EntityPlayer player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.inventorySize = inventorySize;
        this.itemStack1 = stack;
    }

    public ContainerBase<HandHeldBags> getGuiContainer(EntityPlayer player) {
        return new ContainerBags(player, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiBags(new ContainerBags(player, this), itemStack1);
    }

    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemEnergyBags) {
            return false;
        }


        if (ElectricItem.manager.canUse(itemStack1, 50)) {
            return !itemstack.isEmpty();
        } else {
            return false;
        }
    }

}
