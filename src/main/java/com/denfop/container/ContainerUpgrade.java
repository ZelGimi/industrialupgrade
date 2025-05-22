package com.denfop.container;


import com.denfop.IUItem;
import com.denfop.items.ItemStackUpgradeModules;
import com.denfop.items.UpgradeSlot;
import com.denfop.items.UpgradeSlot1;

public class ContainerUpgrade extends ContainerHandHeldInventory<ItemStackUpgradeModules> {


    public ContainerUpgrade(ItemStackUpgradeModules Toolbox1) {
        super(Toolbox1);
        if (IUItem.ejectorUpgrade.isItemEqual(Toolbox1.itemStack1) || IUItem.pullingUpgrade.isItemEqual(Toolbox1.itemStack1)) {
            for (int i = 0; i < 9; i++) {
                this.addSlotToContainer(new SlotVirtual(Toolbox1, i, 33 - 27 + i * 18, 123, new UpgradeSlot1(Toolbox1)));
            }
        }
        if (IUItem.fluidEjectorUpgrade.isItemEqual(Toolbox1.itemStack1) || IUItem.fluidpullingUpgrade.isItemEqual(Toolbox1.itemStack1)) {
            for (int i = 0; i < 9; i++) {
                this.addSlotToContainer(new SlotVirtual(Toolbox1, i, 33 - 27 + i * 18, 123, new UpgradeSlot(Toolbox1)));
            }
        }
        this.addPlayerInventorySlots(Toolbox1.player, 174, 230);


    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

}
