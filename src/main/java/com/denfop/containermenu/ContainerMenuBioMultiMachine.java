package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityBioMultiMachine;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuBioMultiMachine extends ContainerMenuFullInv<BlockEntityBioMultiMachine> {

    public ContainerMenuBioMultiMachine(Player entityPlayer, BlockEntityBioMultiMachine tileEntity1, int sizeWorkingSlot) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            int xDisplayPosition1 = 80 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
            addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.inputSlots, i,
                    xDisplayPosition1, 16
            ));

        }

        if (!tileEntity1.getMachine().output) {
            for (int i = 0; i < sizeWorkingSlot; i++) {
                int xDisplayPosition1 = 80 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                        xDisplayPosition1, 60
                ));
            }
        } else {
            sizeWorkingSlot = sizeWorkingSlot + (tileEntity1.getMachine().output ? 2 : 0);
            int xDisplayPosition = 80 - 45;
            for (int i = 0; i < sizeWorkingSlot; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                        xDisplayPosition + 18 * i, 60
                ));
            }
        }
    }

    public ContainerMenuBioMultiMachine(
            Player entityPlayer,
            BlockEntityBioMultiMachine tileEntity1,
            int sizeWorkingSlot,
            boolean jei
    ) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < sizeWorkingSlot; i++) {
            int xDisplayPosition1 = 80 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
            addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.inputSlots, i,
                    xDisplayPosition1, 16
            ));

        }

        if (!tileEntity1.getMachine().output) {
            for (int i = 0; i < sizeWorkingSlot; i++) {
                int xDisplayPosition1 = 80 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                        xDisplayPosition1, 60
                ));
            }
        } else {
            sizeWorkingSlot = sizeWorkingSlot + (tileEntity1.getMachine().output ? 2 : 0);
            int xDisplayPosition = 80 - 45;
            for (int i = 0; i < sizeWorkingSlot; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                        xDisplayPosition + 18 * i, 60
                ));
            }
        }

    }

}
