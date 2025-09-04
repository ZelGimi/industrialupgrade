package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityCombinerSolidMatter;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCombinerSolidMatter extends ContainerMenuFullInv<BlockEntityCombinerSolidMatter> {

    public ContainerMenuCombinerSolidMatter(Player entityPlayer, BlockEntityCombinerSolidMatter tileEntity1) {
        super(entityPlayer, tileEntity1, 166);
        for (int i = 0; i < 4; i++) {
            addSlot(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 8 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlot(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 16 + count * 18));

        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlot(new SlotInvSlot(tileEntity1.outputSlot, i, 91 + (i - (3 * count)) * 18, 16 + count * 18));

        }
    }


}
