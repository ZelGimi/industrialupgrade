package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntitySolidMixer;
import com.denfop.containermenu.slot.SlotInvSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuSolidMixer extends ContainerMenuFullInv<BlockEntitySolidMixer> {

    public ContainerMenuSolidMixer(Player var1, BlockEntitySolidMixer tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 105, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 131, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 30, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 56, 44));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 10 + i * 18
            ));
        }
    }

}
