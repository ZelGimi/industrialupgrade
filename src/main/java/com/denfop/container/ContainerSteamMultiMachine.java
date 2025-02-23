package com.denfop.container;

import com.denfop.tiles.base.TileSteamMultiMachine;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSteamMultiMachine extends ContainerFullInv<TileSteamMultiMachine> {

    public ContainerSteamMultiMachine(EntityPlayer entityPlayer, TileSteamMultiMachine tileEntity1, int sizeWorkingSlot) {
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

    public ContainerSteamMultiMachine(
            EntityPlayer entityPlayer,
            TileSteamMultiMachine tileEntity1,
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
