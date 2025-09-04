package com.denfop.containermenu;

import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.items.energy.ItemStackUpgradeItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ContainerMenuHeldUpgradeItem extends ContainerMenuHandHeldInventory<ItemStackUpgradeItem> {


    private final ItemStack item;

    public ContainerMenuHeldUpgradeItem(ItemStackUpgradeItem Toolbox1, Player player) {
        super(Toolbox1, player);
        this.item = Toolbox1.itemStack1;
    }


    @Override
    public void removed(Player p_38940_) {
        super.removed(p_38940_);
        UpgradeSystem.system.updateBlackListFromStack(this.item);
    }


}
