package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityImpOilRefiner;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuImpOilRefiner extends ContainerMenuFullInv<BlockEntityImpOilRefiner> {

    public ContainerMenuImpOilRefiner(Player entityPlayer, BlockEntityImpOilRefiner tileEntity1) {
        super(entityPlayer, tileEntity1, 202);
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 39, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 62, 95));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 110, 95));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 87, 21));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 134, 21));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(
                    new SlotInvSlot((tileEntity1).upgradeSlot, i, 152, 42 + i * 18));
        }

    }


}
