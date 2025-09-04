package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityInsulator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuInsulator extends ContainerMenuFullInv<BlockEntityInsulator> {

    public ContainerMenuInsulator(Player entityPlayer, BlockEntityInsulator tileEntity1) {
        super(entityPlayer, tileEntity1, 206);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 25, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 47, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 120, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 1, 67, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 47, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 67, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
