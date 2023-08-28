package com.denfop.container;

import com.denfop.tiles.base.TileAntiUpgradeBlock;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAntiUpgrade extends ContainerFullInv<TileAntiUpgradeBlock> {

    public ContainerAntiUpgrade(EntityPlayer entityPlayer, TileAntiUpgradeBlock tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.input,
                0, 106, 34
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    i, 96 + i * 18, 65
            ));
        }
    }


}
