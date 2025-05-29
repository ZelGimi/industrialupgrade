package com.denfop.container;


import com.denfop.IUItem;
import com.denfop.items.ItemStackUpgradeModules;
import com.denfop.items.UpgradeSlot;
import com.denfop.items.UpgradeSlot1;
import net.minecraft.world.entity.player.Player;

public class ContainerUpgrade extends ContainerHandHeldInventory<ItemStackUpgradeModules> {


    public ContainerUpgrade(ItemStackUpgradeModules Toolbox1, Player player) {
        super(Toolbox1, player);
        if (IUItem.ejectorUpgrade.is(Toolbox1.itemStack1.getItem()) || IUItem.pullingUpgrade.is(Toolbox1.itemStack1.getItem())) {
            for (int i = 0; i < 9; i++) {
                this.addSlotToContainer(new SlotVirtual(Toolbox1, i, 33 - 27 + i * 18, 123, new UpgradeSlot1(Toolbox1)));
            }
        }
        if (IUItem.fluidEjectorUpgrade.is(Toolbox1.itemStack1.getItem()) || IUItem.fluidpullingUpgrade.is(Toolbox1.itemStack1.getItem())) {
            for (int i = 0; i < 9; i++) {
                this.addSlotToContainer(new SlotVirtual(Toolbox1, i, 33 - 27 + i * 18, 123, new UpgradeSlot(Toolbox1)));
            }
        }
        this.addPlayerInventorySlots(Toolbox1.player, 174, 230);


    }


}
