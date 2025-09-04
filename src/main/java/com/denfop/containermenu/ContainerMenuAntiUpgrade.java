package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityAntiUpgradeBlock;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuAntiUpgrade extends ContainerMenuFullInv<BlockEntityAntiUpgradeBlock> {

    public ContainerMenuAntiUpgrade(Player entityPlayer, BlockEntityAntiUpgradeBlock tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.input,
                0, 106, 35
        ));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot,
                    i, 93 + i * 18, 63
            ));
        }
    }


}
