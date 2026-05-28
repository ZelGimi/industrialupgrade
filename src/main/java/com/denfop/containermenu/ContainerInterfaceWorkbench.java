package com.denfop.containermenu;

import com.denfop.blockentity.storage.BlockEntityInterfaceWorkbench;
import net.minecraft.world.entity.player.Player;

public class ContainerInterfaceWorkbench extends ContainerMenuFullInv<BlockEntityInterfaceWorkbench> {

    public ContainerInterfaceWorkbench(BlockEntityInterfaceWorkbench tileEntity1, Player entityPlayer) {
        super(entityPlayer, tileEntity1, 178, 220);

        for (int i = 0; i < tileEntity1.getSlots().size(); i++)
            addSlotToContainer(new SlotInvSlot(tileEntity1.getSlots(),
                    i, 8 + (i % 9) * 18, 10 + (i / 9) * 18
            ));

    }


}
