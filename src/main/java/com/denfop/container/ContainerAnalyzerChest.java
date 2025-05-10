package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import net.minecraft.world.entity.player.Player;

public class ContainerAnalyzerChest extends ContainerFullInv<TileEntityAnalyzerChest> {

    public ContainerAnalyzerChest(Player entityPlayer, TileEntityAnalyzerChest tileEntity1) {
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
