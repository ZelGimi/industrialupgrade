package com.denfop.items.energy;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeldUpgradeItem;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiUpgradeItem;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemStackUpgradeItem extends ItemStackInventory {

    public final ItemStack itemStack1;


    public ItemStackUpgradeItem(Player player, ItemStack stack) {
        super(player, stack, 0);
        this.itemStack1 = stack;
    }


    public ContainerBase<ItemStackUpgradeItem> getGuiContainer(Player player) {
        return new ContainerHeldUpgradeItem(this, player);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player player, ContainerBase<? extends IAdvInventory> isAdmin) {
        return new GuiUpgradeItem((ContainerHeldUpgradeItem) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
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
        return "toolbox";
    }

    public boolean hasCustomName() {
        return false;
    }


    public int getStackSizeLimit() {
        return 64;
    }


}
