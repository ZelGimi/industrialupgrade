package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityMutatron;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuMutatron extends ContainerMenuFullInv<BlockEntityMutatron> {

    public ContainerMenuMutatron(Player entityPlayer, BlockEntityMutatron tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 45 - 8, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 68 - 8, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 2, 122, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45 - 8, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot2, 0, 68 - 8, 79));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot3, 0, 122, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
