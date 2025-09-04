package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityFluidSeparator;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuFluidSeparator extends ContainerMenuFullInv<BlockEntityFluidSeparator> {

    public ContainerMenuFluidSeparator(Player entityPlayer, BlockEntityFluidSeparator tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 50, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 98, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 50, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 98, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
