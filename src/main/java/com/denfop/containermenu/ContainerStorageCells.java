package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityEntityStorageCells;
import net.minecraft.world.entity.player.Player;

public class ContainerStorageCells extends ContainerMenuFullInv<BlockEntityEntityStorageCells> {

    public ContainerStorageCells(BlockEntityEntityStorageCells tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 188);

        for (int i = 0; i < tileEntity1.getSlots().size(); i++)
            addSlotToContainer(new SlotInvSlot(tileEntity1.getSlots(),
                    i, 44 + (i % 5) * 18, 11 + (i / 5) * 18
            ));

    }


}
