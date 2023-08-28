package com.denfop.container;

import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.items.energy.ItemStackUpgradeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHeldUpgradeItem extends ContainerHandHeldInventory<ItemStackUpgradeItem> {


    private final ItemStack item;

    public ContainerHeldUpgradeItem(ItemStackUpgradeItem Toolbox1) {
        super(Toolbox1);
        this.item = Toolbox1.itemStack1;
    }

    @Override
    public void onContainerClosed(final EntityPlayer player) {
        super.onContainerClosed(player);
        UpgradeSystem.system.updateBlackListFromStack(this.item);
    }

}
