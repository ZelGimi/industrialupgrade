package com.denfop.container;

import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.items.energy.HandHeldUpgradeItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHeldUpgradeItem extends ContainerHandHeldInventory<HandHeldUpgradeItem> {


    private final ItemStack item;

    public ContainerHeldUpgradeItem(HandHeldUpgradeItem Toolbox1) {
        super(Toolbox1);
        this.item = Toolbox1.itemStack1;
    }

    @Override
    public void onContainerClosed(final EntityPlayer player) {
        super.onContainerClosed(player);
        UpgradeSystem.system.updateBlackListFromStack(this.item);
    }

}
