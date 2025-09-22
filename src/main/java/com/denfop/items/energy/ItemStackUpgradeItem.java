package com.denfop.items.energy;

import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeldUpgradeItem;
import com.denfop.gui.GuiUpgradeItem;
import com.denfop.invslot.Inventory;
import com.denfop.items.ItemStackInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemStackUpgradeItem extends ItemStackInventory {

    public final ItemStack itemStack1;


    public ItemStackUpgradeItem(EntityPlayer player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }

    public void save() {
        super.save();
    }


    public ContainerBase<ItemStackUpgradeItem> getGuiContainer(EntityPlayer player) {
        return new ContainerHeldUpgradeItem(this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiUpgradeItem(new ContainerHeldUpgradeItem(this), itemStack1);
    }


    @Override
    public void addInventorySlot(final Inventory var1) {

    }


    @Nonnull
    public String getName() {
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }


    public int getStackSizeLimit() {
        return 64;
    }


}
