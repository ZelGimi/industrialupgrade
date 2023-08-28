package com.denfop.items;


import com.denfop.container.ContainerHandHeldInventory;

public class ContainerUpgrade extends ContainerHandHeldInventory<ItemStackUpgradeModules> {


    public ContainerUpgrade(ItemStackUpgradeModules Toolbox1) {
        super(Toolbox1);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

}
