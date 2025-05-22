package com.denfop.container;

import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.tiles.mechanism.multimechanism.IFarmer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerMultiMachine extends ContainerFullInv<TileMultiMachine> {

    public ContainerMultiMachine(EntityPlayer entityPlayer, TileMultiMachine tileEntity1, int sizeWorkingSlot) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1 instanceof IFarmer) {
            if (tileEntity1.getMachine().sizeWorkingSlot != 8) {
                addSlotToContainer(new SlotInvSlot(((IFarmer) tileEntity1).getFertilizerSlot(), 0,
                        178, 50 + 15
                ));
            } else {
                addSlotToContainer(new SlotInvSlot(((IFarmer) tileEntity1).getFertilizerSlot(), 0,
                        178 + 20, 84 + 30
                ));
            }
        }
        if (tileEntity1.getMachine().sizeWorkingSlot != 8) {
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
            for (int i = 0; i < 4; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.upgradeSlot, i, 152, 8 + i * 18));
            }
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot, 0, 178, 84 + 10));

        } else {
            for (int i = 0; i < sizeWorkingSlot; i++) {
                int xDisplayPosition1 = 115 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.inputSlots, i,
                        xDisplayPosition1, 16
                ));

            }

            if (!tileEntity1.getMachine().output) {
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    int xDisplayPosition1 = 115 + (32 - sizeWorkingSlot) * i - sizeWorkingSlot * 10;
                    addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                            xDisplayPosition1, 60
                    ));
                }
            } else {
                sizeWorkingSlot = sizeWorkingSlot + (tileEntity1.getMachine().output ? 2 : 0);
                int xDisplayPosition = 83 - 45;
                for (int i = 0; i < sizeWorkingSlot; i++) {
                    addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.outputSlot, i,
                            xDisplayPosition + 18 * i, 60
                    ));
                }
            }
            for (int i = 0; i < 4; i++) {
                addSlotToContainer(new SlotInvSlot(tileEntity1.multi_process.upgradeSlot, i, 178, 84 + i * 18));
            }
            this.addSlotToContainer(new SlotInvSlot(tileEntity1.input_slot, 0, 178 + 20, 84 + 10));

        }
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.dischargeSlot, 0, 8, 63));
    }

    public ContainerMultiMachine(EntityPlayer entityPlayer, TileMultiMachine tileEntity1, int sizeWorkingSlot, boolean jei) {
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
