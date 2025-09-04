package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityMagnet;
import com.denfop.containermenu.slot.SlotInvSlot;
import com.denfop.containermenu.slot.SlotVirtual;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMagnet extends ContainerMenuFullInv<BlockEntityMagnet> {

    public ContainerMenuMagnet(Player entityPlayer, BlockEntityMagnet tileEntity1) {
        super(entityPlayer, tileEntity1, 186);
        for (int j = 0; j < 9; ++j) {
            addSlotToContainer(new SlotVirtual(tileEntity1, j, 175, 18 + j * 18,
                    tileEntity1.slot
            ));
        }
        for (int j = 9; j < 18; ++j) {
            addSlotToContainer(new SlotVirtual(tileEntity1, j, 202, 18 + (j - 9) * 18,
                    tileEntity1.slot
            ));
        }
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 30 + 18 * j, 6
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 6, 30 + 18 * j, 6 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 12, 30 + 18 * j, 6 + 18 + 18
                ));
            }
            for (int j = 0; j < 6; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 18, 30 + 18 * j, 6 + 18 + 18 + 18
                ));
            }
        }
    }


}
