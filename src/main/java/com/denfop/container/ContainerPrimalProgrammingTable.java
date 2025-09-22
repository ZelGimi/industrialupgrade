package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityPrimalProgrammingTable;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPrimalProgrammingTable
        extends ContainerFullInv<TileEntityPrimalProgrammingTable> {

    public ContainerPrimalProgrammingTable(EntityPlayer entityPlayer, TileEntityPrimalProgrammingTable tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerPrimalProgrammingTable(
            EntityPlayer entityPlayer,
            TileEntityPrimalProgrammingTable tileEntity1,
            int height,
            int upgradeX,
            int upgradeY
    ) {
        super(entityPlayer, tileEntity1, height);
        if ((tileEntity1).inputSlotA != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 56, 26));
        }
        if ((tileEntity1).outputSlot != null) {
            addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 135, 26));
        }

    }


}
