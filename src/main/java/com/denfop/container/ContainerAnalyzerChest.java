package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityAnalyzerChest;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerAnalyzerChest extends ContainerFullInv<TileEntityAnalyzerChest> {

    public ContainerAnalyzerChest(EntityPlayer entityPlayer, TileEntityAnalyzerChest tileEntity1) {
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


    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("outputSlot");
        return ret;
    }


}
