package com.denfop.items.energy;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuHeldUpgradeItem;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenUpgradeItem;
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


    public ContainerMenuBase<ItemStackUpgradeItem> getGuiContainer(Player player) {
        return new ContainerMenuHeldUpgradeItem(this, player);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player player, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenUpgradeItem((ContainerMenuHeldUpgradeItem) isAdmin, itemStack1);
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
