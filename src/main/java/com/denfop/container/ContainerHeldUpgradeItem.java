package com.denfop.container;

import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.items.energy.ItemStackUpgradeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerHeldUpgradeItem extends ContainerHandHeldInventory<ItemStackUpgradeItem> {


    private final ItemStack item;

    public ContainerHeldUpgradeItem(ItemStackUpgradeItem Toolbox1, Player player) {
        super(Toolbox1, player);
        this.item = Toolbox1.itemStack1;
    }


    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        UpgradeSystem.system.updateBlackListFromStack(this.item);
    }


}
