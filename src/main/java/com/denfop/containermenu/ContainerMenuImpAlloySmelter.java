package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityImpAlloySmelter;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuImpAlloySmelter extends ContainerMenuFullInv<BlockEntityImpAlloySmelter> {


    public ContainerMenuImpAlloySmelter(
            Player entityPlayer,
            BlockEntityImpAlloySmelter tileEntity1
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
