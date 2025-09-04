package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityElectricDryer;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuDryer extends ContainerMenuFullInv<BlockEntityElectricDryer> {

    public ContainerMenuDryer(Player entityPlayer, BlockEntityElectricDryer tileEntity1) {
        super(entityPlayer, tileEntity1, 206);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.output1, 0, 45, 99));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 100, 40));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.fluidSlot1, 0, 45, 79));
        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 21 + i * 18
            ));
        }

    }


}
