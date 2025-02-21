package com.denfop.container;

import com.denfop.tiles.bee.TileEntityApiary;
import com.denfop.tiles.mechanism.generator.things.fluid.TileAirCollector;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerApiary extends ContainerFullInv<TileEntityApiary> {

    public ContainerApiary(EntityPlayer entityPlayer, TileEntityApiary tileEntity1) {
        super(entityPlayer, tileEntity1, 207);


        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).frameSlot, i, 75+ i * 18, 99 ));
        }
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 0, 90, 23 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 1, 112, 23 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 2, 81, 45 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 3, 101, 45 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 4, 121, 45 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 5, 90, 67 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotProduct, 6, 112, 67 ));

        addSlotToContainer(
                new SlotInvSlot(tileEntity1.jellyCellSlot, 0, 152, 79 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotJelly, 0, 152, 99 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.foodCellSlot, 0, 8, 79 ));
        addSlotToContainer(
                new SlotInvSlot(tileEntity1.invSlotFood, 0, 8, 99 ));
    }


}

