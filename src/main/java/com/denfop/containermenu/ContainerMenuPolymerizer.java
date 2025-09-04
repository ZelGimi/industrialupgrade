package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityPolymerizer;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuPolymerizer extends ContainerMenuFullInv<BlockEntityPolymerizer> {

    public ContainerMenuPolymerizer(Player entityPlayer, BlockEntityPolymerizer tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 45, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
