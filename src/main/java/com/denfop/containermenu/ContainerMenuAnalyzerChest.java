package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityAnalyzerChest;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAnalyzerChest extends ContainerMenuFullInv<BlockEntityAnalyzerChest> {

    public ContainerMenuAnalyzerChest(Player entityPlayer, BlockEntityAnalyzerChest tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (tileEntity1.outputSlot != null) {

            for (int j = 0; j < 9; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j, 8 + 18 * j, 6
                ));
            }
            for (int j = 0; j < 9; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 9, 8 + 18 * j, 6 + 18
                ));
            }
            for (int j = 0; j < 9; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 18, 8 + 18 * j, 6 + 18 + 18
                ));
            }
            for (int j = 0; j < 9; ++j) {

                addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                        j + 27, 8 + 18 * j, 6 + 18 + 18 + 18
                ));
            }

        }
    }


}
