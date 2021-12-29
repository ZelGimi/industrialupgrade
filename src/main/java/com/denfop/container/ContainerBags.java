//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.denfop.container;

import com.denfop.items.bags.HandHeldBags;
import ic2.core.item.ContainerHandHeldInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerBags extends ContainerHandHeldInventory<HandHeldBags> {


    public final int inventorySize;

    public ContainerBags(EntityPlayer player, HandHeldBags Toolbox1) {
        super(Toolbox1);

        inventorySize = Toolbox1.inventorySize;
        int slots = Toolbox1.inventorySize;
        slots = slots / 9;

        int col;
        for (col = 0; col < slots; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(Toolbox1, col1 + col * 9, 8 + col1 * 18, 24 + col * 18));
            }
        }

        addPlayerInventorySlots(player, 233);

    }

}
