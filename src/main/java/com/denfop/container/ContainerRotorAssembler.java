package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityRotorAssembler;
import net.minecraft.world.entity.player.Player;

public class ContainerRotorAssembler extends ContainerFullInv<TileEntityRotorAssembler> {

    public ContainerRotorAssembler(
            final TileEntityRotorAssembler tileEntity1,
            Player entityPlayer
    ) {
        super(entityPlayer, tileEntity1, 255);

        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 0, 88, 11));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 1, 28, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 2, 88, 131));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 3, 148, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).inputSlotA, 4, 88, 71));
        addSlotToContainer(new SlotInvSlot((tileEntity1).outputSlot, 0, 183, 148));
    }


}
