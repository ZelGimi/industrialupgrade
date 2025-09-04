package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityCombinerMatter;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCombinerMatter extends ContainerMenuFullInv<BlockEntityCombinerMatter> {

    public ContainerMenuCombinerMatter(Player entityPlayer, BlockEntityCombinerMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        addSlotToContainer(new SlotInvSlot(tileEntity1.amplifierSlot, 0, 72, 40));

        addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 125, 59));
        addSlotToContainer(new SlotInvSlot(tileEntity1.containerslot, 0, 125, 23));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 22 + count * 18));

        }
    }


}
