package com.denfop.containermenu;

import com.denfop.blockentity.bee.BlockEntityApiary;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuApiary extends ContainerMenuFullInv<BlockEntityApiary> {

    public ContainerMenuApiary(Player entityPlayer, BlockEntityApiary tileEntity1) {
        super(entityPlayer, tileEntity1, 207);


        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).frameSlot, i, 75 + i * 18, 99));
        }
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 0, 90, 23));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 1, 112, 23));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 2, 81, 45));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 3, 101, 45));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 4, 121, 45));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 5, 90, 67));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 6, 112, 67));

        addSlotToContainer(
                new SlotInvSlot(tileEntity1.jellyCellSlot, 0, 152, 79));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotJelly, 0, 152, 99));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.foodCellSlot, 0, 8, 79));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotFood, 0, 8, 99));
    }


}

