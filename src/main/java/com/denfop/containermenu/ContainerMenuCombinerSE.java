package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityCombinerSEGenerators;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuCombinerSE extends ContainerMenuFullInv<BlockEntityCombinerSEGenerators> {

    public ContainerMenuCombinerSE(Player entityPlayer, BlockEntityCombinerSEGenerators tileEntity1) {
        super(entityPlayer, tileEntity1, 202);
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot, i, 152, 28 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlot, i, 12 + (i - (3 * count)) * 18, 36 + count * 18));

        }
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.input, i, 70, 28 + i * 18));
        }
        for (int i = 0; i < 9; i++) {
            int count = i / 3;
            addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, i, 91 + (i - (3 * count)) * 18, 36 + count * 18));

        }
    }


}
