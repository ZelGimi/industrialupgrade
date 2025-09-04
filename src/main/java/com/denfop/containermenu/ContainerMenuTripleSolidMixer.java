package com.denfop.containermenu;

import com.denfop.blockentity.mechanism.BlockEntityTripleSolidMixer;
import net.minecraft.world.entity.player.Player;

public class ContainerMenuTripleSolidMixer extends ContainerMenuFullInv<BlockEntityTripleSolidMixer> {

    public ContainerMenuTripleSolidMixer(Player var1, BlockEntityTripleSolidMixer tileEntity1) {
        super(var1, tileEntity1);

        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 0, 108, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.outputSlot, 1, 131, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 0, 10, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 1, 36, 44));
        this.addSlotToContainer(new SlotInvSlot(tileEntity1.inputSlotA, 2, 62, 44));

        for (int i = 0; i < 4; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity1.upgradeSlot,
                    i, 152, 10 + i * 18
            ));
        }
    }

}
