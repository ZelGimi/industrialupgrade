package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityImpAlloySmelter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerImpAlloySmelter extends ContainerFullInv<TileEntityImpAlloySmelter> {


    public ContainerImpAlloySmelter(
            EntityPlayer entityPlayer,
            TileEntityImpAlloySmelter tileEntity1
    ) {
        super(entityPlayer, tileEntity1);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    i, 10 + i * 18, 35
            ));
        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 8 + i * 18
            ));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot,
                0, -20, 84
        ));
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                0, 120, 35
        ));
    }


}
