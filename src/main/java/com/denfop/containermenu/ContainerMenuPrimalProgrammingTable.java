package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPrimalProgrammingTable;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPrimalProgrammingTable
        extends ContainerMenuFullInv<BlockEntityPrimalProgrammingTable> {

    public ContainerMenuPrimalProgrammingTable(Player entityPlayer, BlockEntityPrimalProgrammingTable tileEntity1) {
        this(entityPlayer, tileEntity1, 166, 152, 8);
    }

    public ContainerMenuPrimalProgrammingTable(
            Player entityPlayer,
            BlockEntityPrimalProgrammingTable tileEntity1,
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
