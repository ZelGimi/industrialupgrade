package com.denfop.container;

import com.denfop.Config;
import com.denfop.tiles.base.TileCombinerMatter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCombinerMatter extends ContainerFullInv<TileCombinerMatter> {

    public ContainerCombinerMatter(EntityPlayer entityPlayer, TileCombinerMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        if (Config.amplifierSlot) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.amplifierSlot, 0, 72, 40));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 16 + count * 18));

        }
    }


}
