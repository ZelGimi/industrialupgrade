package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPerAlloySmelter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPerAlloySmelter extends ContainerMenuFullInv<BlockEntityPerAlloySmelter> {


    public ContainerMenuPerAlloySmelter(
            Player entityPlayer,
            BlockEntityPerAlloySmelter tileEntity1
    ) {
        super(entityPlayer, tileEntity1);
        for (int i = 0; i < 5; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA,
                    i, 5 + i * 18, 35
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
                0, 127, 35
        ));
    }


}
