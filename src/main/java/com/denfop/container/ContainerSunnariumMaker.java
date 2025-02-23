package com.denfop.container;

import com.denfop.tiles.base.TileSunnariumMaker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

public class ContainerSunnariumMaker extends ContainerFullInv<TileSunnariumMaker> {

    public ContainerSunnariumMaker(EntityPlayer entityPlayer, TileSunnariumMaker tileEntity1) {
        this(entityPlayer, tileEntity1, 167, 152, 8);
    }

    public ContainerSunnariumMaker(
            EntityPlayer entityPlayer,
            TileSunnariumMaker tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(null, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    0, 32, 21
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot,
                0, -20, 84
        ));
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    1, 68, 21
            ));
        }

        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    2, 32, 43
            ));
        }
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA,
                    3, 68, 43
            ));
        }

        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot,
                    0, 110 - 7, 32
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).upgradeSlot,
                    i, upgradeX, upgradeY + 1 + i * 18
            ));
        }
        int xStart = (178 - 162) / 2;

        int col;
        for (col = 0; col < 3; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.addSlotToContainer(new Slot(
                        entityPlayer.inventory,
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.inventory, col, xStart + col * 18, height + -25));
        }

    }


}
