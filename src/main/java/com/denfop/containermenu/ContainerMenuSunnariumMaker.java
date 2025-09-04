package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntitySunnariumMaker;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class ContainerMenuSunnariumMaker extends ContainerMenuFullInv<BlockEntitySunnariumMaker> {

    public ContainerMenuSunnariumMaker(Player entityPlayer, BlockEntitySunnariumMaker tileEntity1) {
        this(entityPlayer, tileEntity1, 167, 152, 8);
    }

    public ContainerMenuSunnariumMaker(
            Player entityPlayer,
            BlockEntitySunnariumMaker tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(null, tileEntity1, height);
        this.player = entityPlayer;
        this.inventory = entityPlayer.getInventory();
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
                        entityPlayer.getInventory(),
                        col1 + col * 9 + 9,
                        xStart + col1 * 18,
                        height + -82 + col * 18
                ));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(entityPlayer.getInventory(), col, xStart + col * 18, height + -25));
        }

    }


}
